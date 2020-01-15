package com.xp.ican.common.token;

import lombok.Data;

import java.util.List;

@Data
public class JwtToken {
    private String token;//请求时携带的token
    private String name;//用户name，用户在数据库里的name
    private List<String> permissions;//用户权限集合
    private Boolean isFlushed;//token字段是否被刷新

    /**
     * 检查 Permissions 集合中是否有参数url
     * 有说明用户拥有访问参数url的权限
     * @param url
     * @return
     */
    public boolean hasUrl(String url){
        if(permissions == null || permissions.size() == 0){
            return false;
        }
        for (String permission : permissions){
            if(url.endsWith(permission)){
                return true;
            }
        }
        return false;
    }
}
