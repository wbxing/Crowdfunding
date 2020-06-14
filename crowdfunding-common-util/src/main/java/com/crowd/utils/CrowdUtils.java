package com.crowd.utils;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
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

    /**
     * 专门负责上传文件到OSS 服务器的工具方法
     *
     * @param endpoint        OSS 参数
     * @param accessKeyId     OSS 参数
     * @param accessKeySecret OSS 参数
     * @param inputStream     要上传的文件的输入流
     * @param bucketName      OSS 参数
     * @param bucketDomain    OSS 参数
     * @param originalName    要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在OSS 上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(String endpoint, String accessKeyId, String accessKeySecret,
                                                       InputStream inputStream, String bucketName,
                                                       String bucketDomain, String originalName) {
        // 创建OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在OSS 服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
            // 调用OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.failed(" 当前响应状态码 = " + statusCode
                        + " 错误消息 = " + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }
}
