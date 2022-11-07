package com.example.library.mapper;

import com.example.library.domain.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 图书mapper
 *
 * @author 冯名豪
 * @date 2022-10-24
 * @since 1.0
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

    Book selectByBookId(@Param("bookId") Long bookId);

}
