package com.joe.web.starter.core.condition;

import com.joe.utils.common.Assert;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.joe.web.starter.core.prop.SysProp;

/**
 * @author JoeKerouac
 * @data 2021-04-10 09:31
 */
public class EnableSpringMvcCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Assert.notNull(beanFactory);
        SysProp prop = beanFactory.getBean(SysProp.class);
        return prop.isDisableJersey();
    }

}
