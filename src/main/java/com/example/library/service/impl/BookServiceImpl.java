package com.example.library.service.impl;

import com.example.library.domain.ApiCode;
import com.example.library.domain.entity.Book;
import com.example.library.mapper.BaseMapper;
import com.example.library.mapper.BookMapper;
import com.example.library.service.BookService;
import com.example.library.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图书服务
 *
 * @author 冯名豪
 * @date 2022-10-10
 * @since 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveBook(Book book) {
        if (bookMapper.selectCount(book) > 0) {
            log.warn("The book is already in db: {}", ObjectMapperUtils.writeValueAsString(book));
            return ApiCode.BOOK_EXIST.getMsg();
        }
        bookMapper.insertSelective(book);
        return ApiCode.SUCCESS.getMsg();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateBook(Book book) {
        bookMapper.updateByPrimaryKey(book);
        return ApiCode.SUCCESS.getMsg();
    }
}
