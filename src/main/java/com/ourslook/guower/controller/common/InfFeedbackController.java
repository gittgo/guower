package com.ourslook.guower.controller.common;

import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.common.InfFeedbackEntity;
import com.ourslook.guower.service.common.InfFeedbackService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import com.ourslook.guower.vo.InfFeedbackVo;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 意见查看
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-05 10:54:04
 */
@CrossOrigin
@RestController
@RequestMapping("sysfullfeedback")
public class InfFeedbackController extends AbstractController {
    @Autowired
    private InfFeedbackService tInfFeedbackService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sysfullfeedback:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<InfFeedbackVo> tInfFeedbackList = tInfFeedbackService.queryVoList(query);
        int total = tInfFeedbackService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(tInfFeedbackList, total, query.getLimit(), query.getPage());

        /**
         * @see InfDictInfoController#deviceList()
         * @see Constant.DeviceConstant
         */
        List<Constant.ConstantBean> constantBeans = Constant.DeviceConstant.getAllDevices();
        Map<Integer, Constant.ConstantBean> constantBeanMap = constantBeans.stream().collect(Collectors.toMap(Constant.ConstantBean::getCode, bean -> bean));
        for (int i = 0; i < tInfFeedbackList.size(); ++i) {
            InfFeedbackVo feedbackVo = tInfFeedbackList.get(i);
            Constant.ConstantBean constantBean = constantBeanMap.get(NumberUtils.toInt(feedbackVo.getFeedbacksource()));
            if (constantBean != null) {
                feedbackVo.setFeedbacksourceName(constantBean.getName());
            }
        }
        return R.ok().putObj("page", pageUtil);
    }

    /**
     * 批量处理  处理完成
     */
    @RequestMapping("/handle")
    @RequiresPermissions("sysfullfeedback:delete")
    public R handle(@RequestBody Long[] ids) {
        for (Long id : ids) {
            InfFeedbackEntity e = tInfFeedbackService.queryObject(id);
            e.setStatus(1);
            tInfFeedbackService.update(e);
        }
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{feedbackid}")
    @RequiresPermissions("sysfullfeedback:info")
    public R info(@PathVariable("feedbackid") Long feedbackid) {
        InfFeedbackEntity tInfFeedback = tInfFeedbackService.queryObject(feedbackid);

        return R.ok().putObj("sysfullfeedback", tInfFeedback);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sysfullfeedback:save")
    public R save(@RequestBody InfFeedbackEntity tInfFeedback) {

        tInfFeedback.setCreateDate(new Date());
        ValidatorUtils.validateEntity(tInfFeedback);
        tInfFeedbackService.save(tInfFeedback);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sysfullfeedback:update")
    public R update(@RequestBody InfFeedbackEntity tInfFeedback) {

        ValidatorUtils.validateEntity(tInfFeedback);
        tInfFeedbackService.update(tInfFeedback);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysfullfeedback:delete")
    public R delete(@RequestBody Long[] feedbackids) {
        tInfFeedbackService.deleteBatch(feedbackids);

        return R.ok();
    }

}
