package com.lxy.web;

import com.lxy.dao.GradeDao;
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

public class GradeDeleteServlet  extends HttpServlet{
    //获取DbUtil来获取connection
    DbUtil dbUtil=new DbUtil();
    //用gradeDao来执行删除工作
    GradeDao gradeDao=new GradeDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String delIds=req.getParameter("delIds");
        Connection con=null;

        try {
            //获取连接
            con=dbUtil.getCon();
            //创建jsonObject一会儿传进去键值对输出
            JSONObject result=new JSONObject();
            //获取删除操作执行后删除的个数
            //这里的Ids就是前台对拿到的选取的数据的集合的里面的每个数据的id的字符串拼装的,每个id隔了一个,
            //可以参考前台的
            int delNums=gradeDao.gradeDelete(con,delIds);
            //判断是否删除了元素
            if(delNums>0){
                //这里对应了前台的function(result)里面的result的success
                result.put("ifsuccess","true");
                //塞键值对信息
                result.put("delNums",delNums);
            }else {
                result.put("errorMsg","删除失败");
            }

            //输出键值对信息
            ResponseUtil.write(resp,result);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
