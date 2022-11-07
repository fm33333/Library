package com.example.library.controller;

import com.example.library.constant.LibraryProperties;
import com.example.library.domain.Response;
import com.example.library.facade.BookFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author 冯名豪
 * @date 2022-11-02
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final BookFacade bookFacade;



}
