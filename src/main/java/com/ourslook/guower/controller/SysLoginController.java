package com.ourslook.guower.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ourslook.guower.config.ShiroConfig;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.annotation.LoggSys;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.shiro.UserRealm;
import com.ourslook.guower.utils.validator.AbstractAssert;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.ourslook.guower.utils.shiro.UserRealm.SHIRO_LOGIN_COUNT;

/**
 * 登录相关
 */
@CrossOrigin
@Controller
@SuppressWarnings("all")
public class SysLoginController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(SysLoginController.class);
    //单点登录完整地址 cas登录
    @Value("${ourslook.casclientLoginUrl:#{null}}")
    private String clientLoginUrl;
    //单点登录退出完整地址  cas退出
    @Value("${ourslook.casLogoutUrl:#{null}}")
    private String casLogoutUrl;

    /**
     * 验证码提供者
     */
    @Autowired
    private Producer producer;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    DefaultWebSessionManager sessionManager;

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
    }

    /**
     * 登录
     *
     * @see com.ourslook.guower.utils.shiro.UserRealm
     */
    @LoggSys("登录日志")
    @SuppressWarnings("all")
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String captcha) throws IOException {

        int errorTime = NumberUtils.toInt(ShiroUtils.getSessionAttribute(SHIRO_LOGIN_COUNT + username) + "");
        if (errorTime >= UserRealm.CAPTCHA_SHOW_TIMES) {
            AbstractAssert.isBlank(captcha, "请正确输入账号密码！", Constant.Code.code_login_errors_failure);
            if (StringUtils.isNotBlank(captcha)) {
                String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
                if (!captcha.equalsIgnoreCase(kaptcha)) {
                    return R.error(Constant.Code.code_login_errors_failure, "验证码不正确");
                }
            }
        }
        try {
            Subject subject = ShiroUtils.getSubject();
            //sha256加密
            password = new Sha256Hash(password).toHex();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            //设置记住密码
            subject.login(token);
        } catch (UnknownAccountException e) {
            //账号不存在
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            //密码不正确
            return R.error(e.getMessage());
        } catch (ExcessiveAttemptsException e) {
            //账号密码输入错误次数过多
            return R.error(Constant.Code.code_login_errors_failure, e.getMessage());
        } catch (LockedAccountException e) {
            //用户被锁定
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            logger.error("账户验证失败", e);
            return R.error("账户验证失败");
        }
        return R.ok();
    }

    /**
     * 退出
     *
     * @see ShiroConfig
     */
    @RequestMapping(value = "logout")
    public String logout() {
        //ShiroUtils.logout();
        return "redirect:login.html";
    }


    //=============================== cas login 相关 start ===========================================
    //cas 单点退出;不用cas注释掉  logout
//	@RequestMapping(value = "/logout", method = RequestMethod.GET)
//	public ModelAndView logout() {
//		//1:自己先销毁session
//		SecurityUtils.getSubject().logout();
//		//2:sso销毁session
//		return new ModelAndView("redirect:" + casLogoutUrl);
//	}
//
//	//cas 单点退出;不用cas注释掉  logoutweb
//	@RequestMapping(value = "/logoutweb", method = RequestMethod.GET)
//	public ModelAndView logoutweb() {
//		//1:自己先销毁session
//		SecurityUtils.getSubject().logout();
//		//2:sso销毁session
//		String casLogoutWebUrl = casLogoutUrl.replace("safety", "safety/redirect/safetyweb");
//		return new ModelAndView("redirect:" + casLogoutWebUrl);
//	}
    //=============================== cas login 相关 end ===========================================


    //=============================== 普通登录 相关 start ===============================================================
    //普通登录;
    //普通退出：使用shiro的filter
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public ModelAndView login() {
//        return new ModelAndView("login");
//    }
    //=============================== 普通登录 相关 start ===============================================================
}
