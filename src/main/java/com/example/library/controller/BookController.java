package com.example.library.controller;

import com.example.library.constant.LibraryProperties;
import com.example.library.domain.Response;
import com.example.library.facade.BookFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 图书控制器
 *
 * @author 冯名豪
 * @date 2022-09-19
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {

    private final BookFacade bookFacade;

    /**
     * 测试
     *
     * @return
     */
    @GetMapping("/test")
    public Response test() {
        String s = "test 01";
        LibraryProperties properties = LibraryProperties.getProperties();
        log.warn("test: {}", properties.getIsbnUrl());
        return Response.success(properties.getApiKey());
    }

    /**
     * 管理员添加新书
     *
     * @param isbn
     * @return
     */
    @PostMapping("/store")
    public Response storeNewBook(@RequestParam("isbn") String isbn) {
        return Response.success(bookFacade.storeNewBook(isbn));
    }

    /**
     * 管理员增加库存
     */
    @PostMapping("/add")
    public Response addBook(
            @RequestParam("book_id") Long bookId
            , @RequestParam("num") Integer num) {
        return Response.success(bookFacade.addBook(bookId, num));
    }

    /**
     * 管理员清理库存
     */

    /**
     * 查询图书
     */

    /**
     * 用户借阅图书
     */

    /**
     * 用户归还图书
     */

}
