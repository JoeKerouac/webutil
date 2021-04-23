package com.joe.web.starter.core.spi;

import com.joe.web.starter.core.model.dto.ResponseDTO;

/**
 * 异常处理器
 * 
 * @author JoeKerouac
 * @data 2021-04-09 21:57
 */
public interface ExceptionHandler {

    /**
     * 处理异常
     * 
     * @param throwable
     *            异常
     * @return 返回非空表示处理成功，返回空表示该处理器不能处理
     */
    ResponseDTO<Object> process(Throwable throwable);

}
