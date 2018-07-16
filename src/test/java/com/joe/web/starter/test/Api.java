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
public class Api {
    @RequestMapping(value = "/test", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String test() {
        return "hello world";
    }
}
