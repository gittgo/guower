package com.ourslook.guower.utils.pay.weixin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 微信支付需要的数据vo
 *
 * app支付 和 微信小程序 支付 都是用该实体
 *
 * @author zhanglin
 * @time 2016-01-04 17:55
 * @see com.ourslook.guower.utils.pay.weixin.config.WxpayConfig
 * @see com.ourslook.guower.utils.pay.weixin.config.WxpayMiniProgremConfig
 */
@ApiModel(description = "微信APP支付需要的参数VO")
public class WxPayVo {

    @ApiModelProperty(value = "交易会话Id;如果是小程序:package")
    private String prepayid;
    @ApiModelProperty(value = "随机字符串")
    private String nonceStr;
    @ApiModelProperty(value = "时间戳")
    private String timeStamp;
    @ApiModelProperty(value = "签名")
    private String sign;
    @ApiModelProperty(value = "appid")
    private String appid;
    @ApiModelProperty(value = "商户号partnerid或者mchID")
    private String partnerid;
    @ApiModelProperty(value = "ordrNo;下单时候的订单号码")
    private String ordrNo;
    @ApiModelProperty(value = "packageValue;微信支付固定值：Sign=WXPay")
    private String packageValue = "Sign=WXPay";


    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getOrdrNo() {
        return ordrNo;
    }

    public void setOrdrNo(String ordrNo) {
        this.ordrNo = ordrNo;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }
}
