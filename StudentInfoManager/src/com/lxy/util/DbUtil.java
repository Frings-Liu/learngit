package com.lxy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//JDBC编程.
//可以通过这个工具包连接数据库
public class DbUtil {
    private String dbUrl="jdbc:mysql://localhost:3306/db_studentInfo";
    private String dbUserName="root";
    private String dbPassword="123456";
    private String jdbcName="com.mysql.jdbc.Driver";

    //获取connection的方法
    public Connection getCon() throws ClassNotFoundException, SQLException {
        //首先调用forname方法
        /**
         * 类初始化概念
         类被加载之后，jvm已经获得了一个描述类结构的Class实例。
         但是还需要进行类初始化操作之后才能正常使用此类，类初始化操作就是执行一遍类的静态语句，
         包括静态变量的声明还有静态代码块。

         Class.forName方法
         此方法含义是：加载参数指定的类，并且初始化它。

         在jdbc连接数据库中的应用
         到这里，不用解释，读者也会明白，在使用jdbc方式连接数据库时，
         为什么要执行Class.forName('驱动类名')方法了：将驱动类的class文件装载到内存中，
         并且形成一个描述此驱动类结构的Class类实例，并且初始化此驱动类，这样jvm就可以使用它了，
         这就是Class.forName()方法的含义。
         */
        Class.forName(jdbcName);
        //创建一个connection,通过drivermanager获取
        Connection con= DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
        //返回con
        return con;
    }

    /**
     * 关闭connection的方法
     * @param connection 要关闭的connection
     */
    public void closeCon (Connection connection) throws SQLException {
        //判断con是否为空,不是空的就关闭
        if (connection!=null){
            connection.close();
        }
    }
    public static void main(String[] arg){
        //创建DButil
        DbUtil dbUtil=new DbUtil();
        try {
            //这个主函数应该是做测试功能用的
            dbUtil.getCon();
            System.out.println("数据库连接成功!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
