package com.crowd.utils;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CrowdUtils {

    /**
     * 判断当前请求是否为 Ajax 请求
     *
     * @param request 请求对象
     * @return true: Ajax 请求; false: 非  Ajax 请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        // 获取请求头信息
        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        // 判断
        // 判断 accept 内容
        boolean acceptIsJson = (accept != null && accept.length() > 0 && accept.contains("application/json"));
        // 判断 xRequestedWith 内容
        boolean xRequestedWithIsJson = (xRequestedWith != null && xRequestedWith.length() > 0
                && xRequestedWith.equals("XMLHttpRequest"));
        // 返回结果
        return acceptIsJson || xRequestedWithIsJson;
    }

    /**
     * 对明文字符串执行 MD5 加密
     *
     * @param source 明文字符串
     * @return 执行 MD5 加密之后的十六进制字符串
     */
    public static String md5(String source) {

        // 判断 source 是否有效
        if (source == null || source.length() == 0) {
            // 空字符串则抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        String algorithm = "md5";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 获取明文字符串对应的字节数组
            byte[] bytes = source.getBytes();
            // 加密
            byte[] output = messageDigest.digest(bytes);
            // 将密文按照十六进制转换为字符串
            int signum = 1;
            int radix = 16;
            return new BigInteger(signum, output).toString(radix).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求发送验证码的方法
     *
     * @param host    短信接口调用的 URL 地址
     * @param path    具体发送短信功能的地址
     * @param method  请求方式
     * @param phone   接收验证码的手机号
     * @param appcode 第三方 appCOde
     * @param sign    短信签名
     * @param skin    短信模板
     * @return 返回结果是否成功以及失败的消息：状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
     */
    public static ResultEntity<String> sendShortMessage(String host, String path,
                                                        String method, String appcode,
                                                        String phone, String sign,
                                                        String skin) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        // 生成验证码
        StringBuilder stringBuilder = new StringBuilder();
        // 6 位
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 10);
            stringBuilder.append(random);
        }
        String code = stringBuilder.toString();
        querys.put("code", code);
        querys.put("phone", phone);
        querys.put("sign", sign);
        querys.put("skin", skin);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            String reasonPhrase = statusLine.getReasonPhrase();
            if (statusCode == 200) {
                return ResultEntity.successWithData(code);
            }
            return ResultEntity.failed(reasonPhrase);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
