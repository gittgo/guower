package com.ourslook.guower.api.app;

import com.ourslook.guower.entity.score.InfCurrencyEntity;
import com.ourslook.guower.entity.score.InfExchangeRecordEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.score.InfCurrencyService;
import com.ourslook.guower.service.score.InfExchangeRecordService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.vo.score.InfExchangeRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 兑换表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@CrossOrigin
@RestController
@RequestMapping("/api/app/currency")
@Api(value = "currency", description = "兑换表")
public class AppApiCurrencyController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenService tokenService;
    @Autowired
    private InfExchangeRecordService infExchangeRecordService;
    @Autowired
    private InfCurrencyService infCurrencyService;
    @Resource
    private BeanMapper beanMapper;

    /**
     * 兑换列表
     */
    @IgnoreAuth
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation(value = "兑换列表")
    public XaResult<List<InfCurrencyEntity>> list(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        Query query = new Query(pageCurrent,pageSize);
        query.put("delFlag","1");
        List<InfCurrencyEntity> infCurrencyList = infCurrencyService.queryList(query);

        XaResult<List<InfCurrencyEntity>> xr = new XaResult<>();
        xr.setObject(infCurrencyList);
        return xr;
    }

    /**
     * 兑换
     */
    @PostMapping("/exchange")
    @ApiOperation(value = "兑换")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    @ResponseBody
    public XaResult<InfExchangeRecordEntity> exchange(
            @ApiParam("货币id,字段名:id") @RequestParam(defaultValue = "") Integer currencyId,
            @ApiParam("钱包地址,字段名:purseAddress") @RequestParam(defaultValue = "") String purseAddress,
            HttpServletRequest request
    ) {
        AbstractAssert.isBlank(purseAddress, "钱包地址不能为空!");
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        InfCurrencyEntity currencyEntity = infCurrencyService.queryObject(currencyId);
        if(XaUtils.isEmpty(currencyEntity) || currencyEntity.getDelFlag() == 0){
            throw new RRException("未找到该货币");
        }else if(currencyEntity.getCount() <= 0){
            throw new RRException("库存不足");
        }else if(userEntity.getScore() < currencyEntity.getScore()){
            throw new RRException("积分不足");
        }
        InfExchangeRecordEntity infExchangeRecordEntity = new InfExchangeRecordEntity();
        infExchangeRecordEntity.setUserId(userEntity.getId());
        infExchangeRecordEntity.setState(Constant.ExchangeRecordStatus.CHANGE_ING);
        infExchangeRecordEntity.setCurrencyId(currencyId);
        infExchangeRecordEntity.setPurseAddress(purseAddress);
        infExchangeRecordEntity.setCreateDate(LocalDateTime.now());
        infExchangeRecordEntity.setScore(currencyEntity.getScore());
        //兑换货币
        infExchangeRecordService.exchangeCurrency(infExchangeRecordEntity);
        return new XaResult<InfExchangeRecordEntity>().setObject(infExchangeRecordEntity);
    }

    /**
     * 兑换记录
     */
    @GetMapping("/exchangeRecord")
    @ResponseBody
    @ApiOperation("兑换记录")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<List<InfExchangeRecordVo>> getExchangeRecordList(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));
        Query query = new Query(pageCurrent, pageSize);
        query.put("userId", userEntity.getId());
        List<InfExchangeRecordVo> exchangeRecordVos = infExchangeRecordService.queryVoList(query);
        XaResult<List<InfExchangeRecordVo>> xaResult = new XaResult<>();
        return xaResult.setObject(exchangeRecordVos);
    }

}
