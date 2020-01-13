package com.xp.ican.shiro;

import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.service.shiroSerivce.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userRealm")
public class UserRealm extends AuthorizingRealm {

    private static final String DEFAULT_JWT_SALT = "asdfh2738yWsdjDfha";//默认的盐

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String account=(String)authenticationToken.getPrincipal();
        UserEntity userEntity=userService.getUserByName(account);
        if(userEntity==null){
            return null;
        }
        ((UserAuthenticationToken)authenticationToken).setUserId(userEntity.getId());

        return new SimpleAuthenticationInfo( userEntity,userEntity.getPassWord().toCharArray(), ByteSource.Util.bytes(DEFAULT_JWT_SALT),getName());
    }
}
