package com.ourslook.guower.api.app;

import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.business.BusNewsService;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.vo.business.BusNewsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 新闻文章表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@RestController
@CrossOrigin
@RequestMapping("/api/app/news")
@Api(value = "news", description = "新闻文章表")
@SuppressWarnings("all")
public class AppApiNewsController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BusNewsService busNewsService;

    /**
     * 搜索新闻文章
     */
    @PostMapping("listByType")
    @ApiOperation(value = "搜索新闻文章")
    @IgnoreAuth
    @ResponseBody
    public XaResult<List<BusNewsVo>> listByType(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("文章类型【字典类remarks字段】") @RequestParam(defaultValue = "") Integer newsType,
            @ApiParam("是否热点【1.是】") @RequestParam(defaultValue = "") String isHotspot,
            @ApiParam("是否专栏【2.是】") @RequestParam(defaultValue = "") String releaseType,
            HttpServletRequest request
    ) {
        if(XaUtils.isNotBlank(isHotspot) && !isHotspot.trim().equals("1")){
            isHotspot = "";
        }
        if(XaUtils.isNotBlank(releaseType) && !releaseType.trim().equals("2")){
            releaseType = "";
        }
        XaResult<List<BusNewsVo>> xr = new XaResult<>();
        Query query = new Query(pageCurrent,pageSize);
        query.put("isHotspot",isHotspot);
        query.put("releaseType",releaseType);
        query.put("newsType",newsType);
        query.put("isRelease","1");

        xr.setObject(busNewsService.queryVoList(query));
        return xr;
    }

    /**
     * 我的新闻文章
     */
    @PostMapping("listByMy")
    @ApiOperation(value = "我的新闻文章")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    @ResponseBody
    public XaResult<List<BusNewsVo>> listByMy(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("没有找到该作者信息");
        }
        XaResult<List<BusNewsVo>> xr = new XaResult<>();
        Query query = new Query(pageCurrent,pageSize);
        //取发布的
        query.put("isRelease","1");
        //作者为自己
        query.put("author",userEntity.getId()+"");
        //个人文章只按发布时间倒序
        query.put("sidx","release_date");
        query.put("order","desc");


        xr.setObject(busNewsService.queryVoList(query));
        return xr;
    }

    /**
     * 根据用户id查找他的新闻文章
     */
    @PostMapping("listByUserId")
    @ApiOperation(value = "根据用户id查找他的新闻文章")
    @IgnoreAuth
    @ResponseBody
    public XaResult<List<BusNewsVo>> listByUserId(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("用户id") @RequestParam(defaultValue = "1") Integer userId,
            HttpServletRequest request
    ) {
        XaResult<List<BusNewsVo>> xr = new XaResult<>();
        Query query = new Query(pageCurrent,pageSize);
        //取发布的
        query.put("isRelease","1");
        //作者为自己
        query.put("author",userId+"");
        //个人文章只按发布时间倒序
        query.put("sidx","release_date");
        query.put("order","desc");


        xr.setObject(busNewsService.queryVoList(query));
        return xr;
    }

    /**
     * 搜索新闻文章
     */
    @PostMapping("listByTitle")
    @ApiOperation(value = "搜索新闻文章")
//    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusNewsVo>> listByTitle(
        @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
        @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
        @ApiParam("标题,若填写排序只按浏览量排序") @RequestParam(defaultValue = "") String title,
        HttpServletRequest request
    ) {
        XaResult<List<BusNewsVo>> xr = new XaResult<>();
        Query query = new Query(pageCurrent,pageSize);
        //取发布的
        query.put("isRelease","1");
        //若title填写了则只按浏览量排序
        if(XaUtils.isNotBlank(title)){
            query.put("title",title);
            query.put("sidx","look_times");
            query.put("order","desc");
        }
        xr.setObject(busNewsService.queryVoList(query));
        return xr;
    }

    /**
     * 详情
     * @param id 新闻文章编号
     * @return
     */
    @ApiOperation(value="新闻文章表详情",notes="详情")
    @ResponseBody
    @RequestMapping(value="findBusNewsByid",method=RequestMethod.POST)
    @IgnoreAuth
    public XaResult<BusNewsVo> info (
            @ApiParam("主键,字段名:id,必填") @RequestParam(value = "id") Integer id
     ){
        BusNewsVo busNews = busNewsService.queryObjectVo(id);
        if(XaUtils.isEmpty(busNews)){
            throw new RRException("新闻文章不存在");
        }
        busNews.setLookTimes(busNews.getLookTimes()==null?1:busNews.getLookTimes()+1);
        busNewsService.update(busNews);
        return new XaResult().success(busNews);
    }


    /**
     * 热门文章
     */
    @PostMapping("listByHot")
    @ApiOperation(value = "热门文章")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusNewsEntity>> listByHot(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "4") Integer pageSize,
            HttpServletRequest request
    ) {
        Query query = new Query(pageCurrent, pageSize);
        List<BusNewsEntity> busNewsEntities = busNewsService.getHotNews(query);
        XaResult<List<BusNewsEntity>> xaResult = new XaResult<>();
        xaResult.setObject(busNewsEntities);
        return xaResult;
    }
}
