package com.ourslook.guower.controller;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.service.SysUserRoleService;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.annotation.LoggSys;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.ValidateResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.utils.validator.Validator;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import com.ourslook.guower.utils.validator.group.AddGroup;
import com.ourslook.guower.utils.validator.group.UpdateGroup;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
@CrossOrigin
@Controller
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        /*if (getUserId() != Constant.SYS_ROLO_ID_SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }*/

        //查询列表数据
        Query query = new Query(params);
        List<SysUserEntity> userList = sysUserService.queryList(query);
        int total = sysUserService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }

    /**
     * 获取登录的用户信息
     */
    @ResponseBody
    @RequestMapping("/info")
    public R info() {
        return R.ok().putObj("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @LoggSys("修改密码")
    @RequestMapping("/password")
    @ResponseBody
    public R password(String password, String newPassword) {
        Long i = sysUserService.queryObject(getUserId()).getCreateUserId();
        if (i == 2L) {
            return R.error("手机用户不支持在平台改密");
        }

        AbstractAssert.isBlank(newPassword, "新密码不为能空");

        //sha256加密
        password = new Sha256Hash(password).toHex();
        //sha256加密
        newPassword = new Sha256Hash(newPassword).toHex();

        //更新密码
        int count = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (count == 0) {
            return R.error("原密码不正确");
        }

        //退出
        ShiroUtils.logout();

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    @ResponseBody
    public R info(@PathVariable("userId") Long userId) throws Exception{
        SysUserEntity user = sysUserService.queryObject(userId);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().putObj("user", user);
    }

    /**
     * 保存用户
     */
    @LoggSys("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    @ResponseBody
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        //检查角色是否越权
        sysUserService.checkRole(getUser(), user);

        user.setCreateUserId(getUserId());
        sysUserService.save(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @LoggSys("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    @ResponseBody
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());

        //检查角色是否越权
        sysUserService.checkRole(getUser(), user);

        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @LoggSys("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    @ResponseBody
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }
        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }
        sysUserService.deleteBatch(userIds);
        return R.ok();
    }

    /**
     * 验证配 是否重复
     *
     * @see  Validator
     * @see  ValidatorUtils
     * @see  AbstractAssert
     *
     * @param request
     * @return
     * @see SysRoleController#checkRoleNameExist(javax.servlet.http.HttpServletRequest)
     */
    @ResponseBody
    @RequestMapping(value = "checkUsernameExist", method = RequestMethod.POST)
    public ValidateResult checkUsernameExist(HttpServletRequest request) {
        Map params = UrlEncode.getUrlParams(request);
        String username = params.get("username") + "".trim();
        boolean userinfoIsAdd = Boolean.parseBoolean(params.get("userinfoIsAdd") + "".trim());
        String usernameOldValue = params.get("usernameOldValue") + "".trim();

        if (org.apache.commons.lang3.StringUtils.containsWhitespace(username+"")) {
            return ValidateResult.error("登录用户名不能包含空格!");
        }

        if (userinfoIsAdd) {
            SysUserEntity sysUserEntity = sysUserService.queryByUserName(username);
            if (XaUtils.isEmpty(sysUserEntity)) {
                return ValidateResult.ok();
            } else {
                return ValidateResult.error();
            }
        } else {
            if (usernameOldValue.equals(username)) {
                return ValidateResult.ok();
            } else {
                SysUserEntity sysUserEntity = sysUserService.queryByUserName(username);
                if (sysUserEntity == null) {
                    return ValidateResult.ok();
                } else {
                    return ValidateResult.error();
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "checkMobileExist", method = RequestMethod.POST)
    public ValidateResult checkMobileExist(javax.servlet.http.HttpServletRequest request) {
        Map params = UrlEncode.getUrlParams(request);
        String mobile = params.get("mobile") + "".trim();
        boolean userinfoIsAdd = Boolean.parseBoolean(params.get("userinfoIsAdd") + "".trim());
        String mobileOldValue = params.get("mobileOldValue") + "".trim();

        if (userinfoIsAdd) {
            SysUserEntity sysUserEntity = sysUserService.queryByMoble(mobile);
            if (sysUserEntity == null) {
                return ValidateResult.ok();
            } else {
                return ValidateResult.error();
            }
        } else {
            if (mobileOldValue.equals(mobile)) {
                return ValidateResult.ok();
            } else {
                SysUserEntity sysUserEntity = sysUserService.queryByMoble(mobile);
                if (sysUserEntity == null) {
                    return ValidateResult.ok();
                } else {
                    return ValidateResult.error();
                }
            }
        }
    }

    /**
     * 后台首页：获取客户端信息
     */
    @RequestMapping("/infoPc")
    @ResponseBody
    public R infoPc(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>(10);
        XaUtils.getBroserInfo(request);
        Browser browser = XaUtils.getBroserInfo(request);
        OperatingSystem os = XaUtils.getBroserInfoOS(request);

        map.put("os", os.getGroup());
        map.put("browser", browser.getName());
        map.put("nowTime", DateUtils8.parseDate(LocalDateTime.now()));
        map.put("ipAddr", XaUtils.getClientIpAddress2(request));
        map.put("server", "nginx-1.13. || centos7.4");
        map.put("javaversion", "jdk8");
        map.put("mysql", "5.6.40");
        map.put("port", request.getRemotePort());
        map.put("localAddr", request.getLocalAddr());
        map.put("localName", request.getLocalName());
        map.put("username", getUser().getUsername());

        if (sysUserService.isSuperAdmin(getUserId())) {
            map.put("systemInfos", true);
        } else {
            map.put("systemInfos", false);
        }
        return R.ok().putObj("userPcInfo", map);
    }

    /**
     * 后台首页：获取客户端信息 获取ip详细新
     */
    @RequestMapping("/infoPcIpAddress")
    @ResponseBody
    public R infoPc(@RequestParam String ip) {
        String ipUrl = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip;
        String jsonStr = restTemplate.getForEntity(ipUrl, String.class).getBody();
        Map map = JSON.parseObject(jsonStr, Map.class);
        String result = "";
        if (map != null) {
            if ((map.get("ret") + "").equals("1")) {
                result = map.get("country") + "" + map.get("province") + map.get("city") + map.get("district");
            }
        } else {
            result = "";
        }
        return R.ok().putObj("ipaddress", result);
    }

    /**
     * 查询当前用户是否是超级管理员，进行权限控制
     *
     * @return
     */
    @RequestMapping("/isSuperAdmin")
    @ResponseBody
    public R isSuperAdmin() {
        return R.ok().putObj("isSuperAdmin", sysUserService.isSuperAdmin(getUserId()));
    }
}
