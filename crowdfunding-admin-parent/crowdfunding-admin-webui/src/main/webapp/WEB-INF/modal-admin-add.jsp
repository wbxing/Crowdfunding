<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="addAdminModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">新增管理员</h4>
            </div>
            <div class="modal-body">
                <form action="admin/save.html" method="post" role="form">
                    <p>${requestScope.exception.message}</p>
                    <div class="form-group">
                        <label for="exampleInputPassword1">登录账号</label> <input name="loginAcct" type="text"
                            class="form-control" id="exampleInputPassword1" placeholder="请输入登录账号">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">登录密码</label> <input name="userPswd" type="password"
                            class="form-control" id="exampleInputPassword1" placeholder="请输入登录密码">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">用户名</label> <input name="userName" type="text"
                            class="form-control" id="exampleInputPassword1" placeholder="请输入用户名称">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">邮箱地址</label> <input name="email" type="email"
                            class="form-control" id="exampleInputEmail1" placeholder="请输入邮箱地址">
                        <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="saveRoleBtn" type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>