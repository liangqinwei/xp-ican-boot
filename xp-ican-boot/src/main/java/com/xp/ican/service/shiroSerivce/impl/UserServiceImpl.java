package com.xp.ican.service.shiroSerivce.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.dto.Req.UpdateUserReq;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.mapper.shiroMapper.UserMapper;
import com.xp.ican.service.shiroSerivce.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


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
        UserEntity userEntity=new UserEntity();
        userEntity.setNickName(updateUserReq.getNickname());
        userEntity.setRemark(updateUserReq.getRemark());
        userEntity.setTelephone(updateUserReq.getTelephone());
        userEntity.setUserFace(updateUserReq.getUserface());
        UpdateWrapper<UserEntity> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",updateUserReq.getId());
        int updates = userMapper.update(userEntity,updateWrapper);
        return updates;
    }
}
