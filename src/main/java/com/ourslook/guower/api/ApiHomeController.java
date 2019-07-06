package com.ourslook.guower.api;

import com.ourslook.guower.config.SwaggerSpringmvcConfig;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.annotation.IgnoreAuth;
import com.ourslook.guower.utils.dfs.DfsClient;
import com.ourslook.guower.utils.dfs.FolderPath;
import com.ourslook.guower.utils.jackson.CustomPhotoImageSerialize;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.validator.AbstractAssert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * 核心接口
 *
 * @author dazer
 * <p>
 * 这里的接口，如果是不登录就可以访问的，请查看
 * @see com.ourslook.guower.config.ShiroConfig#shirFilter(SecurityManager)
 */
@CrossOrigin
@Controller
@RequestMapping("/")
@SuppressWarnings("all")
@Api(value = "home", description = "核心接口", position = 1)
public class ApiHomeController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DfsClient dfsClient;
    @Autowired
    private MultipartResolver multipartResolver;

    /**
     * @return
     * @see com.ourslook.guower.config.SwaggerSpringmvcConfig
     */
    @IgnoreAuth
    @ApiIgnore
    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String swaggerSpringmvcDocs() {
        /**
         * 这里是重定向，地址栏会编号，http状态码是：302 或者 301
         */
        return "redirect:swagger-ui.html";
    }

    @ApiIgnore
    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/docs.json", produces = "text/json;charset=UTF-8", method = RequestMethod.GET)
    public String swaggerSpringmvcDocsJson2(HttpServletRequest request) {
        String swaggerJsonPath = ServletUtils.getHttpRootPath(request) + "v2/api-docs?group=" + SwaggerSpringmvcConfig.SWAGGER_GROUP_SPRING;
        String json = "";
        try {
            AbstractResource urlResource = new UrlResource(new URL(swaggerJsonPath));
            InputStream inputStream = urlResource.getInputStream();
            json = IOUtils.toString(inputStream, "UTF-8");
            logger.info(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @ApiIgnore
    @IgnoreAuth
    @RequestMapping(value = "/swagger.json", produces = "text/json;charset=UTF-8", method = RequestMethod.GET)
    public String swaggerSpringmvcDocsYaml(HttpServletRequest request) {
        return "redirect:docs.json";
    }

    /**
     * @return
     * @see com.ourslook.guower.config.SwaggerJerseyConfig
     */
    @IgnoreAuth
    @ApiIgnore
    @RequestMapping(value = "/doc", method = RequestMethod.GET)
    public String swaggerJerseyDocs() {
        /**
         * 这里是重定向，地址栏会编号，http状态码是：302 或者 301
         */
        return "redirect:swagger-jersey-docs/index.html";
    }

    /**
     * 重定向到项目分享页面
     *
     * @param code
     * @return
     */
    @IgnoreAuth
    @ApiIgnore
    @RequestMapping(value = "/qr/{code}", method = RequestMethod.GET)
    public String redirectToDownload(@PathVariable("code") String code, @ApiIgnore HttpServletRequest request) {
        /**
         * 这里是重定向，地址栏会编号，http状态码是：302 或者 301
         */
        String url = ServletUtils.getHttpRootPath(request) + "share/download.html";
        return "redirect:" + url;
    }

    /**
     * /ueditor/index.html 可以查看ueditor测试页面。
     *
     * @return
     */
    @IgnoreAuth
    @ApiIgnore
    @RequestMapping(value = "/ueditor", method = RequestMethod.GET)
    public String ueditor() {
        return "redirect:ueditor/MyUEController?action=config&&noCache=1514973026923";
    }

    @IgnoreAuth
    @ResponseBody
    @ApiOperation(value = "上传单文件", notes = "上传文件,单文件上传，返回上传的路径，如果是用FastDfs，返回的是全路径; 否则返回的是相关路径（/upload/default/1000000xxx.png);\n" +
            "说明,项目中注意是，这些所有的参数中，如果包含+-./&{}这些特殊符号，请务必先进行 encodeURIComponent() 否则获取到的字符串可能有问题，尤其是在加密的情况")
    @RequestMapping(value = "api/fileUpload", method = RequestMethod.POST)
    public XaResult<String> fileUpload(
            @ApiParam("上传的文件,字段名:myfile,必填") @RequestParam(value = "myfile") MultipartFile myfile,
            @ApiParam("是否是图片,字段名:isImage,选填写,默认是图片") @RequestParam(value = "isImage", defaultValue = "true") Boolean isImage,
            @ApiParam("模块name,字段名:moduleName,选填,具体取值，请查看FolderPath这个类") @RequestParam(value = "moduleName", defaultValue = "default") String moduleName,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        AbstractAssert.isNull(XaUtils.isNotEmpty(myfile), "上传文件不能为空!");
        AbstractAssert.isOk(XaUtils.isNotBlank(moduleName), "moduleName不能为空,请重新填写!");
        String uploadFileStr = dfsClient.uploadFile(myfile, moduleName, isImage ? FolderPath.IMAGE_SUFFFIXS : null);
        return new XaResult<String>().setObject(uploadFileStr);
    }

    /**
     * 上传文件
     *
     * @return 返回上传图片的路径，如果是用FastDfs，返回的是全路径，否则返回的是相关路径（/upload/default/1000000xxx.png）
     * @throws IOException
     * @see FolderPath
     */
    @IgnoreAuth
    @ApiOperation(value = "多文件上传", notes = "多文件上传,说明:传递数据只要多个key一样的字段会自动封装成数组;")
    @PostMapping("api/fileUploads")
    @ResponseBody
    public XaResult<List<String>> fileUploads(
            @ApiParam("上传的文件,支持多个文件上传，这里是一个数组,字段名:myfile,必填；这里myfile在web对应file表单的name属性") @RequestParam(value = "myfile") MultipartFile[] myfile,
            @ApiParam("是否是图片,字段名:isImage,选填写,默认是图片") @RequestParam(value = "isImage", defaultValue = "true") Boolean isImage,
            @ApiParam("模块name,字段名:moduleName,选填,具体取值，请查看FolderPath这个类") @RequestParam(value = "moduleName", defaultValue = "default") String moduleName,
            MultipartHttpServletRequest request) throws IOException {

        //没有myfile-可以的文件，这里进行手工查找
        if (XaUtils.isEmpty(myfile)) {
            MultipartHandler multipartHandler = new MultipartHandler(multipartResolver);
            try {
                multipartHandler.handle(request);
                logger.debug("multiValueMap : {}", multipartHandler.getMultiValueMap());
                logger.debug("multiFileMap : {}", multipartHandler.getMultiFileMap());

                List<MultipartFile> files = new ArrayList<>();
                for (Map.Entry<String, List<MultipartFile>> entry : multipartHandler.getMultiFileMap().entrySet()) {
                    files.addAll(entry.getValue());
                }
                myfile = new MultipartFile[files.size()];
                files.toArray(myfile);
            } finally {
                multipartHandler.clear();
            }
        }

        List<String> datas = new ArrayList<>();
        for (MultipartFile file : myfile) {
            String filePath = dfsClient.uploadFile(file, moduleName, isImage ? FolderPath.IMAGE_SUFFFIXS : null);
            datas.add(filePath);
        }
        return new XaResult<List<String>>().setObject(datas);
    }

    @IgnoreAuth
    @ResponseBody
    @ApiOperation(value = "删除上传的文件", notes = "删除上传的文件,可以是多个文件，数组形式")
    @RequestMapping(value = "api/fileDeletes", method = RequestMethod.POST)
    public XaResult<Map<String, Boolean>> fileDeletes(
            @ApiParam("删除的文件，全路径或者不是全路径都可以,字段名:filePaths,必填") @RequestParam(value = "filePaths", required = false) String[] filePaths,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        //这个仅仅给bootstrap fileupload 组件删除使用
        Map<String, Object> boostrapFileuploadKey = UrlEncode.getUrlParams(request);

        XaResult xr = new XaResult();
        if (XaUtils.isEmpty(filePaths) && XaUtils.isEmpty(boostrapFileuploadKey)) {
            xr.error("删除文件路径不能为空");
            return xr;
        }
        if ((filePaths == null || filePaths.length == 0) && boostrapFileuploadKey.containsKey("key")) {
            filePaths = new String[]{boostrapFileuploadKey.get("key") + ""};
        }

        Map<String, Boolean> sucesses = new Hashtable<>();
        for (String fileStr : filePaths) {
            boolean isSucess = dfsClient.deleteFile(fileStr);
            sucesses.put(fileStr, isSucess);
        }
        return new XaResult<Map<String, Boolean>>().setObject(sucesses);
    }

    /**
     * UEditor上传图片到fastDFS 自定义请求地址 http://blog.csdn.net/kh717586350/article/details/78791952
     * ueditor-自定义请求地址：http://fex.baidu.com/ueditor/#qa-customurl
     * ueditor-响应参数规范：http://fex.baidu.com/ueditor/#dev-request_specification
     * config.json
     * ueditor.custom.ourslook.js
     *
     * @see com.ourslook.guower.config.MyUEditorController
     * @see FolderPath
     */
//    @ApiIgnore
    @IgnoreAuth
    @ResponseBody
    @ApiOperation(value = "百度Ueditor", notes = "百度Ueditor-上传文件到Fastdfs-图片、视频、文件")
    @RequestMapping(value = "ueditor/uploadsFile", method = RequestMethod.POST)
    public Map<String, String> baiduFastDfsUploadsFile(
            @ApiParam("百度ueditor上传的文件,字段名:upfile,必填") @RequestParam(value = "upfile") MultipartFile upfile,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> result = new HashMap<>(4);
        String url = null;
        try {
            //url = dfsClient.uploadFile(upfile, FolderPath.IMAGE_SUFFFIXS);
            url = dfsClient.uploadFile(upfile, CustomPhotoImageSerialize.UPLOAD_UEDITOR_PREFIX);
            result.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("state", e.getLocalizedMessage());
        }
        //拼接完整路径的url,如："upload/demo.jpg"
        result.put("url", url);
        //下面都是文件的名称
        result.put("title", upfile.getOriginalFilename());
        result.put("original", upfile.getOriginalFilename());
        return result;
    }

    @ApiIgnore
    @IgnoreAuth
    @ResponseBody
    @ApiOperation(value = "百度Ueditor", notes = "百度Ueditor-上传文件到Fastdfs-执行列出图片/文件---fastdfs如何获取图片列表")
    @RequestMapping(value = "ueditor/uploadsList", method = RequestMethod.GET)
    public void baiduFastDfsUploadsList(
            @ApiParam("删除的文件，全路径或者不是全路径都可以,字段名:filePaths,必填") @RequestParam(value = "filePaths")
                    HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: 2018/1/3 fastdfs如何获取图片列表
    }

    /**
     * 获取所有被ConstantName注解修饰的类与其属性的集合
     *
     * @return 符合条件的属性Map<类名               ,               Map               <               属性               ,               值>>
     * @see Constant
     * @see Constant.ConstantName
     */
    @IgnoreAuth
    @ApiOperation(value = "全系统的常量", notes = "系统全部的常量")
    @RequestMapping(value = "/constants", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Map<Integer, String>> findConstantNameClass() throws Exception {
        Map<String, Map<Integer, String>> map = new HashMap<>();
        Class[] innerClass = Class.forName(Constant.class.getName()).getClasses();
        for (Class classz : innerClass) {
            if (classz.isAnnotationPresent(Constant.ConstantName.class)) {

                //去掉该项目不用的类型
                Deprecated deprecated = (Deprecated) classz.getAnnotation(Deprecated.class);
                if (deprecated != null) {
                    continue;
                }

                //map添加数据
                String key = classz.getName();
                Constant.ConstantName constantName = (Constant.ConstantName) classz.getAnnotation(Constant.ConstantName.class);
                String[] arr = StringUtils.splitByWholeSeparator(key, "$");
                String keyStr = arr[1] + "(" + constantName.value() + ")";
                map.put(keyStr, ReflectUtils.covetConstaintClassToMap(classz));
            }
        }
        return map;
    }
}
