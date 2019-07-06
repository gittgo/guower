package com.ourslook.guower.dao.common;

import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.common.InfVersionInfoEntity;
import com.ourslook.guower.vo.InfVersionInfoVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 版本更新
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-01-02 13:21:28
 *
 */
public interface InfVersionInfoDao extends BaseDao<InfVersionInfoEntity> {
    InfVersionInfoEntity queryNewObject(String device);
    List<InfVersionInfoVo> queryVoList(Map<String, Object> map);
}
