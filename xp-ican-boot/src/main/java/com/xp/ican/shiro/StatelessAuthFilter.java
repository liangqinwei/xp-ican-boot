package com.xp.ican.shiro;

import com.xp.ican.common.token.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class StatelessAuthFilter extends AccessControlFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("token");
        String username = request.getHeader("username");
        if (StringUtils.isBlank(token) || StringUtils.isBlank(username)) {
            return false;
        }
        if(TokenUtil.validateToken(token,username)){
            UsernamePasswordToken statelessToken = new UsernamePasswordToken(token,token);
            try{
                getSubject(request,servletResponse).login(statelessToken);
                return true;
            }catch (Exception e){
                return false;
            }
        }

        redirectToLogin(request,servletResponse);
        return false;
    }
}


