package com.ourslook.guower.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ourslook.guower.utils.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 系统配置信息
 *
 * @author dazer
 */
@ApiModel(description = "系统配置信息")
public class SysConfigEntity {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("参数名")
    @NotBlank(message = "参数名不能为空")
    private String key;

    @ApiModelProperty("参数值")
    @NotBlank(message = "参数值不能为空")
    private String value;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("参数名")
    @NotBlank(message = "参数名不能为空")
    private String code;

    /**
     * @see Constant.Status
     * 删除状态
     */
    @ApiModelProperty("删除状态；状态   3：删除   非3显示; 具体参见全系统常量Constant.Stauts")
    private String status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
