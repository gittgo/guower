package com.ourslook.guower.controller.common;

import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.entity.common.InfVersionInfoEntity;
import com.ourslook.guower.service.common.InfVersionInfoService;
import com.ourslook.guower.utils.PageUtils;
import com.ourslook.guower.utils.Query;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.dfs.DfsClient;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import com.ourslook.guower.vo.InfVersionInfoVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 版本更新
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-01-02 13:21:28
 */
@CrossOrigin
@RestController
@RequestMapping("infversioninfo")
public class InfVersionInfoController extends AbstractController {
    @Autowired
    private InfVersionInfoService infVersionInfoService;
    @Autowired
    private DfsClient dfsClient;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("infversioninfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<InfVersionInfoVo> infVersionInfoVoList = infVersionInfoService.queryVoList(query);
        int total = infVersionInfoService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(infVersionInfoVoList, total, query.getLimit(), query.getPage());
        return R.ok().putObj("page", pageUtil);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("infversioninfo:info")
    public R info(@PathVariable("id") Long id) {
        InfVersionInfoEntity infVersionInfo = infVersionInfoService.queryObject(id);
        return R.ok().putObj("infVersionInfo", infVersionInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("infversioninfo:save")
    public R save(@RequestBody InfVersionInfoEntity infVersionInfo) {
        infVersionInfo.setCreatetime(new Date());
        infVersionInfo.setCreateuser(getUserId());
        infVersionInfo.setUpdateDate(LocalDateTime.now());
        ValidatorUtils.validateEntity(infVersionInfo);
        infVersionInfoService.save(infVersionInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("infversioninfo:update")
    public R update(@RequestBody InfVersionInfoEntity infVersionInfo) {

        infVersionInfo.setCreateuser(getUserId());
        infVersionInfo.setUpdateDate(LocalDateTime.now());
        ValidatorUtils.validateEntity(infVersionInfo);
        infVersionInfoService.update(infVersionInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("infversioninfo:delete")
    public R delete(@RequestBody Long[] ids) {
        infVersionInfoService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 上传文件，api 或者 ipa
     * @see com.ourslook.guower.api.ApiHomeController#fileUpload(MultipartFile, Boolean, String, HttpServletRequest, HttpServletResponse)
     * @see com.ourslook.guower.api.ApiHomeController#fileUploads(MultipartFile[], Boolean, String, MultipartHttpServletRequest)
     */
    @RequestMapping("/uploadApk")
    @RequiresPermissions("infversioninfo:update")
    public R uploadApk(@RequestParam Map<String, Object> params,
                            @RequestParam(value = "uploadFile", required = true) MultipartFile file,
                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1：检查是否是apk、ipa文件
        /*if ( (file.getOriginalFilename().toLowerCase().endsWith(".apk") || file.getOriginalFilename().toLowerCase().endsWith(".ipa")) ) {
            return R.error("上传文件格式不正确!");
        }*/

        //2: 进行上传
        String filePath = "";
        try {
            filePath = dfsClient.uploadFile(file, "apk");
        } catch (IOException e) {
            throw new RRException("上传文件失败", e);
        }
        return R.ok().putObj("filePath", filePath);
    }
}
