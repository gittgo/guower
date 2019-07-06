package com.ourslook.guower.service.impl.common;

import com.ourslook.guower.dao.common.InfFeedbackDao;
import com.ourslook.guower.entity.common.InfFeedbackEntity;
import com.ourslook.guower.service.common.InfFeedbackService;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.vo.InfFeedbackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("tInfFeedbackService")
public class InfFeedbackServiceImpl implements InfFeedbackService {
	@Autowired
	private InfFeedbackDao tInfFeedbackDao;
    @Resource
    private BeanMapper beanMapper;
	
	@Override
	public InfFeedbackEntity queryObject(Long feedbackid){
		return tInfFeedbackDao.queryObject(feedbackid);
	}
	
	@Override
	public List<InfFeedbackEntity> queryList(Map<String, Object> map){
		return tInfFeedbackDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return tInfFeedbackDao.queryTotal(map);
	}
	
	@Override
	public void save(InfFeedbackEntity tInfFeedback){
		tInfFeedbackDao.save(tInfFeedback);
	}
	
	@Override
	public void update(InfFeedbackEntity tInfFeedback){
		InfFeedbackEntity dest = new InfFeedbackEntity();
        if (tInfFeedback.getId() != null) {
            dest = queryObject(tInfFeedback.getId());
            beanMapper.copy(tInfFeedback, dest);
        }
		tInfFeedbackDao.update(dest);
	}
	
	@Override
	public void delete(Long feedbackid){
		tInfFeedbackDao.delete(feedbackid);
	}
	
	@Override
	public void deleteBatch(Long[] feedbackids){
		tInfFeedbackDao.deleteBatch(feedbackids);
	}

	@Override
	public List<InfFeedbackVo> queryVoList(Map<String, Object> map) {
		return tInfFeedbackDao.queryVoList(map);
	}

}
