package com.ourslook.guower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-13 18:36:41
 */
@ApiModel(description = "")
public class SysJpushRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty("")
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    @Length(message = "标题最长2000个字符", max = 2000)
    private String title;
    /**
     * 副标题
     */
    @ApiModelProperty("副标题")
    @Length(message = "副标题最长2000个字符", max = 2000)
    private String subtitle;
    /**
     * 发送参数
     */
    @ApiModelProperty("发送参数")
    @Length(message = "发送参数最长65535个字符", max = 65535)
    private String notification;
    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    private LocalDateTime createDate;
    /**
     * 推送结果【1成功 0失败】
     */
    @ApiModelProperty("推送结果【1成功 0失败】")
    private Integer result;
    /**
     * 推送平台
     */
    @ApiModelProperty("推送平台")
    @Length(message = "推送平台最长255个字符", max = 255)
    private String platform;
    /**
     * 推送目标
     */
    @ApiModelProperty("推送目标")
    @Length(message = "推送目标最长255个字符", max = 255)
    private String audience;
    /**
     * 推送账号key
     */
    @ApiModelProperty("推送账号key")
    @Length(message = "推送账号key最长255个字符", max = 255)
    private String appKey;
    /**
     * 推送账号secert
     */
    @ApiModelProperty("推送账号secert")
    @Length(message = "推送账号secert最长255个字符", max = 255)
    private String appSecert;
    /**
     *
     */
    @ApiModelProperty("")
    @Length(message = "最长255个字符", max = 255)
    private String errorCode;
    /**
     * 推送失败原因
     */
    @ApiModelProperty("推送失败原因")
    @Length(message = "推送失败原因最长2000个字符", max = 2000)
    private String errorMsg;
    /**
     * 发送编号
     */
    @ApiModelProperty("发送编号")
    //@NotBlank(message = "发送编号不能为空")
    @Length(message = "发送编号最长255个字符", max = 255)
    private String sendNo;
    /**
     * ios的生产环境还是测试环境
     */
    @ApiModelProperty("ios的生产环境还是测试环境")
    @Length(message = "ios的生产环境还是测试环境最长255个字符", max = 255)
    private String apnsProduction;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title != null ? (title).trim() : null;
    }

    public String getTitle() {
        return title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle != null ? (subtitle).trim() : null;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setNotification(String notification) {
        this.notification = notification != null ? (notification).trim() : null;
    }

    public String getNotification() {
        return notification;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    public void setPlatform(String platform) {
        this.platform = platform != null ? (platform).trim() : null;
    }

    public String getPlatform() {
        return platform;
    }

    public void setAudience(String audience) {
        this.audience = audience != null ? (audience).trim() : null;
    }

    public String getAudience() {
        return audience;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey != null ? (appKey).trim() : null;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppSecert(String appSecert) {
        this.appSecert = appSecert != null ? (appSecert).trim() : null;
    }

    public String getAppSecert() {
        return appSecert;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode != null ? (errorCode).trim() : null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg != null ? (errorMsg).trim() : null;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setSendNo(String sendNo) {
        this.sendNo = sendNo != null ? (sendNo).trim() : null;
    }

    public String getSendNo() {
        return sendNo;
    }

    public void setApnsProduction(String apnsProduction) {
        this.apnsProduction = apnsProduction != null ? (apnsProduction).trim() : null;
    }

    public String getApnsProduction() {
        return apnsProduction;
    }
}
