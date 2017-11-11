package com.lxy.web;

import com.lxy.dao.StudentDao;
import com.lxy.model.PageBean;
import com.lxy.model.Student;
import com.lxy.util.DbUtil;
import com.lxy.util.JsonUtil;
import com.lxy.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class StudentListServlet extends HttpServlet{
    StudentDao StudentDao =new StudentDao();
    DbUtil dbUtil=new DbUtil();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page=req.getParameter("page");
        String rows=req.getParameter("rows");
//        String stuName=req.getParameter("studentName");
//        if (stuName==null){
//            stuName="";
//        }
//        Student student=new Student();
//        student.setStuName(stuName);
        //学生信息查询的时候多了六个参数,又要多谢六个参数
        String stuNo=req.getParameter("stuNo");
        String stuName=req.getParameter("stuName");
        String sex=req.getParameter("sex");
        String bbirthday=req.getParameter("bbirthday");
        String ebirthday=req.getParameter("ebirthday");
        String gradeId=req.getParameter("gradeId");

        Student student=new Student();
        if (stuNo!=null&&stuNo!=""){
            student.setStuNo(stuNo);
        }
        if (stuName!=null&&stuName!=""){
            student.setStuName(stuName);
        }
        if (sex!=null&&sex!=null){
            student.setSex(sex);
        }
        if (gradeId!=null&&gradeId!=""){
            //如果没有这个搜索条件的话,默认的student是-1的gradeId
            student.setGradeId(Integer.parseInt(gradeId));
        }

        PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        Connection connection=null;

        try {
            connection=dbUtil.getCon();
            JSONObject result=new JSONObject();
            JSONArray jsonArray= JsonUtil.formatRsToJsonArray(StudentDao.studentList(connection,student,bbirthday,ebirthday,pageBean));
            int total=StudentDao.studentCount(connection,student,bbirthday,ebirthday);
            result.put("rows",jsonArray);
            result.put("total",total);
            ResponseUtil.write(resp,result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
