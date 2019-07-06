package com.ourslook.guower.utils.pay.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ourslook.guower.utils.http.HttpClientSimpleUtil;
import com.ourslook.guower.utils.pay.weixin.config.WxpayMiniProgremConfig;
import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
/**
 * 微信小程序获取用户手机号详解 http://blog.csdn.net/u012369373/article/details/78696228
 * <p>
 * 微信小程序获取用户信息，解密encryptedData https://www.jianshu.com/p/856fe2195ffe
 * <p>
 * 注意是，这些所有的参数中，如果包含+-./&{}这些特殊符号，请务必先进行 encodeURIComponent() 否则获取到的字符串可能有问题
 *
 * @param accessToken   382624bb-593e-4f9c-a9a1-7dece04692ab
 * @param encryptedData yK6FnHdMFvXszdyAEovR41rlY0ie+lPgMhZyk8xvsNF4rHo3BeJtJW2dmAdoVW+f0OLSW1qqmB33PxsbZZfyWR4dS+PQO0zjRe/UOm1YiuaiCcTEgglTXQ+IMLuCsjbNCUvL7ZSz6NtjmikZgYr9cH8NM0uIMnP/3zqOkPzPX8XN2cJ42/mQ9Acp0jzKSMCsoWbpMZ0vE8WpSUDYoAsmylWTOUEYePz1JGKYJkvYQsjgc9IRGP6d2ZdnmVtPDe7ydxR3lsxxBN7rfSJw5b11f84hmPcCUGen0W33/ipGOH6aSi97tpN/BRGRfw7Rr7b+IwdYwjB6TxsExDTYwZosArZHsHecAMWpa3M94zj1lR9VVZ0nj9PM3O9hLkKskguvs0sh0ylH3zh/JEvE7zPpT0JZGUGshRPjDVegdi7GKSP/SKRVcFKX5Yl7gseqhRIUcpVIFLGBEU9QxZLKVIwrbU29P8uOy0m4xOOubqP2RJA=
 * @param iv            PzzHIj5e92bL5vVYQ3q36Q==
 * @param request
 * @param response
 * @return
 */

/**
 * 微信小程序信息获取
 *
 * @author Administrator
 * @Date 2017年2月16日 11:56:08
 * //
 * //
 * @ApiParam("唯一标示,字段名:accessToken") @RequestParam(value = "accessToken", required = false) String accessToken,
 * @ApiParam("小程序调用getUserInfo之后的加密字符串:encryptedData") @RequestParam(value = "encryptedData", required = false) String encryptedData,
 * @ApiParam("偏移量iv") @RequestParam(value = "iv", required = false) String iv,
 */
public class WeiXinMiniprogramInterface {

    private static final String GET_SESSIONKEY_OROPENID_BY_JSCODE_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 根据小程序的jscode 获取微信小程序 session_key 和 openid
     * 这里微信小程序 登录 一般使用该接口，获取用户信息； 但是 获取不到手机号的;
     * 注意，这个只能使用一次，7200就过期了
     *
     * @param jscode 微信端登录code值
     * @return
     */
    public static JSONObject getSessionKeyByopenid(String jscode) {
        Map<String, String> requestUrlParam = new HashMap<>(4);
        requestUrlParam.put("appid", WxpayMiniProgremConfig.APPID);
        requestUrlParam.put("secret", WxpayMiniProgremConfig.APP_SECRECT);
        requestUrlParam.put("js_code", jscode);
        requestUrlParam.put("grant_type", "authorization_code");
        String jsonObject = HttpClientSimpleUtil.getInstance().doGetRequest(WeiXinMiniprogramInterface.GET_SESSIONKEY_OROPENID_BY_JSCODE_URL, requestUrlParam);
        return JSON.parseObject(jsonObject);
    }

    /**
     * 解密用户敏感数据获取用户信息;
     * 这些参数都是前端给的，注意要进行编码;
     *
     * {"nickName":"杨海林","gender":1,"language":"zh_CN","city":"Xi'an","province":"Shaanxi","country":"China","avatarUrl":"https://wx.qlogo.cn/mmopen/vi_32/RXCLBFsuAgmicqTOsMOuZUfVLwcTb2lFIuyEKkYXyiaYGBgtRC5Nfg4MTr37rlSwdRw5gp5VFa3bmDEuy6LrOFeg/132"}
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    public static WeixinAppletUserInfo getUserInfo(String encryptedData, String sessionKey, String iv) throws Exception {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        String jsonStr = decryptUserInfo(keyByte, ivByte, dataByte);
        if (StringUtils.isBlank(jsonStr)) {
            throw new Exception("获取微信小程序信息失败！");
        }
        return JSON.parseObject(jsonStr, WeixinAppletUserInfo.class);
    }

    private static String decryptUserInfo(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData), "UTF-8");
    }

    /**
     * 微信小程序登录成功之后的用户信息
     */
    public static class WeixinAppletUserInfo {
        /**
         * 国家-China
         */
        private String country;
        /**
         * 省份
         */
        private String province;
        /**
         * 城市：Xi'an
         */
        private String city;
        private String area;
        private String address;
        /**
         * 图像
         */
        private String avatarUrl;
        /**
         * 用户昵称，可能包含特殊符号和表情
         */
        private String nickName;
        /**
         * openId
         */
        private String openId;
        /**
         * 性别；1
         */
        private String gender;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }


   /* public static void main(String[] args) throws Exception {
        byte[] encrypData = Base64.decode("mgxuts7uEbmLQKcW329VaQTua4MIdRXKTm+YnFSaOGYK1zd+uKEhhGj5cfU8g5oP1RSk4PXbDrHuQY8MI0zwtfQL2+Zfq0NDm11w8efNCclVSxEbHdUYeJUkhhTsNkZgt3SzYv5ptqTNyJ7TOTaX78wkN1uWTEV2sa/imvdnBclp0Qim0TS3MGoaSyE09MqTvbeG9Z1PjxsDwjVH0FxG1Q==");
        byte[] ivData = Base64.decode("SG386etdytWA3sOXHxqfnw==");
        byte[] sessionKey = Base64.decode("Fn6r4gAIOiZI0JBXn4hQ0w==");
        System.out.println( decryptUserInfo3(sessionKey, ivData, encrypData));
    }*/
}
