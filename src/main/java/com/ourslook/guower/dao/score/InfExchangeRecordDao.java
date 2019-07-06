package com.ourslook.guower.dao.score;

import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.score.InfExchangeRecordEntity;
import com.ourslook.guower.vo.score.InfExchangeRecordVo;

import java.util.List;
import java.util.Map;

/**
 * 兑换记录表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 *
 */
public interface InfExchangeRecordDao extends BaseDao<InfExchangeRecordEntity> {

    List<InfExchangeRecordVo> queryVoList(Map<String, Object> map);

    void grant(Map<String, Object> map);
}
