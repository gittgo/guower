//package com.ourslook.guower.controller;
//
//import com.ourslook.guower.entity.ScheduleJobLogEntity;
//import com.ourslook.guower.service.ScheduleJobLogService;
//import com.ourslook.guower.utils.PageUtils;
//import com.ourslook.guower.utils.Query;
//import com.ourslook.guower.utils.result.R;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 定时任务日志
// */
//@CrossOrigin
//@RestController
//@RequestMapping("/sys/scheduleLog")
//public class ScheduleJobLogController {
//    @Autowired
//    private ScheduleJobLogService scheduleJobLogService;
//
//    /**
//     * 定时任务日志列表
//     */
//    @RequestMapping("/list")
//    @RequiresPermissions("sys:schedule:log")
//    public R list(@RequestParam Map<String, Object> params) {
//        //查询列表数据
//        Query query = new Query(params);
//        List<ScheduleJobLogEntity> jobList = scheduleJobLogService.queryList(query);
//        int total = scheduleJobLogService.queryTotal(query);
//
//        PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());
//
//        return R.ok().putObj("page", pageUtil);
//    }
//
//    /**
//     * 定时任务日志信息
//     */
//    @RequestMapping("/info/{logId}")
//    public R info(@PathVariable("logId") Long logId) {
//        ScheduleJobLogEntity log = scheduleJobLogService.queryObject(logId);
//
//        return R.ok().putObj("log", log);
//    }
//}
