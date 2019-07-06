package com.ourslook.guower.service.common;

import com.ourslook.guower.entity.common.InfDictInfoEntity;
import com.ourslook.guower.vo.InfDictVo;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
public interface InfDictInfoService {
	List<InfDictVo> queryVoList(Map<String, Object> map);
	InfDictInfoEntity queryObject(Long id);

	InfDictInfoEntity queryObjectByCode(String code);

	List<InfDictInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InfDictInfoEntity xaDictInfo);
	
	void update(InfDictInfoEntity xaDictInfo);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	/**
	 * 根据字典类的name 查询具体的字典数据
	 * @param name
	 * @return
	 */
	List<InfDictInfoEntity> getInfDictInfoListByTypeName(String name);
	/**
	 * 根据字典类的code 查询具体的字典数据
	 * @param code
	 * @return
	 */
	List<InfDictInfoEntity> getInfDictInfoListByTypeCode(String code);

}
