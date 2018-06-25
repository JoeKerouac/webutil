package com.joe.web.starter.core.config;

import javax.annotation.Priority;
import javax.ws.rs.Path;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义扫描的类的集合
 *
 * @author joe
 * @version 2018.02.02 17:46
 */
public class ScanConfig {
    public static final List<Class<? extends Annotation>> JERSEY_COMPONENT;

    static {
        List<Class<? extends Annotation>> temp = new ArrayList<>();
        temp.add(Path.class);
        temp.add(PreMatching.class);
        temp.add(Priority.class);
        temp.add(Provider.class);
        JERSEY_COMPONENT = Collections.unmodifiableList(temp);
    }
}
