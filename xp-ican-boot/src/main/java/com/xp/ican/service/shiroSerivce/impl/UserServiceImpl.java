package com.xp.ican.service.shiroSerivce.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xp.ican.common.CommonUtil;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.common.token.JwtUtil;
import com.xp.ican.dto.Req.user.UpdateUserReq;
import com.xp.ican.entity.shiroEntity.*;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.mapper.shiroMapper.*;
import com.xp.ican.service.shiroSerivce.UserService;
import com.xp.ican.shiro.UserAuthenticationToken;
import com.xp.ican.utils.EncryptUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleRefMapper userRoleRefMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private PermissionRoleRefMapper permissionRoleRefMapper;

    @Autowired
    @Lazy
    private JwtUtil jwtUtil;

    @Value("${jwt.period}")
    private Long period;//token有效时间（毫秒）
    @Value(("${jwt.issuer}"))
    private String issuer;//jwt token 签发人


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

    /**
     * 注册用户
     *
     * @param name
     * @param pwd
     * @throws IcanBusinessException
     */
    @Override
    public void userReg(String name, String pwd) throws IcanBusinessException {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (user != null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "已存在此用户");
        }
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(name);
            userEntity.setPassWord(EncryptUtil.md5(pwd));
            userMapper.insert(userEntity);
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "注册用户异常");
        }
    }

    /**
     * 根据用户名，返回用户信息
     *
     * @param name
     * @return
     */
    @Override
    public UserEntity getUserByName(String name) {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        return user;
    }

    /**
     * 根据用ID，返回用户角色信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> getUserRolesByUid(long userId) {
        List<UserRoleRefEntity> userRoleRefEntity = userRoleRefMapper.selectList(new QueryWrapper<UserRoleRefEntity>().eq("uid", userId));
        List<Long> roles = new ArrayList<>();
        for (UserRoleRefEntity user : userRoleRefEntity) {
            roles.add(user.getRid());
        }
        return roles.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据用名，返回用户角色信息
     *
     * @param name
     * @return
     */
    @Override
    public List<Long> getUserRolesByName(String name) {
        UserEntity userEntity = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (userEntity == null) {
            return new ArrayList<>();
        }
        List<UserRoleRefEntity> userRoleRefEntity = userRoleRefMapper.selectList(new QueryWrapper<UserRoleRefEntity>().eq("uid", userEntity.getId()));
        List<Long> roles = new ArrayList<>();
        for (UserRoleRefEntity user : userRoleRefEntity) {
            roles.add(user.getRid());
        }
        return roles.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 根据用户ID，返回用户权限信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserPermissions(long userId) {
        List<UserRoleRefEntity> userRoleRefEntitys = userRoleRefMapper.selectList(new QueryWrapper<UserRoleRefEntity>().eq("uid", userId));
        if (userRoleRefEntitys == null) {
            return new ArrayList<>();
        }
        List<PermissionRoleRefEntity> permissionRoleRefEntity = new ArrayList<>();
        for (UserRoleRefEntity userRoleRefEntity : userRoleRefEntitys) {
            permissionRoleRefEntity.addAll(permissionRoleRefMapper.selectList(new QueryWrapper<PermissionRoleRefEntity>().eq("rid", userRoleRefEntity.getRid())));
        }
        if (permissionRoleRefEntity.size() == 0) {
            return new ArrayList<>();
        }
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        for (PermissionRoleRefEntity permissionRoleRefEntity1 : permissionRoleRefEntity) {
            PermissionEntity permissionEntity = permissionMapper.selectOne(new QueryWrapper<PermissionEntity>().eq("id", permissionRoleRefEntity1.getPid()));
            if (permissionEntity == null) {
                continue;
            }
            permissionEntities.add(permissionEntity);
        }


        //返回
        List<String> rest = new ArrayList<>();
        for (PermissionEntity permission : permissionEntities) {
            rest.add(permission.getPermission());
        }

        return rest.stream().distinct().collect(Collectors.toList());

    }

    /**
     * 返回用户登录token
     *
     * @param name
     * @param pwd
     * @return
     * @throws IcanBusinessException
     */
    public String getLoginToken(String name, String pwd) throws IcanBusinessException {
        // 使用 Shiro 进行登录
        UserAuthenticationToken token;
        try {
            Subject subject = SecurityUtils.getSubject();
            token = new UserAuthenticationToken(name, pwd);
            subject.login(token);
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_AUTHOR_IDENTITY_ERROR, "验证用户信息失败");
        }
        if (token == null) {
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_AUTHOR_IDENTITY_ERROR, "验证用户信息失败");
        }
        // 登录成功后，获取userid，查询该用户拥有的权限
        List<String> permissions = getUserPermissions(token.getUserId());

        // 制作JWT Token
        String jwtToken = jwtUtil.issueJWT(
                CommonUtil.getRandomString(20),//令牌id，必须为整个系统唯一id
                token.getName(),//用户name
                (issuer == null ? JwtUtil.DEFAULT_ISSUER : issuer),//签发人，可随便定义
                null,//访问角色
                JSONArray.toJSONString(permissions),//用户权限集合，json格式
                (period == null ? JwtUtil.DEFAULT_PERIOD : period),//token有效时间
                SignatureAlgorithm.HS512//签名算法
        );

        //token存入 response里的Header
        return jwtToken;
    }


    /**
     * 增加用户角色
     *
     * @param name
     * @param roles
     * @throws IcanBusinessException
     */
    @Override
    public void addUserRole(String name, List<Long> roles) throws IcanBusinessException {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (user == null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "用户不存在");
        }
        try {
            for (Long rid : roles) {
                UserRoleRefEntity userRoleRefEntity = userRoleRefMapper.selectOne(new QueryWrapper<UserRoleRefEntity>().eq("uid", user.getId()).eq("rid", rid));
                if (userRoleRefEntity == null) {
                    RoleEntity roleEntity = roleMapper.selectOne(new QueryWrapper<RoleEntity>().eq("id", rid));
                    if (roleEntity == null) {
                        continue;
                    }
                    UserRoleRefEntity uRole = new UserRoleRefEntity();
                    uRole.setRid(rid);
                    uRole.setUid(user.getId());
                    userRoleRefMapper.insert(uRole);
                }
            }
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "增加用户角色失败");
        }

    }

    /**
     * 删除用户角色
     *
     * @param name
     * @param roles
     * @throws IcanBusinessException
     */
    @Override
    public void deleteUserRole(String name, List<Long> roles) throws IcanBusinessException {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (user == null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "用户不存在");
        }
        try {
            for (Long rid : roles) {
                UserRoleRefEntity userRoleRefEntity = userRoleRefMapper.selectOne(new QueryWrapper<UserRoleRefEntity>().eq("uid", user.getId()).eq("rid", rid));
                if (userRoleRefEntity == null) {
                    continue;
                }
                userRoleRefMapper.deleteById(userRoleRefEntity.getId());
            }
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "删除用户角色失败");
        }

    }

    /**
     * 更新用户角色
     *
     * @param name
     * @param roles
     * @throws IcanBusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(String name, List<Long> roles) throws IcanBusinessException {
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (user == null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "用户不存在");
        }
        List<UserRoleRefEntity> userRoleRefEntitys = userRoleRefMapper.selectList(new QueryWrapper<UserRoleRefEntity>().eq("uid", user.getId()));
        List<Long> newRoles = new ArrayList<>();
        label1:
        for (int i = 0; i < userRoleRefEntitys.size(); i++) {
            label2:
            for (int j = 0; j < roles.size(); j++) {
                if (userRoleRefEntitys.get(i).equals(roles.get(j))) {
                    break label2;
                }
            }
            newRoles.add(userRoleRefEntitys.get(i).getId());
        }
        userRoleRefMapper.deleteBatchIds(newRoles);
        try {
            for (Long rid : roles) {
                UserRoleRefEntity userRoleRefEntity = userRoleRefMapper.selectOne(new QueryWrapper<UserRoleRefEntity>().eq("uid", user.getId()).eq("rid", rid));
                if (userRoleRefEntity == null) {
                    RoleEntity roleEntity = roleMapper.selectOne(new QueryWrapper<RoleEntity>().eq("id", rid));
                    if (roleEntity == null) {
                        continue;
                    }
                    UserRoleRefEntity uRole = new UserRoleRefEntity();
                    uRole.setRid(rid);
                    uRole.setUid(user.getId());
                    userRoleRefMapper.insert(uRole);
                }

            }
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "删除用户角色失败");
        }

    }

    /**
     * 更新角色权限
     *
     * @param rid
     * @param perms
     * @throws IcanBusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPerms(Long rid, List<Long> perms) throws IcanBusinessException {
        List<PermissionRoleRefEntity> permisList = permissionRoleRefMapper.selectList(new QueryWrapper<PermissionRoleRefEntity>().eq("rid", rid));
        if (permisList == null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "未存在此角色ID");
        }
        List<Long> newRoles = new ArrayList<>();
        label1:
        for (int i = 0; i < permisList.size(); i++) {
            label2:
            for (int j = 0; j < perms.size(); j++) {
                if (permisList.get(i).equals(perms.get(j))) {
                    break label2;
                }
            }
            newRoles.add(permisList.get(i).getId());
        }
        permissionRoleRefMapper.deleteBatchIds(newRoles);
        for (Long pid : perms) {
            PermissionRoleRefEntity permisEntity = permissionRoleRefMapper.selectOne(new QueryWrapper<PermissionRoleRefEntity>().eq("rid", rid).eq("pid", pid));
            if (permisEntity == null) {
                PermissionEntity permissionEntity = permissionMapper.selectOne(new QueryWrapper<PermissionEntity>().eq("id", pid));
                if (permissionEntity == null) {
                    continue;
                }
                PermissionRoleRefEntity uPerma = new PermissionRoleRefEntity();
                uPerma.setRid(rid);
                uPerma.setPid(pid);
                permissionRoleRefMapper.insert(uPerma);
            }
        }
    }


    @Override
    public void addUserPerms(Long rid, List<Long> perms) throws IcanBusinessException {
        for(Long pid:perms){
            PermissionRoleRefEntity permisList = permissionRoleRefMapper.selectOne(new QueryWrapper<PermissionRoleRefEntity>().eq("rid", rid).eq("pid",pid));
            if (permisList==null){
                PermissionRoleRefEntity uPerma = new PermissionRoleRefEntity();
                uPerma.setRid(rid);
                uPerma.setPid(pid);
                permissionRoleRefMapper.insert(uPerma);
            }
        }
    }

    @Override
    public void deleteUserPerms(Long rid, List<Long> perms) throws IcanBusinessException {
        for(Long pid:perms){
            PermissionRoleRefEntity permisList = permissionRoleRefMapper.selectOne(new QueryWrapper<PermissionRoleRefEntity>().eq("rid", rid).eq("pid",pid));
            if (permisList==null){
                continue;
            }
            permissionRoleRefMapper.deleteById(permisList.getId());
        }
    }


}
