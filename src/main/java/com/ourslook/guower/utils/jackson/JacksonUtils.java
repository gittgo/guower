package com.ourslook.guower.utils.jackson;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.ourslook.guower.config.Java8TimeConfig;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author dazer
 * <p>
 * Fastjson JSON.parse JSON.toJSONString
 * <p>
 * Jackson ObjectMapper mapper = new ObjectMapper();
 * mapper.readValue(jsonObjectStr, Map.class)
 * mapper.writeValueAsString(users)
 * @see Java8TimeConfig
 */
public class JacksonUtils {
    /**
     * 解决object不能被序列化
     * 错误：JsonMappingException: No serializer found for class java.lang.Object and no properties discovered to create BeanSerializer
     */
    @SuppressWarnings("all")
    public static MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        //Jackson2ObjectMapperBuilder objectMapperBuilder = new MyJackson2ObjectMapperBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        //ObjectMapper objectMapper = objectMapperBuilder.build();
        //mapper.registerModule(new Hibernate4Module());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);


        /**
         * Java8DateConverter
         * @see CustomDoubleSerialize
         *
         *  java8日期处理：http://blog.csdn.net/junlovejava/article/details/78112240
         *  @see org.springframework.format.annotation.DateTimeFormat
         *  @see org.springframework.format.annotation.NumberFormat
         */
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);

        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    /**
     * fastjson
     *
     * @return
     */
    public static HttpMessageConverter fastJsonHttpMessageConverters() {
        //2
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteClassName
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);

        HttpMessageConverter<?> converter = fastConverter;

        //return new HttpMessageConverters(converter);

        return converter;
    }

    /**
     * Spring boot 中 使用 Jackson 将null 字段转换为空字符串写法
     * https://blog.csdn.net/keplerpig/article/details/51435582
     */
    /*private static class MyJackson2ObjectMapperBuilder extends Jackson2ObjectMapperBuilder {
        @SuppressWarnings("all")
        @Override
        public <T extends ObjectMapper> T build() {
            ObjectMapper om = new ObjectMapper();
            // 重写 ObjectMapper  中的一个方法
            om.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                    System.out.println(value+"");
                    jgen.writeObject("");// 输出空字符串
                }
            });
            configure(om);
            return (T) om;
        }
    }*/
}
