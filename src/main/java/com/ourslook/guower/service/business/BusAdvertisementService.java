package com.ourslook.guower.service.business;


import com.ourslook.guower.entity.business.BusAdvertisementEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 广告表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
public interface BusAdvertisementService {

	BusAdvertisementEntity queryObject(Integer id);
	
	List<BusAdvertisementEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BusAdvertisementEntity busAdvertisement);

    void multiOperate(List<String> modelIds, Integer status);

	void update(BusAdvertisementEntity busAdvertisement);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	int queryTotalByNewsId(Integer[] newsIds);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<BusAdvertisementEntity> busAdvertisements, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;
}
