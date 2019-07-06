package com.ourslook.guower.api.web.user;

import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.user.InfExamineEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.business.BusNewsService;
import com.ourslook.guower.service.user.InfExamineService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.utils.validator.Validator;
import com.ourslook.guower.vo.business.BusNewsVo;
import com.ourslook.guower.vo.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aspectj.apache.bcel.classfile.ConstantNameAndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/p/user")
@CrossOrigin
@Api(value = "pUser", description = "web端用户相关接口", position = 5)
public class WebApiUserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private InfExamineService examineService;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private BusNewsService busNewsService;

    /**
     * 用户个人中心接口
     * @param request
     * @return
     */
    @GetMapping("/info")
    @ApiOperation("获取用户详情")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<UserVo> info(HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));
        if (XaUtils.isNotEmpty(userEntity.getNickName()) && Validator.isMobile(userEntity.getNickName())) {
            userEntity.setNickName(userEntity.getNickName().substring(0,3) + "****" + userEntity.getNickName().substring(7, userEntity.getNickName().length()));
        }
        UserVo userVo = new UserVo();
        beanMapper.copy(userEntity, userVo);

        InfExamineEntity examineEntity = examineService.queryObjectByUserId(userEntity.getId());
        if (XaUtils.isNotEmpty(examineEntity)) {
            userVo.setAuthStatus(examineEntity.getResult());
        }

        XaResult<UserVo> xaResult = new XaResult<>();
        xaResult.setObject(userVo);
        return xaResult;
    }

    /**
     * 编辑用户信息
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/updateInfo")
    @ApiOperation("编辑用户信息")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<UserEntity> updateInfo(@RequestBody UserEntity user, HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));

        beanMapper.copy(user, userEntity);
        userService.update(userEntity);
        XaResult<UserEntity> xaResult = new XaResult<>();
        xaResult.setObject(userEntity);
        return xaResult;
    }

    /**
     * 用户提交认证接口
     * @param examine
     * @return
     */
    @PostMapping("/authentication")
    @ApiOperation("用户认证")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<String> userAuthentication(@ApiParam("没值的字段留空") @RequestBody InfExamineEntity examine,
                                               HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));

        examine.setUserId(userEntity.getId());
        examineService.saveAndUpdateUser(examine);

        XaResult<String> xaResult = new XaResult<>();
        return xaResult.setObject("ok");
    }

    /**
     * 用户认证查询
     * @param request
     * @return
     */
    @GetMapping("/getAuthentication")
    @ApiOperation("进入申请认证界面时先请求该接口，如果有返回值说明已经申请过，没有返回值说明未申请过")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<InfExamineEntity> getUserAuthentication(HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));

        InfExamineEntity examineEntity = examineService.queryObjectByUserId(userEntity.getId());
        XaResult<InfExamineEntity> xaResult = new XaResult<>();
        xaResult.setObject(examineEntity);
        return xaResult;
    }

    /**
     * 获取我的文章列表
     * @param pageCurrent
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/getMyNews")
    @ResponseBody
    @ApiOperation("获取我的文章列表,这里是必须要传token")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<Map<String, Object>> getMyNews(
            @ApiParam("页号,字段名:pageCurrent,默认1,从第1页开始") @RequestParam(defaultValue = "1") Integer pageCurrent,
            @ApiParam("页长,字段名:pageSize,默认10") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        AbstractAssert.isOkUser(XaUtils.isNotEmpty(userEntity));

        Query query = new Query(pageCurrent, pageSize);
        query.put("author", userEntity.getId() + "");
        query.put("examineType", Constant.ExamineType.TYPE_EXAMINE_PASS + "");
        query.put("sidx", "release_date");
        query.put("order", "desc");
        List<BusNewsVo> busNewsVos = busNewsService.queryVoList(query);

        int total = busNewsService.queryTotal(query);
        Map<String, Object> map = new HashMap<>();
        map.put("list", busNewsVos);
        map.put("total", total);
        XaResult<Map<String, Object>> xaResult = new XaResult<>();
        xaResult.setObject(map);
        return xaResult;
    }
}
