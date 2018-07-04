package com.joe.web.starter.test;

import com.joe.web.starter.WebApplication;
import com.joe.web.starter.core.prop.SysProp;
import com.joe.web.starter.core.secure.entity.Role;
import com.joe.web.starter.core.secure.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;

/**
 * 测试主类
 *
 * @author joe
 * @version 2018.03.09 18:49
 */
@Slf4j
@ComponentScan("com.joe.web")
public class App {
    public static void main(String[] args) throws Exception {
        SysProp prop = new SysProp();
        //开启jersey
        prop.setDisableJersey(false);
        //提供一个用户身份认证
        prop.enableAuthentication(session -> {
            User user = new User();
            user.setRoles(Collections.singleton(new Role("123", "user")));
            return user;
        });
        WebApplication.runWeb(App.class, prop, args);
    }
}
