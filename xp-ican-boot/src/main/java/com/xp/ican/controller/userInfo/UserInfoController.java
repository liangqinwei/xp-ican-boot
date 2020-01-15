package com.xp.ican.controller.userInfo;

import com.xp.ican.common.constants.RequestMappingConst;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.common.token.JwtUtil;
import com.xp.ican.dto.Resp.user.UserDetailExtResp;
import com.xp.ican.dto.Resp.user.UserDetailResp;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.service.shiroSerivce.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserInfoController {

    @Autowired
    @Lazy
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @Resource
    private HttpServletRequest request;

    @RequestMapping(value = RequestMappingConst.USERINFO, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDetailResp getUserInfo(@RequestParam(value = "name",required = false) String name) throws IcanBusinessException {
        String userName = name;
        UserDetailResp userDetailResp = new UserDetailResp();
        if (userName == null) {
            String token = request.getHeader("token");
            Claims claims;
            try {
                claims = jwtUtil.parseJwt(token);
            } catch (Exception e) {
                throw new IcanBusinessException(ResponseCodeNum.RESPONSE_PARSE_TOKEN_ERROR);
            }
            userName = claims.getSubject();
        }
        if (userName == null) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "用户名不存在");
        }
        UserEntity userEntity = userService.getUserByName(userName);
        BeanUtils.copyProperties(userEntity, userDetailResp);

        return userDetailResp;
    }


    @RequestMapping(value = RequestMappingConst.USERINFOEXT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDetailExtResp getUserInfoExt(@RequestParam(value = "name",required = false) String name) throws IcanBusinessException {
        UserDetailExtResp userDetailExtResp = new UserDetailExtResp();
        String userName = name;
        if (userName == null) {
            String token = request.getHeader("token");
            Claims claims;
            try {
                claims = jwtUtil.parseJwt(token);
            } catch (Exception e) {
                throw new IcanBusinessException(ResponseCodeNum.RESPONSE_PARSE_TOKEN_ERROR);
            }
            userName = claims.getSubject();
            if (userName == null) {
                throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "用户名不存在");
            }
        }
        UserEntity userEntity = userService.getUserByName(userName);
        if (userEntity == null) {
            return userDetailExtResp;
        }
        BeanUtils.copyProperties(userEntity, userDetailExtResp);
        List<Long> roles = userService.getUserRolesByUid(userEntity.getId());
        List<String> permissions = userService.getUserPermissions(userEntity.getId());
        userDetailExtResp.setRoles(roles);
        userDetailExtResp.setPermissions(permissions);

        return userDetailExtResp;
    }
}
