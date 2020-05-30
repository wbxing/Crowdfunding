function myAddDiyDom(treeId, treeNode) {
	// treeId 是整个数形结构附着的 ul 标签的 id
	// console.log("treeId" + treeId);
	// 当前树形节点的全部数据，包括从后端查询得到的 Menu 对象的全部属性
	// console.log(treeNode);
	// 根据 id 的生成规则拼接得到 id

	// zTree 生成id 的规则
	// 例子：treeDemo_7_ico
	// 解析：ul 标签的id_当前节点的序号_功能
	// 提示："ul 标签的id_当前节点的序号" 部分可以通过访问 treeNode 的 tId 属性得到
	// 根据 id 的生成规则拼接出来 span 标签的 id
	// 根据控制图标的 span 标签的 id 找到这个 span 标签
	let spanId = treeNode.tId + "_ico";
	// 替换该标签的 class
	$("#" + spanId).removeClass().addClass(treeNode.icon);
}

// 光标移入显示按钮组
function myAddHoverDom(treeId, treeNode) {
	console.log("myAddHoverDom");
	// 按钮组的标签结构：<span><a><i></i></a><a><i></i></a></span>
	// 按钮组出现的位置：节点中 treeDemo_n_a 超链接的后面
	// 为了在需要移除按钮组的时候能够精确定位到按钮组所在 span，需要给 span 设置有规律的 id
	let btnGroupId = treeNode.tId + "_btnGrp";
	// 判断一下以前是否已经添加了按钮组
	if($("#" + btnGroupId).length > 0) {
		return ;
	}
	// 准备各个按钮的 html 标签
	let addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs'" +
	" style='margin-left:10px;padding-top:0;' href='#' title=' 添加子节点'>" +
	"&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
	let removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs'" +
	" style='margin-left:10px;padding-top:0;' href='#' title=' 删除节点'>" +
	"&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>"
	let editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs'" +
	" style='margin-left:10px;padding-top:0;' href='#' title=' 修改节点'>" +
	"&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
	// 获取当前节点的 level
	let level = treeNode.level;
	// 声明变量存储拼装好的按钮代码
	let btnHTML = "";

	// 判断当前节点的级别
	if(level === 0) {
		// 级别为 0 时是根节点，只能添加子节点
		btnHTML = addBtn;
	}
	if(level === 1) {
		// 级别为 1 时是分支节点，可以添加子节点、修改
		btnHTML = addBtn + " " + editBtn;
		// 获取当前节点的子节点数量
		let length = treeNode.children.length;
		// 如果没有子节点，可以删除
		if(length === 0) {
			btnHTML = btnHTML + " " + removeBtn;
		}
	}
	if(level === 2) {
		// 级别为 2 时是叶子节点，可以修改、删除
		btnHTML = editBtn + " " + removeBtn;
	}

	// 找到附着按钮组的超链接
	let anchorId = treeNode.tId + "_a";
	// 执行在超链接后面附加 span 元素的操作
	$("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHTML + "</span>");

}

// 光标移出关闭按钮组
function myRemoveHoverDom(treeId, treeNode) {
	console.log("myRemoveHoverDom");
	// 拼接按钮组的 id
	let btnGroupId = treeNode.tId + "_btnGrp";
	// 移除对应的元素
	$("#" + btnGroupId).remove();
}

// 生成树形结构的函数
function generateTree() {
    // 准备生成树形结构的数据，数据来源是 ajax 请求得到的
    $.ajax({
    	"url": "menu/get/whole/tree.json",
    	"type":"post",
    	"dataType":"json",
    	"success" : function(response) {
            let result = response.result;
            if (result === "SUCCESS") {
                // 创建 json 对象存储 zTree 所做的设置
                let setting = {
                	"view": {
                		"addDiyDom": myAddDiyDom,
                		"addHoverDom": myAddHoverDom,
                		"removeHoverDom": myRemoveHoverDom
                	},
                    "data": {
                        "key": {
                            "url": "maomi"
                        }
                    }
                };
            	// 请求获取的数据
            	let zNodes = response.data;
            	// 初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if (result === "FAILED") {
                layer.msg("操作失败！" + response.message);
            }
        },
        "error" : function(response) {
            layer.msg(response.status + " " + response.statusText);
        }
    });
}
