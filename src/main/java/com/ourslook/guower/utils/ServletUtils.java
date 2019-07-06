package com.ourslook.guower.utils;

import com.ourslook.guower.utils.encryptdir.EncodeUtils;
import eu.bitwalker.useragentutils.Browser;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * servlet utils.
 *
 * @author Lingo
 * @see org.springframework.http.MediaType
 * @see javax.activation.MimetypesFileTypeMap
 * @see org.springframework.mail.javamail.ConfigurableMimeFileTypeMap mime.types
 *
 * @see UrlEncode
 */
public class ServletUtils {
    // -- Content Type 定义 MIME 类型 --//

    /**
     * text type.
     */
    public static final String TEXT_TYPE = "text/plain";

    /**
     * json type.
     */
    public static final String JSON_TYPE = "application/json";

    /**
     * xml type.
     */
    public static final String XML_TYPE = "text/xml";

    /**
     * html type.
     */
    public static final String HTML_TYPE = "text/html";

    /**
     * js type.
     */
    public static final String JS_TYPE = "text/javascript";

    /**
     * office type.
     */
    public static final String EXCEL_TYPE = "application/vnd.ms-excel";
    public static final String PDF_TYPE = "application/pdf";
    public static final String WORD_TYPE = "application/msword";
    /**
     * 原始字节流
     */
    public static final String STREAM_TYPE = "application/octet-stream;charset=utf-8";

    // -- Header 定义 --//
    /**
     * authencation header.
     */
    public static final String AUTHENTICATION_HEADER = "Authorization";

    // -- 常用数值定义 --//
    /**
     * one year seconds.
     */
    public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

    /**
     * mill seconds.
     */
    public static final int MILL_SECONDS = 1000;

    /**
     * protected constructor.
     */
    protected ServletUtils() {
    }

    /**
     * 设置客户端缓存过期时间的Header.
     *
     * @param response       HttpServletResponse
     * @param expiresSeconds long
     */
    public static void setExpiresHeader(HttpServletResponse response,
                                        long expiresSeconds) {
        // Http 1.0 header
        response.setDateHeader("Expires", System.currentTimeMillis()
                + (expiresSeconds * MILL_SECONDS));
        // Http 1.1 header
        response.setHeader("Cache-Control", "private, max-age="
                + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     *
     * @param response HttpServletResponse
     */
    public static void setDisableCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        // Http 1.1 header
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     *
     * @param response         HttpServletResponse
     * @param lastModifiedDate long
     */
    public static void setLastModifiedHeader(HttpServletResponse response,
                                             long lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }

    /**
     * 禁止图片缓存
     *
     * @param response
     */
    public static void setDisableImageCacheHeader(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
    }

    /**
     * 设置Etag Header.
     *
     * @param response HttpServletResponse
     * @param etag     String
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader("ETag", etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * <p>
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param lastModified 内容的最后修改时间.
     * @return boolean
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request,
                                               HttpServletResponse response, long lastModified) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");

        if ((ifModifiedSince != -1)
                && (lastModified < (ifModifiedSince + MILL_SECONDS))) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

            return false;
        }

        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * <p>
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param etag     内容的ETag.
     * @return boolean
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request,
                                               HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");

        if (headerValue != null) {
            boolean conditionSatisfied = false;

            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(
                        headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();

                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader("ETag", etag);

                return false;
            }
        }

        return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * 在输出流执行之前调用
     *
     * <code>
         response.setContentType(ServletUtils.STREAM_TYPE);
         ServletUtils.setFileDownloadHeader(request, response,tableModel.getFilename() + ".csv");
         response.getOutputStream().write(buff.toString().getBytes(encoding));
         response.getOutputStream().flush();
     * </code>
     *
       <code>
         response.reset();
         //改成输出excel文件
         response.setContentType(ServletUtils.EXCEL_TYPE);
         //设置下载头
         ServletUtils.setFileDownloadHeader(request, response, fileName + ".xls");
         //输出
         OutputStream fileOut = response.getOutputStream();
         wb.write(response.getOutputStream());
         fileOut.close();
       </code>
     *
     *
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletRequest request,
                                             HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException {
        // 中文文件名支持
        if (XaUtils.isEmpty(fileName)) {
            fileName = "";
        }
        String encodedFileName = null;
        // 替换空格，否则firefox下有空格文件名会被截断,其他浏览器会将空格替换成+号
        //其中%20是空格在UTF-8下的编码

        Browser browser = XaUtils.getBroserInfo(request);
        if (Browser.EDGE.equals(browser.getGroup()) || Browser.IE.equals(browser.getGroup())) {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8")+"";

            if (Browser.IE.equals(browser.getGroup())) {
                if (encodedFileName.length() > 150) {
                    //根据request的locale 得出可能的编码，中文操作系统通常是gb2312
                    String guestEncode = request.getCharacterEncoding();
                    encodedFileName = new String(encodedFileName.getBytes(guestEncode), "ISO8859-1");
                }
            }
        }  else {

            if (Browser.FIREFOX.equals(browser.getGroup())) {
                encodedFileName = fileName.trim().replaceAll(" ", "_");
            } else  {
                encodedFileName = fileName;
            }
            encodedFileName = new String(encodedFileName.getBytes("UTF-8"),
                    "ISO8859-1");
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
    }

    /**
     * 取得带相同前缀的Request Parameters. 返回的结果的Parameter名已去除前缀.
     *
     * @param request HttpServletRequest
     * @param prefix  String
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParametersStartingWith(
            ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");

        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();

        String thePrefix = (prefix == null) ? "" : prefix;

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            if ("".equals(thePrefix) || paramName.startsWith(thePrefix)) {
                String unprefixed = paramName.substring(thePrefix.length());
                String[] values = request.getParameterValues(paramName);

                if ((values == null) || (values.length == 0)) {
                    // Do nothing, no values found at all.
                    continue;
                }

                if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }

        return params;
    }

    public static Map<String, Object> getParametersStartingWith(
            Map<String, Object> parameterMap, String prefix) {
        Map<String, Object> params = new TreeMap<String, Object>();

        String thePrefix = (prefix == null) ? "" : prefix;

        for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();

            if ("".equals(thePrefix) || paramName.startsWith(thePrefix)) {
                String unprefixed = paramName.substring(thePrefix.length());

                if (paramValue == null) {
                    // Do nothing, no values found at all.
                    continue;
                }

                params.put(unprefixed, paramValue);
            }
        }

        return params;
    }

    /**
     * 获取文件的mine类型
     * 不太准，不如判断后缀来的快
     *
     * @param fileName
     * @return
     */
    @Deprecated
    public static String getFileMineType(File fileName) {
        String mimeType = new ConfigurableMimeFileTypeMap().getContentType(fileName);
        return mimeType;
    }

    /**
     * 对Http Basic验证的 Header进行编码.
     *
     * @param userName String
     * @param password String
     * @return String
     */
    public static String encodeHttpBasic(String userName, String password)
            throws UnsupportedEncodingException {
        String encode = userName + ":" + password;
        return "Basic " + EncodeUtils.base64Encode(encode.getBytes("UTF-8"));
    }

    /**
     * 获取web项目 请求的跟路径
     * eg:http://localhost:8080/servername/
     *
     *  //添加导入试题的模板的名称
     *   String examImportTemplateUrl = ServletUtils.getHttpRootPath(request) + "importTemplate/考试题导入模板.xls";
     */
    public static String getHttpRootPath(HttpServletRequest request) {
        String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getRequestURI().split("/")[1] + "/";
        return rootPath;
    }

    /**
     * eg:http://localhost:8080/servername
     * @param request
     * @return
     */
    public static String getHttpRootPathNoEndPath(HttpServletRequest request) {
        return org.apache.commons.lang.StringUtils.removeEnd(ServletUtils.getHttpRootPath(ServletUtils.getHttpServletRequest()), "/");
    }

    /**
     * eg:http://localhost:8080
     * @param request
     * @return
     */
    public static String getHttpPath(HttpServletRequest request) {
        String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return rootPath;
    }


    /**
     * 使用spring mvc 获取 当前请求的 request
     * String uri = request.getServletPath();
     *
     * HttpContextUtils
     *
     * @see  UrlEncode#getUrlParams(HttpServletRequest)
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static boolean isStaticFile(String requestUrl) {
        Iterator<String> iterator = IGNORE_EXT.iterator();
        while (iterator.hasNext()) {
            String suffix = iterator.next();
            if (StringUtils.endsWithIgnoreCase(requestUrl+"", suffix)) {
                return true;
            }
        }
        return false;
    }

    public static final Set<String> IGNORE_EXT = new HashSet<>();
    static {
        String extStr = "css|js|jpg|jpeg|gif|png|ico|bmp|gz|xml|zip|rar|swf|txt|xls|xlsx|flv|mid|doc|ppt|pdf|mp3|wma";
        for (int i = 0 ; i < extStr.split("\\|").length; ++i) {
            String ext = extStr.split("\\|")[i];
            IGNORE_EXT.add("."+ext);
        }
        //字体文件
        IGNORE_EXT.add(".woff");
        IGNORE_EXT.add(".docx");
        IGNORE_EXT.add(".pptx");
        IGNORE_EXT.add(".ttf");
        IGNORE_EXT.add(".woff2");
        IGNORE_EXT.add(".svg");
        IGNORE_EXT.add(".eot");
    }
}