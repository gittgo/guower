package com.ourslook.guower.api.app;

import com.google.code.kaptcha.Producer;
import com.ourslook.guower.config.OurslookConfig;
import com.ourslook.guower.entity.user.InfExamineEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.service.user.InfExamineService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.encryptdir.DesUtil;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.shortmsg.yuntongxun.YuntongxunSmsUtil;
import com.ourslook.guower.utils.validator.AbstractAssert;
import com.ourslook.guower.utils.validator.Validator;
import com.ourslook.guower.vo.user.UserVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/app/user")
@CrossOrigin
@Api(value = "user", description = "用户相关接口")
public class AppApiUserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 图形验证码
     */
    private static String SESSIN_KEY_IMAGE = "imageCode";

    /**
     * 验证码
     */
    private static String SESSION_KEY = "phoneAndode";
    /**
     * 验证码发送时间
     */
    private static String SESSION_MSG_SENDTIME_KEY = "nowdate";
    /**
     * 发送的验证码有效时间
     */
    private static Integer SESSION_MSG_TIME_KEY = 5;

    @Autowired
    private OurslookConfig springBootMyApplication;
    @Autowired
    private InfExamineService infExamineService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private Producer producer;

    /**
     * 图形验证码
     */
    @IgnoreAuth
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成数字验证码
        String text = (int)(Math.random()*9000+1000)+"";
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(SESSIN_KEY_IMAGE, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
    }

    /**
     * 检查图形验证码是否正确
     */
    @SuppressWarnings({"all"})
    public static boolean checkImgCode(HttpServletRequest request, String code) {
        String imageCode = ShiroUtils.getSessionAttribute(SESSIN_KEY_IMAGE)==null?"":ShiroUtils.getSessionAttribute(SESSIN_KEY_IMAGE).toString();
        if (XaUtils.isBlank(imageCode) || XaUtils.isBlank(code)) {
            return false;
        }
        boolean b = code.equals(imageCode);
        return b;
    }

    /**
     * 注册
     */
    @IgnoreAuth
    @PostMapping("/register")
    @ApiOperation(value = "注册", position = 2)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "password", value = "密码", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "msgcode", value = "短信验证码", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "imgcode", value = "图片验证码", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "userType", value = "用户类型, 1.app 2.web   这里默认传1", defaultValue = "1")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "响应成功"),
            @ApiResponse(code = 400, message = "请求无效 (Bad request)"),
            @ApiResponse(code = 401, message = "没有授权"),
            @ApiResponse(code = 405, message = "请求方式不支持"),
            @ApiResponse(code = 500, message = "服务器内部错误"),
            @ApiResponse(code = 403, message = "访问被禁止")
    })
    public XaResult<UserVo> register(String mobile, String password, String msgcode,String imgcode, Integer userType, HttpServletRequest request) {
        AbstractAssert.isBlank(mobile, "手机号不能为空");
        AbstractAssert.isOk(Validator.isMobile(mobile), "手机号格式不正确！");
        AbstractAssert.isBlank(password, "密码不能为空");
        AbstractAssert.isBlank(msgcode, "短信验证码不能为空");
        AbstractAssert.isBlank(imgcode, "图片验证码不能为空");
        AbstractAssert.isOk(checkCode(request, mobile, msgcode), "手机验证码不正确!!");
        AbstractAssert.isOk(checkImgCode(request, imgcode), "图片验证码不正确!!");

        XaResult<UserVo> xr = new XaResult<>();
        if (password.trim().length() < 6) {
            xr.error("密码最少6位！");
            return xr;
        }
        UserEntity userEntity = userService.queryByMobile(mobile);
        AbstractAssert.isOk(userEntity == null, "该手机号已注册");
        try {
            password = DesUtil.decrypt(password, DesUtil.DES_KEY_PASSWORD);
        } catch (Exception e) {
            throw new RRException("非法用户,加密失败!!!");
        }

        UserEntity entity = userService.registUser(mobile, password, userType);
        long userId = entity.getId();

        //生成token
        Map<String, Object> map = tokenService.createToken(userId);

        UserVo tbUserVo = new UserVo();
        beanMapper.copy(entity, tbUserVo);
        tbUserVo.setToken(map.get("token") + "");

        xr.setMsg("用户注册成功!");
        xr.setObject(tbUserVo);
        return xr;
    }

    /**
     * @param mobile
     * @return
     * @Title: sendCode
     * @Description: 发送短信验证码
     * <p>
     * HTML encodeURIComponent 编码后java后台的解码 http://blog.csdn.net/hejin17909mm/article/details/52527959
     */
    @SuppressWarnings("all")
    @IgnoreAuth
    @ApiOperation(value = "获取手机验证码", notes = "获取验证码，app端，ios/andriod必须开启对cookie的支持,当返回的code=1时，取出object进行显示,存放为单体对象", position = 5)
    @ResponseBody
    @RequestMapping(value = "getMobileCode", method = RequestMethod.POST)
    public XaResult<String> getMobileCode(
            @ApiParam(value = "类型,字段名:type(1:注册 2:忘记密码 3:修改密码 4:修改手机号码 5:手机号码登录 6:绑定用户),必填", defaultValue = "3") @RequestParam(value = "type") Integer type,
            @ApiParam(value = "用户Id,字段名:userId,(type=3)必传，否则不传", defaultValue = "1") @RequestParam(defaultValue = "0", required = false) Long userId,
            @ApiParam("手机号码,字段名:mobile,必填") @RequestParam(value = "mobile", defaultValue = "18049531390") String mobile,
            @ApiParam(value = "手机号码,字段名:mobile进行DES加密,具体加密方式要协商,加密之后的字符串要使用js的函数encodeURIComponent编码，否则包含+—==等特殊字符无法识别,必填", defaultValue = "BuzaVKuBLsGJPDUknqEe6Q==") @RequestParam(value = "mobileEncrypt") String mobileEncrypt,
            @ApiParam(value = "类型,字段名:device(1：ios、2：android、3：web) ", defaultValue = "3") @RequestParam(defaultValue = "0", required = false) int driverName,
            @ApiParam(value = "用户类型0:参常量：UserTypes；1.app 2.web", defaultValue = "2") @RequestParam(value = "userType") Integer userType,
            HttpServletRequest request) throws Exception {
        XaResult<String> xr = new XaResult<>();
        AbstractAssert.isNull(mobile, "手机号码不能为空");
        if (!Validator.isMobile(mobile)) {
            throw new RRException("手机号码格式不正确");
        }

        //APP校验
        if (XaUtils.isEmpty(mobileEncrypt)) {
            xr.error("mobileEncrypt不能为空");
            return xr;
        }
        String deMobile = null;
        try {
            deMobile = DesUtil.decrypt(mobileEncrypt, DesUtil.DES_KEY_MOBILE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("非法用户,加密失败!!");
        }
        AbstractAssert.isOk(mobile.equals(deMobile), "手机号加密错误!!");

        AbstractAssert.isNull(type, "type类型不能为空");
        AbstractAssert.isNull((type >= 1 && type <= 7), "type类型错误,取值范围[1,7]");
        AbstractAssert.isNull((type == 1 || type == 2 || type == 7) && XaUtils.isNotEmpty(userId), "userId不能为空");

        //验证手机号是否注册
        UserEntity userEntity = userService.queryByMobile(mobile);

        if (type != 6) {
            if (type == 2 || type == 3 || type == 4) {
                if (XaUtils.isEmpty(userEntity)) {
                    xr.error("该手机号尚未注册");
                    return xr;
                }
            } else if (type == 1) {
                //注册
                if (XaUtils.isNotEmpty(userEntity)) {
                    xr.error("该手机号已注册");
                    return xr;
                }
            }
        }
        //随机生成六位验证码
        Map<String, Object> phoneAndCode = new HashMap<>();
        long code = (long) (Math.random() * 899999) + 100000;
        logger.info("******************手机号=" + mobile + "*******验证码=" + code + "****************************");
        if (springBootMyApplication.isOpenShortMsg()) {
            phoneAndCode.put(mobile, code);
            phoneAndCode.put(SESSION_MSG_SENDTIME_KEY, new Date());
            ShiroUtils.setSessionAttribute(SESSION_KEY, phoneAndCode);
            String s = request.getSession().getId();
            return xr.success(code + "");
        }

        //HashMap<String, Object> result = YuntongxunSmsUtil.sendMessege(mobile, Constant.YuntongxunSmsTemplate.COMMON.getTcode(), new String[]{actionStr, code + "", SESSION_MSG_TIME_KEY+""});
        HashMap<String, Object> result = YuntongxunSmsUtil.sendMessege(mobile, Constant.YuntongxunSmsTemplate.COMMON.getTcode(), new String[]{code + "", SESSION_MSG_TIME_KEY + "分钟"});
        if ("160038".equals((String) result.get("statusCode"))) {
            xr.error("短信验证码发送过频繁，请稍后再试！");
            //return xr;
        }
        if ("000000".equals((String) result.get("statusCode"))) {
            //将生成的验证码存到Map集合中返回到前端
            phoneAndCode.put(mobile, code);
            phoneAndCode.put(SESSION_MSG_SENDTIME_KEY, new Date());
            //将生成的验证码保存在session中用来判断验证码是否正确
            ShiroUtils.setSessionAttribute(SESSION_KEY, phoneAndCode);
            //设置有效时间为5分钟
            //session.setMaxInactiveInterval(SESSION_MSG_TIME_KEY * 60);
            //if (XaUtil.isNotEmpty(userId)) {
            //    xr.success(phoneAndCode);
            //}
        } else {
            String errorMsg = XaUtils.isEmpty(result.get("statusMsg")) ? null : result.get("statusMsg").toString();
            if (XaUtils.isNotEmpty(errorMsg)) {
                xr.error(errorMsg);
            } else {
                xr.error("验证码发送失败，请稍后再试");
            }
            logger.error("云通讯statusCode:" + result.get("statusCode" + "(" + result.get("statusMsg") + ")"), new Throwable("云通讯验证码发送失败"));
        }
        return xr;
    }

    /**
     * 登录
     */
    @SuppressWarnings({"all"})
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "登录接口;该项目中未说明的时间", position = 1)
    public XaResult<UserVo> login(
            @ApiParam(value = "手机号,字段名:mobile") @RequestParam(value = "mobile", defaultValue = "13891572551") String mobile,
            @ApiParam("密码（加密）,字段名:password") @RequestParam(value = "password", defaultValue = "y51njc/FZpU=") String password,
            @ApiParam("用户类型1app2web,字段名:userType") @RequestParam(value = "userType", defaultValue = "1") Integer userType,
            HttpServletRequest request
    ) {
        AbstractAssert.isBlank(mobile, "手机号不能为空!");
        AbstractAssert.isBlank(password, "密码不能为空!");
        XaResult<UserVo> xr = new XaResult<>();

        try {
            password = DesUtil.decrypt(password, DesUtil.DES_KEY_PASSWORD);

        } catch (Exception e) {
            throw new RRException("非法用户,加密失败!!!");
        }

        //登录
        UserEntity userEntity = userService.login(mobile, password);
        //生成token
        Map<String, Object> map = tokenService.createToken(userEntity.getId().longValue());

        UserVo userVo = new UserVo();
        beanMapper.copy(userEntity, userVo);
        userVo.setToken(map.get("token") + "");

        xr.setObject(userVo);
        xr.setMsg("用户登录成功！");

        return xr;
    }

    /**
     * @param mobile
     * @return
     * @Title: 忘记密码
     * @Description: 忘记密码
     */
    @IgnoreAuth
    @ApiOperation(value = "忘记密码(登录)", notes = "忘记密码,当返回的code=1时，保存成功后object返回对象", position = 4)
    @ResponseBody
    @PostMapping(value = "pwdForget")
    public XaResult<String> pwdForget(
            @ApiParam("手机号,字段名:mobile") @RequestParam(value = "mobile", defaultValue = "13891572551") String mobile,
            @ApiParam("验证码,字段名:msgcode") @RequestParam String msgcode,
            @ApiParam("图片验证码,字段名:imgcode") @RequestParam String imgcode,
            @ApiParam("新密码,字段名:newPassword") @RequestParam(value = "newPassword") String newPassword,
            HttpServletRequest request) {
        XaResult<String> xr = new XaResult<>();
        AbstractAssert.isBlank(mobile, "手机号码不能为空!");
        AbstractAssert.isBlank(newPassword, "密码不能为空!");

        try {
            newPassword = DesUtil.decrypt(newPassword, DesUtil.DES_KEY_PASSWORD);
        } catch (Exception e) {
            throw new RRException("非法用户,加密失败!!!");
        }

        AbstractAssert.isOk(Validator.isMobile(mobile), "手机号码格式不正确!");
        AbstractAssert.isBlank(msgcode, "手机验证码不能为空!");
        AbstractAssert.isOk(checkCode(request, mobile, msgcode), "手机验证码不正确!!");
        AbstractAssert.isOk(checkImgCode(request, imgcode), "图片验证码不正确!!");

        userService.forgetPwd(mobile, newPassword);

        xr.success("密码重置成功！");

        return xr;
    }

    /**
     * @return
     * @Title: 修改密码
     * @Description: 修改密码
     */
    @ApiOperation(value = "修改密码(登录)", notes = "修改密码,当返回的code=1时，保存成功后object返回对象", position = 3)
    @ResponseBody
    @RequestMapping(value = "pwdUpdate", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, defaultValue = "33bc2411-1065-4a35-963c-2678d5226538")
    public XaResult<String> pwdUpdate(
            @ApiParam("手机号,字段名:mobile") @RequestParam(value = "mobile", defaultValue = "13891572551") String mobile,
            @ApiParam("验证码,字段名:msgcode") @RequestParam String msgcode,
            @ApiParam("图片验证码,字段名:imgcode") @RequestParam String imgcode,
            @ApiParam("新密码,字段名:password") @RequestParam String password,
            HttpServletRequest request
    ) {
        XaResult<String> xr = new XaResult<>();
        AbstractAssert.isBlank(mobile, "手机号码不能为空!");
        AbstractAssert.isOk(Validator.isMobile(mobile), "手机号码格式不正确!");
        AbstractAssert.isBlank(msgcode, "手机验证码不能为空!");
        AbstractAssert.isOk(checkCode(request, mobile, msgcode), "手机验证码不正确!!");
        AbstractAssert.isBlank(msgcode, "图片验证码不能为空!");
        AbstractAssert.isOk(checkImgCode(request, imgcode), "图片验证码不正确!!");
        AbstractAssert.isBlank(password, "密码不能为空!");

        UserEntity userEntity = tokenService.queryUserByRequest(request);

        if (userEntity == null) {
            xr.error("用户不存在！");
            return xr;
        }
        if (!mobile.equalsIgnoreCase(userEntity.getTel())) {
            xr.error("请使用自己的手机号！");
            return xr;
        }
        try {
            password = DesUtil.decrypt(password, DesUtil.DES_KEY_PASSWORD);
        } catch (Exception e) {
            throw new RRException("非法用户,加密失败!!!");
        }
        userService.updatePwd(userEntity.getId(), password);
        return xr.setObject("密码修改成功！");
    }

    /**
     * 检查验证码是否正确
     *
     * @param request
     * @param phone
     * @param code
     * @return
     */
    @SuppressWarnings({"all"})
    public static boolean checkCode(HttpServletRequest request, String phone, String code) {
        HashMap<String, Object> phoneAndCodeMap = (HashMap<String, Object>) ShiroUtils.getSessionAttribute(SESSION_KEY);
        if (XaUtils.isEmpty(phoneAndCodeMap)) {
            return false;
        }
        Date sendTime = (Date) phoneAndCodeMap.get(SESSION_MSG_SENDTIME_KEY);
        if (System.currentTimeMillis() - sendTime.getTime() > SESSION_MSG_TIME_KEY * 60 * 1000) {
            ShiroUtils.setSessionAttribute(SESSION_KEY, null);
            return false;
        }
        boolean b = phoneAndCodeMap.containsKey(phone) && phoneAndCodeMap.containsValue(Long.valueOf(code));
        return b;
    }

    /**
     * 修改头像
     */
    @PostMapping("updateHead")
    @ApiOperation(value = "修改头像")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, defaultValue = "33bc2411-1065-4a35-963c-2678d5226538")
    public XaResult<UserEntity> updateHead(
            @ApiParam(value = "头像,字段名:headPortrait") @RequestParam(value = "headPortrait", defaultValue = "") String headPortrait,
            HttpServletRequest request
    ) {
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        userEntity.setHeadPortrait(headPortrait==null?"":headPortrait);
        userService.update(userEntity);
        XaResult<UserEntity> xr = new XaResult<>();
        xr.success(userEntity);
        return xr;
    }

    /**
     * 修改昵称
     */
    @PostMapping("updateNickName")
    @ApiOperation(value = "修改昵称")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, defaultValue = "33bc2411-1065-4a35-963c-2678d5226538")
    public XaResult<UserEntity> updateNickName(
            @ApiParam(value = "昵称,字段名:nickName") @RequestParam(value = "nickName", defaultValue = "") String nickName,
            HttpServletRequest request
    ) {
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        userEntity.setNickName(nickName==null?"":nickName);
        userService.update(userEntity);
        XaResult<UserEntity> xr = new XaResult<>();
        xr.success(userEntity);
        return xr;
    }

    /**
     * 修改个性签名
     */
    @PostMapping("updateSignature")
    @ApiOperation(value = "修改个性签名")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, defaultValue = "33bc2411-1065-4a35-963c-2678d5226538")
    public XaResult<UserEntity> updateSignature(
            @ApiParam(value = "个性签名,字段名:signature") @RequestParam(value = "signature", defaultValue = "") String signature,
            HttpServletRequest request
    ) {
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        userEntity.setSignature(signature==null?"":signature);
        userService.update(userEntity);
        XaResult<UserEntity> xr = new XaResult<>();
        xr.success(userEntity);
        return xr;
    }

    /**
     * 用户个人中心接口
     * @param request
     * @return
     */
    @GetMapping("/info")
    @ApiOperation("获取用户详情")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<UserEntity> info(HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        XaResult<UserEntity> xr = new XaResult<>();
        xr.success(userEntity);
        return xr;
    }

    /**
     * id查询用户详情
     */
    @GetMapping("/infoByUserId")
    @ApiOperation("id查询用户详情")
    @IgnoreAuth
    public XaResult<UserEntity> infoByUserId(
            @ApiParam("主键,字段名:id,必填") @RequestParam(value = "id") Integer id
    ){
        UserEntity userEntity = userService.queryObject(id);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        XaResult<UserEntity> xr = new XaResult<>();
        xr.success(userEntity);
        return xr;
    }

    /**
     * 获取用户最新认证状态
     * @param request
     * @return
     */
    @GetMapping("/getExamineType")
    @ApiOperation("获取用户最新认证状态")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public XaResult<InfExamineEntity> getUserAuthentication(HttpServletRequest request){
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        InfExamineEntity examineEntity = infExamineService.queryObjectByUserId(userEntity.getId());
        XaResult<InfExamineEntity> xaResult = new XaResult<>();
        xaResult.setObject(examineEntity);
        return xaResult;
    }

    /**
     * 申请认证
     */
    @PostMapping("applyExamine")
    @ApiOperation(value = "申请认证")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, defaultValue = "33bc2411-1065-4a35-963c-2678d5226538")
    public XaResult<InfExamineEntity> applyExamine(
            @ApiParam(value = "认证类型,字段名:userType【1.企业认证 2.作者认证 3.媒体认证】") @RequestParam(value = "userType",defaultValue = "2")Integer userType,
            @ApiParam(value = "姓名,字段名:userName") @RequestParam(value = "userName", defaultValue = "")String userName,
            @ApiParam(value = "身份证号,字段名:userIdCard") @RequestParam(value = "userIdCard", defaultValue = "")String userIdCard,
            @ApiParam(value = "手机号码,字段名:userTel") @RequestParam(value = "userTel", defaultValue = "")String userTel,
            @ApiParam(value = "电子邮箱,字段名:userEmail") @RequestParam(value = "userEmail", defaultValue = "")String userEmail,
            @ApiParam(value = "证件照,字段名:userCertificatesImage") @RequestParam(value = "userCertificatesImage", defaultValue = "")String userCertificatesImage,
            @ApiParam(value = "企业名称,字段名:enterpriseName") @RequestParam(value = "enterpriseName", defaultValue = "")String enterpriseName,
            @ApiParam(value = "企业证件号,字段名:enterpriseIdCard") @RequestParam(value = "enterpriseIdCard", defaultValue = "")String enterpriseIdCard,
            @ApiParam(value = "营业执照,字段名:enterpriseImage") @RequestParam(value = "enterpriseImage", defaultValue = "")String enterpriseImage,
            HttpServletRequest request
    ) {
        UserEntity userEntity = tokenService.queryUserByRequest(request);
        if(XaUtils.isEmpty(userEntity)){
            throw new RRException("未找到该用户");
        }
        InfExamineEntity examineEntity = infExamineService.queryObjectByUserId(userEntity.getId());
        if(XaUtils.isNotEmpty(examineEntity) && examineEntity.getResult() == Constant.ExamineType.TYPE_EXAMINE_UNTREATED){
            throw new RRException("认证申请正在审核中，请勿重复申请");
        }
        AbstractAssert.isBlank(userName, "姓名不能为空!");
        AbstractAssert.isBlank(userIdCard, "身份证号不能为空!");
        AbstractAssert.isBlank(userTel, "手机号码不能为空!");
        AbstractAssert.isBlank(userEmail, "电子邮箱不能为空!");
        AbstractAssert.isBlank(userCertificatesImage, "证件照不能为空!");
        if(Constant.UserType.TYPE_USER_ENTERPRISE == userType){
            AbstractAssert.isBlank(enterpriseName, "企业名称不能为空!");
            AbstractAssert.isBlank(enterpriseIdCard, "企业证件号不能为空!");
            AbstractAssert.isBlank(enterpriseImage, "营业执照不能为空!");
        }
        InfExamineEntity infExamineEntity = new InfExamineEntity();
        infExamineEntity.setCreateDate(LocalDateTime.now());
        infExamineEntity.setResult(Constant.ExamineType.TYPE_EXAMINE_UNTREATED);
        infExamineEntity.setUserId(userEntity.getId());
        infExamineEntity.setUserType(userType);
        infExamineEntity.setUserName(userName);
        infExamineEntity.setUserIdCard(userIdCard);
        infExamineEntity.setUserTel(userTel);
        infExamineEntity.setUserEmail(userEmail);
        infExamineEntity.setUserCertificatesImage(userCertificatesImage);

        infExamineEntity.setEnterpriseName(enterpriseName);
        infExamineEntity.setEnterpriseIdCard(enterpriseIdCard);
        infExamineEntity.setEnterpriseImage(enterpriseImage);
        infExamineService.save(infExamineEntity);

        XaResult<InfExamineEntity> xr = new XaResult<>();
        xr.success(infExamineEntity);
        return xr;
    }

}
