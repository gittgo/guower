package com.ourslook.guower.service;

import com.ourslook.guower.entity.SysMenuEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 菜单管理
 */
public interface SysMenuService {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 根据父id获取下面所有的菜单 （递归）闫
	 * @return
	 */

	List<Long> getChildList(Long parentId);

	/**
	 * 用户所有菜单中获取目录
	 * */
	 List<SysMenuEntity> getCatalogList(List<SysMenuEntity> menuList);


	/**
	 * 获取用户权限列表
	 */
	Set<String> getUserPermissions(long userId);
	
	
	/**
	 * 查询菜单
	 */
	SysMenuEntity queryObject(Long menuId);
	
	/**
	 * 查询菜单列表
	 */
	List<SysMenuEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存菜单
	 */
	void save(SysMenuEntity menu);
	
	/**
	 * 修改
	 */
	void update(SysMenuEntity menu);
	
	/**
	 * 删除
	 */
	void deleteBatch(Long[] menuIds);

	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);
}
