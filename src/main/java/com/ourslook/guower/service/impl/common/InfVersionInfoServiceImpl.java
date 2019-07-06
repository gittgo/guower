package com.ourslook.guower.service.impl.common;

import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.vo.InfVersionInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


import com.ourslook.guower.dao.common.InfVersionInfoDao;
import com.ourslook.guower.entity.common.InfVersionInfoEntity;
import com.ourslook.guower.service.common.InfVersionInfoService;



@Service("infVersionInfoService")
public class InfVersionInfoServiceImpl implements InfVersionInfoService {
	@Autowired
	private InfVersionInfoDao infVersionInfoDao;
    @Resource
    private BeanMapper beanMapper;
	
	@Override
	public InfVersionInfoEntity queryObject(Long id){
		return infVersionInfoDao.queryObject(id);
	}
	@Override
	public InfVersionInfoEntity queryNewObject(String device){
		return infVersionInfoDao.queryNewObject(device);
	}
	@Override
	public List<InfVersionInfoEntity> queryList(Map<String, Object> map){
		return infVersionInfoDao.queryList(map);
	}
	@Override
	public List<InfVersionInfoVo> queryVoList(Map<String, Object> map){
		return infVersionInfoDao.queryVoList(map);
	};
	@Override
	public int queryTotal(Map<String, Object> map){
		return infVersionInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(InfVersionInfoEntity infVersionInfo){
		infVersionInfoDao.save(infVersionInfo);
	}
	
	@Override
	public void update(InfVersionInfoEntity infVersionInfo){
		InfVersionInfoEntity  dest = new InfVersionInfoEntity();
        if (infVersionInfo.getId() != null) {
            dest = queryObject(infVersionInfo.getId());
            beanMapper.copy(infVersionInfo, dest);
        }
		infVersionInfoDao.update(dest);
	}
	
	@Override
	public void delete(Long id){
		infVersionInfoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		infVersionInfoDao.deleteBatch(ids);
	}
	
}
