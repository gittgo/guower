package com.ourslook.guower.utils.jackson;

import com.ourslook.guower.utils.encryptdir.EncodeUtils;
import org.springframework.core.convert.converter.Converter;

import java.net.URLDecoder;

/**
 * 对所有的请求进行编码处理
 * @author dazer
 */
public class StringEncodeConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        String str = source;
        try {
            if (str != null) {
                //这里处理所有的请求参数包含字符串的情况
                str = str.trim();
            }
            //一般这个不要，除非乱码.
           /* if (EncodeUtils.isISO_8859_1(source)) {
                str = new String(source.getBytes("ISO-8859-1"), "UTF-8");
            }*/
            /**
             * 处理URLencode问题
             */
            //前后一致，说明已经被成功decode了，防止多次被Encode
            str = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
