package com.xp.ican.controller;


import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.common.token.TokenUtil;
import com.xp.ican.dto.Req.UserReq;
import com.xp.ican.dto.Resp.UserDetailResp;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.CommonResponse;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.service.shiroSerivce.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
public class LoginRegController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResponse login(@RequestBody UserReq userReq) throws Exception {
        if (StringUtils.isBlank(userReq.getName())) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "登录用户名不能为空");
        }
        if (StringUtils.isBlank(userReq.getPwd())) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "登录密码不能为空");
        }
        try{
            UserEntity loginUser = userService.userLogin(userReq.getName(), userReq.getPwd());

            UserDetailResp userDetailResp = new UserDetailResp();
            BeanUtils.copyProperties(userDetailResp,loginUser);

            if (userDetailResp == null) {
                throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGIN_ERROR);
            }
            CommonResponse commonResponse=new CommonResponse();
            String a=TokenUtil.generateToken(userDetailResp.getUserName());
            commonResponse.setData(a);
            return commonResponse;
        }catch (Exception e){
                throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGIN_ERROR);
        }



    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void loginuser() throws Exception {
        try{
            userService.logout();
        }catch (Exception e){
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGOUT_ERROR);
        }
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String regUser(@RequestParam("id")  String id) throws Exception {
        try{
            return id;
        }catch (Exception e){
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGOUT_ERROR);
        }
    }


}
