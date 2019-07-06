package com.ourslook.guower.dao;

import java.util.List;

import com.ourslook.guower.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 用户与角色对应关系
 *
 */
public interface SysUserRoleDao extends BaseDao<SysUserRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(@Param("userId") Long userId);
}
