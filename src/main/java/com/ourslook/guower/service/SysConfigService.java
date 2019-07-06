package com.ourslook.guower.service;

import com.ourslook.guower.entity.SysConfigEntity;
import com.ourslook.guower.utils.Constant;

import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 */
public interface SysConfigService {

    /**
     * 保存配置信息
     */
    void save(SysConfigEntity config);

    /**
     * 更新配置信息
     */
    void update(SysConfigEntity config);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     */
    void deleteBatch(String[] ids);

    /**
     * 获取List列表
     */
    List<SysConfigEntity> queryList(Map<String, Object> map);

    /**
     * 获取总记录数
     */
    int queryTotal(Map<String, Object> map);

    SysConfigEntity queryObject(String id);

    /**
     * 根据key，获取配置的value值
     *
     * @param key          key
     * @param defaultValue 缺省值
     */
    String getValue(String key, String defaultValue);

    /**
     * 根据key，获取value的Object对象
     *
     * @param key   key
     * @param clazz Object对象
     */
    <T> T getConfigObject(String key, Class<T> clazz);


    /**
     * 根据code，获取配置的value值
     *
     * @param code
     */
    String getValueByCode(String code);

    /**
     * 根据code，获取配置的value值
     *
     * @param code
     *
     * @see Constant.SysConfigValue
     */
    SysConfigEntity getObjectByCode(String code);

}
