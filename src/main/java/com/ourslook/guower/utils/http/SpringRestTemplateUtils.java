package com.ourslook.guower.utils.http;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.config.RestTemplateConfig;
import com.ourslook.guower.utils.UrlEncode;
import com.ourslook.guower.utils.spring.SpringContextHelper;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

/**
 * 使用spring自带的rest工具类
 *
 * @author dazer
 * @see com.ourslook.guower.api.ApiRestController
 * @see SpringContextHelper
 * @see RestTemplateConfig
 * @see HttpClientSimpleUtil
 * @see UrlEncode
 */
public class SpringRestTemplateUtils {
    private Logger logger = Logger.getLogger(SpringRestTemplateUtils.class);
    private RestTemplate restTemplate;
    private static SpringRestTemplateUtils instant = null;

    public synchronized static SpringRestTemplateUtils getInstance() {
        if (instant == null) {
            instant = new SpringRestTemplateUtils();
        }
        return instant.init();
    }

    private SpringRestTemplateUtils() {
    }

    private SpringRestTemplateUtils init() {
        if (restTemplate == null) {
            //restTemplate = SpringContextHelper.getBean(RestTemplate.class);
            restTemplate = new RestTemplate();
        }
        return this;
    }

    /**
     * http post form形式,没有安全认证
     */
    public String sendPost(String url, Map<String, Object> map) {
        return this.sendPost(url, map, true, false);
    }

    /**
     * http post body-application/json 形式,没有安全认证
     */
    public String sendPostWithBodyRawJson(String url, Map<String, Object> map) {
        return this.sendPost(url, map, true, false);
    }

    public String sendPost(String url, Map<String, Object> map, boolean mediaTypeIsForm, boolean httpAuthIsBasicAtuh) {
        //测试post
        /**
         * contentType : 表示请求体发送给服务器的编码格式，如表单、json等 (application/x-www-form-urlencoded\application/json;charset=UTF-8)
         * accept: 表示客户端可以接受的格式
         */
        //最近使用RestTemplate发送post请求，遇到了很多问题，如转换httpMessage失败、中文乱码等，调了好久才找到下面较为简便的方法：
        // ContentType 设置请求方式
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        if (mediaTypeIsForm) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        } else {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        if (httpAuthIsBasicAtuh) {
            headers.putAll(getHeaders());
        }

        HttpEntity<String> formEntity = null;
        if (mediaTypeIsForm) {
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            //  也支持中文
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = String.valueOf(entry.getValue());
                    params.add(key, value);
                }
            }
            String jsonObj = JSON.toJSONString(params);
            formEntity = new HttpEntity<>(jsonObj, headers);
        } else {
            String jsonObj = JSON.toJSONString(map == null ? new Hashtable<>() : map);
            formEntity = new HttpEntity<>(jsonObj, headers);
        }

        String json = restTemplate.postForEntity(url, formEntity, String.class).getBody();
        logger.info(json);
        return json;
    }

    /**
     * http get form形式,没有安全认证
     */
    public String sendGet(String url, Map<String, Object> map) {
        return this.sendGet(url, map,false);
    }

    public String sendGet(String url, Map<String, Object> map, boolean httpAuthIsBasicAtuh) {
        //Get
        /**
         * contentType : 表示请求体发送给服务器的编码格式，如表单、json等 (application/x-www-form-urlencoded\application/json;charset=UTF-8)
         * accept: 表示客户端可以接受的格式
         */
        //最近使用RestTemplate发送post请求，遇到了很多问题，如转换httpMessage失败、中文乱码等，调了好久才找到下面较为简便的方法：
        // ContentType 设置请求方式
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if (httpAuthIsBasicAtuh) {
            headers.putAll(getHeaders());
        }

        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        //  也支持中文
        if (map != null) {
            String paramUrl = UrlEncode.getUrlParamsByMap(map, true);
            if (!url.contains("?")) {
                url = url + "?" + paramUrl;
            } else {
                url = url + (url.endsWith("&") ? "" : "&" + paramUrl);
            }
        }
        String json = restTemplate.getForEntity(url, String.class).getBody();
        logger.info(json);
        return json;
    }


    /**
     *
     * 目前Spring Rest 验证还有问题，不能使用
     *
     * base auth 示例
     * HTTP Basic Authentication认证的各种语言 后台用的 https://segmentfault.com/a/1190000004362731
     * https://www.cnblogs.com/alex-13/p/4845658.html
     * header:Authorization: "Basic 用户名和密码的base64加密字符串"
     *
     * 一是在请求头中添加Authorization：
     * Authorization: "Basic 用户名和密码的base64加密字符串",注意这里的格式哦，为 "username:password"
     *
     * 二是在url中添加用户名和密码：
     * http://userName:password@api.minicloud.com.cn/statuses/friends_timeline.xml
     */
    private static HttpHeaders getHeaders(){
        String plainCredentials="6S40WSzHgzlgDZXv:ATbuvlxLmFRGxM7rOI9zjucLH0odvoIXbg9dCuJ6RzyTMFRz7wAuHHLWP8JknTpG";
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        return headers;
    }

//    public static void main(String[] args) {
//        System.out.println(SpringRestTemplateUtils.getInstance().sendPost("http://api.goseek.cn/Tools/holiday?date=20180129", null));
//        //String url = "https://gupiao.baidu.com/api/concept/getthemelist?from=pc&os_ver=1&cuid=xxx&vv=3.3.0&format=json&timestamp="+System.currentTimeMillis()/1000;
//        System.out.println(SpringRestTemplateUtils.getInstance().sendGet("https://gupiao.baidu.com/api/concept/getthemelist?from=pc&os_ver=1&cuid=xxx&vv=3.3.0&format=json&timestamp=" + System.currentTimeMillis() / 1000, null));
//
//        String url = "http://esms.etonenet.com/sms/mt?spid=3060&sppassword=hbkj3060&das=8618611178949&command=MULTI_MT_REQUEST&sm=a1beccd4b1a6a1bf20cda8b5c0bdd3c8ebcdeab3c9a3a1&dc=15";
//        Map<String, Object> map = new Hashtable<>();
//        map.put("aaa", "bbb");
//        System.out.println(SpringRestTemplateUtils.getInstance().sendGet(url, map));
//    }
}
