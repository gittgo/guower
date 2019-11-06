package com.ourslook.guower.utils.pay.weixin.mp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.utils.LoggerUtil;
import com.ourslook.guower.api.wxPublicNumber.WeChatController;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.http.HttpClientSimpleUtil;
import com.ourslook.guower.utils.pay.weixin.PayMinProgressUtil;
import com.ourslook.guower.utils.pay.weixin.WXPayUtil;
import com.ourslook.guower.utils.pay.weixin.common.IServiceRequest;
import com.ourslook.guower.utils.pay.weixin.common.Util;
import com.ourslook.guower.utils.pay.weixin.config.WxPublicNumberConfig;
import com.ourslook.guower.utils.pay.weixin.config.WxpayConfig;
import com.ourslook.guower.utils.result.XaResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @see WeChatController
 */
@Controller
public class WeixinMpInterface {

    //调用微信接口凭证
    private static String ACCESS_TOKEN = "";

    //调用微信接口凭证到期时间
    private static LocalDateTime ACCESS_TIME = LocalDateTime.now();

    //调用JSApi接口凭证
    private static String JSAPI_TICKET = "";

    //调用JSApi接口凭证到期时间
    private static LocalDateTime JSAPI_TIME = LocalDateTime.now();

    /**
     * 获取access_token
     * @return  token
     */
    public static String getAccessToken(){
        //token过期
        if(LocalDateTime.now().isAfter(ACCESS_TIME)){
            HttpClientSimpleUtil h = HttpClientSimpleUtil.getInstance();

            String appid = WxPublicNumberConfig.getAppId();
            String secret = WxPublicNumberConfig.getAppSecret();
            String result = h.doGetRequest("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret);
            Map resultMap = JSON.parseObject(result,Map.class);
            if(resultMap.get("access_token")==null || resultMap.get("access_token").toString().trim().equals("")){
                throw new RRException(resultMap.get("errcode").toString()+":"+resultMap.get("errmsg").toString());
            }
            //当前时间加上有效时间减5分钟
            ACCESS_TIME = LocalDateTime.now().plusMinutes(((new Long(resultMap.get("expires_in").toString()))/60)-5);
            ACCESS_TOKEN = resultMap.get("access_token").toString();
        }
        return ACCESS_TOKEN;
    }

    /**
     * 获取jsapi_ticket
     * @return  token
     */
    private String getJsapiTicket(){
        //获取jsapi_ticket过期
        if(LocalDateTime.now().isAfter(JSAPI_TIME)){
            //先获取到AccessToken
            String accessToken = getAccessToken();
            HttpClientSimpleUtil h = HttpClientSimpleUtil.getInstance();
            String result = h.doGetRequest("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi");
            Map resultMap = JSON.parseObject(result,Map.class);
            if(resultMap.get("ticket")==null || resultMap.get("ticket").toString().trim().equals("")){
                throw new RRException(resultMap.get("errcode").toString()+":"+resultMap.get("errmsg").toString());
            }
            //当前时间加上有效时间减5分钟
            JSAPI_TIME = LocalDateTime.now().plusMinutes(((new Long(resultMap.get("expires_in").toString()))/60)-5);
            JSAPI_TICKET = resultMap.get("ticket").toString();
        }
        return JSAPI_TICKET;
    }

    /**
     * 获取jsapi调用配置信息
     * @param nowUrl 当前网址
     * @return
     */
    public Map getJsapiConfig(String nowUrl){
        Map resultMap = new HashMap<>();
        try{
            Map map = new HashMap<>();
            String randomStr = PayMinProgressUtil.createCode(10);
            String timestamp = String.valueOf(new Date().getTime()/1000);
            String jsapi_ticket = getJsapiTicket();
            map.put("noncestr",randomStr);
            map.put("jsapi_ticket",jsapi_ticket);
            map.put("timestamp",timestamp);
            map.put("url",nowUrl);
            Util.log("LOOK NOW URL:" + nowUrl);
            String result = WXPayUtil.generateSignatureByMapInSha1(map);
            resultMap.put("appId",WxpayConfig.getAppID());
            resultMap.put("timestamp",timestamp);
            resultMap.put("nonceStr",randomStr);
            resultMap.put("sign",result);
            resultMap.put("url",nowUrl);
            resultMap.put("jsapi_ticket",jsapi_ticket);
            return resultMap;
        }catch (Exception e){
            return resultMap;
        }
    }

    /**
     * 获取用户信息，网页授权，用户传入code
     * @param code 换取access_token的票据,注意这个和普通的access_token不一样
     * @param isAll 是否查询全部信息,不查询全部则只返回openid
     * @return  结果集 详见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
     */
    public Map<String,Object> getUserByCode(String code,boolean isAll){
        String appid = WxPublicNumberConfig.getAppId();
        String secret = WxPublicNumberConfig.getAppSecret();
        HttpClientSimpleUtil h = HttpClientSimpleUtil.getInstance();
        String result = h.doGetRequest("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code");
        Map map = JSON.parseObject(result,Map.class);
        LoggerUtil.info("\n ===https://api.weixin.qq.com/sns/oauth2/access_token======result:"+result);
        for(Object str : map.keySet()){
            LoggerUtil.info("\n "+str+"="+map.get(str));
        }
        if(map.get("access_token") == null || map.get("openid") == null){
            return map;
        }
        String openid = map.get("openid").toString();
        if(isAll){
            String token = map.get("access_token").toString();
            String userInfo = h.doGetRequest("https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+openid+"&lang=zh_CN");
            Map userInfoMap = JSON.parseObject(userInfo,Map.class);
            return userInfoMap;
        }
        Map openidMap = new HashMap();
        openidMap.put("openid",openid);
        return openidMap;
    }

    /**
     * 获取单个用户信息
     * @param openid 用户标识
     * @return  结果集 详见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140839
     */
    public Map<String,Object> getUserByOpenId(String openid){
        String accessToken = getAccessToken();
        HttpClientSimpleUtil h = HttpClientSimpleUtil.getInstance();
        String result = h.doGetRequest("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN");
        Map map = JSON.parseObject(result,Map.class);
        return map;
    }

    /**
     * 获取全部用户列表
     * total	关注该公众账号的总用户数
     * count	拉取的OPENID个数，最大值为10000
     * data	 列表数据，OPENID的列表
     * next_openid	拉取列表的最后一个用户的OPENID
     * 可参考 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140840
     * @return 用户openid
     */
    public Map getUserList(){
        HttpClientSimpleUtil h = HttpClientSimpleUtil.getInstance();
        String token = getAccessToken();
        String result = h.doGetRequest("https://api.weixin.qq.com/cgi-bin/user/get?access_token="+token);
        Map resultMap = JSON.parseObject(result,Map.class);
        return resultMap;
    }

    /**
     * 下载微信服务器临时素材
     * @param  wxSourceId 微信临时素材id
     * @param  saveUrl 保存到的url 不包含文件名
     * @return   下载到服务器中的url 包含文件名
     */
    public String loadWxSource(String wxSourceId,String saveUrl) {
        return downLoacWxSource(wxSourceId,"image",saveUrl);
    }

    /**
     * //由于视频使用的是http协议，而图片、语音使用http协议，故此处需要传递media_id和type
     * @param media_id 微信临时素材id
     * @param type  类型
     * @param saveUrl   下载到服务器中的url 包含文件名
     * @return
     */
    private  String downLoacWxSource(String media_id, String type,String saveUrl){
        String GET_TMP_MATERIAL_VIDEO = "http://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
        String GET_TMP_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
        try {
            String token = getAccessToken();
            String url = null;
            //视频是http协议
            if("video".equalsIgnoreCase(type)){
                url = String.format(GET_TMP_MATERIAL_VIDEO, token, media_id);
            }else{
                url = String.format(GET_TMP_MATERIAL, token, media_id);;
            }
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            String content_disposition = conn.getHeaderField("content-disposition");
            //微信服务器生成的文件名称
//            String file_name ="";
//            String[] content_arr = content_disposition.split(";");
//            if(content_arr.length  == 2){
//                String tmp = content_arr[1];
//                int index = tmp.indexOf("\"");
//                file_name =tmp.substring(index+1, tmp.length()-1);
//            }
            if(!saveUrl.endsWith(File.separator)){
                saveUrl += File.separator;
            }
            //创建文件
            String newName = System.currentTimeMillis() + ("video".equalsIgnoreCase(type)?".mp4":".png");

            File filedict = new File(saveUrl);
            if (!filedict.exists()) {
                filedict.mkdirs();
            }
            File file = new File(saveUrl + newName);
            //生成不同文件名称
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buf = new byte[2048];
            int length = bis.read(buf);
            while(length != -1){
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            String resultPath = saveUrl + newName ;
            file.createNewFile();
            return resultPath;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成自定义菜单（一般只会执行一次）
     * 因为在公众号后台设置的菜单在启用服务器时将失效，只能用自定义菜单接口来创建
     * 请注意：
     * 1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
     * 2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
     * 3、创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
     *    如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
     *    测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
     * 请参考 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013
     * @return  结果map errcode=0为成功
     */
    public static Map createMenu () throws Exception {
        Map queryOrderResultMap;
        try {
            //获取access_token
            String accessToken = WeixinMpInterface.getAccessToken();
            String menuUrl="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;

            JSONArray btnArray=new JSONArray();

            //第一个一级菜单
            JSONObject btn1=new JSONObject();
            btn1.put("name","案例干货");
            //第一个一级菜单的子菜单
            JSONArray btn1Array=new JSONArray();

            JSONObject btn1_1=new JSONObject();
            btn1_1.put("type","view");//类型（view表示网页类型，click表示点击类型，miniprogram表示小程序类型）
            btn1_1.put("name","案例干货");//名称
            btn1_1.put("url","http://chijun.xin/jiulongwechat/HTML/Cases/cases.html");//点击跳转的网页链接
            btn1_1.put("key","1_1");//菜单KEY值，用于消息接口推送，不超过128字节
            JSONObject btn1_2=new JSONObject();
            btn1_2.put("type","view");
            btn1_2.put("name","精彩视频");
            btn1_2.put("url","http://chijun.xin/jiulongwechat/HTML/Cases/GreatVideo.html");
            btn1_2.put("key","1_2");
            JSONObject btn1_3=new JSONObject();
            btn1_3.put("type","view");
            btn1_3.put("name","新闻动态");
            btn1_3.put("url","http://chijun.xin/jiulongwechat/HTML/Cases/newsDynamic.html");
            btn1_3.put("key","1_3");

            btn1Array.add(btn1_1);
            btn1Array.add(btn1_2);
            btn1Array.add(btn1_3);
            //设置子菜单
            btn1.put("sub_button",btn1Array);

            //第二个一级菜单
            JSONObject btn2=new JSONObject();
            btn2.put("name","合伙对接");
            //第二个一级菜单的子菜单
            JSONArray btn2Array=new JSONArray();
            JSONObject btn2_1=new JSONObject();
            btn2_1.put("type","view");
            btn2_1.put("name","找项目");
            btn2_1.put("url","http://chijun.xin/jiulongwechat/HTML/CooperaDocking/CooperaClass.html?title=findProject");
            btn2_1.put("key","2_1");
            JSONObject btn2_2=new JSONObject();
            btn2_2.put("type","view");
            btn2_2.put("name","找团队");
            btn2_2.put("url","http://chijun.xin/jiulongwechat/HTML/CooperaDocking/CooperaClass.html?title=findTeam");
            btn2_2.put("key","2_2");
            JSONObject btn2_3=new JSONObject();
            btn2_3.put("type","view");
            btn2_3.put("name","找工具");
            btn2_3.put("url","http://chijun.xin/jiulongwechat/HTML/CooperaDocking/CooperaClass.html?title=findTool");
            btn2_3.put("key","2_3");
            JSONObject btn2_4=new JSONObject();
            btn2_4.put("type","view");
            btn2_4.put("name","加入合伙人");
            btn2_4.put("url","http://chijun.xin/jiulongwechat/HTML/CooperaDocking/JoinPartner/index.html");
            btn2_4.put("key","2_4");
            JSONObject btn2_5=new JSONObject();
            btn2_5.put("type","view");
            btn2_5.put("name","活动报名");
            btn2_5.put("url","http://chijun.xin/jiulongwechat/HTML/CooperaDocking/ActivityApply/Enrollment.html");
            btn2_5.put("key","2_5");
            btn2Array.add(btn2_1);
            btn2Array.add(btn2_2);
            btn2Array.add(btn2_3);
            btn2Array.add(btn2_4);
            btn2Array.add(btn2_5);

            //设置子菜单
            btn2.put("sub_button",btn2Array);

            //第三个一级菜单
            JSONObject btn3=new JSONObject();
            btn3.put("name","个人中心");
            //第三个一级菜单的子菜单
            JSONArray btn3Array=new JSONArray();

            JSONObject btn3_1=new JSONObject();
            btn3_1.put("type","view");
            btn3_1.put("name","个人中心");
            btn3_1.put("url","http://chijun.xin/jiulongwechat/HTML/PersonalCenter/index.html");
            btn3_1.put("key","3_1");

            btn3Array.add(btn3_1);
            btn3.put("sub_button",btn3Array);

            JSONObject json=new JSONObject();
            btnArray.add(btn1);
            btnArray.add(btn2);
            btnArray.add(btn3);
            //设置子菜单
            json.put("button",btnArray);

            Class c = Class.forName(WxpayConfig.HttpsRequestClassName);
            IServiceRequest serviceRequest = (IServiceRequest) c.newInstance();
            String result = serviceRequest.send(menuUrl, json.toString());
            queryOrderResultMap = JSON.parseObject(result,Map.class);
        }catch (Exception e){
            e.getStackTrace();
            throw e;
        }
        return queryOrderResultMap;
    }
}
