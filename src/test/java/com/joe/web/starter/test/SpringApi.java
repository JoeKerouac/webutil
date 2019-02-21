package com.joe.web.starter.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * springMVC控制器
 *
 * @author joe
 * @version 2018.03.09 18:49
 */
@Controller
@RequestMapping("api")
public class SpringApi implements Api {
    @RequestMapping(value = "/hello", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String hello() {
        return "hello";
    }
}
