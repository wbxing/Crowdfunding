<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="confirmAdminModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">删除管理员</h4>
            </div>
            <div class="modal-body">
                <h4>确认是否要删除下列管理员：</h4>
                <span id="adminNameSpan"></span>
            </div>
            <div class="modal-footer">
                <button id="removeadminBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-ok"></i>
                    确认
                </button>
            </div>
        </div>
    </div>
</div>