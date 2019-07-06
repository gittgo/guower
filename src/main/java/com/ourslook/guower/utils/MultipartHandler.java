package com.ourslook.guower.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 根据request查找所有的文件
 * @<code>
 *      MultipartHandler multipartHandler = new MultipartHandler(multipartResolver);
 *         try {
 *             multipartHandler.handle(request);
 *             logger.debug("multiValueMap : {}", multipartHandler.getMultiValueMap());
 *             logger.debug("multiFileMap : {}", multipartHandler.getMultiFileMap());
 *         } finally {
 *             multipartHandler.clear();
 *         }
 * </code>
 */
public class MultipartHandler {
    private static Logger logger = LoggerFactory
            .getLogger(MultipartHandler.class);
    private MultipartResolver multipartResolver;
    private MultipartHttpServletRequest multipartHttpServletRequest = null;
    private MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
    private MultiValueMap<String, MultipartFile> multiFileMap;

    public MultipartHandler(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    public void handle(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            logger.debug("force cast to MultipartHttpServletRequest");

            MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
            this.multiFileMap = req.getMultiFileMap();
            logger.debug("multiFileMap : {}", multiFileMap);
            this.handleMultiValueMap(req);
            logger.debug("multiValueMap : {}", multiValueMap);

            return;
        }

        if (multipartResolver.isMultipart(request)) {
            logger.debug("is multipart : {}",
                    multipartResolver.isMultipart(request));
            this.multipartHttpServletRequest = multipartResolver
                    .resolveMultipart(request);

            logger.debug("multipartHttpServletRequest : {}",
                    multipartHttpServletRequest);
            this.multiFileMap = multipartHttpServletRequest.getMultiFileMap();
            logger.debug("multiFileMap : {}", multiFileMap);
            this.handleMultiValueMap(multipartHttpServletRequest);
            logger.debug("multiValueMap : {}", multiValueMap);
        } else {
            this.handleMultiValueMap(request);
            logger.debug("multiValueMap : {}", multiValueMap);
        }
    }

    public static List<MultipartFile> getMultipartFilesByReqeust(HttpServletRequest request) {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        {
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();

                while (iter.hasNext()) {
                    //一次遍历所有文件
                    MultipartFile file = multiRequest.getFile(iter.next().toString());
                    multipartFiles.add(file);
                }
            }
        }
        return multipartFiles;
    }

    public void clear() {
        if (multipartHttpServletRequest == null) {
            return;
        }

        multipartResolver.cleanupMultipart(multipartHttpServletRequest);
    }

    public void handleMultiValueMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();

            for (String value : entry.getValue()) {
                multiValueMap.add(key, value);
            }
        }
    }

    public MultiValueMap<String, String> getMultiValueMap() {
        return multiValueMap;
    }

    public MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return multiFileMap;
    }
}
