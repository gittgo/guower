package com.ourslook.guower.service.common;

import com.ourslook.guower.entity.common.InfVersionInfoEntity;
import com.ourslook.guower.vo.InfVersionInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 版本更新
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-01-02 13:21:28
 */
public interface InfVersionInfoService {
	
	InfVersionInfoEntity queryObject(Long id);
	InfVersionInfoEntity queryNewObject(String device);
	List<InfVersionInfoEntity> queryList(Map<String, Object> map);
	List<InfVersionInfoVo> queryVoList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InfVersionInfoEntity infVersionInfo);
	
	void update(InfVersionInfoEntity infVersionInfo);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
