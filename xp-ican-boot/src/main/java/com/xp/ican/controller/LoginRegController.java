package com.xp.ican.controller;


import com.xp.ican.common.constants.RequestMappingConst;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.common.constants.WebConst;
import com.xp.ican.common.token.JwtUtil;
import com.xp.ican.dto.Req.user.UserReq;
import com.xp.ican.exception.CommonResponse;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.service.shiroSerivce.UserService;
import com.xp.ican.utils.EncryptUtil;
import com.xp.ican.utils.MyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@RestController
public class LoginRegController {




    @Resource
    private UserService userService;


    @Resource
    private HttpServletResponse response;


    @RequestMapping(value = RequestMappingConst.LOGOUT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void logoutUser() throws Exception {
        try {
            userService.logout();
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGOUT_ERROR);
        }
    }

    @RequestMapping(value = RequestMappingConst.RegUser, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void regUser(@RequestBody UserReq userReq) throws Exception {
        try {
            boolean nameMatcher = MyValidator.isValidate(userReq.getName().trim(), WebConst.NAMEPATTERN);
            boolean pwdMatcher = MyValidator.isValidate(userReq.getPwd().trim(), WebConst.PWDPATTERN);
            if (!nameMatcher) {
                throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "请输入正确邮箱帐号");
            }
            if (!pwdMatcher) {
                throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "请输入6-12位数字字母组合密码");
            }

            userService.userReg(userReq.getName().trim(), userReq.getPwd().trim());
            String jwtToken = userService.getLoginToken(userReq.getName().trim(), userReq.getPwd().trim());
            //token存入 response里的Header
            response.setHeader(WebConst.TOKEN, jwtToken);

        } catch (IcanBusinessException e) {
            throw new IcanBusinessException(e.getMessageCode(), e.getMessage());
        }catch (Exception e){
            throw  new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"注册异常");
        }
    }

    /**
     * 用户登录
     * 验证码校验和请求参数校验功能已去除，完整版参考Demo
     *
     * @return
     */
    @RequestMapping(value = RequestMappingConst.LOGIN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResponse login(@RequestBody UserReq userReq) throws Exception {

        boolean nameMatcher = MyValidator.isValidate(userReq.getName().trim(), WebConst.NAMEPATTERN);
        boolean pwdMatcher = MyValidator.isValidate(userReq.getPwd().trim(), WebConst.PWDPATTERN);
        if (!nameMatcher) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "请输入正确邮箱帐号");
        }
        if (!pwdMatcher) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "请输入6-12位数字字母组合密码");
        }

        String jwtToken = userService.getLoginToken(userReq.getName().trim(), userReq.getPwd().trim());
        //token存入 response里的Header
        response.setHeader(WebConst.TOKEN, jwtToken);

        // 返回Message的json
        CommonResponse message = new CommonResponse();
        message.setMsg("登录成功，token已存入header");
        message.setData(userReq.getName());
        return message;
    }

    /**
     * 用户登录
     * 验证码校验和请求参数校验功能已去除，完整版参考Demo
     *
     * @return
     */
    @RequestMapping(value = RequestMappingConst.P_PWD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResponse getUserPwd(@RequestBody UserReq userReq) {

        // 制作JWT Token
        String token = EncryptUtil.md5(userReq.getPwd().trim());

        // 返回Message的json
        CommonResponse message = new CommonResponse();
        message.setData(token);
        return message;
    }


}
