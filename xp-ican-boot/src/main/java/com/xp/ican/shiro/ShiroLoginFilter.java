package com.xp.ican.shiro;

import com.alibaba.fastjson.JSON;
import com.xp.ican.common.CommonUtil;
import com.xp.ican.common.WebUtil;
import com.xp.ican.common.constants.WebConst;
import com.xp.ican.common.token.JwtToken;
import com.xp.ican.common.token.JwtUtil;
import com.xp.ican.exception.CommonResponse;
import com.xp.ican.exception.IcanBusinessException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroLoginFilter extends AccessControlFilter {

    @Autowired
    @Lazy
    private JwtUtil jwtUtil;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 判断请求是否是公共请求，通过请求的url判断
        if (WebUtil.isPublicRequest((HttpServletRequest) request)) {
            return true;
        }
        return false;//  拒绝，统一交给 onAccessDenied 处理
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // ========== 判断是否是登录请求，是就放行，登录处理放在了controller层 ==========
        if (WebUtil.isLoginRequest(httpServletRequest)) {
            return true;
        }
        // ========== 其他请求，都需要验证 ==========

        //验证是否登录（检查json token）
        if (CommonUtil.isBlank(httpServletRequest.getHeader(WebConst.TOKEN))) {
            // 返回JSON给请求方
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMsg("[" + WebConst.TOKEN + "] 不能为空，请将token存入header");
            WebUtil.writeJSONToResponse(httpServletResponse, JSON.toJSONString(commonResponse));
            return false;
        }
        String token = httpServletRequest.getHeader(WebConst.TOKEN);
        JwtToken jwtToken;
        try {
            jwtToken = jwtUtil.parseJwt(token);
        } catch (IcanBusinessException re) {//出现异常，说明验证失败
            CommonResponse message = new CommonResponse();
            message.setMsg("验证失败");
            WebUtil.writeJSONToResponse((HttpServletResponse) response, JSON.toJSONString(message));//返回json
            return false;
        }
        if (jwtToken.getIsFlushed()) {//需要刷新token
            httpServletResponse.setHeader(WebConst.TOKEN, jwtToken.getToken());// 更新response
        }

        // 检查用户是否具备权限
        if (!jwtToken.hasUrl(((HttpServletRequest) request).getRequestURI())) {
            CommonResponse message = new CommonResponse();
            message.setMsg("验证失败");
            WebUtil.writeJSONToResponse((HttpServletResponse) response, JSON.toJSONString(message
            ));
            return false;
        } else {//登录验证通过
            return true;
        }
    }
}
