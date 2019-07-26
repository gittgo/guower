package com.ourslook.guower.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ourslook.guower.entity.business.BusFastNewsEntity;
import com.ourslook.guower.service.business.BusFastNewsService;
import com.ourslook.guower.utils.encryptdir.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    private BusFastNewsService busFastNewsService;

    //3.添加定时任务
//    @Scheduled(cron = "*/5 * * * * ?")  // 5秒
    @Scheduled(cron = "0 */1 * * * ?")   // 1分
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        // 获取鸵鸟区块链数据
        String outString =  HttpConnectUtil.doPost();
        JSONObject pa= JSONObject.parseObject(outString);
        JSONArray array = pa.getJSONArray("result_list");
//        System.out.println("------_________________--------------------");
        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);
            newsflash newsflash = JSON.toJavaObject(jo,newsflash.class);
            // 查询是否已经加载过了
            Map map = new HashMap();
            map.put("tuoniaoId",newsflash.getId());
            map.put("outWeb","1");
            List<BusFastNewsEntity> busFastNewsEntities = busFastNewsService.queryList(map);
            if(busFastNewsEntities.size()>0){
//                System.out.println("已经存在跳过！");
                continue;
            }
            // 整理数据
            //  创建快报
            BusFastNewsEntity busFastNewsEntity = new BusFastNewsEntity();
            //
            busFastNewsEntity.setTitle(newsflash.getTitle().replace("鸵鸟区块链",""));
            busFastNewsEntity.setTitle(busFastNewsEntity.getTitle().replace("鸵鸟",""));
            //
            busFastNewsEntity.setMainText(newsflash.getContent().replace("鸵鸟区块链",""));
            busFastNewsEntity.setMainText(busFastNewsEntity.getMainText().replace("鸵鸟",""));
            //
            busFastNewsEntity.setReleaseUserId(1);
            busFastNewsEntity.setReleaseUserName("superAdmin");
            busFastNewsEntity.setReleaseDate(LocalDateTime.now());
            busFastNewsEntity.setGood(newsflash.getBull()+(int)((Math.random()*9+1)*1000));
            busFastNewsEntity.setBad(newsflash.getBear()+(int)(Math.random()*900)+100);
            busFastNewsEntity.setGuowerIndex(5);
            busFastNewsEntity.setIsNewsFlash(1);
            busFastNewsEntity.setLookTimes(5883);
            busFastNewsEntity.setOutWeb("1");  // 1代表鸵鸟区块链
            busFastNewsEntity.setTuoniaoId(newsflash.getId());
            if(null == newsflash.getTitle() || "".equals(newsflash.getTitle()) || null == newsflash.getContent() || "".equals(newsflash.getContent()) ){
//                System.out.println("标题或内容为空！");
                continue;
            }
            busFastNewsService.save(busFastNewsEntity);
        }
//        System.out.println("--------------------------");
//        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }


    //3.添加定时任务
//    @Scheduled(cron = "*/5 * * * * ?")  // 5秒
    @Scheduled(cron = "0 */1 * * * ?")   // 1分
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureBishijie() {
        // 获取币世界数据
        String timestamp =  (new Date()).getTime()/1000+"";
        String MD5sign = Md5Util.getMD5String("app_id=8495d32b1165b464&page=1&size=20&timestamp="+timestamp+"d64d8e9a0ed6a622c0d6c24806668709");
        String MD5sign2 = Md5Util.getMd5("app_id=8495d32b1165b464&page=1&size=20&timestamp="+timestamp+"d64d8e9a0ed6a622c0d6c24806668709");
        String outString =  HttpConnectUtil.getHttp("https://iapi.bishijie.com/newsflash/list?app_id=8495d32b1165b464&page=1&size=20&timestamp="+timestamp+"&sign="+MD5sign);
        JSONObject pa= JSONObject.parseObject(outString);
        JSONArray array = pa.getJSONArray("items");



        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);
            BiShiJieNewFlash biShiJieNewFlash = JSON.toJavaObject(jo,BiShiJieNewFlash.class);
            // 查询是否已经加载过了
            Map map = new HashMap();
            map.put("tuoniaoId",biShiJieNewFlash.getId());
            map.put("outWeb","2");
            List<BusFastNewsEntity> busFastNewsEntities = busFastNewsService.queryList(map);
            if(busFastNewsEntities.size()>0){
//                System.out.println("已经存在跳过！");
                continue;
            }
            // 整理数据
            //  创建快报
            BusFastNewsEntity busFastNewsEntity = new BusFastNewsEntity();
            //
            busFastNewsEntity.setTitle(biShiJieNewFlash.getTitle());
            //
            busFastNewsEntity.setMainText(biShiJieNewFlash.getContent());
            //
            busFastNewsEntity.setReleaseUserId(1);
            busFastNewsEntity.setReleaseUserName("superAdmin");
            busFastNewsEntity.setReleaseDate(LocalDateTime.now());
            busFastNewsEntity.setGood((int)((Math.random()*9+1)*1000));
            busFastNewsEntity.setBad((int)(Math.random()*900)+100);
            busFastNewsEntity.setGuowerIndex(5);
            busFastNewsEntity.setIsNewsFlash(1);
            busFastNewsEntity.setLookTimes(5883);
            busFastNewsEntity.setOutWeb("2");  // 2 代表币世界
            busFastNewsEntity.setTuoniaoId(biShiJieNewFlash.getId());
            if(null == busFastNewsEntity.getTitle() || "".equals(busFastNewsEntity.getTitle()) || null == busFastNewsEntity.getMainText() || "".equals(busFastNewsEntity.getMainText()) ){
//                System.out.println("标题或内容为空！");
                continue;
            }
            busFastNewsService.save(busFastNewsEntity);
        }
        System.out.println("--------------------------");
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }



}
