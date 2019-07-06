package com.ourslook.guower.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
@ApiModel(description = "字典表")
public class InfDictInfoEntity implements Serializable {

    /**
     * 字典类型： banner类型
     * @see InfAdvertisementEntity
     */
    public static final String TYPE_BANNER_ADVERTISING = "TYPE_BANNER_ADVERTISING";

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date createTime;
    private String createUser;
    private Long status;

    @ApiModelProperty("字典信息编码，字典信息编码使用之后不能修改")
    @NotBlank(message = "字典信息编码不能为空")
    @Length(message = "字典信息编码最长200个字符", max = 200)
    private String code;

    @ApiModelProperty("字典信息，字典信息名称使用之后不能修改")
    @NotBlank(message = "字典类型编码不能为空")
    @Length(message = "字典类型编码最长200个字符", max = 200)
    private String name;

    @ApiModelProperty("sort,排序字段")
    private Long sort = 0L;

    @ApiModelProperty("字典信息类型，关联XaDitcypeInfoEntity表的id")
    private String type;

    @ApiModelProperty("备注信息-字典取值")
    private String remarks;

    @ApiModelProperty("跳转链接取值")
    private String remarks1;

    @ApiModelProperty("是否推送至首页")
    private Integer isHomePage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code.trim() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks != null ? remarks.trim() : "" ;
    }

    public Integer getIsHomePage() {
        return isHomePage;
    }

    public void setIsHomePage(Integer isHomePage) {
        this.isHomePage = isHomePage;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }
}
