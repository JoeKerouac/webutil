package com.joe.pay.wechat.pojo;

import lombok.Data;

/**
 * @author joe
 * @version 2018.06.29 17:53
 */
@Data
public class PayResponse {
    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String trade_type;
    private String prepay_id;
}
