package com.ourslook.guower.controller.business;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import com.ourslook.guower.service.business.BusAdvertisementService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.ValidateResult;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 广告表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@CrossOrigin
@RestController
@RequestMapping("busadvertisement")
public class BusAdvertisementController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusAdvertisementService busAdvertisementService;
    @Resource
    private BeanMapper beanMapper;

    /**
     * 查询记录数提示用户
     */
    @ResponseBody
    @RequestMapping("/checkThisTypeCount")
    @RequiresPermissions("busadvertisement:list")
    public ValidateResult checkThisTypeCount(HttpServletRequest request)  {
        Map map = UrlEncode.getUrlParams(request);
        //查询列表数据
        int total = busAdvertisementService.queryTotal(map);
        String recommendCount = Constant.AdvertisementType.getRecommendCount(Integer.parseInt(map.get("advertisementType")!=null?map.get("advertisementType").toString():"0"));
        return ValidateResult.ok("该类型广告已存在"+total+"条，推荐"+recommendCount+"条");
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("busadvertisement:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        //后台只按发布时间倒序
        query.put("sidx","release_date");
        query.put("order" ,"desc");
        List<BusAdvertisementEntity> busAdvertisementList = busAdvertisementService.queryList(query);
        int total = busAdvertisementService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(busAdvertisementList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("busadvertisement:info")
    public R info(@PathVariable("id") Integer id) {
            BusAdvertisementEntity busAdvertisement = busAdvertisementService.queryObject(id);

        return R.ok().putObj("busAdvertisement", busAdvertisement);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("busadvertisement:save")
    public R save(@RequestBody BusAdvertisementEntity busAdvertisement) {

        //当前登录用户
        SysUserEntity sysUser = ShiroUtils.getUserEntity();
        busAdvertisement.setReleaseUserId(sysUser.getUserId().intValue());
        busAdvertisement.setReleaseDate(LocalDateTime.now());
        busAdvertisement.setLookTimes((int)(Math.random()*10001+30000));
        if((Constant.AdvertisementType.TYPE_ADVERTISING_GUOWER_BOX+"").equals(busAdvertisement.getAdvertisementType()) && busAdvertisement.getJumpType() != 4) return R.error("果味Box只能跳转文章");
        ValidatorUtils.validateEntity(busAdvertisement);
            busAdvertisementService.save(busAdvertisement);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("busadvertisement:update")
    public R update(@RequestBody BusAdvertisementEntity busAdvertisement) {

            //busAdvertisement.setModifyuser(getUserId().toString());
            //busAdvertisement.setModifytime(new Date());
        if((Constant.AdvertisementType.TYPE_ADVERTISING_GUOWER_BOX+"").equals(busAdvertisement.getAdvertisementType()) && busAdvertisement.getJumpType() != 4) return R.error("果味Box只能跳转文章");
        ValidatorUtils.validateEntity(busAdvertisement);
            busAdvertisementService.update(busAdvertisement);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("busadvertisement:delete")
    public R delete(@RequestBody Integer[]ids) {

        //一下两种删除根据情况自己选择

        //物理删除
        busAdvertisementService.deleteBatch(ids);

        //逻辑删除
        //List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        //busAdvertisementService.multiOperate(idStr, Constant.Status.delete);

        return R.ok();
    }

   /**
    * 根据id修改busadvertisement状态
    * 这里是一个通用操作，如： 上下架、删除、启用，禁用 等等状态改变的使用这个一个方法就够了
    *
    * @param ids: 编号,字段名:modelIds,必填
    * @param status: 操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作, 参考常量Constant.Status
    */
    @RequiresPermissions("busadvertisement:delete")
    @RequestMapping(value="operateBusAdvertisementByIds")
    public XaResult operateByIds (
            @RequestParam(value = "modelIds") Integer[] ids,
            @RequestParam(defaultValue = "3") Integer status
    ) {
        List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        busAdvertisementService.multiOperate(idStr, status);
        return new XaResult().success("修改成功");
    }

    /**
    * 如果指定行，就按照指定行导出，否则 导出所有数据成excel格式
    */
    @RequestMapping("/exportsExcelAll")
    @RequiresPermissions("busadvertisement:list")
    @SuppressWarnings("unchecked")
    public void exportsExcelAll(@RequestParam Map<String, Object> params , @RequestParam Boolean isCvs, @RequestParam String[] modelIds,  HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        //指定行导出
        if (XaUtils.isNotEmpty(modelIds)) {
            /**
             * 导出指定行成为Excel格式
             * modelIds客户端使用逗号分割，这里就要用数组接收
             */
            Query query = new Query();
            query.put("id_IN", Lists.newArrayList(modelIds));
            List<BusAdvertisementEntity> vos = busAdvertisementService.queryList(query);
            busAdvertisementService.exportsToExcels(vos, request, response, false);
        } else {
            //导出所有
            if (params.containsKey("searchParams")) {
                params.putAll(JSON.parseObject(params.get("searchParams")+"", Map.class));
                params.remove("searchParams");
            }
            for (Map.Entry<String,Object> item : params.entrySet()) {
                Object value = (item.getValue() == null || item.getValue().toString().equalsIgnoreCase("null")) ? "" : item.getValue();
                item.setValue(value);
            }
            List<BusAdvertisementEntity> vos = busAdvertisementService.queryList(params);
            busAdvertisementService.exportsToExcels(vos, request, response,  isCvs == null ? false : isCvs);
        }
    }
}
