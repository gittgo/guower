package com.ourslook.guower.service.common;

import com.ourslook.guower.entity.common.InfDitcypeInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 字典类型表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
public interface InfoDitcypeInfoService {

    InfDitcypeInfoEntity queryObject(Long id);

    List<InfDitcypeInfoEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(InfDitcypeInfoEntity xaDitcypeInfo);

    void update(InfDitcypeInfoEntity xaDitcypeInfo);

    void delete(Long id);

    void deleteBatch(Long[] ids);

    /**
     * 测试事物
     */
    void testTransactionals() throws Exception;

    /**
     * 测试多线程同步
     */
    void testSynchronized(String param);

    void testSynchronizeds(String param);
}
