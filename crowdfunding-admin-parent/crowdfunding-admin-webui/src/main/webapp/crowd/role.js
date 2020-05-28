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
        let checkboxTd = "<td><input type='checkbox'></td>";
        let roleNameTd = "<td>" + roleName + "</td>";
        let checkBtn = "<button type='button' class='btn btn-success btn-xs'><i class='glyphicon glyphicon-check'></i></button>";
        let pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'" +
            " title='修改'><i" +
            " class='glyphicon" +
            " glyphicon-pencil'></i></button>";
        let removeBtn = "<button type='button' class='btn btn-danger btn-xs'><i class='glyphicon glyphicon-remove'></i></button>";
        let buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        let tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
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
