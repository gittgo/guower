package com.ourslook.guower.controller.business;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.business.BusAdvertisementService;
import com.ourslook.guower.service.business.BusNewsService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import com.ourslook.guower.vo.business.BusNewsVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 新闻文章表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@CrossOrigin
@RestController
@RequestMapping("busnews")
public class BusNewsController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BusNewsService busNewsService;
    @Autowired
    private BusAdvertisementService busAdvertisementService;
    @Resource
    private BeanMapper beanMapper;
    @Autowired
    private UserService userService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("busnews:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        //后台只按发布时间倒序
        query.put("sidx","release_date");
        query.put("order" ,"desc");
        List<BusNewsVo> busNewsList = busNewsService.queryVoList(query);
        int total = busNewsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(busNewsList, total, query.getLimit(), query.getPage());

        return R.ok().putObj("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("busnews:info")
    public R info(@PathVariable("id") Integer id) {
            BusNewsEntity busNews = busNewsService.queryObject(id);

        return R.ok().putObj("busNews", busNews);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("busnews:save")
    public R save(@RequestBody BusNewsEntity busNews) {

        //当前登录用户
        SysUserEntity sysUser = ShiroUtils.getUserEntity();
        busNews.setReleaseUserId(sysUser.getUserId().intValue());
        busNews.setReleaseDate(LocalDateTime.now());
        busNews.setLookTimes(0);
        // 作者名称
        if("".equals(busNews.getAuthorname()) || null == busNews.getAuthorname()  ){
            // 作者为绑定的用户作者
            busNews.setAuthor(sysUser.getAuthor());
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("userName",busNews.getAuthorname());
            List<UserEntity> userEntities =  userService.queryList(map);
            if(userEntities.isEmpty()){
                busNews.setAuthor(sysUser.getAuthor());
            }else {
                busNews.setAuthor(userEntities.get(0).getId());
            }
        }
        busNews.setExamineType(1);//默认通过
        busNews.setIsRelease(0);//默认未发布
        busNews.setReleaseType(1);//后台发布
        //初始阅读量为3万到4万随机（包含3万，4万）
        busNews.setLookTimes((int)(Math.random()*10001+30000));
        ValidatorUtils.validateEntity(busNews);
        busNewsService.save(busNews);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("busnews:update")
    public R update(@RequestBody BusNewsEntity busNews) {

            //busNews.setModifyuser(getUserId().toString());
            //busNews.setModifytime(new Date());

        ValidatorUtils.validateEntity(busNews);
            busNewsService.update(busNews);

        return R.ok();
    }

    /**
     * 文章审核
     */
    @RequestMapping("/examine")
    @RequiresPermissions("busnews:update")
    public R examine(@RequestBody ExamineCheck examineCheck) {

        //当前登录用户
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        examineCheck.setSysUserId(sysUserEntity.getUserId().intValue());
        busNewsService.examine(examineCheck);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("busnews:delete")
    public R delete(@RequestBody Integer[]ids) {

        //一下两种删除根据情况自己选择
        int count = busAdvertisementService.queryTotalByNewsId(ids);
        if(count>0){
            return R.error("广告中引用的文章不可删除");
        }

        //物理删除
        busNewsService.deleteBatch(ids);

        //逻辑删除
        //List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        //busNewsService.multiOperate(idStr, Constant.Status.delete);

        return R.ok();
    }

   /**
    * 根据id修改busnews状态
    * 这里是一个通用操作，如： 上下架、删除、启用，禁用 等等状态改变的使用这个一个方法就够了
    *
    * @param ids: 编号,字段名:modelIds,必填
    * @param status: 操作类型,字段名:status,-1锁定,0无效,1正常,2发布,3删除,选填,默认删除操作, 参考常量Constant.Status
    */
    @RequiresPermissions("busnews:delete")
    @RequestMapping(value="operateBusNewsByIds")
    public XaResult operateByIds (
            @RequestParam(value = "modelIds") Integer[] ids,
            @RequestParam(defaultValue = "3") Integer status
    ) {
        List<String> idStr = Stream.of(ids).map(String::valueOf).collect(Collectors.toList());
        busNewsService.multiOperate(idStr, status);
        return new XaResult().success("修改成功");
    }

    /**
    * 如果指定行，就按照指定行导出，否则 导出所有数据成excel格式
    */
    @RequestMapping("/exportsExcelAll")
    @RequiresPermissions("busnews:list")
    @SuppressWarnings("unchecked")
    public void exportsExcelAll(@RequestParam Map<String, Object> params , @RequestParam Boolean isCvs, @RequestParam String[] modelIds,  HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        //指定行导出
        if (XaUtils.isNotEmpty(modelIds)) {
            /**
             * 导出指定行成为Excel格式
             * modelIds客户端使用逗号分割，这里就要用数组接收
             */
            Query query = new Query();
            query.put("id_IN", Lists.newArrayList(modelIds));
            List<BusNewsEntity> vos = busNewsService.queryList(query);
            busNewsService.exportsToExcels(vos, request, response, false);
        } else {
            //导出所有
            if (params.containsKey("searchParams")) {
                params.putAll(JSON.parseObject(params.get("searchParams")+"", Map.class));
                params.remove("searchParams");
            }
            for (Map.Entry<String,Object> item : params.entrySet()) {
                Object value = (item.getValue() == null || item.getValue().toString().equalsIgnoreCase("null")) ? "" : item.getValue();
                item.setValue(value);
            }
            List<BusNewsEntity> vos = busNewsService.queryList(params);
            busNewsService.exportsToExcels(vos, request, response,  isCvs == null ? false : isCvs);
        }
    }
}
