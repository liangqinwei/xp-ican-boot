package com.xp.ican.controller;


import com.alibaba.fastjson.JSONArray;
import com.xp.ican.common.CommonUtil;
import com.xp.ican.common.constants.RequestMappingConst;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.common.constants.WebConst;
import com.xp.ican.common.token.JwtUtil;
import com.xp.ican.common.token.TokenUtil;
import com.xp.ican.dto.Req.user.UserReq;
import com.xp.ican.dto.Resp.UserDetailResp;
import com.xp.ican.entity.shiroEntity.UserEntity;
import com.xp.ican.exception.CommonResponse;
import com.xp.ican.exception.IcanBusinessException;
import com.xp.ican.service.shiroSerivce.UserService;
import com.xp.ican.shiro.UserAuthenticationToken;
import com.xp.ican.utils.EncryptUtil;
import com.xp.ican.utils.MyValidator;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
public class LoginRegController {

    private String NAMEPATTERN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private String PWDPATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,12})$";


    @Resource
    private UserService userService;
    @Value("${jwt.period}")
    private Long period;//token有效时间（毫秒）
    @Value(("${jwt.issuer}"))
    private String issuer;//jwt token 签发人
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = "/login2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResponse login(@RequestBody UserReq userReq) throws Exception {
        if (StringUtils.isBlank(userReq.getName())) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "登录用户名不能为空");
        }
        if (StringUtils.isBlank(userReq.getPwd())) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "登录密码不能为空");
        }
        try {
            UserEntity loginUser = userService.userLogin(userReq.getName(), userReq.getPwd());

            UserDetailResp userDetailResp = new UserDetailResp();
            BeanUtils.copyProperties(userDetailResp, loginUser);

            if (userDetailResp == null) {
                throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGIN_ERROR);
            }
            CommonResponse commonResponse = new CommonResponse();
            String a = TokenUtil.generateToken(userDetailResp.getUserName());
            commonResponse.setData(a);
            return commonResponse;
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGIN_ERROR);
        }


    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void loginuser() throws Exception {
        try {
            userService.logout();
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.RESPONSE_LOGOUT_ERROR);
        }
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void regUser(@RequestBody UserReq userReq) throws Exception {
        try {
            boolean nameMatcher = MyValidator.isValidate(userReq.getName().trim(), NAMEPATTERN);
            boolean pwdMatcher = MyValidator.isValidate(userReq.getPwd().trim(), PWDPATTERN);
            if (!nameMatcher) {
                throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "请输入正确邮箱帐号");
            }
            if (!pwdMatcher) {
                throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, "请输入6-12位数字字母组合密码");
            }

            userService.userReg(userReq.getName().trim(), userReq.getPwd().trim());
        } catch (Exception e) {
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR, e.getMessage());
        }
    }

    /**
     * 用户登录
     * 验证码校验和请求参数校验功能已去除，完整版参考Demo
     *
     * @return
     */
    @RequestMapping(value = RequestMappingConst.LOGIN,method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResponse login1( @RequestBody UserReq userReq) throws Exception {

        // 使用 Shiro 进行登录
        Subject subject = SecurityUtils.getSubject();
        UserAuthenticationToken token = new UserAuthenticationToken(userReq.getName(), userReq.getPwd());
        subject.login(token);

        // 登录成功后，获取userid，查询该用户拥有的权限
        List<String> permissions = userService.getUserPermissions(token.getUserId());

        // 制作JWT Token
        String jwtToken = jwtUtil.issueJWT(
                CommonUtil.getRandomString(20),//令牌id，必须为整个系统唯一id
                token.getUserId() + "",//用户id
                (issuer == null ? JwtUtil.DEFAULT_ISSUER : issuer),//签发人，可随便定义
                null,//访问角色
                JSONArray.toJSONString(permissions),//用户权限集合，json格式
                (period == null ? JwtUtil.DEFAULT_PERIOD : period),//token有效时间
                SignatureAlgorithm.HS512//签名算法，我也不知道是啥来的
        );

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
    @RequestMapping(value = RequestMappingConst.T_LOGIN,method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResponse getLoginToken( @RequestBody UserReq userReq) throws Exception {


        // 登录成功后，获取userid，查询该用户拥有的权限
        List<String> permissions = new ArrayList<>();

        // 制作JWT Token
        String jwtToken = EncryptUtil.md5(userReq.getPwd());



        // 返回Message的json
        CommonResponse message = new CommonResponse();
        message.setMsg("登录成功");
        message.setData(jwtToken);
        return message;
    }


}
