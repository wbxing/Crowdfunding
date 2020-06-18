package com.crowd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowd.entity.vo.OrderProjectVO;
import com.crowd.mapper.AddressPOMapper;
import com.crowd.mapper.OrderPOMapper;
import com.crowd.mapper.OrderProjectPOMapper;
import com.crowd.service.api.OrderService;

@Transactional(readOnly = true)
@Service
public class OderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;
    @Autowired
    private OrderPOMapper orderPOMapper;
    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }
}
