<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="addModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">新增角色</h4>
            </div>
            <form class="form-signin" role="form">
                <div class="modal-body">
                    <div class="form-group has-success has-feedback">
                        <input type="text" name="roleName" class="form-control" id="inputSuccess4" placeholder="请输入角色名称"
                               autofocus>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="resetRoleBtn" type="reset" class="btn btn-danger"><i
                            class="glyphicon glyphicon-refresh"></i> 重置
                    </button>
                    <button id="saveRoleBtn" type="button" class="btn btn-primary"><i
                            class="glyphicon glyphicon-plus"></i> 保存
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>