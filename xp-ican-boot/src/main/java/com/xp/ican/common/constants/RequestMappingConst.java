package com.xp.ican.common.constants;

public class RequestMappingConst {

    // ------  大类url ------
    public static final String BASIC_URL_PUBLIC = "/public";//公用接口的url
    public static final String BASIC_URL_USER = "/user";//用户接口的url
    public static final String BASIC_URL_ADMIN = "/admin";//用户接口的url

    // ------  公用接口 ------
    public static final String V_CODE = BASIC_URL_PUBLIC + "/verifycode";// 验证码接口
    public static final String P_PWD = BASIC_URL_PUBLIC + "/getPwd";   //获取用户登录密码
    public static final String LOGIN = BASIC_URL_PUBLIC + "/login";// 登录接口
    public static final String RegUser = BASIC_URL_PUBLIC + "/reg";// 登录接口

    // ------  用户接口 ------
    public static final String USERINFO = BASIC_URL_USER + "/user/info";// 用户信息接口
    public static final String USERINFOEXT = BASIC_URL_USER + "/user/info/ext";// 用户详情接口
    public static final String LOGOUT = BASIC_URL_USER + "/logout";// 注销接口

    //------管理员接口-----
    public static final String A_UPDATEUSER = BASIC_URL_ADMIN + "/user/update";// 更新用户信息接口
    public static final String A_ADDUSER_ROLE = BASIC_URL_ADMIN + "/user/role/add";// 增加用户角色信息接口
    public static final String A_DELETEUSER_ROLE = BASIC_URL_ADMIN + "/user/role/delete";// 删除用户角色信息接口
    public static final String A_UPDATEUSER_ROLE = BASIC_URL_ADMIN + "/user/role/update";// 更新用户角色信息接口
    public static final String A_UPDATEUSER_PERMS = BASIC_URL_ADMIN + "/user/perms/update";// 更新角色权限接口
    public static final String A_ADDUSER_PERMS = BASIC_URL_ADMIN + "/user/perms/add";// 增加角色权限接口
    public static final String A_DELETEUSER_PERMS = BASIC_URL_ADMIN + "/user/perms/delete";// 删除角色权限接口





}
