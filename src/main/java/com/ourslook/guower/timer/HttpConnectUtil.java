package com.ourslook.guower.timer;

/**
 * Created by rock on 17/3/14.
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import com.ourslook.guower.utils.encryptdir.Md5Util;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import okhttp3.*;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpConnectUtil {

    private static String DUOSHUO_SHORTNAME = "yoodb";//多说短域名 ****.yoodb.****
    private static String DUOSHUO_SECRET = "xxxxxxxxxxxxxxxxx";//多说秘钥

    public static final MediaType XML
            = MediaType.parse("application/xml; charset=utf-8");

    /**
     * get方式
     * @param url
     * @author www.yoodb.com
     * @return
     */
    public static String getHttp(String url) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(getMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = getMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放连接
            getMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg;
    }

    /**
     * post方式
     * @author www.yoodb.com
     * @return
     */
    public static String postHttp() {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("UTF-8");
        PostMethod postMethod = new PostMethod("https://foreign.tuoniaox.com/newsflash/newsflash/list");
//        postMethod.addParameter(type, code);
        // MD5(MD5(app)1539242841)
        String timestemp = System.currentTimeMillis()+"";
        postMethod.addParameter("encryptionkey", Md5Util.getMd5(Md5Util.getMd5("guo")+timestemp));
        // 时间戳
        postMethod.addParameter("timestamp", timestemp);
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }


    public static String doPost() {
        // 获取输入流
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建post请求方法实例对象
        PostMethod postMethod = new PostMethod("https://foreign.tuoniaox.com/newsflash/newsflash/list");
        // 设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);

        NameValuePair[] nvp = null;
        Map<String, Object> paramMap = new HashMap<>();
        String timestemp = System.currentTimeMillis()+"";
//        postMethod.addParameter("encryptionkey", Md5Util.getMd5(Md5Util.getMd5("guo")+timestemp));
        // 时间戳
//        postMethod.addParameter("timestamp", timestemp);
        paramMap.put("encryptionkey", Md5Util.getMd5(Md5Util.getMd5("guo")+timestemp));
        paramMap.put("timestamp", timestemp);
        // 判断参数map集合paramMap是否为空
        if (null != paramMap && paramMap.size() > 0) {// 不为空
            // 创建键值参数对象数组，大小为参数的个数
            nvp = new NameValuePair[paramMap.size()];
            // 循环遍历参数集合map
            Set<Entry<String, Object>> entrySet = paramMap.entrySet();
            // 获取迭代器
            Iterator<Entry<String, Object>> iterator = entrySet.iterator();

            int index = 0;
            while (iterator.hasNext()) {
                Entry<String, Object> mapEntry = iterator.next();
                // 从mapEntry中获取key和value创建键值对象存放到数组中
                try {
                    nvp[index] = new NameValuePair(mapEntry.getKey(),
                            new String(mapEntry.getValue().toString().getBytes("UTF-8"), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                index++;
            }
        }
        // 判断nvp数组是否为空
        if (null != nvp && nvp.length > 0) {
            // 将参数存放到requestBody对象中
            postMethod.setRequestBody(nvp);
        }
        // 执行POST方法
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            // 判断是否成功
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method faild: " + postMethod.getStatusLine());
            }
            // 获取远程返回的数据
            is = postMethod.getResponseBodyAsStream();
            // 封装输入流
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuffer sbf = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp).append("\r\n");
            }

            result = sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 释放连接
            postMethod.releaseConnection();
        }
        return result;
    }
}

//    public static String post(String url, String requestBody) throws IOException{
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(XML, requestBody);
//        Request request = new Request.Builder()
//                .url(Global.getConfig("erpurl"))
//                .post(body)
//                .build();
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }


