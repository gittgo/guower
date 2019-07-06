package com.ourslook.guower.api.web.business;

import com.ourslook.guower.entity.score.InfCurrencyEntity;
import com.ourslook.guower.entity.score.InfExchangeRecordEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.score.InfCurrencyService;
import com.ourslook.guower.service.score.InfExchangeRecordService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.vo.score.InfExchangeRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api/p/currency")
@RestController
@CrossOrigin
@Api(value = "currency", description = "货币兑换相关接口", position = 8)
public class WebApiBusCurrencyController {

    @Autowired
    private InfCurrencyService currencyService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private InfExchangeRecordService exchangeRecordService;

    /**
     * 货币列表
     * @param pageCurrent
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("货币列表")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<Map<String, Object>> list(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));

        Query query = new Query(pageCurrent, pageSize);
        List<InfCurrencyEntity> currencyEntities = currencyService.queryList(query);

        int total = currencyService.queryTotal(query);
        Map<String, Object> map = new HashMap<>();
        map.put("list", currencyEntities);
        map.put("total", total);

        XaResult<Map<String, Object>> xaResult = new XaResult<>();
        return xaResult.setObject(map);
    }

    /**
     * 货币兑换提交
     * @param currencyId
     * @param purseAddress
     * @param request
     * @return
     */
    @PostMapping("/exchangeRecord")
    @ApiOperation("提交货币兑换")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<String> exchangeRecord(
            @ApiParam("货币id，currencyId") @RequestParam(value = "currencyId") Integer currencyId,
            @ApiParam("钱包地址，purseAddress") @RequestParam(value = "purseAddress") String purseAddress,
            HttpServletRequest request
    ){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));

        InfExchangeRecordEntity exchangeRecordEntity = new InfExchangeRecordEntity();
        exchangeRecordEntity.setCurrencyId(currencyId);
        exchangeRecordEntity.setPurseAddress(purseAddress);
        exchangeRecordEntity.setCreateDate(LocalDateTime.now());
        exchangeRecordEntity.setUserId(userEntity.getId());
        exchangeRecordEntity.setState(Constant.ExchangeRecordStatus.CHANGE_ING);
        exchangeRecordService.saveExchangeRecord(exchangeRecordEntity);

        XaResult<String> xaResult = new XaResult<>();
        return xaResult.setObject("提交成功");
    }

    /**
     * 兑换记录
     * @param pageCurrent
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/getExchangeRecordList")
    @ResponseBody
    @ApiOperation("兑换记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<Map<String, Object>> getExchangeRecordList(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));
        Query query = new Query(pageCurrent, pageSize);
        query.put("sidx", "create_date");
        query.put("order", "desc");
        query.put("userId", userEntity.getId() + "");
        List<InfExchangeRecordVo> exchangeRecordVos = exchangeRecordService.queryVoList(query);

        int total = exchangeRecordService.queryTotal(query);
        Map<String, Object> map = new HashMap<>();
        map.put("list", exchangeRecordVos);
        map.put("total", total);

        XaResult<Map<String, Object>> xaResult = new XaResult<>();
        return xaResult.setObject(map);
    }
}
