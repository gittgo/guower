package com.ourslook.guower.controller.user;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.entity.user.InfExamineEntity;
import com.ourslook.guower.service.user.InfExamineService;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.XaUtils;
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
 * 审核表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
@CrossOrigin
@RestController
@RequestMapping("infexamine")
public class InfExamineController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InfExamineService infExamineService;
    @Resource
    private BeanMapper beanMapper;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("infexamine:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<InfExamineEntity> infExamineList = infExamineService.queryList(query);
        int total = infExamineService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(infExamineList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("infexamine:info")
    public R info(@PathVariable("id") Integer id) {
            InfExamineEntity infExamine = infExamineService.queryObject(id);

        return R.ok().putObj("infExamine", infExamine);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("infexamine:save")
    public R save(@RequestBody InfExamineEntity infExamine) {

        //infExamine.setCreatetime(new Date());
        // infExamine.setCreateuser(getUserId().toString());
        ValidatorUtils.validateEntity(infExamine);
            infExamineService.save(infExamine);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("infexamine:update")
    public R update(@RequestBody InfExamineEntity infExamine) {

            //infExamine.setModifyuser(getUserId().toString());
            //infExamine.setModifytime(new Date());

        ValidatorUtils.validateEntity(infExamine);
            infExamineService.update(infExamine);

        return R.ok();
    }

    /**
     * 认证审核
     */
    @RequestMapping("/examine")
    @RequiresPermissions("infexamine:update")
    public R delete(@RequestBody ExamineCheck examineCheck) {

        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        examineCheck.setSysUserId(sysUserEntity.getUserId().intValue());
        examineCheck.setExamineDate(LocalDateTime.now());
        infExamineService.examine(examineCheck);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("infexamine:delete")
    public R delete(@RequestBody Integer[]ids) {

        //一下两种删除根据情况自己选择

        //物理删除
         //infExamineService.deleteBatch(ids);

        //逻辑删除
        //List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        //infExamineService.multiOperate(idStr, Constant.Status.delete);

        return R.ok();
    }

   /**
    * 根据id修改infexamine状态
    * 这里是一个通用操作，如： 上下架、删除、启用，禁用 等等状态改变的使用这个一个方法就够了
    *
    * @param ids: 编号,字段名:modelIds,必填
    * @param status: 操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作, 参考常量Constant.Status
    */
    @RequiresPermissions("infexamine:delete")
    @RequestMapping(value="operateInfExamineByIds")
    public XaResult operateByIds (
            @RequestParam(value = "modelIds") Integer[] ids,
            @RequestParam(defaultValue = "3") Integer status
    ) {
        List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        infExamineService.multiOperate(idStr, status);
        return new XaResult().success("修改成功");
    }

    /**
    * 如果指定行，就按照指定行导出，否则 导出所有数据成excel格式
    */
    @RequestMapping("/exportsExcelAll")
    @RequiresPermissions("infexamine:list")
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
            query.put("id``_IN", Lists.newArrayList(modelIds));
            List<InfExamineEntity> vos = infExamineService.queryList(query);
            infExamineService.exportsToExcels(vos, request, response, false);
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
            List<InfExamineEntity> vos = infExamineService.queryList(params);
            infExamineService.exportsToExcels(vos, request, response,  isCvs == null ? false : isCvs);
        }
    }
}
