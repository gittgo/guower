package com.ourslook.guower.api.web.business;

import com.ourslook.guower.entity.business.BusFastNewsEntity;
import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.service.business.BusFastNewsService;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.pay.weixin.mp.WeixinMpInterface;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.XaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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

    @Autowired
    private WeixinMpInterface weixinMpInterface;

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
        List<BusFastNewsEntity> lists = busFastNewsService.queryList(query);
        String url = request.getParameter("url");
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).setWeixinapi(weixinMpInterface.getJsapiConfig(url));
        }
        xr.setObject(lists);
        return xr;
    }


    /**
     * 信息
     */
    @PostMapping("info")
    @ApiOperation(value = "查询所有快报表信息")
    @IgnoreAuth
    @ResponseBody
    public XaResult<BusFastNewsEntity> info(
            @ApiParam("快报id") @RequestParam(defaultValue = "0") Integer id,
            HttpServletRequest request
    ) {
        XaResult<BusFastNewsEntity> xr = new XaResult<>();
        BusFastNewsEntity busFastNewsEntity = busFastNewsService.queryObject(id);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = busFastNewsEntity.getReleaseDate().atZone(zone).toInstant();
        Date date = Date.from(instant);
        busFastNewsEntity.setWeek(GetDayOfWeek(date));
        xr.setObject(busFastNewsEntity);
        return xr;
    }

    public String GetDayOfWeek(Date date){
            final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六" };
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
            if(dayOfWeek <0)dayOfWeek=0;
            return dayNames[dayOfWeek];
        }

        /**
     * 添加快报
     */
    @PostMapping("addFastNews")
    @ApiOperation(value = "添加快报")
    @IgnoreAuth
    @ResponseBody
    public XaResult<Boolean> addFastNews(
            @ApiParam("快报id") @RequestParam(defaultValue = "0") Integer id,
            @ApiParam("标题") @RequestParam(defaultValue = "") String title,
            @ApiParam("内容") @RequestParam(defaultValue = "") String content,
            @ApiParam("img") @RequestParam(defaultValue = "") String post_img,
            @ApiParam("发布时间") @RequestParam(defaultValue = "") String date,
            @ApiParam("链接地址") @RequestParam(defaultValue = "") String source_link,
            @ApiParam("利好") @RequestParam(defaultValue = "0") Integer bull,
            @ApiParam("利空") @RequestParam(defaultValue = "0") Integer bear,
            HttpServletRequest request
    ) {
        XaResult<Boolean> xr = new XaResult<Boolean>();
        try{
            //  C创建快报
            BusFastNewsEntity busFastNewsEntity = new BusFastNewsEntity();
            busFastNewsEntity.setTitle(title.replace("鸵鸟区块链讯",""));
            busFastNewsEntity.setMainText(content.replace("鸵鸟区块链讯",""));
            busFastNewsEntity.setReleaseUserId(1);
            busFastNewsEntity.setReleaseUserName("superAdmin");
            if(null != date && !"".equals(date)){
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.parse(date,df);
                busFastNewsEntity.setReleaseDate(ldt);
            }else {
                busFastNewsEntity.setReleaseDate(LocalDateTime.now());
            }
            busFastNewsEntity.setGood(bull);
            busFastNewsEntity.setBad(bear);
            busFastNewsEntity.setGuowerIndex(5);
            busFastNewsEntity.setIsNewsFlash(1);
            busFastNewsEntity.setLookTimes(5883);
            if(null == title || "".equals(title) || null == content || "".equals(content) ){
                xr.setObject(false);
                return xr;
            }
            busFastNewsService.save(busFastNewsEntity);
            xr.setObject(true);
        }catch (Exception e){
            xr.setObject(false);
            e.printStackTrace();
        }
        return xr;
    }


}
