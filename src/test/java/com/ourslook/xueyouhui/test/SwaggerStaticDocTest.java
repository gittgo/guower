//package com.ourslook.guower.test;
//
//import com.alibaba.fastjson.JSON;
//import com.ourslook.guower.SpringBootMyApplication;
//import com.ourslook.guower.config.SwaggerSpringmvcConfig;
//import com.ourslook.guower.entity.common.TbUserEntity;
//import io.github.robwin.markup.builder.MarkupLanguage;
//import io.github.robwin.swagger2markup.GroupBy;
//import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
//import org.junit.After;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import springfox.documentation.staticdocs.SwaggerResultHandler;
//
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @version V1.0
// * @Title:
// * @ClassName: SwaggerStaticDocTest.java
// * @Description:
// * @Copyright 2016-2018  - Powered By 研发中心
// * @author: 王延飞
// * @date: 2018-01-22 16:06
// * <p>
// * Swagger2离线文档：PDF和Html5格式
// * https://blog.csdn.net/fly910905/article/details/79131755
// * <p>
// * 代码：https://github.com/Fly0905/SwaggerOfflineDoc
// */
//@AutoConfigureMockMvc
//@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringBootMyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//public class SwaggerStaticDocTest {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    private String snippetDir = "target/generated-snippets";
//    private String outputDir = "target/asciidoc";
//    @Autowired
//    private MockMvc mockMvc;
//
//    @After
//    public void Test() throws Exception {
//        String path1 = "/v2/api-docs?group=" + SwaggerSpringmvcConfig.SWAGGER_GROUP_SPRING;
//        String path2 = "/rs/swagger.json";
//        logger.info(path1);
//        // 得到swagger.json,写入outputDir目录中
//        //mockMvc.perform(get(path1).accept(MediaType.APPLICATION_JSON))
//        mockMvc.perform(get(path1)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
//                .andExpect(status().isOk())
//                .andReturn();
//        // 读取上一步生成的swagger.json转成asciiDoc,写入到outputDir
//        // 这个outputDir必须和插件里面<generated></generated>标签配置一致
//        Swagger2MarkupConverter.from(outputDir + "/swagger.json")
//                .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
//                .withExamples(snippetDir)
//                .build()
//                .intoFolder(outputDir);// 输出
//    }
//
//    @Test
//    public void TestApi() throws Exception {
//        mockMvc.perform(get("/api/user/findUserDetail").param("userid", "1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcRestDocumentation.document("api_user_findUserDetail", preprocessResponse(prettyPrint())));
//        TbUserEntity user = new TbUserEntity();
//        user.setUserid(1L);
//        user.setName("dazers");
//        user.setAlipayAcount("18049531390");
//        user.setSex(1);
//
//        mockMvc.perform(post("/api/user/contacts").contentType(MediaType.APPLICATION_JSON)
//                .content(JSON.toJSONString(user))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andDo(MockMvcRestDocumentation.document("api_user_contacts", preprocessResponse(prettyPrint())));
//    }
//}
//
