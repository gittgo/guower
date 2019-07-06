package com.ourslook.guower.service.impl;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.dao.SysConfigDao;
import com.ourslook.guower.entity.SysConfigEntity;
import com.ourslook.guower.service.SysConfigService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author dazer
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

	@Autowired
	private SysConfigDao sysConfigDao;
	@Resource
	private BeanMapper beanMapper;
	
	@Override
	public void save(SysConfigEntity config) {
		config.setStatus(String.valueOf(Constant.Status.valid));
		config.setCreateTime(LocalDateTime.now());
		sysConfigDao.save(config);
	}

	@Override
	public void update(SysConfigEntity config) {
		SysConfigEntity dest = new SysConfigEntity();
		if (config.getId() != null) {
			dest = queryObject(config.getId());
			beanMapper.copy(config, dest);
		}
		sysConfigDao.update(dest);
	}

	@Override
	public void updateValueByKey(String key, String value) {
		sysConfigDao.updateValueByKey(key, value);
	}

	@Override
	public void deleteBatch(String[] ids) {

		sysConfigDao.deleteBatch(ids);
	}

	@Override
	public List<SysConfigEntity> queryList(Map<String, Object> map) {
		return sysConfigDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysConfigDao.queryTotal(map);
	}

	@Override
	public SysConfigEntity queryObject(String id) {
		return sysConfigDao.queryObject(id);
	}

	@Override
	public String getValue(String key, String defaultValue) {
		String value = sysConfigDao.queryByKey(key);
		if(StringUtils.isBlank(value)){
			return defaultValue;
		}
		return value;
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key, null);
		if(StringUtils.isNotBlank(value)){
			return JSON.parseObject(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败");
		}
	}

	@Override
	public String getValueByCode(String code){
		return sysConfigDao.queryValueByCode(code);
	};

	/**
	 * 根据code，获取配置的value值
	 *
	 * @param code
	 */
	@Override
	public SysConfigEntity getObjectByCode(String code){
		return sysConfigDao.queryObjectByCode(code);
	}
}
