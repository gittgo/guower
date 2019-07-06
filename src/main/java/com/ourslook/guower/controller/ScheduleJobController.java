//package com.ourslook.guower.controller;
//
//import com.ourslook.guower.entity.ScheduleJobEntity;
//import com.ourslook.guower.service.ScheduleJobService;
//import com.ourslook.guower.utils.PageUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import com.ourslook.guower.utils.result.R;
//
//import com.ourslook.guower.utils.Query;
//import com.ourslook.guower.utils.annotation.LoggSys;
//import com.ourslook.guower.utils.validator.ValidatorUtils;
//import org.testng.collections.Lists;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 定时任务
// *
// * @author dazer
// */
//@CrossOrigin
//@RestController
//@RequestMapping("/sys/schedule")
//public class ScheduleJobController {
//	@Autowired
//	private ScheduleJobService scheduleJobService;
//
//	/**
//	 * 定时任务列表
//	 */
//	@RequestMapping("/list")
//	@RequiresPermissions("sys:schedule:list")
//	public R list(@RequestParam Map<String, Object> params){
//		//查询列表数据
//		Query query = new Query(params);
//		//这里暂时不显示两个测试的账号
//		query.put("jobId_NOT_IN", Lists.newArrayList(1,2,3,4, 5));
//		List<ScheduleJobEntity> jobList = scheduleJobService.queryList(query);
//		int total = scheduleJobService.queryTotal(query);
//		PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());
//		return R.ok().putObj("page", pageUtil);
//	}
//
//	/**
//	 * 定时任务信息
//	 */
//	@RequestMapping("/info/{jobId}")
//	@RequiresPermissions("sys:schedule:info")
//	public R info(@PathVariable("jobId") Long jobId){
//		ScheduleJobEntity schedule = scheduleJobService.queryObject(jobId);
//		return R.ok().putObj("schedule", schedule);
//	}
//
//	/**
//	 * 保存定时任务
//	 */
//	@LoggSys("保存定时任务")
//	@RequestMapping("/save")
//	@RequiresPermissions("sys:schedule:save")
//	public R save(@RequestBody ScheduleJobEntity scheduleJob){
//		ValidatorUtils.validateEntity(scheduleJob);
//		scheduleJobService.save(scheduleJob);
//		return R.ok();
//	}
//
//	/**
//	 * 修改定时任务
//	 */
//	@LoggSys("修改定时任务")
//	@RequestMapping("/update")
//	@RequiresPermissions("sys:schedule:update")
//	public R update(@RequestBody ScheduleJobEntity scheduleJob){
//		ValidatorUtils.validateEntity(scheduleJob);
//		scheduleJobService.update(scheduleJob);
//		return R.ok();
//	}
//
//	/**
//	 * 删除定时任务
//	 */
//	@LoggSys("删除定时任务")
//	@RequestMapping("/delete")
//	@RequiresPermissions("sys:schedule:delete")
//	public R delete(@RequestBody Long[] jobIds){
//		scheduleJobService.deleteBatch(jobIds);
//		return R.ok();
//	}
//
//	/**
//	 * 立即执行任务
//	 */
//	@LoggSys("立即执行任务")
//	@RequestMapping("/run")
//	@RequiresPermissions("sys:schedule:run")
//	public R run(@RequestBody Long[] jobIds){
//		scheduleJobService.run(jobIds);
//		return R.ok();
//	}
//
//	/**
//	 * 暂停定时任务
//	 */
//	@LoggSys("暂停定时任务")
//	@RequestMapping("/pause")
//	@RequiresPermissions("sys:schedule:pause")
//	public R pause(@RequestBody Long[] jobIds){
//		scheduleJobService.pause(jobIds);
//		return R.ok();
//	}
//
//	/**
//	 * 恢复定时任务
//	 */
//	@LoggSys("恢复定时任务")
//	@RequestMapping("/resume")
//	@RequiresPermissions("sys:schedule:resume")
//	public R resume(@RequestBody Long[] jobIds){
//		scheduleJobService.resume(jobIds);
//		return R.ok();
//	}
//
//	/**
//	 * cron含义
//	 */
//	@LoggSys("cron含义")
//	@RequestMapping("/cronNotes/{jobId}")
//	@RequiresPermissions("sys:schedule:cronNotes")
//	public R cronNotes(@PathVariable("jobId") Long jobId){
//		String jsonResult = scheduleJobService.cronNotes(jobId);
//		return R.ok().putObj("cornNote", jsonResult);
//	}
//
//}
