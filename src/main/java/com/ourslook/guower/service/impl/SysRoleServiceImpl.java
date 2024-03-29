package com.ourslook.guower.service.impl;

import com.google.common.collect.Sets;
import com.ourslook.guower.config.DruidConfig;
import com.ourslook.guower.dao.SysRoleDao;
import com.ourslook.guower.entity.SysRoleEntity;
import com.ourslook.guower.service.SysRoleMenuService;
import com.ourslook.guower.service.SysRoleService;
import com.ourslook.guower.service.SysUserRoleService;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 角色
 * 
 */
@SuppressWarnings("all")
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysUserService sysUserService;
	@Resource
	private BeanMapper beanMapper;
	@Value("${spring.datasource.dbType:#{null}}")
	private String dbType;


	@Override
	public SysRoleEntity queryObject(Long roleId) {
		return sysRoleDao.queryObject(roleId);
	}

	@Override
	public List<SysRoleEntity> queryList(Map<String, Object> map) {
		return sysRoleDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysRoleDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		sysRoleDao.save(role);
		
		//检查权限是否越权
		checkPrems(role);
		
		//保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
	}

	@Override
	@Transactional
	public void update(SysRoleEntity role) {
		sysRoleDao.update(role);
		
		//检查权限是否越权
		checkPrems(role);
		
		//更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] roleIds) {
		if (DruidConfig.DBTYPE_ORACLE.equalsIgnoreCase(dbType)) {
			sysRoleDao.deleteBatch(roleIds);
			sysRoleDao.deleteBatch2(roleIds);
			sysRoleDao.deleteBatch3(roleIds);
		} else {
			if (XaUtils.isNotEmpty(roleIds) && Sets.newHashSet(roleIds).contains(1L)) {
				throw new RRException("超级管理员不能删除！");
			}
			sysRoleDao.deleteBatch(roleIds);
		}
	}
	
	@Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return sysRoleDao.queryRoleIdList(createUserId);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role){
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if(role.getCreateUserId() == Constant.SYS_ROLO_ID_SUPER_ADMIN){
			return ;
		}
		
		//查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
		
		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new RRException("新增角色的权限，已超出你的权限范围");
		}
	}
}
