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

        //这个是做gradeSave的servlet的url
        var url;


        //通过这个方法,将添加框里面的value全部重置了
//        function resetValue() {
//            $("#gradeName").val("");
//            $("#gradeDesc").val("");
//        }
        //通过这个js方法,用jquery写的,将id为dataGrid的table的dataGrid发送了一条消息,name是gradeName,类似于键值对的数据类型
        function searchGrade() {
            $("#dataGrid").datagrid('load', {
                gradeName: $("#s_gradeName").val()
            });
        }

        //这个删除button的js函数
        function deleteGrade() {
            //获取所有选中项的集合
            //可以在谷歌浏览器里面调试看到selectedRows的内容
            var selectedRows = $("#dataGrid").datagrid('getSelections');
            //如果一个都没选,应该给用户一个信息
            //下面的语句是easyui提供的方法
            if (selectedRows.length == 0) {
                $.messager.alert("系统提示", "请选择要删除的数据!");
                //提示了之后,肯定就不让继续执行了,需要返回
                return;
            }
            //如果用户选择了数据,那需要把数局的的id拼装成一个字符串,Idset集合
            var strIds = [];
            //循环遍历将selectedRows的所有id,push到数组里面
            for (var i = 0; i <= selectedRows.length - 1; i++) {
                strIds.push(selectedRows[i].id);
            }
            //用join方法,拼成一个字符串,每个数据之间间隔一个","
            var Ids = strIds.join(",");
            //alert(Ids);

            //做个确认框确认用户是不是想要删除这些数据
            $.messager.confirm("系统提示", "你确认要删掉这" + "<font color='red'>" + selectedRows.length + "</font>" + "条数据吗?", function (r) {
                if (r) {
                    //利用jquery的一个ajax异步回传信息
                    alert("恩");
//                    url (String) : 发送请求的URL地址.
//                    data (Map) : (可选) 要发送给服务器的数据，以 Key/value 的键值对形式表示。
//                    callback (Function) : (可选) 载入成功时回调函数(只有当Response的返回状态是success才是调用该方法)。
//                    这个返回状态和我后台在结果里面返回的success是两回事,所以我可以把后台改成了ifsuccess
//                    后台的东西是给我判断是否在后台成功执行了删除操作的
//                    type (String) : (可选)官方的说明是：Type of data to be sent。其实应该为客户端请求的类型(JSON,XML,等等)


                    //这里的result的response返回时返回的一个result,也就是我后台的一个jsonobjcet,可以参考gradeDeleteServlet
                    //里面的jsonObject的result
                    //這個post就是easyui的一套了

                    //这个里面的result他妹的..就是一个json对象
                    $.post("GradeDelete", {delIds: Ids}, function (result) {
                        if (result.ifsuccess) {
                            //alert("delete ok!");
                            //还要告诉用户他到底成功删除了多少条
                            $.messager.alert("系统提示", "您已经成功删除了<font color='red'>" + result.delNums + "</font>条数据!");
                            //刷新一下这个datagrid
                            $("#dataGrid").datagrid('reload');
                        } else {
                            //信息框提示用户删除失败
                            $.messager.alert("系统提示", "删除失败\n错误信息:" + result.errorMsg);
                            alert("delete failure")
                        }
                    }, "json")
                }//else {
                //alert("咦..");
                //}
            })
        }

        //点击添加按钮,弹出一个easyui-dialog框的方法
        function openGradeAddDialog() {
            //会使用到easyui-dialog的一些方法
            $("#dialog").dialog('open').dialog('setTitle', "添加班级信息");
            //打开的时候给gradeSave的url赋值好
            url = "gradeSave";
        }

        //点击关闭按钮,实现关闭easyui-dialog
        function closeGradeDialog() {
            $("#dialog").dialog('close');
            //但是还要reset一下里面的值
            resetDialog();
        }

        //reset gradeName和gradeDesc,dialog的输入框里面的值
        function resetDialog() {
            $("#gradeDesc").val("");
            $("#gradeName").val("");
        }

        //dialog的保存按钮
        function saveGrade() {
            $("#form").form('submit', {
                url: url,
                //这个事件在发送请求之前执行
                onSubmit: function () {
                    return $(this).form('validate')
                },
                //这个函数会返回一个result,从后台返回的result
                //这个success事件必须要求后台返回的success是true才会执行,所以后台无论如何都需要返回success为true
                //他妹的,这里的result居然是json字符串,必须得转换一下才行
                success: function (result) {
                    //如果有错误信息
                    //必须先将jsonstring转换为json对象才行
                    res=eval('('+result+')');
                    if (res.errorMsg) {
                        $.messager.alert("系统提示", res.errorMsg);
                        return;
                    } else {
                        //保存成功
                        //$.messager.alert("系统提示", result.errorMsg);
                        $.messager.alert("系统提示", "保存成功");
                        resetDialog();
                        //关掉dialog
                        $("#dialog").dialog('close');
                        //重新加载一下datagrid
                        $("#dataGrid").datagrid('reload');
                    }

                }
            })
        }

        function openGradeModifyDialog() {
            //首先获取用户选择的行数
            //如果没选要提示去选,如果多选,要提示多选了
            var selectedRows=$("#dataGrid").datagrid('getSelections');
            if (selectedRows.length!=1){
                $.messager.alert("系统提示","请选择一条数据进行修改!");
                return;
            }
            //selectedRows是一行行数据的集合,当然这里只有一行,我们需要取到这一行
            var row=selectedRows[0];
            //弹出对话框
            $("#dialog").dialog('open').dialog('setTitle',"添加班级信息");
            //此时对话框的input里面的value是被置空了的,我们直接把这一行的数据load进去,就可以在原有的基础上改数据了
            //要求是键值对的key要和input的name一样
            $("#form").form('load',row);
            //修改一下url,地址还是servlet的地址,但是可以通过?id=123的方式,增加一条消息给req
            url="gradeSave?id="+row.id;


        }
    </script>
    <title>班级信息管理</title>
</head>
<body style="margin: 5px">
<!--请求地址在里面-->
<%--url是easyui特有的属性,配套的一系列用法,后台获取数据,转换为json形式,再通过response获得的writer输出--%>
<table id="dataGrid" title="班级信息" class="easyui-datagrid" fitColumns="true" pagination="true"
<%--GradeList和谷歌浏览器里面调试时抓到的包的名称一样,通过这个名字去找到那个包--%>
       rownumbers="true" url="GradeList" fit="true" toolbar="#toolBar">
    <thead>
    <tr>
        <%--field 里面的值要和对应的json的key一样--%>
        <%--在后台会查询拿到resultset,通过json-lib包,将resultset利用jsonobject和jsonarray和resultsetmeta获取每一行元素的--%>
        <%--key也就是列标题和value也就是对应的值存储在jsonobject(一行)中,再装进jsonArray,最后传成(rows:jsonArray)返回给table--%>
        <%--增加一个checkbox方便用户批量删除    --%>
        <th field="checkBox" checkbox="true"></th>
        <th field="id" width="50">编号</th>
        <th field="gradeName" width="100">年级名称</th>
        <th field="gradeDesc" width="250">年级描述</th>
    </tr>
    </thead>
</table>
<%--这个toolbar被添加到table里面了,所以input的数据包应该也在table的url GradeList里面???(未定)....不是的啊--%>
<%--这里的input里面的值通过调用a标签里面的js方法使得easyui的datagrid加载了一条信息,然后通过datagrid也就是那个table传给了后台--%>
<%--总的来说,是easyui的一套ajax方法吧,否则的话一般request.getparameter()是要在括号里面用name的,比如input的name--%>
<div id="toolBar">
    <div>
        <a href="javascript:openGradeAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
        <a href="javascript:openGradeModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
        <%--给删除添加一个超链接做js函数--%>
        <a href="javascript:deleteGrade()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        <%--href里面用一个js方法--%>
        &nbsp;班级名称&nbsp;<input type="text" name="s_gradeName" id="s_gradeName"/>&nbsp;<a href="javascript:searchGrade()"
                                                                                         class="easyui-linkbutton"
                                                                                         iconCls="icon-search"
                                                                                         plain="true">搜索</a>
    </div>
</div>


<%--写一个div,引入easyui的dialog--%>
<%--在添加按钮中写一个js方法,每次点击按钮就打开一个dialog--%>
<div id="dialog" class="easyui-dialog" style="width:400px;height:280px;padding: 0px 20px"
     closed="true" buttons="#dialogButton">
    <%--写一个表单,因为需要提交数据--%>
    <form id="form" method="post">
        <%--需要两行,一行显示gradeName,一行是gradeDesc--%>
        <table>
            <tr>
                <td style="font-size: 14px">年级名称:</td>
                <%--添加一个easyui的class使得这个input成为必填值--%>
                <td><input type="text" name="gradeName" id="gradeName" class="easyui-validatebox" required="true"/></td>
            </tr>
            <tr>
                <td style="font-size: 14px" valign="top">年级描述:</td>
                <td><textarea rows="7" cols="30" name="gradeDesc" id="gradeDesc"></textarea></td>
            </tr>
        </table>
    </form>
</div>

<%--写一个div会添加到easyui-dialog里面变成按钮--%>
<div id="dialogButton">
    <a href="javascript:saveGrade()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeGradeDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>
