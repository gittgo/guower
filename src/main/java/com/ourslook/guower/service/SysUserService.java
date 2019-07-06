package com.ourslook.guower.service;

import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.entity.common.TbUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 */
public interface SysUserService {
	
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
	 * 根据手机号查询
	 */
	SysUserEntity queryByMoble(String moble);
	
	/**
	 * 根据用户ID，查询用户
	 * @param userId
	 * @return
	 */
	SysUserEntity queryObject(Long userId);
	
	/**
	 * 查询用户列表
	 */
	List<SysUserEntity> queryList(Map<String, Object> map);

	/**
	 * 查询所有的用户
	 */
	List<SysUserEntity> queryList();
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存用户
	 */
	void save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);
	
	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	int updatePassword(Long userId, String password, String newPassword);

	/**
	 * 是否是超级管理员;用来处理数据权限用
	 * @param userId
	 * @return
	 */
	boolean isSuperAdmin(Long userId);

	/**
	 * @see SysUserEntity
	 * @see TbUserEntity
	 * 根据后台用户id获取关联的前台用户id; 即根据 SysUserEntity 的 id 查询 TbUserEntity 的用户id
	 * @param adminUserId
	 * @return
	 */
	Long getTbUserId(Long adminUserId);

	/**
	 * 检查角色是否越权
	 */
	void checkRole(SysUserEntity createUser, SysUserEntity user);
}
