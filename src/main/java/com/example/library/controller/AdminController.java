package com.example.library.controller;

import com.example.library.facade.BookFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 *
 * @author 冯名豪
 * @date 2022-11-02
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final BookFacade bookFacade;





}
