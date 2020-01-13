package com.xp.ican.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class UserAuthenticationToken implements AuthenticationToken {

    private Long userId;
    private String name;
    private String pwd;

    @Override
    public Object getPrincipal() {
        return this.name;
    }

    @Override
    public Object getCredentials() {
        return this.pwd;
    }

    public UserAuthenticationToken(String name,String pwd){
        this.name=name;
        this.pwd=pwd;
    }
}
