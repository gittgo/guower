package com.ourslook.guower.utils.pay.alipay;

import com.ourslook.guower.utils.pay.alipay.config.AlipayConfig;
import com.ourslook.guower.utils.pay.alipay.util.AlipaySubmit;
import com.ourslook.guower.utils.pay.alipay.util.UtilDate;

import java.util.HashMap;
import java.util.Map;

/**
 * 1：APP支付
 *
 * 2：批量付款到支付宝账号(接口有密)  提现/转账用  申请地址：https://b.alipay.com/order/appInfo.htm?salesPlanCode=2011052500326597&channel=ent
 *    接口要收费：最低一块钱；最高25；0.5%的费率
 */
public class PayService {

    //andoid,ios支付宝必须签约：【APP支付】

	/**
	 * 请求退款订单接口
	 *
	 * 支付宝退款说明（有密码和无密码）：http://www.jianshu.com/p/9357865ae424
	 *
	 * //有密文档(不用重新申请权限，但是要每笔交易都要后台管理人员输入密码，后台处理压力比较大)
     * 问答中心-->移动支付 https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.3L8x7e&treeId=62&articleId=104744&docType=1
	 *
	 * @throws Exception
	 */
	public static String refundHavePwd(PayReqRefundData data) throws Exception {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.Api.ALIPAY_SERVICE_REFUND);
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", data.getNotify_url());
        sParaTemp.put("seller_email", AlipayConfig.EMAIL);
		sParaTemp.put("seller_user_id", AlipayConfig.PARTNER);
		sParaTemp.put("refund_date", UtilDate.getDateFormatter());
		sParaTemp.put("batch_no", data.getBatch_no());
		sParaTemp.put("batch_num", data.getBatch_num());
		sParaTemp.put("detail_data", data.getDetail_data());

		// merchant_sign_type  md5
		// sign 签名值

		//建立请求  后台请求形式
		//String sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp);
		//return sHtmlText;

		//建立请求 (网页形式)
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		return sHtmlText;
	}

    /**
     * Created by jack on 2017/1/26.
	 * 转账 or 提现 到支付宝账号
     * 相当提现到支付宝账号
     * 要签约【批量付款到支付宝账户】  https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.a5Nzss&treeId=64&articleId=103569&docType=1
	 *
	 * 接口报错，错误代码 ILLEGAL_PARTNER_EXTERFACE 调试错误，请回到请求来源地，重新发起请求。错误代码
	 * https://support.open.alipay.com/support/hotProblemDetail.htm?spm=a219a.7386797.0.0.0y8zzk&id=226434
	 *
	 *
	 *
	 * 要签约的权限：
	 * 新批量付款到支付宝账
	 * 批量付款到支付宝账号(接口有密)
	 *
	 *
	 * 接口报错：批量付款申请提交失败  证书有误，无法复核批次记录  http://www.lnctime.com/1827.html  数字证书的问题
	 * 浏览器条件：ie8+  32 http://blog.csdn.net/jbk3311/article/details/52299902 要安装支付宝数字证书
     */
	public static String batchTransService(PayReqBatchTransData data) throws Exception {
        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.Api.ALIPAY_SERVICE_BATCH_TRANS);
        sParaTemp.put("partner", AlipayConfig.PARTNER);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", data.getNotify_url());
		//付款账号 付款方的支付宝账号，支持邮箱和手机号2种格式。 eg:exiaodao@exiaodao.com
        sParaTemp.put("email", AlipayConfig.EMAIL);
		//付款账号名 付款方的支付宝账户名。eg:毛毛
        sParaTemp.put("account_name", AlipayConfig.account_name);
        sParaTemp.put("pay_date", data.getPay_date());
        sParaTemp.put("batch_no", data.getBatch_no());
        sParaTemp.put("batch_fee", data.getBatch_fee());
        sParaTemp.put("batch_num", data.getBatch_num());
        sParaTemp.put("detail_data", data.getDetail_data());
        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");

        return sHtmlText;
    }

}
