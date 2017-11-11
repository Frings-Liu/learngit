<%--
  Created by IntelliJ IDEA.
  User: Frings
  Date: 2017/10/12
  Time: 19:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生信息管理系统</title>
    <!--下面是一个js脚本,用于重置输入框里面的值,id定义在input里面的属性中-->
    <script>
        function resetValue() {
            document.getElementById("userName").value = "";
            document.getElementById("password").value = "";
        }
    </script>
</head>
<body>
<div align="center" style="padding-top: 50px">
    <!--添加一個表格,因為servlet的dopost方法裡面的getparameter是要用表格的-->
    <!--需要在里面改一下action和method,action和xml里面配置的url-pattern一样-->
    <%--action是指数据要提交到那个地址去,可以是jsp,也可以是servlet,而url-pattern就是servlet的地址--%>
    <!--login对应了xml里面的servlet map的login 映射到对应的servlet,映射到对应的class文件,再对应到java文件里面的dopost代码-->
    <form action="login" method="post">
        <table align="center" background="images/login.jpg" width="740" height="500">
            <tr height="180">

            </tr>
            <tr height="10">
                <td width="40%"></td>
                <td width="10%">用户名</td>
                <!-- value是显示的默认的值    name 属性规定 input 元素的名称 -->
                <td><input type="text" value="${userName}" name="userName" id="userName"/></td>
                <td width="30%"></td>
            </tr>
            <tr height="10">
                <td width="40%"></td>
                <td width="10%">密码</td>
                <!-- value是显示的默认的值    name 属性规定 input 元素的名称 -->
                <%--关于EL表达式--%>
                <%--EL存取变量数据的方法很简单，例如：${username}。它的意思是取出某一范围中名称为username的变量。--%>
                <%--因为我们并没有指定哪一个范围的username，所以它会依序从Page、Request、Session、Application范围查找。--%>
                <td><input type="password" value="${password}" name="password" id="password"/></td>
                <td width="30%"></td>
            </tr>

            <tr height="10">
                <td width="40%"></td>
                <td width="10%"><input type="submit" value="登录"/></td>
                <!-- 通过添加onclick事件(通过js)实现重置功能-->
                <td><input type="button" value="重置" onclick="resetValue()"/></td>
                <td width="30%"></td>
            </tr>

            <tr height="10">
                <td width="40%"></td>
                <td colspan="3">
                    <!--通过$符号将serlvlet里面的添加的服务端跳转信息输出-->
                    <font color="red">${error}</font>
                </td>
            </tr>
            <tr>
            </tr>

        </table>
    </form>
</div>
</body>
</html>
