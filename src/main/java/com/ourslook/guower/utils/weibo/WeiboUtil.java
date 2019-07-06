package com.ourslook.guower.utils.weibo;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.http.HttpClientSimpleUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.time.LocalDateTime;
import java.util.*;

public class WeiboUtil {

    /**
     * 访问数据接口（直接访问,无需授权）
     */
    private static final String DATA_URL = "https://m.weibo.cn/api/container/getIndex";

    /**
     * OAuth2的authorize接口地址
     */
    private static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";

    /**
     * OAuth2的access_token接口地址
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

    /**
     * 根据用户ID获取用户信息接口地址
     */
    private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";

    /**
     * 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限
     */
    private static final String ACCESS_TOKEN_STATE_URL = "https://api.weibo.com/oauth2/get_token_info";

    /**
     *获取当前登录用户及其所关注（授权）用户的最新微博接口地址
     */
    private static final String HOME_TIMELINE_URL = "https://api.weibo.com/2/statuses/home_timeline.json";

    /**
     * access_token
     */
    private static String ACCESS_TOKEN = "2.00tTGMVG9ZaiBEe3b0476be0Iv_p1E";

    /**
     * access_token到期时间
     */
    private static LocalDateTime ACCESS_TOKEN_EXPIRY_TIME = LocalDateTime.now();


    /**
     * 请求用户授权Token
     */
    public static String authorize(){
        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
        String result = httpClientSimpleUtil.doGetRequest(AUTHORIZE_URL+"?client_id="+WeiboConfig.getAppKey()+"&redirect_uri="+WeiboConfig.getRedirectUri());
        return result;
    }

    /**
     * 刷新token
     */
    public static boolean refreshToken(String code) {
        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
        Map paramMap = new HashMap();
        paramMap.put("client_id",WeiboConfig.getAppKey());
        paramMap.put("client_secret",WeiboConfig.getAppSecret());
        paramMap.put("redirect_uri",WeiboConfig.getRedirectUri());
        paramMap.put("grant_type","authorization_code");
        paramMap.put("code",code);
        String result = httpClientSimpleUtil.doPostRequest(ACCESS_TOKEN_URL,paramMap);
        Map resultMap = "".equals(result)?new HashMap():JSON.parseObject(result,Map.class);
        if(resultMap.get("access_token")==null || resultMap.get("access_token").toString().trim().equals("")){
            return false;
        }
        //当前时间加上有效时间减5分钟
        ACCESS_TOKEN_EXPIRY_TIME = LocalDateTime.now().plusMinutes(((new Long(resultMap.get("expires_in").toString()))/60)-5);
        ACCESS_TOKEN = resultMap.get("access_token").toString();
        WeiboConfig.setUid(resultMap.get("uid").toString());
        return true;
    }

    /**
     * 微博登出
     */
    public static void logout() {
        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
        httpClientSimpleUtil.doGetRequest("http://open.weibo.com/logout.php");
    }

    /**
     * 获取accessToken
     * @param code  授权获取的code
     * @return  ACCESS_TOKEN
     * @throws Exception
     */
    private static String getAccessToken(String code) {
        //是否过期
        if(LocalDateTime.now().compareTo(ACCESS_TOKEN_EXPIRY_TIME)>0){
            HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
            Map paramMap = new HashMap();
            paramMap.put("client_id",WeiboConfig.getAppKey());
            paramMap.put("client_secret",WeiboConfig.getAppSecret());
            paramMap.put("redirect_uri",WeiboConfig.getRedirectUri());
            paramMap.put("grant_type","authorization_code");
            paramMap.put("code",code);
            String result = httpClientSimpleUtil.doPostRequest(ACCESS_TOKEN_URL,paramMap);
            Map resultMap = JSON.parseObject(result,Map.class);
            if(resultMap.get("access_token")==null || resultMap.get("access_token").toString().trim().equals("")){
                throw new RRException(resultMap.get("errcode").toString()+":"+resultMap.get("errmsg").toString());
            }
            //当前时间加上有效时间减5分钟
            ACCESS_TOKEN_EXPIRY_TIME = LocalDateTime.now().plusMinutes(((new Long(resultMap.get("expires_in").toString()))/60)-5);
            ACCESS_TOKEN = resultMap.get("access_token").toString();
            WeiboConfig.setUid(resultMap.get("uid").toString());
        }
        return ACCESS_TOKEN;
    }

    /**
     * 获取当前登录用户信息
     * @return  数据map
     */
    public static Map<String,Object> getNowUserInfo(){
        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
        Map paramMap = new HashMap();
        paramMap.put("access_token",ACCESS_TOKEN);
        paramMap.put("uid",WeiboConfig.getUid());
        String result = httpClientSimpleUtil.doGetRequest(USER_INFO_URL,paramMap);
        Map resultMap = "".equals(result)?new HashMap():JSON.parseObject(result,Map.class);
        return resultMap;
    }

    /**
     * 获取当前access_token信息
     * @return  数据map
     */
    public static Map<String,Object> getAccessTokenState(){
        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
        Map paramMap = new HashMap();
        paramMap.put("access_token",ACCESS_TOKEN);
        String result = httpClientSimpleUtil.doPostRequest(ACCESS_TOKEN_STATE_URL,paramMap);
        Map resultMap = "".equals(result)?new HashMap():JSON.parseObject(result,Map.class);
        return resultMap;
    }

    /**
     * 获取当前登录用户及其所关注（授权）用户的最新微博
     * @param pageCurrent  页号,字段名:pageCurrent,默认1,从第1页开始
     * @param pageSize  页长,字段名:pageSize,默认10
     * @return  数据map
     */
    public static Map<String,Object> getHomeTimeline(Integer pageCurrent,Integer pageSize){
        if (pageCurrent == null || pageCurrent < 1) {
            pageCurrent = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();
        Map paramMap = new HashMap();
        paramMap.put("page",pageCurrent);
        paramMap.put("count",pageSize);
        paramMap.put("access_token",ACCESS_TOKEN);
        String result = httpClientSimpleUtil.doGetRequest(HOME_TIMELINE_URL,paramMap);
        Map map = JSON.parseObject(result,Map.class);
        return map;
    }



    private static String refreshSinaToken() {
        Properties props = new Properties();
        try {
//            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
            String url = AUTHORIZE_URL;/*模拟登录的地址，https://api.weibo.com/oauth2/authorize*/
            PostMethod postMethod = new PostMethod(url);
            postMethod.addParameter("client_id", WeiboConfig.getAppKey());//client id
            postMethod.addParameter("redirect_uri", WeiboConfig.getRedirectUri());//回调url
            postMethod.addParameter("userId", WeiboConfig.getUserName());//微博的帐号
            postMethod.addParameter("passwd", WeiboConfig.getPASSWORD());//微博的密码
            postMethod.addParameter("isLoginSina", "0");
            postMethod.addParameter("action", "submit");
            postMethod.addParameter("response_type", "code");//code
            HttpMethodParams param = postMethod.getParams();
            param.setContentCharset("UTF-8");
            List<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Referer", AUTHORIZE_URL+"?client_id=" + WeiboConfig.getAppKey() + "&redirect_uri=" + WeiboConfig.getRedirectUri() + "&from=sina&response_type=code"));//伪造referer
            headers.add(new Header("Host", "api.weibo.com"));
            headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0"));
            HttpClient client = new HttpClient();
            client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
            client.executeMethod(postMethod);
            int status = postMethod.getStatusCode();
            if (status != 302) {
                return null;
            }
            Header location = postMethod.getResponseHeader("Location");
            if (location != null) {
                String retUrl = location.getValue();
                int begin = retUrl.indexOf("code=");
                if (begin != -1) {
                    int end = retUrl.indexOf("&", begin);
                    if (end == -1)
                        end = retUrl.length();
                    String code = retUrl.substring(begin + 5, end);
                    if (code != null) {
                        return getAccessToken(code);
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    /**
     * 获取指定oid用户微博
     * @param oid  oid
     * @param pageCurrent  页号,字段名:pageCurrent,默认1,从第1页开始
     * @param pageSize  页长,字段名:pageSize,默认10
     * @return  数据map
     */
    public static Map<String,Object> findWeiboByOid(String oid,Integer pageCurrent,Integer pageSize){
        if (pageCurrent == null || pageCurrent < 1) {
            pageCurrent = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        HttpClientSimpleUtil httpClientSimpleUtil = HttpClientSimpleUtil.getInstance();

        Map paramMap = new HashMap();
        paramMap.put("containerid",WeiboConfig.getContainerId(oid));
        paramMap.put("page",pageCurrent);
        paramMap.put("count",pageSize);
        String result = httpClientSimpleUtil.doGetRequest(DATA_URL,paramMap);

        Map<String,Object> map = JSON.parseObject(result,Map.class);
        return map;
    }


}
