package com.ourslook.guower.vo;

import com.ourslook.guower.entity.common.InfFeedbackEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 意见反馈  对软件 的反馈  分为N平台
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-28 10:52:48
 * @see com.ourslook.guower.entity.common.InfFeedbackEntity
 */
@ApiModel(description = "意见反馈  对软件 的反馈  分为N平台", parent = InfFeedbackEntity.class)
public class InfFeedbackVo extends InfFeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 扩展两个字段   分别为   用户的姓名与反馈的来源
     * 意见反馈者
     */
    @ApiModelProperty("意见反馈者")
    private String feedbackusername;

    private Integer status;

    @ApiModelProperty("状态反转")
    private String statusname;

    @ApiModelProperty("联系电话")
    private String tel;

    @ApiModelProperty("来源")
    private String feedbacksourceName;

    public String getFeedbackusername() {
        return feedbackusername;
    }

    public void setFeedbackusername(String feedbackusername) {
        this.feedbackusername = feedbackusername;
    }

    public String getFeedbacksourceName() {
        return feedbacksourceName;
    }

    public void setFeedbacksourceName(String feedbacksourceName) {
        this.feedbacksourceName = feedbacksourceName;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    @Override
    public String getTel() {
        return tel;
    }

    @Override
    public void setTel(String tel) {
        this.tel = tel;
    }
}
