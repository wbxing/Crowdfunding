package com.crowd.controller;

import com.crowd.api.MySQLRemoteService;
import com.crowd.constant.CrowdConstant;
import com.crowd.entity.vo.PortalTypeVO;
import com.crowd.utils.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalController {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(Model model) {
        // 调用 MySQLRemoteService 提供的方法查询首页要显示得数据
        ResultEntity<List<PortalTypeVO>> resultEntity =
                mySQLRemoteService.getPortalTypeProjectDataRemote();
        // 检查查询结果
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            // 获取查询结果数据
            List<PortalTypeVO> list = resultEntity.getData();
            // 存入模型
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, list);
        }
        // 这里实际开发中需要加载数据
        return "portal";
    }
}

