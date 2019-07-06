package com.ourslook.guower.utils.jpush;

import com.google.gson.Gson;
import com.ourslook.guower.utils.XaUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangl
 * @Times: 2015-8-19下午02:10:39
 * 类的说明：
 * <p>
 * 这里默认推送，是推个用户端，如果引用有多个端的话，见重载方法
 * @see JPush
 **/
@SuppressWarnings("all")
public class JPushMessgeUtil {
    /**
     * @see JPush#IOS_APNS_PRODUCTION IOS推送的环境
     */
    public static final String EXTRASS_KEY_DATA = "data";//附加数据
    public static final String EXTRASS_KEY_TYPE = "type";//附加类型;区分是那个模块的推送消息
    public static final String EXTRASS_KEY_MUSIC = "music";//附加播放声音文本内容
    public static final String EXTRASS_KEY_MUSIC_OPEN = "musicOpen";//附加播放声音-开关是否开启, 参加用户表的switchVoiceOrder
    /**
     * 一下相关是区分那个端的账号、推送环境的
     */
    public static final String EXTRASS_KEY_ACCOUNT_APPKEY = "appkey";//指定推送账号用的key
    public static final String EXTRASS_KEY_ACCOUNT_MASTER_SECRET = "master_secret";//指定推送账号用的密钥
    public static final String EXTRASS_KEY_ACCOUNT_IOS_APNS_PRODUCTION = "ios_apns_production";//指定推送环境

    /**
     * 使用jpush推送消息给指定别名的用户，别名每个用户必须是唯一的
     * @param tilte 消息标题 ；可以为空
     * @param tilteSub 消息副标题，可以为空；
     * @param aliass  别名，如：用户id 、 手机号等 不能为空
     * @param extrass 附加消息，要包含数据、类型等, map的key请见如下常量；不能为空
     *
     * @see JPushMessgeUtil#EXTRASS_KEY_DATA  对象，如,map 、list 、 实体类 或者 vo
     * @see JPushMessgeUtil#EXTRASS_KEY_TYPE 可以用常量 {@link com.ourslook.guower.utils.Constant.JpushMessageType}
     * @see JPushMessgeUtil#EXTRASS_KEY_MUSIC
     * @see JPushMessgeUtil#EXTRASS_KEY_MUSIC_MUTABLE
     *
     * 示例代码
     * @<code>
     *   {
     *     String msg = messageSource.getMessage("hello", new Object[]{"dazer", "2018-05-24"}, LocaleContextHolder.getLocale());
     *     Map<String, Object> extrass = new HashMap<>(3);
     *     extrass.put(JPushMessgeUtil.EXTRASS_KEY_TYPE, Constant.JpushMessageType.SELLER_ORDER);
     *     extrass.put(JPushMessgeUtil.EXTRASS_KEY_DATA, new TbUserEntity());
     *     extrass.put(JPushMessgeUtil.EXTRASS_KEY_MUSIC, "我是要语音播报的声音");
     *     JPushMessgeUtil.pushMessageToBuinesss(tilte, msg, Lists.newArrayList("18049531390"), extrass);
     *   }
     * </code>
     *
     * 实例1：
     * 标题：商家已已接单
     * 内容：您的订单xx商家已接单、商家正在准备中，等待骑手取货
     *
     * 实例2：
     * 标题：骑手派送中
     * 内容：您的订单xx骑手已经取货、请耐心等待
     *
     * 实例3：
     * 标题：订单待评价
     * 内容：您的订单xx已经完成、快去发表评价
     *
     *  实例4：
     *  标题：恭喜您获得XX元推荐奖励
     *  内容：您邀请的学生（XXX） 有新的消费了，看去查看奖励吧~
     *
     */
    public static void pushMessage(String tilte, String tilteSub, List<String> aliass, Map<String, Object> extrass) {
        //附加信息
        HashMap<String, String> extrasHashMap = new HashMap<>(4);
        if (extrass != null) {
            for (Map.Entry<String, Object> entry : extrass.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (val == null) {
                    val = "";
                } else {
                    if (val instanceof Number) {
                        val = String.valueOf(val);
                    } else if (val instanceof CharSequence) {
                        val = val;
                    } else {
                        Gson gson = new Gson();
                        val = gson.toJson(val);
                    }
                }
                extrasHashMap.put(key, String.valueOf(val));
            }
        }
        String accountKey = extrasHashMap.get(EXTRASS_KEY_ACCOUNT_APPKEY);
        String accountMasterSecret = extrasHashMap.get(EXTRASS_KEY_ACCOUNT_MASTER_SECRET);

        extrasHashMap.remove(EXTRASS_KEY_ACCOUNT_APPKEY);
        extrasHashMap.remove(EXTRASS_KEY_ACCOUNT_MASTER_SECRET);

        tilte = StringUtils.isNotBlank(tilte) ? tilte : "您有一条新的提醒";
        tilteSub = StringUtils.isNotBlank(tilteSub) ? tilteSub : "您有一条新的提醒...";
        JPush push = new JPush(tilte, tilteSub);
        if (XaUtils.isNotEmpty(accountKey) && XaUtils.isNotEmpty(accountMasterSecret)) {
            //使用指定的账号 - 商家端
            extrasHashMap.put(EXTRASS_KEY_ACCOUNT_IOS_APNS_PRODUCTION, JPush.IOS_APNS_PRODUCTION_2+"");
            push.sendPush(push.buildPushObjectAndroidAndIos(extrasHashMap, aliass), accountMasterSecret, accountKey);
        } else {
            //使用默认的账号 - 用户端
            extrasHashMap.put(EXTRASS_KEY_ACCOUNT_IOS_APNS_PRODUCTION, JPush.IOS_APNS_PRODUCTION+"");
            push.sendPush(push.buildPushObjectAndroidAndIos(extrasHashMap, aliass));
        }
    }

    public static void pushAllMessage(String content) {
        //推送
        JPush jpush = new JPush("您有一条新的提醒", content);
        jpush.sendPush(jpush.buildPushObjectAlert());
    }

    /**
     * ================ 推送给商家端  ================
     * ================ 推送给商家端  ================
     * ================ 推送给商家端  ================
     */
    public static void pushMessageToBuinesss(String tilte, String subTilte, List<String> aliass, Map<String, Object> extrass) {
        //推送
        if (extrass != null) {
            extrass.put(EXTRASS_KEY_ACCOUNT_APPKEY, JPush.USER2_APPKEY);
            extrass.put(EXTRASS_KEY_ACCOUNT_MASTER_SECRET, JPush.USER2_MASTER_SECRET);
        }
        pushMessage(tilte, subTilte, aliass, extrass);
    }
}
