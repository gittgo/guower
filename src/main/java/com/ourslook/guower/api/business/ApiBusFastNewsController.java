//package com.ourslook.guower.api.business;
//
//import com.ourslook.guower.entity.business.BusFastNewsEntity;
//import com.ourslook.guower.service.business.BusFastNewsService;
//import com.ourslook.guower.utils.Query;
//import com.ourslook.guower.utils.result.XaResult;
//import com.ourslook.guower.utils.validator.ValidatorUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * 快报表
// *
// * @author dazer
// * @email ab601026460@qq.com
// * @date 2018-06-29 10:50:12
// */
//@RestController
//@CrossOrigin
//@RequestMapping("/api/busfastnews")
//@Api(value = "busfastnews", description = "快报表", position = 1)
//@SuppressWarnings("all")
//public class ApiBusFastNewsController {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private BusFastNewsService busFastNewsService;
//
//    /**
//     * 查询所有快报表
//     */
//    @PostMapping("list")
//    @ApiOperation(value = "查询所有快报表信息")
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
//    @ResponseBody
//    public XaResult<List<BusFastNewsEntity>> list(
//        @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
//        @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
//        HttpServletRequest request
//    ) {
//        XaResult<List<BusFastNewsEntity>> xr = new XaResult<>();
//        Query query = new Query(pageCurrent,pageSize);
//        xr.setObject(busFastNewsService.queryList(query));
//        return xr;
//    }
//
//    /**
//     * 更新和保存
//     */
//    @ApiOperation(value = "快报表更新和保存", notes = "快报表更新和保存", position = 1)
//    @ResponseBody
//    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
//    public XaResult<BusFastNewsEntity> saveOrUpdate(
//                        @ApiParam("编号,字段名:id,编号,如：") @RequestParam(defaultValue = "", required = false) Integer id,
//                        @ApiParam("标题,字段名:title,标题,如：") @RequestParam(defaultValue = "", required = false) String title,
//                        @ApiParam("正文【详情】,字段名:mainText,正文【详情】,如：") @RequestParam(defaultValue = "", required = false) String mainText,
//                        @ApiParam("发布人,字段名:releaseUserId,发布人,如：") @RequestParam(defaultValue = "", required = false) Integer releaseUserId,
//                        @ApiParam("发布时间,字段名:releaseDate,发布时间,如：") @RequestParam(defaultValue = "", required = false) LocalDateTime releaseDate,
//                        @ApiParam("排序,字段名:sort,排序,如：") @RequestParam(defaultValue = "", required = false) Integer sort,
//                        @ApiParam("排序到期时间,字段名:sortTime,排序到期时间,如：") @RequestParam(defaultValue = "", required = false) LocalDateTime sortTime,
//                        @ApiParam("果味指数,字段名:guowerIndex,果味指数,如：") @RequestParam(defaultValue = "", required = false) Integer guowerIndex,
//                        @ApiParam("是否为7*24小时快讯【0.默认快报  1.7*24小时快讯】,字段名:isNewsFlash,是否为7*24小时快讯【0.默认快报  1.7*24小时快讯】,如：") @RequestParam(defaultValue = "", required = false) Integer isNewsFlash,
//                        @ApiParam("阅读量,字段名:lookTimes,阅读量,如：") @RequestParam(defaultValue = "", required = false) Integer lookTimes,
//                        @ApiParam("暂留,字段名:fastnewsRemarks1,暂留,如：") @RequestParam(defaultValue = "", required = false) String fastnewsRemarks1,
//                        @ApiParam("暂留,字段名:fastnewsRemarks2,暂留,如：") @RequestParam(defaultValue = "", required = false) String fastnewsRemarks2,
//                        @ApiParam("暂留,字段名:fastnewsRemarks3,暂留,如：") @RequestParam(defaultValue = "", required = false) String fastnewsRemarks3,
//                    HttpServletRequest request
//    ) {
//        XaResult<BusFastNewsEntity> xr = new XaResult<>();
//        BusFastNewsEntity  busFastNewsEntity = new BusFastNewsEntity();
//                    busFastNewsEntity.setId(id);
//                    busFastNewsEntity.setTitle(title);
//                    busFastNewsEntity.setMainText(mainText);
//                    busFastNewsEntity.setReleaseUserId(releaseUserId);
//                    busFastNewsEntity.setReleaseDate(releaseDate);
//                    busFastNewsEntity.setSort(sort);
//                    busFastNewsEntity.setSortTime(sortTime);
//                    busFastNewsEntity.setGuowerIndex(guowerIndex);
//                    busFastNewsEntity.setIsNewsFlash(isNewsFlash);
//                    busFastNewsEntity.setLookTimes(lookTimes);
//                    busFastNewsEntity.setFastnewsRemarks1(fastnewsRemarks1);
//                    busFastNewsEntity.setFastnewsRemarks2(fastnewsRemarks2);
//                    busFastNewsEntity.setFastnewsRemarks3(fastnewsRemarks3);
//                ValidatorUtils.validateEntity(busFastNewsEntity);
//
//        if (id == null) {
//            busFastNewsService.save(busFastNewsEntity);
//        } else {
//            busFastNewsService.update(busFastNewsEntity);
//        }
//        xr.setObject(busFastNewsEntity);
//        return xr;
//    }
//
//   /**
//    * 信息
//    */
//    @ApiOperation(value="快报表详情",notes="详情")
//    @ResponseBody
//    @RequestMapping(value="findBusFastNewsByid",method=RequestMethod.POST)
//    public XaResult<BusFastNewsEntity> info (
//            @ApiParam("主键,字段名:id,必填") @RequestParam(value = "id") Integer id
//     ){
//        BusFastNewsEntity busFastNews = busFastNewsService.queryObject(id);
//        return new XaResult().success(busFastNews);
//    }
//
//   /**
//    * 删除
//    */
//    @ApiOperation(value="根据id修改busfastnews状态",notes="修改busfastnews状态,当返回的code=1时，保存成功后object返回对象")
//    @ResponseBody
//    @RequestMapping(value="operateBusFastNewsByid",method=RequestMethod.POST)
//    public XaResult info (
//        @ApiParam("编号,字段名:modelIds,必填") @RequestParam(value = "modelId") Integer[] ids,
//        @ApiParam("操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作") @RequestParam(defaultValue = "3") Integer status
//    ) {
//        busFastNewsService.deleteBatch(ids);
//        return new XaResult<>().success(null);
//    }
//}
