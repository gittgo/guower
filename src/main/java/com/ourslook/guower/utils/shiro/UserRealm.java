package com.ourslook.guower.utils.shiro;

import com.ourslook.guower.config.ShiroConfig;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.service.SysMenuService;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.ShiroUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 *
 * @see ShiroConfig
 */
@Component
public class UserRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(UserRealm.class);

    /**
     * 使用的@Lazy注解，不是用lazy集成的cachable注解就有问题
     */
    @Lazy
    @Autowired
    private SysUserService sysUserService;
    @Lazy
    @Autowired
    private SysMenuService sysMenuService;
    /**
     * 用户登录次数计数  redisKey 前缀
     */
    public static final String SHIRO_LOGIN_COUNT = "shiro_login_count_";
    /**
     * 用户密码错误3次出现验证码
     */
    public static final int CAPTCHA_SHOW_TIMES = 3;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();;
        Long userId = user.getUserId();
        //用户权限列表
        Set<String> permsSet = sysMenuService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        logger.info("---------------------开始验证登录信息login---------------------");
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        //查询用户信息
        logger.info("sysUser search start");
        SysUserEntity user = sysUserService.queryByUserName(username);
        logger.info("sysUser search end");

        //账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员!");
        }

        //密码错误
        if (!password.equals(user.getPassword())) {
            int errorTime = NumberUtils.toInt(ShiroUtils.getSessionAttribute(SHIRO_LOGIN_COUNT + username)+"", 1);
            ShiroUtils.setSessionAttribute(SHIRO_LOGIN_COUNT + username, errorTime + 1);

            //进行密码错误次数记录，超过三次抛出异常
            if (errorTime > CAPTCHA_SHOW_TIMES) {
                throw new IncorrectCredentialsException("账号或密码错误!!!");
            } else {
                throw new IncorrectCredentialsException("账号或密码错误!");
            }
        }
        //登录成功清除账号错误次数
        ShiroUtils.setSessionAttribute(SHIRO_LOGIN_COUNT + username, 0);


        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        logger.info("登录验证成功！！！");

        return info;
    }

}
