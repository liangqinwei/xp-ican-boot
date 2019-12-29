package com.xp.ican.shiro;

import com.xp.ican.common.token.TokenUtil;
import com.xp.ican.entity.shiroEntity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShiroFilter extends FormAuthenticationFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //这里只有返回false才会执行onAccessDenied方法,因为
        // return super.isAccessAllowed(request, response, mappedValue);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        String token = getRequestToken((HttpServletRequest) request);
        String login = ((HttpServletRequest) request).getServletPath();

        //如果为登录,就放行
        if ("/login".equals(login)) {
            return true;
        }
        if (StringUtils.isBlank(token)) {
            System.out.println("没有token");
            return false;
        }

        //从当前shiro中获得用户信息
        UserEntity user = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        //对当前ID进行SHA256加密
        String encryptionKey = TokenUtil.generateToken(user.getUserName());
        if (encryptionKey.equals(token)) {
            return true;
        } else {
            System.out.println("无效token");
        }
        return true;
    }

    private String getRequestToken(HttpServletRequest request) {
        //默认从请求头中获得token
        String token = request.getHeader("token");

        return token;
    }

}
