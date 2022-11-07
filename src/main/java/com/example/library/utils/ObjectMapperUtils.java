package com.example.library.utils;

import com.example.library.config.WebMvcConfig;
import com.example.library.domain.ApiCode;
import com.example.library.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ObjectMapper工具类
 *
 * @author 冯名豪
 * @date 2022-08-02
 * @since 1.0
 */
@Slf4j
@Component
public final class ObjectMapperUtils {

    private ObjectMapperUtils() {

    }

    /**
     * 对象转字符串
     *
     * @param value
     * @return
     * @throws BusinessException
     */
    public static String writeValueAsString(Object value) {
        try {
            return objectMapper().writeValueAsString(value);
        } catch (Exception e) {
            log.error("对象转JSON失败, {}", value, e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR, "对象转JSON失败");
        }
    }

    /**
     * json转对象
     *
     * @param input
     * @param clazz
     * @return
     * @throws BusinessException
     */
    public static <T> T readValue(String input, Class<T> clazz) {
        if (!StringUtils.hasText(input)) {
            log.error("input参数为空");
            throw new BusinessException(ApiCode.SYSTEM_ERROR, "JSON转换失败");
        }
        try {
            return objectMapper().readValue(input, clazz);
        } catch (Exception e) {
            log.error("JSON转对象失败, input: {}", input, e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR, "JSON转换失败");
        }
    }

    /**
     * json转map
     *
     * @param input
     * @return
     * @throws BusinessException
     */
    public static Map<String, Object> readValueMap(String input) {
        if (!StringUtils.hasText(input)) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper().readValue(input, Map.class);
        } catch (Exception e) {
            log.error("JSON转Map失败, input: {}", input, e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR, "JSON转换失败");
        }
    }

    /**
     * json转list
     *
     * @param input
     * @param clazz
     * @return
     * @throws BusinessException
     */
    public static <T> List<T> readValueList(String input, Class<?> clazz) {
        if (!StringUtils.hasText(input)) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = objectMapper();
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(input, collectionType);
        } catch (Exception e) {
            log.error("JSON转List失败, input: {}", input, e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR, "JSON转换失败");
        }
    }

    /**
     * 对象转对象
     *
     * @param fromValue
     * @param toValueType
     * @return
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper().convertValue(fromValue, toValueType);
    }

    private static ObjectMapper objectMapper() {
        return SpringBeanUtils.getBean(WebMvcConfig.REST_OBJECT_MAPPER);
    }

}
