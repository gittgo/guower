package com.ourslook.guower.utils.dfs;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author dazer
 * 上传文件相关
 * @date 2018/1/2 下午3:10
 */ 
public interface DfsClient {

    /**
     * @see FolderPath
     *
     * fastdfs 处于简洁考虑不支持 文件目录
     *
     * @param file
     * @param modules 模块名称，常量类，具体参见：FolderPath
     * @param fileExtensions 支持的后缀，不要包含.,不区分大小写, 默认是 default
     * @return 返回的完整的访问地址
     * @throws IOException
     */
    String uploadFile(MultipartFile file, String modules, String[] fileExtensions) throws IOException;
    String uploadFile(MultipartFile file, String[] fileExtensions) throws IOException;
    String uploadFile(MultipartFile file, String modules) throws IOException;
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 将一段字符串生成一个文件上传
     *
     * 从Url当中解析存储路径对象
     * http://ip/group/path),路径地址必须包含group
     * 有效的路径样式为(group/path) 或者
     *
     * @param content       文件内容
     *
     * @param fileExtension
     * @return
     */
    String uploadFile(String content, String fileExtension);
    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址,全路径或者不是全路径都可以
     * @return
     */
    boolean deleteFile(String fileUrl);
}
