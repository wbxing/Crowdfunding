// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    // 1.获取分页数据
    let pageInfo = getPageInfoRemote();
    // 2.填充表格
    fillTableBody(pageInfo);
}

// 远程访问服务器端程序获取 pageInfo 数据
function getPageInfoRemote() {
    // 调用 $.ajax() 函数发送请求并接受 $.ajax() 函数的返回值
    let ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    });
    console.log(ajaxResult);
    // 判断当前响应状态码是否为200
    let statusCode = ajaxResult.status;
    // 如果当前响应状态码不是200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
    if (statusCode !== 200) {
        layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
        return null;
    }
    // 如果响应状态码是200，说明请求处理成功，获取pageInfo
    let resultEntity = ajaxResult.responseJSON;
    // 从 resultEntity 中获取result 属性
    let result = resultEntity.result;
    // 判断result 是否成功
    if (result === "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }
    // 确认 result 为成功后获取 pageInfo
    // 返回 pageInfo
    return resultEntity.data;
}

// 填充表格
function fillTableBody(pageInfo) {
    // 清除 tbody 中的旧的内容
    $("#rolePageBody").empty();
    // 这里清空是为了让没有搜索结果时不显示页码导航条
    $("#Pagination").empty();
    $("#summaryBox").prop("checked", false);
    // 判断 pageInfo 对象是否有效
    if (pageInfo === null || pageInfo === undefined || pageInfo.list === null || pageInfo.list.length === 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜索的数据！</td></tr>");
        return;
    }
    // 使用 pageInfo 的 list 属性填充 tbody
    for (let i = 0; i < pageInfo.list.length; i++) {
        let role = pageInfo.list[i];
        let roleId = role.id;
        let roleName = role.name;
        let numberTd = "<td>" + (i + 1) + "</td>";
        let checkboxId = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>";
        let roleNameTd = "<td>" + roleName + "</td>";
        let checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn' title='选择'>" +
            "<i class=' glyphicon glyphicon-check'></i></button>";
        let pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'" +
            " title='修改'><i class='glyphicon glyphicon-pencil'></i></button>";
        let removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'" +
            " title='删除'><i class=' glyphicon glyphicon-remove'></i></button>";
        let buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        let tr = "<tr>" + numberTd + checkboxId + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }
    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {
    // 获取总记录数
    let totalRecord = pageInfo.total;
    // 声明相关属性
    let properties = {
        "num_edge_entries": 3,
        "num_display_entries": 5,
        "callback": paginationCallBack,
        "items_per_page": pageInfo.pageSize,
        "current_page": pageInfo.pageNum - 1,
        "prev_text": "上一页",
        "next_text": "下一页"
    }
    // 调用pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
    // 修改 window 对象的 pageNum 属性
    window.pageNum = pageIndex + 1;
    // 调用分页函数
    generatePage();
    // 取消页码超链接的默认行为
    return false;
}

// 获取总页数
function getTotalPages(pageInfo) {
    return pageInfo.total;
}

// 全局变量存储 roleId
window.roleIdArray = [];

// 声明函数生成确认模态框
function showConfirmModal(roleArray) {
    // 打开模态框
    $("#confirmModal").modal("show");
    // 清楚旧数据
    $("#roleNameSpan").empty();

    // 遍历 roleArray 数组
    for (let i = 0; i < roleArray.length; i++) {
        let role = roleArray[i];
        let roleName = role.name;
        $("#roleNameSpan").append(roleName + "<br/>");
        let roleId = role.id;
        window.roleIdArray.push(roleId);
    }
}

// 在 Auth 模态框中加载树形结构数据
function fillAuthTree() {
    // 发送 ajax 请求查询 Auth 数据
    let ajaxResult = $.ajax({
        "url": "assign/get/all/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false
    });
    // 判断当前响应状态码是否为200
    let statusCode = ajaxResult.status;
    // 如果当前响应状态码不是 200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
    if (statusCode !== 200) {
        layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
        return;
    }
    // 如果响应状态码是 200，说明请求处理成功，获取 pageInfo
    // 此时说明服务器相应成功
    let resultEntity = ajaxResult.responseJSON;
    // 从 resultEntity 中获取 result 属性
    let result = resultEntity.result;
    // 逻辑上判断 result 是否成功
    if (result === "FAILED") {
        layer.msg(resultEntity.message);
        return;
    }

    // 从响应数据中获取 auth 信息
    let authList = resultEntity.data;
    // 准备对 zTree 进行设置的 json 对象
    let setting = {
        data: {
            "simpleData": {
                // 开启简单 json 功能
                "enable": true,
                // 使用categoryId 属性关联父节点，不用默认的pId 了
                "pIdKey": "categoryId"
            },
            "key": {
                // 使用title 属性显示节点名称，不用默认的name 作为属性名了
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };
    // 生成树形结构
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    // 获取zTreeObj 对象
    let zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 调用zTreeObj 对象的方法，把节点展开
    zTreeObj.expandAll(true);
    // 查询已经分配的 Auth 的 id 组成
    ajaxResult = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false
    });
    // 判断当前响应状态码是否为200
    statusCode = ajaxResult.status;
    // 如果当前响应状态码不是 200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
    if (statusCode !== 200) {
        layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
        return;
    }
    let authIdArray = ajaxResult.responseJSON.data;
    // 根据 authIdArray 把树形结构中对应的节点勾选上
    for (let i = 0; i < authIdArray.length; i++) {
        let authId = authIdArray[i];
        let treeNode = zTreeObj.getNodeByParam("id", authId);
        console.log(treeNode);
        // checked = true 表示勾选节点
        let checked = true;
        // checkTypeFlag = false 表示只修改此节点勾选状态，无任何勾选联动操作
        let checkTypeFlag = false;
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}
