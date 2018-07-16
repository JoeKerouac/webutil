package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joe.pay.pojo.Response;

import lombok.Data;

/**
 * 公共响应参数
 * <p>
 * 由于校验签名需要，该类的所有子类不能使用基本类型（byte、int等）
 *
 * @author joe
 * @version 2018.06.29 11:10
 */
@Data
public class AliPublicResponse implements Response {
    /**
     * 网关返回码
     */
    private String code;
    /**
     * 网关返回码描述
     */
    private String msg;
    /**
     * 业务返回码，参见具体的API接口文档
     */
    @JsonProperty("sub_code")
    private String subCode;
    /**
     * 业务返回码描述，参见具体的API接口文档
     */
    @JsonProperty("sub_msg")
    private String subMsg;
    /**
     * 签名
     */
    private String sign;
}
