package com.ourslook.guower.controller.common;

import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.common.InfClauseInfoEntity;
import com.ourslook.guower.entity.common.InfDictInfoEntity;
import com.ourslook.guower.service.common.InfClauseInfoService;
import com.ourslook.guower.service.common.InfDictInfoService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文案维护(协议维护、关于我们等)
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-06 09:45:08
 */
@CrossOrigin
@RestController
@RequestMapping("tinfclauseinfo")
public class InfClauseInfoController extends AbstractController {
    @Autowired
    private InfClauseInfoService tInfClauseInfoService;
    @Autowired
    private InfDictInfoService infDictInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("tinfclauseinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<InfClauseInfoEntity> tInfClauseInfoList = tInfClauseInfoService.queryList(query);
        int total = tInfClauseInfoService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(tInfClauseInfoList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{clauseId}")
    @RequiresPermissions("tinfclauseinfo:info")
    public R info(@PathVariable("clauseId") Long clauseId) {
        InfClauseInfoEntity tInfClauseInfo = tInfClauseInfoService.queryObject(clauseId);

        return R.ok().putObj("tInfClauseInfo", tInfClauseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("tinfclauseinfo:save")
    public R save(@RequestBody InfClauseInfoEntity tInfClauseInfo) {
        tInfClauseInfo.setCreatetime(DateUtils.getCurrentDate());
        tInfClauseInfo.setCreateuser(getUserId().toString());
        tInfClauseInfo.setModifytime(DateUtils.getCurrentDate());
        tInfClauseInfo.setModifyuser(getUserId().toString());
        ValidatorUtils.validateEntity(tInfClauseInfo);
        {
            //校验每种文案只能添加一次
            List<InfClauseInfoEntity>  infClauseInfoEntities = tInfClauseInfoService.queryList(new Query());
            if (infClauseInfoEntities.stream().map(InfClauseInfoEntity::getClausetype).collect(Collectors.toSet()).contains(tInfClauseInfo.getClausetype())) {
                throw new RRException("每种文案只能添加一次,请选择其他文案类型!");
            }
        }

        //这里保存名称
        InfDictInfoEntity infDictInfoEntity = infDictInfoService.queryObjectByCode(tInfClauseInfo.getClausetype());
        if (infDictInfoEntity != null) {
            tInfClauseInfo.setName(infDictInfoEntity.getName());
        }

        tInfClauseInfoService.save(tInfClauseInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("tinfclauseinfo:update")
    public R update(@RequestBody InfClauseInfoEntity tInfClauseInfo) {
        tInfClauseInfo.setModifytime(DateUtils.getCurrentDate());
        tInfClauseInfo.setModifyuser(getUserId().toString());
        ValidatorUtils.validateEntity(tInfClauseInfo);

        //这里保存名称
        InfDictInfoEntity infDictInfoEntity = infDictInfoService.queryObjectByCode(tInfClauseInfo.getClausetype());
        if (infDictInfoEntity != null) {
            tInfClauseInfo.setName(infDictInfoEntity.getName());
        }

        tInfClauseInfoService.update(tInfClauseInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("tinfclauseinfo:delete")
    public R delete(@RequestBody Long[] clauseIds) {
        tInfClauseInfoService.deleteBatch(clauseIds);
        return R.ok();
    }

}
