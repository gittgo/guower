package com.ourslook.guower.utils.jpush;

import com.google.common.collect.Lists;
import com.ourslook.guower.utils.Constant;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dazer
 * @date 2018/2/4 下午6:05
 */ 
public class TestJPushMessgeUtil {
    @Test
    public void pushMessage() {
//       JPushMessgeUtil.pushMessage("来二个", Lists.newArrayList("7"), null);
//        JPushMessgeUtil.pushAllMessage("xxxxxxxxxxx");
//        System.out.println("jpush sucess...");


        Map<String, Object> extrass = new HashMap<>(4);
        extrass.put(JPushMessgeUtil.EXTRASS_KEY_TYPE, Constant.JpushMessageType.GRAB_SEND_ORDER);
        extrass.put(JPushMessgeUtil.EXTRASS_KEY_DATA, "我是数据....");
        extrass.put(JPushMessgeUtil.EXTRASS_KEY_MUSIC, "播放文本..");
        JPushMessgeUtil.pushMessage("title","您有baibai..", Lists.newArrayList("28"), extrass);

    }
}
