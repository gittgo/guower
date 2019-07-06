package com.ourslook.guower.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ourslook.guower.config.MyUEditorController;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * @author dazer
 * jackson 处理百度富文本组件 上传的图片，由相对路径变成全路径
 * get方法：@JsonSerialize(using = CustomUeditorPathSerialize.class)
 *
 * @see MyUEditorController
 */
public class CustomUeditorPathSerialize extends JsonSerializer<String> {

    public static final String UPLOAD_PREFIX = "upload/";
    public static final String UPLOAD_UEDITOR_PREFIX = "ueditor";

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(convetImg(s));
    }


    private static String convetImg(String s) {

        s = removeHeadBase(s);

        if (StringUtils.isNotBlank(s) && s.contains(UPLOAD_PREFIX + UPLOAD_UEDITOR_PREFIX)) {
            String path = ServletUtils.getHttpPath(ServletUtils.getHttpServletRequest());
            String htmlBase = "<head><base href='" + path + "'/></head>";
            return htmlBase + "     " + s;
        } else {
            return XaUtils.getNutNullStr(s);
        }
    }

    /**
     * 这里移除，不用的heade
     *
     * @param content
     * @return
     * @see ValidatorUtils
     * @see com.ourslook.guower.utils.validator.Validator
     */
    private static String removeHeadBase(String content) {
        if (content != null) {
            content = content.replaceAll("<base href=.*?/>", "");
            return content;
        }
        return "";
    }

    /**
     * @author 吴楠
     * 把String中相对路径变为项目绝对路径
     * @param str 要转换的String
     * @return  结果
     */
    private String caseRelativePathToAbsolutelyByString(String str){
        String result = str;
        if(str.contains("/guower/upload/")){
            String rootPath = StringUtils.removeEnd(ServletUtils.getHttpRootPath(ServletUtils.getHttpServletRequest()), "/");
            String[] dlss = str.split("/guower/upload/");
            StringBuffer overDls = new StringBuffer();
            for (int i = 0;i<dlss.length;i++){
                overDls.append(dlss[i]);
                if(dlss[i].endsWith("src=\"")){
                    overDls.append(rootPath+"/upload/");
                }else if(i<dlss.length-1){
                    overDls.append("/guower/upload/");
                }
            }
            result = overDls.toString();
        }
        return result;
    }
}