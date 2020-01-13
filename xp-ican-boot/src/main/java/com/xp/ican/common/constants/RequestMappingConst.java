package com.xp.ican.common.constants;

public class RequestMappingConst {
    // ------  大类url ------
    public static final String BASIC_URL_PUBLIC = "/public";//公用接口的url
    public static final String BASIC_URL_USER = "/user";//用户接口的url

    // ------  公用接口 ------
    public static final String V_CODE = BASIC_URL_PUBLIC + "/verifycode";// 验证码接口
    public static final String T_LOGIN = BASIC_URL_PUBLIC + "/getToken";

    // ------  用户接口 ------
    public static final String LOGIN = BASIC_URL_USER + "/login";// 登录接口
    public static final String LOGOUT = BASIC_URL_USER + "/logout";// 注销接口
}
