package com.joe.web.starter.core.filter;

import com.joe.utils.common.DateUtil;
import com.joe.utils.parse.json.JsonParser;
import com.joe.web.starter.core.dto.InterfaceInfo;
import com.joe.web.starter.core.prop.SysProp;
import org.glassfish.jersey.message.MessageUtils;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 请求预处理，优先级应为最高，对于请求来说，优先处理@Priority值小的，对于响应来说，优先处理@Priority值大的
 *
 * @author joe
 */
@Provider
@PreMatching
@Priority(Integer.MIN_VALUE)
public class PretreatmentFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger logger = LoggerFactory.getLogger("Statistics");
    private static final JsonParser JSON = JsonParser.getInstance();
    private static final String format = "yyyy-MM-dd HH:mm:ss SSS";
    @Context
    private HttpServletRequest httpServletRequest;
    /**
     * 最大读取数量
     */
    private int maxReadSize;
    @Context
    private HttpServletResponse response;
    @Autowired
    private SysProp prop;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        this.maxReadSize = prop.getMaxReadSize() <= 0 ? SysProp.DEFAULT_MAX_SIZE : prop.getMaxReadSize();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //创建接口请求信息
        ContainerRequest request = (ContainerRequest) requestContext;
        ExtendedUriInfo uriInfo = request.getUriInfo();
        InterfaceInfo info = new InterfaceInfo();
        info.setBeginTime(DateUtil.getFormatDate(format));
        info.setRealRequestAddr(uriInfo.getPath());
        info.setIp(httpServletRequest.getRemoteAddr());
        info.setMethod(requestContext.getMethod());
        info.setPath(httpServletRequest.getRequestURL().toString());
        requestContext.setProperty("InterfaceInfo", info);

        logger.info("接收到请求，请求方法为：" + info.getMethod() + "，请求地址为：" + info.getPath() + "，请求接口为：" +
                info.getRealRequestAddr() + "，开始处理");
        logRequestInfo(requestContext);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        //完善接口请求信息
        InterfaceInfo info = (InterfaceInfo) requestContext.getProperty("InterfaceInfo");
        info.setFinish(true);
        info.setEndTime(DateUtil.getFormatDate(format));
        int consume = (int) (DateUtil.parse(info.getEndTime(), format).getTime() - DateUtil.parse(info.getBeginTime()
                , format).getTime());
        info.setConsumeTime(consume);
        Object obj = responseContext.getEntity();
        logger.debug("响应结果是：" + JSON.toJson(obj));
        logger.info("此次请求共耗时" + info.getConsumeTime() + "ms");
    }

    /**
     * 读取请求中的信息（最大读取{@link #maxReadSize}字节）
     *
     * @param requestContext 请求上下文
     * @throws IOException IO异常
     */
    private void logRequestInfo(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getMethod().equals("GET")) {
            return;
        }
        InputStream input = requestContext.getEntityStream();
        if (!input.markSupported()) {
            input = new BufferedInputStream(input);
        }

        input.mark(maxReadSize + 1);
        byte[] data = new byte[maxReadSize + 1];
        int readSize = input.read(data);
        if (readSize < 1) {
            logger.info("请求post信息为空");
            input.reset();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("请求post信息为：");
        sb.append(new String(data, 0, Math.min(readSize, maxReadSize),
                MessageUtils.getCharset(requestContext.getMediaType())));
        if (readSize > maxReadSize) {
            sb.append("...more....");
        }
        logger.info(sb.toString());
        input.reset();
        requestContext.setEntityStream(input);
        return;
    }
}
