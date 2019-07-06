package com.ourslook.guower.dao.common;

import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.common.InfDictInfoEntity;
import com.ourslook.guower.vo.InfDictVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
@Component
public interface InfDictInfoDao extends BaseDao<InfDictInfoEntity> {

    List<InfDictVo> queryVoList(Map<String, Object> map);

    List<InfDictInfoEntity> getInfDictInfoListByTypeName(String name);

    List<InfDictInfoEntity> getInfDictInfoListByTypeCode(String code);
}
