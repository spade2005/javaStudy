package com.spade.jdoc.controller;


import com.spade.jdoc.model.PageType;
import com.spade.jdoc.model.User;
import com.spade.jdoc.model.form.IdMap;
import com.spade.jdoc.service.BookService;
import com.spade.jdoc.service.PageTypeService;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.ReturnMessage;
import com.spade.jdoc.utils.SearchPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/page-type")
@Tag(name = "PageTypeAPI", description = "pageType接口")
public class PageTypeController {

    @Resource
    private PageTypeService pageTypeService;
    @Resource
    private BookService bookService;

    @GetMapping(value = "/list")
    @Operation(summary = "list", description = "pageType列表", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage list(SearchPage searchMessage) {
        var user = (User) CommonUtils.cacheMap.get("user");
        searchMessage.setUser(user);
        var searchList = pageTypeService.findAll(searchMessage);
        return ReturnMessage.success()
                .setData("list", searchList.getList())
                .setData("count", searchList.getCount());
    }


    @GetMapping(value = "/one")
    @Operation(summary = "one", description = "获取单个pageType", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage one(Integer id) {
        var user = (User) CommonUtils.cacheMap.get("user");
        var newtmp = pageTypeService.findById(id);
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newtmp.getUserId())) {
            return ReturnMessage.error("not allow todo");
        }
        return ReturnMessage.success()
                .setData("page_type", newtmp);
    }

    @PostMapping(value = "/create")
    @Operation(summary = "create", description = "create pageType", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage create(@RequestBody PageType pageType) {
        var user = (User) CommonUtils.cacheMap.get("user");
        var book = bookService.findById(pageType.getBookId());
        if (book == null || !user.getId().equals(book.getUserId())) {
            return ReturnMessage.error("not allow todo");
        }
        Long time = CommonUtils.getTime();
        pageType.setId(null);
        pageType.setCreateAt(time);
        pageType.setUpdateAt(time);
        pageType.setDeleted(0);
        if (pageType.getSortBy() == null) {
            pageType.setSortBy(100);
        }
        pageType.setUserId(user.getId());
        pageTypeService.create(pageType);
        return ReturnMessage.success()
                .setData("page_type_id", pageType.getId());
    }

    @PostMapping(value = "/update")
    @Operation(summary = "update", description = "update pageType", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage update(@RequestBody PageType pageType) {
        var user = (User) CommonUtils.cacheMap.get("user");
        var newtmp = pageTypeService.findById(pageType.getId());
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newtmp.getUserId())) {
            return ReturnMessage.error("your data not found");
        }
        Long time = CommonUtils.getTime();
        newtmp.setUpdateAt(time);
        newtmp.setName(pageType.getName());
        newtmp.setMark(pageType.getMark());
        newtmp.setSortBy(pageType.getSortBy());
        newtmp.setParentId(pageType.getParentId());
        pageTypeService.update(newtmp);
        return ReturnMessage.success();
    }

    @PostMapping(value = "/del")
    @Operation(summary = "del", description = "del pageType", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage del(@RequestBody IdMap idMap) {
        int id = idMap.getId();
        var user = (User) CommonUtils.cacheMap.get("user");
        var newtmp = pageTypeService.findById(id);
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newtmp.getUserId())) {
            return ReturnMessage.error("your data not found");
        }
        Long time = CommonUtils.getTime();
        newtmp.setUpdateAt(time);
        newtmp.setDeleted(1);
        pageTypeService.update(newtmp);
        return ReturnMessage.success();
    }


}
