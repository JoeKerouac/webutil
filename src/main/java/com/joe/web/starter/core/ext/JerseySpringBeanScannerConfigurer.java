package com.joe.web.starter.core.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.joe.web.starter.core.prop.SysProp;

/**
 * 自定义bean处理器
 *
 * @author joe
 * @version 2018.02.02 11:00
 */
public class JerseySpringBeanScannerConfigurer implements BeanFactoryPostProcessor,
                                               ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SysProp prop = beanFactory.getBean(SysProp.class);
        JerseySpringScanner scanner = new JerseySpringScanner((BeanDefinitionRegistry) beanFactory);
        scanner.setResourceLoader(this.applicationContext);
        //此处定义扫描com包，用户定义的组件所在的包必须在com包下才能扫描到
        scanner.scan(prop.getJerseyScan());
    }
}
