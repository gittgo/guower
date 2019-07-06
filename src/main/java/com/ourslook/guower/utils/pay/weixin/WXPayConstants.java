package com.ourslook.guower.utils.pay.weixin;

/**
 *
 */
public class WXPayConstants {
    /**
     * 返回状态码
     */
    public static final String RETURN_CODE = "return_code";

    /**
     * 返回提示信息
     */
    public static final String RETURN_MSG = "return_msg";

    /**
     * 应用ID
     */
    public static final String APPID = "appid";

    /**
     * 商户号
     */
    public static final String MCH_ID = "mch_id";

    /**
     * 商户号
     */
    public static final String PARTNERID = "partnerid";

    /**
     * 随机字符串
     */
    public static final String NONCE_STR = "nonce_str";

    /**
     * 随机字符串
     */
    public static final String NONCESTR = "noncestr";

    /**
     * 商品描述
     */
    public static final String BODY = "body";

    /**
     * 商户订单号
     */
    public static final String OUT_TRADE_NO = "out_trade_no";

    /**
     * 总金额
     */
    public static final String TOTAL_FEE = "total_fee";

    /**
     * 终端IP
     */
    public static final String SPBILL_CREATE_IP = "spbill_create_ip";

    /**
     * 通知地址
     */
    public static final String NOTIFY_URL = "notify_url";


    /**
     * 交易类型
     */
    public static final String TRADE_TYPE = "trade_type";


    /**
     * 签名
     */
    public static final String SIGN = "sign";

    /**
     * 支付完成时间
     */
    public static final String TIME_END = "time_end";

    /**
     * 微信支付订单号
     */
    public static final String TRANSACTION_ID = "transaction_id";

    /**
     * 业务结果
     */
    public static final String RESULT_CODE = "result_code";

    /**
     * 时间戳
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * 扩展字段
     */
    public static final String PACKAGE = "package";

    /**
     * 预支付交易会话标识
     */
    public static final String PREPAY_ID = "prepay_id";

    /**
     * 预支付交易会话标识
     */
    public static final String PREPAYID = "prepayid";

    /**
     * 回传参数
     */
    public static final String ATTACH = "attach";

    /**
     * 商户退款单号
     */
    public static final String OUT_REFUND_NO = "out_refund_no";

    /**
     * 退款金额
     */
    public static final String REFUND_FEE = "refund_fee";

    public enum SignType {
        MD5, HMACSHA256
    }

    public static final String DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String DOMAIN_API2 = "api2.mch.weixin.qq.com";
    public static final String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
    public static final String DOMAIN_APIUS = "apius.mch.weixin.qq.com";

    /**
     * 失败
     */
    public static final String FAIL     = "FAIL";

    /**
     * 成功
     */
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String MICROPAY_URL_SUFFIX     = "/pay/micropay";
    public static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    public static final String ORDERQUERY_URL_SUFFIX   = "/pay/orderquery";
    public static final String REVERSE_URL_SUFFIX      = "/secapi/pay/reverse";
    public static final String CLOSEORDER_URL_SUFFIX   = "/pay/closeorder";
    public static final String REFUND_URL_SUFFIX       = "/secapi/pay/refund";
    public static final String REFUNDQUERY_URL_SUFFIX  = "/pay/refundquery";
    public static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    public static final String REPORT_URL_SUFFIX       = "/payitil/report";
    public static final String SHORTURL_URL_SUFFIX     = "/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";

    // sandbox
    public static final String SANDBOX_MICROPAY_URL_SUFFIX     = "/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX   = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL_SUFFIX      = "/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX   = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX       = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX  = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX       = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX     = "/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL_SUFFIX = "/sandboxnew/tools/authcodetoopenid";

}

