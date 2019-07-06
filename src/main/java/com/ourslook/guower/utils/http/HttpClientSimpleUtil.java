package com.ourslook.guower.utils.http;


import com.alibaba.druid.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.XaUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


/**
 * @author dazer
 * @see org.apache.http.HttpStatus
 * @see org.apache.commons.httpclient.HttpStatus
 * @see org.springframework.http.HttpStatus
 * @see RestTemplate
 * @see com.ourslook.guower.api.ApiRestController
 * @see HttpClient
 * @see HttpClientUtils
 * @see SpringRestTemplateUtils
 * <p>
 * 最简单的请求方式，请求[{"key":"Content-Type","value":"application/x-www-form-urlencoded","description":""}]
 * 并且是没有任何认证：Authorization是none
 */
@SuppressWarnings("unchecked")
public class HttpClientSimpleUtil {
    private static Logger logger = Logger.getLogger(HttpClientSimpleUtil.class);
    private static HttpClient httpClient = null;

    // 构造单例
    private HttpClientSimpleUtil() {

        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        // 默认连接超时时间
        params.setConnectionTimeout(1000 * 60 * 3);
        // 默认读取超时时间
        params.setSoTimeout(1000 * 60);
        // 默认单个host最大连接数
        // very important!!
        params.setDefaultMaxConnectionsPerHost(100);
        // 最大总连接数
        // very important!!
        params.setMaxTotalConnections(256);
        httpConnectionManager.setParams(params);

        httpClient = new HttpClient(httpConnectionManager);

        httpClient.getParams().setConnectionManagerTimeout(3000);
        // httpClient.getParams().setIntParameter("http.socket.timeout", 10000);
        // httpClient.getParams().setIntParameter("http.connection.timeout", 5000);
    }

    private static class ClientUtilInstance {
        private static final HttpClientSimpleUtil ClientUtil = new HttpClientSimpleUtil();
    }

    public static HttpClientSimpleUtil getInstance() {
        return ClientUtilInstance.ClientUtil;
    }

    /**
     * 发送http GET请求，并返回http响应字符串
     *
     * @param urlstr 完整的请求url字符串
     * @return
     */


    public String doGetRequest(String urlstr) {
        String response = "";

        HttpMethod httpmethod = new GetMethod(urlstr);
        try {
            int statusCode = httpClient.executeMethod(httpmethod);
            InputStream _InputStream = null;
            if (statusCode == HttpStatus.SC_OK) {
                _InputStream = httpmethod.getResponseBodyAsStream();
            }
            if (_InputStream != null) {
                response = GetResponseString(_InputStream, "UTF-8");
            }
        } catch (HttpException e) {
            throw new RRException("请求失败：" + e.getMessage());
        } catch (IOException e) {
            throw new RRException("请求失败2：" + e.getMessage());
        } finally {
            httpmethod.releaseConnection();

        }
        return response;
    }

    public String doGetRequest(String urlstr, Map<String, String> paramsMap) {
        String response = "";

        HttpMethod httpmethod = new GetMethod(urlstr);
        //建立HttpPost对象
        List<NameValuePair> params = new ArrayList<>();
        //建立一个NameValuePair数组，用于存储欲传送的参数
        if (XaUtils.isNotEmpty(paramsMap)) {
            int i = 0;
            for (Map.Entry entry : paramsMap.entrySet()) {
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                NameValuePair nameValuePair = new NameValuePair(key, val);
                params.add(nameValuePair);
                i++;
            }
        }
        NameValuePair[] arr = new NameValuePair[params.size()];
        params.toArray(arr);
        httpmethod.setQueryString(arr);

        try {
            int statusCode = httpClient.executeMethod(httpmethod);
            InputStream inputstream = null;
            if (statusCode == HttpStatus.SC_OK) {
                inputstream = httpmethod.getResponseBodyAsStream();
            }
            if (inputstream != null) {
                response = GetResponseString(inputstream, "UTF-8");
            }
        } catch (HttpException e) {
            throw new RuntimeException("请求失败：" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("请求失败2：" + e.getMessage());
        } finally {
            httpmethod.releaseConnection();

        }
        return response;
    }

    /**
     * 发送http Post请求，并返回http响应字符串
     *
     * @param urlstr 完整的请求url字符串
     * @return
     */
    public String doPostRequest(String urlstr, Map<String, String> paramsMap) {

        //POST的URL
        HttpPost httppost = new HttpPost(urlstr);
        //建立HttpPost对象
        List<org.apache.http.NameValuePair> params = new ArrayList<>();
        //建立一个NameValuePair数组，用于存储欲传送的参数
        if (paramsMap != null) {
            for (Map.Entry entry : paramsMap.entrySet()) {
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                params.add(new BasicNameValuePair(key, val));
            }
        }
        String result = "";
        try {
            //添加参数
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            //设置编码
            HttpResponse response = HttpClientBuilder.create().build().execute(httppost);
            //发送Post,并返回一个HttpResponse对象
            //如果状态码为200,就是正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream _InputStream = response.getEntity().getContent();
                if (_InputStream != null) {
                    result = GetResponseString(_InputStream, "UTF-8");
                }
            }
        } catch (Exception e) {
            logger.error("获取响应错误，原因：" + e.getMessage());
        }

        return result;
    }


    /**
     * 如果请求区分数据类型格式，请自行拼接json
     */
    public String doPostRequestByJson(String urlstr, Map<String, String> paramsMap) {
        return this.doPostRequestByJson(urlstr, paramsMap == null ? null : JSON.toJSONString(paramsMap));
    }

    /**
     * 如果请求区分不区分格式，可以使用json
     */
    public String doPostRequestByJson(String urlstr, String jsonStr) {
        //POST的URL
        HttpPost httppost = new HttpPost(urlstr);
        //建立HttpPost对象
        String result = "";
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httppost.addHeader("Content-type", "application/json; charset=utf-8");
            httppost.setHeader("Accept", "application/json");
            httppost.addHeader(getBasicAuthHeaders());
            //添加参数
            if (jsonStr != null) {
                httppost.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
            }
            //设置编码
            HttpResponse response = HttpClientBuilder.create().build().execute(httppost);

            //发送Post,并返回一个HttpResponse对象
            //如果状态码为200,就是正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream _InputStream = response.getEntity().getContent();
                if (_InputStream != null) {
                    result = GetResponseString(_InputStream, "UTF-8");
                }
            } else if (response.getStatusLine().getStatusCode() == 422 || response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 400) {
                InputStream _InputStream = response.getEntity().getContent();
                if (_InputStream != null) {
                    result = GetResponseString(_InputStream, "UTF-8");
                }
            }
            logger.info("statusLine:" + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            logger.error("获取响应错误，原因：" + e.getMessage());
        }

        return result;
    }


    /**
     * @param inputstream
     * @param charset
     * @return
     */

    private String GetResponseString(InputStream inputstream, String charset) {
        String response = "";
        try {
            if (inputstream != null) {
                StringBuffer buffer = new StringBuffer();
                InputStreamReader isr = new InputStreamReader(inputstream, charset);
                Reader in = new BufferedReader(isr);
                int ch;
                while ((ch = in.read()) > -1) {
                    buffer.append((char) ch);
                }
                response = buffer.toString();
                buffer = null;
            }
        } catch (Exception e) {
            logger.error("获取响应错误，原因：" + e.getMessage());
            response = response + e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    public static String getHttpFullURL(String requestStr, String paramString) {
        if (requestStr != null) {
            // Only add the query string if it isn't empty and it
            // isn't equal to '?'.
            if (!"".equals(paramString) && !"?".equals(paramString)) {
                requestStr += requestStr.toString().contains("?") ? "&" : "?";
                requestStr += paramString;
            }
        }
        return requestStr;
    }


    public static String getHttpFullURL(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String paramString = request.getQueryString();
        if (url != null) {
            // Only add the query string if it isn't empty and it
            // isn't equal to '?'.
            if (!"".equals(paramString) && !"?".equals(paramString)) {
                url += url.toString().contains("?") ? "&" : "?";
                url += paramString;
            }
        }
        return url;
    }

    /**
     * base auth 示例
     * HTTP Basic Authentication认证的各种语言 后台用的 https://segmentfault.com/a/1190000004362731
     * https://www.cnblogs.com/alex-13/p/4845658.html
     * header:Authorization: "Basic 用户名和密码的base64加密字符串"
     * <p>
     * 一是在请求头中添加Authorization：
     * Authorization: "Basic 用户名和密码的base64加密字符串",注意这里的格式哦，为 "username:password"
     * <p>
     * 二是在url中添加用户名和密码：
     * http://userName:password@api.minicloud.com.cn/statuses/friends_timeline.xml
     */
    private static org.apache.http.Header getBasicAuthHeaders() {
        String plainCredentials = "6S40WSzHgzlgDZXv:ATbuvlxLmFRGxM7rOI9zjucLH0odvoIXbg9dCuJ6RzyTMFRz7wAuHHLWP8JknTpG";
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());

        org.apache.http.Header header = new BasicHeader("Authorization", "Basic " + base64Credentials);
        return header;
    }

//    public static void main(String[] args) {
//        Map map = new Hashtable();
//        System.out.println(HttpClientSimpleUtil.getInstance().doPostRequest("http://api.goseek.cn/Tools/holiday?date=20180129", null));
//
//        System.out.println();
//
//        //{"imei":"868575027786310","charge_control":1,"charge_time":60}
//        map.put("imei", "8685575027786310");
//        map.put("charge_control", 1);
//        map.put("charge_time", 60);
//
//        String json = "{\"imei\":\"868575027786310\",\"charge_control\":1,\"charge_time\":60}";
//
//        System.out.println("~~~~~~~~" + HttpClientSimpleUtil.getInstance().doPostRequestByJson("http://api.openluat.com//mafu/event/yiwangwuji/device/launch", json));
//        System.out.println();
//        System.out.println();
//        System.out.println("~~~~~~~~" + HttpClientSimpleUtil.getInstance().doPostRequestByJson("http://api.openluat.com//mafu/event/yiwangwuji/device/launch", map));
//    }
}
