package com.ourslook.guower.utils.pay.weixin.config;


import com.ourslook.guower.utils.pay.weixin.common.HttpsRequest;

/**
 * 微信支付基础数据配置
 *
 * @author zhanglin
 * @time 2016-01-04 12:07
 */
public class WxpayConfig {
    /**
     * 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
     * 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
     * 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改
     * 商户平台配置的一个私钥,用来验证签名的
     * 商户支付密钥
     * 微信商户平台-->API安全-->设置api密钥(需要客户的手机验证码，电脑需要安装两个证书才能修改)
     * private static String key = "tongyansiquliudaliudaapp12345678";
     * private static String key = "exiaodao20161212application12345";
     */
    private static String key = "b939114adda0a18ce991b00deaf8fc19";

    /**
     * 微信分配的公众号ID（开通公众号之后可以获取到）
     * private static String AppID = "wxf561071f6c82a1d4";
     * 应用APPID
     */
    private static String AppID = "wx9030f13347f2ca2b";

    /**
     * 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到），10位数字  参见商户平台 https://pay.weixin.qq.com
     * 微信支付商户号
     * //private static String mchID = "1332972201";
     */
    private static String mchID = "1502948571";

    /**
     * 受理模式下给子商户分配的子商户号
     */
    private static String subMchID = "";

    /**
     * 微信安全规范：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3
     * HTTPS证书的本地路径 证书和密钥都要客户提供
     * 微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->证书下载
     * 撤销、退款申请API中调用
     *
     * @see HttpsRequest#init()
     */
    private static String httpsCertLocalPath = "classpath:config/wxpay/apiclient_cert.p12";

    /**
     * HTTPS证书密码，默认密码等于商户号MCHID
     * private static String httpsCertPassword = "1332972201";
     */
    private static String httpsCertPassword = "1502948571";

    /**
     * 是否使用异步线程的方式来上报API测速，默认为异步模式
     */
    private static boolean useThreadToDoReport = true;

    /**
     * JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
     *
     * MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
     *
     * 已经用到的：
     * 1: APP 原生app支付
     * 2：微信小程序或者公众号支付：JSAPI
     */
    private static String trade_type = "APP";

    //机器IP
    private static String ip = "";

    /**
     * //"com.wxpay.common.HttpsRequest";
     */
    public static String HttpsRequestClassName = HttpsRequest.class.getName();

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        WxpayConfig.key = key;
    }

    public static String getAppID() {
        return AppID;
    }

    public static void setAppID(String appID) {
        WxpayConfig.AppID = appID;
    }

    public static String getMchID() {
        return mchID;
    }

    public static void setMchID(String mchID) {
        WxpayConfig.mchID = mchID;
    }

    public static String getSubMchID() {
        return subMchID;
    }

    public static void setSubMchID(String subMchID) {
        WxpayConfig.subMchID = subMchID;
    }

    public static String getHttpsCertLocalPath() {
        return httpsCertLocalPath;
    }

    public static void setHttpsCertLocalPath(String httpsCertLocalPath) {
        WxpayConfig.httpsCertLocalPath = httpsCertLocalPath;
    }

    public static String getHttpsCertPassword() {
        return httpsCertPassword;
    }

    public static void setHttpsCertPassword(String httpsCertPassword) {
        WxpayConfig.httpsCertPassword = httpsCertPassword;
    }

    public static boolean isUseThreadToDoReport() {
        return useThreadToDoReport;
    }

    public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
        WxpayConfig.useThreadToDoReport = useThreadToDoReport;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        WxpayConfig.ip = ip;
    }

    public static void setHttpsRequestClassName(String httpsRequestClassName) {
        HttpsRequestClassName = httpsRequestClassName;
    }

    public static String getTrade_type() {
        return trade_type;
    }

    public static void setTrade_type(String trade_type) {
        WxpayConfig.trade_type = trade_type;
    }
}
