package com.ourslook.guower.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Sets;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dazer
 * jackson 处理上传的图片，由相对路径变成全路径
 * <p>
 * 使用方法：
 * @<code>@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")</code>
 * @<code>@JsonSerialize(using = CustomPhotoImageSerialize.class)</code>
 * @<code>@JsonSerialize(using = CustomDoubleSerialize.class)</code>
 * @<code>@JsonSerialize(using = CustomDoubleSerialize.class)</code>
 *
 * <img src="/guower/upload/ueditor/1528523331705062346.png" style="border: 0px; margin: 10px auto 0px; padding: 0px; display: block; max-width: 100%; height: auto;" width="684" height="360"/>
 *
 * @see CustomUeditorPathSerialize
 */
public class CustomPhotoImageSerialize extends JsonSerializer<String> {

    public static final String UPLOAD_PREFIX = "upload/";
    public static final String UPLOAD_UEDITOR_PREFIX = "ueditor";

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(convetImg(s));
    }

    public static String convetImg(String s) {

        if (StringUtils.isNotBlank(s) && s.contains(Constant.SERVIER_NAME_SUFFIX+ "/" + UPLOAD_PREFIX)) {
            //这里可以同时处理单个或者多个图片
            String[] imgs = s.split(Constant.StringConstant.DOT.getValue());
            List<String> imgArr = new LinkedList<>();
            for (int i = 0; i < imgs.length; ++i) {
                String imgPath = ServletUtils.getHttpRootPath(ServletUtils.getHttpServletRequest()) +
                        s.substring(s.indexOf(Constant.SERVIER_NAME_SUFFIX) + Constant.SERVIER_NAME_SUFFIX.length() + 1);
                imgArr.add(imgPath);
            }
            return StringUtils.join(Sets.newTreeSet(imgArr), Constant.StringConstant.DOT.getValue());
        } else {
            return XaUtils.getNutNullStr(s);
        }
    }
}