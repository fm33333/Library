package com.example.library.service;

import com.example.library.domain.ApiCode;
import com.example.library.domain.entity.Book;

import java.util.List;

/**
 * 图书服务
 *
 * @author 冯名豪
 * @date 2022-10-10
 * @since 1.0
 */
public interface BookService {

    /**
     * 图书入库
     *
     * @param book
     */
    String saveBook(Book book);

    /**
     * 更新图书信息
     *
     * @param book
     * @return
     */
    String updateBook(Book book);

}
