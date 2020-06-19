package com.crowd.service.impl;

import com.crowd.entity.po.AddressPO;
import com.crowd.entity.po.AddressPOExample;
import com.crowd.entity.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crowd.entity.vo.OrderProjectVO;
import com.crowd.mapper.AddressPOMapper;
import com.crowd.mapper.OrderPOMapper;
import com.crowd.mapper.OrderProjectPOMapper;
import com.crowd.service.api.OrderService;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {

        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);

        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);
        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }

        return addressVOList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);

        addressPOMapper.insert(addressPO);
    }
}
