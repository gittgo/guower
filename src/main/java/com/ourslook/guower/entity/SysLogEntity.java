package com.ourslook.guower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ourslook.guower.utils.annotation.LoggSys;
import io.swagger.annotations.ApiModel;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @see LoggSys
 */
@ApiModel(description = "系统日志")
public class SysLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    //用户名
    private String username;
    //用户操作
    private String operation;
    /**
     * 请求url
     */
    private String url;
    //请求方法
    private String method;
    //请求参数
    private String params;
    //IP地址
    private String ip;
    //创建时间
    private Date createDate;
    /**
     * @see com.ourslook.guower.utils.XaUtils#getBroserDeviceRamark(HttpServletRequest)
     */
    private String deviceRemark;

    public SysLogEntity() {
    }

    /**
     * 源类型 0系统日志1接口日志
     */
    private Integer sourceType;

    /**
     * user agent
     */
    private String userAgent;

    /**
     * 设备版本号以及设备型号
     */
    private String deviceInfo;

    /**
     * 设置：
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public String getId() {
        return id;
    }

    /**
     * 设置：用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取：用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置：用户操作
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * 获取：用户操作
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置：请求方法
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取：请求方法
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置：请求参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取：请求参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置：IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取：IP地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 获取：设备备注
     */
    public String getDeviceRemark() {
        return deviceRemark;
    }

    /**
     * 设置：设备备注
     */
    public void setDeviceRemark(String deviceRemark) {
        this.deviceRemark = deviceRemark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
