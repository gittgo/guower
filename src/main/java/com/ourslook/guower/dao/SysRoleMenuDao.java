package com.ourslook.guower.dao;

import com.ourslook.guower.entity.SysRoleMenuEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 */
public interface SysRoleMenuDao extends BaseDao<SysRoleMenuEntity> {
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long roleId);
	
	void saveRoleMenu(List<SysRoleMenuEntity>list);
	
}
