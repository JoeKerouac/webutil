package com.joe.web.starter.core.ext;

import com.joe.web.starter.core.config.ScanConfig;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.ws.rs.Path;
import java.util.Set;

/**
 * 自定义扫描器（用来扫描jersey的{@link Path}注解）
 *
 * @author joe
 * @version 2018.02.02 11:03
 */
public class JerseySpringScanner extends ClassPathBeanDefinitionScanner {
    JerseySpringScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void registerDefaultFilters() {
        // 添加自定义类型，将以下注解加入扫描
        ScanConfig.JERSEY_COMPONENT.parallelStream().map(AnnotationTypeFilter::new).forEach(this::addIncludeFilter);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition);
    }
}