package com.ourslook.guower.service.score;


import com.ourslook.guower.entity.score.InfScoreDayMaxEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 单日积分获取上限表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
public interface InfScoreDayMaxService {

	InfScoreDayMaxEntity queryObject(Integer id);
	
	List<InfScoreDayMaxEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InfScoreDayMaxEntity infScoreDayMax);

    void multiOperate(List<String> modelIds, Integer status);

	void update(InfScoreDayMaxEntity infScoreDayMax);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<InfScoreDayMaxEntity> infScoreDayMaxs, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;
}
