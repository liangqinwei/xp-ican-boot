package com.xp.ican.service.shiroSerivce;

import com.xp.ican.dto.Req.user.UpdateUserReq;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.IcanBusinessException;

import java.util.List;

public interface UserService {


    void logout();

    int updateUser(UpdateUserReq updateUserReq) throws IcanBusinessException;

    void userReg(String user,String pwd) throws IcanBusinessException;

    UserEntity getUserByName(String name);

    List<String> getUserPermissions(long userId);

    String getLoginToken(String name,String pwd) throws IcanBusinessException;

    List<Long> getUserRolesByUid(long userId);

    List<Long> getUserRolesByName(String name);

    void addUserRole(String name,List<Long> roles) throws IcanBusinessException;

    void deleteUserRole(String name,List<Long> roles) throws IcanBusinessException;

    void updateUserRole(String name, List<Long> roles) throws IcanBusinessException;

    void updateUserPerms(Long rid, List<Long> perms) throws IcanBusinessException;

    void addUserPerms(Long rid, List<Long> perms) throws IcanBusinessException;

    void deleteUserPerms(Long rid, List<Long> perms) throws IcanBusinessException;

}
