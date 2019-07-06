package com.ourslook.guower.controller;

import com.ourslook.guower.entity.SysConfigEntity;
import com.ourslook.guower.service.SysConfigService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.annotation.LoggSys;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.ValidateResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 * 
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;
	@Resource
	private BeanMapper beanMapper;

	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:config:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		query.put("status_NE", Constant.Status.delete);
		List<SysConfigEntity> configList = sysConfigService.queryList(query);
		int total = sysConfigService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(configList, total, query.getLimit(), query.getPage());

		return R.ok().putObj("page", pageUtil);
	}
	
	
	/**
	 * 配置信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public R info(@PathVariable("id") String id){
		SysConfigEntity config = sysConfigService.queryObject(id);
		
		return R.ok().putObj("config", config);
	}
	
	/**
	 * 保存配置
	 */
	@LoggSys("系统配置-保存")
	@RequestMapping("/save")
	@RequiresPermissions("sys:config:save")
	public R save(@RequestBody SysConfigEntity config){
		String name = config.getKey();
		Query query = new Query();
		List<SysConfigEntity> configEntity = sysConfigService.queryList(query);
		for(SysConfigEntity s:configEntity){
			if(s.getKey().equals(name)){
				throw new RRException("参数名已存在");
			}
		}
		ValidatorUtils.validateEntity(config);

		sysConfigService.save(config);
		
		return R.ok();
	}

	/**
	 * 复制copy配置信息
	 */
	@RequestMapping("/copy/{id}")
	@RequiresPermissions("sys:config:save")
	public R copy(@PathVariable("id") String id){

		SysConfigEntity config = sysConfigService.queryObject(id);

		SysConfigEntity copy = new SysConfigEntity();
		beanMapper.copy(config, copy);
		copy.setId(null);
        copy.setCode(config.getCode()+"_1");
        copy.setKey(config.getKey()+"_1");
        copy.setCreateTime(LocalDateTime.now());
		sysConfigService.save(copy);
		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
	@LoggSys("系统配置-修改")
	@RequestMapping("/update")
	@RequiresPermissions("sys:config:update")
	public R update(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);
		config.setUpdateTime(LocalDateTime.now());
		sysConfigService.update(config);
		
		return R.ok();
	}
	
	/**
	 * 删除配置
	 * @see Constant.SysConfigValue
	 */
	@LoggSys("删除配置")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public R delete(@RequestBody String[] ids){
		//sysConfigService.deleteBatch(ids);
		//逻辑删除
		for(String id:ids){
			SysConfigEntity configEntity = sysConfigService.queryObject(id);
			configEntity.setUpdateTime(LocalDateTime.now());
			configEntity.setStatus(String.valueOf(Constant.Status.delete));
			sysConfigService.update(configEntity);
		}
		return R.ok();
	}

	/**
	 * 验证配置Cfg code 是否重复
     * 相关配置说明见
	 * @see SysRoleController#checkRoleNameExist(HttpServletRequest)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkCfgCode",method=RequestMethod.POST)
	public ValidateResult checkCfgCodeExist(HttpServletRequest request)  {
		String cfgCode = UrlEncode.getUrlParams(request).get("code") + "".trim();

		Query query = new Query();
		query.put("code", cfgCode);
		List<SysConfigEntity> entities = sysConfigService.queryList(query);
		if (XaUtils.isEmpty(entities)) {
			return ValidateResult.ok();
		} else {
			return ValidateResult.error();
		}
	}

	/**
	 * 验证配置Cfg key 是否重复
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkCfgKey",method=RequestMethod.POST)
	public ValidateResult checkCfgKeyExist(HttpServletRequest request)  {
		String cfgKey = UrlEncode.getUrlParams(request).get("key") + "".trim();

		Query query = new Query();
		query.put("key", cfgKey);
		List<SysConfigEntity> entities = sysConfigService.queryList(query);
		if (XaUtils.isEmpty(entities)) {
			return ValidateResult.ok();
		} else {
			return ValidateResult.error();
		}
	}

}
