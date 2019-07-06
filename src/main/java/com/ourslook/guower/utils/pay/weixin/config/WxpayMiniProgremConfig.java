package com.ourslook.guower.utils.pay.weixin.config;

import com.ourslook.guower.utils.pay.weixin.PayMinProgressUtil;

/**
 * 微信小程序支付相关配置
 * @ClassName: WxpayMiniProgremConfig
 * @Description:
 * @author dazer
 * @date 2018/3/9 下午8:28
 *
 * 小程序支付api:https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_10&index=1#
 *
 * @see PayMinProgressUtil
 *
 * 小程序支付就两个类
 */
public class WxpayMiniProgremConfig {
    /**
     * 微信商户号mchID在WxpayConfig中配置
     * // grant_type填写为 authorization_code
     *
     * MCH_ID：微信支付分配的商户号ID
     * SIGN_KEY:商户支付密钥
     *
     * APPID:小程序appid
     *
     *
     */
    public static final String APPID = "wx6656d3436d8cde89";
    public static final String APP_SECRECT = "17c618fd2ea25fe48205365520c5d9fb";
    public static final String MCH_ID = WxpayConfig.getMchID();
    /**
     * 小程序和微信公众号支付：这里都是JSAPI
     */
    public static final String TRADE_TYPE = "JSAPI";
    /**
     * 一下两个参数是小程序微信支付特有的
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String SIGN_KEY = WxpayConfig.getKey();
}
