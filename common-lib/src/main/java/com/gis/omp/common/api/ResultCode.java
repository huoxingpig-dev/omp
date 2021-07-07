package com.gis.omp.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful", "成功"),

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception", "坏请求"),

    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "request Unauthorized", "请求未受权限"),

    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 Not Found", "404"),

    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "Message Can't be Read", ""),

    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Supported", ""),

    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Media Type Not Supported", ""),

    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "request Rejected", ""),

    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", ""),

    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Required Parameter", ""),

    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Mismatch", ""),

    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error", ""),

    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Validation Error", ""),

    NO_PERMISSIONS(HttpServletResponse.SC_BAD_REQUEST, "request not permitted", "权限不足"),

    // user
    USER_EXIST(20401, "该用户名已经存在", "该用户名已经存在"),
    USER_PWD_NULL(20402, "密码不能为空", "密码不能为空"),
    USER_INEQUALITY(20403, "两次密码不一致", "两次密码不一致"),
    USER_OLD_PWD_ERROR(20404, "原来密码不正确", "原来密码不正确"),
    USER_NAME_PWD_NULL(20405, "用户名和密码不能为空", "用户名和密码不能为空"),
    USER_CAPTCHA_ERROR(20406, "验证码错误", "验证码错误"),

    // role
    ROLE_EXIST(21401, "该角色标识已经存在，不允许重复！", "该角色标识已经存在，不允许重复！"),

    // menu
    NO_ADMINROLEMENU_STATUS(22501, "超级管理员拥有所有菜单权限，不允许修改", "超级管理员拥有所有菜单权限，不允许修改"),
    UNAUTHORIZED(402, "暂未登录或token已经过期", "暂未登录或token已经过期"),
    MENU_EXIST_USEDCHILD(401, "包含已被占用子菜单，无法删除", "包含已被占用子菜单，无法删除"),
    MENU_USED(401, "菜单已被占用，禁止删除", "菜单已被占用，禁止删除"),

    // authority
    AUTHORITY_EXIST(23401, "该权限名已经存在", "该权限名已经存在"),

    STATUS_ERROR(401, "非法操作：状态有误", "非法操作：状态有误")
    ;

    private final int code;
    private final String enMsg;
    private final String zhMsg;

    public String getMessage() {
        if ( Locale.SIMPLIFIED_CHINESE.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage())) {
            return this.zhMsg;
        } else {
            return this.enMsg;
        }
    }
}

