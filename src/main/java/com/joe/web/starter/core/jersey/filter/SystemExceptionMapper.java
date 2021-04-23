package com.joe.web.starter.core.jersey.filter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.joe.web.starter.core.exception.AbstractExceptionMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理器
 *
 * @author joe
 * @version 2018.02.02 17:44
 */
@Slf4j
public class SystemExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.error("系统异常，返回", exception);
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(super.process(exception))
            .build();
    }
}
