package com.lxy.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

public class JsonUtil {
    /*1，JSONObject
      json对象，就是一个键对应一个值，使用的是大括号{ }，如：{key1:value1,key2,value2...}
      2，JSONArray
      json数组，使用中括号[ ],只不过数组里面的项也是json键值对格式的
     */
    public static JSONArray formatRsToJsonArray(ResultSet rs) throws SQLException {
        //rsmd是一个存储了resultset的一些元信息的东西,通过它,我们可以获得resultset的列数,也就是数据库中表单的列数
        ResultSetMetaData rsmd = rs.getMetaData();
        //获取列数
        int colNum = rsmd.getColumnCount();
        //定义返回数组jsonArray
        JSONArray jsonArray = new JSONArray();
        //逐行遍历数据库表单的每一行,再在每一行中遍历每个列
        while (rs.next()) {
            JSONObject mapOfColValues = new JSONObject();
            //列数的序号是从1开始的而不是从0开始的
            for (int i = 1; i <= colNum; i++) {
                //key是对应的列数的名称,值是对应列数的值,分别用meta和rs得到
                //这里在rs.getObject的时候,会从数据库传回来的数据集中拿到date,date在塞进json键值对的时候会报错
                //拿到对象
                Object o = rs.getObject(i);
                //从后台拿到的从数据库里面传过来的是date类型,需要把date类型的数据转换为字符串,塞到jsonArray里面去再给前台显示
                //判断是否是date类型的对象
                if (o instanceof Date) {
                    //如果是就转换为string
                    mapOfColValues.put(rsmd.getColumnName(i), DateUtil.formatDateToString((Date) o, "yyyy-MM-dd"));
                } else {
                    mapOfColValues.put(rsmd.getColumnName(i), o);
                }
            }
            jsonArray.add(mapOfColValues);
        }
        return jsonArray;
    }
}
