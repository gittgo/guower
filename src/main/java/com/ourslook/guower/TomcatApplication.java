package com.ourslook.guower;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 使用外置tomcat必须放开此类，使用内置tomcat必须注释此类
 *
 * mvn clean install ： 安装
 * mvn clean test : 测试
 * mvn clean deploy： 部署
 *
 * 打包步骤：
 * 1：打包 mvn clean package  -Dmaven.test.skip=true
 * 2：生成war包：如何生成war文件，注意要进入guower目录下面：jar -cvf guower.war *
 * 3：查看war文件的方式：jar -tf guower.war
 *
 * @author dazer
 * @date 2017/12/11 下午3:04
 */
@SpringBootApplication
public class TomcatApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TomcatApplication.class);
    }

   /* public static void main(String[] args) {
        SpringApplication.run(com.ourslook.guower.TomcatApplication.class, args);
    }*/
}
