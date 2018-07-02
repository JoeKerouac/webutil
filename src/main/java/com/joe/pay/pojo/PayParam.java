package com.joe.pay.pojo;

import lombok.Data;

/**
 * 支付参数
 *
 * @author joe
 * @version 2018.07.02 10:36
 */
@Data
public class PayParam {
    /**
     * 系统唯一订单号
     */
    private String orderId;
    /**
     * 对一笔交易的描述信息
     */
    private String body;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。
     * <p>
     * 对于微信支付来说需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     */
    private String subject;
    /**
     * 订单生成时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String createTime;
    /**
     * 支付时长，单位为秒，如果设置300就是五分钟，超过五分钟订单自动取消
     */
    private long expire;
    /**
     * 订单金额，单位为分
     */
    private int totalAmount;
    /**
     * 发起请求的用户的实际IP
     */
    private String ip;
    /**
     * 支付类型：APP、WEB（微信对应公众号，支付宝对应网页支付）
     */
    private String tradeType;
    /**
     * 用户openid，只有在微信公众号支付时需要
     */
    private String openid;
}
