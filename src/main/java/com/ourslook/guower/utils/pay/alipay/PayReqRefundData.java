package com.ourslook.guower.utils.pay.alipay;

/**
 * 封装退款请求参数
 *
 * @author zhanglin
 * @time 2016-01-04 12:32
 * @see PayService refundHavePwd
 */
public class PayReqRefundData {

    private String batch_no = "";
    private String batch_num = "";
    /**
     * 退款详细数据，必填，格式（支付宝交易号^退款金额^备注），多笔请用#隔开
     */
    private String detail_data = "";
    private String notify_url = "";

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getBatch_num() {
        return batch_num;
    }

    public void setBatch_num(String batch_num) {
        this.batch_num = batch_num;
    }

    public String getDetail_data() {
        return detail_data;
    }

    public void setDetail_data(String detail_data) {
        this.detail_data = detail_data;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
