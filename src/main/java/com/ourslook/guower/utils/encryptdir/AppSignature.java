package com.ourslook.guower.utils.encryptdir;


import com.ourslook.guower.utils.StringUtilss;
import com.ourslook.guower.utils.XaUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.collections.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.ourslook.guower.utils.encryptdir.Md5Util.SHA1;
import static com.ourslook.guower.utils.encryptdir.Md5Util.getMd5;

/**
 * @author dazer
 * 对应ios、android、web调用接口进行验证签名
 * <p>
 * <p>
 * <p>
 * 后续研究的加密方式：
 * 访问需要HTTP Basic Authentication认证的资源的各种开发语言的实现
 * https://www.cnblogs.com/pingming/p/4165057.html
 * <p>
 * 一是在请求头中添加Authorization：
 * Authorization: "Basic 用户名和密码的base64加密字符串"
 * 二是在url中添加用户名和密码：
 * http://userName:password@api.minicloud.com.cn/statuses/friends_timeline.xml
 */
public class AppSignature {
    /**
     * 进行验签看用户是否合法
     *
     * @param signature  客户端签名后的值
     * @param deviceName 即：driverName,取值 ios,android,web
     * @param deviceUuid 即：uuid 设备全球唯一编码,android's IMEI，iphone's UUID
     * @param timestamp  时间戳
     * @return 合法用户返回空字符；否则返回错误原因
     */
    @SuppressWarnings("unchecked")
    public static String isAuthUser(String signature, String deviceName, String deviceUuid, String privateKey, String timestamp,
                                    Map<String, String[]> params) {
        String errorMsg = "";
        if (XaUtils.isBlank(signature)) {
            errorMsg = "auth signature blank error";
            return errorMsg;
        }
        if (XaUtils.isBlank(deviceName)) {
            errorMsg = "auth deviceName blank error";
            return errorMsg;
        }
        if (!Lists.newArrayList("ios", "android", "web").
                contains(deviceName.trim().toLowerCase())) {
            errorMsg = "auth deviceName blank error ! ";
            return errorMsg;
        }
        if (XaUtils.isBlank(deviceUuid)) {
            errorMsg = "auth deviceUuid  blank error";
            return errorMsg;
        }
        if (XaUtils.isNotBlank(privateKey)) {
            errorMsg = "auth privateKey must is blank error";
            return errorMsg;
        }
        if (XaUtils.isBlank(timestamp)) {
            errorMsg = "auth timestamp blank error";
            return errorMsg;
        }
        if (timestamp.trim().length() != 10) {
            errorMsg = "auth timestamp length error,unit min ";
            return errorMsg;
        }
        long curSeconds = System.currentTimeMillis() / 1000;
        Long minMillisDiff = (curSeconds -
                NumberUtils.toLong(timestamp, curSeconds)) * 1000;
        if (minMillisDiff > 4 * 60 * 1000 || minMillisDiff <= -2 * 60 * 1000) {
            errorMsg = "auth timestamp fail !";
            return errorMsg;
        }

        //0:处理数据
        privateKey = "pwk421341234sdaf41232141234123";
        if ("android".equalsIgnoreCase(deviceName)) {
            privateKey = "pakf98dc169b0ce15f4f4248198322";
        } else if ("ios".equalsIgnoreCase(deviceName)) {
            privateKey = "pikb25cc12ee8e677418c738460b05";
        }
        params.put("privateKey", new String[]{privateKey});
        //1:排序
        List<String> keys = Lists.newArrayList(params.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        //2:拼接
        String keyValues = "";
        for (int i = 0; i < keys.size(); ++i) {
            String key = keys.get(i);
            String[] val = params.get(key);
            if (XaUtils.isNotEmpty(val)) {
                keyValues = keyValues + (key + "=" + StringUtilss.join(Lists.newArrayList(val), ","));
            } else {
                keyValues = keyValues + (key + "=");
            }
        }

        //加密
        //signature = sha1(substr(md5(adf=123123os=iospasswd=123123privateKey=pikb25cc12ee8e677418c738460b05timestamp=1489659289180uname=秋伟光zdfsdf=sadfasdf),3,28))
        String serverSignature = SHA1(getMd5(keyValues).substring(3, 28));
        if (!signature.equalsIgnoreCase(serverSignature)) {
            errorMsg = "auth signature fail !";
        }
        return errorMsg;
    }
}
