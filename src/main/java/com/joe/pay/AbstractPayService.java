package com.joe.pay;

import com.joe.http.IHttpClientUtil;
import com.joe.pay.pojo.prop.PayProp;

import static com.joe.utils.validator.ValidatorUtil.validate;

/**
 * 抽象支付服务类
 *
 * @author joe
 * @version 2018.07.05 10:32
 */
public abstract class AbstractPayService implements PayService {
    protected static final IHttpClientUtil DEFAULT_CLIENT = new IHttpClientUtil();
    /**
     * 当前是否是沙箱，true表示是，false表示不是
     */
    protected boolean sandbox;

    public AbstractPayService(PayProp prop) {
        //验证参数
        validate(prop);
        PayProp.Environment environment = prop.getEnvironment();
        environment = environment == null ? PayProp.Environment.PROD : environment;
        switch (environment) {
            case PROD:
                this.sandbox = false;
                useSandbox(false);
                break;
            case SANDBOX:
                this.sandbox = true;
                useSandbox(true);
                break;
            default:
                throw new IllegalArgumentException("未知支付环境：" + environment);
        }
    }

    /**
     * 启动/关闭沙箱模式
     *
     * @param sandbox true表示启用沙箱模式，false表示关闭沙箱模式
     */
    public abstract void useSandbox(boolean sandbox);
}
