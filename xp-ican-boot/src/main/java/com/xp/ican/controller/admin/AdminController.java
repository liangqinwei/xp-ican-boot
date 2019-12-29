package com.xp.ican.controller.admin;

import com.xp.ican.dto.Req.UpdateUserReq;
import com.xp.ican.dto.Resp.UserDetailResp;
import com.xp.ican.service.shiroSerivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addAdmin(@RequestBody UpdateUserReq updateUserReq) throws Exception{
//        if (updateUserReq.getId())
        userService.updateUser(updateUserReq);


    }



}
