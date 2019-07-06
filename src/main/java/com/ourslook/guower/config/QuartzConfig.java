//package com.ourslook.guower.config;
//
//import com.ourslook.guower.controller.ScheduleJobController;
//import com.ourslook.guower.controller.ScheduleJobLogController;
//import com.ourslook.guower.service.impl.ScheduleJobLogServiceImpl;
//import com.ourslook.guower.service.impl.ScheduleJobServiceImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
///**
// * 定时任务配置
// *
// * @see DruidConfig
// * <p>
// * @see ScheduleJobServiceImpl#init()
// * @see ScheduleJobLogServiceImpl
// * @see ScheduleJobLogController
// * @see ScheduleJobController
// * <p>
// * @see org.springframework.scheduling.annotation.Scheduled
// * @see com.ourslook.guower.utils.ScheduleJob
// * @see com.ourslook.guower.utils.ScheduleUtils
// * @see com.ourslook.guower.utils.ScheduleRunnable
// * <p>
// * Spring Schedule 和 Quartz
// */
//@Configuration
//public class QuartzConfig {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Bean
//    @Lazy
//    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setDataSource(dataSource);
//
//        //quartz参数
//        Properties prop = new Properties();
//        prop.put("org.quartz.scheduler.instanceName", "CfScheduler");
//        prop.put("org.quartz.scheduler.instanceId", "AUTO");
//        //线程池配置  这里线程池的数量可以配置少一点，启动效率高；生产环境前程池数量配合高一点
//        //prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
//        //prop.put("org.quartz.threadPool.threadCount", "50");
//        prop.put("org.quartz.threadPool.threadCount", "2");
//        prop.put("org.quartz.threadPool.threadPriority", "5");
//        //JobStore配置
//        //prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//        prop.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore ");
//        //集群配置
//        prop.put("org.quartz.jobStore.isClustered", "true");
//        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
//        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
//
//        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
//        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//        factory.setQuartzProperties(prop);
//
//        factory.setSchedulerName("CfScheduler");
//        // 延时启动，应用启动20秒后
//        factory.setStartupDelay(120);
//        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
//        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
//        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job
//        factory.setOverwriteExistingJobs(true);
//        //设置自动启动，默认为true;如果不启动，不会执行对应的方法
//        factory.setAutoStartup(false);
//
//        logger.info("quarts start....................");
//
//        return factory;
//    }
//}
//
