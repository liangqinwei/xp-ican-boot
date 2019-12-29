package com.xp.ican.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xp.ican.common.token.TokenUtil;
import com.xp.ican.entity.shiroEntity.*;
import com.xp.ican.mapper.shiroMapper.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class AuthRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;


    @Resource
    private UserRoleRefMapper userRoleRefMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionRoleRefMapper permissionRoleRefMapper;

    @Resource
    private PermissionMapper permissionMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        UserEntity user = (UserEntity) principalCollection.getPrimaryPrincipal();
        List<Long> roleLists = new ArrayList<>();
//        获取用户角色
        userRoleRefMapper.selectList(new QueryWrapper<UserRoleRefEntity>().eq("uid", user.getId())).forEach(urRef -> {
            roleLists.add(urRef.getRid());
        });
        List<RoleEntity> roles = roleMapper.selectList(new QueryWrapper<RoleEntity>().lambda().in(RoleEntity::getId, roleLists));

        List<PermissionRoleRefEntity> permissionRoleRefs = permissionRoleRefMapper.selectList(new QueryWrapper<PermissionRoleRefEntity>().lambda().in(PermissionRoleRefEntity::getRid, roleLists));

        List<PermissionEntity> permissionLists = permissionMapper.selectList(new QueryWrapper<PermissionEntity>().lambda().in(PermissionEntity::getId, permissionRoleRefs));

        for (RoleEntity role : roles) {
            simpleAuthorizationInfo.addRole(role.getRole());
        }

        for (PermissionEntity permission : permissionLists) {
            simpleAuthorizationInfo.addStringPermission(permission.getPermission());
        }

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String name= (String) authenticationToken.getPrincipal();

        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (user == null) {
            return null;
        }
        if (user.getIsDelete() == 1) {
            return null;
        }
//        String generateToken = TokenUtil.generateToken(name);

        return new SimpleAuthenticationInfo(user, user.getPassWord(), this.getClass().getName());

    }
}
