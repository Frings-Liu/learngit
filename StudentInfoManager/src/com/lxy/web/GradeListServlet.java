package com.lxy.web;

import com.lxy.dao.GradeDao;
import com.lxy.model.Grade;
import com.lxy.model.PageBean;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeListServlet extends HttpServlet {
    //先获得DbUtil以便后面连接数据库
    DbUtil dbUtil=new DbUtil();
    //获得GradeDao以进行获取Grade数据操作
    GradeDao gradeDao=new GradeDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page=req.getParameter("page");
        String rows=req.getParameter("rows");
        //为了能搜索特定的年级名称的年级信息所做的准备工作,这里拿到搜索框里面的搜的信息
        String gradeName=req.getParameter("gradeName");
        //如果是空的会传一个null给gradeName
        if (gradeName==null){
            gradeName="";
        }
        Grade grade=new Grade();
        grade.setGradeName(gradeName);
        System.out.println(page);
        //pageBean每一页的那个标准的Bean类,tabs里面的表格的每一页
        PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        //使用DbUtil连接数据库获得连接connection
        Connection connection=null;
        try {
            //最终返回给easyui的table一个jsonObject,这里先创建好
            connection=dbUtil.getCon();
            JSONObject result=new JSONObject();
            //拿到gradeResultSet
           ResultSet gradeResultSet =gradeDao.getGradeResultSet(connection,grade,pageBean);
           //通过自己封装的jsonUtil将它解析为JsonArray并拿到
            JSONArray gradeJsonArray= JsonUtil.formatRsToJsonArray(gradeResultSet);
            //拿到数据总数
            int total=gradeDao.gradeCount(connection,grade);
            //返回的JsonObject需要两个键值对
            //rows,代表了获取的所有的那些行的数据
            result.put("rows",gradeJsonArray);
            //total,代表了那些行数据的个数,即行数
            result.put("total",total);
            ResponseUtil.write(resp,result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
