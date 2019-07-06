package com.ourslook.guower.dao.business;


import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.business.BusAdvertisementEntity;

/**
 * 广告表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 *
 */
public interface BusAdvertisementDao extends BaseDao<BusAdvertisementEntity> {

    int queryTotalByNewsId(Integer[] newsIds);
}
