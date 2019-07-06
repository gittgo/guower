//package com.ourslook.guower.api.business;
//
//import com.ourslook.guower.entity.business.BusAdvertisementEntity;
//import com.ourslook.guower.service.business.BusAdvertisementService;
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
// * 广告表
// *
// * @author dazer
// * @email ab601026460@qq.com
// * @date 2018-06-29 10:50:12
// */
//@RestController
//@CrossOrigin
//@RequestMapping("/api/busadvertisement")
//@Api(value = "busadvertisement", description = "广告表", position = 1)
//@SuppressWarnings("all")
//public class ApiBusAdvertisementController {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private BusAdvertisementService busAdvertisementService;
//
//    /**
//     * 查询所有广告表
//     */
//    @PostMapping("list")
//    @ApiOperation(value = "查询所有广告表信息")
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
//    @ResponseBody
//    public XaResult<List<BusAdvertisementEntity>> list(
//        @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
//        @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
//        HttpServletRequest request
//    ) {
//        XaResult<List<BusAdvertisementEntity>> xr = new XaResult<>();
//        Query query = new Query(pageCurrent,pageSize);
//        xr.setObject(busAdvertisementService.queryList(query));
//        return xr;
//    }
//
//    /**
//     * 更新和保存
//     */
//    @ApiOperation(value = "广告表更新和保存", notes = "广告表更新和保存", position = 1)
//    @ResponseBody
//    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
//    public XaResult<BusAdvertisementEntity> saveOrUpdate(
//                        @ApiParam("编号,字段名:id,编号,如：") @RequestParam(defaultValue = "", required = false) Integer id,
//                        @ApiParam("标题,字段名:title,标题,如：") @RequestParam(defaultValue = "", required = false) String title,
//                        @ApiParam("副标题【简介】,字段名:smallTitle,副标题【简介】,如：") @RequestParam(defaultValue = "", required = false) String smallTitle,
//                        @ApiParam("大图标,字段名:image,大图标,如：") @RequestParam(defaultValue = "", required = false) String image,
//                        @ApiParam("小图标,字段名:smallImage,小图标,如：") @RequestParam(defaultValue = "", required = false) String smallImage,
//                        @ApiParam("正文【详情】,字段名:mainText,正文【详情】,如：") @RequestParam(defaultValue = "", required = false) String mainText,
//                        @ApiParam("广告类型,字段名:advertisementType,广告类型,如：") @RequestParam(defaultValue = "", required = false) String advertisementType,
//                        @ApiParam("阅读量,字段名:lookTimes,阅读量,如：") @RequestParam(defaultValue = "", required = false) Integer lookTimes,
//                        @ApiParam("发布人,字段名:releaseUserId,发布人,如：") @RequestParam(defaultValue = "", required = false) Integer releaseUserId,
//                        @ApiParam("发布时间,字段名:releaseDate,发布时间,如：") @RequestParam(defaultValue = "", required = false) LocalDateTime releaseDate,
//                        @ApiParam("跳转方式【1.链接  2.视频  3.富文本  4.新闻文章】,字段名:jumpType,跳转方式【1.链接  2.视频  3.富文本  4.新闻文章】,如：") @RequestParam(defaultValue = "", required = false) Integer jumpType,
//                        @ApiParam("跳转地址,字段名:jumpUrl,跳转地址,如：") @RequestParam(defaultValue = "", required = false) String jumpUrl,
//                        @ApiParam("跳转新闻文章id,字段名:jumpNewsId,跳转新闻文章id,如：") @RequestParam(defaultValue = "", required = false) Integer jumpNewsId,
//                        @ApiParam("暂留,字段名:advertisemenRemarks1,暂留,如：") @RequestParam(defaultValue = "", required = false) String advertisemenRemarks1,
//                        @ApiParam("暂留,字段名:advertisemenRemarks2,暂留,如：") @RequestParam(defaultValue = "", required = false) String advertisemenRemarks2,
//                        @ApiParam("暂留,字段名:advertisemenRemarks3,暂留,如：") @RequestParam(defaultValue = "", required = false) String advertisemenRemarks3,
//                        @ApiParam("排序,字段名:sort,排序,如：") @RequestParam(defaultValue = "", required = false) Integer sort,
//                        @ApiParam("排序到期时间,字段名:sortTime,排序到期时间,如：") @RequestParam(defaultValue = "", required = false) LocalDateTime sortTime,
//                    HttpServletRequest request
//    ) {
//        XaResult<BusAdvertisementEntity> xr = new XaResult<>();
//        BusAdvertisementEntity  busAdvertisementEntity = new BusAdvertisementEntity();
//                    busAdvertisementEntity.setId(id);
//                    busAdvertisementEntity.setTitle(title);
//                    busAdvertisementEntity.setSmallTitle(smallTitle);
//                    busAdvertisementEntity.setImage(image);
//                    busAdvertisementEntity.setSmallImage(smallImage);
//                    busAdvertisementEntity.setMainText(mainText);
//                    busAdvertisementEntity.setAdvertisementType(advertisementType);
//                    busAdvertisementEntity.setLookTimes(lookTimes);
//                    busAdvertisementEntity.setReleaseUserId(releaseUserId);
//                    busAdvertisementEntity.setReleaseDate(releaseDate);
//                    busAdvertisementEntity.setJumpType(jumpType);
//                    busAdvertisementEntity.setJumpUrl(jumpUrl);
//                    busAdvertisementEntity.setJumpNewsId(jumpNewsId);
//                    busAdvertisementEntity.setAdvertisemenRemarks1(advertisemenRemarks1);
//                    busAdvertisementEntity.setAdvertisemenRemarks2(advertisemenRemarks2);
//                    busAdvertisementEntity.setAdvertisemenRemarks3(advertisemenRemarks3);
//                    busAdvertisementEntity.setSort(sort);
//                    busAdvertisementEntity.setSortTime(sortTime);
//                ValidatorUtils.validateEntity(busAdvertisementEntity);
//
//        if (id == null) {
//            busAdvertisementService.save(busAdvertisementEntity);
//        } else {
//            busAdvertisementService.update(busAdvertisementEntity);
//        }
//        xr.setObject(busAdvertisementEntity);
//        return xr;
//    }
//
//   /**
//    * 信息
//    */
//    @ApiOperation(value="广告表详情",notes="详情")
//    @ResponseBody
//    @RequestMapping(value="findBusAdvertisementByid",method=RequestMethod.POST)
//    public XaResult<BusAdvertisementEntity> info (
//            @ApiParam("主键,字段名:id,必填") @RequestParam(value = "id") Integer id
//     ){
//        BusAdvertisementEntity busAdvertisement = busAdvertisementService.queryObject(id);
//        return new XaResult().success(busAdvertisement);
//    }
//
//   /**
//    * 删除
//    */
//    @ApiOperation(value="根据id修改busadvertisement状态",notes="修改busadvertisement状态,当返回的code=1时，保存成功后object返回对象")
//    @ResponseBody
//    @RequestMapping(value="operateBusAdvertisementByid",method=RequestMethod.POST)
//    public XaResult info (
//        @ApiParam("编号,字段名:modelIds,必填") @RequestParam(value = "modelId") Integer[] ids,
//        @ApiParam("操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作") @RequestParam(defaultValue = "3") Integer status
//    ) {
//        busAdvertisementService.deleteBatch(ids);
//        return new XaResult<>().success(null);
//    }
//}
