//package com.ourslook.guower.utils.shirocas;
//
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.filter.mgt.FilterChainManager;
//import org.apache.shiro.web.filter.mgt.FilterChainResolver;
//import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
//import org.apache.shiro.web.mgt.WebSecurityManager;
//import org.apache.shiro.web.servlet.AbstractShiroFilter;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.beans.factory.BeanInitializationException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 继承 ShiroFilterFactoryBean 处理拦截资源文件问题。
// *
// * @see DefaultWebSessionManager
// *
// * @author   单红宇(365384722)
// * @myblog  http://blog.csdn.net/catoop/
// * @create    2016年3月8日
// */
//@SuppressWarnings("ALL")
//public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {
//    /**
//     * // 对ShiroFilter来说，需要直接忽略的请求
//     */
//    private Set<String> ignoreExt;
//
//    public MyShiroFilterFactoryBean() {
//        super();
//        ignoreExt = new HashSet<>();
//        String extStr = "css|js|jpg|jpeg|gif|png|ico|bmp|gz|xml|zip|rar|swf|txt|xls|xlsx|flv|mid|doc|ppt|pdf|mp3|wma";
//        for (int i = 0 ; i < extStr.split("\\|").length; ++i) {
//            String ext = extStr.split("\\|")[i];
//            ignoreExt.add("."+ext);
//        }
//        //字体文件
//        ignoreExt.add(".woff");
//        ignoreExt.add(".docx");
//        ignoreExt.add(".pptx");
//        ignoreExt.add(".ttf");
//        ignoreExt.add(".woff2");
//        ignoreExt.add(".svg");
//        ignoreExt.add(".eot");
//    }
//
//    @Override
//    protected AbstractShiroFilter createInstance() throws Exception {
//
//        SecurityManager securityManager = getSecurityManager();
//        if (securityManager == null) {
//            String msg = "SecurityManager property must be set.";
//            throw new BeanInitializationException(msg);
//        }
//
//        if (!(securityManager instanceof WebSecurityManager)) {
//            String msg = "The security manager does not implement the WebSecurityManager interface.";
//            throw new BeanInitializationException(msg);
//        }
//
//        //加载所有的访问url 拦截链 @dazer
//        FilterChainManager manager = createFilterChainManager();
//
//        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
//        chainResolver.setFilterChainManager(manager);
//
//        return new MSpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
//    }
//
//    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
//    private final class MSpringShiroFilter extends AbstractShiroFilter {
//
//        protected MSpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
//            super();
//            if (webSecurityManager == null) {
//                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
//            }
//            setSecurityManager(webSecurityManager);
//            if (resolver != null) {
//                setFilterChainResolver(resolver);
//            }
//        }
//
//        @SuppressWarnings("AliControlFlowStatementWithoutBraces")
//        @Override
//        protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
//                                        FilterChain chain) throws ServletException, IOException {
//            HttpServletRequest request = (HttpServletRequest)servletRequest;
//            String str = request.getRequestURI().toLowerCase();
//            // 因为ShiroFilter 拦截所有请求（在上面我们配置了urlPattern 为 * ，当然你也可以在那里精确的添加要处理的路径，这样就不需要这个类了），而在每次请求里面都做了session的读取和更新访问时间等操作，这样在集群部署session共享的情况下，数量级的加大了处理量负载。
//            // 所以我们这里将一些能忽略的请求忽略掉。
//            // 当然如果你的集群系统使用了动静分离处理，静态资料的请求不会到Filter这个层面，便可以忽略。
//            boolean flag = true;
//            int idx = 0;
//            if(( idx = str.indexOf(".")) > 0){
//                str = str.substring(idx);
//                //noinspection AliControlFlowStatementWithoutBraces
//                if(ignoreExt.contains(str.toLowerCase())) {
//                    flag = false;
//                }
//            }
//            if(flag){
//                super.doFilterInternal(servletRequest, servletResponse, chain);
//            }else{
//                chain.doFilter(servletRequest, servletResponse);
//            }
//        }
//
//    }
//}
