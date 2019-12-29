package com.xp.ican.shiro;

import com.xp.ican.utils.EncryptUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CredentialMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken= (UsernamePasswordToken) token;
        String password=new String(usernamePasswordToken.getPassword());
        password=EncryptUtil.MD5Pwd(password);
        String dbPwd= (String) info.getCredentials();
        return this.equals(password,dbPwd);
    }
}
