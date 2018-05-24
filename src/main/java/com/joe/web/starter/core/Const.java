package com.joe.web.starter.core;

/**
 * 常量表
 *
 * @author joe
 * @version 2018.05.24 15:38
 */
public class Const {
    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String FILE_PREFIX = "file:";
    public static final String DEFAULT_DOC_ROOT = Thread.currentThread().getContextClassLoader().getResource("")
            .getFile();
}
