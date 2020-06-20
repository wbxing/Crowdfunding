package com.crowd.constant;

public class CrowdConstant {

    public static final String MESSAGE_LOGIN_FAILED = "用户名或密码错误！请重新输入";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "用户名已存在";
    public static final String MESSAGE_ACCESS_FORBIDDEN = "请登录后访问";
    public static final String MESSAGE_STRING_INVALIDATE = "输入数据不合法，请重新输入";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统错误：登录账号不唯一";
    public static final Object MESSAGE_CODE_NOT_EXISTS = "验证码已过期，请重新获取";
    public static final Object MESSAGE_CODE_INVALID = "验证码错误";
    public static final String MESSAGE_HEADER_PIC_EMPTY = "头图不可为空！";
    public static final String MESSAGE_HEADER_PIC_UPLOAD_FAILED = "头图上传失败！";
    public static final String MESSAGE_DETAIL_PIC_EMPTY = "详情图片不可为空！";
    public static final String MESSAGE_DETAIL_PIC_UPLOAD_FAILED = "详情图片上传失败！";
    public static final String MESSAGE_TEMPLE_PROJECT_MISSING = "临时存储的Project对象丢失！";

    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ACCT = "loginAcct";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_ADMIN_NAME = "admin";
    public static final String ATTR_NAME_MEMBER_NAME = "member";
    public static final String ATTR_NAME_ASSIGNED_ROLE_NAME = "assignedRoleList";
    public static final String ATTR_NAME_UNASSIGNED_ROLE_NAME = "unassignedRoleList";
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String ATTR_NAME_TEMPLE_PROJECT = "tempProject";
    public static final String ATTR_NAME_PORTAL_DATA = "portal_data";
    public static final String ATTR_NAME_DETAIL_PROJECT = "detailProjectVO";
    public static final String ATTR_NAME_ORDER_PROJECT = "orderProjectVO";
    public static final String ATTR_NAME_ADDRESS_LIST_NAME = "addressVOList";
    public static final String ATTR_NAME_ORDER_NAME = "orderVO";

    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX_";
}
