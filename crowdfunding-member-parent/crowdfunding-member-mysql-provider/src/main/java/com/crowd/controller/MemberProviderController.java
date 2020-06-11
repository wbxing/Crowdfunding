package com.crowd.controller;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.po.MemberPO;
import com.crowd.service.api.MemberService;
import com.crowd.utils.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberProviderController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(
            @RequestParam("loginacct") String loginacct) {
        try {
            // 调用本地Service 完成查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);
            // 如果没有抛异常，那么就返回成功的结果
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果捕获到异常则返回失败的结果
            return ResultEntity.failed(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {
        try {
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }
}
