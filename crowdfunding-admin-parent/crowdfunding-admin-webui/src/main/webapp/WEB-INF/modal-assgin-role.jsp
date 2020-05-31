<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="assginRoleModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">分配角色</h4>
            </div>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form role="form" class="form-inline">
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select id="unassigned"
                                    class="form-control"
                                    multiple="multiple" size="10"
                                    style="width: 100px; overflow-y: auto;">
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top: 20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left: 40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select id="assigned"
                                    class="form-control"
                                    multiple="multiple" size="10"
                                    style="width: 100px; overflow-y: auto;">
                            </select>
                        </div>
                        <button id="saveAdminRoleRelationshipBtn" type="button" class="btn btn-primary">
                            <i class="glyphicon glyphicon-plus"></i> 保存
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>