package com.crowd.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.crowd.entity.vo.AddressVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crowd.api.MySQLRemoteService;
import com.crowd.constant.CrowdConstant;
import com.crowd.entity.vo.MemberLoginVO;
import com.crowd.entity.vo.OrderProjectVO;
import com.crowd.utils.ResultEntity;

@Controller
public class OrderController {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("returnId") Integer returnId, HttpSession session) {
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO orderProjectVO = resultEntity.getData();
            // 为了能够在后续操作中保持 orderProjectVO 数据，存入 Session 域
            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);
        }
        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount, HttpSession session) {
        // 把接收到的回报数量合并到 Session 域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);
        // 获取当前已登录用户的 id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER_NAME);
        Integer memberId = memberLoginVO.getId();
        // 查询目前的收货地址数据
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESS_LIST_NAME, list);
        }
        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        logger.debug("地址保存处理结果：" + resultEntity.getResult());
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:http://www.crowd.com/order/confirm/order/" + returnCount;
    }
}
