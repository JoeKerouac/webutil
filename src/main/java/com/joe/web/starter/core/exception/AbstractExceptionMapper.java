package com.joe.web.starter.core.exception;

import org.springframework.beans.factory.annotation.Autowired;

import com.joe.web.starter.core.model.dto.ResponseDTO;
import com.joe.web.starter.core.spi.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽象异常处理器
 * 
 * @author JoeKerouac
 * @data 2021-04-10 08:48
 */
@Slf4j
public class AbstractExceptionMapper {

    public static final ResponseDTO<Object> ERROR = ResponseDTO.buildError(ErrorCode.UNKNOWN, "服务器异常，请稍后重试");

    @Autowired
    private ExceptionHandler exceptionHandler;

    /**
     * 异常处理
     * 
     * @param ex
     *            异常
     * @return 处理结果
     */
    protected ResponseDTO<Object> process(Throwable ex) {
        ResponseDTO<Object> dto = null;
        try {
            dto = exceptionHandler.process(ex);
        } catch (Throwable throwable) {
            log.error("异常处理失败", throwable);
        }

        if (dto == null) {
            dto = ERROR;
        }

        return dto;
    }

}
