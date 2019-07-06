package com.ourslook.guower.controller;

import com.google.common.collect.Lists;
import com.ourslook.guower.entity.SysRoleEntity;
import com.ourslook.guower.service.SysRoleMenuService;
import com.ourslook.guower.service.SysRoleService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.annotation.LoggSys;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.ValidateResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author dazer
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam Map<String, Object> params){
		//如果不是超级管理员，则只查询自己创建的角色列表
		/*if(getUserId() != Constant.SYS_ROLO_ID_SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}*/
		
		//查询列表数据
		Query query = new Query(params);
		List<SysRoleEntity> list = sysRoleService.queryList(query);
		int total = sysRoleService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		
		return R.ok().putObj("page", pageUtil);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(){
		Map<String, Object> map = new HashMap<>();
		
		//如果不是超级管理员，则只查询自己所拥有的角色列表
		/*if(getUserId() != Constant.SYS_ROLO_ID_SUPER_ADMIN){
			map.put("createUserId", getUserId());
		}*/
		List<SysRoleEntity> list = sysRoleService.queryList(map);
		
		return R.ok().putObj("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.queryObject(roleId);
		
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);
		
		return R.ok().putObj("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@LoggSys("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		role.setCreateUserId(getUserId());
		sysRoleService.save(role);
		
		return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@LoggSys("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		role.setCreateUserId(getUserId());
		sysRoleService.update(role);
		return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@LoggSys("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] roleIds){
		if (ArrayUtils.contains(roleIds, Constant.SYS_ROLO_ID_SUPER_ADMIN)) {
			return R.error("不能删除超级管理员角色");
		}
		if (ArrayUtils.contains(roleIds, Constant.SYS_ROLO_ID_SUPER_ADMINID)) {
			return R.error("不能平台管理员角色");
		}
		if (Lists.newArrayList(roleIds).contains(Constant.SYS_ROLO_ID_SUPER_ADMIN)) {
			throw new RRException("不能删除超级管理员角色!");
		}
		sysRoleService.deleteBatch(roleIds);
		return R.ok();
	}

	/**
	 * 校验角色是否重复
	 * @param roleName 必选，角色名称
	 * @param roleId   可选，角色id
	 * @return
	 */
	/**
	 * 客户端 远程校验 https://niceue.com/validator/demo/remote.php
	 * 中间必须有空格
	 * remote[post:sys/role/checkRole, roleId]
	 * remote[check/username.php, field1, field2]
	 * 自己拼接的字符串，ajax处理json要处理，否则是字符串，而不是json对象；
	 * <code>
	 if (typeof data === 'string') {
	 data = JSON.parseKeyUuid(data);
	 }
	 * </code>
     *
     * RequestParam 用来处理Content-Type: 为 application/x-www-form-urlencoded
     * RequestBody  用来处理Content-Type: 为 application/json
     *
     * 此接口使用  application/json
     *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkRole",method=RequestMethod.POST)
	public ValidateResult checkRoleNameExist(HttpServletRequest request)  {
        /**
         * @RequestParam 只能用-Content-Type: 为 application/x-www-form-urlencoded，数据只能request.getParameter()
         * @RequestBody  只能用-Content-Type: 为 application/json,数据只能request.getInputStream转成字符串，在转成对应的类型
         */
		Map params = UrlEncode.getUrlParams(request);
	    String roleName = params.get("roleName") + "".trim();
		boolean roleIsAdd = Boolean.parseBoolean(params.get("roleIsAdd") + "");
		String roleNameOldValue = params.get("roleNameOldValue") + "";

		if (roleIsAdd) {
			Query query = new Query();
			query.put("roleName_EQ", roleName);
			List<SysRoleEntity> entities = sysRoleService.queryList(query);
			if (XaUtils.isEmpty(entities)) {
				return ValidateResult.ok();
			} else {
				return ValidateResult.error();
			}
		} else {
			if (roleNameOldValue.equals(roleName)) {
				return ValidateResult.ok();
			} else {
				Query query = new Query();
				query.put("roleName", roleName);
				List<SysRoleEntity> entities = sysRoleService.queryList(query);
				if (XaUtils.isEmpty(entities)) {
					return ValidateResult.ok();
				} else {
					return ValidateResult.error();
				}
			}
		}
	}
}
