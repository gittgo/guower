package com.ourslook.guower.utils.pay.weixin;

import com.ourslook.guower.utils.pay.weixin.common.IServiceRequest;
import com.ourslook.guower.utils.pay.weixin.config.WxpayConfig;
import com.ourslook.guower.utils.pay.weixin.reqeust.PayReqData;
import com.ourslook.guower.utils.pay.weixin.reqeust.PayReqRefundData;
import com.ourslook.guower.utils.pay.weixin.reqeust.PayReqRefundQueryData;

/**
 * 微信支付-商户平台 所有的支付操作 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_3
 */
@SuppressWarnings("all")
public class PayService {

    /**
     * 统一下单接口地址(必须用的一个接口)
     * 统一下单1：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
     * 统一下单2：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
     * 统一下单3：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1
     * 调起支付接口：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2
     * 微信支付-商品平台 下单业务流程: https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_3
     */
    public static String API_PAY_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 订单查询接口 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_2&index=4
     */
    public static String API_PAY_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 申请退款 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_4&index=6
     * 该接口需要安装微信的证书
     */
    public static String API_PAY_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 退款接口查询地址  查询退款  https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_5&index=7
     */
    public static String API_PAY_REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";


    /**发请求的HTTPS请求器*/
    private static IServiceRequest serviceRequest;

    /**
     * 请求统一下单接口
     * //@param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     *
     * @return API返回的数据
     * @throws Exception
     */
    public static String request(PayReqData data) throws Exception {
        String responseString = null;
        Class c = Class.forName(WxpayConfig.HttpsRequestClassName);
        serviceRequest = (IServiceRequest) c.newInstance();
        responseString = serviceRequest.sendPostNoSSL(PayService.API_PAY_UNIFIED_ORDER_URL, data);
        return responseString;
    }

    /**
     * 查询订单接口
     *
     * @return API返回的数据
     * @throws Exception
     */
    public static String queryRequest(PayReqData data) throws Exception {
        Class c = Class.forName(WxpayConfig.HttpsRequestClassName);
        serviceRequest = (IServiceRequest) c.newInstance();
        String responseString = serviceRequest.sendPostNoSSL(API_PAY_ORDER_QUERY, data);
        return responseString;
    }

    /**
     * 请求退款接口
     *
     * @return API返回的数据
     * @throws Exception
     */
    public static String payRefund(PayReqRefundData data) throws Exception {
        Class c = Class.forName(WxpayConfig.HttpsRequestClassName);
        serviceRequest = (IServiceRequest) c.newInstance();
        String responseString = serviceRequest.sendPost(API_PAY_REFUND_URL, data);
        return responseString;
    }

    /**
     * 退款接口查询地址
     *
     * @return API返回的数据
     * @throws Exception
     */
    public static String payRefundQuery(PayReqRefundQueryData data) throws Exception {
        Class c = Class.forName(WxpayConfig.HttpsRequestClassName);
        serviceRequest = (IServiceRequest) c.newInstance();
        String responseString = serviceRequest.sendPost(API_PAY_REFUND_QUERY_URL, data);
        return responseString;
    }

}
