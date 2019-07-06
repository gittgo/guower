package com.ourslook.guower.dao;

import com.ourslook.guower.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 系统配置信息
 *
 */
public interface SysConfigDao extends BaseDao<SysConfigEntity> {
	
	/**
	 * 根据key，查询value
	 */
	String queryByKey(String paramKey);
	
	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);


	/**
	 * 根据code，查询value
	 */
	String queryValueByCode(String code);

	/**
	 * 根据code，查询对象
	 */
	SysConfigEntity queryObjectByCode(String code);


}
