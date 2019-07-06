package com.ourslook.guower.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * 获取资源文件
 *
 * @author dazer
 * @Description:
 * @date 2018/2/25 下午2:51
 *
 * 常见操作IO、文件工具类
 * @see FileUtils      copyxx
 * @see IOUtils
 * @see ResourceUtils  getFile
 */
@SuppressWarnings("all")
public class ResourceOrFileUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /*public static void main(String[] args) throws Exception {
        file0();
    }*/

    /**
     * 获取classpath文件
     * @throws Exception
     */
    public static void file0()  throws Exception{
        //第一种
        File cfgFile = ResourceUtils.getFile("classpath:nginx.conf");
        System.out.println(cfgFile.getParentFile().getParentFile().getPath());
        //第二种
        org.springframework.core.io.Resource fileRource = new ClassPathResource("test.txt");
        //获取文件：fileRource.getFile();
        //获取文件流：fileRource.getInputStream();
    }

    /**
     * 获取绝对路径文件
     */
    public void file1() throws Exception{
        String filePath = "D:/file/test.properties";//绝对路径
        Resource resource1 = new FileSystemResource(filePath);
        InputStream is = resource1.getInputStream();//获取inputstream
        File file = resource1.getFile(); //获取file对象
        //.........
    }

    /**
     * 获取编译路径下面文件
     */
    public void file2() throws Exception {
        File file1 = ResourceUtils.getFile("classpath:config/test.properties"); //类路径
        String path = "file:D:/file/test.properties"; //绝对路径
        File file2 = ResourceUtils.getFile(path);
    }

    /**
     * 获取WEB-INF下面文件
     */
    public void file3() {
        ServletUtils.getHttpServletRequest().getServletContext().getRealPath("/WEB-INF/upload");
    }

    /**
     * 下载URL到本地文件夹
     */
    public void downloadUrlTofile() throws Exception {
        AbstractResource urlResource = new UrlResource(new URL("http://dl.360safe.com/360zip_setup_4.0.0.1050.exe"));
        IOUtils.copy(urlResource.getInputStream(), new FileOutputStream("/Users/duandazhi/Desktop/360.exe"));
    }
}
