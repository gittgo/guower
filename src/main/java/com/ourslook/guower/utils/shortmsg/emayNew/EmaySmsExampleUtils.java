package com.ourslook.guower.utils.shortmsg.emayNew;


import cn.emay.eucp.inter.http.v1.dto.request.BalanceRequest;
import cn.emay.eucp.inter.http.v1.dto.request.SmsSingleRequest;
import cn.emay.eucp.inter.http.v1.dto.response.BalanceResponse;
import cn.emay.eucp.inter.http.v1.dto.response.SmsResponse;
import cn.emay.util.AES;
import cn.emay.util.GZIPUtils;
import cn.emay.util.JsonHelper;
import cn.emay.util.http.*;
import com.ourslook.guower.utils.Identities;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 伊美文档中心
 * <p>
 * 注意要导入：emay-sms-interface.jar
 * <p>
 * http://bjmtn.b2m.cn:8080/static/doc/doc.html
 * 序列号：EUCP-EMY-SMS1-6BNBX   	密码：EEEA7F26B5256304
 * 下载接口文档地址： http://bjmtn.b2m.cn:8080/static/doc/doc.html
 * 发送失败可以查看 接口状态码表 在 http://bjmtn.b2m.cn:8080/static/doc/%E4%BA%BF%E7%BE%8E%E7%9F%AD%E4%BF%A1%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E.pdf
 */

/**
 *
 *
 * Hi mike，
 短信平台开户已成功，充值后即可使用：
 序列号：EUCP-EMY-SMS1-6BNBX   	密码：EEEA7F26B5256304
 下载接口文档地址： http://bjmtn.b2m.cn:8080/static/doc/doc.html
 */
public class EmaySmsExampleUtils {

    private static Logger logger = LoggerFactory.getLogger(EmaySmsExampleUtils.class);

    /**
     * appId
     */
    private static final String appId = "EUCP-EMY-SMS1-6BNBX";
    /**
     * 密钥
     */
    private static final String secretKey = "EEEA7F26B5256304";
    /**
     * 接口地址
     */
    private static final String host = "bjmtn.b2m.cn";
    /**
     * 加密算法
     */
    private static final String algorithm = "AES/ECB/PKCS5Padding";
    /**
     * encode
     */
    private static final String encode = "UTF-8";
    /**
     * 是否压缩
     */
    private static final boolean isGizp = false;


//    public static void main(String[] args) {
//        StartMenu("18049531390", 100222l);
//        // 获取余额
//        getBalance(appId, secretKey, host, algorithm, isGizp, encode);
//        // 获取状态报告
//        getReport(appId, secretKey, host, algorithm, isGizp, encode);
//        // 获取上行
//        getMo(appId, secretKey, host, algorithm, isGizp, encode);
//        //发送单条短信 【签名】+内容
//        setSingleSms(appId, secretKey, host, algorithm, "【浙江省储粮管理有限公司】你好今天天气不错，挺风和日丽的", null, null, "18049531390", isGizp, encode);
//        // 发送批次短信[有自定义SMSID]
//        setBatchSms(appId, secretKey, host, algorithm, "你好今天天气不错，挺风和日丽的", null, new CustomSmsIdAndMobile[]{new CustomSmsIdAndMobile("1", "18001000000"), new CustomSmsIdAndMobile("2", "18001000001")}, isGizp, encode);
//        // 发送批次短信[无自定义SMSID]
//        setBatchOnlySms(appId, secretKey, host, algorithm, "你好今天天气不错，挺风和日丽的", null, new String[]{"18001000000", "18001000001"}, isGizp, encode);
//        // 发送个性短信
//        setPersonalitySms(appId, secretKey, host, algorithm, null, new CustomSmsIdAndMobileAndContent[]{new CustomSmsIdAndMobileAndContent("1", "18001000000", "你好今天天气不错，挺风和日丽的"), new CustomSmsIdAndMobileAndContent("2", "18001000001", "你好今天天气不错，挺风和日丽的啊")}, isGizp, encode);
//        // 发送全个性短信
//        setPersonalityAllSms(appId, secretKey, host, algorithm, new PersonalityParams[]{new PersonalityParams("101", "18001000000", "天气不错", "01", null), new PersonalityParams("102", "18001000001", "天气不错1", "02", "2017-12-01 11:00:00")}, isGizp, encode);
//    }

    public EmaySmsExampleUtils(){
    }

    @Test
    public void testSms(){
        String content = "【浙江省储粮管理有限公司】你的验证码是:" + Identities.getRandomNumberNo0Stat(6);
        sendSMS(appId, secretKey, host, content, "17691137282");
    }

    @Test
    public void testBalance(){
        getBalance(appId,secretKey,host,algorithm,true, encode);
    }

    /**
     *
     * @param mobile 手机号码
     * @return
     *  返回结果为空字符串：表示短信发送成功；否则短信发送失败，返回失败原因
     */
    public static String sendSMS(String appId, String secretKey, String host, String content, String mobile) {
        return setSingleSms(appId, secretKey, host, algorithm, content, null, null, mobile, isGizp, encode);
    }

    /**
     * 获取短信余额
     * @param appId
     * @param secretKey
     * @param host
     * @return
     */
    public static Long getBalance(String appId, String secretKey, String host){
        return getBalance(appId, secretKey,host, algorithm, true, encode);
    }

//    public static String sendContent(String mobile, String content) {
//        String param = "";
//        String url = baseUrl + "regist.action";
//        param = "cdkey=" + sn + "&password=" + password;
//        String ret = SDKHttpClient.registAndLogout(url, param);
//        try {
//            content = URLEncoder.encode(content, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        param = "cdkey=" + sn + "&password=" + key + "&phone=" + mobile + "&message=" + content + "&addserial=&seqid=666888";
//        url = baseUrl + "sendsms.action";
//        ret = SDKHttpClient.sendSMS(url, param);
//        return ret;
//    }
//
//
    /**
     * 获取余额
     * @param isGzip 是否压缩
     */
    public static Long getBalance(String appId, String secretKey, String host, String algorithm, boolean isGzip, String encode) {
        logger.info("=============begin getBalance==================");
        BalanceRequest pamars = new BalanceRequest();
        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/getBalance", isGzip, encode);
        logger.info("result code :" + result.getCode());
        if ("SUCCESS".equals(result.getCode())) {
            BalanceResponse response = JsonHelper.fromJson(BalanceResponse.class, result.getResult());
            if (response != null) {
                logger.info("result data : " + response.getBalance());
                return response.getBalance();
            }
        }
        logger.info("=============end getBalance==================");
        return null;
    }
//
//    /**
//     * 获取状态报告
//     * @param isGzip 是否压缩
//     */
//    private static void getReport(String appId, String secretKey, String host, String algorithm, boolean isGzip, String encode) {
//        System.out.println("=============begin getReport==================");
//        ReportRequest pamars = new ReportRequest();
//        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/getReport", isGzip, encode);
//        System.out.println("result code :" + result.getCode());
//        if ("SUCCESS".equals(result.getCode())) {
//            ReportResponse[] response = JsonHelper.fromJson(ReportResponse[].class, result.getResult());
//            if (response != null) {
//                for (ReportResponse d : response) {
//                    System.out.println("result data : " + d.getExtendedCode() + "," + d.getMobile() + "," + d.getCustomSmsId() + "," + d.getSmsId() + "," + d.getState() + "," + d.getDesc()
//                            + "," + d.getSubmitTime() + "," + d.getReceiveTime());
//                }
//            }
//        }
//        System.out.println("=============end getReport==================");
//    }
//
//    /**
//     * 获取上行
//     * @param isGzip 是否压缩
//     */
//    private static void getMo(String appId, String secretKey, String host, String algorithm, boolean isGzip, String encode) {
//        System.out.println("=============begin getMo==================");
//        MoRequest pamars = new MoRequest();
//        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/getMo", isGzip, encode);
//        System.out.println("result code :" + result.getCode());
//        if ("SUCCESS".equals(result.getCode())) {
//            MoResponse[] response = JsonHelper.fromJson(MoResponse[].class, result.getResult());
//            if (response != null) {
//                for (MoResponse d : response) {
//                    System.out.println("result data:" + d.getContent() + "," + d.getExtendedCode() + "," + d.getMobile() + "," + d.getMoTime());
//                }
//            }
//        }
//        System.out.println("=============end getMo==================");
//    }
//
    /**
     * 发送单条短信
     * @param isGzip 是否压缩
     */
    public static String setSingleSms(String appId, String secretKey, String host, String algorithm, String content, String customSmsId, String extendCode, String mobile, boolean isGzip, String encode) {
        logger.info("=============begin setSingleSms==================");
        SmsSingleRequest pamars = new SmsSingleRequest();
        pamars.setContent(content);
        pamars.setCustomSmsId(customSmsId);
        pamars.setExtendedCode(extendCode);
        pamars.setMobile(mobile);
        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/sendSingleSMS", isGzip, encode);
        logger.info("result code :" + result.getCode());
        if ("SUCCESS".equals(result.getCode())) {
            SmsResponse response = JsonHelper.fromJson(SmsResponse.class, result.getResult());
            if (response != null) {
                logger.info("data : " + response.getMobile() + "," + response.getSmsId() + "," + response.getCustomSmsId());
                logger.info("important success 还是没有收到短信，说明是短信内容没有加签名，请加上签名");
            }
            return "0";
        } else if ("ERROR_BALANCE".equalsIgnoreCase(result.getCode())) {
            logger.info("余额不足请充值！");
            return "余额不足请充值";
        } else {
            logger.info("其他错误！！！，短信发送失败！！");
        }
        logger.info("=============end setSingleSms==================");
        return "其他错误！！！，短信发送失败！！code:" + result.getCode();
    }
//
//    /**
//     * 发送批次短信
//     * @param isGzip 是否压缩
//     */
//    private static void setBatchOnlySms(String appId, String secretKey, String host, String algorithm, String content, String extendCode, String[] mobiles, boolean isGzip, String encode) {
//        System.out.println("=============begin setBatchOnlySms==================");
//        SmsBatchOnlyRequest pamars = new SmsBatchOnlyRequest();
//        pamars.setMobiles(mobiles);
//        pamars.setExtendedCode(extendCode);
//        pamars.setContent(content);
//        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/sendBatchOnlySMS", isGzip, encode);
//        System.out.println("result code :" + result.getCode());
//        if ("SUCCESS".equals(result.getCode())) {
//            SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
//            if (response != null) {
//                for (SmsResponse d : response) {
//                    System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
//                }
//            }
//        }
//        System.out.println("=============end setBatchOnlySms==================");
//    }
//
//    /**
//     * 发送批次短信
//     * @param isGzip 是否压缩
//     */
//    private static void setBatchSms(String appId, String secretKey, String host, String algorithm, String content, String extendCode, CustomSmsIdAndMobile[] customSmsIdAndMobiles, boolean isGzip, String encode) {
//        System.out.println("=============begin setBatchSms==================");
//        SmsBatchRequest pamars = new SmsBatchRequest();
//        pamars.setSmses(customSmsIdAndMobiles);
//        pamars.setExtendedCode(extendCode);
//        pamars.setContent(content);
//        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/sendBatchSMS", isGzip, encode);
//        System.out.println("result code :" + result.getCode());
//        if ("SUCCESS".equals(result.getCode())) {
//            SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
//            if (response != null) {
//                for (SmsResponse d : response) {
//                    System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
//                }
//            }
//        }
//        System.out.println("=============end setBatchSms==================");
//    }
//
//    /**
//     * 发送个性短信
//     * @param isGzip 是否压缩
//     */
//    private static void setPersonalitySms(String appId, String secretKey, String host, String algorithm, String extendCode, CustomSmsIdAndMobileAndContent[] customSmsIdAndMobileAndContents, boolean isGzip, String encode) {
//        System.out.println("=============begin setPersonalitySms==================");
//        SmsPersonalityRequest pamars = new SmsPersonalityRequest();
//        pamars.setSmses(customSmsIdAndMobileAndContents);
//        pamars.setExtendedCode(extendCode);
//        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/sendPersonalitySMS", isGzip, encode);
//        System.out.println("result code :" + result.getCode());
//        if ("SUCCESS".equals(result.getCode())) {
//            SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
//            if (response != null) {
//                for (SmsResponse d : response) {
//                    System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
//                }
//            }
//        }
//        System.out.println("=============end setPersonalitySms==================");
//    }
//
//    /**
//     * 发送个性短信
//     * @param isGzip 是否压缩
//     */
//    private static void setPersonalityAllSms(String appId, String secretKey, String host, String algorithm, PersonalityParams[] customSmsIdAndMobileAndContents, boolean isGzip, String encode) {
//        System.out.println("=============begin setPersonalityAllSms==================");
//        SmsPersonalityAllRequest pamars = new SmsPersonalityAllRequest();
//        pamars.setSmses(customSmsIdAndMobileAndContents);
//        ResultModel result = request(appId, secretKey, algorithm, pamars, "http://" + host + "/inter/sendPersonalityAllSMS", isGzip, encode);
//        System.out.println("result code :" + result.getCode());
//        if ("SUCCESS".equals(result.getCode())) {
//            SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
//            if (response != null) {
//                for (SmsResponse d : response) {
//                    System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
//                }
//            }
//        }
//        System.out.println("=============end setPersonalityAllSms==================");
//    }

    /**
     * 公共请求方法
     */
    private static ResultModel request(String appId, String secretKey, String algorithm, Object content, String url, final boolean isGzip, String encode) {
        Map<String, String> headers = new HashMap<String, String>();
        EmayHttpRequestBytes request = null;
        try {
            headers.put("appId", appId);
            headers.put("encode", encode);
            String requestJson = JsonHelper.toJsonString(content);
            logger.info("result json: " + requestJson);
            byte[] bytes = requestJson.getBytes(encode);
            logger.info("request data size : " + bytes.length);
            if (isGzip) {
                headers.put("gzip", "on");
                bytes = GZIPUtils.compress(bytes);
                logger.info("request data size [com]: " + bytes.length);
            }
            byte[] parambytes = AES.encrypt(bytes, secretKey.getBytes(), algorithm);
            logger.info("request data size [en] : " + parambytes.length);
            request = new EmayHttpRequestBytes(url, encode, "POST", headers, null, parambytes);
        } catch (Exception e) {
            logger.info("加密异常");
            e.printStackTrace();
        }
        EmayHttpClient client = new EmayHttpClient();
        String code = null;
        String result = null;
        try {
            EmayHttpResponseBytes res = client.service(request, new EmayHttpResponseBytesPraser());
            if (res == null) {
                logger.info("请求接口异常");
                return new ResultModel(code, result);
            }
            if (res.getResultCode().equals(EmayHttpResultCode.SUCCESS)) {
                if (res.getHttpCode() == 200) {
                    code = res.getHeaders().get("result");
                    if ("SUCCESS".equals(code)) {
                        byte[] data = res.getResultBytes();
                        logger.info("response data size [en and com] : " + data.length);
                        data = AES.decrypt(data, secretKey.getBytes(), algorithm);
                        if (isGzip) {
                            data = GZIPUtils.decompress(data);
                        }
                        logger.info("response data size : " + data.length);
                        result = new String(data, encode);
                        logger.info("response json: " + result);
                    }
                } else {
                    logger.info("请求接口异常,请求码:" + res.getHttpCode());
                }
            } else {
                logger.info("请求接口网络异常:" + res.getResultCode().getCode());
            }
        } catch (Exception e) {
            logger.info("解析失败");
            e.printStackTrace();
        }
        ResultModel re = new ResultModel(code, result);
        return re;
    }

    private static class ResultModel {

        private String code;
        private String result;

        public ResultModel(String code, String result) {
            this.code = code;
            this.result = result;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

    }
}

