package com.ourslook.guower.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 意见查看
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-05 10:54:04
 */
@ApiModel(description = "意见查看")
public class InfFeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @ApiModelProperty("")
    private Long id;
    /**
     * 意见内容
     */
    @ApiModelProperty("意见内容")
    @Length(message = "意见内容最长5000个字符", max = 5000)
    private String feedbackcontent;
    /**
     * @see com.ourslook.guower.utils.Constant.DeviceConstant
     * 意见来源（与字典表中的设备数据联系）
     * 使用英文字母
     */
    @ApiModelProperty("意见来源（与字典表中的设备数据联系）")
    private String feedbacksource;
    /**
     *
     * 意见反馈者
     */
    @ApiModelProperty("意见反馈者")
    private Long feedbackuserid;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;
    @ApiModelProperty("状态：0表示未处理，1表示已处理")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFeedbackcontent(String feedbackcontent) {
        this.feedbackcontent = feedbackcontent  != null ? feedbackcontent.trim() : "";
    }

    public String getFeedbackcontent() {
        return feedbackcontent;
    }

    public void setFeedbacksource(String feedbacksource) {
        this.feedbacksource = feedbacksource;
    }

    public String getFeedbacksource() {
        return feedbacksource;
    }

    public void setFeedbackuserid(Long feedbackuserid) {
        this.feedbackuserid = feedbackuserid;
    }

    public Long getFeedbackuserid() {
        return feedbackuserid;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public Date getCreateDate() {
        return createDate;
    }

    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}

