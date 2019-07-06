package com.ourslook.guower.api;

import com.ourslook.guower.dao.score.InfScoreDayMaxDao;
import com.ourslook.guower.dao.score.InfScoreUsedDao;
import com.ourslook.guower.entity.score.InfScoreDayMaxEntity;
import com.ourslook.guower.entity.score.InfScoreUsedEntity;
import com.ourslook.guower.utils.spring.SpringContextHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 这里是放置业务相关的工具类，不具备复用性，只有单个项目有关，和业务耦合放到这里
 */
public class BusUtils {

    /**
     * 生成新闻初始阅读量30000~40000
     * @return
     */
    public static Integer getNewsLookTimes(){
        int max=40000;
        int min=30000;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    /**
     * 获取奖励积分
     * @param userId        用户id
     * @param obtainType    获取途径
     * @return      最终奖励积分
     */
    public static Integer getPlusScore(Integer userId,Integer obtainType){
        InfScoreUsedDao infScoreUsedDao = SpringContextHelper.getBean(InfScoreUsedDao.class);
        InfScoreDayMaxDao infScoreDayMaxDao = SpringContextHelper.getBean(InfScoreDayMaxDao.class);
        Integer resultScore;//最终获得量
        Integer todayMaxScore = 0;//今日积分上限
        Integer score = 0;//单次积分获取量
        Integer todayScore = 0;//今日已获取积分
        Map queryScoreMap = new HashMap<>();
        queryScoreMap.put("getType", obtainType + "");
        List<InfScoreDayMaxEntity> scoreList = infScoreDayMaxDao.queryList(queryScoreMap);
        if(scoreList != null && scoreList.size() > 0){
            score = scoreList.get(0).getGetNumber();
            todayMaxScore = scoreList.get(0).getGetMax();
        }
        Map queryScoreUsedMap = new HashMap<>();
        queryScoreUsedMap.put("userId", userId + "");
        queryScoreUsedMap.put("changeType", obtainType + "");
        queryScoreUsedMap.put("TODAY", "today");
        List<InfScoreUsedEntity> scoreUsedList = infScoreUsedDao.queryList(queryScoreUsedMap);
        if(scoreUsedList != null && scoreUsedList.size() > 0){
            for (InfScoreUsedEntity scoreUsed : scoreUsedList){
                todayScore += scoreUsed.getScoreChange()==null||scoreUsed.getScoreChange()<0?0:scoreUsed.getScoreChange();
            }
        }
        //判断最终奖励积分
        //如果将超过上限，则结果为距离上限的差值
        if(todayScore + score > todayMaxScore){
            resultScore = todayMaxScore - todayScore;
        }else{
            resultScore = score;
        }
        return resultScore;
    }

}
