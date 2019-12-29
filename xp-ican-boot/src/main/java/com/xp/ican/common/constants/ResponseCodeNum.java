package com.xp.ican.common.constants;


public enum ResponseCodeNum {

    /**
     * 通用响应
     */
    RESPONSE_TEXT(100, "请把信息反馈给用户"),
    RESPONSE_LINK(101, "建议跳转至新地址"),
    RESPONSE_IMAGE(102, "建议显示图片"),
    RESPONSE_SUCCESS(200, "success"),
    RESPONSE_NOT_MODIFIED(304, "nothing change"),
    RESPONSE_NOT_AUTHORIZED(401, "授权未通过"),
    RESPONSE_FORBIDDEN(403, "禁止访问."),
    RESPONSE_NOT_FOUND(404, "请求的资源不存在"),
    RESPONSE_SERVER_ERROR(500, "服务器内部错误"),
    RESPONSE_SERVER_Validate_ERROR(50001, "服务器内部错误"),
    INVALID_PARAM(4001001, "无效参数"),
    LACK_PARAM(4001002, "缺少参数"),
    AUTH_FAILED(401, "请求未认证，跳转登录页"),

    MOBILE_NOT_REGISTER(402, "请正确填写已登记的手机号码"),

    METHOD_NOT_ALLOWED(405, "请求方法错误"),

    REQUEST_ERROR(400, "%s"),


    RESPONSE_LOGIN_ERROR(400200,"获取用户登录信息失败"),
    RESPONSE_LOGOUT_ERROR(400201,"退出登录失败"),
    ;




    private int code;

    private String message;


    ResponseCodeNum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * 根据状态码获取反馈
     *
     * @param code 状态码
     * @return 反馈体
     */
    static public ResponseCodeNum getResponseCodeNum(int code) {
        ResponseCodeNum[] responseCodeNums = values();
        for (ResponseCodeNum responseCodeNum : responseCodeNums) {
            if (responseCodeNum.getCode() == code) {
                return responseCodeNum;
            }
        }
        return null;
    }

    /**
     * 根据CODE获取消息
     *
     * @param code 状态码
     * @return 文案
     */
    static public String getMessage(int code) {
        ResponseCodeNum[] responseStatuses = values();
        for (ResponseCodeNum responseStatus : responseStatuses) {
            if (responseStatus.getCode() == code) {
                return responseStatus.getMessage();
            }
        }
        return "";
    }


    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}
