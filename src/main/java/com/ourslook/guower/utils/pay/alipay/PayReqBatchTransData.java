package com.ourslook.guower.utils.pay.alipay;

/**
 * @ClassName: PayReqBatchTransData
 * @Description: 封装批量付款到支付宝账户请求参数
 * @see PayService batchTransService
 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.j0voyu&treeId=64&articleId=104804&docType=1
 * @author zhanglin
 * @date 2017/2/6 上午11:29
 */
public class PayReqBatchTransData {

    private String notify_url;//服务器异步通知页面路径 200
    private String pay_date; //支付日期 支付时间（必须为当前日期）。 YYYYMMDD 20080107;
    private String batch_no; //批量付款批次号;11～32位的数字或字母或数字与字母的组合，且区分大小写。 20080107001
    private String batch_fee; //付款总金额;格式：10.01，精确到分。
    private String batch_num; //付款总笔数;批量付款笔数（最少1笔，最多1000笔）。;1000
    //格式为：流水号1^收款方账号1^收款账号姓名1^付款金额1^备注说明1|流水号2^收款方账号2^收款账号姓名2^付款金额2^备注说明2。
    private String detail_data; //付款详细数据;付款的详细数据，最多支持1000笔。;

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getBatch_fee() {
        return batch_fee;
    }

    public void setBatch_fee(String batch_fee) {
        this.batch_fee = batch_fee;
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
}
