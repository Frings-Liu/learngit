package com.lxy.web;

import com.lxy.dao.UserDao;
import com.lxy.model.User;
import com.lxy.util.DbUtil;
import com.lxy.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//创建一个servlet类
//继承自httpservlet
//一般实现doget dopost方法
//这些方法都是对应了index里面的表单的也就是套了表单的input的登录按钮的
public class LoginServlet extends HttpServlet {
    DbUtil dbUtil=new DbUtil();
    UserDao userDao=new UserDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //两个方法都采用dopost方法
        this.doPost(req,resp);
    }

    //dopost和get提交方式的不同,原理不同,dopost更加安全合理
    //具体原理可以百度,一般采用都post提交
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从前台获取数据
        //通过jsp里面的input的name属性获得相应的数据
        String userName=req.getParameter("userName");
        String password=req.getParameter("password");
        req.setAttribute("userName",userName);
        req.setAttribute("password",password);
        //做一个空字符串的判断
        if(StringUtil.isEmpty(userName)||StringUtil.isEmpty(password)){
            //做一个服务器端跳转
            //给request加一个信息
            req.setAttribute("error","用户名或密码为空!");
            //跳转到index.jsp
            //跳转的信息在index上通过jsp $显示
            req.getRequestDispatcher("index.jsp").forward(req,resp);
            return;
        }
        System.out.println(userName);
        System.out.println(password);
        //接下来实现登录功能
        //首先需要在前面引出DbUtil 和 UserDao 一个用于实现数据库连接,另外一个用于实现用户登录功能
        User user=new User(userName,password);
        Connection con= null;
        try {
            con = dbUtil.getCon();
            User currentUser=userDao.Login(con,user);
            //这里拿到返回的user做一个是否为空的判断,如果是空的,就说明用户名和密码不能在数据库中找到配对
            //即用户名或密码错误
            if (currentUser==null){
                //做一个服务器端跳转,输出提示消息
                req.setAttribute("error","用户名或密码错误");
                //这句话....以后要删了测试一下有什么不同,不是很能理解有什么作用
                //被用来从这个Servlet向其它服务器资源传递请求。当一个Servlet对响应作了初步的处理，
                // 并要求其它的对象对此作出响应时，可以使用这个方法。
                //应该是把这个req传给下一个需要处理它的对象,跳转到下一个需要处理它的对象
                //Dispatcher请求转发的意思。。。直接把客户端的请求在服务器处理以后跳转到下一个页面或者是处理类。。。
                // 此时的地址栏上的URL是不会变化的。。。。Redirect是重定向的意思。。。
                // 客户端的请求到达服务器处理以后，让客户端的页面链接重新定到另一个页面。。。
                // 此时地址栏的URL和你请求以前是不同的。
                req.getRequestDispatcher("index.jsp").forward(req,resp);
            }else {//如果登录成功做一个客户端跳转
                //登陆成功就要拿到session
                //用session做权限验证,如果没登录,不允许跳转到main.jsp
                //塞进去一个属性,判断的时候拿出来判断有没有
                HttpSession session=req.getSession();
                session.setAttribute("currentUser",currentUser);
                resp.sendRedirect("main.jsp");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {//最终要关闭和数据库的连接
            try {
                dbUtil.closeCon(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
