package com.lxy.web;

import com.lxy.dao.GradeDao;
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

public class GradeComboListServlet extends HttpServlet {

    Connection connection=null;
    DbUtil dbUtil=new DbUtil();
    GradeDao gradeDao=new GradeDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            connection= dbUtil.getCon();
            //这个servlet是用于取到所有的gradeId给Combobox的
            //这一次搜索从数据库取result的时候不需要传grade对象和pageBean用于分页了
            //返回的时候,只需要返回一个jsonArray就可以了,不需要返回jsonObject了,jsonObject是给datagrid的

            //先添加一个jsonObject进去,显示"请选择..."的提示
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id","");
            jsonObject.put("gradeName","请选择.....");
            //创建一个jsonArray然后,把刚才的jsonObject先add进去
            JSONArray jsonArray=new JSONArray();
            jsonArray.add(jsonObject);
            jsonArray.addAll(JsonUtil.formatRsToJsonArray(gradeDao.getGradeResultSet(connection,null,null)));
            ResponseUtil.write(resp,jsonArray);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
