package com.ourslook.guower.api.web.business;

import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.business.BusAdvertisementService;
import com.ourslook.guower.service.business.BusNewsService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.utils.validator.Validator;
import com.ourslook.guower.vo.business.AuthorNewsVo;
import com.ourslook.guower.vo.business.AuthorSortVo;
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
@RequestMapping("/api/p/busnews")
@Api(value = "Pbusnews", description = "web端新闻相关接口", position = 1)
@SuppressWarnings("all")
public class WebApiBusNewsController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusNewsService busNewsService;
    @Autowired
    private UserService userService;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BusAdvertisementService advertisementService;

    /**
     * 查询新闻文章
     */
    @IgnoreAuth
    @PostMapping("/list")
    @ApiOperation(value = "获取新闻列表")
    @ResponseBody
    public XaResult<List<BusNewsVo>> list(
            @ApiParam("是否是热点文章,字段名:isHotsPot,是热点传1，默认为0") @RequestParam(value = "isHotsPot", required = false) Integer isHotsPot,
            @ApiParam("是否是专栏,字段名:isAuthor,是专栏传2,不是传1，默认不传") @RequestParam(value = "isAuthor", required = false) Integer isAuthor,
            @ApiParam("是否是广告,字段名:isAdvertisement,是广告传1，默认为0") @RequestParam(value = "isAdvertisement", required = false) Integer isAdvertisement,
            @ApiParam("文章类型,字段名:newsType,在字典接口中根据TYPE_NEWS类型代码查询后取remarks传过来") @RequestParam(value = "newsType", required = false) String newsType,
            @ApiParam("作者id,字段名:author，这里用于作者详情页查看作者所有文章时用到") @RequestParam(value = "author",  required = false) Long author,
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        XaResult<List<BusNewsVo>> xr = new XaResult<>();
        Query query = new Query(pageCurrent, pageSize);
        if (XaUtils.isNotEmpty(author) && author > 0) {
            query.put("author", author + "");
            query.put("sidx", "release_date");
            query.put("order", "desc");
        }
        if (XaUtils.isNotEmpty(newsType)){
            query.put("newsType", newsType);
        }
        if (XaUtils.isNotEmpty(isAuthor)) {
            query.put("releaseType", isAuthor);
        }
        if (XaUtils.isNotEmpty(isHotsPot) && isHotsPot == 1) {
            query.put("isHotspot", isHotsPot + "");
        }
        if (XaUtils.isNotEmpty(isAdvertisement)) {
            query.put("isAdvertisement", isAdvertisement + "");
        }
        query.put("examineType", Constant.ExamineType.TYPE_EXAMINE_PASS + "");
        query.put("isRelease", "1");
        xr.setObject(busNewsService.queryVoList(query));
        return xr;
    }

    /**
     * 获取热门文章
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/getHotNews")
    @ApiOperation(value = "获取热门文章")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<BusNewsEntity>> getHotNews(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认5") @RequestParam(defaultValue = "5") Integer pageSize
    ){

        Query query = new Query(pageCurrent, pageSize);
        List<BusNewsEntity> busNewsEntities = busNewsService.getHotNews(query);

        XaResult<List<BusNewsEntity>> xaResult = new XaResult<>();
        xaResult.setObject(busNewsEntities);
        return xaResult;
    }

    /**
     * 作者详情页
     * @return
     */
    @IgnoreAuth
    @GetMapping("/author/info/{id}")
    @ResponseBody
    @ApiOperation(value = "根据作者id获取详情", notes = "下方文章列表请调【list】接口")
    public XaResult<AuthorNewsVo> getNewsInfoForAuthor(@PathVariable("id") Integer id){
        AbstractAssert.isOk(XaUtils.isNotEmpty(id) && id > 0, "id不能为空");
        UserEntity userEntity = userService.queryObject(id);
        if (XaUtils.isEmpty(userEntity)) {
            throw new RRException("没有找到该作者信息");
        }
        AuthorNewsVo authorNewsVo = new AuthorNewsVo();
        if (XaUtils.isNotEmpty(userEntity.getNickName()) && Validator.isMobile(userEntity.getNickName())) {
            userEntity.setNickName(userEntity.getNickName().substring(0,3) + "****" + userEntity.getNickName().substring(7, userEntity.getNickName().length()));
        }
        authorNewsVo.setNickName(userEntity.getNickName());
        authorNewsVo.setHeadImg(userEntity.getHeadPortrait());
        authorNewsVo.setSignature(userEntity.getSignature());
        authorNewsVo.setUserLevel(userEntity.getUserLevel());

        //查询总文章数
        Query query = new Query();
        query.put("author", id + "");
        query.put("examineType", Constant.ExamineType.TYPE_EXAMINE_PASS + "");
        query.put("isRelease", "1");
        int readTotal = busNewsService.queryTotal(query);
        authorNewsVo.setNewsTotal(readTotal);

        int browseTotal = busNewsService.getBrowseTotalByAuthor(id.longValue());
        authorNewsVo.setBrowseTotal(browseTotal);

        XaResult<AuthorNewsVo> xaResult = new XaResult<>();
        xaResult.setObject(authorNewsVo);
        return xaResult;
    }

    /**
     * 文章详情页
     * @param id
     * @return
     */
    @IgnoreAuth
    @GetMapping("/info/{id}")
    @ResponseBody
    @ApiOperation("文章详情页，根据id获取")
    public XaResult<BusNewsVo> info(@PathVariable("id") Long id){
        Query query = new Query();
        query.put("id", id + "");
        query.put("examineType", Constant.ExamineType.TYPE_EXAMINE_PASS + "");
        List<BusNewsVo> busNewsVos = busNewsService.queryVoList(query);
        XaResult<BusNewsVo> xaResult = new XaResult<>();
        if (XaUtils.isNotEmpty(busNewsVos)) {
            BusNewsVo busNewsVo = busNewsVos.get(0);
            xaResult.setObject(busNewsVo);
            //需要更新浏览数量
            BusNewsEntity busNewsEntity = new BusNewsEntity();
            beanMapper.copy(busNewsVo, busNewsEntity);
            busNewsEntity.setLookTimes(busNewsEntity.getLookTimes() + 1);
            busNewsService.update(busNewsEntity);
        }
        return xaResult;
    }

    /**
     * 用户发表文章
     * @param busnews
     * @return
     */
    @PostMapping("/saveNews")
    @ApiOperation("用户发表文章")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<String> saveNews(@RequestBody BusNewsEntity busnews, HttpServletRequest request){
        Long userId = tokenService.queryUseIdByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userId) && userId > 0);
        busnews.setAuthor(userId.intValue());
        busNewsService.addNews(busnews);

        XaResult<String> xaResult = new XaResult<>();
        return xaResult.setObject("ok");
    }

    /**
     * 文章搜索
     * @param key
     * @param tag
     * @param pageCurrent
     * @param pageSize
     * @param request
     * @return
     */
    @IgnoreAuth
    @GetMapping("/searchNews")
    @ResponseBody
    @ApiOperation("文章搜索，这里如果是关键字搜索则只传关键字，如果标签搜索只传标签，切记")
    public XaResult<List<BusNewsVo>> searchNews(
            @ApiParam("根据关键字搜索，字段名:key") @RequestParam(value = "key", required = false) String key,
            @ApiParam("根据标签搜索，字段名:tag") @RequestParam(value = "tag", required = false) String tag,
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ){
        if (XaUtils.isEmpty(key) && XaUtils.isEmpty(tag)) {
            throw new RRException("参数异常");
        }
        Query query = new Query(pageCurrent, pageSize);
        query.put("sidx", "look_times");
        query.put("order", "desc");
        if (XaUtils.isNotEmpty(key)) {
            //关键字搜索，只对标题进行模糊查询
            query.put("title", key);
        }
        if (XaUtils.isNotEmpty(tag)) {
            query.put("tagKey", tag);
        }
        query.put("isRelease", "1");
        List<BusNewsVo> busNewsVos = busNewsService.queryVoList(query);
        XaResult<List<BusNewsVo>> xaResult = new XaResult<>();
        xaResult.setObject(busNewsVos);
        return xaResult;
    }

    /**
     * 作者排行
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    @GetMapping("/getAuthorCol")
    @ApiOperation("作者排行")
    @ResponseBody
    @IgnoreAuth
    public XaResult<List<AuthorSortVo>> getAuthorCol(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize
    ){
        Query query = new Query(pageCurrent, pageSize);
        List<AuthorSortVo> authorSortVos = userService.queryAuthorSort(query);
        XaResult<List<AuthorSortVo>> xaResult = new XaResult<>();
        xaResult.setObject(authorSortVos);
        return xaResult;
    }

}
