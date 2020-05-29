<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh_CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="crowd/role.js"></script>
<script type="text/javascript">
    $(function () {
        // 为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        // 调用执行分页的函数，显示分页效果
        generatePage();

        // 给查询按钮绑定单击响应函数
        $("#searchBtn").click(function () {
            // 获取关键词数据赋值给对应的全局变量
            window.keyword = $("#keywordInput").val();
            // 调用分页函数刷新页面
            generatePage();
        });

        // 点击新增按钮打开模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });

        // 给新增模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function () {
            // 获取用户在文本框中输入的角色名称
            // #addModal 表示找到整个模态框
            // 空格表示在后代元素中继续查找
            // [name=roleName]表示匹配name 属性等于roleName 的元素
            var roleName = $.trim($("#addModal [name=roleName]").val());
            // 发送Ajax 请求
            $.ajax({
                "url": "role/save.json",
                "type": "post",
                "data": {
                    "name": roleName
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        // 将页码定位到最后一页 + 1
                        window.pageNum = getTotalPages(getPageInfoRemote()) + 1;
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
            $("#addModal").modal("hide");
            // 清理模态框
            $("#addModal [name=roleName]").val("");
        });

        // 给页面上的“铅笔”按钮绑定单击响应函数，目的是打开模态框
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            // 打开模态框
            $("#editModal").modal("show");
            // 获取表格中当前行中的角色名称
            var roleName = $(this).parent().prev().text();
            // 获取当前角色的 id
            // 为了让执行更新的按钮能够获取到 roleId 的值，把它放在全局变量上
            window.roleId = this.id;
            // 使用roleName 的值设置模态框中的文本框
            $("#editModal [name=roleName]").val(roleName);
        });

        // 给更新模态框中的更新按钮绑定单击响应函数
        $("#updateRoleBtn").click(function () {
            // 从文本框中获取新的角色名称
            let roleName = $("#editModal [name=roleName]").val();
            // 发送Ajax 请求执行更新
            $.ajax({
                "url": "role/update.json",
                "type": "post",
                "data": {
                    "id": window.roleId,
                    "name": roleName
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
            $("#editModal").modal("hide");
        });

        // 执行删除
        $("#removeRoleBtn").click(function() {
            var requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                "url" : "role/remove.json",
                "type" : "post",
                "data" : requestBody,
                "contentType" : "application/json;charset=UTF-8",
                "dataType" : "json",
                "success" : function(response) {
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
                "error" : function(response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            // 关闭模态框
            $("#confirmModal").modal("hide");
        });

        // 单条删除
        $("#rolePageBody").on("click", ".removeBtn", function() {
            // 获取 name
            var name = $(this).parent().prev().text()
            // 创建 role 对象，存入数组
            var roleArray = [ {
                id : this.id,
                name : name
            } ];
            // 调用函数显示模态框
            console.log("name = " + name);
            console.log("id = " + this.id);
            showConfirmModal(roleArray);
        });

        // 给全选绑定单击响应函数
        $("#summaryBox").click(function () {
            $(".itemBox").prop("checked", this.checked);
        });

        // 反向绑定
        $("#rolePageBody").on("click", ".itemBox", function () {
            // 获取当前选中的数量
            let checkedCount = $(".itemBox:checked").length;
            // 总数量
            let totalCount = $(".itemBox").length;
            // 设置全选状态
            $("#summaryBox").prop("checked", checkedCount === totalCount);
        });

        // 批量删除
        $("#batchRemoveBtn").click(function () {
            var roleArray = [];
            // 遍历当前选中的角色
            $(".itemBox:checked").each(function () {

                let roleId = this.id;
                let roleName = $(this).parent().next().text();
                roleArray.push({
                    "id": roleId,
                    "name": roleName
                });
            });
            // 检查 roleArray 长度
            if (roleArray.length === 0) {
                layer.msg("未选择任何角色");
                return;
            }
            showConfirmModal(roleArray);
        });

        // checked 按钮
        $("#rolePageBody").on("click", ".checkBtn", function() {
            var checkBox = $(this).parent().parent().find(":checkbox");
            //console.log(checkBox);
            var status = checkBox[0].checked;
            //console.log(status);
            $(checkBox[0]).prop("checked", !status);
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
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float: right; margin-left: 10px;">
                        <i class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" class="btn btn-primary" style="float: right;"
                            id="showAddModalBtn">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear: both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
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
<%@include file="/WEB-INF/modal-role-add.jsp" %>
<%@include file="/WEB-INF/modal-role-edit.jsp" %>
<%@include file="/WEB-INF/modal-role-confrim.jsp"%>
</body>
</html>