//package com.xp.ican.shiro;
//
//import com.xp.ican.common.constants.ResponseCodeNum;
//import com.xp.ican.common.token.TokenUtil;
//import com.xp.ican.exception.IcanBusinessException;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//
//@Component("authShiroFilter")
//public class AuthShiroFilter extends FormAuthenticationFilter {
//
//
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        //这里只有返回false才会执行onAccessDenied方法,因为
//        // return super.isAccessAllowed(request, response, mappedValue);
//        return false;
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//
//        String token = getRequestToken((HttpServletRequest) request);
//        String path = ((HttpServletRequest) request).getServletPath();
//        String name=getRequestUsername((HttpServletRequest) request);
//
//        //如果为登录,就放行
//        if ("/login".equals(path)||"/reg".equals(path)) {
//            return true;
//        }
//        if (StringUtils.isBlank(token)) {
//            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"token缺失或不能为空");
//        }
//
//        //从当前shiro中获得用户信息
////        UserEntity user = (UserEntity) SecurityUtils.getSubject().getPrincipal();
//        //对当前ID进行SHA256加密
//        if(!TokenUtil.validateToken(token,name)){
//            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"token验证不通过或已失效");
//        }
//
//        String desName=TokenUtil.getUsernameFromToken(token);
//        if(!(name.equals(desName))){
//            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"用户信息不匹配");
//        }
//
//        return true;
//    }
//
//    private String getRequestToken(HttpServletRequest request) {
//        //默认从请求头中获得token
//        String token = request.getHeader("token");
//
//        return token;
//    }
//
//    private String getRequestUsername(HttpServletRequest request) {
//        //默认从请求头中获得token
//        String username = request.getHeader("username");
//
//        return username;
//    }
//
//}
