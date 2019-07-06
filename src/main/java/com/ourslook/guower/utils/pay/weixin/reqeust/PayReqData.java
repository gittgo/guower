package com.ourslook.guower.utils.pay.weixin.reqeust;

/**
 * 统一下单请求数据封装
 *
 * @author zhanglin
 * @time 2016-01-04 12:32
 */
@SuppressWarnings("all")
public class PayReqData {

    //公众账号ID	微信分配的公众账号ID（企业号corpid即为此appId）
    private String appid = "";
    //商户号		微信支付分配的商户号
    private String mch_id = "";
    //设备号		终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    private String device_info = "";
    //随机字符串	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str = "";
    //签名		签名，详见签名生成算法
    private String sign = "";
    //商品描述		商品或支付单简要描述
    private String body = "";
    //附加数据		附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    private String attach = "";
    //商户订单号	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private String out_trade_no = "";
    //总金额		订单总金额，单位为分
    private Integer total_fee;
    //终端IP		APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    private String spbill_create_ip = "";
    //交易起始时间	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
    private String time_start = "";
    //交易结束时间	订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
    private String time_expire = "";
    //商品标记		商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    private String goods_tag = "";
    //通知地址		接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    private String notify_url = "";
    //交易类型		取值如下：JSAPI，NATIVE，APP
    private String trade_type = "";
    //商品ID		trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    private String product_id = "";
    //指定支付方式	no_credit --指定不能使用信用卡支付
    private String limit_pay = "";
    //用户标识
    private String openid = "";
    //微信订单号
    private String transaction_id = "";

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

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    @Override
    public String toString() {
        return "PayReqData [appid=" + appid + ", mch_id=" + mch_id
                + ", device_info=" + device_info + ", nonce_str=" + nonce_str
                + ", sign=" + sign + ", body=" + body + ", attach=" + attach
                + ", out_trade_no=" + out_trade_no + ", total_fee=" + total_fee
                + ", spbill_create_ip=" + spbill_create_ip + ", time_start="
                + time_start + ", time_expire=" + time_expire + ", goods_tag="
                + goods_tag + ", notify_url=" + notify_url + ", trade_type="
                + trade_type + ", product_id=" + product_id + ", limit_pay="
                + limit_pay + ", openid=" + openid + ", getAppid()="
                + getAppid() + ", getMch_id()=" + getMch_id()
                + ", getDevice_info()=" + getDevice_info()
                + ", getNonce_str()=" + getNonce_str() + ", getSign()="
                + getSign() + ", getBody()=" + getBody() + ", getAttach()="
                + getAttach() + ", getOut_trade_no()=" + getOut_trade_no()
                + ", getTotal_fee()=" + getTotal_fee()
                + ", getSpbill_create_ip()=" + getSpbill_create_ip()
                + ", getTime_start()=" + getTime_start()
                + ", getTime_expire()=" + getTime_expire()
                + ", getGoods_tag()=" + getGoods_tag() + ", getNotify_url()="
                + getNotify_url() + ", getTrade_type()=" + getTrade_type()
                + ", getProduct_id()=" + getProduct_id() + ", getLimit_pay()="
                + getLimit_pay() + ", getOpenid()=" + getOpenid()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }

}
