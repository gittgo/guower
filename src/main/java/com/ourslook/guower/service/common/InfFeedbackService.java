package com.ourslook.guower.service.common;

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
 */
public interface InfFeedbackService {

    InfFeedbackEntity queryObject(Long feedbackid);

    List<InfFeedbackEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(InfFeedbackEntity tInfFeedback);

    void update(InfFeedbackEntity tInfFeedback);

    void delete(Long feedbackid);

    void deleteBatch(Long[] feedbackids);

    List<InfFeedbackVo> queryVoList(Map<String, Object> map);
}
