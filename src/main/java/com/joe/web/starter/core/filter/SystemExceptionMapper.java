package com.joe.web.starter.core.filter;

import com.joe.utils.data.BaseDTO;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 统一异常处理器
 *
 * @author joe
 * @version 2018.02.02 17:44
 */
@Slf4j
public class SystemExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        log.error("系统异常，返回", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(BaseDTO
                .buildError()).build();
    }
}
