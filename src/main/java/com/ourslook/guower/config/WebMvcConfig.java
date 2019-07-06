package com.ourslook.guower.config;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.LoginUser;
import com.ourslook.guower.utils.encryptdir.AppSignature;
import com.ourslook.guower.utils.interceptor.AuthorizationInterceptor;
import com.ourslook.guower.utils.jackson.DateConverter;
import com.ourslook.guower.utils.jackson.JacksonUtils;
import com.ourslook.guower.utils.jackson.StringEncodeConverter;
import com.ourslook.guower.utils.resolver.LoginUserHandlerMethodArgumentResolver;
import com.ourslook.guower.utils.result.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.util.ResourceUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.beans.PropertyEditorSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MVC配置
 *
 * @see ShiroConfig
 */
@SuppressWarnings("all")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private OurslookConfig springBootMyApplication;

    /**
     * 如果使用springboot内置的tomcat这里有用，换成自己本地的tomcat
     * <p>
     * 线上环境，这里要注释掉
     * <p>
     */
    private final String springBootTomcatPath = "file:" + ResourceUtils.getFile("classpath:nginx.conf").getParentFile().getParentFile().getParentFile().getPath();
    /**
     * token验证相关
     */
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    /**
     * @see LoginUser 注入当前用户
     */
    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    public WebMvcConfig() throws Exception {
    }

    // 拦截器配置 spring-boot学习笔记之Web拦截器 http://www.jianshu.com/p/f14ed6ca4e56
    // spring拦截器、与filter的区别: http://blog.csdn.net/zqlsnow/article/details/52946826

    /**
     * 过滤器属于Servlet范畴的API，与spring 没什么关系。
     * Web开发中，我们除了使用 Filter 来过滤请web求外，还可以使用Spring提供的HandlerInterceptor（拦截器）。
     * HandlerInterceptor 的功能跟过滤器类似，但是提供更精细的的控制能力：
     * 在request被响应之前、request被响应之后、视图渲染之前以及request全部结束之后。
     * 我们不能通过拦截器修改request内容，但是可以通过抛出异常（或者返回false）来暂停request的执行。
     *
     * @see ShiroConfig
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(LocaleConfig.localeChangeInterceptor());
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/**");
        // 多个拦截器组成一个拦截器链
        registry.addInterceptor(new MyApiguowerInterceptor())
                //.addPathPatterns("/**") //所有的都进行拦截
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/whq/**",
                        "/api/fileUpload",
                        "/api/fileUploads",
                        "/api/fileDeletes",
                        "/api/oequestion/fileUploadFromExcel"
                ) //排除拦截
        ;
    }

    /**
     * 配置过滤器，这里过滤器主要是对（swagger json ）返回值做后继处理
     * https://blog.csdn.net/kokjuis/article/details/77371324
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ResponseSwaggerFilter());// 配置一个返回值加密过滤器
        //registration.addUrlPatterns("/*");
        registration.addUrlPatterns("/v2/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("swaggerResponseFilter");
        return registration;
    }

    /**
     * request io 流只能读取一次，这里 进行包装让可以多次读取
     * <p>
     * request io 可以多次读取处理的io流。
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean repeatlyReadFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatlyReadFilter());// 配置一个返回值加密过滤器
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("repeatlyReadFilterRegistration");
        return registration;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }

    /**
     * 下面两个设置允许跨域请求
     *
     * @see org.springframework.web.bind.annotation.CrossOrigin
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE", "POST", "GET", "COPY", "OPTIONS", "HEAD", "PUT", "PATCH", "LINK")
                .allowedHeaders("*")
                .exposedHeaders()
                .allowCredentials(true).maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    //================================================= Spring Boot 中文乱码解决  ================================================================

    @Autowired

    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * http://blog.csdn.net/xuqingge/article/details/53561529
     * Spring Boot完美使用FastJson解析JSON数据
     * spring boot 默认使用的json解析库比较陌生，使用我们熟悉的fastjson
     *
     * @param converters
     * @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
     * <p>
     * 序列化指定格式的double格式
     * @JsonSerialize(using = CustomDoubleSerialize.class)
     * @see com.fasterxml.jackson.annotation.JsonFormat  SpringMVC的Date与String互转  https://yq.aliyun.com/articles/47040
     * Json解析工具Jackson（使用注解） https://www.cnblogs.com/javaee6/p/3714766.html
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        //最终结局乱码问题
        converters.add(responseBodyConverter());
        //解决对象是空对象EmptyObject转成json出错的问题
        //# jackson配置，防止是Object的对象不能被序列化 #spring.jackson.default-property-inclusion: NON_NULL
        converters.add(JacksonUtils.jacksonMessageConverter());
        //#支持alibaba fastjson
        converters.add(JacksonUtils.fastJsonHttpMessageConverters());
        //#支持Gson
        converters.add(new GsonHttpMessageConverter());
    }

    /**
     * 终极结局response乱码问题;spring boot 乱码解决汇总
     * https://blog.csdn.net/wujianmin577/article/details/61197485
     *
     * @return
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    /**
     * WebMvcAutoConfiguration 会自动加载转换器，参加：addFormatters 方法
     * 或者重写addFormatters 手动添加转换器也可以
     * 自定义对象使用Date参见：
     *
     * @see org.springframework.format.annotation.DateTimeFormat @DateTimeFormat( pattern = "yyyy-MM-dd" )
     * @see stringConverter()
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverter(new DateConverter());
        registry.addConverter(new StringEncodeConverter());
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 上传文件处理,使用内置tomcat才会生效;
         * 上线环境注释掉
         */
        registry.addResourceHandler("/upload/**").addResourceLocations(
                XaUtils.parseFilePath(springBootTomcatPath + "/src/guowerUploadRoot/upload/"))
        ;
        /**
         * 项目引入webjars
         * http://www.webjars.org/
         */
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver());
        /**
         * 项目开启GZIP压缩
         *
         * 网站启用GZip压缩后，速度快了3倍！ https://blog.csdn.net/joeyon1985/article/details/48786653
         * 通过GZIP优化性能 https://blog.csdn.net/guduyishuai/article/details/60140445
         * Nginx开启Gzip压缩大幅提高页面加载速度 https://www.cnblogs.com/mitang/p/4477220.html
         * Nginx开启Gzip详解 https://blog.csdn.net/zhuyiquan/article/details/52709864
         *
         * #########nginx 配置
         * gzip on;#打开关闭
         * gzip_min_length 2k; #gzip 开启法制
         * gzip_buffers 4 16k;
         * gzip_comp_level 2; #压缩比例，一般1就够用了；压缩级别，1-10，数字越大压缩的越好，时间也越长，看心情随便改吧
         * gzip_types text/plain application/x-javascript text/css application/xml application/json text/javascript application/x-httpd-php image/jpeg image/gif image/png;
         * gzip_vary off;
         * gzip_disable "MSIE [1-6]\."; #IE6不开启
         *
         *
         * ########### tomcat配置GZip,两个配置二选一.
         */
        super.addResourceHandlers(registry);
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }


    //=================================================================================================================
    @SuppressWarnings("unchecked")
    private class MyApiguowerInterceptor extends HandlerInterceptorAdapter {
        //在请求处理之前进行调用（Controller方法调用之前）"
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler) throws Exception {
            logger.error("请求地址 、 接口地址 ================= :" + request.getRequestURI());
            //1:加密校验
            if (springBootMyApplication.isSecurityAcess()) {
                String signature = request.getHeader("signature");
                String privateKey = request.getHeader("privateKey");

                String driverName = request.getParameter("driverName");
                String uuid = request.getParameter("uuid");
                String timestamp = request.getParameter("timestamp");
                String requestUrl = request.getRequestURI();
                if (XaUtils.isEmpty(privateKey)) {
                    privateKey = request.getParameter("privateKey");
                }
                if (XaUtils.isEmpty(driverName)) {
                    driverName = request.getParameter("os");
                }

                Map<String, String[]> params = request.getParameterMap();
                String errorMsg = "";
                Map copyParams = new HashMap(params);
                if (XaUtils.isNotEmpty(errorMsg = AppSignature.isAuthUser(signature, driverName, uuid, privateKey, timestamp, copyParams))) {

                    R result = R.error(errorMsg);

                    //要加这个否则服务器返回的不是json,有的浏览器还乱码
                    response.setContentType(ServletUtils.JSON_TYPE);
                    response.getWriter().println(JSON.toJSONString(result));
                    response.getWriter().flush();
                    response.getWriter().close();
                    return false;
                }
            }
            // 只有返回true才会继续向下执行，返回false取消当前请求
            return true;
        }

        //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response,
                               Object handler, ModelAndView modelAndView) throws Exception {
            logger.trace("=======22:postHandle=======" + request.getRequestURI());
            super.postHandle(request, response, handler, modelAndView);
        }

        //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                    Object handler, Exception ex) throws Exception {
            logger.trace("=======33:afterCompletion=======" + request.getRequestURI());
            super.afterCompletion(request, response, handler, ex);
        }
    }

    /**
     * spring自定义属性编辑器CustomEditorConfigurer和注册方式
     *
     * @see org.springframework.beans.propertyeditors.CustomNumberEditor
     * @see org.springframework.beans.PropertyEditorRegistrySupport
     * <p>
     * java.lang.IllegalArgumentException: Invalid character found in the request target. The valid charact
     * <p>
     * 如果前端传递的参数中包含特殊的字符串，如 {} ， ￥ # 等特殊符号，部分浏览器会报错
     */
    public class SocketAddressEditor extends PropertyEditorSupport implements PropertyEditorRegistrar {
        @Override
        public void registerCustomEditors(PropertyEditorRegistry registry) {
            registry.registerCustomEditor(InetSocketAddress.class, this);
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            String[] addresses = StringUtils.split(text, ":");
            if (addresses.length > 0) {
                if (addresses.length != 2) {
                    throw new RuntimeException("address[" + text + "] is illegal, eg.127.0.0.1:3306");
                } else {
                    setValue(new InetSocketAddress(addresses[0], Integer.valueOf(addresses[1])));
                }
            } else {
                setValue(null);
            }
        }
    }

    /**
     * 返回值输出过滤器，这里用来加密返回值
     *
     * @author kokJuis
     * @Title: ResponseFilter
     * @Description:
     * @date 上午9:52:42
     */
    private class ResponseSwaggerFilter extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            //这里进行包装respone 进行再次编辑 修改
            ResponseWrapper wrapperResponse = new ResponseWrapper((HttpServletResponse) response);//转换成代理类
            // 这里只拦截返回，直接让请求过去，如果在请求前有处理，可以在这里处理
            filterChain.doFilter(request, wrapperResponse);

            byte[] content = wrapperResponse.getContent();//获取返回值
            //判断是否有值
            if (content.length > 0) {
                String str = new String(content, "UTF-8");
                logger.info("返回值:" + str);
                String ciphertext = null;
                try {
                    //根据需要处理返回值
                    ciphertext = StringUtils.replace(str, "http://localhost:8981/guower/", ServletUtils.getHttpRootPath((HttpServletRequest) request));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //把返回值输出到客户端
                ServletOutputStream out = response.getOutputStream();
                out.write(ciphertext.getBytes());
                out.flush();
            }
        }
    }

    /**
     * HttpServletResponse返回值输出代理类
     *
     * @author kokJuis
     * @Title: ResponseWrapper
     * @Description:
     * @date 上午9:52:11
     */
    private class ResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream buffer;

        private ServletOutputStream out;

        public ResponseWrapper(HttpServletResponse httpServletResponse) {
            super(httpServletResponse);
            buffer = new ByteArrayOutputStream();
            out = new WrapperOutputStream(buffer);
        }

        @Override
        public ServletOutputStream getOutputStream()
                throws IOException {
            return out;
        }

        @Override
        public void flushBuffer()
                throws IOException {
            if (out != null) {
                out.flush();
            }
        }

        public byte[] getContent()
                throws IOException {
            flushBuffer();
            return buffer.toByteArray();
        }

        class WrapperOutputStream extends ServletOutputStream {
            private ByteArrayOutputStream bos;

            public WrapperOutputStream(ByteArrayOutputStream bos) {
                this.bos = bos;
            }

            @Override
            public void write(int b)
                    throws IOException {
                bos.write(b);
            }

            @Override
            public boolean isReady() {
                return false;

            }

            @Override
            public void setWriteListener(WriteListener arg0) {
            }
        }
    }

    //======================================================================================================================
    private class RepeatlyReadFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            //Do nothing
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            if (httpRequest.getRequestURI().toLowerCase().contains("fileUpload") ||
                    httpRequest.getRequestURI().toLowerCase().contains("uploadApk") ||
                    httpRequest.getRequestURI().toLowerCase().contains("uploadsFile")
                    ) {
                logger.info("=========== 上传文件😄😄不过滤 =====================");
                chain.doFilter(request, response);
                return;
            }

            if (request instanceof HttpServletRequest) {
                request = new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
            }
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
            //Do nothing
        }
    }

    /**
     * 构建可重复读取inputStream的request
     * https://blog.csdn.net/bjo2008cn/article/details/53888923
     */
    private class RepeatedlyReadRequestWrapper extends HttpServletRequestWrapper {

        private static final int BUFFER_START_POSITION = 0;

        private static final int CHAR_BUFFER_LENGTH = 1024;
        /**
         * input stream 的buffer
         * 字符流 处理有问题
         */
        private final String body = "";
        /**
         * 字节流 可以处理任何数据，没有问题
         */
        private final List<Byte> bodyByte = new ArrayList<>();

        /**
         * @param request {@link HttpServletRequest} object.
         */
        public RepeatedlyReadRequestWrapper(HttpServletRequest request) {
            super(request);

            //这里是当做字符流来处理
           /* {
                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = null;
                try {
                    inputStream = request.getInputStream();
                } catch (IOException e) {
                    logger.error("Error reading the request body…", e);
                }
                if (inputStream != null) {
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                        char[] charBuffer = new char[CHAR_BUFFER_LENGTH];
                        int bytesRead;
                        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                            stringBuilder.append(charBuffer, BUFFER_START_POSITION, bytesRead);
                        }
                    } catch (IOException e) {
                        logger.error("Fail to read input stream", e);
                    }
                } else {
                    stringBuilder.append("");
                }
                body = stringBuilder.toString();
            }*/

            //如果是字节流，如何处理
            {
                InputStream inputStream = null;
                try {
                    inputStream = request.getInputStream();
                } catch (IOException e) {
                    logger.error("Error reading the request body…", e);
                }
                List<Byte> datas = new ArrayList<>();

                if (inputStream != null) {
                    // 设置一个，每次 装载信息的容器
                    byte[] buf = new byte[1024];
                    // 开始读取数据
                    int len = 0;// 每次读取到的数据的长度
                    int yy = 0;
                    try {
                        while ((len = inputStream.read(buf)) != -1) {// len值为-1时，表示没有数据了
                            //append方法往datas对象里面添加数据
                            for (yy = 0; yy < len; ++yy) {
                                datas.add(buf[yy]);
                            }
                        }
                        bodyByte.clear();
                        bodyByte.addAll(datas);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            //字符
            //final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

            //字节
            Byte[] bytes = new Byte[bodyByte.size()];
            bodyByte.toArray(bytes);
            byte[] bytess = new byte[bytes.length];
            for (int i = 0; i < bytes.length; ++i) {
                bytess[i] = bytes[i];
            }

            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytess);
            return new DelegatingServletInputStream(byteArrayInputStream);
        }
    }
}

