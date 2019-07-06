//package com.ourslook.guower.utils.shirocas;
//
//import com.ourslook.guower.service.common.TbUserService;
//import com.ourslook.guower.utils.shirocas.session.CasLogoutFilter;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.cas.CasFilter;
//import org.apache.shiro.cas.CasSubjectFactory;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.jasig.cas.client.session.SingleSignOutFilter;
//import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.Ordered;
//import org.springframework.web.filter.DelegatingFilterProxy;
//
//import javax.servlet.Filter;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//
///**
// * Shiro集成Cas配置
// *
// * @author 单红宇(365384722)
// * @myblog http://blog.csdn.net/catoop/
// * @create 2016年1月17日
// *
// * springboot 下将shiro-cas替换为buji-pac4j
// * http://blog.csdn.net/ywslakers123/article/details/78288112
// *
// * @see com.ourslook.guower.config.ShiroConfig
// *
// */
//@SuppressWarnings("all")
//@Configuration //不是用cas 暂时注释
//public class ShiroCasConfiguration {
//
//    private static final Logger logger = LoggerFactory.getLogger(ShiroCasConfiguration.class);
//
//    /**
//     * ehcache memcache redis
//     * @return
//     */
//    @Bean
//    public EhCacheManager getEhCacheManager() {
//        EhCacheManager em = new EhCacheManager();
//        em.setCacheManagerConfigFile("classpath:ehcache_shiro.xml");
//
//        //Shiro 设置session超时时间 http://blog.csdn.net/zsg88/article/details/69488728
//        //小于0的话是session永不过期
//        //SecurityUtils.getSubject().getSession().setTimeout(1800000 * 10);//30min，单位是ms //
//        return em;
//    }
//
//    @Lazy
//    @Bean(name = "myShiroCasRealm")
//    public MyShiroCasRealm myShiroCasRealm(EhCacheManager cacheManager) {
//        MyShiroCasRealm realm = new MyShiroCasRealm();
//        realm.setCacheManager(cacheManager);
//        return realm;
//    }
//
//    /**
//     * 注册单点登出listener
//     * @return
//     */
//    @Bean
//    public ServletListenerRegistrationBean singleSignOutHttpSessionListener(){
//        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
//        bean.setListener(new SingleSignOutHttpSessionListener());
//        //bean.setFilename(""); //默认为bean name
//        bean.setEnabled(true);
//        //bean.setOrder(Ordered.HIGHEST_PRECEDENCE); //设置优先级
//        return bean;
//    }
//
//    /**
//     * 注册单点登出filter
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean singleSignOutFilter(){
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilename("singleSignOutFilter");
//        bean.setFilter(new SingleSignOutFilter());
//        bean.addUrlPatterns("/*");
//        bean.setEnabled(true);
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }
//
//
//    /**
//     * 注册DelegatingFilterProxy（Shiro）
//     *
//     * @return
//     * @author SHANHY
//     * @create 2016年1月13日
//     */
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
//        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
//        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
//        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
//        filterRegistration.setEnabled(true);
//        filterRegistration.addUrlPatterns("/*");
//        return filterRegistration;
//    }
//
//    @Bean(name = "lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean
//    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
//        daap.setProxyTargetClass(true);
//        return daap;
//    }
//
//    //SessionManager
//    //new DefaultWebSessionManager()
//    @Bean(name = "securityManager")
//    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroCasRealm myShiroCasRealm) {
//        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
//        dwsm.setRealm(myShiroCasRealm);
//        //<!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
//        dwsm.setCacheManager(getEhCacheManager());
//        // 指定 SubjectFactory
//        dwsm.setSubjectFactory(new CasSubjectFactory());
//
//        //dazer add
//        SecurityUtils.setSecurityManager(dwsm);
//
//        return dwsm;
//    }
//
//
//    @Bean
//    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
//
//    /**
//     * CAS过滤器
//     *
//     * @return
//     * @author SHANHY
//     * @create 2016年1月17日
//     */
//    @Bean(name = "casFilter")
//    public CasFilter getCasFilter(MyShiroCasRealm myShiroCasRealm) {
//        CasFilter casFilter = new CasFilter();
//        casFilter.setFilename("casFilter");
//        casFilter.setEnabled(true);
//        // 登录失败后跳2`
//        // 转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
//        // 我们选择认证失败后再打开登录页面
//        casFilter.setFailureUrl(myShiroCasRealm.springBootMyApplication.getCasclientLoginUrl());
//        return casFilter;
//    }
//
//    /**
//     * ShiroFilter<br/>
//     * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
//     * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
//     *
//     * @param stuService
//     * @param scoreDao
//     * @return
//     * @author SHANHY
//     * @create 2016年1月14日
//     */
//    @Bean(name = "shiroFilter")
//    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, CasFilter casFilter, CasLogoutFilter casLogoutFilter, TbUserService stuService, MyShiroCasRealm myShiroCasRealm) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        // 必须设置 SecurityManager
//        //SecurityUtils.setSecurityManager();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        shiroFilterFactoryBean.setLoginUrl(myShiroCasRealm.springBootMyApplication.getCasclientLoginUrl());
//        // 登录成功后要跳转的连接
//        //如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        //shiroFilterFactoryBean.setSuccessUrl("/index");
//        shiroFilterFactoryBean.setSuccessUrl("/redirect/safetyweb");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//        // 添加casFilter到shiroFilter中
//        Map<String, Filter> filters = new HashMap<>();
//
//        filters.put("casLogout", casLogoutFilter);
//        filters.put("casFilter", casFilter);
//
//        shiroFilterFactoryBean.setFilters(filters);
//
//        loadShiroFilterChain(shiroFilterFactoryBean, stuService, myShiroCasRealm);
//        return shiroFilterFactoryBean;
//    }
//
//    /**
//     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
//     *
//     * @author SHANHY
//     * @create 2016年1月14日
//     */
//    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean, TbUserService stuService, MyShiroCasRealm myShiroCasRealm) {
//        /////////////////////// 下面这些规则配置最好配置到配置文件中 ///////////////////////
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//
//        // shiro集成cas后，首先添加该规则
//        filterChainDefinitionMap.put(myShiroCasRealm.springBootMyApplication.getCasclientCasFilterUrlPattern(), "casFilter");
//
//        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
//        // anon：它对应的过滤器里面是空的,什么都没做
//        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
//        filterChainDefinitionMap.put("/login", "anon");
//        //filterChainDefinitionMap.put("/static/**", "anon");//可以匿名（静态资源）
//        //filterChainDefinitionMap.put("/webjars/**", "anon");//可以匿名（静态资源）
//        filterChainDefinitionMap.put("/swagger-ui.html", "anon");//可以匿名(swagger-ui)
//        filterChainDefinitionMap.put("/api/**", "anon");//可以匿名（静态资源）
//        filterChainDefinitionMap.put("/api/whq/findWhquantityList", "anon");//可以匿名（静态资源）
//        filterChainDefinitionMap.put("/api/whq/findTestdetailVoList", "anon");//可以匿名（静态资源）
//        filterChainDefinitionMap.put("/api/whq/findPesttestdetailVoList", "anon");//可以匿名（静态资源）
//        //filterChainDefinitionMap.put("/logout", "logout"); //注释掉；等于去掉filter的拦截，自己处理logout逻辑,见：LoginController
//        filterChainDefinitionMap.put("/index", "user");//表示必须存在用户x
//        filterChainDefinitionMap.put("/", "user");// /** 和 / 拦截的范围是不一样的
//        filterChainDefinitionMap.put("/redirect/**", "user");// /** 和 / 拦截的范围是不一样的
//        //filterChainDefinitionMap.put("/**", "user");// /** 和 / 拦截的范围是不一样的
//        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
//        //filterChainDefinitionMap.put("/**", "authc");//anon 可以理解为不拦截  authc表示需要认证才能使用
//
//        shiroFilterFactoryBean.getFilterChainDefinitionMap().putAll(filterChainDefinitionMap);
//    }
//
//    //---------------------------解决session sso shiro 不能注销的问题-----------------------------
//    @Bean(name = "casLogoutFilter")
//    public CasLogoutFilter casLogoutFilter(DefaultWebSecurityManager securityManager){
//        CasLogoutFilter filter = new CasLogoutFilter();
//        //LogoutFilter
//        filter.setFilename("casLogoutFilter");
//        filter.setEnabled(false);
//        filter.setSessionManager(securityManager);
//        return filter;
//    }
//}