package com.joe.pay.alipay.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 公共响应参数
 *
 * @author joe
 * @version 2018.06.29 11:10
 */
@Data
public class PublicResponse {
    private String code;
    private String msg;
    @JsonProperty("sub_code")
    private String subCode;
    @JsonProperty("sub_msg")
    private String subMsg;
    private String sign;
}
