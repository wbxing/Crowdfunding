package com.crowd.mvc.controller;

import com.crowd.entity.Menu;
import com.crowd.service.api.MenuService;
import com.crowd.utils.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuController {

    private MenuService menuService;

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        // 查询全部的Menu 对象
        List<Menu> menuList = menuService.getAll();
        // 声明一个变量用来存储找到的根节点
        Menu root = null;
        // 创建Map 对象用来存储id 和Menu 对象的对应关系便于查找父节点
        Map<Integer, Menu> menuMap = new HashMap<>();
        // 遍历menuList 填充menuMap
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }
        // 再次遍历menuList 查找根节点、组装父子节点
        for (Menu menu : menuList) {
            // 获取当前menu 对象的pid 属性值
            Integer pid = menu.getPid();
            // 如果pid 为null，判定为根节点
            if (pid == null) {
                root = menu;
                // 8.如果当前节点是根节点，那么肯定没有父节点，不必继续执行
                continue;
            }
            // 如果pid 不为null，说明当前节点有父节点，那么可以根据pid 到menuMap 中查找对应的Menu 对象
            Menu father = menuMap.get(pid);
            // 将当前节点存入父节点的children 集合
            father.getChildren().add(menu);
        }
        // 经过上面的运算，根节点包含了整个树形结构，返回根节点就是返回整个树
        return ResultEntity.successWithData(root);
    }
}
