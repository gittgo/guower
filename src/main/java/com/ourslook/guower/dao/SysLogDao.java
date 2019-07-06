package com.ourslook.guower.dao;

import com.ourslook.guower.entity.SysLogEntity;

import java.util.List;

/**
 * 系统日志
 *
 */
public interface SysLogDao extends BaseDao<SysLogEntity> {
    List<String> queryOperationList();
}
