package com.xp.ican.shiro;

import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.utils.EncryptUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

public class PasswordCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        if (authenticationToken instanceof UserAuthenticationToken){
            if(authenticationInfo.getPrincipals().getPrimaryPrincipal() instanceof UserEntity){
                UserEntity userEntity= (UserEntity) authenticationInfo.getPrincipals().getPrimaryPrincipal();
                if(authenticationToken.getPrincipal().equals(userEntity.getUserName())&&EncryptUtil.md5((String)authenticationToken.getCredentials()).equals(userEntity.getPassWord())){
                    return true;
                }
            }
        }

        return false;
    }
}
