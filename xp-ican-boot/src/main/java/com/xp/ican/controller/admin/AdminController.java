package com.xp.ican.controller.admin;

import com.xp.ican.common.constants.RequestMappingConst;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.dto.Req.user.PermissionReq;
import com.xp.ican.dto.Req.user.RoleReq;
import com.xp.ican.dto.Req.user.UpdateUserReq;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.service.shiroSerivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 更新用户信息
     * @param updateUserReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_UPDATEUSER,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateUserInfo(@RequestBody UpdateUserReq updateUserReq) throws Exception{
        userService.updateUser(updateUserReq);
    }

    /**
     * 增加角色信息
     * @param roleReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_ADDUSER_ROLE,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addUserRole(@RequestBody RoleReq roleReq) throws Exception{
        try{
            userService.addUserRole(roleReq.getName().trim(),roleReq.getRoles());
        }catch (IcanBusinessException e){
            throw new IcanBusinessException(e.getMessageCode(),e.getMessage());
        }
    }

    /**
     * 删除角色信息
     * @param roleReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_DELETEUSER_ROLE,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteUserRole(@RequestBody RoleReq roleReq) throws Exception{
        try{
        userService.deleteUserRole(roleReq.getName().trim(),roleReq.getRoles());
        }catch (IcanBusinessException e){
            throw new IcanBusinessException(e.getMessageCode(),e.getMessage());
        }
    }

    /**
     * 更新用户角色信息
     * @param roleReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_UPDATEUSER_ROLE,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateUserRole(@RequestBody RoleReq roleReq) throws Exception{
        try{
            userService.updateUserRole(roleReq.getName().trim(),roleReq.getRoles());
        }catch (IcanBusinessException e){
            throw new IcanBusinessException(e.getMessageCode(),e.getMessage());
        }
    }

    /**
     * 更新角色权限
     * @param permissionReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_UPDATEUSER_PERMS,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateRolePermission(@RequestBody PermissionReq permissionReq) throws Exception{
        try{
            userService.updateUserPerms(permissionReq.getRid(),permissionReq.getPerms());
        }catch (IcanBusinessException e){
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"更新角色权限失败");
        }
    }

    /**
     * 更新角色权限
     * @param permissionReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_ADDUSER_PERMS,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addRolePermission(@RequestBody PermissionReq permissionReq) throws Exception{
        try{
            userService.addUserPerms(permissionReq.getRid(),permissionReq.getPerms());
        }catch (IcanBusinessException e){
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"更新角色权限失败");
        }
    }

    /**
     * 更新角色权限
     * @param permissionReq
     * @throws Exception
     */
    @RequestMapping(value = RequestMappingConst.A_ADDUSER_PERMS,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRolePermission(@RequestBody PermissionReq permissionReq) throws Exception{
        try{
            userService.deleteUserPerms(permissionReq.getRid(),permissionReq.getPerms());
        }catch (IcanBusinessException e){
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"更新角色权限失败");
        }
    }


}
