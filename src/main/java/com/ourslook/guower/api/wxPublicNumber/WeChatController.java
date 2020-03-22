package com.ourslook.guower.api.wxPublicNumber;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.ourslook.guower.utils.RandomUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.encryptdir.Md5Util;
import com.ourslook.guower.utils.http.HttpClientSimpleUtil;
import com.ourslook.guower.utils.pay.weixin.config.WxPublicNumberConfig;
import com.ourslook.guower.utils.pay.weixin.mp.WeixinMpInterface;
import com.ourslook.guower.utils.result.XaResult;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信配置服务器路径就是项目地址/api/wechat/token/get
 */
@Controller
@RequestMapping("/api/wechat/token")
@ApiIgnore
public class WeChatController {

    /**
     * 文本消息
     */
    public static final String MESSAGE_TEXT = "text";
    /**
     * 图片消息
     */
    public static final String MESSAtGE_IMAGE = "image";
    /**
     * 图文消息
     */
    public static final String MESSAGE_NEWS = "news";


    // 各种消息类型,除了扫带二维码事件
    /**
     * 语音消息
     */
    public static final String MESSAGE_VOICE = "voice";
    /**
     * 视频消息
     */
    public static final String MESSAGE_VIDEO = "video";
    /**
     * 小视频消息
     */
    public static final String MESSAGE_SHORTVIDEO = "shortvideo";
    /**
     * 地理位置消息
     */
    public static final String MESSAGE_LOCATION = "location";
    /**
     * 链接消息
     */
    public static final String MESSAGE_LINK = "link";
    /**
     * 事件推送消息
     */
    public static final String MESSAGE_EVENT = "event";
    /**
     * 事件推送消息中,事件类型，subscribe(订阅)
     */
    public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";
    /**
     * 事件推送消息中,事件类型，unsubscribe(取消订阅)
     */
    public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";
    /**
     * 事件推送消息中,上报地理位置事件
     */
    public static final String MESSAGE_EVENT_LOCATION_UP = "LOCATION";
    /**
     * 事件推送消息中,自定义菜单事件,点击菜单拉取消息时的事件推送
     */
    public static final String MESSAGE_EVENT_CLICK = "CLICK";
    /**
     * 事件推送消息中,自定义菜单事件,点击菜单跳转链接时的事件推送
     */
    public static final String MESSAGE_EVENT_VIEW = "VIEW";
    /**
     * 返回给微信的token，在公众号配置服务器时填写的token
     */
    private static final String TOKEN = "ourslook_jiulong_token";
    /**
     * 微信公众号设置的EncodingAESKey
     * 首先请注意，开发者在接收消息和事件时，都需要进行消息加解密（某些事件可能需要回复，回复时也需要先进行加密）
     * 但是，通过API主动调用接口（包括调用客服消息接口发消息）时，不需要进行加密
     */
    private static final String EncodingAESKey = "GguQ6CyESYtPKHJ5QbMLHY8NNvJA4hY6XxRGHdHYf67";
    /**
     * 获取微信token的url
     */
    private static final String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WxPublicNumberConfig.getAppId() + "&secret=" + WxPublicNumberConfig.getAppSecret();
    /**
     * 获取微信ticket
     */
    private static final String WX_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    /**
     * 生成签名的参数排序
     */
    private static final String WX_SIGNATURE_PARAM = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";

    private static final String REDIS_WX_TOKEN_KEY = "wx_token";
    private static final String REDIS_WX_TICKET_KEY = "wx_ticket";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将request的参数转化为String
     *
     * @param request
     * @return
     */
    private static String reqToStr(HttpServletRequest request) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String xmlResult = sb.toString();
        return xmlResult;
    }

    /**
     * 将request的参数转化为Map集合（未加密）
     *
     * @param request
     * @return
     */
    private static Map<String, String> reqToMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        InputStream ins = null;
        try {
            ins = request.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Document doc = null;
        try {
            doc = reader.read(ins);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        Element root = doc.getRootElement();
        @SuppressWarnings("unchecked")
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        try {
            ins.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return map;
    }

    /**
     * 微信公众号开启服务器配置
     *
     * @return
     */
    @RequestMapping("get")
    @IgnoreAuth
    public void getToken(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        try {
            //判断微信发来的请求类型,需要分开处理
            //get为获取token
            //post为推送消息
            boolean isGet = request.getMethod().toLowerCase().equals("get");
            if (isGet) {
                // 将token、timestamp、nonce三个参数进行字典序排序
                System.err.println("signature:" + request.getParameter("signature"));
                System.err.println("timestamp:" + request.getParameter("timestamp"));
                System.err.println("nonce:" + request.getParameter("nonce"));
                System.err.println("echostr:" + request.getParameter("echostr"));
                System.err.println("TOKEN:" + TOKEN);
                String[] params = new String[]{TOKEN, request.getParameter("timestamp"), request.getParameter("nonce")};
                Arrays.sort(params, String.CASE_INSENSITIVE_ORDER);
                // 将三个参数字符串拼接成一个字符串进行sha1加密
                String clearText = params[0] + params[1] + params[2];
                String result = Md5Util.SHA1(clearText);
                // 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
                if (request.getParameter("signature").equals(result)) {
                    response.getWriter().print(request.getParameter("echostr"));
                }
            } else {
                System.err.println("post请求:");
                System.err.println("signature:" + request.getParameter("signature"));
                System.err.println("timestamp:" + request.getParameter("timestamp"));
                System.err.println("nonce:" + request.getParameter("nonce"));
                System.err.println("echostr:" + request.getParameter("echostr"));
                System.err.println("TOKEN:" + TOKEN);
                // TODO 加密未完成，因加密需要修改jdk中的文件，并未测试,当前是明文模式
                //加解密工具
//                WXBizMsgCrypt web = new WXBizMsgCrypt(TOKEN, EncodingAESKey, WxPublicNumberConfig.getAppId());

                String resultXmlStr = "";
                //通过request读取数据生成map

                Map<String, String> map = reqToMap(request);
//                String fromXML = reqToStr(request);
//                //密文
//                String encrypt = map.get("Encrypt");
//                //时间戳
//                String timeStamp = map.get("timestamp");
//                //随机数
//                String nonce = map.get("nonce");
//                //消息体签名
//                String msgSignature = map.get("msg_signature");
//                //解密
//                String msgString = web.decryptMsg(msgSignature, timeStamp, nonce, fromXML);
//                map = WXPayUtil.xmlToMap(msgString);
                //根据map自动获取结果
                resultXmlStr = processRequest(map);
                //加密
//                resultXmlStr = web.encryptMsg(resultXmlStr, timeStamp, nonce);
                //将结果写入responseBody
                response.getWriter().print(resultXmlStr);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 获取微信转发的签名
     *
     * @param url 需要转发的url
     * @return
     * @throws Exception
     */
    @RequestMapping("/signature")
    @ResponseBody
    @IgnoreAuth
    public XaResult<Map<String, Object>> getSignature(@ApiParam("需要转发的url") @RequestParam(defaultValue = "http://www.iguower.com") String url) throws Exception {
        //先从redis获取
        //String ticket = stringRedisTemplate.opsForValue().get(REDIS_WX_TICKET_KEY);
        //if (StringUtils.isBlank(ticket)) {
        String result = HttpClientSimpleUtil.getInstance().doGetRequest(WX_TOKEN_URL);
        if (StringUtils.isBlank(result)) {
            throw new Exception("获取微信token失效");
        }
        //获取token和失效时间
        String token = JSON.parseObject(result).getString("access_token");
        int expireMills = JSON.parseObject(result).getInteger("expires_in");
        stringRedisTemplate.opsForValue().set(REDIS_WX_TOKEN_KEY, token, expireMills - 100);
        String ticketResult = HttpClientSimpleUtil.getInstance().doGetRequest(String.format(WX_TICKET_URL, token));
        String ticket = JSON.parseObject(ticketResult).getString("ticket");
        stringRedisTemplate.opsForValue().set(REDIS_WX_TICKET_KEY, ticket, expireMills - 100);
        //}

        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = RandomUtils.getRandomStringByLength(32);
        //sha1

        String sha1Str = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        String signature = Md5Util.SHA1(sha1Str);
        return new XaResult<Map<String, Object>>().setObject(ImmutableMap.of("timestamp", timestamp, "signature", signature, "nonceStr", nonceStr, "ticket", ticket, "oldStr", sha1Str));
    }


    /**
     * 用户消息处理
     *
     * @param map
     * @return
     * @throws Exception
     */
    private String processRequest(Map<String, String> map) throws Exception {
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");

        // 对消息进行处理
        //最终发送结果
        Map<String, String> textMessageMap = new HashMap();
        //发送方，接收方，创建时间
        textMessageMap.put("ToUserName", fromUserName);
        textMessageMap.put("FromUserName", toUserName);
        textMessageMap.put("CreateTime", System.currentTimeMillis() + "");
        // 默认回复的内容
        String responseMessage;
        if (MESSAGE_TEXT.equals(msgType)) {
            // 用户发送的是文本消息
            //消息内容
            String content = map.get("Content");
            //可对发送来的消息做判断，具体回复消息
            if (content != null && !content.equals("") && content.contains("新闻")) {
                textMessageMap.put("MsgType", MESSAGE_NEWS);
            } else {
                textMessageMap.put("MsgType", MESSAGE_TEXT);
                textMessageMap.put("Content", "XXX正在认真思考您的消息，请先看看其他的内容吧");
            }
        } else if (MESSAGE_EVENT.equals(msgType)) {
            //事件推送处理
            //事件类型
            String eventType = map.get("Event");
            if (MESSAGE_EVENT_SUBSCRIBE.equals(eventType)) {
                //关注,默认发送文本信息
                textMessageMap.put("MsgType", MESSAGE_TEXT);
                textMessageMap.put("Content", "欢迎关注XXX！");
                try {
                    //fromUserName为关注者的openid，可编写自己的业务逻辑
                } catch (Exception e) {
                    e.getStackTrace();
                }
            } else if (MESSAGE_EVENT_UNSUBSCRIBE.equals(eventType)) {
                //取消关注事件

            }
        } else {
            //这里填写默认的自动回复
            textMessageMap.put("MsgType", MESSAGE_TEXT);
            textMessageMap.put("Content", "XXX默认回复的内容XXX");
        }
        //把map生成为最终回复给微信的xml
        responseMessage = replyMessage(textMessageMap);
        return responseMessage;

    }


    /**
     * 生成自定义菜单（一般只会执行一次）
     * 因为在公众号后台设置的菜单在启用服务器时将失效，只能用自定义菜单接口来创建
     * 请注意：
     * 1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
     * 2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
     * 3、创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
     * 如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
     * 测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
     * 请参考 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013
     *
     * @return 结果map errcode=0为成功
     */
    @GetMapping("createMenu")
    @IgnoreAuth
    @ResponseBody
    public Map createMenu(
            String key
    ) throws Exception {
        return WeixinMpInterface.createMenu();
    }


    /**
     * 回复消息
     *
     * @param map 参数集合    MsgType决定回复类型
     * @return xml字符串
     */
    public String replyMessage(Map<String, String> map) {
        //回复消息类型
        String msgType = map.get("MsgType");
        StringBuffer resultXml = new StringBuffer();
        resultXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        resultXml.append("<xml>");

        resultXml.append("<ToUserName><![CDATA[");
        //接收方帐号（收到的OpenID）
        resultXml.append(map.get("ToUserName"));
        resultXml.append("]]></ToUserName>");

        resultXml.append("<FromUserName><![CDATA[");
        //开发者微信号
        resultXml.append(map.get("FromUserName"));
        resultXml.append("]]></FromUserName>");

        resultXml.append("<CreateTime>");
        //消息创建时间 （整型）
        resultXml.append(map.get("CreateTime"));
        resultXml.append("</CreateTime>");

        resultXml.append("<MsgType><![CDATA[");//消息类型

        StringBuffer xml = new StringBuffer();
        //文本
        if (MESSAGE_TEXT.equals(msgType)) {
            //消息类型
            resultXml.append(MESSAGE_TEXT);
            resultXml.append("]]></MsgType>");

            xml.append("<Content><![CDATA[");
            //回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
            xml.append(map.get("Content"));
            xml.append("]]></Content>");
        } else if (MESSAGE_NEWS.equals(msgType)) {
            //这里可查找自己的广告或者新闻发送
            try {
                List advertisementEntityList = new ArrayList();
                //图文消息总条数，请注意，最多为8个，超过8个微信将无法推送成功
                int count = 0;
                xml.append("<Articles>");
                for (Object advertisementEntity : advertisementEntityList) {
                    xml.append("<item>");

                    xml.append("<Title><![CDATA[");
                    //标题
                    xml.append("这是标题");
                    xml.append("]]></Title>");

                    xml.append("<Description><![CDATA[");
                    //描述
                    xml.append("这是描述");
                    xml.append("]]></Description>");

                    xml.append("<PicUrl><![CDATA[");
                    //图片地址(支持JPG、PNG格式，较好的效果为大图360*200，小图200*200)
                    xml.append("这是图片地址");
                    xml.append("]]></PicUrl>");

                    xml.append("<Url><![CDATA[");
                    //跳转地址
                    xml.append("这是点击跳转地址");
                    xml.append("]]></Url>");

                    xml.append("</item>");
                    count++;
                    if (count >= 8) {
                        break;
                    }
                }
                xml.append("</Articles>");

                xml.append("<ArticleCount>");
                //图文消息个数，限制为8条以内
                xml.append(count);
                xml.append("</ArticleCount>");
                //如果暂时没有数据，则发送文本提示，防止报错
                //这里因为推送类型为news而无数据，公众号将出现“该公众号暂时无法提供服务，请稍后再试”，为了避免这种情况，若无数据则改为文本发送
                if (count <= 0) {
                    //消息类型
                    resultXml.append(MESSAGE_TEXT);
                    resultXml.append("]]></MsgType>");

                    xml = new StringBuffer();
                    xml.append("<Content><![CDATA[");
                    //回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
                    xml.append("暂无内容，XXX");
                    xml.append("]]></Content>");

                } else {
                    //消息类型
                    resultXml.append(MESSAGE_NEWS);
                    resultXml.append("]]></MsgType>");
                }

            } catch (Exception e) {
                throw e;
            }
        }
        //添加内容
        resultXml.append(xml);
        resultXml.append("</xml>");
        return resultXml.toString();
    }


}

