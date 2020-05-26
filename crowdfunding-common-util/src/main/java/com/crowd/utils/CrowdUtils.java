package com.crowd.utils;

import com.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
