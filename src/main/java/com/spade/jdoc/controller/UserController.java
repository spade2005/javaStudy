package com.spade.jdoc.controller;


import com.spade.jdoc.model.User;
import com.spade.jdoc.model.form.IdMap;
import com.spade.jdoc.service.UserService;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.ReturnMessage;
import com.spade.jdoc.utils.SearchMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/user")
@Tag(name = "userAPI", description = "user接口")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/list")
    @Operation(summary = "list", description = "user列表", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage list(SearchMessage searchMessage) {
        var currentUser = (User) CommonUtils.cacheMap.get("user");
        if (!currentUser.checkSuper()) {
            return ReturnMessage.error("data not allow");
        }
        var searchList = userService.findAll(searchMessage);
        return ReturnMessage.success()
                .setData("list", searchList.getList())
                .setData("count", searchList.getCount());
    }


    @GetMapping(value = "/one")
    @Operation(summary = "one", description = "获取单个user", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage one(Integer id) {
        var currentUser = (User) CommonUtils.cacheMap.get("user");
        if (!currentUser.checkSuper()) {
            return ReturnMessage.error("data not allow");
        }
        var user = userService.findById(id);
        if (user == null || !user.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        return ReturnMessage.success()
                .setData("user", user);
    }

    @PostMapping(value = "/create")
    @Operation(summary = "create", description = "create user", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage create(@RequestBody User user) {
        var currentUser = (User) CommonUtils.cacheMap.get("user");
        if (!currentUser.checkSuper()) {
            return ReturnMessage.error("data not allow");
        }
        Long time = CommonUtils.getTime();
        user.setId(null);
        user.setPasswordHash(CommonUtils.passwordHash(user.getPasswordHash()));
        user.setStatus(0);
        user.setCreateAt(time);
        user.setUpdateAt(time);
        user.setRoleId(1);
        user.setDeleted(0);
        userService.createUser(user);
        return ReturnMessage.success()
                .setData("user_id", user.getId());
    }

    @PostMapping(value = "/update")
    @Operation(summary = "update", description = "update user", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage update(@RequestBody User user) {
        var currentUser = (User) CommonUtils.cacheMap.get("user");
        if (!currentUser.checkSuper()) {
            return ReturnMessage.error("data not allow");
        }
        var newtmp = userService.findById(user.getId());
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        Long time = CommonUtils.getTime();
        if (!user.getPasswordHash().isEmpty()) {
            newtmp.setPasswordHash(CommonUtils.passwordHash(user.getPasswordHash()));
        }
        newtmp.setStatus(user.getStatus());
        newtmp.setUpdateAt(time);
        newtmp.setRoleId(user.getRoleId());
        newtmp.setPhone(user.getPhone());
        newtmp.setEmail(user.getEmail());
        userService.createUser(newtmp);
        return ReturnMessage.success();
    }

    @PostMapping(value = "/del")
    @Operation(summary = "del", description = "del user", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage del(@RequestBody IdMap idMap) {
        int id = idMap.getId();
        var user = (User) CommonUtils.cacheMap.get("user");
        if (!user.checkSuper()) {
            return ReturnMessage.error("data not allow");
        }
        var newtmp = userService.findById(id);
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        Long time = CommonUtils.getTime();
        newtmp.setUpdateAt(time);
        newtmp.setDeleted(1);
        userService.createUser(newtmp);
        return ReturnMessage.success();
    }


    @PostMapping(value = "/save")
    @Operation(summary = "save", description = "save current user", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage save(@RequestBody User user) {
        var currentUser = (User) CommonUtils.cacheMap.get("user");
        var newtmp = userService.findById(currentUser.getId());
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getPasswordHash().isEmpty()) {
            newtmp.setPasswordHash(CommonUtils.passwordHash(user.getPasswordHash()));
        }
        newtmp.setUpdateAt(CommonUtils.getTime());
        newtmp.setPhone(user.getPhone());
        newtmp.setEmail(user.getEmail());
        newtmp.setNickName(user.getNickName());
        userService.createUser(newtmp);
        return ReturnMessage.success();
    }


    @GetMapping(value = "/info")
    @Operation(summary = "info", description = "获取当前user", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage info() {
        var currentUser = (User) CommonUtils.cacheMap.get("user");
//        if (!currentUser.checkSuper()) {
//            return ReturnMessage.error("data not allow");
//        }
        var user = userService.findById(currentUser.getId());
        if (user == null || !user.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        return ReturnMessage.success()
                .setData("user", user);
    }

}
