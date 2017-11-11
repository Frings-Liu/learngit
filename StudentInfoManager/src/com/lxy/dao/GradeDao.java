package com.lxy.dao;

import com.lxy.model.Grade;
import com.lxy.model.PageBean;
import com.lxy.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//用于从数据库中读取grade数据并返回显示到grade的tabs中的表格
//包含了普通的查询和高级的搜索查询


public class GradeDao {
    public ResultSet getGradeResultSet(Connection con, Grade grade, PageBean pageBean) throws SQLException {
        StringBuffer sb = new StringBuffer("select * from t_grade");
        //为了实现查找特定年级的信息的功能,这里做个判断看grade是否有gradeName
        //有可能我为了读取数据给combobox传了个grade为null进来
        if (grade!=null&&StringUtil.isNotEmpty(grade.getGradeName())){
            //如果是就需要附加上新的查询条件
            sb.append(" and gradeName like '%"+grade.getGradeName()+"%'");
        }
        //判断pageBean是否为空,如果不是空,就拿到limit限制条件中的start和读取的rows数量,拼成一个字符串
        if (pageBean != null) {
            sb.append(" limit " + pageBean.getStart() + "," + pageBean.getRows());
        }
        //准备好询问语句
        //如果有字符串中有and,也就是说要查询特定名称的年级信息,需要把and替换成 where
        PreparedStatement preparedStatement = con.prepareStatement(sb.toString().replace("and","where"));
        return preparedStatement.executeQuery();
    }
    //写一个方法获取grade的信息总数
    public int gradeCount(Connection con,Grade grade) throws SQLException {
        //查询的时候考虑两种情况
        //如果不是特定名称的查询,计算所有的数据的值为total
        //如果是特定名称的查询,计算那个名字的的数据的值weitotal
        StringBuffer sqlStatement=new StringBuffer("select count(*) as total from t_grade");
        //这里判断grade的Name是否为空,如果不是空,就说明是要查询特定名称的数据
        if (StringUtil.isNotEmpty(grade.getGradeName())){
            sqlStatement.append(" and gradeName like '%"+grade.getGradeName()+"%'");
        }
        PreparedStatement pstmt=con.prepareStatement(sqlStatement.toString().replaceFirst("and","where"));
        ResultSet rs=pstmt.executeQuery();
        if (rs.next()){
            //这里rs里面只有一个东西,叫做total,是columnLabel
            return rs.getInt("total");
        }else {
           return 0;
        }
    }
    //用于对grade信息的删除操作
    public int gradeDelete(Connection connection,String delIds) throws SQLException {
        //sql的方便的删除批量数据的语句
        //delete from t_grade where field in (1,3,5)里面的field可以是id之类的
        String sql="delete from t_grade where id in("+delIds+")";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        //返回的是删除的条数是多少
        return pstmt.executeUpdate();
    }


    //做添加功能和更新功能的都写在一个servlet里面,因为相似度很高
    public int gradeAdd(Connection connection,Grade grade) throws SQLException {
        //创建sql语句
        String sql="INSERT INTO t_grade VALUES (NULL ,?,?)";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1,grade.getGradeName());
        pstmt.setString(2,grade.getGradeDesc());
        //返回执行成功的记录的条数
        return pstmt.executeUpdate();
    }
    public int  gradeModify(Connection connection,Grade grade)throws SQLException{
        //创建sql语句
        String sql="update t_grade set gradeName=?,gradeDesc=? where id=?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1,grade.getGradeName());
        pstmt.setString(2,grade.getGradeDesc());
        pstmt.setInt(3,grade.getId());
        return pstmt.executeUpdate();
    }
}
