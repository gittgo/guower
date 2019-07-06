package com.ourslook.guower.utils.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ourslook.guower.entity.SysJpushRecordEntity;
import com.ourslook.guower.service.SysJpushRecordService;
import com.ourslook.guower.utils.spring.SpringContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangl
 * @times: 2015-4-29下午03:50:33
 * 类的说明：消息推送
 * <p>
 * <p>
 * 常见问题 - JPush 合集（持续更新）https://community.jiguang.cn/t/jpush/5145/2
 * 你是否推送的是自定义消息而不是通知消息，自定义消息只有当 App 处于前台时才会接收，且默认是不展示的。
 * <p>
 * 开发文档：https://docs.jiguang.cn/jpush/server/sdk/java_sdk/
 * <p>
 * <p>
 * jpush:别名与标签
 * jpush:alias 与 tag
 * https://docs.jiguang.cn/jpush/client/Android/android_api/#api_3
 * 别名 alias:每个用户只能指定一个别名。
 * 标签 tag:为安装了应用程序的用户，打上标签。其目的主要是方便开发者根据标签，来批量下发 Push 消息。可为每个用户打多个标签。
 **/
public class JPush {

    private static final Logger LOG = LoggerFactory.getLogger(JPush.class);

    /**
     * 用户端
     */
    protected static final String USER_APPKEY = "39f48109668a0274f7cabd99";
    protected static final String USER_MASTER_SECRET = "46504e49a189135746f2cb53";
    /**
     * ios的推送环境是正式的还是测试环境，开发调试阶段是false; 正式上架是Ture
     * 第一个是用户端；
     * 第二个是商家端：
     */
    protected static final Boolean IOS_APNS_PRODUCTION = Boolean.TRUE;
    protected static final Boolean IOS_APNS_PRODUCTION_2 = Boolean.TRUE;

    /**
     * 商家端
     */
    protected static final String USER2_APPKEY = "5ba5ccdba0fad04eadb27d53";
    protected static final String USER2_MASTER_SECRET = "46659359886ab90dfeba846d";

    private String title = null;
    private String subTitle = null;

    @SuppressWarnings("static-access")
    public JPush(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    /**
     * 指定jpush的key和secret进行推送
     *
     * @param payload
     * @param appsecret
     * @param appkey
     * @return
     */
    public boolean sendPush(PushPayload payload, String appsecret, String appkey) {
        JPushClient jpushClient = new JPushClient(appsecret, appkey);
        //保存推送日志信息
        SysJpushRecordEntity sysJpushRecordEntity = new SysJpushRecordEntity();
        try {

            this.updateSysJpushRecord(sysJpushRecordEntity, payload, appsecret, appkey);

            PushResult result = jpushClient.sendPush(payload);

            LOG.info("Got result - " + result);
            return true;
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);

            sysJpushRecordEntity.setErrorCode("");
            sysJpushRecordEntity.setErrorMsg(e.getMessage());
            sysJpushRecordEntity.setResult(0);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());

            sysJpushRecordEntity.setErrorCode(e.getErrorCode() + "");
            sysJpushRecordEntity.setErrorMsg(e.getErrorMessage());
            sysJpushRecordEntity.setResult(0);
        } finally {
            //保存推送日志
            sysJpushRecordEntity.setAppKey(appkey);
            sysJpushRecordEntity.setAppSecert(appsecret);
            sysJpushRecordEntity.setNotification(payload != null ? payload.toString() : "");
            sysJpushRecordEntity.setCreateDate(LocalDateTime.now());

            SysJpushRecordService sysJpushRecordService = SpringContextHelper.getBean(SysJpushRecordService.class);
            sysJpushRecordService.save(sysJpushRecordEntity);
        }
        return false;
    }

    public boolean sendPush(PushPayload payload) {
        return sendPush(payload, USER_MASTER_SECRET, USER_APPKEY);
    }


    /**
     * 广播通知（通知所有设备）
     *
     * @return
     */
    public PushPayload buildPushObjectAlert() {
        return PushPayload.alertAll(subTitle);
    }


    /**
     * 构建IOS和ANDRIOD对象
     * <p>
     * 注意：incrBadge 设置ios每次角标增加1；setMutableContent： ios可以在应用杀死的情况还可以朗读声音
     *
     * @param extras 参数值
     * @param mobile
     * @return
     */
    public PushPayload buildPushObjectAndroidAndIos(Map extras, List<String> mobile) {
        Audience aud;
        if (mobile != null) {
            aud = Audience.newBuilder().addAudienceTarget(AudienceTarget.alias(mobile)).build();
        } else {
            aud = Audience.all();
        }

        /**
         * ios 推送环境设置
         */
        boolean apnProdution = Boolean.parseBoolean(extras.get(JPushMessgeUtil.EXTRASS_KEY_ACCOUNT_IOS_APNS_PRODUCTION) + "");

        return PushPayload
                .newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(aud)
                //自定义消息
                /*.setMessage(
                        Message.newBuilder().setMsgContent(this.subTitle)
                                .setTitle(title).build())*/
                //通知消息
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(subTitle)
                                .addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtras(extras).build())
                                .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).setSound("sound.caf").setMutableContent(true).setContentAvailable(true).addExtras(extras).build())
                                .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(apnProdution)
                        .build()).build();
    }

    /**
     * @author 吴楠
     * 保存推送日志信息
     * @param sysJpushRecordEntity
     * @param payload
     * @param appsecret
     * @param appkey
     * @return
     */
    private SysJpushRecordEntity updateSysJpushRecord(SysJpushRecordEntity sysJpushRecordEntity, PushPayload payload, String appsecret, String appkey) {
        sysJpushRecordEntity.setResult(1);
        //记录到数据库
        JSONObject jsonObject = JSON.parseObject(payload.toString());
        //推送平台
        List<String> platform = (List) jsonObject.get("platform");
        StringBuffer platforms = new StringBuffer();
        for (String s : platform) {
            platforms.append(s);
            platforms.append(",");
        }
        if (platforms.length() > 1) {
            platforms.deleteCharAt(platforms.length() - 1);
        }
        sysJpushRecordEntity.setPlatform(platforms.toString());
        //推送目标
        List<String> audience = (List) ((JSONObject) jsonObject.get("audience")).get("alias");
        StringBuffer audiences = new StringBuffer();
        for (String s : audience) {
            audiences.append(s);
            audiences.append(",");
        }
        if (audiences.length() > 1) {
            audiences.deleteCharAt(audiences.length() - 1);
        }
        sysJpushRecordEntity.setAudience(audiences.toString());
        //ios的生产环境还是测试环境
        String apnsProduction = ((JSONObject) jsonObject.get("options")).get("apns_production").toString();
        sysJpushRecordEntity.setApnsProduction(apnsProduction);
        //发送编号
        String sendNo = ((JSONObject) jsonObject.get("options")).get("sendno").toString();
        sysJpushRecordEntity.setSendNo(sendNo);
        //通知内容
        if(platform.contains("android")){
            //安卓
            JSONObject androidMsg = (JSONObject) ((JSONObject) jsonObject.get("notification")).get("android");
        }
        if(platform.contains("ios")){
            //ios
            JSONObject iosMsg = (JSONObject) ((JSONObject) jsonObject.get("notification")).get("ios");
        }
        //标题
        sysJpushRecordEntity.setTitle(title);
        //副标题
        sysJpushRecordEntity.setSubtitle(subTitle);
        return sysJpushRecordEntity;
    }
}
