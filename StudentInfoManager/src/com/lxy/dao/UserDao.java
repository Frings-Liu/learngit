package com.lxy.dao;

import com.lxy.model.User;
import java.sql.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public User Login(Connection con,User user) throws SQLException {
        //用于返回结果用户
        //返回后好做一个检验判断是否存在这么一个用户
        User resultUser=null;
        //sql注入语句
        String sql="select * from t_user where userName=? and password=?";
        //PreparedStatement用于使查询语句准备好,可以使用这条语句将上述查询语句的?补齐
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1,user.getUserName());
        pstmt.setString(2,user.getPassword());
        ResultSet rs=pstmt.executeQuery();
        //用resultset接收查询的结果
        //一开始标号在第一行前面
        //所以需要next一下
        //查询只可能收到一个结果
        if(rs.next()){
            resultUser=new User();
            resultUser.setPassword(rs.getString("password"));
            resultUser.setUserName(rs.getString("userName"));
        }
        //如果没查询到的话,就会返回一个空的用户
        return resultUser;
    }
}
