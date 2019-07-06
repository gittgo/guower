//package com.ourslook.guower.service.impl;
//
//import com.ourslook.guower.dao.ScheduleJobDao;
//import com.ourslook.guower.entity.ScheduleJobEntity;
//import com.ourslook.guower.service.ScheduleJobService;
//import com.ourslook.guower.utils.Constant.ScheduleStatus;
//import com.ourslook.guower.utils.Query;
//import com.ourslook.guower.utils.ScheduleUtils;
//import com.ourslook.guower.utils.http.HttpClientSimpleUtil;
//import com.ourslook.guower.utils.validator.AbstractAssert;
//import org.quartz.CronTrigger;
//import org.quartz.Scheduler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service("scheduleJobService")
//public class ScheduleJobServiceImpl implements ScheduleJobService {
//    @Autowired
//    private Scheduler scheduler;
//    @Autowired
//    private ScheduleJobDao schedulerJobDao;
//
//    /*** 项目启动时，初始化定时器*/
//    @PostConstruct
//    public void init() {
//        /**
//         * 这里也可以全部注释掉，不影响使用
//         */
//        if (1 == 1) {
//            return;
//        }
//        List<ScheduleJobEntity> scheduleJobList = schedulerJobDao.queryList(new HashMap<>());
//        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
//            try {
//                CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
//                //如果不存在，则创建
//                if (cronTrigger == null) {
//                    ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
//                } else {
//                    ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public ScheduleJobEntity queryObject(Long jobId) {
//        return schedulerJobDao.queryObject(jobId);
//    }
//
//    @Override
//    public List<ScheduleJobEntity> queryList(Map<String, Object> map) {
//        return schedulerJobDao.queryList(map);
//    }
//
//    @Override
//    public int queryTotal(Map<String, Object> map) {
//        return schedulerJobDao.queryTotal(map);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void save(ScheduleJobEntity scheduleJob) {
//
//        Query query = new Query();
//        query.put("beanName", scheduleJob.getBeanName());
//        query.put("methodName", scheduleJob.getMethodName());
//        int count = schedulerJobDao.queryTotal(query);
//        AbstractAssert.isOk(count == 0, "请修改beanName名称、methodName名称！");
//
//        scheduleJob.setCreateTime(new Date());
//        scheduleJob.setStatus(ScheduleStatus.NORMAL.getValue());
//        schedulerJobDao.save(scheduleJob);
//
//        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void update(ScheduleJobEntity scheduleJob) {
//
//        Query query = new Query();
//        query.put("beanName", scheduleJob.getBeanName());
//        query.put("methodName", scheduleJob.getMethodName());
//        int count = schedulerJobDao.queryTotal(query);
//        AbstractAssert.isOk(count <= 1, "请修改beanName名称、methodName名称！");
//
//        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
//
//        schedulerJobDao.update(scheduleJob);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteBatch(Long[] jobIds) {
//        for (Long jobId : jobIds) {
//            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
//        }
//
//        //删除数据
//        schedulerJobDao.deleteBatch(jobIds);
//    }
//
//    @Override
//    public int updateBatch(Long[] jobIds, int status) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("list", jobIds);
//        map.put("status", status);
//        return schedulerJobDao.updateBatch(map);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void run(Long[] jobIds) {
//        for (Long jobId : jobIds) {
//            ScheduleJobEntity scheduleJobEntity = queryObject(jobId);
//            ScheduleUtils.run(scheduler, scheduleJobEntity);
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void pause(Long[] jobIds) {
//        for (Long jobId : jobIds) {
//            ScheduleUtils.pauseJob(scheduler, jobId);
//        }
//
//        updateBatch(jobIds, ScheduleStatus.PAUSE.getValue());
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void resume(Long[] jobIds) {
//        for (Long jobId : jobIds) {
//            ScheduleUtils.resumeJob(scheduler, jobId);
//        }
//
//        updateBatch(jobIds, ScheduleStatus.NORMAL.getValue());
//    }
//
//    @Override
//    public String cronNotes(Long jobId) {
//        ScheduleJobEntity jobEntity = queryObject(jobId);
//        if (jobEntity != null) {
//            String cronExpression = jobEntity.getCronExpression();
//            try {
//                cronExpression = URLEncoder.encode(cronExpression, "UTF-8");
//                //查询Cron表达式中文含义的网址查询Cron表达式中文含义的网址
//                String coroQueryUrl = "https://cronexpressiondescriptor.azurewebsites.net/api/descriptor/?locale=zh-CN&expression=" + cronExpression;
//                return HttpClientSimpleUtil.getInstance().doGetRequest(coroQueryUrl);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//}
