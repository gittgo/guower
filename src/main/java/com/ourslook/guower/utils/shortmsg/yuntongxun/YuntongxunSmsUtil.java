package com.ourslook.guower.utils.shortmsg.yuntongxun;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

import java.util.HashMap;
import java.util.Set;

/**
 * @author duandazhi
 * @ClassName: 云通讯 短信发送平台
 *  http://www.yuntongxun.com/; ˙账号:portal@portal.com  ;密码:hy123456
 *  http://www.yuntongxun.com/; ˙账号:15805321233        ;密码:tianchuang
 *  http://doc.yuntongxun.com/p/5a533de33b8496dd00dce07c
 * @date 2016/10/28 上午10:07
 *
 *
 * 【取道华山】您的验证码为{1}，请于{2}分钟内正确输入，如非本人操作，请忽略此短信。
 */
public class YuntongxunSmsUtil {

    /**
     * accountSid   ACCOUNT SID http://www.yuntongxun.com/member/main      管理->开发者主账号->ACCOUNT SID
     * accountToken AUTH TOKEN  http://www.yuntongxun.com/member/main     管理->开发者主账号->AUTH TOKEN
     * appId        APP ID      http://www.yuntongxun.com/member/app/view  管理->应用管理->应用名称->App ID
     * 取道教育测试账号
     */
    private static final String ACCONT_SID   = "8a216da864a7c9e30164acb375910379";
    private static final String ACCONT_TOKEN = "f5c6b87940f64d2ebeed2c22dcfa4b88";
    private static final String APPID        = "8a216da864a7c9e30164acb375ef0380";

    /*public static void main(String[] args) {
        //测试
        YuntongxunSmsUtil.sendMessege("18049531390", Constant.YuntongxunSmsTemplate.COMMON.getTcode(), new String[]{"", "8883", "5"});
    }*/

    /**
     * 发送短信
     *
     * @param phoneNum 手机号
     * @param model    模板id
     * @param arr
     * @return
     */
    public static HashMap<String, Object> sendMessege(String phoneNum, String model, String[] arr) {

        HashMap<String, Object> result = null;

        //初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        //*沙盒环境（用于应用开发调试）：restAPI.init("https://sandboxapp.cloopen.com:8883", "8883");*
        /**
         * 初始化服务地址和端口  地址须为sandboxapp.cloopen.com 无需带协议头，目前默认是https协议
         * @param serverIP            必选参数		服务器地址
         * @param serverPort        必选参数		服务器端口
         */
        restAPI.init("app.cloopen.com", "8883");
        //黄金账户

        /**
         * 初始化主帐号信息
         * @param accountSid        必选参数		主帐号
         * @param accountToken        必选参数		主账号令牌
         */
        restAPI.setAccount(ACCONT_SID, ACCONT_TOKEN);
        /**
         * 初始化应用Id
         * @param appId            必选参数	 	应用Id
         */
        restAPI.setAppId(APPID);


        //******************************注释****************************************************************
        //*调用发送模板短信的接口发送短信                                                                  *
        //*参数顺序说明：                                                                                  *
        //*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
        //*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
        //*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
        //*第三个参数是要替换的内容数组。																														       *
        //**************************************************************************************************

        //**************************************举例说明***********************************************************************
        //*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
        //*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
        //*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
        //*********************************************************************************************************************
        /**
         * 发送短信模板请求
         * @param to                必选参数   		短信接收端手机号码集合，用英文逗号分开，每批发送的手机号数量不得超过100个
         * @param templateId            必选参数		模板Id
         * @param datas            可选参数		内容数据，用于替换模板中{序号}
         * @return
         */
        result = restAPI.sendTemplateSMS(phoneNum, model, arr);


        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            @SuppressWarnings("unchecked")
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                //System.out.println(object);
            }
            data.put("statusCode", result.get("statusCode"));
            data.put("statusMsg", result.get("statusMsg"));
            return data;
        } else {
            //异常返回输出错误码和错误信息
            //System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
            result.put("statusCode", result.get("statusCode"));
            result.put("statusMsg", result.get("statusMsg"));
            return result;
        }
    }
}
