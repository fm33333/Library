package com.example.library.constant;

import com.example.library.utils.SpringBeanUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 图书馆配置文件
 *
 * @author 冯名豪
 * @date 2022-09-28
 * @since 1.0
 */
@Data
@Validated
@Component
@Configuration
@ConfigurationProperties(prefix = "library")
public class LibraryProperties {

    /**
     * ISBN公开信息查询接口
     */
    private String isbnUrl;

    /**
     * apiKey
     */
    private String apiKey;

    public static LibraryProperties getProperties() {
        return SpringBeanUtils.getBean(LibraryProperties.class);
    }

}
