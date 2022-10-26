package com.spade.jdoc.controller;


import com.spade.jdoc.model.Book;
import com.spade.jdoc.model.User;
import com.spade.jdoc.model.form.IdMap;
import com.spade.jdoc.service.BookService;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.ReturnMessage;
import com.spade.jdoc.utils.SearchMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/book")
@Tag(name = "bookAPI", description = "book接口")
public class BookController {

    @Resource
    private BookService bookService;

    @GetMapping(value = "/list")
    @Operation(summary = "list", description = "book列表", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage list(SearchMessage searchMessage) {
        var user = (User) CommonUtils.cacheMap.get("user");
        searchMessage.setUser(user);
        var searchList = bookService.findAll(searchMessage);
        return ReturnMessage.success()
                .setData("list", searchList.getList())
                .setData("count", searchList.getCount());
    }


    @GetMapping(value = "/one")
    @Operation(summary = "one", description = "获取单个book", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage one(Integer id) {
        var user = (User) CommonUtils.cacheMap.get("user");
        Book book = bookService.findById(id);
        if (book == null || !book.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(book.getUserId())) {
            return ReturnMessage.error("this data not found");
        }
        return ReturnMessage.success()
                .setData("book", book);
    }

    @PostMapping(value = "/create")
    @Operation(summary = "create", description = "create book", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage create(@RequestBody Book book) {
        var user = (User) CommonUtils.cacheMap.get("user");
        Long time = CommonUtils.getTime();
        book.setId(null);
        book.setCreateAt(time);
        book.setUpdateAt(time);
        book.setDeleted(0);
        if (book.getSortBy() <= 0) {
            book.setSortBy(100);
        }
        book.setUserId(user.getId());
        bookService.createBook(book);
        return ReturnMessage.success()
                .setData("book_id", book.getId());
    }

    @PostMapping(value = "/update")
    @Operation(summary = "update", description = "update book", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage update(@RequestBody Book book) {
        var user = (User) CommonUtils.cacheMap.get("user");
        Book newbook = bookService.findById(book.getId());
        if (newbook == null || !newbook.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newbook.getUserId())) {
            return ReturnMessage.error("your data not found");
        }
        Long time = CommonUtils.getTime();
        newbook.setUpdateAt(time);
        newbook.setName(book.getName());
        newbook.setMark(book.getMark());
        newbook.setSortBy(book.getSortBy());
        newbook.setType(book.getType());
        newbook.setVisitPass(book.getVisitPass());
        bookService.updateBook(newbook);
        return ReturnMessage.success();
    }

    @PostMapping(value = "/del")
    @Operation(summary = "del", description = "del book", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage del(@RequestBody IdMap idMap) {
        int id = idMap.getId();
        var user = (User) CommonUtils.cacheMap.get("user");
        Book newbook = bookService.findById(id);
        if (newbook == null || !newbook.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        if (!user.getId().equals(newbook.getUserId())) {
            return ReturnMessage.error("your data not found");
        }
        Long time = CommonUtils.getTime();
        newbook.setUpdateAt(time);
        newbook.setDeleted(1);
        bookService.updateBook(newbook);
        return ReturnMessage.success();
    }


}
