package com.ourslook.guower.utils.weibo;

import java.util.ArrayList;
import java.util.List;

public class WeiboConfig {

    /**
     * 微博查询用户数据信息头
     */
    private static final String USER_HEAD = "100505";

    /**
     * 微博查询微博数据信息头
     */
    private static final String HEAD = "107603";

    /**
     * 无需授权，直接访问（需求要求，没办法）oid集合
     */
    private static final List<String> OID_LIST = new ArrayList<>();
    {
        OID_LIST.add("5957748261");//果味Pro
        OID_LIST.add("3908645609");//比特币秋山君
        OID_LIST.add("2811910187");//比特币女博士
        OID_LIST.add("1402559840");//暴走恭亲王
        OID_LIST.add("2026366955");//孙宇晨
        OID_LIST.add("1658151040");//区块链最前线
        OID_LIST.add("6324624653");//白话区块链
        OID_LIST.add("5941645212");//Bitangel宝二爷
        OID_LIST.add("1658066713");//赵乐天
        OID_LIST.add("1232750075");//长铗
        OID_LIST.add("5339148412");//硅谷王川
        OID_LIST.add("6501723193");//日本区块链前哨
        OID_LIST.add("1839109034");//比特币战车
        OID_LIST.add("2636267377");//比特币科技
        OID_LIST.add("1867153750");//肖磊看市
        OID_LIST.add("3632226187");//比特币的那点事
        OID_LIST.add("5426678271");//老赵读币
        OID_LIST.add("6575589741");//山寨币大王
        OID_LIST.add("2341202571");//币讯比特币
        OID_LIST.add("1867153750");//肖磊看市
        //OID_LIST.add("未找到");//币圈猫叔
        OID_LIST.add("1576218000");//李笑来
        OID_LIST.add("1265915191");//何一Miss
        OID_LIST.add("3458288384");//币圈大事件123
        OID_LIST.add("1967417421");//比特吴
        OID_LIST.add("1187986757");//徐小平
        OID_LIST.add("5513785613");//币圈参讯
        OID_LIST.add("2588334612");//INB老猫
        OID_LIST.add("3896414217");//知乎君
    }

    /**
     * 新浪微博开发者账号
     */
    private static final String USER_NAME = "18501421062";

    /**
     * 新浪微博开发者密码
     */
    private static final String PASSWORD = "Gnation2018";

    /**
     * appKey
     */
    private static final String APP_KEY = "3689934678";

    /**
     * appSecret
     */
    private static final String APP_SECRET = "a7198001008fb8b1349711843ec5fd7c";

    /**
     * 授权回调地址（必须与应用填写的回调地址一致）
     */
    private static final String REDIRECT_URI = "http://127.0.0.1:8981/guower/api/weibo/checkCode";

    /**
     * 调用accessToken的code（通过微博调接口赋值）
     */
    private static String code = "";

    /**
     * 当前用户id
     */
    private static String uid = "5957748261";

    public static String getUserName() {
        return USER_NAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getAppKey() {
        return APP_KEY;
    }

    public static String getAppSecret() {
        return APP_SECRET;
    }

    public static String getRedirectUri() {
        return REDIRECT_URI;
    }

    public static String getCode() {
        return code;
    }

    public static void setCode(String newCode) {
        code = newCode;
    }

    public static List<String> getOidList() {
        return OID_LIST;
    }

    public static String getHEAD() {
        return HEAD;
    }

    public static String getContainerId(String oid) {
        return HEAD+oid;
    }

    public static String getUserHead() {
        return USER_HEAD;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        WeiboConfig.uid = uid;
    }
}
