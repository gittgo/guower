package com.ourslook.guower.api;

import com.ourslook.guower.entity.SysConfigEntity;
import com.ourslook.guower.entity.common.*;
import com.ourslook.guower.service.SysConfigService;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.common.*;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import com.ourslook.guower.vo.InfDictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 配置相关接口
 *
 * @author dazer
 * @date 2017/12/12 上午11:44
 */
@RestController
@CrossOrigin
@RequestMapping("/api/cfg")
@Api(value = "cfg", description = "config相关接口", position = 1)
public class ApiCfgController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenService tokenService;
    @Autowired
    private InfFeedbackService infFeedbackService;
    @Autowired
    private InfVersionInfoService infVersionInfoService;
    @Autowired
    private InfDictInfoService infDictInfoService;
    @Autowired
    private InfoDitcypeInfoService infoDitcypeInfoService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private InfClauseInfoService tInfClauseInfoService;


    /**
     * 参数表接口调用
     *
     * @see Constant.SysConfigValue
     */
    @GetMapping("syscoflist")
    @ApiOperation(value = "参数表接口调用")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<SysConfigEntity>> syscoflist(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "" + Short.MAX_VALUE) Integer pageSize,
            @ApiParam("系统配置代码,字段名:sysConfigCode,多个用英文逗号分割，也可以是单个，字段名:sysConfigCode;选择传入，如果传入值返回对应code的配置信息")
            @RequestParam(value = "sysConfigCodes", required = false) List<String> sysConfigCodes
    ) {
        XaResult<List<SysConfigEntity>> xr = new XaResult<>();
        Query query = new Query(pageCurrent, pageSize);
        if (XaUtils.isNotEmpty(sysConfigCodes)) {
            query.put("code_IN", sysConfigCodes);
        }
        List<SysConfigEntity> configList = sysConfigService.queryList(query);
        return xr.setObject(configList);
    }

    /**
     * 字典表接口调用
     */
    @GetMapping("dictTypes")
    @ApiOperation(value = "查询所有的字典类型")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<InfDitcypeInfoEntity>> dictTypes(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        XaResult<List<InfDitcypeInfoEntity>> xr = new XaResult<>();
        Query query = new Query(pageCurrent, pageSize);
        List<InfDitcypeInfoEntity> infoEntities = infoDitcypeInfoService.queryList(query);
        return xr.setObject(infoEntities);
    }

    @GetMapping("dictInfo")
    @ApiOperation(value = "查询字典，只能查询单个字典")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<InfDictVo>> dictInfo(
            @ApiParam("字典类型code,字段名:code") @RequestParam(value = "code", required = false, defaultValue = "TYPE_CLAUS") String dictTypeCode) {
        //如不提供名称表示获取全部一级字典
        XaResult<List<InfDictVo>> xr = new XaResult<>();
        Query query = new Query();
        if (XaUtils.isNotEmpty(dictTypeCode)) {
            query.put("type", dictTypeCode);
        }
        List<InfDictVo> dictVos = infDictInfoService.queryVoList(query);
        dictVos.sort(Comparator.comparing(InfDictInfoEntity::getSort));
        xr.setObject(infDictInfoService.queryVoList(query));
        return xr;
    }

    @GetMapping("dictInfos")
    @ApiOperation(value = "查询字典,支持查询多个字典")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<InfDictVo>> dictInfos(
            @ApiParam(value = "字典类型dictTypeCodes,多个用英文逗号分割，也可以是单个，字段名:dictTypeCodes", defaultValue = "TYPE_CLAUS,TYPE_BANNER_ADVERTISING,TYPE_USER_TYPE") @RequestParam(value = "dictTypeCodes", required = false) List<String> dictTypeCodes) {
        XaResult<List<InfDictVo>> xr = new XaResult<>();
        Query query = new Query();
        if (XaUtils.isNotEmpty(dictTypeCodes)) {
            query.put("type_IN", dictTypeCodes);
        }
        List<InfDictVo> dictVos = infDictInfoService.queryVoList(query);
        dictVos = dictVos.stream().sorted(Comparator.comparing(InfDictVo::getCode).thenComparing(InfDictVo::getSort)).collect(Collectors.toList());
        xr.setObject(dictVos);
        return xr;
    }

    /**
     * 用户意见反馈😜😜
     * 如果，数据库不支持，请参见连接设置：https://blog.csdn.net/ab601026460/article/details/17886955 搜表情
     *
     * @see Constant.DeviceConstant
     */
    @PostMapping("userFeedback")
    @ApiOperation(value = "用户反馈意见 | 意见反馈")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, defaultValue = "e10d87b7-5bb6-4719-b850-27979a567810")
    public XaResult<String> userFeedback(
            @ApiParam(value = "反馈内容,字段名:feedbackcontent,必填", defaultValue = "app登录不太方便，是否能够增加短信验证码登录😜|\uD83D\uDE1C;😪😒😒😓😌😭😒😓😔🍞🍜🍘🍘☕🍝🍜🐱🌹🐰☔🌊🌅💦🐾🐛🌜🐛☁🐾") @RequestParam(value = "feedbackcontent") String feedbackcontent,
            @ApiParam(value = "来源平台,字段名:feedbackfsource,必填，取值，请查看常量类Constant.DeviceConstant的code取值.;1:IOS 2:ANDROID 其他具体参考常量类", defaultValue = "2") @RequestParam(value = "feedbackfsource") String feedbackfsource,
            @ApiParam(value = "联系方式,字段名:tel", defaultValue = "18049531390") @RequestParam(value = "tel", required = false) String tel,
            HttpServletRequest request
    ) {
        InfFeedbackEntity entity = new InfFeedbackEntity();
        entity.setTel(tel);
        entity.setCreateDate(new Date());
        entity.setFeedbackuserid(tokenService.queryUseIdByRequest(request));
        entity.setFeedbackcontent(feedbackcontent);
        entity.setFeedbacksource(feedbackfsource);
        entity.setStatus(Constant.Status.invalid);
        ValidatorUtils.validateEntity(entity);
        infFeedbackService.save(entity);
        XaResult<String> sm = new XaResult<>();
        sm.success("反馈成功");
        return sm;
    }

    /**
     * 版本控制
     *
     * @author cyl
     * @see Constant.DeviceConstant
     */
    @ApiOperation(value = "版本更新", notes = "根据平台传入指定的平台编号 设备：ios、android、web 进行版本查询 与比对")
    @RequestMapping(value = "findNewInfVersionInfo", method = {RequestMethod.GET})
    @IgnoreAuth
    public XaResult<Serializable> findNewInfVersionInfo(
            @ApiParam(value = "平台编号,字段名:device, 取值，请查看常量类Constant.DeviceConstant的code取值.;1:IOS 2:ANDROID 其他具体参考常量类") @RequestParam(value = "device") String device
    ) {
        XaResult<Serializable> xr = new XaResult<>();
        InfVersionInfoEntity infVersionInfoEntity = infVersionInfoService.queryNewObject(device);
        if (infVersionInfoEntity != null) {
            xr.success(infVersionInfoEntity);
        } else {
            xr.error("当前已经是最新版本!");
        }
        return xr;
    }


    /**
     * 所有文案
     */
    @GetMapping("clauseList")
    @ApiOperation(value = "所有文案")
    @IgnoreAuth
    @ResponseBody
    public XaResult<List<InfClauseInfoEntity>> clauseList(
    ) {
        XaResult<List<InfClauseInfoEntity>> xr = new XaResult<>();
        List<InfClauseInfoEntity> list = tInfClauseInfoService.queryList(new HashMap<>());
        xr.setObject(list);
        return xr;
    }

    /**
     * 文案
     */
    @GetMapping("clause")
    @ApiOperation(value = "文案")
    @IgnoreAuth
    @ResponseBody
    public XaResult<InfClauseInfoEntity> clause(
            @ApiParam("文案类型") @RequestParam(value = "ClauseType",required = true) String ClauseType
    ) {
        Map map = new HashMap<>();
        map.put("clausetype",ClauseType);
        XaResult<InfClauseInfoEntity> xr = new XaResult<>();
        List<InfClauseInfoEntity> list = tInfClauseInfoService.queryList(map);
        InfClauseInfoEntity clauseInfoEntity = null;
        if(list != null && list.size() > 0){
            clauseInfoEntity = list.get(0);
        }
        xr.setObject(clauseInfoEntity);
        return xr;
    }

}
