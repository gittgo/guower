package com.ourslook.guower.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ourslook.guower.utils.jackson.CustomUeditorPathSerialize;
import com.ourslook.guower.utils.validator.constraints.NotContainSpaces;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


/**
 * 文案维护(协议维护、关于我们等)
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-06 09:45:08
 */
@ApiModel(description = "文案维护(协议维护、关于我们等)")
public class InfClauseInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文案名称
     */
    @Length(message = "文案名称最长50个字符", max = 50)
    private String name;
    /**
     * 文案内容
     */
    @NotBlank(message = "文案内容不能为空")
    @Length(message = "文案内容最长16777215个字符", max = 16777215)
    private String content;
    /**
     * 文案类型 1：会员注册条款；2：司机注册条款；3：保险条款 4：营运服务介绍 5：关于我们(用户) 6：代驾价格说明 7：营运价格说明 8：司机招募说明 9：关于我们(司机)
     */
    @NotBlank(message = "文案类型 1：用户注册协议 2：委托合作协议不能为空}")
    @NotContainSpaces(message = "文案类型不能为空")
    @Length(message = "文案类型 1：用户注册协议 2：委托合作协议最长50个字符", max = 50)
    private String clausetype;
    /**
     * 创建人
     */
    @NotBlank(message = "创建人不能为空}")
    @Length(message = "创建人最长50个字符", max = 50)
    private String createuser;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 修改人
     */
    @NotBlank(message = "修改人不能为空}")
    @Length(message = "修改人最长50个字符", max = 50)
    private String modifyuser;
    /**
     * 修改时间
     */
    private Date modifytime;
    /**
     *
     */
    private Long clauseId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 富文本解决相对路径的问题
     * @return
     */
    @JsonSerialize(using = CustomUeditorPathSerialize.class)
    public String getContent() {
        return content;
    }

    public void setClausetype(String clausetype) {
        this.clausetype = clausetype;
    }

    public String getClausetype() {
        return clausetype;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public Date getCreatetime() {
        return createtime;
    }

    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }

    public String getModifyuser() {
        return modifyuser;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public Date getModifytime() {
        return modifytime;
    }

    public void setClauseId(Long clauseId) {
        this.clauseId = clauseId;
    }

    public Long getClauseId() {
        return clauseId;
    }
}
