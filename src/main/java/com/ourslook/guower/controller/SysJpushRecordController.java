package com.ourslook.guower.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ourslook.guower.entity.SysJpushRecordEntity;
import com.ourslook.guower.service.SysJpushRecordService;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-13 18:36:41
 */
@CrossOrigin
@RestController
@RequestMapping("sys/jpushrecord")
public class SysJpushRecordController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_PLATFORM_IOS_ALL = "ios_all";
    private static final String KEY_PLATFORM_IOS_APNS_PRODUCTION_TRUE = "ios_apns_production_true";
    private static final String KEY_PLATFORM_IOS_APNS_PRODUCTION_FALSE = "ios_apns_production_false";
    private static final String KEY_ERROR_CODE = "errorCode";
    private static final String KEY_ERROR_CODE_JPUSH_SERVER_ERROR = "8888_8888";

    @Autowired
    private SysJpushRecordService sysJpushRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:jpushrecord:list")
    public R list(@RequestParam Map<String, Object> params) {

        //1： 处理平台筛选条件
        String platForm = params.get(KEY_PLATFORM) + "";
        if (XaUtils.isNotBlank(platForm)) {
            if ((platForm.toLowerCase().contains("ios"))) {
                switch (platForm) {
                    case KEY_PLATFORM_IOS_APNS_PRODUCTION_TRUE:
                        params.put("apnsProduction", "true");
                        break;
                    case KEY_PLATFORM_IOS_APNS_PRODUCTION_FALSE:
                        params.put("apnsProduction", "false");
                        break;
                    default:
                        break;
                }
                params.put(KEY_PLATFORM, "ios");
            }
        }
        //2:处理error_code
        String errorCode = params.get(KEY_ERROR_CODE) + "";
        if (errorCode.equals(KEY_ERROR_CODE_JPUSH_SERVER_ERROR)) {
            params.put(KEY_ERROR_CODE, "");
            params.put("errorMsg", "support@jpush.cn");
        }

        //查询列表数据
        Query query = new Query(params);

        List<SysJpushRecordEntity> sysJpushRecordList = sysJpushRecordService.queryList(query);
        int total = sysJpushRecordService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysJpushRecordList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }

    /**
     * 统计
     */
    @RequestMapping("/chart")
    @RequiresPermissions("sys:jpushrecord:list")
    public R chart(@RequestBody Map<String, Object> params) {
        String chartsType = params.get("chartsType") == null || params.get("chartsType").equals("") ? "line" : params.get("chartsType").toString();

        DateTimeFormatter dateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Object> resultMap = new HashMap<>();
        String dd = dateStr.format(LocalDate.now());
        //查询列表数据
        params.put("createDate_BETWEEN", dateStr.format(LocalDate.now().minusDays(6)) + " - " + dateStr.format(LocalDate.now()));
        Query query = new Query(params);
        List<SysJpushRecordEntity> sysJpushRecordList = sysJpushRecordService.queryList(query);

        //x轴list
        List<String> xList = new ArrayList<>();
        xList.add(dateStr.format(LocalDate.now().minusDays(6)));
        xList.add(dateStr.format(LocalDate.now().minusDays(5)));
        xList.add(dateStr.format(LocalDate.now().minusDays(4)));
        xList.add(dateStr.format(LocalDate.now().minusDays(3)));
        xList.add(dateStr.format(LocalDate.now().minusDays(2)));
        xList.add(dateStr.format(LocalDate.now().minusDays(1)));
        xList.add(dateStr.format(LocalDate.now()));
        //每条数据的标题
        List<String> dataTitleList = new ArrayList<>();
        dataTitleList.add("ios成功数");
        dataTitleList.add("ios失败数");
        dataTitleList.add("android成功数");
        dataTitleList.add("android失败数");
        dataTitleList.add("总成功数");
        dataTitleList.add("总失败数");


        List<Integer> iosSuccess = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        List<Integer> iosFail = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        List<Integer> androidSuccess = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        List<Integer> androidFail = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        List<Integer> allSuccess = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        List<Integer> allFail = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
        for (SysJpushRecordEntity sysJpushRecordEntity : sysJpushRecordList) {
            boolean isPasss = false;
            if (sysJpushRecordEntity.getResult() != null && sysJpushRecordEntity.getResult().intValue() == 1) {
                isPasss = true;
            }
            //索引值
            int index = xList.indexOf(dateStr.format(sysJpushRecordEntity.getCreateDate()));
            if (index != -1 && sysJpushRecordEntity.getPlatform() != null) {
                if (sysJpushRecordEntity.getPlatform().contains("ios")) {
                    if (isPasss) {
                        iosSuccess.set(index, iosSuccess.get(index) + 1);
                    } else {
                        iosFail.set(index, iosFail.get(index) + 1);
                    }
                }
                if (sysJpushRecordEntity.getPlatform().contains("android")) {
                    if (isPasss) {
                        androidSuccess.set(index, androidSuccess.get(index) + 1);
                    } else {
                        androidFail.set(index, androidFail.get(index) + 1);
                    }
                }
                if (isPasss) {
                    allSuccess.set(index, allSuccess.get(index) + 1);
                } else {
                    allFail.set(index, allFail.get(index) + 1);
                }
            }
        }

        //颜色列表
        List<String> colorList = new ArrayList<>();
        colorList.add("#669900");
        colorList.add("#FF6600");
        colorList.add("#669999");
        colorList.add("#FF6699");
        colorList.add("#00FF00");
        colorList.add("#FF0000");

        //数据列表
        List<JSONObject> dataList = new ArrayList<>();
        JSONObject item1 = new JSONObject();
        item1.put("name", "ios成功数");
        item1.put("type", chartsType);
        item1.put("stack", "ios成功数");
        item1.put("data", iosSuccess);
        JSONObject item1Config = JSON.parseObject("{normal:{show: true,position: 'top', textStyle: {color: '" + colorList.get(0) + "'}}}");
        item1.put("label", item1Config);

        JSONObject item2 = new JSONObject();
        item2.put("name", "ios失败数");
        item2.put("type", chartsType);
        item2.put("stack", "ios失败数");
        item2.put("data", iosFail);
        JSONObject item2Config = JSON.parseObject("{normal:{show: true,position: 'top', textStyle: {color: '" + colorList.get(1) + "'}}}");
        item2.put("label", item2Config);

        JSONObject item3 = new JSONObject();
        item3.put("name", "android成功数");
        item3.put("type", chartsType);
        item3.put("stack", "android成功数");
        item3.put("data", androidSuccess);
        JSONObject item3Config = JSON.parseObject("{normal:{show: true,position: 'top', textStyle: {color: '" + colorList.get(2) + "'}}}");
        item3.put("label", item3Config);

        JSONObject item4 = new JSONObject();
        item4.put("name", "android失败数");
        item4.put("type", chartsType);
        item4.put("stack", "android失败数");
        item4.put("data", androidFail);
        JSONObject item4Config = JSON.parseObject("{normal:{show: true,position: 'top', textStyle: {color: '" + colorList.get(3) + "'}}}");
        item4.put("label", item4Config);

        JSONObject item5 = new JSONObject();
        item5.put("name", "总成功数");
        item5.put("type", chartsType);
        item5.put("stack", "总成功数");
        item5.put("data", allSuccess);
        JSONObject item5Config = JSON.parseObject("{normal:{show: true,position: 'top', textStyle: {color: '" + colorList.get(4) + "'}}}");
        item5.put("label", item5Config);

        JSONObject item6 = new JSONObject();
        item6.put("name", "总失败数");
        item6.put("type", chartsType);
        item6.put("stack", "总失败数");
        item6.put("data", allFail);
        JSONObject item6Config = JSON.parseObject("{normal:{show: true,position: 'top', textStyle: {color: '" + colorList.get(5) + "'}}}");
        item6.put("label", item6Config);

        dataList.add(item1);
        dataList.add(item2);
        dataList.add(item3);
        dataList.add(item4);
        dataList.add(item5);
        dataList.add(item6);

        resultMap.put("title", "最近七天极光推送记录");
        resultMap.put("xList", xList);
        resultMap.put("dataTitleList", dataTitleList);
        resultMap.put("dataList", dataList);
        resultMap.put("colorList", colorList);


        return R.ok().putObj("chartData", resultMap);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:jpushrecord:info")
    public R info(@PathVariable("id") Long id) {
        SysJpushRecordEntity sysJpushRecord = sysJpushRecordService.queryObject(id);

        return R.ok().putObj("sysJpushRecord", sysJpushRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:jpushrecord:save")
    public R save(@RequestBody SysJpushRecordEntity sysJpushRecord) {

        //sysJpushRecord.setCreatetime(new Date());
        // sysJpushRecord.setCreateuser(getUserId().toString());
        ValidatorUtils.validateEntity(sysJpushRecord);
        sysJpushRecordService.save(sysJpushRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:jpushrecord:update")
    public R update(@RequestBody SysJpushRecordEntity sysJpushRecord) {

        //sysJpushRecord.setModifyuser(getUserId().toString());
        //sysJpushRecord.setModifytime(new Date());

        ValidatorUtils.validateEntity(sysJpushRecord);
        sysJpushRecordService.update(sysJpushRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:jpushrecord:delete")
    public R delete(@RequestBody Long[] ids) {

        //一下两种删除根据情况自己选择

        //物理删除
        //sysJpushRecordService.deleteBatch(ids);

        //逻辑删除
        //List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        //sysJpushRecordService.multiOperate(idStr, Constant.Status.delete);

        return R.ok();
    }


    /**
     * 导出指定行成为Excel格式
     * modelIds客户端使用逗号分割，这里就要用数组接收
     */
    @RequestMapping("/exportsExcel")
    @RequiresPermissions("sys:jpushrecord:list")
    public void exportsExcel(@RequestParam String[] modelIds, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        Query params = new Query();
        params.put("id_IN", Lists.newArrayList(modelIds));
        List<SysJpushRecordEntity> vos = sysJpushRecordService.queryList(params);
        sysJpushRecordService.exportsToExcels(vos, request, response, false);
    }

    /**
     * 导出所有数据成excel格式
     */
    @RequestMapping("/exportsExcelAll")
    @RequiresPermissions("sys:jpushrecord:list")
    @SuppressWarnings("unchecked")
    public void exportsExcelAll(@RequestParam Map<String, Object> params, @RequestParam Boolean isCvs, HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        if (params.containsKey("searchParams")) {
            params.putAll(JSON.parseObject(params.get("searchParams") + "", Map.class));
            params.remove("searchParams");
        }
        for (Map.Entry<String, Object> item : params.entrySet()) {
            Object value = (item.getValue() == null || item.getValue().toString().equalsIgnoreCase("null")) ? "" : item.getValue();
            item.setValue(value);
        }

        List<SysJpushRecordEntity> vos = sysJpushRecordService.queryList(params);
        sysJpushRecordService.exportsToExcels(vos, request, response, isCvs == null ? false : isCvs);
    }
}
