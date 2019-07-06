package com.ourslook.guower.controller.score;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.entity.score.InfCurrencyEntity;
import com.ourslook.guower.service.score.InfCurrencyService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.R;
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
 * 货币表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@CrossOrigin
@RestController
@RequestMapping("infcurrency")
public class InfCurrencyController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InfCurrencyService infCurrencyService;
    @Resource
    private BeanMapper beanMapper;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("infcurrency:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<InfCurrencyEntity> infCurrencyList = infCurrencyService.queryList(query);
        int total = infCurrencyService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(infCurrencyList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("infcurrency:info")
    public R info(@PathVariable("id") Integer id) {
            InfCurrencyEntity infCurrency = infCurrencyService.queryObject(id);

        return R.ok().putObj("infCurrency", infCurrency);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("infcurrency:save")
    public R save(@RequestBody InfCurrencyEntity infCurrency) {

        //当前登录用户
        SysUserEntity sysUser = ShiroUtils.getUserEntity();
        infCurrency.setReleaseUserId(sysUser.getUserId().intValue());
        infCurrency.setReleaseDate(LocalDateTime.now());
        infCurrency.setDelFlag(Constant.Status.valid);
        ValidatorUtils.validateEntity(infCurrency);
            infCurrencyService.save(infCurrency);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("infcurrency:update")
    public R update(@RequestBody InfCurrencyEntity infCurrency) {

            //infCurrency.setModifyuser(getUserId().toString());
            //infCurrency.setModifytime(new Date());

        ValidatorUtils.validateEntity(infCurrency);
            infCurrencyService.update(infCurrency);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("infcurrency:delete")
    public R delete(@RequestBody Integer[]ids) {

        //逻辑删除，兑换表关联，不可物理删除
        infCurrencyService.deleteBatch(ids);

        return R.ok();
    }

   /**
    * 根据id修改infcurrency状态
    * 这里是一个通用操作，如： 上下架、删除、启用，禁用 等等状态改变的使用这个一个方法就够了
    *
    * @param ids: 编号,字段名:modelIds,必填
    * @param status: 操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作, 参考常量Constant.Status
    */
    @RequiresPermissions("infcurrency:delete")
    @RequestMapping(value="operateInfCurrencyByIds")
    public XaResult operateByIds (
            @RequestParam(value = "modelIds") Integer[] ids,
            @RequestParam(defaultValue = "3") Integer status
    ) {
        List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        infCurrencyService.multiOperate(idStr, status);
        return new XaResult().success("修改成功");
    }

    /**
    * 如果指定行，就按照指定行导出，否则 导出所有数据成excel格式
    */
    @RequestMapping("/exportsExcelAll")
    @RequiresPermissions("infcurrency:list")
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
            List<InfCurrencyEntity> vos = infCurrencyService.queryList(query);
            infCurrencyService.exportsToExcels(vos, request, response, false);
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
            List<InfCurrencyEntity> vos = infCurrencyService.queryList(params);
            infCurrencyService.exportsToExcels(vos, request, response,  isCvs == null ? false : isCvs);
        }
    }
}
