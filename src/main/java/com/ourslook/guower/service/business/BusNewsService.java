package com.ourslook.guower.service.business;


import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.vo.business.BusNewsVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 新闻文章表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
public interface BusNewsService {

	BusNewsEntity queryObject(Integer id);
	
	List<BusNewsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BusNewsEntity busNews);

    void multiOperate(List<String> modelIds, Integer status);

	void update(BusNewsEntity busNews);

	void examine(ExamineCheck examineCheck);

	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	List<BusNewsVo> queryVoList(Map<String, Object> map);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<BusNewsEntity> busNewss, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;

	/**
	 * 根据作者查询总浏览量
	 * @param author
	 * @return
	 */
	int getBrowseTotalByAuthor(Long author);

	/**
	 * 获取热门文章
	 * @return
	 */
	List<BusNewsEntity> getHotNews(Map<String, Object> map);

	BusNewsVo queryObjectVo(Integer id);

	/**
	 * 发布文章逻辑
	 * @param busNews
	 */
	void addNews(BusNewsEntity busNews);
}
