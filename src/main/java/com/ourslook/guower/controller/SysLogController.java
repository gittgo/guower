package com.ourslook.guower.controller;

import com.ourslook.guower.entity.SysLogEntity;
import com.ourslook.guower.service.SysLogService;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.result.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ourslook.guower.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 * 
 */
@CrossOrigin
@Controller
@RequestMapping("/sys/log")
public class SysLogController extends AbstractController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<SysLogEntity> sysLogList = sysLogService.queryList(query);
		int total = sysLogService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysLogList, total, query.getLimit(), query.getPage());
		
		return R.ok().putObj("page", pageUtil);
	}

	/**
	 *  查询所有的操作名称，即：operation
	 */
	@ResponseBody
	@RequestMapping("/operationList")
	@RequiresPermissions("sys:log:list")
	@SuppressWarnings("unchecked")
	public R operationList(){
		List<String> sysLogList = sysLogService.queryOperationList();
		return R.ok().setData(sysLogList);
	}
}
