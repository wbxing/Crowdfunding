<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh_CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="crowd/admin.js"></script>
<script type="text/javascript">
    $(function () {
        // 为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        // 调用分页函数，显示分页效果
        generatePage();

        // 给查询按钮绑定单击响应函数
        $("#searchBtn").click(function () {
            // 获取关键词数据赋值给对应的全局变量
            window.keyword = $("#keywordInput").val();
            // 调用分页函数刷新页面
            generatePage();
        });

        // 点击新增按钮打开模态框
        $("#showAddAdminModalBtn").click(function () {
            $("#addAdminModal").modal("show");
        });

        // 给新增模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function () {
            // 获取用户在文本框中输入的角色名称
            // #addModal 表示找到整个模态框
            // 空格表示在后代元素中继续查找
            // [name=roleName]表示匹配name 属性等于roleName 的元素
            var loginAcct = $.trim($("#addAdminModal [name=loginAcct]").val());
            var userPswd = $.trim($("#addAdminModal [name=userPswd]").val());
            var userName = $.trim($("#addAdminModal [name=userName]").val());
            var email = $.trim($("#addAdminModal [name=email]").val());
            // 发送Ajax 请求
            $.ajax({
                "url": "admin/save.json",
                "type": "post",
                "data": {
                    "loginAcct": loginAcct,
                    "userPswd": userPswd,
                    "userName": userName,
                    "email": email
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        // 将页码定位到最后一页
                        window.pageNum = getTotalPages(getPageInfoRemote()) + 1;
                        // 重新加载分页数据
                        generatePage();
                        // 关闭模态框
                        $("#addAdminModal").modal("hide");
                        // 清理模态框
                        $("#addAdminModal [name=loginAcct]").val("");
                        $("#addAdminModal [name=userPswd]").val("");
                        $("#addAdminModal [name=userName]").val("");
                        $("#addAdminModal [name=email]").val("");
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                    $("#addAdminModal").modal("hide");
                    // 清理模态框
                    $("#addAdminModal [name=loginAcct]").val("");
                    $("#addAdminModal [name=userPswd]").val("");
                    $("#addAdminModal [name=userName]").val("");
                    $("#addAdminModal [name=email]").val("");
                }
            });
        });

        $("#adminPageBody").on("click", ".pencilBtn", function () {
            // 打开模态框
            $("#editAdminModal").modal("show");
            // 获取表格中当前行中的数据
            var acct = $(this).parent().prev().prev().prev().text();
            var name = $(this).parent().prev().prev().text();
            var email = $(this).parent().prev().text();
            // 获取当前角色的 id
            // 为了让执行更新的按钮能够获取到 roleId 的值，把它放在全局变量上
            window.adminId = this.id;
            // 使用roleName 的值设置模态框中的文本框
            $("#editAdminModal [name=loginAcct]").val(acct);
            $("#editAdminModal [name=userName]").val(name);
            $("#editAdminModal [name=email]").val(email);
        });

        // 给更新模态框中的更新按钮绑定单击响应函数
        $("#updateAdminBtn").click(function () {
            // 从文本框中获取新的角色名称
            var loginAcct = $.trim($("#editAdminModal [name=loginAcct]").val());
            var userName = $.trim($("#editAdminModal [name=userName]").val());
            var email = $.trim($("#editAdminModal [name=email]").val());
            // 发送Ajax 请求执行更新
            $.ajax({
                "url": "admin/update.json",
                "type": "post",
                "data": {
                    "id": window.adminId,
                    "loginAcct": loginAcct,
                    "userName": userName,
                    "email": email
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功！");
                        // 重新加载分页数据
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            // 关闭模态框
            $("#editAdminModal").modal("hide");
        });

        // 执行删除
        $("#removeadminBtn").click(function () {
            var requestBody = JSON.stringify(window.adminIdArray);
            $.ajax({
                "url": "admin/remove.json",
                "type": "post",
                "data": requestBody,
                "contentType": "application/json;charset=UTF-8",
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        // 重新加载分页数据
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            // 关闭模态框
            $("#confirmAdminModal").modal("hide");
        });

        // 单条删除
        $("#adminPageBody").on("click", ".removeBtn", function () {
            // 获取 name
            var name = $(this).parent().prev().prev().text();
            // 创建 admin 对象，存入数组
            var adminArray = [{
                "adminId": this.id,
                "adminName": name
            }];
            // 调用函数显示模态框
            showConfirmModal(adminArray);
        });

        // 给全选绑定单击响应函数
        $("#summaryBox").click(function () {
            $(".itemBox").prop("checked", this.checked);
        });

        // 反向绑定
        $("#adminPageBody").on("click", ".itemBox", function () {
            // 获取当前选中的数量
            let checkedCount = $(".itemBox:checked").length;
            // 总数量
            let totalCount = $(".itemBox").length;
            // 设置全选状态
            $("#summaryBox").prop("checked", checkedCount === totalCount);
        });

        // 批量删除
        $("#batchRemoveBtn").click(function () {
            var adminArray = [];
            // 遍历当前选中的角色
            $(".itemBox:checked").each(function () {

                let adminId = this.id;
                let adminName = $(this).parent().next().next().text();
                adminArray.push({
                    "adminId": adminId,
                    "adminName": adminName
                });
            });
            // 检查 roleArray 长度
            if (adminArray.length === 0) {
                layer.msg("未选择任何角色");
                return;
            }
            showConfirmModal(adminArray);
        });

        // 打开模态框
        $("#adminPageBody").on("click", ".checkBtn", function () {
            // 打开模态框
            $("#assginRoleModal").modal("show");
            // 清除旧数据
            $("#assigned").empty();
            $("#unassigned").empty();
            window.assginingAdminId = this.id;
            $.ajax({
                "url": "assign/role.json",
                "type": "post",
                "data": {
                    "adminId": this.id
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    var data = response.data;
                    var assignedRoleList = data.assignedRoleList
                    var unassignedRoleList = data.unassignedRoleList
                    if (result === "SUCCESS") {
                        //layer.msg("查询成功！");
                        //$("#assgin").
                        for (let i = 0; i < unassignedRoleList.length; i++) {
                            var unassgined = unassignedRoleList[i];
                            $("#unassigned").append("<option value='" + unassgined.id + "'>" + unassgined.name + "</option>");
                        }
                        for (let i = 0; i < assignedRoleList.length; i++) {
                            var assgined = assignedRoleList[i];
                            $("#assigned").append("<option value='" + assgined.id + "'>" + assgined.name + "</option>");

                        }
                    }
                    if (result === "FAILED") {
                        layer.msg("查询失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
        });
        // 左右移动
        $("#toRightBtn").click(function () {
            $("#unassigned>option:selected").appendTo("#assigned");
        });
        $("#toLeftBtn").click(function () {
            $("#assigned>option:selected").appendTo("#unassigned");
        });
        // 执行保存
        $("#saveAdminRoleRelationshipBtn").click(function () {
            let roleIdList = [];
            let assigned = $("#assigned>option")
            for (let i = 0; i < assigned.length; i++) {
                roleIdList.push(assigned[i].value);
                //console.log(assigned[i].value);
            }
            //console.log(roleIdList);
            let requestBody = JSON.stringify(roleIdList);
            // console.log(requestBody);
            // console.log(window.assginingAdminId);
            $.ajax({
                "url": "assign/save.json",
                "type": "post",
                "data": {
                    "adminId": window.assginingAdminId,
                    "roleIdList": requestBody
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("分配成功！");
                        // 重新加载分页数据
                        // generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("分配失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            // 关闭模态框
            $("#assginRoleModal").modal("hide");
        });
    });
</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <i class="glyphicon glyphicon-th"></i> 数据列表
                    </h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float: left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning">
                            <i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger"
                            style="float: right; margin-left: 10px;">
                        <i class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" id="showAddAdminModalBtn" class="btn btn-primary" style="float: right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>

                    <hr style="clear: both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="adminPageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination">
                                        <!-- 这里显示分页 -->
                                    </div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal-admin-confrim.jsp" %>
<%@include file="/WEB-INF/modal-admin-add.jsp" %>
<%@include file="/WEB-INF/modal-admin-edit.jsp" %>
<%@include file="/WEB-INF/modal-assgin-role.jsp" %>
</body>
</html>