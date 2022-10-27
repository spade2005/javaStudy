package com.spade.jdoc.controller;


import com.spade.jdoc.model.Page;
import com.spade.jdoc.model.PageType;
import com.spade.jdoc.model.User;
import com.spade.jdoc.service.BookService;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.ReturnMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/")
@Tag(name = "ApiAPI", description = "api聚合接口")
public class ApiController {

    @Resource
    private BookService bookService;


    @GetMapping(value = "/item")
    @Operation(summary = "item", description = "返回book下all page & pageType", security = {@SecurityRequirement(name = "token")})
    public ReturnMessage item(Integer id) {
        var book = bookService.findById(id);
        if (book == null || !book.getDeleted().equals(0)) {
            return ReturnMessage.error("data not found");
        }
        var user = (User) CommonUtils.cacheMap.get("user");
        if (!user.getId().equals(book.getUserId())) {
            return ReturnMessage.error("this operation not allow");
        }
        //query page and query pageType.
        var listPageType = bookService.queryPageType(book);
        var listPage = bookService.queryPage(book);

        List<Page> parentPage = new ArrayList<>();
        List<Page> childPage = new ArrayList<>();
        for (Page page : listPage) {
            if (page.getTypeId() > 0) {
                childPage.add(page);
            } else {
                parentPage.add(page);
            }
        }
        book.setPages(parentPage);
        for (PageType pageType : listPageType) {
            List<Page> tmpPage = new ArrayList<>();
            for (Page page : childPage) {
                if (page.getTypeId().equals(pageType.getId())) {
                    tmpPage.add(page);
                }
            }
            pageType.setListPage(tmpPage);
        }
        book.setPageTypes(listPageType);
        return ReturnMessage.success().setData("item", book);
    }


}
