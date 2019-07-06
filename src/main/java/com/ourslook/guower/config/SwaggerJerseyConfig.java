package com.ourslook.guower.config;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.AcceptHeaderApiListingResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dazer
 * @date 2018/4/14 下午2:44
 * <p>
 * Resful Jersey 形式
 * @see SwaggerSpringmvcConfig
 * <p>
 * Spring Boot + Jersey https://blog.csdn.net/kiwi_coder/article/details/32940025
 *
 * cxf-rs 和 swagger 的点 https://www.cnblogs.com/zno2/p/5553319.html
 *
 * Swagger Core RESTEasy 2.X Project Setup 1.5 https://github.com/swagger-api/swagger-core/wiki/Swagger-Core-Jersey-2.X-Project-Setup-1.5
 *
 * http://localhost:8981/guower/rs/rest/city
 * http://localhost:8981/guower/rs/swagger.json
 * http://localhost:8981/guower/rs/swagger.yaml
 * http://localhost:8981/guower/rs/swagger
 */
@Configuration
public class SwaggerJerseyConfig {

    /**
     * @return
     * @see ServletContainer
     * @see SwaggerJerseyConfig
     * @see ApiModelProperty
     */
    @Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ServletContainer(), "/rs/*");
        //ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ServletContainer(), "/*");
        registrationBean.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyResourceConfig.class.getName());
        return registrationBean;
    }

    public static class JerseyResourceConfig extends ResourceConfig {
        public JerseyResourceConfig() {
            this.registerEndpoints();
            this.configureSwagger();
        }

        private void registerEndpoints() {
            register(RequestContextFilter.class);
            // Available at localhost:port/swagger.json
            // http://localhost:port/guower/rs/swagger.json
            /**
             * @see io.swagger.jersey.config.JerseyJaxrsConfig
             */
            this.register(ApiListingResource.class);
            this.register(AcceptHeaderApiListingResource.class);
            this.register(SwaggerSerializers.class);
            /**
             * 配置restful classes eg:register(DemoResource.class);packages("com.ourslook.guower.webrs");
             * 配置restful package.
             */
            packages("com.ourslook.guower.webrs");
//            register(DemoResource.class);
        }

        /**
         * 这里配置的host和basepath仅仅是作为swagger的基请求地址
         */
        @SuppressWarnings("all")
        private void configureSwagger() {
            BeanConfig config = new BeanConfig();
            config.setResourcePackage("com.ourslook.guower.webrs");//resourcePackage ：JAX-RS 的资源包
            //config.setHost("http://localhost:8981/guower");//host：访问文档地址
            config.setBasePath("guower/rs");//basePath ：所有的资源url pattern 再追加，例如：某个java类中的资源@Path("/user") ，则这个资源最终是 api/user
            config.setConfigId("springboot-jersey-swagger-docker-example");
            config.setTitle("Spring Boot + Jersey + Swagger + Docker Example");
            config.setVersion("v1");
            config.setContact("dazers@aliyun.com");
            //config.setSchemes(new String[]{"http","https"});
            config.setPrettyPrint(true);
            config.setScan(true);
        }
    }
}





