package com.ourslook.guower.config;

import com.ourslook.guower.utils.shiro.UserRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro配置
 * <p>
 * SpringBoot+Shiro学习之数据库动态权限管理和Redis缓存
 * http://www.jianshu.com/p/22f78f8677f3
 * <p>
 * springboot 从redis取缓存的时候java.lang.ClassCastException:异常
 * http://blog.csdn.net/u014104286/article/details/67639893
 * <p>
 * //@see com.ourslook.guower.utils.shirocas.ShiroCasConfiguration
 *
 * @see UserRealm
 */
@EnableConfigurationProperties(value = {RedisProperties.class, OurslookConfig.class})
@Configuration
public class ShiroConfig {

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private OurslookConfig springBootMyApplication;

    //ehCacheManager 使用 start ---
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache_shiro.xml");
        return ehCacheManager;
    }

    private SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //cookie生效时间，单位：秒
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
    // end -----


    /**
     * 配置shiro redisManager
     *
     * @return
     */
    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost());
        redisManager.setPort(redisProperties.getPort());
        redisManager.setTimeout(redisProperties.getTimeout());
        redisManager.setPassword(redisProperties.getPassword());
        //过期时间单位是秒，默认30分钟=1800s
        redisManager.setExpire(1800);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     *
     * @return
     */
    private RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     */
    private RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    //----------------------------以上均是 第三方 redis相关----------------------

    /**
     * 这个类必须是static的，否则 shiro 的配置 总是 优先于 spring 的配置文件
     * <p><a href="">spring boot整合shiro引用配置文件配置是出现的问题</a></></p>
     *
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        if (springBootMyApplication.isRedisSessionEnable()) {
            //使用shiro新增加的
            sessionManager.setSessionDAO(redisSessionDAO());
        }
        return sessionManager;
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager());
        //用户授权信息Cache, 采用EhCache，本地缓存最长时间应比中央缓存时间短一些，以确保Session中doReadSession方法调用时更新中央缓存过期时间
        securityManager.setCacheManager(ehCacheManager());

        //使用redis新增加的
        if (springBootMyApplication.isRedisSessionEnable()) {
            // 自定义缓存实现 使用redis
            // 这里的cache不是用，不使用redis，redis的cache频繁的doReadSession
            //这里一直在频繁的调用！！！！
            //securityManager.setCacheManager(redisCacheManager());
        }

        return securityManager;
    }

    /**
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login.html");
        //shiroFilter.setSuccessUrl("/index.html");
        shiroFilter.setUnauthorizedUrl("/");

        /**
         * http://blog.csdn.net/catoop/article/details/50520958
         * @see FormAuthenticationFilter
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/public/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/api/**", "anon");
        filterMap.put("/share/**", "anon");
        filterMap.put("/rs/**", "anon");
        filterMap.put("/qr/**", "anon");
        filterMap.put("/upload/**", "anon");
        //swagger配置
        filterMap.put("/swagger**", "anon");
        filterMap.put("/v2/api-docs**", "anon");
        filterMap.put("/doc**", "anon");
        filterMap.put("/rs**", "anon");
        filterMap.put("/swagger-jersey-docs/index.html", "anon");
        filterMap.put("/swagger-resources/configuration/ui", "anon");

        filterMap.put("/ueditor/**", "anon");
        filterMap.put("/constants/**", "anon");

        filterMap.put("/login.html", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/captcha.jpg**", "anon");

        for (int i = 0; i < ignoreExt.size(); ++i) {
            // /**/*.htm
            filterMap.put("/**/*" + ignoreExt.get(i), "anon");
            filterMap.put("/**/*" + ignoreExt.get(i).toUpperCase(), "anon");
        }

        filterMap.put("/**", "authc");
        filterMap.put("/logout/**", "logout");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    private static List<String> ignoreExt;

    static {
        ignoreExt = new ArrayList<>();
        String extStr = "css|js|jpg|jpeg|gif|png|ico|bmp|gz|xml|zip|rar|swf|txt|xls|xlsx|flv|mid|doc|ppt|pdf|mp3|wma";
        for (int i = 0; i < extStr.split("\\|").length; ++i) {
            String ext = extStr.split("\\|")[i];
            ignoreExt.add("." + ext);
        }
        //字体文件
        ignoreExt.add(".woff");
        ignoreExt.add(".docx");
        ignoreExt.add(".pptx");
        ignoreExt.add(".ttf");
        ignoreExt.add(".woff2");
        ignoreExt.add(".svg");
        ignoreExt.add(".eot");
    }

}
