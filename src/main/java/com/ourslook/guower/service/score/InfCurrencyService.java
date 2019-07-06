package com.ourslook.guower.service.score;


import com.ourslook.guower.entity.score.InfCurrencyEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 货币表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
public interface InfCurrencyService {

	InfCurrencyEntity queryObject(Integer id);
	
	List<InfCurrencyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InfCurrencyEntity infCurrency);

    void multiOperate(List<String> modelIds, Integer status);

	void update(InfCurrencyEntity infCurrency);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<InfCurrencyEntity> infCurrencys, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;
}
