package com.ourslook.guower.config;

import com.google.common.collect.Lists;
import com.ourslook.guower.webservice.DemoSoapService;
import com.sun.xml.ws.transport.http.servlet.WSServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * WebService配置
 *
 * @author dazer
 * @date 2018/4/14 下午1:58
 *
 * webservice示例
 * @see DemoSoapService
 */
@Configuration
public class WebServiceConfig {

    @Lazy
    @Bean(name = "Aurora3DSoap")
    public ServletRegistrationBean registrationMyServlet() {

        //第一个参数是第1步中创建的WeChatServlet实例，第二个参数是其对应的路径，相当于web.xml中配置时的url-pattern。
        List<String> data = Lists.newArrayList("/service/Aurora3DSoap");
        String[] arr = new String[data.size()];
        String[] arrData = data.toArray(arr);

        return new ServletRegistrationBean(new WSServlet(), arrData);
    }

}
