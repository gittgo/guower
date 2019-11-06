package com.ourslook.guower.utils.pay.weixin.config;

/**
 * 微信公众号相关配置
 */
public class WxPublicNumberConfig {

    /**
     * 微信分配的公众号ID（开通公众号之后可以获取到）
     */
    private static String appId = "wxaf0c55277e5973ef";

    /**
     * 要注册人用weixin扫描才能看到取值
     */
    private static String appSecret = "d6512a662d433830dcf9dbed0f6fc19f";

    /**
     * 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
     * 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
     * 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改
     * 商户平台配置的一个私钥,用来验证签名的
     * 商户支付密钥
     * 微信商户平台-->API安全-->设置api密钥(需要客户的手机验证码)
     */
    private static String key = "";

    /**
     * 	微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）  参见商户平台 https://pay.weixin.qq.com
     */
    private static String mchID = "";


    public static String getAppId() {
        return appId;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    public static String getKey() {
        return key;
    }

    public static String getMchID() {
        return mchID;
    }
}
