package com.spade.jdoc.controller;


import com.spade.jdoc.model.form.LoginUser;
import com.spade.jdoc.model.User;
import com.spade.jdoc.service.UserService;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.ErrorMessage;
import com.spade.jdoc.utils.ReturnMessage;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth")
@Tag(name = "LoginAPI", description = "用户Login接口")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/login")
    @Operation(summary = "login", description = "登录")
    public ReturnMessage login(@RequestBody LoginUser loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        User user = userService.findByUserName(username);
        if (user == null) {
            return ReturnMessage.error(ErrorMessage.ACCOUNT_NOT_FOUND);
        }
        if (!CommonUtils.passwordVerify(password, user.getPasswordHash())) {
            return ReturnMessage.error(ErrorMessage.ACCOUNT_ERR_PASSWORD);
        }
        if (user.getStatus() == 1) {
            return ReturnMessage.error(ErrorMessage.ACCOUNT_ERR_DISABLED);
        }
        //quert token
        var userToken = userService.getUserToken(user);
        var newToken = userService.refreshToken(userToken);
        return ReturnMessage.success().setData("token", newToken.getToken());
    }

    @GetMapping(value = "/logout")
    @Operation(summary = "logout", description = "退出")
//    @Parameters({
//            @Parameter(name = "request", description = "请求request", in = ParameterIn.HEADER)
//    })
    public ReturnMessage logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        var userToken = userService.findToken(token);
        userService.removeToken(userToken);
        return ReturnMessage.success();
    }


    @PostMapping(value = "/register")
    @Operation(summary = "register", description = "注册")
    public ReturnMessage register(@RequestBody User user) {
        User tmpUser = userService.findByUserName(user.getUsername());
        if (tmpUser != null) {
            return ReturnMessage.error(ErrorMessage.ACCOUNT_ERR_EXISTS);
        }
        Long time = CommonUtils.getTime();
        user.setId(null);
        user.setPasswordHash(CommonUtils.passwordHash(user.getPasswordHash()));
        user.setStatus(0);
        user.setCreateAt(time);
        user.setUpdateAt(time);
        user.setRoleId(1);
        user.setDeleted(0);
//        System.out.println(user.toString());
        userService.createUser(user);
        return ReturnMessage.success();
    }

}
