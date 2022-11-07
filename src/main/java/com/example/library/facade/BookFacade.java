package com.example.library.facade;

import com.example.library.constant.LibraryProperties;
import com.example.library.domain.ApiCode;
import com.example.library.domain.bo.BookBO;
import com.example.library.domain.entity.Book;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.BookMapper;
import com.example.library.service.BookService;
import com.example.library.utils.HttpUtils;
import com.example.library.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookFacade {

    private final BookService bookService;

    private final BookMapper bookMapper;

    /**
     * 图书入库
     *
     * @param isbn
     */
    public String storeNewBook(String isbn) {
        LibraryProperties properties = LibraryProperties.getProperties();
        String apiKey = properties.getApiKey();
        String isbnUrl = properties.getIsbnUrl() + isbn;
        Map<String, String> param = new HashMap<>();
        param.put("apikey", apiKey);

        // 拉取图书并入库
        try {
            String result = HttpUtils.doGet(isbnUrl, param);
            log.info("result: {}", result);
            BookBO bookBO = ObjectMapperUtils.readValue(result, BookBO.class);
            Book book = bookBO.toBook();
            log.info("book: {}", ObjectMapperUtils.writeValueAsString(book));
            return bookService.saveBook(book);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR);
        }
    }

    /**
     * 增加图书库存
     *
     * @param bookId
     * @return
     */
    public String addBook(Long bookId, Integer num) {
        Book book = bookMapper.selectByBookId(bookId);
        Integer bookNum = book.getNum();
        Integer remainNum = book.getRemainNum();
        log.info("num: {} remainNum: {}", bookNum, remainNum);
        book.setNum(bookNum + num);
        book.setRemainNum(remainNum + num);
        log.info("num: {} remainNum: {}", book.getNum(), book.getRemainNum());
        return bookService.updateBook(book);
    }

}
