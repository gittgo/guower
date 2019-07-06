package com.ourslook.guower.dao.common;

import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.common.InfFeedbackEntity;
import com.ourslook.guower.vo.InfFeedbackVo;

import java.util.List;
import java.util.Map;

/**
 * 意见查看
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-05 10:54:04
 *
 */
public interface InfFeedbackDao extends BaseDao<InfFeedbackEntity> {
    List<InfFeedbackVo> queryVoList(Map<String, Object> map);
}
