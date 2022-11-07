package com.example.library.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * SpringBean 工具类
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @version 1.0
 */
@Slf4j
@Component
public class SpringBeanUtils implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext ctx = null;
    private static Environment environment;

    private SpringBeanUtils() {

    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        SpringBeanUtils.ctx = ctx;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return (T) ctx.getBean(beanClass);
    }

    public static <T> T getBean(String name) {
        return (T) ctx.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> beanClass) {
        return (T) ctx.getBean(name, beanClass);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return ctx.getBeansOfType(type);
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringBeanUtils.environment = environment;
    }

    /**
     * 获取系统配置
     *
     * @param property
     * @return
     */
    public static String getProperty(String property) {
        return environment.getProperty(property);
    }
}
