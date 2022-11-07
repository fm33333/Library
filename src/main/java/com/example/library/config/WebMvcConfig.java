/*
 * Copyright(c) 2022, 深圳市链融科技股份有限公司
 * ShenZhen LianRong Technology Co., Ltd.
 * All rights reserved.
 */

package com.example.library.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 基础WebMvc全局配置
 *
 * @author 冯名豪
 * @date 2022-08-02
 * @since 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String OBJECT_MAPPER = "objectMapper";
    public static final String REST_OBJECT_MAPPER = "restObjectMapper";

    static {
        // 默认是根据系统语言(永远返回中文提示)
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * RestTemplate远程调用
     *
     * @param builder builder
     * @return ObjectMapper
     */
    @Bean(name = REST_OBJECT_MAPPER)
    public ObjectMapper restObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();
        setObjectMapper(mapper);
        return mapper;
    }

    @Bean(name = OBJECT_MAPPER)
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();
        setObjectMapper(mapper);

        // 字段驼峰转下划线（请求和响应）
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        return mapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return customizer -> customizer.deserializerByType(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
                // 去除JSON数据String前后空格
                return StringUtils.trimWhitespace(jp.getValueAsString());
            }
        });
    }

    /**
     * 设置ObjectMapper属性
     *
     * @param mapper ObjectMapper
     */
    public static void setObjectMapper(ObjectMapper mapper) {
        // 序列化日期时以timestamps输出，默认true
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 忽略json字符串中不识别的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 忽略无法转换的对象
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 检举以索引输出
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        // 没有匹配的属性名称时不作失败处理
        mapper.enable(MapperFeature.AUTO_DETECT_FIELDS);
        // 属性为NULL不序列化
        mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 允许出现特殊字符和转义符
        mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        // 允许出现单引号
        mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 时区为东8区
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

}

