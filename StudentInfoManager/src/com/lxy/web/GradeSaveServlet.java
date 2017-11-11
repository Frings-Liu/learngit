package com.lxy.web;


import com.lxy.dao.GradeDao;
import com.lxy.model.Grade;
import com.lxy.util.DbUtil;
import com.lxy.util.ResponseUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//用于处理添加和更新功能的都写在一个servlet里面
public class GradeSaveServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        //tomcat容器自身的默认编码不是utf-8,所以需要在这里改一下编码,否则保存的数据会出现乱码问题
        req.setCharacterEncoding("utf-8");

        DbUtil dbUtil=new DbUtil();
        String gradeName=req.getParameter("gradeName");
        String gradeDesc=req.getParameter("gradeDesc");
        String id=req.getParameter("id");
        Grade grade =new Grade(gradeName,gradeDesc);
        GradeDao gradeDao=new GradeDao();
        Connection connection=null;
        //如果id不是空就给grade的id赋值
        if(id!=null){
            grade.setId(Integer.parseInt(id));
        }

        try {
            connection= dbUtil.getCon();
            JSONObject result=new JSONObject();
            int saveNums=0;
            //判断是做添加还是修改
           if (id!=null){
               //有id做修改
               saveNums= gradeDao.gradeModify(connection,grade);
           }else {
               //没id做添加
               saveNums=gradeDao.gradeAdd(connection,grade);
           }
            if(saveNums>0){
                //说明保存成功
                result.put("success","true");
            }else {
                //说明保存失败
                //这里的true指的不是保存成功了,而是表单提交给后台成功了,不写true,前台不会执行,后台返回消息后的回调函数
                //也就是说表单会认为提交失败,也不会执行提交成功之后应该干的操作了
                result.put("success","true");
                //塞一个错误信息
                result.put("errorMsg","操作失败");
            }
            ResponseUtil.write(resp,result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
