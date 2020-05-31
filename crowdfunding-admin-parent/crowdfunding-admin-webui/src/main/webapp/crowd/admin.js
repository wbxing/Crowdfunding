// 全局变量存储 adminId
window.adminIdArray = [];
// 声明函数生成确认模态框
function showConfirmModal(adminArray) {
	// 打开模态框
	$("#confirmAdminModal").modal("show");
	// 清楚旧数据
	$("#adminNameSpan").empty();
		
	// 遍历 adminArray 数组
	for (let i = 0; i < adminArray.length; i++) {
		let admin = adminArray[i];
		let adminName = admin.adminName;
		$("#adminNameSpan").append(adminName + "<br/>");
		let adminId = admin.adminId;
		window.adminIdArray.push(adminId);
	}
}

//执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
	// 获取分页数据    
	let pageInfo = getPageInfoRemote();
	// 填充表格
	fillTableBody(pageInfo);
}

// 远程访问服务器端程序获取 pageInfo 数据
function getPageInfoRemote() {
	// 调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
	let ajaxResult = $.ajax({
		"url" : "admin/get/page/info.json",
		"type" : "post",
		"data" : {
			"pageNum" : window.pageNum,
			"pageSize" : window.pageSize,
			"keyword" : window.keyword
		},
		"async" : false,
		"dataType" : "json"
	});
	console.log(ajaxResult);
	// 判断当前响应状态码是否为200
	let statusCode = ajaxResult.status;
	// 如果当前响应状态码不是 200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
	if (statusCode != 200) {
		layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
		return null;
	}
	// 如果响应状态码是 200，说明请求处理成功，获取 pageInfo
	// 此时说明服务器相应成功
	let resultEntity = ajaxResult.responseJSON;
	// 从 resultEntity 中获取 result 属性
	let result = resultEntity.result;
	// 逻辑上判断 result 是否成功
	if (result == "FAILED") {
		layer.msg(resultEntity.message);
		return null;
	}
	// 此时时成功的情况
	// 确认 result 为成功后获取 pageInfo
	let pageInfo = resultEntity.data;
	// 返回 pageInfo
	return pageInfo;
}

// 填充表格
function fillTableBody(pageInfo) {
	// 清除 tbody 中的旧的内容
	$("#adminPageBody").empty();
	// 这里清空是为了让没有搜索结果时不显示页码导航条
	$("#Pagination").empty();
	$("#summaryBox").prop("checked", false);
	// 判断 pageInfo 对象是否有效
	if (pageInfo === null || pageInfo === undefined || pageInfo.list === null
			|| pageInfo.list.length === 0) {
		$("#adminPageBody").append(
				"<tr><td colspan='6' align='center'>抱歉！没有查询到您搜索的数据！</td></tr>");
		return;
	}
	// 使用 pageInfo 的 list 属性填充 tbody
	var pageNum = pageInfo.pageNum;
	for (var i = 0; i < pageInfo.list.length; i++) {
		let admin = pageInfo.list[i];
		let adminId = admin.id;
		let loginAcct = admin.loginAcct;
		let adminName = admin.userName;
		let email = admin.email;
		let numberId = "<td>" + (i + 1) + "</td>";
		let checkboxId = "<td><input id='" + adminId + "' class='itemBox' type='checkbox'></td>";
		let adminLoginAcct = "<td>" + loginAcct + "</td>";
		let adminUserName = "<td>" + adminName + "</td>";
		let adminEmail = "<td>" + email + "</td>";

		let checkBtn = "<button id='" + adminId + "' type='button' class='btn btn-success btn-xs checkBtn' title='分配角色'><i class=' glyphicon glyphicon-check'></i></button>";
		let pencilBtn = "<button id='" + adminId + "' type='button' class='btn btn-primary btn-xs pencilBtn' title='修改'><i class=' glyphicon glyphicon-pencil'></i></button>";
		let removeBtn = "<button id='" + adminId + "' type='button' class='btn btn-danger btn-xs removeBtn' title='删除'><i class=' glyphicon glyphicon-remove'></i></button>";

		let buttonId = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn
				+ "</td>";

		let tr = "<tr>" + numberId + checkboxId + adminLoginAcct + adminUserName + adminEmail + buttonId
				+ "</tr>";

		$("#adminPageBody").append(tr);
	}
	// 生成分页页码导航条
	generateNavigator(pageInfo);
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {
	// 获取总记录数
	var totalRecord = pageInfo.total;
	// 声明相关属性
	var properties = {
		"num_edge_entries" : 3,
		"num_display_entries" : 5,
		"callback" : paginationCallBack,
		"items_per_page" : pageInfo.pageSize,
		"current_page" : pageInfo.pageNum - 1,
		"prev_text" : "上一页",
		"next_text" : "下一页"
	}
	// 调用 pagination() 函数
	$("#Pagination").pagination(totalRecord, properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex, js) {
	// 修改window 对象的pageNum 属性
	window.pageNum = pageIndex + 1;
	// 调用分页函数，不是同步调用，所以不会发生死循环
	generatePage();
	// 取消页码超链接的默认行为
	return false;
}

// 获取总页数
function getTotalPages(pageInfo) {
	return pageInfo.total;
}


