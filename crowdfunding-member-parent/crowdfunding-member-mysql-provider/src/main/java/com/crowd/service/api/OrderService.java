package com.crowd.service.api;

import com.crowd.entity.po.MemberPO;
import com.crowd.entity.vo.AddressVO;
import com.crowd.entity.vo.OrderProjectVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);
}
