package com.ourslook.guower.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ourslook.guower.utils.validator.constraints.NotContainSpaces;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


/**
 * 字典类型表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
@ApiModel(description = "字典类型表")
public class InfDitcypeInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date createTime;
    private String createUser;
    private Long status;

    @ApiModelProperty("字典类型编码，字字典类型编码使用之后不能修改")
    @NotBlank(message = "字典类型编码不能为空")
    @Length(message = "字典类型编码最长200个字符", max = 200)
    @NotContainSpaces(message = "字典类型编码不能有空白字符")
    private String code;

    @NotBlank(message = "字典类型编码名称")
    @Length(message = "字典类型编码名称最长200个字符", max = 200)
    @NotContainSpaces(message = "字典类型编码名称不能包含空白字符")
    private String name;

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
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
