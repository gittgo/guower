package com.ourslook.guower.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author dazer
 *   Spring RestTemplate: 比httpClient更优雅的Restful URL访问
 *   Spring RestTemplate详解 https://www.cnblogs.com/caolei1108/p/6169950.html
 * @see org.apache.commons.httpclient.HttpClient
 * @see com.ourslook.guower.api.ApiRestController
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //ms
        factory.setReadTimeout(1000 * 60);
        //ms
        factory.setConnectTimeout(1000 * 60 * 2);

        return factory;
    }
}
