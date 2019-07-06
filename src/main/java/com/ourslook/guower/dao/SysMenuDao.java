package com.ourslook.guower.dao;

import com.ourslook.guower.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单管理
 */
public interface SysMenuDao extends BaseDao<SysMenuEntity> {

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuEntity> queryNotButtonList();

    /**
     * 查询用户的权限列表
     */
    List<SysMenuEntity> queryUserList(Long userId);

    /**
     * 删除孤儿按钮级别菜单
     *
     * @param menuids
     * @return
     */
    int deleteOrphanBtnMenus(@Param("array") List menuids);

    /**
     * 查询孤儿菜单
     * @return
     */
    List<Long> queryOrphanBtnMenus();

    /**
     * oracle不支持同时执行多条语句，只用于oracle数据库
     *
     * @param ids
     * @return
     */
    void deleteBatchMenuOnOracle(List<Long> ids);
}
