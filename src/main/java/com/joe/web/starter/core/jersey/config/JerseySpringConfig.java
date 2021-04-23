package com.joe.web.starter.core.jersey.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * jersey配置
 *
 * @author joe
 * @version 2018.02.02 10:32
 */
@Slf4j
public class JerseySpringConfig extends ResourceConfig {
    /**
     * 动态注册的组件集合，必须在系统启动前注册进来才有效
     */
    private static final List<Class<?>> COMPONENTS = new ArrayList<>();

    public JerseySpringConfig(ConfigurableApplicationContext context) {
        ConfigurableListableBeanFactory factory = context.getBeanFactory();
        log.info("初始化jersey");
        // 用spring的扫包代替jersey自带的扫包，jersey自带的扫包只能扫到当前项目的，扫不到jar包中的，所以用spring的代替jersey的
        // 此处必须使用串行流，不能使用并行流，否则会死锁
        Stream<Collection<Object>> springComponents =
            ScanConfig.JERSEY_COMPONENT.stream().map(factory::getBeansWithAnnotation).map(Map::values);
        Stream<Collection<Class<?>>> customComponents = Stream.of(COMPONENTS);

        log.info("注册jersey组件");
        // 此处也不能使用并行流，只能使用串行流，这里不能将两个流合并为一个流，否则调用register时class会被当做object注册
        springComponents.flatMap(Collection::stream).forEach(this::register);
        customComponents.flatMap(Collection::stream).forEach(this::register);
        log.info("jersey组件注册完毕");
    }

    /**
     * 注册组件，必须在系统启动前注册进来才有效
     *
     * @param clazz
     *            组件class
     */
    public static void registerComponent(Class<?> clazz) {
        COMPONENTS.add(clazz);
    }
}
