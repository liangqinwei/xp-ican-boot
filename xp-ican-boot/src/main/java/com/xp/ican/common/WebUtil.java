package com.xp.ican.common;

import com.xp.ican.common.constants.RequestMappingConst;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class WebUtil {
    /**
     * 检查url是否需要登录验证
     * @param url
     * @return false 不需要登录即可访问
     *         true  需要登录才可以访问
     */
    public static boolean needLogin(String url){

        if(url.indexOf(RequestMappingConst.V_CODE) >= 0 || //验证码
                url.indexOf(RequestMappingConst.LOGIN) >= 0){//登录
            return false;
        }

        return true;
    }

    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0,index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }


    /**
     * 检查请求是否为登录请求
     * @param request
     * @return
     */
    public static boolean isLoginRequest(HttpServletRequest request) {
        if(request.getRequestURI().indexOf(RequestMappingConst.LOGIN) >= 0){
            return true;
        }
        return false;
    }

    /**
     * 检查请求是否为注销请求
     * @param request
     * @return
     */
    public static boolean isLogoutRequest(HttpServletRequest request) {
        if(request.getRequestURI().indexOf(RequestMappingConst.LOGOUT) >= 0){
            return true;
        }
        return false;
    }

    /**
     * 检查请求是否为公共请求
     * @param request
     * @return
     */
    public static boolean isPublicRequest(HttpServletRequest request) {
//        if(request.getRequestURI().indexOf(RequestMappingConst.BASIC_URL_PUBLIC) >= 0){
//            return true;
//        }
//        return false;
        return isContainAuthUrl(RequestMappingConst.BASIC_URL_PUBLIC,request);
    }

    public static boolean isContainAuthUrl(String url,HttpServletRequest request){
        String str="/"+request.getRequestURI().split("/")[1];
        if(str.equals(url)&&request.getRequestURI().startsWith(url)&&request.getRequestURI().indexOf(url)>=0){
            return true;
        }
        return false;
    }

    /**
     * 输出json字符串到 HttpServletResponse
     * @param response
     * @param str : 字符串
     */
    public static void writeJSONToResponse(HttpServletResponse response, String str){
        PrintWriter jsonOut = null;
        response.setContentType("application/json;charset=UTF-8");
        try {
            jsonOut = response.getWriter();
            jsonOut.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(jsonOut != null){
                jsonOut.close();
            }
        }
    }
}
