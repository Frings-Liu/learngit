package com.lxy.web;

import com.lxy.dao.StudentDao;
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

public class StudentDeleteServlet extends HttpServlet {
    DbUtil dbUtil=new DbUtil();
    StudentDao studentDao=new StudentDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String delIds=req.getParameter("delIds");
        Connection connection=null;
        JSONObject result=new JSONObject();
        try {
            connection= dbUtil.getCon();
            int delNums;
            delNums=studentDao.studentDelete(connection,delIds);
            //判断是否成功删除数据
            if (delNums>0){
                result.put("ifsuccess","true");
                result.put("delNums",delNums);
            }else {
                result.put("errorMsg","删除失败");
            }
            ResponseUtil.write(resp,result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
