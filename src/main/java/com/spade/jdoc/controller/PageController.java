package com.spade.jdoc.controller;


import com.spade.jdoc.model.Page;
import com.spade.jdoc.model.User;
import com.spade.jdoc.model.form.IdMap;
import com.spade.jdoc.service.BookService;
import com.spade.jdoc.service.PageService;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.ReturnMessage;
import com.spade.jdoc.utils.SearchPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/page")
@Tag(name = "PageeAPI", description = "page接口")
public class PageController {

    @Resource
    private PageService pageService;
    @Resource
    private BookService bookService;

    @GetMapping(value = "/list")
    @Operation(summary = "list", description = "page列表", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage list(SearchPage searchMessage) {
        var user = (User) CommonUtils.cacheMap.get("user");
        searchMessage.setUser(user);
        var searchList = pageService.findAll(searchMessage);
        return ReturnMessage.success()
                .setData("list", searchList.getList())
                .setData("count", searchList.getCount());
    }


    @GetMapping(value = "/one")
    @Operation(summary = "one", description = "获取单个page", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage one(Integer id) {
        var user = (User) CommonUtils.cacheMap.get("user");
        var newtmp = pageService.findById(id);
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newtmp.getUserId())) {
            return ReturnMessage.error("not allow todo");
        }
        var pageContent = pageService.findLastPageContent(newtmp.getId());
        if (pageContent != null) {
            newtmp.setContent(pageContent.getContent());
        }
        return ReturnMessage.success()
                .setData("page", newtmp);
    }

    @PostMapping(value = "/create")
    @Operation(summary = "create", description = "create page", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage create(@RequestBody Page page) {
        var user = (User) CommonUtils.cacheMap.get("user");
        var book = bookService.findById(page.getBookId());
        if (book == null || !user.getId().equals(book.getUserId())) {
            return ReturnMessage.error("not allow todo");
        }
        Long time = CommonUtils.getTime();
        page.setId(null);
        page.setCreateAt(time);
        page.setUpdateAt(time);
        page.setDeleted(0);
        if (page.getSortBy() == null) {
            page.setSortBy(100);
        }
        page.setUserId(user.getId());
        pageService.create(page);
        return ReturnMessage.success()
                .setData("page_id", page.getId());
    }

    @PostMapping(value = "/update")
    @Operation(summary = "update", description = "update page", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage update(@RequestBody Page page) {
        var user = (User) CommonUtils.cacheMap.get("user");
        var newtmp = pageService.findById(page.getId());
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newtmp.getUserId())) {
            return ReturnMessage.error("your data not found");
        }
        Long time = CommonUtils.getTime();
        newtmp.setUpdateAt(time);
        newtmp.setTitle(page.getTitle());
        newtmp.setTypeId(page.getTypeId());
        newtmp.setSortBy(page.getSortBy());
        newtmp.setContent(page.getContent());
        pageService.update(newtmp);
        return ReturnMessage.success();
    }

    @PostMapping(value = "/del")
    @Operation(summary = "del", description = "del page", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage del(@RequestBody IdMap idMap) {
        int id = idMap.getId();
        var user = (User) CommonUtils.cacheMap.get("user");
        var newtmp = pageService.findById(id);
        if (newtmp == null || !newtmp.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newtmp.getUserId())) {
            return ReturnMessage.error("your data not found");
        }
        Long time = CommonUtils.getTime();
        newtmp.setUpdateAt(time);
        newtmp.setDeleted(1);
        pageService.update(newtmp);
        return ReturnMessage.success();
    }


}
