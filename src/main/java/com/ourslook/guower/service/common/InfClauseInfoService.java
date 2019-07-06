package com.ourslook.guower.service.common;


import com.ourslook.guower.entity.common.InfClauseInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 文案维护(协议维护、关于我们等)
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-06 09:45:08
 */
public interface InfClauseInfoService {

	InfClauseInfoEntity queryObject(Long clauseId);

	List<InfClauseInfoEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(InfClauseInfoEntity tInfClauseInfo);

	void update(InfClauseInfoEntity tInfClauseInfo);

	void delete(Long clauseId);

	void deleteBatch(Long[] clauseIds);
}
