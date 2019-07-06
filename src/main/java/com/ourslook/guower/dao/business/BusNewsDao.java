package com.ourslook.guower.dao.business;


import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.vo.business.BusNewsVo;

import java.util.List;
import java.util.Map;

/**
 * 新闻文章表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 *
 */
public interface BusNewsDao extends BaseDao<BusNewsEntity> {

    List<BusNewsVo> queryVoList(Map<String, Object> map);

    int getBrowseTotalByAuthor(Long author);

    List<BusNewsEntity> getHotNews(Map<String, Object> map);

    void examine(ExamineCheck examineCheck);

    BusNewsVo queryObjectVo(Integer id);
}
