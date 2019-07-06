package com.ourslook.guower.utils.pay.guofubao;

/**
 * 官方网站：https://www.gopay.com.cn/
 * 开发资料：官方网站-->客户服务--->文件下载---->国付宝人民币标准网关接口手册V2.2
 * https://www.gopay.com.cn/helpcenterV2/channel-7050.html
 * or
 * https://www.gopay.com.cn/helpcenter/channel-7050.html
 * <p>
 * <p>
 * <p>
 * 商户号 0000071769
 * 商户识别码 huazan171214
 * 收款人账号 0000000002000018110
 * 深圳盛世嘉鸿贸易有限公司
 * <p>
 * ---------------
 * 类名：GopayCommonConfig
 * 功能：基础配置类
 * 详细：设置帐户有关信息及前后台通知地址
 * 版本：2.1
 * 日期：2012-06-27
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究国付宝接口使用，只是提供一个参考。
 */
public class GopayConfig {
    /**
     * 国付宝提供给商户调试的网关地址
     */
//    public static String gopay_gateway = "https://gateway.gopay.com.cn/Trans/WebClientAction.do";//old
//    public static String gopay_gateway = "https://gatewaymer.gopay.com.cn/Trans/WebClientAction.do";//new ok过
//    public static String gopay_gateway = "https://gateway.gopay.com.cn/Trans/MobileClientAction.do";
    public static String gopay_gateway = "https://gatewaymer.gopay.com.cn/Trans/MobileClientAction.do?";

    /**
     * 国付宝服务器时间，反钓鱼时使用
     */
    public static String gopay_server_time_url = "https://gateway.gopay.com.cn/time.do";

    /**
     * 字符编码格式，目前支持 GBK 或 UTF-8
     */
    public static String input_charset = "UTF-8";
}
