package com.xp.ican.service.shiroSerivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.dto.Req.user.UpdateUserReq;
import com.xp.ican.entity.shiroEntity.PermissionEntity;
import com.xp.ican.entity.shiroEntity.PermissionRoleRefEntity;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.mapper.shiroMapper.PermissionMapper;
import com.xp.ican.mapper.shiroMapper.PermissionRoleRefMapper;
import com.xp.ican.mapper.shiroMapper.UserMapper;
import com.xp.ican.service.shiroSerivce.UserService;
import com.xp.ican.utils.EncryptUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private PermissionRoleRefMapper permissionRoleRefMapper;


    @Override
    public UserEntity userLogin(String userName, String pwd) throws IcanBusinessException {
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);

        try {
            subject.login(token);
            return (UserEntity) subject.getPrincipals().getPrimaryPrincipal();

        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "登录验证异常");
        }
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public int updateUser(UpdateUserReq updateUserReq) throws IcanBusinessException {

        Long uid = Optional.ofNullable(updateUserReq).map(UpdateUserReq::getId).orElse(null);
        UserEntity user = userMapper.selectById(uid);
        if (null == user) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "无效用户ID");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setNickName(updateUserReq.getNickname());
        userEntity.setRemark(updateUserReq.getRemark());
        userEntity.setTelephone(updateUserReq.getTelephone());
        userEntity.setUserFace(updateUserReq.getUserface());
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", updateUserReq.getId());
        int updates = userMapper.update(userEntity, updateWrapper);
        return updates;
    }

    @Override
    public void userReg(String name, String pwd) throws IcanBusinessException {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (user != null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "已存在此用户");
        }
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(name);
            userEntity.setPassWord(EncryptUtil.MD5Pwd(pwd));
            userMapper.insert(userEntity);
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "注册用户异常");
        }
    }

    @Override
    public UserEntity getUserByName(String name) {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        return user;
    }


    public List<String> getUserPermissions(long userId) {
        UserEntity userEntity=userMapper.selectById(userId);
        if(userEntity==null){
            return new ArrayList<>();
        }
        List<PermissionRoleRefEntity> permissionRoleRefEntity=permissionRoleRefMapper.selectList(new QueryWrapper<PermissionRoleRefEntity>().eq("rid",userEntity.getId()));
        if (permissionRoleRefEntity.size()==0){
            return new ArrayList<>();
        }
        List<PermissionEntity>permissionEntities=new ArrayList<>();
        for(PermissionRoleRefEntity permissionRoleRefEntity1:permissionRoleRefEntity){
            PermissionEntity permissionEntity=permissionMapper.selectOne(new QueryWrapper<PermissionEntity>().eq("id",permissionRoleRefEntity1.getPid()));
            if (permissionEntity==null){
                continue;
            }
            permissionEntities.add(permissionEntity);
        }
        //返回
        List<String> rest = new ArrayList<>();
        for(PermissionEntity permission : permissionEntities){
            rest.add(permission.getPermission());
        }
        return rest;

    }


}
