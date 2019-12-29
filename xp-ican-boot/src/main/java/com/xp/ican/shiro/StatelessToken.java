package com.xp.ican.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {

    private String userName;

    private String token;

    public StatelessToken(String userName,String token){
        this.userName=userName;
        this.token=token;
    }

    @Override
    public Object getPrincipal() {
        return userName;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
