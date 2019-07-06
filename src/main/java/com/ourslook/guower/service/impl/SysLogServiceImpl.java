package com.ourslook.guower.service.impl;

import com.ourslook.guower.dao.SysLogDao;
import com.ourslook.guower.entity.SysLogEntity;
import com.ourslook.guower.service.SysLogService;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;
	@Resource
	private BeanMapper beanMapper;
	
	@Override
	public SysLogEntity queryObject(Long id){
		return sysLogDao.queryObject(id);
	}
	
	@Override
	public List<SysLogEntity> queryList(Map<String, Object> map){
		return sysLogDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return sysLogDao.queryTotal(map);
	}
	
	@Override
	public void save(SysLogEntity sysLog){
		sysLogDao.save(sysLog);
	}
	
	@Override
	public void update(SysLogEntity sysLog){
		SysLogEntity dest = new SysLogEntity();
		if (sysLog.getId() != null) {
			dest = queryObject(NumberUtils.toLong(sysLog.getId(),0));
			beanMapper.copy(sysLog, dest);
		}
		sysLogDao.update(dest);
	}
	
	@Override
	public void delete(Long id){
		sysLogDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysLogDao.deleteBatch(ids);
	}

	@Override
	public List<String> queryOperationList() {
		return sysLogDao.queryOperationList();
	}
}
