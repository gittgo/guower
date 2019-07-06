package com.ourslook.guower.service.impl.common;

import com.ourslook.guower.dao.common.InfClauseInfoDao;
import com.ourslook.guower.entity.common.InfClauseInfoEntity;
import com.ourslook.guower.service.common.InfClauseInfoService;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



@Service("tInfClauseInfoService")
public class InfClauseInfoServiceImpl implements InfClauseInfoService {
	@Autowired
	private InfClauseInfoDao tInfClauseInfoDao;
	@Resource
	private BeanMapper beanMapper;

	@Override
	public InfClauseInfoEntity queryObject(Long clauseId){
		return tInfClauseInfoDao.queryObject(clauseId);
	}

	@Override
	public List<InfClauseInfoEntity> queryList(Map<String, Object> map){
		return tInfClauseInfoDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return tInfClauseInfoDao.queryTotal(map);
	}


	@Override
	public void save(InfClauseInfoEntity tInfClauseInfo){
		tInfClauseInfoDao.save(tInfClauseInfo);
	}

	@Override
	public void update(InfClauseInfoEntity tInfClauseInfo){
		InfClauseInfoEntity dest = new InfClauseInfoEntity();
		if (tInfClauseInfo.getClauseId() != null) {
			dest = queryObject(tInfClauseInfo.getClauseId());
			beanMapper.copy(tInfClauseInfo, dest);
		}
		tInfClauseInfoDao.update(dest);
	}

	@Override
	public void delete(Long clauseId){
		tInfClauseInfoDao.delete(clauseId);
	}

	@Override
	public void deleteBatch(Long[] clauseIds){
		tInfClauseInfoDao.deleteBatch(clauseIds);
	}

}
