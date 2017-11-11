package com.lxy.dao;

import com.lxy.model.PageBean;
import com.lxy.model.Student;
import com.lxy.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//现在加上了搜索查询,有可能需要很多参数(附加很多条件),所以把student传进来,把出生日期范围传进来
public class StudentDao {
    public ResultSet studentList(Connection connection, Student student,String bbirthday,String ebirthday, PageBean pageBean) throws SQLException {
        //下面是关联查询时所需要的的语句
        //这个做的是内连接查询,可以把两张表按照条件连接起来,返回需要的连接好的东西,这里返回了*,但是*里面有两个id
        //所以最好把学生的id给改一下改成stuId,前台没有显示连接起来的所有数据,是因为我没有选择这个东西显示出来
        //我只选择了学生表的东西,只想把学生表的东西显示出来
        StringBuffer sb = new StringBuffer("select * from t_student s,t_grade g where s.gradeId=g.id");
        //判断所有的查询条件是否存在

        //有%的代表的是模糊查询
        if (student.getStuNo()!=null&&student.getStuNo()!=""){
            sb.append(" and s.stuNo like '%"+student.getStuNo()+"%'");
        }
        if (student.getStuName()!=null&&student.getStuName()!=""){
            sb.append(" and s.stuName like '%"+student.getStuName()+"%'");
        }
        //sex不需要模糊查询
        if (student.getSex()!=null&&student.getSex()!=""){
            sb.append(" and s.sex ='"+student.getSex()+"'");
        }
        //sql采用todays的实现方式,来针对日期,把表中的数据todays,且要求todays后大于我给的日期最低范围取值todays的结果
        if (bbirthday!=null&&bbirthday!=""){
            sb.append(" and TO_DAYS(s.birthday)>=TO_DAYS('"+bbirthday+"')");
        }
        if (ebirthday!=null&&ebirthday!=""){
            sb.append(" and TO_DAYS(s.birthday)<=TO_DAYS('"+ebirthday+"')");
        }
        //student创建的时候默认id为-1,如果有搜索条件是班级id的话肯定就不是-1了
        if (student.getGradeId()!=-1){
            sb.append(" and s.gradeId like '%"+student.getGradeId()+"%'");
        }
        if (pageBean != null) {
            sb.append(" limit " + pageBean.getStart() + "," + pageBean.getRows());
        }
        //这个里面由于是连接查询,所以and变where是多余的,因为连接的时候已经写了where条件要求内外键相等了
        PreparedStatement pstmt = connection.prepareStatement(sb.toString());
        return pstmt.executeQuery();

    }

    //既然上面都加了条件的参数,那么count肯定也是需要加条件参数的
    public int studentCount(Connection connection, Student student,String bbirthday,String ebirthday) throws SQLException {
        StringBuffer sb = new StringBuffer("select count(*) as total from t_student s,t_grade g where s.gradeId=g.id");
        //判断所有的查询条件是否存在

        //有%的代表的是模糊查询
        if (student.getStuNo()!=null&&student.getStuNo()!=""){
            sb.append(" and s.stuNo like '%"+student.getStuNo()+"%'");
        }
        if (student.getStuName()!=null&&student.getStuName()!=""){
            sb.append(" and s.stuName like '%"+student.getStuName()+"%'");
        }
        //sex不需要模糊查询
        if (student.getSex()!=null&&student.getSex()!=""){
            sb.append(" and s.sex ='"+student.getSex()+"'");
        }
        //sql采用todays的实现方式,来针对日期,把表中的数据todays,且要求todays后大于我给的日期最低范围取值todays的结果
        //在对日期的处理中,前台传给后台日期的字符串,查询语句写的也是日期的字符串,但是在查询的时候会转换为date因为查询语句里面有个TO_DAYS
        if (bbirthday!=null&&bbirthday!=""){
            sb.append(" and TO_DAYS(s.birthday)>=TO_DAYS('"+bbirthday+"')");
        }
        if (ebirthday!=null&&ebirthday!=""){
            sb.append(" and TO_DAYS(s.birthday)<=TO_DAYS('"+ebirthday+"')");
        }
        //student创建的时候默认id为-1,如果有搜索条件是班级id的话肯定就不是-1了
        if (student.getGradeId()!=-1){
            sb.append(" and s.gradeId like '%"+student.getGradeId()+"%'");
        }
        //这个里面由于是连接查询,所以and变where是多余的,因为连接的时候已经写了where条件要求内外键相等了
        PreparedStatement pstmt = connection.prepareStatement(sb.toString());
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("total");
        } else {
            return 0;
        }
    }
    public int  studentDelete(Connection connection,String delIds) throws SQLException {
        String sql="delete from t_student where stuId in ("+delIds+")";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        return pstmt.executeUpdate();
    }
    public int  studentAdd(Connection connection,Student student) throws SQLException {
        String sql="insert into t_student values(null,?,?,?,?,?,?,?)";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1,student.getStuNo());
        pstmt.setString(2,student.getStuName());
        pstmt.setString(3,student.getSex());
        //从前台传过来一个date,我们把date转换为string,然后再放到sql数据库查询语句里面去
        pstmt.setString(4, DateUtil.formatDateToString(student.getBirthday(),"yyyy-MM-dd"));
        pstmt.setInt(5,student.getGradeId());
        pstmt.setString(6,student.getEmail());
        pstmt.setString(7,student.getStuDesc());
        return pstmt.executeUpdate();
    }
    public int studentModify(Connection connection,Student student) throws SQLException {
        String sql="update t_student set stuNo=?,stuName=?,sex=?,birthday=?,gradeId=?,email=?,stuDesc=?,where stuId=?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1,student.getStuNo());
        pstmt.setString(2,student.getStuName());
        pstmt.setString(3,student.getSex());
        //从student传过来一个date,我们把date转换为string,然后再放到sql数据库查询语句里面去
        pstmt.setString(4, DateUtil.formatDateToString(student.getBirthday(),"yyyy-MM-dd"));
        pstmt.setInt(5,student.getGradeId());
        pstmt.setString(6,student.getEmail());
        pstmt.setString(7,student.getStuDesc());
        //再加一个参数stuId
        pstmt.setInt(8,student.getStuId());
        return pstmt.executeUpdate();
    }
}