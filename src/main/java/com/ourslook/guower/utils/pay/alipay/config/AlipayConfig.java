package com.ourslook.guower.utils.pay.alipay.config;


import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;

/**
 * @author dazer
 *
 * 类名：AlipayConfig
 * 功能：基础配置类
 * 详细：设置帐户有关信息及返回路径
 * 版本：1.0
 * 日期：2016-06-06
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *
 * 蚂蚁金服-->首页--->我的商家服务-->产品中心--->我的产品 （支付宝开放平台开发者服务协议、开放平台服务商身份协议、自助签约--高级手机网站支付V4、即时到账无密退款V3）
 *
 * 开通所有的产品
 * https://b.alipay.com/signing/authorizedProductSet.htm#/
 * 如：
 * APP支付
 * 手机网站支付
 * 电脑网站支付
 * APP支付宝登录
 * 当面付：收银员使用扫描枪进行扫描客户二维码
 * 单笔转账到支付宝账户(转账到支付宝账户)
 *
 * 支付宝支付要配置-->PID和公钥管理-->开放平台密钥     https://open.alipay.com/platform/keyManage.htm  (应用公钥，支付宝公钥，授权回调地址(随便填))
 * 支付宝退款要配置-->PID和公钥管理-->mapi网关产品密钥 https://open.alipay.com/platform/keyManage.htm
 *
 * 开发中常见报错：系统繁忙，请稍后再试；al140247 ALI40247 https://openclub.alipay.com/read.php?tid=250&fid=2
 * 支付宝错误代码：https://docs.open.alipay.com/common/105806
 *
 * 查看应用所有的应用签约状态：https://openhome.alipay.com/platform/appManage.htm
 *
 * 支付宝认证需要的东西：营业执照、appicon、店铺门面照片、店内3张照片、
 *
 *
 * 如何开通花呗与信用卡收款：目前您成功签约当面付/APP支付/电脑网站支付/手机网站支付等产品都是默认开通信用卡和花呗渠道的，如您已签约成功后无法使用信用卡或花呗收款，可咨询在线人工客服，并提供您签约的支付宝账号和公司名称
 */
public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /**
     * 合作身份者ID(PID)，以2088开头由16位纯数字组成的字符串  2088521108139413
     * 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
     * exiaodao: 2088021320955403
     */
    public static final String PARTNER = "2088031778677150";

    /**
     * 商户私钥，pkcs8格式;这个私钥要自己生成的
     * 商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
     *
     * "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANtmdgaAilCmuR8oce/0fGmnE6naQwPpMqRuzLf1XIMG76F5f+zSZLxN4EbzgU6lqivfXEcWDWvoIRUBFyydsthOL2m2fMpYazgO0uR1EJInfN+dSlqvlx+53op+9tk6OHDeS02Oj8u10plw/geK4LL9YwPUbOCXWyYW2I04t/kvAgMBAAECgYEAziuFg2ytDUbutSHbl6KGev3E2DAEt/DsBNSmxednJ8a9gFnvhKjiPQhamsOuuogdGPLoh1N9XXICKXishFq6e0gEC6KhleTQHEyIPvV/t2juQkY9l4zHZ3XH/BrviWVfHTPOvtsrDjJ3qVbue35rtzFK8Ov/7KxS1O/NTEaFTkCQQD2wN5v3Nrzc372QccG+RTIzADol5Q7HoqtxgKapVu8UyMQBHfmPk8ZmR3hjq6OUTxQqO8SG52WblM2f/GKPqGtAkEA458wwgBf64sd5Mp0Mj3OAY/5U71bTIFHdJ79SqyF4yRCZNsgjZID3kfgRHsrIz7nhN3ccDqTf44xXIz/S495ywJBAOR5TPzy2GEXDfXijfCs7UvHaQf0YjIjY3DnqT2b4scLi6mnGYW9J+yh1AMn5ASuv1RIN6TioRXK7Y3HBdVljLkCQG8ovcHwNuern6SsrgQfJKykp+kaLyz0cXXlcuL1z8i2W2ovjP02EoeJ6F7pG47x2FSNmGqCYKfJRBfjj4VSAvMCQQDJ5dvwwpDw3rv6D8fzFr0Zertb1qJDgmewE18W8HtM3d0Z0+5h9Kf1t6UtShc29PWY1piw/kKSjxFhXdg+A2SD
     */
    public static final String  RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbEwHG/GdR/d6TP25lSKlP19voQVDPE5iT4d5o/CuTlAGnvwfEF7G3DDy6tROBS/9OHMslr8kdgv3cq0J4f/A1lsxOwaa+aCfMTvi3vNo6/a1RKcbnVGsuOPeju4OaDUC+/VFcxVevmb/A8ghrq3ybWGhgN9cZyHRuaOnZcSy4lL0a6nsU1qE2sxWGL92N5lapTDRR1L8HPVkdmvnpcMdUbGuHzpopNov+lU3+ycJ0Nfr77/lmPLjHQ7SJAHpFcPMi34vsB1krBA1nul37G7b1yZ4kjovHTO6EFAUZMmGV8DdvIe+EqmADLh4aYceRNTXaXbp7xqgk6UuZe/G1IVctAgMBAAECggEALF+e9ZKJ+hM72kSfUUoecKNi4K238tk3orFSFBZ8XalPY/GsxjbFeox84CwqXagicPaaRrlrQg+WlstGpPZvBNaWtb6B+SASq0q5rtHSXGj0bDIZIV6toLdaXTat7Kn4Ke6Qlow9kBjdntdjA3Q5nrYIf+fanB0Y+/L3X2ocEuDCAVJDI/q8HJHeHT4V4PAs9sdJLTXUgh7G+aPzJehNKWQaN3aK+eKn8qK1IxJVkaYYKNCBKdc2QsUWZODv04kEX9LKx0a1N+vXKc3pyce2bcrRN8WyomekBdwnU26v2NlEui6efBYdxNqoOZpsBoBNJcFtKDmCiLwEwkzpyjBQPQKBgQDf8ShH6yla5yU+0kwWMw5aensn0BFovk9UxhOJlrXgaF6zxhrrq5xlK1OKIyERteMo3QgALNwrGXbYFUGH1/Mq3WhPKLr2SMjx7KYM9XMJ89IVIHmI2AgDNkMCe2AB0+YoOHgcOfHk4CTf821HjpHys0mtHAA5e8Hr8XrPYCU3uwKBgQCxRgpFiLDIIQf+VJodj8E63pzY9BBjUolIqQOH1RepIKXZ/+nX5wH4jrgcKBzTXO1auqEf1qkbn7Gf9t/Srq1+iy4PGU7BO1vCfmjBISi8AN8Zel7U0dVlpwK5pKIugs47c2SCZjEzORct9VaFNmKBjSBhY1e2Cg5GrxgGnmY6NwKBgBAfHlU/GrUOmuHQ5eVbO5dL5f3Rvs8ZlEB4GgP3s5Q6L0cl5d49dn7aBaOWL9VEkNvqPZ81m4wGw0V/Ej6jT0AwvEemmP8N8lloPfQE34lB4kffFKDDXKBOkbhmBV1cUd9nYU425bEiacUtkxppqxwbyBwMPf/lZuqDceHn/AXtAoGADnSHoePjhm4rGLINXxu5xfMhFw/PxkmUqDgf8ZOOulvPKleT2eccYghBOwqKTzHkqOFPAime4IHFGiYnWiakPw1aVCDcKyWL9fAY2qa5i5sGli1ETlZN8FwN/NVP5DsGVhKyv29V07d0DGtVAsfMgIPhgA95t35aa6kbbG6+J0ECgYEAgDT8zx2ecnwVm65bomRv2VB7xKP9B5TtTxoV56KyNB1p/1TwGFJIHURqTh8lpltI6hc3qnpvoRw69Ik/tjx7/c1/TuRJ3DX1xutSmTVqanlSIWJ2bKULBXZeWZd9/F4/2MY+UkZ7kKl+Da62g1y5OhTpe8dT/BmptuT78QWoEQQ=";


    /**
     * 应用公钥(RSA1widthRsa)和支付宝公钥查看地址 https://openhome.alipay.com/platform/detailApp.htm?appId=2017050907175656&tab=appSetting
     */
    public static final String APPLICATION_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjFA5m3n3A6Yc+CL3Lw2Y/IQzQbaMuUnrNtN/dludy2k5Tz1HxMzm8FoZwAdGg8BkKtWnIM7aL/+at0jb7n38SgG1XdW2lqV1x7Ez5kSrK0qaa5zGflCi0X7sft8rSOynD0TfldTUuVIkVRMREM2vlj+YN8fEl9IiVUouqEfPuvkCE/aqgIkXswMezcYY97eQbu/6ulHsz3g0D4QbtWMrxWPrORw50PkL8JaQR0EWruAl4fp4EJEPLkbMcZHmp5LIwrOD0gyGUW3CkGbdJEMiE6fBjyQR5/nf5qybouJ5PpQoWjf4w7YVUiCRwvCN6XcfMCUpZT53kUwhprkraZFIGwIDAQAB";

    /**
     * 支付宝的公钥，无需修改该值
     */
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgnieoe8r9K24LlF0Lv//dGyTBQiPgLWRxvzCo2cJNc0K7+IrD54s1HZyZhDCwtGJuzYDz1aYokoBASknAxpz2SAbLE4R/Gruf1Zs6cC/2rVsna4u1kYAIOl+08sQbQy2Jsl20TWg7pJDKgceQBlt1BWxyxV9nPkvYvLG+/pdSXVZcSp8ifpl+wLI06+/32j8j3KkdF4UxtsKqHH4ki9CV6+mJSccE/SGLoXqZrypbkfYtGXgEHEro2EoV17uTlwIf4S5FcgnQcXEM7EyZ6sqFrYgLwYmNVh8Xp8WK9Eqj0ChdZY3zHmFXaXyo9WX7+SjPfIcjRLZ9st1Y07J6ATg5wIDAQAB";

    /**
     * 查看appid:https://openhome.alipay.com/platform/keyManage.htm
     * 支付宝支付业务：入参app_id
     * exiaodao
     *      apppid:2016112103041339
     *      EMAIL:exiaodao@exiaodao.com
     *      account_name:上海孝道信息技术有限公司
     */
    public static final String APPID = "2018042360007452";
    public static final String EMAIL = "313177692@qq.com";
    public static final String account_name = "青岛今朝天创创业服务有限公司";

    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 调试用，创建TXT日志文件夹路径
     */
    public static  final String log_path = XaUtils.getWebUploadVirtalRootPath(ServletUtils.getHttpServletRequest(), Constant.SERVIER_NAME_SUFFIX);

    /**
     * 字符编码格式 目前支持 gbk 或 utf-8 UTF-8
     */
    public static final String input_charset = "UTF-8";

    /**
     * 商户生成签方法
     * 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     * 签名方式 不需修改
     * RSA2
     */
    public static final String merchant_sign_type = "RSA2";


    /**
     * 支付宝应用公钥有两种签名方式
     * SHA256withRsa：支付宝推荐
     * SHA1withRsa:另外一种加密方式
     */
    public static  final String ALIPAY_SIGN_ALGORITHMS = "SHA256withRsa";

    /**
     * 全部文档中找：app支付相关；页面展开可以看到所有的支付宝api和产品介绍
     * 支付宝支付接口
     * https://docs.open.alipay.com/204/105051
     * https://docs.open.alipay.com/204/105303/
     * https://docs.open.alipay.com/api_1/alipay.trade.query
     * https://docs.open.alipay.com/api_1/alipay.trade.refund
     * 支付宝资金接口
     * https://docs.open.alipay.com/309/106235/
     * https://docs.open.alipay.com/309/106237/ 单笔转账到支付宝账户产品
     * 花呗分期商家接入说明
     * https://docs.open.alipay.com/277/106748/
     */
    public static final class Api {
        /**正式环境-网关*/
        public static final String ALIPAY_GATEWAY_PROD = "https://openapi.alipay.com/gateway.do";
        /**App支付接口*/
        public static final String ALIPAY_TRADE_APP_PAY = "alipay.trade.app.pay";
        /**交易状态查询接口*/
        public static final String ALIPAY_TRADE_QUERY = "alipay.trade.query";
        /**交易退款接口*/
        public static final String ALIPAY_TRADE_REFUND = "alipay.trade.refund";
        /**交易退款查询接口*/
        public static final String ALIPAY_TRADE_FASTPAY_REFUND_QUERY = "alipay.trade.fastpay.refund.query";
        /**
         * 支付宝资金接口
         * https://docs.open.alipay.com/309/106235/
         * @see com.alipay.api.request.AlipayFundTransToaccountTransferRequest
         */
        /**单笔转账到支付宝账户接口--用于向指定支付宝账户转账*/
        public static final String ALIPAY_FUND_TRANS_TOACCOUNT_TRANSFER = "alipay.fund.trans.toaccount.transfer";
        /**单笔转账到支付宝账户接口--用于查询转账结果*/
        public static final String ALIPAY_FUND_TRANS_ORDER_QUERY = "alipay.fund.trans.order.query";

        /**
         * 退款： 调用的接口名(即时到账有密退款接口)，无需修改 https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.3L8x7e&treeId=62&articleId=104744&docType=1
         */
        public static final String ALIPAY_SERVICE_REFUND = "refund_fastpay_by_platform_pwd";
        /**
         * 提现（转账）： 批量付款到支付宝账户  https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.a5Nzss&treeId=64&articleId=103569&docType=103569
         */
        public static final String ALIPAY_SERVICE_BATCH_TRANS = "batch_trans_notify";
    }
}
