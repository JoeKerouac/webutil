package com.joe.web.starter.core.spring.exception;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.joe.utils.serialize.json.JsonParser;
import com.joe.web.starter.core.constant.Const;
import com.joe.web.starter.core.exception.AbstractExceptionMapper;
import com.joe.web.starter.core.model.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理器
 *
 * @author JoeKerouac
 * @data 2021-04-09 21:55
 */
@Slf4j
public class SpringExceptionMapper extends AbstractExceptionMapper implements HandlerExceptionResolver {

    private static final JsonParser JSON = JsonParser.getInstance();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) {
        ResponseDTO<Object> dto = super.process(ex);

        String json = JSON.toJson(dto);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(Const.DEFAULT_CHARSET);

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(json.getBytes(Const.DEFAULT_CHARSET));
            out.flush();
        } catch (IOException e) {
            log.error("IO异常", e);
        }

        return new ModelAndView();
    }
}
