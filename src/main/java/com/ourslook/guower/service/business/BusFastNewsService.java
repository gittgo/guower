package com.ourslook.guower.service.business;


import com.ourslook.guower.entity.business.BusFastNewsEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 快报表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
public interface BusFastNewsService {

	BusFastNewsEntity queryObject(Integer id);
	
	List<BusFastNewsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BusFastNewsEntity busFastNews);

    void multiOperate(List<String> modelIds, Integer status);

	void update(BusFastNewsEntity busFastNews);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<BusFastNewsEntity> busFastNewss, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;
}
