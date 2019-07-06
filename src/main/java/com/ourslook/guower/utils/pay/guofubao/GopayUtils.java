package com.ourslook.guower.utils.pay.guofubao;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

public class GopayUtils {
	//来一些默认 初始化参数
    public static String version ="2.2";

    public static String charset ="2";

    public static String language ="1";

    public static String signType ="1";

    public static String tranCode ="8888";

    //商户ID(merchantID)
    public static String merchantID = "0000001502";
    /* public static String merchantID = "0000071769";*/

    public static String feeAmt ="0.0";

    public static String respCode = "0000";

    public static String msgExt = "交易成功";

    public static String currencyType ="156";

   /* public static String frontMerUrl ="http://47.100.34.60/qiquanbao";*/
   public static String frontMerUrl ="";
    public static String virCardNoIn = "0000000002000000257";
   /* public static String virCardNoIn = "0000000002000018110";*/


    public static String isRepeatSubmit = "1";

    /**
     * 标记为移动支付接口
     */
    public static String buyerName = "MWEB";

    public static String verficationCode ="11111aaaaa";

	/**
	 * 获取国付宝服务器时间 用于时间戳
	 * @return 格式YYYYMMDDHHMMSS
	 */
	public static String getGopayServerTime() {

		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000);
		GetMethod getMethod = new GetMethod(GopayConfig.gopay_server_time_url);
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		// 执行getMethod
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(getMethod);			
			if (statusCode == HttpStatus.SC_OK){
				String respString = StringUtils.trim((new String(getMethod.getResponseBody(),"UTF-8")));
				return respString;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
    /**
     * 对字符串进行MD5签名
     * 
     * @param text
     *            明文
     * 
     * @return 密文
     */
    public static String md5(String text) {
        return DigestUtils.md5Hex(getContentBytes(text, GopayConfig.input_charset));
    }
    
    /**
     * 对字符串进行SHA签名
     * 
     * @param text
     *            明文
     * 
     * @return 密文
     */
    public static String sha(String text) {
        return DigestUtils.shaHex(getContentBytes(text, GopayConfig.input_charset));
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }

        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
