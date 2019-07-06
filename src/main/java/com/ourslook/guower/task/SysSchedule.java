package com.ourslook.guower.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dazer
 * eg:
 * @Scheduled(cron = "0 0 10 * * ?")    //每天的10点触发
 * @Scheduled(cron = "0 0/10 * * * ?") //每10min
 */
@Component("sysSchedule")
public class SysSchedule {
    private Logger logger = LoggerFactory.getLogger(getClass());


//    /**
//     * 定时任务
//     *
//     */
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void orderAutoConfrimRecive() {
//
//    }

}
