package com.ourslook.guower.dao;

import com.ourslook.guower.entity.ScheduleJobEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 定时任务
 */
@Component
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {
	
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
