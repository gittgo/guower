//package com.ourslook.guower.controller.common;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
//import com.ourslook.guower.controller.AbstractController;
//import com.ourslook.guower.controller.SysRoleController;
//import com.ourslook.guower.entity.common.TbUserEntity;
//import com.ourslook.guower.service.SysUserService;
//import com.ourslook.guower.service.TokenService;
//import com.ourslook.guower.service.common.TbUserService;
//import com.ourslook.guower.utils.*;
//import com.ourslook.guower.utils.beanmapper.BeanMapper;
//import com.ourslook.guower.utils.result.R;
//import com.ourslook.guower.utils.result.ValidateResult;
//import com.ourslook.guower.utils.validator.ValidatorUtils;
//import com.ourslook.guower.vo.TbUserVo;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * 用户信息
// *
// * @author dazer
// * @email ab601026460@qq.com
// * @date 2017-12-05 15:21:55
// */
//@CrossOrigin
//@RestController
//@RequestMapping("user")
//public class TbUserController extends AbstractController {
//    @Autowired
//    private TbUserService tbUserService;
//    @Autowired
//    private SysUserService sysUserService;
//    @Autowired
//    private TokenService tokenService;
//    @Autowired
//    private BeanMapper beanMapper;
//
//    /**
//     * 用户列表
//     */
//    @RequestMapping("/list")
//    @RequiresPermissions("user:list")
//    public R list(@RequestParam Map<String, Object> params) {
//        //查询列表数据
//        Query query = new Query(params);
//        List<String> type = new LinkedList<>();
//        type.add(Constant.UserTypes.USER_NORMAL_1.toString());
//        type.add(Constant.UserTypes.USER_NORMAL_2.toString());
//        type.add(Constant.UserTypes.USER_NORMAL_3.toString());
//        type.add(Constant.UserTypes.USER_NORMAL_4.toString());
//        type.add(Constant.UserTypes.USER_NORMAL_5.toString());
//        type.add(Constant.UserTypes.USER_NORMAL_6.toString());
//        query.put("userType_IN", type);
//        List<TbUserVo> users = tbUserService.queryShopList(query);
//        //List<TbUserEntity> users = tbUserService.queryListUser(query);
//        int total = tbUserService.queryShopTotal(query);
//        PageUtils pageUtil = new PageUtils(users, total, query.getLimit(), query.getPage());
//        return R.ok().putObj("page", pageUtil);
//    }
//
//    /**
//     * 商家列表
//     */
//    @RequestMapping("/shoplist")
//    public R shoplist(@RequestParam Map<String, Object> params) {
//        //查询列表数据
//        Query query = new Query(params);
//        query.put("userTypes", Constant.UserTypes.USER_SELLER);
//        {
//            //数据权限@@@
//            if (!sysUserService.isSuperAdmin(getUserId())) {
//                query.put("userid", sysUserService.getTbUserId(getUserId()));
//            }
//        }
//        List<TbUserVo> users = tbUserService.queryShopList(query);
//        int total = tbUserService.queryTotal(query);
//
//        PageUtils pageUtil = new PageUtils(users, total, query.getLimit(), query.getPage());
//
//        return R.ok().putObj("page", pageUtil);
//    }
//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{userid}")
//    @RequiresPermissions("user:info")
//    public R info(@PathVariable("userid") Long userid) {
//        TbUserVo user = tbUserService.queryShopObject(userid);
//        if (user.getPayEndBalance() == null) {
//            user.setPayEndBalance(0D);
//        }
//        if (user.getCashDeposit() == null) {
//            user.setCashDeposit(0L);
//        }
//        return R.ok().putObj("user", user);
//    }
//
//    /**
//     * 保存- 这里是新增商家用户； 普通用户不能咋这里新增
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions("user:save")
//    public R save(@RequestBody TbUserVo user) {
//        {
//            //项目特殊临时加的逻辑
//            user.setUserTypes(Constant.UserTypes.USER_SELLER);
//            if (StringUtils.isNotBlank(user.getName()) && StringUtils.isBlank(user.getUsername())) {
//                user.setUsername(user.getName());
//            }
//            if (StringUtils.isNotBlank(user.getUsername()) && StringUtils.isBlank(user.getName())) {
//                user.setName(user.getUsername());
//            }
//            //这里验证手机号是否已经被用
//            if (XaUtils.isBlank(user.getMobile())) {
//                throw new RRException("手机号不能为空！");
//            }
//            TbUserEntity tbUserEntity = tbUserService.queryByMobile(user.getMobile(), Constant.UserTypes.USER_SELLER);
//            if (tbUserEntity != null) {
//                throw new RRException("手机号已经被占用，请重新填写！");
//            }
//        }
//
//        tbUserService.save(user);
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("user:update")
//    public R update(@RequestBody TbUserVo user) {
//        Long id = NumberUtils.toLong(user.getStudyHotel());
//        ValidatorUtils.validateEntity(user);
//        tbUserService.sellerUpdate(user);
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("user:delete")
//    public R delete(@RequestBody Long[] userids) {
//        for (int i = 0; i < userids.length; i++) {
//            TbUserEntity tbUserEntity = tbUserService.queryObject(userids[i]);
//            tbUserEntity.setStatus(Constant.Status.delete);
//            tbUserService.update(tbUserEntity);
//        }
//        return R.ok();
//    }
//
//    /**
//     * 导出指定行成为Excel格式
//     */
//    @ResponseBody
//    @RequestMapping("/exportsExcel")
//    @RequiresPermissions("user:list")
//    public void exportsExcel(@RequestParam String[] modelIds, HttpServletRequest request,
//                             HttpServletResponse response) throws Exception {
//        Query params = new Query();
//        params.put("userid_IN", Lists.newArrayList(modelIds));
//        List<TbUserEntity> vos = tbUserService.queryList(params);
//        tbUserService.exportsToExcels(vos, request, response, false);
//    }
//
//    /**
//     * 导出所有数据成excel格式
//     */
//    @ResponseBody
//    @RequestMapping("/exportsExcelAll")
//    @RequiresPermissions("user:list")
//    @SuppressWarnings("unchecked")
//    public void exportsExcelAll(@RequestParam Map<String, Object> params, @RequestParam Boolean isCvs, HttpServletRequest request,
//                                HttpServletResponse response) throws Exception {
//        if (params.containsKey("searchParams")) {
//            params.putAll(JSON.parseObject(params.get("searchParams") + "", Map.class));
//            params.remove("searchParams");
//        }
//        for (Map.Entry<String, Object> item : params.entrySet()) {
//            Object value = (item.getValue() == null || item.getValue().toString().equalsIgnoreCase("null")) ? "" : item.getValue();
//            item.setValue(value);
//        }
//        List<TbUserEntity> vos = tbUserService.queryList(params);
//        tbUserService.exportsToExcels(vos, request, response, isCvs == null ? false : isCvs);
//    }
//
//    /**
//     * 验证手机号是否重复
//     *
//     * @param request
//     * @return
//     * @see SysRoleController#checkRoleNameExist(javax.servlet.http.HttpServletRequest)
//     * <p>
//     * data-rule="手机号:required;length(0~11);mobile;remote[post:/guower/user/checkTbUserMobileExist]"
//     */
//    @ResponseBody
//    @RequestMapping(value = "checkTbUserMobileExist", method = RequestMethod.POST)
//    public ValidateResult checkTbUserMobileExist(javax.servlet.http.HttpServletRequest request) {
//        String mobile = UrlEncode.getUrlParams(request).get("mobile") + "".trim();
//
//        TbUserEntity tbUserEntity = tbUserService.queryByMobile(mobile, Constant.UserTypes.USER_SELLER);
//        if (XaUtils.isEmpty(tbUserEntity)) {
//            return ValidateResult.ok();
//        } else {
//            return ValidateResult.error();
//        }
//    }
//
//
//    /**
//     * 解封
//     */
//    @RequestMapping("/jiefeng")
//    @RequiresPermissions("user:update")
//    public R jiefeng(@RequestBody Long[] userids) {
//        for (Long i : userids) {
//            tbUserService.jiefengBatch(i);
//        }
//        return R.ok();
//    }
//
//    /**
//     * 查封
//     */
//    @RequestMapping("/chafeng")
//    @RequiresPermissions("user:delete")
//    public R chafeng(@RequestBody Long[] userids) {
//        for (Long i : userids) {
//            tbUserService.chafengBatch(i);
//        }
//        return R.ok();
//    }
//}
