<%--
  Created by IntelliJ IDEA.
  User: Frings
  Date: 2017/10/16
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <!--引入easyUi里面的东西-->
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.3/themes/icon.css">
    <script type="text/javascript" src="jquery-easyui-1.5.3/jquery.min.js"></script>
    <script type="text/javascript" src="jquery-easyui-1.5.3/jquery.easyui.min.js"></script>
    <!--easyUI的本地化引入-->
    <script type="text/javascript" src="jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        //        会使用的js函数
        function searchStudent() {

            //这里从datagrid向后台发送请求,这样返回也是返回到datagrid,这样就能datagrid就能拿到有搜索条件之后的result了
            $("#dataGrid").datagrid('load', {
                stuNo: $("#s_stuNo").val(),
                stuName: $("#s_stuName").val(),
                sex: $("#s_sex").combobox('getValue'),
                bbirthday: $("#s_bbirthday").datebox('getValue'),
                ebirthday: $("#s_ebirthday").datebox('getValue'),
                gradeId: $("#s_gradeId").combobox('getValue')
            })
//          jquery取dom
//            alert($("#s_stuNo").val());
////          但是如果你想取到combobox里面的东西的话你必须使用easyui自己封装的方法
//            alert($("#s_sex").combobox("getValue"));
////          datebox也是一样的
//            alert($("#s_bbirthday").datebox("getValue"));
//            alert($("#s_ebirthday").datebox("getValue"));
////            取到combobox里面的值
//            alert($("#s_gradeId").combobox("getValue"));
        }

        function deleteStudent() {
                //拿到datagrid里面checkbox的选择的东西
                var selectedRows = $("#dataGrid").datagrid('getSelections');
                //提示用户选择上要删除的元素
                if (selectedRows.length == 0) {
                    $.messager.alert("系统提示", "请选择要删除的数据!");
                    //直接返回
                    return;
                }
                //创建一个数组存放选择到的Id
                var strIds = [];
                for (var i = 0; i <= selectedRows.length - 1; i++) {
                    strIds.push(selectedRows[i].stuId);
                }
                //用join方法,拼成一个字符串,数据之间用,隔开
                var Ids = strIds.join(",");
                //test
                //alert(Ids);
                //确认框确认用户是不是想要删除这些数据
                //function(r)是一个回调函数,用户点击确认时,会返回true,否则返回false
                $.messager.confirm("系统提示", "你确认要删除掉这" + "<font color='red'>" + selectedRows.length + "</font>" + "条数据吗?", function (r) {
                    if (r) {
                        //test
                        //alert("恩");
                        //利用jquery的一个ajax异步回传信息

//                    url (String) : 发送请求的URL地址.
//                    data (Map) : (可选) 要发送给服务器的数据，以 Key/value 的键值对形式表示。
//                    callback (Function) : (可选) 载入成功时回调函数(只有当Response的返回状态是success才是调用该方法)。
//                    type (String) : (可选)官方的说明是：Type of data to be sent。其实应该为客户端请求的类型(JSON,XML,等等)
//                    result是从后台返回的result

                        $.post("studentDelete", {delIds: Ids}, function (result) {
                            if (result.ifsuccess) {
                                //代表后台删除成功
                                //只有后台删除成功的时候后台才会塞一个true的键值对过来
                                $.messager.alert("系统提示", "您已经成功删除了<font color='red'>" + result.delNums + "</font>条数据!");
                                //刷新一个datagrid
                                $("#dataGrid").datagrid('reload');
                            } else {
                                $.messager.alert("系统提示", "删除失败\n错误信息:" + result.errorMsg);
                            }
                        }, "json")
                    }
                })
        }
    </script>
</head>
<body style="margin: 5px">
<!--请求地址在里面-->
<%--url是easyui特有的属性,配套的一系列用法,后台获取数据,转换为json形式,再通过response获得的writer输出--%>
<table id="dataGrid" title="学生信息" class="easyui-datagrid" fitColumns="true" pagination="true"
<%--GradeList和谷歌浏览器里面调试时抓到的包的名称一样,通过这个名字去找到那个包--%>
       rownumbers="true" url="studentList" fit="true" toolbar="#toolBar">
    <thead>
    <tr>
        <%--field 里面的值要和对应的json的key一样--%>
        <%--在后台会查询拿到resultset,通过json-lib包,将resultset利用jsonobject和jsonarray和resultsetmeta获取每一行元素的--%>
        <%--key也就是列标题和value也就是对应的值存储在jsonobject(一行)中,再装进jsonArray,最后传成(rows:jsonArray)返回给table--%>
        <%--增加一个checkbox方便用户批量删除    --%>
        <th field="checkBox" checkbox="true"></th>
        <th field="stuId" width="50" align="center">编号</th>
        <th field="stuNo" width="50" align="center">学生学号</th>
        <th field="sex" width="50" align="center">学生性别</th>
        <th field="stuName" width="50" align="center">学生名称</th>
        <th field="email" width="120" align="center">学生邮箱</th>
        <th field="gradeId" width="50" align="center">学生班级Id</th>
        <th field="gradeName" width="120" align="center">学生班级名称</th>
        <th field="birthday" width="100" align="center">学生生日</th>
        <th field="gradeDesc" width="250" align="center">学生描述</th>
    </tr>
    </thead>
</table>

<%--这个toolbar被添加到table里面了,所以input的数据包应该也在table的url GradeList里面???(未定)....不是的啊--%>
<%--这里的input里面的值通过调用a标签里面的js方法使得easyui的datagrid加载了一条信息,然后通过datagrid也就是那个table传给了后台--%>
<%--总的来说,是easyui的一套ajax方法吧,否则的话一般request.getparameter()是要在括号里面用name的,比如input的name--%>
<div id="toolBar">
    <div>
        <a href="javascript:openStudentAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
        <a href="javascript:openStudentModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
        <%--给删除添加一个超链接做js函数--%>
        <a href="javascript:deleteStudent()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        <%--href里面用一个js方法--%>
        &nbsp;学号&nbsp;<input type="text" name="s_stuNo" id="s_stuNo" size="10"/>&nbsp;
        &nbsp;姓名&nbsp;<input type="text" name="s_stuName" id="s_stuName" size="10"/>&nbsp;
        <%--用select控件来实现easyui的combobox,也可以用input,查文档得到使用方法--%>
        <%--select就是创建带有选项的选择列表：--%>
        &nbsp;性别&nbsp;<select class="easyui-combobox" name="s_sex" id="s_sex" editable="false" panelHeight="auto">
        <option value="">请选择.....</option>
        <option value="男">男</option>
        <option value="女">女</option>
    </select>
        <%--用easyui的databox来实现出生日期的选择--%>
        <%--实际上本质是一个input--%>
        &nbsp;出生日期&nbsp;<input class="easyui-datebox" name="s_bbirthday" id="s_bbirthday" size="10" editable="false"/>--><input
            class="easyui-datebox" name="s_ebirthday" id="s_ebirthday" size="10" editable="false"/>
        <%--因为要从后台ajax异步取值,所以可能需要使用input实现combobox--%>
        <%--valueField是值,textField是显示的东西--%>
        <%--这个值会在搜索的时候取到并返回给后台--%>
        <%--添加了data-options里面的属性后回想后台索取值--%>
        &nbsp;学生班级&nbsp;<input class="easyui-combobox" name="s_gradeId" id="s_gradeId" size="10"
                               data-options="editable:false,valueField:'id',textField:'gradeName',url:'gradeComboList'"/>
        <a href="javascript:searchStudent()"
           class="easyui-linkbutton"
           iconCls="icon-search"
           plain="true">搜索</a>
    </div>
</div>

</body>

</html>
