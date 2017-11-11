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
    <title>学生信息管理系统主页面</title>
    <!--session的认证,权限验证是否登录过了,否则跳转回登录界面-->
    <!--在以下框框中才能写java代码-->
<%
    //权限验证
    if(session.getAttribute("currentUser")==null){
        System.out.println("弹回登录页面");
        response.sendRedirect("index.jsp");
        return;
    }
%>
    <!--引入easyUi里面的东西-->
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.3/themes/icon.css">
    <script type="text/javascript" src="jquery-easyui-1.5.3/jquery.min.js"></script>
    <script type="text/javascript" src="jquery-easyui-1.5.3/jquery.easyui.min.js"></script>
    <!--easyUI的本地化引入-->
    <script type="text/javascript" src="jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js"></script>
    >
    <!--导航菜单的js-->
    <script type="text/javascript">
        $(function () {
            //数据
            //[]是一个数组结构
            //{}包含一组数据
            //这就是一个tree表,其中可以有很多根,子结构,子结构又是一个数组,有两个元素
            var treeData = [{
                text: "根",
                children: [{
                    text: "班级信息管理",
                    attributes: {
                        url: "gradeInfoManage.jsp"
                    }
                }, {
                    text: "学生信息管理",
                    attributes: {
                        url: "studentInfoManage.jsp"
                    }
                }]
                //var末尾要加上分号
            }];
            //实例化树菜单,这里会用到ul的id tree
            //可以查easyUI的文档
            $("#tree").tree({
                data: treeData,
                lines: true,
                onClick: function (node) {
                    //如果是有属性的才执行函数
                    if (node.attributes) {
                        //函数末尾要加上分号
                        openTab(node.text, node.attributes.url);
                    }
                }
                //$末尾要加上分号
            });

            //添加一个函数,实现点击一下就新增一个tabs,jQuery取id要加#
            function openTab(text, url) {
                //先判断是否已经有相同的tabs了,如果有就选中
                if ($("#tabs").tabs("exists", text)) {
                    $("#tabs").tabs("select", text);
                } else {
                    //定义一个内联框,里面放传进来的url
                    var content="<iframe frameborder='0' scrolling='auto'  style='width: 100%;height: 100%' src="+url+"></iframe>"
                    $("#tabs").tabs('add', {
                        title: text,
                        closable: true,
                        content: content
                    });
                }
            }
        });

    </script>
</head>
<!--class属性一般用于指向css文件中的layout,也可以利用它通过 JavaScript 来改变带有指定 class 的 HTML 元素。-->
<body class="easyui-layout">
<!--分成四个区域-->
<div region="north" style="height: 80px;background-color: #E0EDFF">
    <div align="left" style="width: 80%;float: left">
        <img src="images/main.jpg"></div>
    当前用户:<font color="red">${sessionScope.currentUser.userName}</font>
    <!--$大括号可以去到request或session里面的set过的attribute-->
    <%--<div style="padding-top: 50px;padding-right:20px">当前用户: &nbsp;<font color="black">${currentUser.userName}</font> </div>--%>
</div>
<div region="west" width="150px" title="导航菜单" split="true">
    <!--西边的是一个菜单栏,用一个ul标签做好-->
    <!--结合js一起做好-->
    <ul id="tree"></ul>
</div>
<div region="center">
    <!--中间加一个tabs控件-->
    <div class="easyui-tabs" border="false" fit="true" id="tabs">
        <div title="首页">
            <div align="center" style="padding-top: 100px"><font color=aqua size="10">欢迎使用</font></div>
        </div>
    </div>
</div>
<div region="south" height="25px" align="center">版权所有<a href="http://www.baidu.com"> 我的网址名称</a></div>
</body>
</html>
