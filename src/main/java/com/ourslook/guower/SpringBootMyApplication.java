package com.ourslook.guower;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.ourslook.guower.config.OurslookConfig;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.beanmapper.OrikaBeanMapperImpl;
import com.ourslook.guower.utils.dfs.DefaultDfsClientImpl;
import com.ourslook.guower.utils.dfs.DfsClient;
import com.ourslook.guower.utils.dfs.FastDFSClientImpl;
import com.ourslook.guower.utils.office.csvexports.DefaultCsvExportor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.http.HttpStatus;

/**
 * @author dazer
 * on 2017/8/9
 * <p>
 * Import : 导入fdfs相关配置的注解
 * SpringBootApplication : spring boot 启动类必须的注解
 * ConfigurationProperties : spring boot 方便在properties文件进行配置的注解
 * ServletComponentScan ：当使用该注解，扫描Servlet组件时，Servlet、过滤器和监听器可以是通过@WebServlet、@WebFilter和@WebListener自动注册
 * <p>
 * <p>
 * SpringBoot使用devtools导致的类型转换异常
 * http://blog.csdn.net/m0_38043362/article/details/78064539
 */
@Import(FdfsClientConfig.class)
@SpringBootApplication
@ServletComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties(value = {ServerProperties.class, OurslookConfig.class})
public class SpringBootMyApplication implements ApplicationListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Lazy
    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private OurslookConfig ourslookConfig;

    /**
     * 使用外置tomcat启动注释掉启动入口;同时修改pom中的jar形式
     * <p>
     * 点击这里的main方法也是使用的外部的tomcat..
     * <p>
     * Spring Boot Maven Plugin打包异常及三种解决方法：Unable to find main class
     * https://www.cnblogs.com/thinking-better/p/7827368.html
     */
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringBootMyApplication.class);
        //添加一个启动后监听
        ApplicationContext ctx = (ApplicationContext) springApplication.run(args);
        System.out.println("main start.....");
    }

    @Bean
    protected EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
            ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/403.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
            container.addErrorPages(error401Page, error403Page, error404Page, error500Page);
        };
    }


    //================================================= 注入系统常用的Bean,直接使用，不要手工New  ================================================================
    @Bean(name = "csvExportor")
    public DefaultCsvExportor registUtilsCsvExportor() {
        return new DefaultCsvExportor();
    }

    @Bean(name = "beanMapper")
    public BeanMapper registUtilsBeanMapper() {
        return new OrikaBeanMapperImpl();
    }

    @Lazy
    @Bean(name = "dfsclient")
    public DfsClient registUtilsDfs() {
        if (ourslookConfig.isFastdfsEnable()) {
            return new FastDFSClientImpl();
        }
        return new DefaultDfsClientImpl();
    }
    //================================================= Spring Boot 注册servlet  ================================================================

    /**
     * 监听启动事件
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 在这里可以监听到Spring Boot的生命周期
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            logger.info("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) {
            logger.info("初始化完成");
        } else if (event instanceof ContextRefreshedEvent) {
            logger.info("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {
            logger.info("应用已启动完成,在这里默认访问一次数据库");
//
//            TbUserService tbUserService = SpringContextHelper.getBean(TbUserService.class);
//            TbUserEntity tbUserEntity = tbUserService.queryObject(1L);
            logger.info("应用已启动完成,在这里默认访问一次数据库....sucess...");

            String version = SpringBootVersion.getVersion();
            logger.info("访问OK👌" + ";spring boot version:" + version);
            logger.info("http://127.0.0.1:" + serverProperties.getPort() + serverProperties.getContextPath() + "/index.html");
            logger.info("http://127.0.0.1:" + serverProperties.getPort() + serverProperties.getContextPath() + "/docs");

            //加载配置文件
        } else if (event instanceof ContextStartedEvent) {
            logger.info("应用启动，需要在代码动态添加监听器才可捕获");
        } else if (event instanceof ContextStoppedEvent) {
            logger.info("应用停止");
        } else if (event instanceof ContextClosedEvent) {
            logger.info("应用关闭");
        } else {
        }
    }

}
