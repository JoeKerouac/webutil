package com.joe.pay.wechat.pojo;

import com.joe.utils.parse.xml.XmlNode;
import lombok.Data;

/**
 * 微信接口公共返回
 *
 * @author joe
 * @version 2018.07.02 09:59
 */
@Data
public class WxPublicResponse {
    /**
     * SUCCESS/FAIL
     * <p>
     * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    @XmlNode(name = "return_code")
    private String returnCode;
    /**
     * 返回信息，如非空，为错误原因
     * <p>
     * 例如：签名失败、参数格式校验错误
     */
    @XmlNode(name = "return_msg")
    private String returnMsg;
    /*-----------------以下字段在return_code为SUCCESS的时候有返回-----------------*/
    /**
     * 调用接口提交的应用ID
     */
    @XmlNode(name = "appid")
    private String appid;
    /**
     * 调用接口提交的商户号
     */
    @XmlNode(name = "mch_id")
    private String mchId;
    /**
     * 调用接口提交的终端设备号
     */
    @XmlNode(name = "device_info")
    private String deviceInfo;
    /**
     * 微信返回的随机字符串
     */
    @XmlNode(name = "nonceStr")
    private String nonceStr;
    /**
     * 微信返回的签名
     */
    @XmlNode(name = "sign")
    private String sign;
    /**
     * 业务结果,SUCCESS/FAIL
     */
    @XmlNode(name = "result_code")
    private String resultCode;
    /**
     * 错误代码
     */
    @XmlNode(name = "err_code")
    private String errCode;
    /**
     * 错误代码描述
     */
    @XmlNode(name = "err_code_des")
    private String errCodeDes;
    /*-----------------以上字段在return_code为SUCCESS的时候有返回-----------------*/
}
