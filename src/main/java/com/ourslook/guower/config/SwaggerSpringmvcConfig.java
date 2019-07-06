package com.ourslook.guower.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * swagger在线文档和离线文档
 * https://www.cnblogs.com/tietazhan/p/7080219.html
 * <p>
 * 在springboot中整合jersey和springfox-swagger2
 * http://blog.51cto.com/skyfly/1901744
 * <p>
 * swagger2 导出离线Word/PDF/HTML文档
 * http://www.leftso.com/blog/402.html
 * <p>
 * Swagger2离线文档：PDF和Html5格式
 * https://blog.csdn.net/fly910905/article/details/79131755
 * 代码：https://github.com/Fly0905/SwaggerOfflineDoc
 * <p>
 * Swagger文档转Word 文档
 * https://www.cnblogs.com/jmcui/p/8298823.html
 * 这里是手工解析的json
 *
 * @author dazer
 * @see SwaggerJerseyConfig
 * <p>
 * SwaggerStaticDocTest
 */
@Lazy
@Configuration
@EnableSwagger2
public class SwaggerSpringmvcConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public static final String SWAGGER_GROUP_SPRING = "spring_mvc_controller";

    /**
     * dazer新增
     * 在swagger-ui上访问API需要的授权key设置，在SwaggerConfiguration类中增加apikey的bean：
     * 所有接口功能验证
     *
     * @author dazer
     * @date 2018/5/1 下午5:58
     */
    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey("access_token", "token", "header");
    }

    /**
     * SpringMVC的controller文档配置
     * xxxxxxxxx/docs/
     */
    @Bean
    public Docket springMvcRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(SWAGGER_GROUP_SPRING)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ourslook.guower.api"))
                //过滤的接口
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .enableUrlTemplating(false)
                .securitySchemes(Arrays.asList(apiKey()))
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages())
                ;
    }

    /**
     * @see com.ourslook.guower.utils.result.XaResult
     * @see com.ourslook.guower.utils.Constant.Code
     * @return
     */
    private ArrayList<ResponseMessage> responseMessages() {
        return new ArrayList<ResponseMessage>() {{
            /*add(new ResponseMessageBuilder().code(200).message("成功").build());
            add(new ResponseMessageBuilder().code(302).message("url重定向").build());
            add(new ResponseMessageBuilder().code(400).message("请求参数错误").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(401).message("权限认证失败").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(403).message("请求资源不可用").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(404).message("请求资源不存在").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(405).message("请求方式错误，如本应该使用post缺使用get").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(409).message("请求资源冲突").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(415).message("请求格式错误").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(423).message("请求资源被锁定").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(501).message("请求方法不存在").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(503).message("服务暂时不可用").responseModel(new ModelRef("Error")).build());
            add(new ResponseMessageBuilder().code(-1).message("未知异常").responseModel(new ModelRef("Error")).build());*/
        }};
    }

    private ApiInfo apiInfo() {
        String iosWebView = "[_webView loadHTMLString:[NSString stringWithFormat:@\"%@%@\", @\"<head><style>img{max-width:100% !important;height:auto !important;} a{text-decoration:none; color:black}</style></head>\",strUrl] baseURL:[NSURL URLWithString:HTTPCBLURL]];";
        String frontBrowser = "<head><base href=\"服务器根域名....\"/></head>";
        return new ApiInfoBuilder()
                //大标题
                .title("上海千穆果味财经接口")
                //详细描述
                .description("1：http://localhost:8981/guower/v2/api-docs?group=" + SWAGGER_GROUP_SPRING + "  " + "2：生成服务器、客户端代码http://editor.swagger.io/   3：导出为html文档：导入到postman、导出v1版postman.json、导入http://zpizza.com    " +
                        "4：注意事项" +
                        "4-1：所有的参数中，如果包含+-./&{}这些特殊符号，请务必先进行 encodeURIComponent() 否则获取到的字符串可能有问题; " +
                        "4-2：所有的密码前端必须加密; " +
                        "4-3: 类似发送验证码这种接口要防止盗发 " +
                        "4-4: json格式化 http://www.kjson.com/ " +
                        "4-5: 普通上传图片的图片路径都是全路径，日期全部是yyyy-MM-dd HH:mm:ss形式的字符串 " +
                        "4-6: 富文本返回的图片都是相对路径,app获取前端如果显示不出来，请自行拼接全路径；如ios处理方法：" + iosWebView  + "  如：前端处理方法： " + frontBrowser +
                        "4-7: 前端或者android、ios等必须开启cookie的支持，否则可能导致session有问题，如android的retrofit就要处理一下"
                )
                .termsOfServiceUrl("https://blog.csdn.net/russ44/article/details/70054922")
                //作者
                .contact(new Contact("果味财经平台", "http://www.ourslook.com", "duandazhi@ourslook.com"))
                .version("1.0")
                .build();
    }

    /*请求url匹配，支持and or，可以过滤筛选*/
    @SuppressWarnings("all")
    private Predicate<String> paths() {
        return Predicates.or(regex("/rs/.*"), regex("/rest/.*"));
    }
}
