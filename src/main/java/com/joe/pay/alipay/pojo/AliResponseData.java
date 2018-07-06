package com.joe.pay.alipay.pojo;

/**
 * 阿里原始响应接口
 *
 * @author joe
 * @version 2018.07.06 18:11
 */
public interface AliResponseData<T extends AliPublicResponse> {
    /**
     * 获取数据
     *
     * @return 对应的数据
     */
    T getData();

    /**
     * 获取签名
     *
     * @return 签名
     */
    String getSign();
}
