package com.ourslook.guower.service.score;


import com.ourslook.guower.entity.score.InfExchangeRecordEntity;
import com.ourslook.guower.vo.score.InfExchangeRecordVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 兑换记录表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
public interface InfExchangeRecordService {

	InfExchangeRecordEntity queryObject(Integer id);
	
	List<InfExchangeRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InfExchangeRecordEntity infExchangeRecord);

    void multiOperate(List<String> modelIds, Integer status);

	void update(InfExchangeRecordEntity infExchangeRecord);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	/**
	 * 兑换货币（生成兑换记录，更新用户积分，生成积分使用记录，更新货币库存）
	 * @param infExchangeRecordEntity 兑换记录实体类，请自行检查
	 */
	void exchangeCurrency(InfExchangeRecordEntity infExchangeRecordEntity);

	/**
	 * 发放货币
	 */
	void grant(Map<String, Object> map);

	List<InfExchangeRecordVo> queryVoList(Map<String, Object> map);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<InfExchangeRecordEntity> infExchangeRecords, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;

	/**
	 * 保存货币兑换逻辑
	 * @param exchangeRecordEntity
	 */
	void saveExchangeRecord(InfExchangeRecordEntity exchangeRecordEntity);
}
