package com.xp.ican.service.shiroSerivce;

import com.xp.ican.dto.Req.user.UpdateUserReq;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.IcanBusinessException;

import java.util.List;

public interface UserService {

    UserEntity userLogin(String userName, String pwd) throws IcanBusinessException;

    void logout();

    int updateUser(UpdateUserReq updateUserReq) throws IcanBusinessException;

    void userReg(String user,String pwd) throws IcanBusinessException;

    UserEntity getUserByName(String name);

    List<String> getUserPermissions(long userId);

}
