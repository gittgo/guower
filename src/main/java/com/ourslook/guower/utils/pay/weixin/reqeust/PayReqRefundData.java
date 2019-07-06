package com.ourslook.guower.utils.pay.weixin.reqeust;

/**
 * 请求退款接口数据封装
 *
 * @author zhanglin
 * @time 2016-01-04 12:32
 */
@SuppressWarnings("all")
public class PayReqRefundData {

    //公众账号ID	微信分配的公众账号ID（企业号corpid即为此appId）
    private String appid = "";
    //商户号		微信支付分配的商户号
    private String mch_id = "";
    //随机字符串	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str = "";
    //签名		签名，详见签名生成算法
    private String sign = "";
    //签名类型 	sign_type  	HMAC-SHA256 	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    private String sign_type = "MD5";
    //微信订单号
    private String transaction_id = "";
    //商户订单号	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private String out_trade_no = "";
    //商户退款单号 	商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    private String out_refund_no = "";
    //总金额		订单总金额，单位为分
    private int total_fee = 0;
    //退款金额 		退款总金额，订单总金额，单位为分，只能为整数
    private int refund_fee = 0;
    //退款结果通知url
    private String notify_url;
    //退款原因 	refund_desc 	否 	String(80) 	商品已售完; 非必填；
    private String refund_desc;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public void setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
    }

    @Override
    public String toString() {
        return "PayReqRefundData{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", out_refund_no='" + out_refund_no + '\'' +
                ", total_fee=" + total_fee +
                ", refund_fee=" + refund_fee +
                ", notify_url='" + notify_url + '\'' +
                ", refund_desc='" + refund_desc + '\'' +
                '}';
    }
}
