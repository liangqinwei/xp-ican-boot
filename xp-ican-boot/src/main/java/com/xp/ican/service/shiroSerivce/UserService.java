package com.xp.ican.service.shiroSerivce;

import com.xp.ican.dto.Req.UpdateUserReq;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.IcanBusinessException;

public interface UserService {

    UserEntity userLogin(String userName, String pwd) throws IcanBusinessException;

    void logout();

    int updateUser(UpdateUserReq updateUserReq) throws IcanBusinessException;

}
