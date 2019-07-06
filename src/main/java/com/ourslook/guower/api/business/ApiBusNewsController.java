//package com.ourslook.guower.api.business;
//
//import com.ourslook.guower.entity.business.BusNewsEntity;
//import com.ourslook.guower.service.business.BusNewsService;
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
// * 新闻文章表
// *
// * @author dazer
// * @email ab601026460@qq.com
// * @date 2018-06-29 10:50:12
// */
//@RestController
//@CrossOrigin
//@RequestMapping("/api/busnews")
//@Api(value = "busnews", description = "新闻文章表", position = 1)
//@SuppressWarnings("all")
//public class ApiBusNewsController {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private BusNewsService busNewsService;
//
//    /**
//     * 查询所有新闻文章表
//     */
//    @PostMapping("list")
//    @ApiOperation(value = "查询所有新闻文章表信息")
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
//    @ResponseBody
//    public XaResult<List<BusNewsEntity>> list(
//        @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
//        @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
//        HttpServletRequest request
//    ) {
//        XaResult<List<BusNewsEntity>> xr = new XaResult<>();
//        Query query = new Query(pageCurrent,pageSize);
//        xr.setObject(busNewsService.queryList(query));
//        return xr;
//    }
//
//    /**
//     * 更新和保存
//     */
//    @ApiOperation(value = "新闻文章表更新和保存", notes = "新闻文章表更新和保存", position = 1)
//    @ResponseBody
//    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
//    public XaResult<BusNewsEntity> saveOrUpdate(
//                        @ApiParam("编号,字段名:id,编号,如：") @RequestParam(defaultValue = "", required = false) Integer id,
//                        @ApiParam("标题,字段名:title,标题,如：") @RequestParam(defaultValue = "", required = false) String title,
//                        @ApiParam("副标题【简介】,字段名:smallTitle,副标题【简介】,如：") @RequestParam(defaultValue = "", required = false) String smallTitle,
//                        @ApiParam("大图标,字段名:image,大图标,如：") @RequestParam(defaultValue = "", required = false) String image,
//                        @ApiParam("小图标,字段名:smallImage,小图标,如：") @RequestParam(defaultValue = "", required = false) String smallImage,
//                        @ApiParam("文章标签【2.NEW 1.HOT 0.无标签】,字段名:tag,文章标签【2.NEW 1.HOT 0.无标签】,如：") @RequestParam(defaultValue = "", required = false) Integer tag,
//                        @ApiParam("内容标签1,字段名:tag1,内容标签1,如：") @RequestParam(defaultValue = "", required = false) String tag1,
//                        @ApiParam("内容标签2,字段名:tag2,内容标签2,如：") @RequestParam(defaultValue = "", required = false) String tag2,
//                        @ApiParam("内容标签3,字段名:tag3,内容标签3,如：") @RequestParam(defaultValue = "", required = false) String tag3,
//                        @ApiParam("正文【详情】,字段名:mainText,正文【详情】,如：") @RequestParam(defaultValue = "", required = false) String mainText,
//                        @ApiParam("文章类型,字段名:newsType,文章类型,如：") @RequestParam(defaultValue = "", required = false) String newsType,
//                        @ApiParam("阅读量,字段名:lookTimes,阅读量,如：") @RequestParam(defaultValue = "", required = false) Integer lookTimes,
//                        @ApiParam("作者,字段名:author,作者,如：") @RequestParam(defaultValue = "", required = false) Integer author,
//                        @ApiParam("责任编辑,字段名:responsibleEditor,责任编辑,如：") @RequestParam(defaultValue = "", required = false) String responsibleEditor,
//                        @ApiParam("发布类型【1.后台 2.作者】,字段名:releaseType,发布类型【1.后台 2.作者】,如：") @RequestParam(defaultValue = "", required = false) Integer releaseType,
//                        @ApiParam("发布人,字段名:releaseUserId,发布人,如：") @RequestParam(defaultValue = "", required = false) Integer releaseUserId,
//                        @ApiParam("发布时间,字段名:releaseDate,发布时间,如：") @RequestParam(defaultValue = "", required = false) LocalDateTime releaseDate,
//                        @ApiParam("是否为广告【1.广告  0.非广告】,字段名:isAdvertisement,是否为广告【1.广告  0.非广告】,如：") @RequestParam(defaultValue = "", required = false) Integer isAdvertisement,
//                        @ApiParam("是否上热点【1.热点  0.非热点】,字段名:isHotspot,是否上热点【1.热点  0.非热点】,如：") @RequestParam(defaultValue = "", required = false) Integer isHotspot,
//                        @ApiParam("暂留,字段名:newsRemarks1,暂留,如：") @RequestParam(defaultValue = "", required = false) String newsRemarks1,
//                        @ApiParam("暂留,字段名:newsRemarks2,暂留,如：") @RequestParam(defaultValue = "", required = false) String newsRemarks2,
//                        @ApiParam("暂留,字段名:newsRemarks3,暂留,如：") @RequestParam(defaultValue = "", required = false) String newsRemarks3,
//                        @ApiParam("排序,字段名:sort,排序,如：") @RequestParam(defaultValue = "", required = false) Integer sort,
//                        @ApiParam("排序到期时间,字段名:sortTime,排序到期时间,如：") @RequestParam(defaultValue = "", required = false) LocalDateTime sortTime,
//                        @ApiParam("审核类型【2.未通过 0.审核中 1.通过】,字段名:examineType,审核类型【2.未通过 0.审核中 1.通过】,如：") @RequestParam(defaultValue = "", required = false) Integer examineType,
//                    HttpServletRequest request
//    ) {
//        XaResult<BusNewsEntity> xr = new XaResult<>();
//        BusNewsEntity  busNewsEntity = new BusNewsEntity();
//                    busNewsEntity.setId(id);
//                    busNewsEntity.setTitle(title);
//                    busNewsEntity.setSmallTitle(smallTitle);
//                    busNewsEntity.setImage(image);
//                    busNewsEntity.setSmallImage(smallImage);
//                    busNewsEntity.setTag(tag);
//                    busNewsEntity.setTag1(tag1);
//                    busNewsEntity.setTag2(tag2);
//                    busNewsEntity.setTag3(tag3);
//                    busNewsEntity.setMainText(mainText);
//                    busNewsEntity.setNewsType(newsType);
//                    busNewsEntity.setLookTimes(lookTimes);
//                    busNewsEntity.setAuthor(author);
//                    busNewsEntity.setResponsibleEditor(responsibleEditor);
//                    busNewsEntity.setReleaseType(releaseType);
//                    busNewsEntity.setReleaseUserId(releaseUserId);
//                    busNewsEntity.setReleaseDate(releaseDate);
//                    busNewsEntity.setIsAdvertisement(isAdvertisement);
//                    busNewsEntity.setIsHotspot(isHotspot);
//                    busNewsEntity.setNewsRemarks1(newsRemarks1);
//                    busNewsEntity.setNewsRemarks2(newsRemarks2);
//                    busNewsEntity.setNewsRemarks3(newsRemarks3);
//                    busNewsEntity.setSort(sort);
//                    busNewsEntity.setSortTime(sortTime);
//                    busNewsEntity.setExamineType(examineType);
//                ValidatorUtils.validateEntity(busNewsEntity);
//
//        if (id == null) {
//            busNewsService.save(busNewsEntity);
//        } else {
//            busNewsService.update(busNewsEntity);
//        }
//        xr.setObject(busNewsEntity);
//        return xr;
//    }
//
//   /**
//    * 信息
//    */
//    @ApiOperation(value="新闻文章表详情",notes="详情")
//    @ResponseBody
//    @RequestMapping(value="findBusNewsByid",method=RequestMethod.POST)
//    public XaResult<BusNewsEntity> info (
//            @ApiParam("主键,字段名:id,必填") @RequestParam(value = "id") Integer id
//     ){
//        BusNewsEntity busNews = busNewsService.queryObject(id);
//        return new XaResult().success(busNews);
//    }
//
//   /**
//    * 删除
//    */
//    @ApiOperation(value="根据id修改busnews状态",notes="修改busnews状态,当返回的code=1时，保存成功后object返回对象")
//    @ResponseBody
//    @RequestMapping(value="operateBusNewsByid",method=RequestMethod.POST)
//    public XaResult info (
//        @ApiParam("编号,字段名:modelIds,必填") @RequestParam(value = "modelId") Integer[] ids,
//        @ApiParam("操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作") @RequestParam(defaultValue = "3") Integer status
//    ) {
//        busNewsService.deleteBatch(ids);
//        return new XaResult<>().success(null);
//    }
//}
