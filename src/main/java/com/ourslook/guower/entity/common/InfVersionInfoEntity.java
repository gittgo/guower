package com.ourslook.guower.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 版本更新
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-01-02 13:21:28
 * @see com.ourslook.guower.vo.InfVersionInfoVo
 */
@ApiModel(description = "版本更新")
public class InfVersionInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;
    /**
     * 版本代码
     */
    @ApiModelProperty("版本代码")
    private Integer versioncode;
    /**
     * 版本名称
     */
    @ApiModelProperty("版本名称")
    @Length(message = "版本名称最长100个字符", max = 100)
    private String versionname;
    /**
     * 是否强制更新
     */
    @ApiModelProperty("是否强制更新")
    private Integer isforceupdate;
    /**
     * 版本更新内容说明
     */
    @ApiModelProperty("版本更新内容说明")
    @Length(message = "版本更新内容说明最长200个字符", max = 200)
    private String updatecontent;
    /**
     * 下载地址
     */
    @ApiModelProperty("下载地址")
    @Length(message = "下载地址最长200个字符", max = 200)
    private String downloadurl;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createtime;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    private LocalDateTime updateDate;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createuser;
    /**
     * 设备：ios、android、web
     * @see com.ourslook.guower.utils.Constant.DeviceConstant
     */
    @ApiModelProperty("设备：ios、android、web")
    @Length(message = "设备：ios、android、web最长50个字符", max = 50)
    private String device;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVersioncode(Integer versioncode) {
        this.versioncode = versioncode;
    }

    public Integer getVersioncode() {
        return versioncode;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname != null ? versionname.trim() : "";
    }

    public String getVersionname() {
        return versionname;
    }

    public void setIsforceupdate(Integer isforceupdate) {
        this.isforceupdate = isforceupdate;
    }

    public Integer getIsforceupdate() {
        return isforceupdate;
    }

    public void setUpdatecontent(String updatecontent) {
        this.updatecontent = updatecontent != null ? updatecontent.trim() : "";
    }

    public String getUpdatecontent() {
        return updatecontent;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl != null ? downloadurl.trim() : "";
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreateuser(Long createuser) {
        this.createuser = createuser;
    }

    public Long getCreateuser() {
        return createuser;
    }

    public void setDevice(String device) {
        this.device = device == null ? "" : device.trim();
    }

    public String getDevice() {
        return device;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
