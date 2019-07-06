package com.ourslook.guower.utils.pay.ips;

/**
 * 环迅支付相关配置
 * <p>
 * 下载demo资料：https://download.csdn.net/download/ab601026460/10402876
 *
 * 官网：http://www.ips.com.cn
 * 后台商户登录地址：:https://newmer.ips.com.cn/pfas-mfes/
 * @author dazer
 * @date 2018/5/9 下午2:51
 */
public class HxIpsConfig {
    /**
     * merCode 商户号(用来做验签，需与页面上传输的商户号保持一致)
     */
    public static final String MER_CODE = "209196";
    /**
     * URL formAction 商户订单提交的接口
     */
    public static final String FORM_ACTION = "https://newpay.ips.com.cn/psfp-entry/gateway/payment.do";
    /**
     * 商户证书 directStr
     */
    public static final String DIRECT_STR = "t977Jftn09PN7HsR3c1A22SK4x78OHR2mZ0bo6gJUB0kdbw2nUXnJcdmmBCn623zGE1ZjLnJMR1cyBn9NZPBMeHttPSAT2q89d7mEWw446Pzz8yV9JeAqepn9OEGVQZd";
    /**
     * ips公钥 ipsRsaPub
     */
    public static final String IPS_RSA_PUB = "https://newpay.ips.com.cn/psfp-entry/gateway/payment.do";
}
