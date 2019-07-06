package com.ourslook.guower.service;

import com.ourslook.guower.entity.SysJpushRecordEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-13 18:36:41
 */
public interface SysJpushRecordService {

	SysJpushRecordEntity queryObject(Long id);
	
	List<SysJpushRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysJpushRecordEntity sysJpushRecord);

	void update(SysJpushRecordEntity sysJpushRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<SysJpushRecordEntity> sysJpushRecords, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;
}
