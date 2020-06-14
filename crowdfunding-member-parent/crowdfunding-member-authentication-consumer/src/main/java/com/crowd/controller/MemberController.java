package com.crowd.controller;

import com.crowd.api.MySQLRemoteService;
import com.crowd.api.RedisRemoteService;
import com.crowd.config.ShortMessageProperties;
import com.crowd.constant.CrowdConstant;
import com.crowd.entity.po.MemberPO;
import com.crowd.entity.vo.MemberLoginVO;
import com.crowd.entity.vo.MemberVO;
import com.crowd.utils.CrowdUtils;
import com.crowd.utils.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberController {

    @Autowired
    private ShortMessageProperties smp;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {
        // 获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 从 Redis 中读取 验证码
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);
        // 检查查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }
        if (ResultEntity.SUCCESS.equals(result)) {
            String redisCode = resultEntity.getData();
            if (redisCode == null) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
                return "member-reg";
            }
            String formCode = memberVO.getCode();
            if (!Objects.equals(formCode, redisCode)) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
                return "member-reg";
            }
    
            // 删除 Redis 中的验证码
            redisRemoteService.removeRedisKeyRemote(key);
            // 执行密码加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            memberVO.setUserpswd(encoder.encode(memberVO.getUserpswd()));
            // 执行保存
            MemberPO memberPO = new MemberPO();
            // 复制属性
            BeanUtils.copyProperties(memberVO, memberPO);
            ResultEntity<String> savEntity = mySQLRemoteService.saveMember(memberPO);
            if (ResultEntity.FAILED.equals(savEntity.getResult())) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, savEntity.getMessage());
                return "member-reg";
            }
        }
        // 避免注册表单重复提交
        return "redirect:/auth/member/to/login/page";
    }

    @ResponseBody
    @RequestMapping("/auth/member/sent/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        // 发送验证码
        ResultEntity<String> sendShortMessageResultEntity = CrowdUtils.sendShortMessage(smp.getHost(), smp.getPath(),
                smp.getMethod(), smp.getAppcode(), phoneNum, smp.getSign(), smp.getSkin());
        // 判断发送结果
        if (ResultEntity.SUCCESS.equals(sendShortMessageResultEntity.getResult())) {
            // 发送成功，存入 Redis
            // 获取验证码
            String code = sendShortMessageResultEntity.getData();
            // 拼接前缀
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            // 存入 Redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code,
                    15, TimeUnit.MINUTES);
            // 判断存储是否成功
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendShortMessageResultEntity;
        }
    }

    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap, HttpSession session) {

        ResultEntity<MemberPO> memberEntity =
                mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        // 判断是否查询成功
        if (ResultEntity.FAILED.equals(memberEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    memberEntity.getMessage());
            return "member-login";
        }
        // 查看是否查到用户
        MemberPO member = memberEntity.getData();
        if (member == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 获取数据库中密码
        String passwordDatabase = member.getUserpswd();
        // 加密表单中的密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 比较密码，盐值随机
        boolean matches = encoder.matches(userpswd, passwordDatabase);
        if (!matches) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 存入 Session 域
        MemberLoginVO memberLoginVO = new MemberLoginVO(member.getId(),
                member.getUsername(),
                member.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_MEMBER_NAME, memberLoginVO);
        return "redirect:http://www.crowd.com/auth/member/to/center/page";
    }

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:http://www.crowd.com";
    }
}
