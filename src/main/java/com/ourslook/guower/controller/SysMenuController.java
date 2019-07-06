package com.ourslook.guower.controller;

import com.ourslook.guower.entity.SysMenuEntity;
import com.ourslook.guower.service.SysMenuService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Constant.MenuType;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.annotation.LoggSys;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author dazer
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 所有菜单列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SysMenuEntity> menuList = sysMenuService.queryList(query);
        int total = sysMenuService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(menuList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }

    /**
     * 获取目录下拉框
     */
    @RequestMapping(value = "/catalogList", produces = {"application/json;charset=UTF-8"})
    public R getCatalogList() {
        //获取所有目录及菜单
        List<SysMenuEntity> menuListForQuery = sysMenuService.getUserMenuList(getUserId());
        //提取出来目录
        List<SysMenuEntity> catalogList = sysMenuService.getCatalogList(menuListForQuery);
        return R.ok().putObj("catalogList", catalogList);

    }

    /**
     * 选择菜单(添加、修改菜单)
     * 访问路径：sys/menu/select
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public R select() {
        //查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

        //添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);

        menuList.add(root);

        return R.ok().putObj("menuList", menuList);
    }

    /**
     * 角色授权菜单
     */
    @RequestMapping("/perms")
    @RequiresPermissions("sys:menu:perms")
    public R perms() {
        //查询列表数据
        List<SysMenuEntity> menuList = null;

        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() == Constant.SYS_ROLO_ID_SUPER_ADMIN) {
            menuList = sysMenuService.queryList(new HashMap<>());
        } else {
            menuList = sysMenuService.queryUserList(getUserId());
        }
        return R.ok().putObj("menuList", menuList);
    }

    /**
     * 菜单信息
     */
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public R info(@PathVariable("menuId") Long menuId) {
        SysMenuEntity menu = sysMenuService.queryObject(menuId);
        return R.ok().putObj("menu", menu);
    }

    /**
     * 保存
     */
    @LoggSys("保存菜单")
    @RequestMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public R save(@RequestBody SysMenuEntity menu) {
        ValidatorUtils.validateEntity(menu);
        //数据校验
        verifyForm(menu);

        menu.setCreateTime(LocalDateTime.now());
        sysMenuService.save(menu);
        return R.ok();
    }

    /**
     * 修改
     */
    @LoggSys("修改菜单")
    @RequestMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public R update(@RequestBody SysMenuEntity menu) {
        ValidatorUtils.validateEntity(menu);
        //数据校验
        verifyForm(menu);
        menu.setUpdateTime(LocalDateTime.now());
        sysMenuService.update(menu);

        return R.ok();
    }

    /**
     * 删除
     */
    @LoggSys("删除菜单")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    public R delete(@RequestBody Long[] menuIds) {
        for (Long menuId : menuIds) {
            if (menuId <= 30) {
                return R.error("系统菜单，不能删除");
            }
        }
        //删除菜单
        sysMenuService.deleteBatch(menuIds);
        return R.ok();
    }

    /**
     * 用户菜单列表
     */
    @RequestMapping("/user")
    public R user() {
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
        Set<String> permissions = sysMenuService.getUserPermissions(getUserId());
        return R.ok().putObj("menuList", menuList).putObj("permissions", permissions);
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenuEntity parentMenu = sysMenuService.queryObject(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuType.CATALOG.getValue() ||
                menu.getType() == MenuType.MENU.getValue()) {
            if (parentType != MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == MenuType.BUTTON.getValue()) {
            if (parentType != MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
