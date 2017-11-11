package com.lxy.web;

import com.lxy.dao.StudentDao;
import com.lxy.model.Student;
import com.lxy.util.DateUtil;
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
import java.text.ParseException;

public class StudentSaveServlet extends HttpServlet {
    Student student = null;
    DbUtil dbUtil = new DbUtil();
    StudentDao studentDao = new StudentDao();
    Connection connection = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //tomcat容器自身的默认编码不是utf-8,所以需要在这里改一下编码,否则保存的数据会出现乱码问题
        //在要使用中文的时候就需要考虑这个问题
        req.setCharacterEncoding("utf-8");
        String stuNo = req.getParameter("stuNo");
        String stuName = req.getParameter("stuName");
        String sex = req.getParameter("sex");
        String birthday = req.getParameter("birthday");
        String gradeId = req.getParameter("gradeId");
        String email = req.getParameter("email");
        String stuDesc = req.getParameter("stuDesc");
        String stuId = req.getParameter("stuId");

        int saveNums;
        JSONObject result=new JSONObject();
        try {
            student = new Student(Integer.parseInt(stuId), stuName, sex, DateUtil.formatStringToDate(birthday, "yyyy-MM-dd"), Integer.parseInt(gradeId), stuDesc, email);
            //判断stuId是否为空
            if (stuId != null && stuId != "") {
                student.setStuId(Integer.parseInt(stuId));
                connection = dbUtil.getCon();
            }
            if (stuId != null) {
                //有stuId做修改
                saveNums=studentDao.studentModify(connection,student);
            } else {
                //没有stuId做添加
                saveNums=studentDao.studentAdd(connection,student);
            }

            //有saveNums代表操作成功,没有则代表失败
            if (saveNums>0){
                //保存成功
                result.put("success","true");
            }else {
                //说明保存失败
                //这里的true指的不是保存成功了,而是表单提交给后台成功了,不写true,前台不会执行,后台返回消息后的回调函数
                //也就是说表单会认为提交失败,也不会执行提交成功之后应该干的操作了
                result.put("success","true");
                result.put("errorMsg","true");
            }
            ResponseUtil.write(resp,result);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
