package com.crowd.utils;

/**
 * 统一项目中 Ajax 请求返回的数据类型
 *
 * @param <T> 泛型
 */
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    // 用来封装当前请求处理的结果是成功还是失败
    private String result;
    // 请求处理失败是返回的错误信息
    private String message;
    // 要返回的数据
    private T data;

    /**
     * 请求处理成功，不需要返回数据
     *
     * @param <E> 泛型
     * @return 返回没有数据的 ResultEntity
     */
    public static <E> ResultEntity<E> successWithoutData() {
        return new ResultEntity<E>(SUCCESS, null, null);
    }

    /**
     * 请求处理成功，需要返回数据
     *
     * @param <E>  泛型
     * @param data 需要返回的数据
     * @return 返回带数据的 ResultEntity
     */
    public static <E> ResultEntity<E> successWithData(E data) {
        return new ResultEntity<E>(SUCCESS, null, data);
    }

    /**
     * 请求处理成功
     *
     * @param <E>     泛型
     * @param message 请求处理失败是返回的错误信息
     * @return 返回失败对应的 ResultEntity
     */
    public static <E> ResultEntity<E> failed(String message) {
        return new ResultEntity<E>(FAILED, message, null);
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
