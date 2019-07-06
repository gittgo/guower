package com.ourslook.guower.api.app;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import com.ourslook.guower.service.business.BusAdvertisementService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.result.XaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 广告表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@RestController
@CrossOrigin
@RequestMapping("/api/app/advertisement")
@Api(value = "advertisement", description = "广告表")
@SuppressWarnings("all")
public class AppApiAdvertisementController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusAdvertisementService busAdvertisementService;

    /**
     * 首页banner
     */
    @PostMapping("list")
    @ApiOperation(value = "首页banner")
    @IgnoreAuth
    @ResponseBody
    public XaResult<List<BusAdvertisementEntity>> list(
        @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
        @ApiParam("页长,字段名:pageSize,默认5") @RequestParam(defaultValue = "5") Integer pageSize,
        HttpServletRequest request
    ) {
        XaResult<List<BusAdvertisementEntity>> xr = new XaResult<>();
        Query query = new Query(pageCurrent,pageSize);
        query.put("advertisementType",Constant.AdvertisementType.TYPE_ADVERTISING_APP_BANNER);
        xr.setObject(busAdvertisementService.queryList(query));
        return xr;
    }

    /**
     * 文章详情广告
     */
    @PostMapping("listByNewsDetails")
    @ApiOperation(value = "文章详情广告")
    @IgnoreAuth
    @ResponseBody
    public XaResult<List<BusAdvertisementEntity>> listByNewsDetails(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认3") @RequestParam(defaultValue = "3") Integer pageSize,
            HttpServletRequest request
    ) {
        XaResult<List<BusAdvertisementEntity>> xr = new XaResult<>();
        Query query = new Query(pageCurrent,pageSize);
        query.put("advertisementType",Constant.AdvertisementType.TYPE_ADVERTISING_APP_DETAILS_BANNER);
        xr.setObject(busAdvertisementService.queryList(query));
        return xr;
    }

   /**
    * 信息
    */
    @IgnoreAuth
    @ApiOperation(value="广告表详情",notes="详情")
    @ResponseBody
    @RequestMapping(value="findBusAdvertisementByid",method=RequestMethod.POST)
    public XaResult<BusAdvertisementEntity> info (
            @ApiParam("主键,字段名:id,必填") @RequestParam(value = "id") Integer id
     ){
        BusAdvertisementEntity busAdvertisement = busAdvertisementService.queryObject(id);
        busAdvertisement.setLookTimes(busAdvertisement.getLookTimes()==null?1:busAdvertisement.getLookTimes()+1);
        busAdvertisementService.update(busAdvertisement);
        return new XaResult().success(busAdvertisement);
    }
}
