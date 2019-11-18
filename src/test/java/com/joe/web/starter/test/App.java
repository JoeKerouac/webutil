package com.joe.web.starter.test;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.joe.http.ws.ResourceFactory;
import com.joe.http.ws.core.ResourceType;
import com.joe.web.starter.WebApplication;
import com.joe.web.starter.core.prop.SysProp;
import com.joe.web.starter.core.secure.entity.Role;
import com.joe.web.starter.core.secure.entity.User;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试主类
 *
 * @author joe
 * @version 2018.03.09 18:49
 */
@Slf4j
@ComponentScan("com.joe.web")
public class App {

    @Test
    public void testJersey() {
        int port = (int) (Math.random() * 30000) + 30000;
        doTest(true, port, JerseyApi.class);
    }

    @Test
    public void testSpring() {
        int port = (int) (Math.random() * 30000) + 30000;
        doTest(false, port, SpringApi.class);
    }

    /**
     * 启动一个web容器
     * @param jersey 是否启用Jersey，true表示启用
     * @param port 端口
     * @param clazz API类型
     * @return web容器
     */
    private <T extends Api> void doTest(boolean jersey, int port, Class<T> clazz) {
        SysProp prop = new SysProp();
        //开启jersey
        prop.setDisableJersey(!jersey);
        prop.setPort(port);
        prop.setRoot("custom/test");
        //提供一个用户身份认证
        prop.enableAuthentication(session -> {
            User user = new User();
            user.setRoles(Collections.singleton(new Role("123", "user")));
            return user;
        });
        ResourceType type = jersey ? ResourceType.JERSEY : ResourceType.SPRING;
        try (ConfigurableApplicationContext ignored = WebApplication.runWeb(App.class, prop,
            null)) {
            ResourceFactory factory = new ResourceFactory(String.format("http://127.0.0.1:%s/custom/test", port), type);
            Api api = factory.build(clazz);
            Assert.assertEquals("接口返回与预期不符", "hello", api.hello());
        }
    }
}
