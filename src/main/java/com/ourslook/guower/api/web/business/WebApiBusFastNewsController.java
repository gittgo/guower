package com.ourslook.guower.api.web.business;

import com.ourslook.guower.entity.business.BusFastNewsEntity;
import com.ourslook.guower.service.business.BusFastNewsService;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
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
 * 快报表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@RestController
@CrossOrigin
@RequestMapping("/api/p/busfastnews")
@Api(value = "Pbusfastnews", description = "web端快报相关接口", position = 1)
@SuppressWarnings("all")
public class WebApiBusFastNewsController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusFastNewsService busFastNewsService;

    /**
     * 查询所有快报表
     */
    @PostMapping("list")
    @ApiOperation(value = "快报列表")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusFastNewsEntity>> list(
            @ApiParam(value = "是否是7*24快讯，字段名:isNewsFlash, 传1，默认为0") @RequestParam(value = "isNewsFlash",defaultValue = "0") Integer isNewsFlash,
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        XaResult<List<BusFastNewsEntity>> xr = new XaResult<>();
        Query query = new Query(pageCurrent, pageSize);
        if (XaUtils.isNotEmpty(isNewsFlash) && isNewsFlash == 1) {
            query.put("isNewsFlash", isNewsFlash);
        }
        query.put("sidx", "release_date");
        query.put("order", "desc");
        xr.setObject(busFastNewsService.queryList(query));
        return xr;
    }


}
