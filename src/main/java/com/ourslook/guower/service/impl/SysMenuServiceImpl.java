package com.ourslook.guower.service.impl;

import com.google.common.collect.Lists;
import com.ourslook.guower.config.DruidConfig;
import com.ourslook.guower.dao.SysMenuDao;
import com.ourslook.guower.entity.SysMenuEntity;
import com.ourslook.guower.service.SysMenuService;
import com.ourslook.guower.service.SysRoleMenuService;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.Constant.MenuType;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private BeanMapper beanMapper;

    private List<Long> idList = new ArrayList<>();
    @Value("${spring.datasource.dbType:#{null}}")
    private String dbType;


    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = sysMenuDao.queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return sysMenuDao.queryNotButtonList();
    }

    @Override
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == 1) {
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    @Override
    public List<SysMenuEntity> queryUserList(Long userId) {
        return sysMenuDao.queryUserList(userId);
    }


    /**
     * 根据父id获取下面所有的菜单（递归）闫
     *
     * @return
     */
    @Override
    public List<Long> getChildList(Long parentId) {
        List<SysMenuEntity> menuList = sysMenuDao.queryListParentId(parentId);
        if (menuList.size() > 0) {
            for (SysMenuEntity menu : menuList) {
//                System.out.println("idList");
                logger.info("idList");
                idList.add(menu.getMenuId());
                menu.setList(this.getChildList(menu.getMenuId()));
            }
        }
        return idList;

    }

    /**
     * 用户所有菜单中获取目录  闫
     */
    @Override
    public List<SysMenuEntity> getCatalogList(List<SysMenuEntity> menuList) {
        List<SysMenuEntity> catalogList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menu.getType() == 0L) {
                catalogList.add(menu);
            }
        }
        return catalogList;
    }

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == 1) {
            List<SysMenuEntity> menuList = queryList(new HashMap<String, Object>());
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserService.queryAllPerms(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<String>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysMenuEntity queryObject(Long menuId) {
        return sysMenuDao.queryObject(menuId);
    }

    @Override
    public List<SysMenuEntity> queryList(Map<String, Object> map) {
        return sysMenuDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysMenuDao.queryTotal(map);
    }

    @Override
    public void save(SysMenuEntity menu) {
        sysMenuDao.save(menu);
    }

    @Override
    public void update(SysMenuEntity menu) {
        sysMenuDao.update(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] menuIds) {
        //oracle数据库
        if (DruidConfig.DBTYPE_ORACLE.equalsIgnoreCase(dbType.toLowerCase())) {
            sysMenuDao.deleteBatchMenuOnOracle(Lists.newArrayList(menuIds));
        } else {
            ///mysql请修改配置文件中的值
            sysMenuDao.deleteBatch(menuIds);

            List orphanBtnMenus = sysMenuDao.queryOrphanBtnMenus();

            if (XaUtils.isNotEmpty(orphanBtnMenus)) {
                sysMenuDao.deleteOrphanBtnMenus(orphanBtnMenus);
            }
        }
    }

    /**
     * 获取所有目录列表
     */
    private List<SysMenuEntity> getAllCatalogList(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        return menuList;
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }


    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

        for (SysMenuEntity entity : menuList) {
            //目录
            if (entity.getType() == MenuType.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }


}
