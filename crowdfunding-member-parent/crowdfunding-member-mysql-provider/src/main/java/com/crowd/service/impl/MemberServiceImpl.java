package com.crowd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crowd.entity.po.MemberPO;
import com.crowd.entity.po.MemberPOExample;
import com.crowd.entity.po.MemberPOExample.Criteria;
import com.crowd.mapper.MemberPOMapper;
import com.crowd.service.api.MemberService;

// 在类上使用 @Transactional(readOnly = true) 针对查询操作设置事务属性
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {
        // 创建 Example 对象
        MemberPOExample example = new MemberPOExample();
        // 创建 Criteria 对象
        Criteria criteria = example.createCriteria();
        // 封装查询条件
        criteria.andLoginacctEqualTo(loginacct);
        // 执行查询
        List<MemberPO> list = memberPOMapper.selectByExample(example);
        // 获取结果，账户唯一，只能查到一个
        return list.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class,
            readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}