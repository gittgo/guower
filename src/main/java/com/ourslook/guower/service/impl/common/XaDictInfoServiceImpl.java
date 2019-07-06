package com.ourslook.guower.service.impl.common;

import com.ourslook.guower.dao.common.InfDictInfoDao;
import com.ourslook.guower.entity.common.InfDictInfoEntity;
import com.ourslook.guower.service.common.InfDictInfoService;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.vo.InfDictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("xaDictInfoService")
public class XaDictInfoServiceImpl implements InfDictInfoService {

	@Autowired
	private InfDictInfoDao xaDictInfoDao;

	@Override
	public InfDictInfoEntity queryObject(Long id){
		return xaDictInfoDao.queryObject(id);
	}

	@Override
	public InfDictInfoEntity queryObjectByCode(String code) {
		Query query = new Query();
		query.put("code", code);
		List<InfDictInfoEntity> list = xaDictInfoDao.queryList(query);
		if (XaUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<InfDictInfoEntity> queryList(Map<String, Object> map){
		return xaDictInfoDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return xaDictInfoDao.queryTotal(map);
	}

	@Override
	public void save(InfDictInfoEntity xaDictInfo){
		xaDictInfoDao.save(xaDictInfo);
	}

	@Override
	public void update(InfDictInfoEntity xaDictInfo){
		xaDictInfoDao.update(xaDictInfo);
	}

	@Override
	public void delete(Long id){
		xaDictInfoDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids){
		xaDictInfoDao.deleteBatch(ids);
	}

	@Override
	public List<InfDictInfoEntity> getInfDictInfoListByTypeName(String name) {
		return xaDictInfoDao.getInfDictInfoListByTypeName(name);
	}

	@Override
	public List<InfDictInfoEntity> getInfDictInfoListByTypeCode(String code) {
		return xaDictInfoDao.getInfDictInfoListByTypeCode(code);
	}

	@Override
	public List<InfDictVo> queryVoList(Map<String, Object> map){
		return xaDictInfoDao.queryVoList(map);
	}
	
}
