package com.ourslook.guower.api;

import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.weibo.WeiboConfig;
import com.ourslook.guower.utils.weibo.WeiboUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/api/weibo")
@Api(value = "weibo", description = "微博相关接口",position = 3)
public class WeiboController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 刷新时间
     */
    private LocalDateTime refreshTime = LocalDateTime.now().minusMinutes(5);
    /**
     * 暂存用户名
     */
    private String userName = "";
    /**
     * 暂存token剩余有效时间(秒)
     */
    private String expireIn = "";

    /**
     * 用于接收微博回调的code
     * @param code
     * @param request
     * @return
     */
    @ApiIgnore
    @IgnoreAuth
    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
    public String checkCode(String code, HttpServletRequest request){
        if(XaUtils.isNotBlank(code)){
            WeiboConfig.setCode(code);
            WeiboUtil.refreshToken(code);
        }
        String url = ServletUtils.getHttpRootPath(request) + "generator/user/weiboauthorize.html";
        return "redirect:"+url;
    }

    /**
     * 查询当前用户信息以及token状态
     * @return
     */
    @ApiIgnore
    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/nowUser", method = RequestMethod.GET)
    public XaResult<Map<String,Object>> nowUserState(){
        XaResult<Map<String,Object>> xr = new XaResult<>();
        Map<String,Object> userMap = new HashMap<>();
        Map<String,Object> tokenMap = new HashMap<>();
        //过5分钟才刷新，否则容易超过频次
        if(LocalDateTime.now().compareTo(refreshTime.plusMinutes(5))>0){
            userMap = WeiboUtil.getNowUserInfo();
            tokenMap = WeiboUtil.getAccessTokenState();
            userName = userMap.get("screen_name")!=null?userMap.get("screen_name").toString():"";
            expireIn = tokenMap.get("expire_in")!=null?tokenMap.get("expire_in").toString():"";
            refreshTime = LocalDateTime.now();
        }else{
            userMap.put("screen_name",userName);
            tokenMap.put("expire_in",expireIn);
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("user",userMap);
        resultMap.put("token",tokenMap);
        xr.setObject(resultMap);
        return xr;
    }

    /**
     * 查询微博列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询微博列表(客户提供账号所发布微博)")
    @ResponseBody
    @IgnoreAuth
    public XaResult<Map<String,Object>> list(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认5") @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        XaResult<Map<String,Object>> xr = new XaResult<>();
        try {
            Map<String,Object> map  = WeiboUtil.findWeiboByOid("5957748261",pageCurrent,pageSize);
            xr.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return xr;
    }

    /**
     * 查询微博列表
     */
    @PostMapping("/homeTimeline")
    @ApiOperation(value = "查询微博列表(获取当前登录用户及其所关注（授权）用户的最新微博)")
    @ResponseBody
    @IgnoreAuth
    public XaResult<Map<String,Object>> homeTimeline(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认5") @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        XaResult<Map<String,Object>> xr = new XaResult<>();
        try {
            Map<String,Object> map  = WeiboUtil.getHomeTimeline(pageCurrent,pageSize);
            xr.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return xr;
    }


}
