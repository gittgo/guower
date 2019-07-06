package com.ourslook.guower.dao;

import com.ourslook.guower.entity.SysUserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 */
public interface SysUserDao extends BaseDao<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 根据手机号，查询系统用户
	 */
	SysUserEntity queryByMobile(String mobile);
	
	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);
	/**
	 * 
	    * @Title: deleteBatch1
	    * @Description: 删除用户关联的角色
	    * @param @param id
	    * @param @return    参数
	    * @return int    返回类型
	    * @throws
	 */
	int deleteBatch1(Object[] id);

	/**
	 * 查询所有的用户
	 */
	List<SysUserEntity> queryAllList();

	/**
	 * 根据token，查询系统用户
	 */
	SysUserEntity queryToken(String token);


}
