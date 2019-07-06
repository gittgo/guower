package com.ourslook.guower.config;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.upload.Uploader;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.spring.SpringContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author Sven  dazer
 * 问题一：解决虚目录问题
 * 自己写的方法不是UEditor自带的
 * 解决ueditor上传文件到tomcat虚拟路径等问题 http://blog.csdn.net/u010810431/article/details/53012613
 * @Description: (UEditor编辑器控制器);只是覆写：controller.jsp 而已；
 * @date 2017年5月17日
 * <p>
 * 访问路径：
 * xxxproject/ueditor/index.html
 * <p>
 * http://localhost:8180/guower/ueditor/MyUEController?action=config&&noCache=1514973026923
 * <p>
 * ueditor-自定义请求地址：http://fex.baidu.com/ueditor/#qa-customurl
 * ueditor-响应参数规范：http://fex.baidu.com/ueditor/#dev-request_specification
 * <p>
 *
 *
 * 问题二：上传图片到文件服务器的问题
 * UEditor上传图片到fastDFS 自定义请求地址 http://blog.csdn.net/kh717586350/article/details/78791952
 * @see Uploader
 * @see ActionEnter
 * @see com.baidu.ueditor.define.ActionMap
 * ueditor.custom.ourslook.js
 * @see com.ourslook.guower.api.ApiHomeController#baiduFastDfsUploadsFile
 * @see com.ourslook.guower.utils.jackson.CustomUeditorPathSerialize
 *
 *
 * 问题三：上传解决百度富文本图片自适应问题
 * UEditor生成适配移动端的HTML:https://blog.csdn.net/xichenguan/article/details/78073353
 *
 * 百度编辑器呢会自动把添加的图片设置宽高属性，所以我们要把这两行代码干掉——找到js所在位置ueditor/ueditor/dialogs/image，找到image.js文件，
 * 打开编辑，第272行和279行分别设置了宽高属性，将其屏蔽即可style: "width:" + data['width'] + "px;height:" + data['height'] + "px;"
 */
@WebServlet(name = "UEditorServlet", urlPatterns = "/ueditor/MyUEController")
@SuppressWarnings("ALL")
public class MyUEditorController extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(MyUEditorController.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        //跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        PrintWriter out = response.getWriter();

        //dazer 修改上传目录
        // TODO: 2017/7/11 注意不同的项目进行修改
        String rootUrl = SpringContextHelper.getBean(ServerProperties.class).getContextPath();
        String oldRootPath = XaUtils.getWebRootPath(request);
        String virtalRootPath = XaUtils.getWebUploadVirtalRootPath(request, rootUrl);

        String configPath = "WEB-INF/classes/static/ueditor/jsp/config.json";
        String result = null;
        File srcFile = new File(oldRootPath + configPath);
        File destFile = null;
        File uploadUeditor = null;
        String baiduUeditorRootPath = "";


        if (SpringContextHelper.getBean(OurslookConfig.class).isTomcatVirtalPath()) {
            // 虚目录start
            // ueditor/jsp/config.json 要放到 自定义的rootPath/ueditor/config.json
            // rootPath是Tomcat虚目录
            destFile = new File(virtalRootPath + "ueditor/config.json");
            uploadUeditor = new File(virtalRootPath + "upload/ueditor");
            baiduUeditorRootPath = virtalRootPath;
            //虚目录end
        } else {
            //真实目录开始start
            destFile = new File(oldRootPath + "ueditor/config.json");
            uploadUeditor = new File(virtalRootPath + "upload/ueditor");
            baiduUeditorRootPath = oldRootPath;
            //真实目录开始end
        }

        try {
            if (!uploadUeditor.exists()) {
                uploadUeditor.mkdirs();
            }

            //处理目标文件
            if (destFile.exists()) {
                destFile.delete();
            }
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
            if (!srcFile.exists()) {
                srcFile = ResourceUtils.getFile("classpath:static/ueditor/jsp/config.json");
            }
            //开始copy
            copyFile(srcFile, destFile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ueditor/jsp/config.json文件copy失败！", e);
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  这里的 oldRootPath 很重要，要谨慎操作
        result = new ActionEnter(request, baiduUeditorRootPath).exec();

        //配置文件初始化失败
        String error = "{\"state\": \"\\u914d\\u7f6e\\u6587\\u4ef6\\u521d\\u59cb\\u5316\\u5931\\u8d25\"}";
        if (error.equalsIgnoreCase(result)) {
            logger.error("配置文件初始化失败,请检查config.json的位置；config.json 注意要放到 rootPath/ueditor/config.json 的地方，否则加载不到配置");
            result = "{\"state\": \"配置文件初始化失败\"}";
        }
        String action = request.getParameter("action");

        // ueditor本的bug，获取在线文件时去除绝对路径
        if (action != null && (action.equals("listimage") || action.equals("listfile"))) {
            result = result.replaceAll(virtalRootPath, "");
        }

        /**
         *  @see com.ourslook.guower.api.ApiHomeController#baiduFastDfsUploadsFile
         *  必须注释 ueditor.custom.ourslook.js
         *  ueditor/config.js 配置
         */
        if (!SpringContextHelper.getBean(OurslookConfig.class).isFastdfsEnable()) {
            // 替换config.json中的文件访问路径前缀，避免要到json中写死
            // TODO: 绝对 or 相对，如果是绝对路径要吧 ourslook.js 放开
            if (false) {
                //使用绝对路径
                result = result.replaceAll("/waitReplaceUrl", "");
            } else {
                //使用相对路径
                result = result.replaceAll("waitReplaceUrl", rootUrl.substring(1));
            }
            if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
                // s.replaceAll("\\\\",""); \\\\ 表示反斜杠
                virtalRootPath = virtalRootPath.replace("\\\\", "/");
                result = result.replaceAll(virtalRootPath, "/");
            }
        } else {
            // fastdfs 开启
            // 必须添加这个js ueditor.custom.ourslook.js
            result = result.replaceAll("/waitReplaceUrl", "");
            result = result.replaceAll("\\\\", "");
        }

        if ("{\"state\": \"\\u65e0\\u6548\\u7684Action\"}".equalsIgnoreCase(result)) {
            result = "{\"state\": \"无效的Action\"}";
        }


        out.write(result);
        out.flush();
        out.close();
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile 源文件File
     *                //@param destDir
     *                目标目录File
     *                //@param newFileName
     *                新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @SuppressWarnings("resource")
    private static long copyFile(File srcFile, File destFile) throws Exception {
        long copySizes = 0;
        FileChannel fcin = new FileInputStream(srcFile).getChannel();
        FileChannel fcout = new FileOutputStream(destFile).getChannel();
        long size = fcin.size();
        fcin.transferTo(0, fcin.size(), fcout);
        fcin.close();
        fcout.close();
        copySizes = size;
        return copySizes;
    }

}
