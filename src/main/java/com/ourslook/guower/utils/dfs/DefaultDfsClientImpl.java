package com.ourslook.guower.utils.dfs;

import com.ourslook.guower.config.OurslookConfig;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.validator.AbstractAssert;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的上传文件实现类
 *
 * @author dazer
 * ServletUtils.getHttpServletRequest()
 */
public class DefaultDfsClientImpl implements DfsClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OurslookConfig springBootMyApplication;

    @Override
    public String uploadFile(MultipartFile file, String modules, String[] fileExtensions) throws IOException {
        AbstractAssert.isNull(file, "文件不能为空！");

        modules = StringUtils.isBlank(modules) ? "default" : modules;

        // dazer 修改上传目录
        // String root = request.getServletContext().getRealPath("/");
        String root = XaUtils.getWebRootPath(ServletUtils.getHttpServletRequest());
        if (springBootMyApplication.isTomcatVirtalPath()) {
            root =  XaUtils.getWebUploadVirtalRootPath(ServletUtils.getHttpServletRequest(), Constant.SERVIER_NAME_SUFFIX);
        }

        String picturePath = "/upload/" + modules;
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //如果时间戳使用毫秒会重复，这里使用纳秒或者毫秒+随机字符串
        String newName = System.currentTimeMillis() + "_" + RandomStringUtils.randomAlphanumeric(5) + ext;
        File filedict = new File(root + picturePath);
        if (!filedict.exists()) {
            filedict.mkdirs();
        }
        File targetFile = new File(root + picturePath + File.separator + newName);

        if (fileExtensions != null && fileExtensions.length != 0) {
            AbstractAssert.isOk(XaUtils.containsAny(ext, fileExtensions), "上传文件类型不允许,请上传指定的类型,具体联系后台管理员！");
        }

        try {
            file.transferTo(targetFile);
            Map<String, Object> map = new HashMap<>(3);
            if (XaUtils.getOperatingSystem() != OperatingSystem.LINUX ) {
                {
                    BufferedImage bimg = ImageIO.read(targetFile);
                    if (bimg != null) {
                        int width = bimg.getWidth();
                        int height = bimg.getHeight();
                        map.put("pictureHeight", height);
                        map.put("pictureWidth", width);
                    }
                    map.put("originalFilename", file.getOriginalFilename());
                }
            }
            map.put("picturePath", picturePath + "/" + newName);
            return getResAccessUrl(map.get("picturePath") + "");
        } catch (Exception e) {
            logger.error("图片上传失败(Ex)", e);
            throw new IOException("图片上传失败(Ex)");
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String[] fileExtensions) throws IOException {
        return uploadFile(file, null, fileExtensions);
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, null, null);
    }

    @Override
    public String uploadFile(MultipartFile file, String modules) throws IOException {
        return uploadFile(file, modules, null);
    }

    @Override
    public String uploadFile(String content, String fileExtension) {
        throw new RRException(DefaultDfsClientImpl.class.getSimpleName() + "类:uploadFile 方法没有实现");
    }

    /**
     * 这里的fileUrl是相对路径，和上传的地方一致，有效路径：/upload/xxx/xxx
     *
     * @param fileUrl 文件访问地址
     */
    @Override
    public boolean deleteFile(String fileUrl) {
        AbstractAssert.isOk(fileUrl != null && fileUrl.contains("/upload"), "请输入有效的图片");
        String root = XaUtils.getWebRootPath(ServletUtils.getHttpServletRequest());
        if (springBootMyApplication.isTomcatVirtalPath()) {
            root = XaUtils.getWebUploadVirtalRootPath(ServletUtils.getHttpServletRequest(), Constant.SERVIER_NAME_SUFFIX);
        }
        try {
            String storePath = getStorePath(fileUrl);
            File file = new File(root + storePath);
            file.delete();
            return true;
        } catch (Exception e) {
            logger.error("文件删除失败：{}", e, fileUrl);
        }
        return false;
    }

    private String getResAccessUrl(String storePath) {
        return StringUtils.removeEnd(ServletUtils.getHttpRootPath(ServletUtils.getHttpServletRequest()), "/") + storePath;
    }

    private String getStorePath(String filePath) {
        int groupStartPos = (Constant.SERVIER_NAME_SUFFIX+"").length();
        if ((groupStartPos += filePath.indexOf(Constant.SERVIER_NAME_SUFFIX))+1  == (Constant.SERVIER_NAME_SUFFIX+"").length()) {
            throw new RRException("解析文件路径错误,被解析路径url没有"+Constant.SERVIER_NAME_SUFFIX+",当前解析路径为".concat(filePath));
        }
        // 获取group起始位置
        String groupAndPath = filePath.substring(groupStartPos);
        return groupAndPath + "";
    }
}
