package com.ourslook.guower.utils.dfs;

import com.github.tobato.fastdfs.conn.TrackerConnectionManager;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>Description: FastDFS文件上传下载包装类 http://blog.csdn.net/xyang81/article/details/52850667 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * 客户端提供的api还有很多，可根据自身的业务需求，将其它接口也添加到工具类中即可
 *
 * @author 杨信
 * @version 1.0
 * @date 2016/9/7
 * <p>
 * <p>
 * <strong>类概要： FastDFS Java客户端工具类</strong> <br>
 * <strong>创建时间： 2017-11-9 上午10:26:48</strong> <br>
 * <p>
 * YuQing与yuqih官网版本：https://github.com/happyfish100/fastdfs-client-java
 * 第三方客户端版本：http://blog.csdn.net/xyang81/article/details/52850667
 * SpringMVC整合fastdfs-client-java实现web文件上传下载:http://blog.csdn.net/wlwlwlwl015/article/details/52682153
 *
 * @see
 */
public class FastDFSClientImpl implements DfsClient {

    private final Logger logger = LoggerFactory.getLogger(FastDFSClientImpl.class);

    @Autowired
    private FastFileStorageClient storageClient;

    private static final String PROTOCOL = "http://";

    private static final String SEPARATOR = "/";

    /**
     * fastdfs 访问的 地址，访问地址和上传地址通常是不一样的
     * 访问地址一般要借助nginx进行才能访问
     * @see TrackerConnectionManager#trackerList
     */
    @Value("${fdfs.trackerNginxAddr:#{null}}")
    private String TRACKER_NGNIX_ADDR;

    @Override
    public String uploadFile(MultipartFile file, String modules, String[] fileExtensions) throws IOException {
        return uploadFile(file);
    }

    @Override
    public String uploadFile(MultipartFile file, String[] fileExtensions) throws IOException {
        return uploadFile(file, null, fileExtensions);
    }

    @Override
    public String uploadFile(MultipartFile file, String modules) throws IOException {
       return uploadFile(file);
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return getResAccessUrl(storePath);
    }

    @Override
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return getResAccessUrl(storePath);
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return false;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            return true;
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
        return false;
    }

    /**
     * 封装图片完整URL地址
     * fileUrl = "http://127.0.0.1:22122/" + storePath.getFullPath();
     */
    private String getResAccessUrl(StorePath storePath) {
        return PROTOCOL
                + TRACKER_NGNIX_ADDR
                + SEPARATOR + storePath.getFullPath();
    }
}