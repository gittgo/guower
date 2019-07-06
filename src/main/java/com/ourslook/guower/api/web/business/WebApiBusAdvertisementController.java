package com.ourslook.guower.api.web.business;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.service.business.BusAdvertisementService;
import com.ourslook.guower.service.business.BusNewsService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.vo.business.HomeAdvertisementVo;
import com.ourslook.guower.vo.business.NewsAdvertisementVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.math.NumberUtils;
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
@RequestMapping("/api/p/busadvertisement")
@Api(value = "Pbusadvertisement", description = "web端广告相关接口", position = 1)
@SuppressWarnings("all")
public class WebApiBusAdvertisementController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusAdvertisementService advertisementService;
    @Autowired
    private BusNewsService newsService;

    /**
     * 首页广告接口
     * @return
     */
    @GetMapping("/homeAdvertisement")
    @ResponseBody
    @ApiOperation("首页广告接口")
    @IgnoreAuth
    public XaResult<HomeAdvertisementVo> getHomeAdvertisement(){
        HomeAdvertisementVo homeAdvertisementVo = new HomeAdvertisementVo();
        //查询首页头部广告
        Query query = new Query(1, 5);
        query.put("sidx", "sort");
        query.put("order", "asc");
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_HOME_TOP_BUTTON + "");
        List<BusAdvertisementEntity> homeHead = advertisementService.queryList(query);

        //查询首页右边广告
        query = new Query(1, 3);
        query.put("sidx", "sort");
        query.put("order", "asc");
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_HOME_RIGHT_BUTTON + "");
        List<BusAdvertisementEntity> homeRight = advertisementService.queryList(query);

        homeAdvertisementVo.setHomeHead(homeHead);
        homeAdvertisementVo.setHomeRight(homeRight);

        XaResult<HomeAdvertisementVo> xaResult = new XaResult<>();
        xaResult.setObject(homeAdvertisementVo);
        return xaResult;
    }

    /**
     * 文章详情页右侧广告
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/newInfoAdvertisement")
    @ApiOperation("文章详情页右边广告列表")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusAdvertisementEntity>> newInfoAdvertisement(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_NEWS_DETAILS_RIGHT_BUTTON + "");
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<BusAdvertisementEntity> advertisementEntities = advertisementService.queryList(query);

        XaResult<List<BusAdvertisementEntity>> xaResult = new XaResult<>();
        return xaResult.setObject(advertisementEntities);
    }

    /**
     * 战略合作接口
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/strategicCooperation")
    @ApiOperation("战略合作接口")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusAdvertisementEntity>> strategicCooperation(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_STRATEGIC_COOPERATION + "");
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<BusAdvertisementEntity> advertisementEntities = advertisementService.queryList(query);

        XaResult<List<BusAdvertisementEntity>> xaResult = new XaResult<>();
        return xaResult.setObject(advertisementEntities);
    }

    /**
     * 合作内容接口
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/cooperationContent")
    @ApiOperation("合作内容接口")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusAdvertisementEntity>> cooperationContent(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_COOPERATIVE_CONTENT + "");
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<BusAdvertisementEntity> advertisementEntities = advertisementService.queryList(query);

        XaResult<List<BusAdvertisementEntity>> xaResult = new XaResult<>();
        return xaResult.setObject(advertisementEntities);
    }

    /**
     * 新闻列表页右侧广告
     * @return
     */
    @GetMapping("/newsAdvertisement")
    @ResponseBody
    @ApiOperation("新闻列表页右侧广告")
    @IgnoreAuth
    public XaResult<NewsAdvertisementVo> newsAdvertisement(){
        NewsAdvertisementVo newsAdvertisementVo = new NewsAdvertisementVo();
        //查询果味box
        Query boxQuery = new Query(1, 1);
        boxQuery.put("sidx", "sort");
        boxQuery.put("order", "asc");
        boxQuery.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_GUOWER_BOX + "");
        List<BusAdvertisementEntity> boxLists = advertisementService.queryList(boxQuery);
        if (XaUtils.isNotEmpty(boxLists)) {
            BusNewsEntity busNewsEntity = newsService.queryObject(boxLists.get(0).getJumpNewsId());
            for (BusAdvertisementEntity entity : boxLists) {
                entity.setLookTimes(busNewsEntity.getLookTimes());
            }
        }
        newsAdvertisementVo.setGuowerBox(boxLists);
        //查询视频广告
        boxQuery.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_NEWS_VIDEO + "");
        List<BusAdvertisementEntity> videoLists = advertisementService.queryList(boxQuery);
        newsAdvertisementVo.setRightVideo(videoLists);
        //查询右侧button
        boxQuery = new Query(1, 5);
        boxQuery.put("sidx", "sort");
        boxQuery.put("order", "asc");
        boxQuery.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_NEWS_RIGHT_BUTTON + "");
        List<BusAdvertisementEntity> lists = advertisementService.queryList(boxQuery);
        newsAdvertisementVo.setRightButton(lists);

        XaResult<NewsAdvertisementVo> xaResult = new XaResult<>();
        return xaResult.setObject(newsAdvertisementVo);
    }

    /**
     * 新闻列表页右侧广告
     * @return
     */
    @GetMapping("/newsColumnAdvertisement")
    @ResponseBody
    @ApiOperation("专栏列表页右侧广告")
    @IgnoreAuth
    public XaResult<NewsAdvertisementVo> newsColumnAdvertisement(){
        NewsAdvertisementVo newsAdvertisementVo = new NewsAdvertisementVo();
        //查询果味box
        Query boxQuery = new Query(1, 1);
        boxQuery.put("sidx", "sort");
        boxQuery.put("order", "asc");
        boxQuery.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_GUOWER_BOX + "");
        List<BusAdvertisementEntity> boxLists = advertisementService.queryList(boxQuery);
        if (XaUtils.isNotEmpty(boxLists)) {
            BusNewsEntity busNewsEntity = newsService.queryObject(boxLists.get(0).getJumpNewsId());
            for (BusAdvertisementEntity entity : boxLists) {
                entity.setLookTimes(busNewsEntity.getLookTimes());
            }
        }
        newsAdvertisementVo.setGuowerBox(boxLists);
        //查询视频广告
        boxQuery.put("advertisementType", Constant.AdvertisementType.TYPE_SPECIALCOLUMN_VIDEO + "");
        List<BusAdvertisementEntity> videoLists = advertisementService.queryList(boxQuery);
        newsAdvertisementVo.setRightVideo(videoLists);
        //查询右侧button
        boxQuery = new Query(1, 5);
        boxQuery.put("sidx", "sort");
        boxQuery.put("order", "asc");
        boxQuery.put("advertisementType", Constant.AdvertisementType.TYPE_SPECIALCOLUMN_RIGHT_BUTTON + "");
        List<BusAdvertisementEntity> lists = advertisementService.queryList(boxQuery);
        newsAdvertisementVo.setRightButton(lists);

        XaResult<NewsAdvertisementVo> xaResult = new XaResult<>();
        return xaResult.setObject(newsAdvertisementVo);
    }

    /**
     * 快报页面右侧广告
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/fastNewsAdvertisement")
    @ResponseBody
    @ApiOperation("快报页面右侧广告")
    @IgnoreAuth
    public XaResult<List<BusAdvertisementEntity>> fastNewsAdvertisement(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        query.put("sidx", "sort");
        query.put("order", "asc");
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_FASTNEWS_RIGHT_BUTTON + "");
        List<BusAdvertisementEntity> advertisementEntities = advertisementService.queryList(query);

        XaResult<List<BusAdvertisementEntity>> xaResult = new XaResult<>();
        return xaResult.setObject(advertisementEntities);
    }

    /**
     * 新闻专栏banner
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/getNewsBanner")
    @ApiOperation("新闻页banner")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusAdvertisementEntity>> getNewsBanner(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认4") @RequestParam(defaultValue = "4") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_NEWS_BANNER);
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<BusAdvertisementEntity> newsBanner = advertisementService.queryList(query);

        XaResult<List<BusAdvertisementEntity>> xaResult = new XaResult<>();
        return xaResult.setObject(newsBanner);
    }

    /**
     * 新闻专栏banner
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/getNewsColumnBanner")
    @ApiOperation("专栏页banner")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusAdvertisementEntity>> getNewsColumnBanner(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认4") @RequestParam(defaultValue = "4") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        query.put("advertisementType", Constant.AdvertisementType.TYPE_SPECIALCOLUMN_BANNER);
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<BusAdvertisementEntity> newsBanner = advertisementService.queryList(query);

        XaResult<List<BusAdvertisementEntity>> xaResult = new XaResult<>();
        return xaResult.setObject(newsBanner);
    }

    /**
     * 该接口用于点击广告时记录浏览量
     * @param id
     * @return
     */
    @IgnoreAuth
    @GetMapping("/updateLookTimes")
    @ApiOperation("用于广告点击时记录浏览量")
    public XaResult<String> updateLookTimes(
            @ApiParam("广告id") @RequestParam(value = "id") Integer id
    ){
        AbstractAssert.isOk(XaUtils.isNotEmpty(id), "id不能为空");
        BusAdvertisementEntity advertisementEntity = advertisementService.queryObject(id);
        advertisementEntity.setLookTimes(advertisementEntity.getLookTimes() + 1);
        advertisementService.update(advertisementEntity);

        XaResult<String> xaResult = new XaResult<>();
        xaResult.setObject("ok");
        return xaResult;
    }
}
