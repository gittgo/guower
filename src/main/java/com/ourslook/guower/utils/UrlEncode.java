package com.ourslook.guower.utils;

/**
 * Created by dazer on 2017/5/21.
 */
import com.ourslook.guower.utils.pay.unionpay.SDKUtil;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jiyuren
 *
 * @see ServletUtils
 */
public class UrlEncode {
    /**
     * 将 String 转为 map
     *
     * eg:sex=man&name=zhangsan&age=20
     * @param param
     *            aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>();
        if ("".equals(param) || null == param) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 使用java8处理
     * index.jsp?itemId=1&userId=1001&password=2388&token=14324324132432414324123&key=index
     * 参数转换成为Map
     */
    public static Map<String,String> getUrlParamsByJava8(String queryStr) {
        return Stream.of(queryStr.split("&")).map(str -> str.split("=")).collect(Collectors.toMap(arr -> arr[0], arr -> (arr.length>1 ? arr[1] : "")));
    }

    public static Map<String,String> getUrlParamsByJava8(HttpServletRequest request) {
        try {
            String params = IOUtils.toString(request.getInputStream(), "UTF-8");
            try {
                //url 解码
                params = URLDecoder.decode(params, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (XaUtils.isNotBlank(params)) {
                return getUrlParamsByJava8(params);
            } else {
                return getAllRequestParam(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }

    /**
     * 获取请求参数中所有的信息
     *
     * @see com.ourslook.guower.utils.UrlEncode
     *
     * @param request
     * @return
     */
    private static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }


    /**
     * 说明
     * @RequestParam 只能用-Content-Type: 为 application/x-www-form-urlencoded，数据只能request.getParameter()
     * @RequestBody  只能用-Content-Type: 为 application/json,数据只能request.getInputStream转成字符串，在转成对应的类型
     */
    public static Map<String, Object> getUrlParams(HttpServletRequest request) {
        try {
            String params = IOUtils.toString(request.getInputStream(), "UTF-8");
            try {
                //url 解码
                params = URLDecoder.decode(params, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return getUrlParams(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * 将map 转为 string
     * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
     *
     * @see SDKUtil#coverMap2String(Map)
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map,
                                           boolean isSort) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<>(map.keySet());
        if (isSort) {
            Collections.sort(keys);
        }
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = map.get(key);
            if (XaUtils.isArray(value)) {
                String[] arr = (String[]) value;
                sb.append(key + "=" +  (arr.length == 1 ? arr[0] :  (arr.length > 1 ? Arrays.toString(arr) : "")));
            } else {
                sb.append(key + "=" + XaUtils.getNutNullStr(value, ""));
            }
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.lastIndexOf("&"));
        }
        return s;
    }
}