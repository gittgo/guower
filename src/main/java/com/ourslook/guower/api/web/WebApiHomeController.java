package com.ourslook.guower.api.web;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import com.ourslook.guower.entity.business.BusFastNewsEntity;
import com.ourslook.guower.entity.common.InfDictInfoEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.business.BusAdvertisementService;
import com.ourslook.guower.service.business.BusFastNewsService;
import com.ourslook.guower.service.common.InfDictInfoService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.Validator;
import com.ourslook.guower.vo.business.WebHomeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Api(value = "pHome", description = "web端首页api", position = 6)
@RequestMapping("/api/p/")
public class WebApiHomeController {

    @Autowired
    private InfDictInfoService dictInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusFastNewsService fastNewsService;
    @Autowired
    private BusAdvertisementService advertisementService;

    /**
     * 首页接口
     * @return
     */
    @GetMapping("/home")
    @ResponseBody
    @ApiOperation("首页接口")
    @IgnoreAuth
    public XaResult<WebHomeVo> home(){
        WebHomeVo homeVo = new WebHomeVo();
        Query query = new Query(1, 10000);
        //获取首页banner
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_HOME_BANNER + "");
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<BusAdvertisementEntity> homeBanner = advertisementService.queryList(query);
        homeVo.setHomeBanner(homeBanner);
        //首页焦点button
        query.put("advertisementType", Constant.AdvertisementType.TYPE_ADVERTISING_HOME_FOCUS_BUTTON);
        List<BusAdvertisementEntity> homeSmailBanner = advertisementService.queryList(query);
        homeVo.setHomeSmaillBanner(homeSmailBanner);
        //新闻类型
        homeVo.setNewsType(getNewsType(true));
        //7*24快讯
        homeVo.setFastNews(getFastNews(1,10));
        //作者专栏
        homeVo.setAuthorList(getColumnUser(Constant.PushPositionType.TYPE_PUSH_POSITION_ONE, 1, 6));
        //企业专栏
        homeVo.setCompanyList(getColumnUser(Constant.PushPositionType.TYPE_PUSH_POSITION_TWO, 1, 6));

        XaResult<WebHomeVo> xaResult = new XaResult<>();
        xaResult.setObject(homeVo);
        return xaResult;
    }

    /**
     * 获取文章类型
     * @param isHome true为首页数据 false为所有
     * @return
     */
    public List<InfDictInfoEntity> getNewsType(Boolean isHome){
        Query query = new Query();
        query.put("type", "TYPE_NEWS");
        if (isHome) {
            query = new Query(1, 4);
            query.put("type", "TYPE_NEWS");
            query.put("isHomePage", Constant.Status.valid + "");
        }
        query.put("sidx", "sort");
        query.put("order", "asc");
        List<InfDictInfoEntity> dictInfoEntities = dictInfoService.queryList(query);
        return dictInfoEntities;
    }

    /**
     * 获取专栏排行
     * @see Constant.PushPositionType
     * @param pushPositionType
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> getColumnUser(Integer pushPositionType, Integer pageCurrent, Integer pageSize){

        Query query = new Query(pageCurrent, pageSize);
        query.put("pushPosition", pushPositionType + "");
        if (pushPositionType == Constant.PushPositionType.TYPE_PUSH_POSITION_TWO) {
            //企业专栏
            query.put("sidx", "push_two_sort");
            query.put("order", "asc");
        } else if (pushPositionType == Constant.PushPositionType.TYPE_PUSH_POSITION_ONE) {
            //专栏作者
            query.put("sidx", "push_one_sort");
            query.put("order", "asc");
        }
        List<UserEntity> userEntities = userService.queryList(query);
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map = null;
        for (UserEntity user : userEntities) {
            map = new HashMap<>();
            map.put("userId", user.getId());
            if (XaUtils.isNotEmpty(user.getNickName()) && Validator.isMobile(user.getNickName())) {
                user.setNickName(user.getNickName().substring(0,3) + "****" + user.getNickName().substring(7, user.getNickName().length()));
            }
            map.put("nickName", user.getNickName());
            map.put("headImg", user.getHeadPortrait());
            map.put("signature", user.getSignature());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 获取7*24快讯
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    public List<BusFastNewsEntity> getFastNews(Integer pageCurrent, Integer pageSize){
        Query query = new Query(pageCurrent, pageSize);
        query.put("isNewsFlash", "1");
        List<BusFastNewsEntity> fastNewsEntities = fastNewsService.queryList(query);
        return fastNewsEntities;
    }
}
