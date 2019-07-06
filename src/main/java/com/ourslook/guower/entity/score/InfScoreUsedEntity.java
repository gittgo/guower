package com.ourslook.guower.entity.score;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 积分使用表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-04-17 17:26:07
 */
@ApiModel("积分使用表")
public class InfScoreUsedEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /**
     * 
     */
    @ApiModelProperty("编号")
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 积分变动量
     */
    @ApiModelProperty("积分变动量")
    private Integer scoreChange;
    /**
     * 变动说明
     */
    @ApiModelProperty("变动说明")
    //@NotBlank(message = "变动说明不能为空")
    //@NotContainSpaces(message = "变动说明不能包含空格")
    @Length(message = "变动说明最长255个字符", max = 255)
    private String changeNote;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    //@NotBlank(message = "创建时间不能为空")
    private LocalDateTime createDate;
    /**
     *
     */
    @ApiModelProperty("获得/使用途径【暂用于获得途径】")
    //@NotBlank(message = "获得/使用途径【暂用于获得途径】不能为空")
    private Integer changeType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScoreChange() {
        return scoreChange;
    }

    public void setScoreChange(Integer scoreChange) {
        this.scoreChange = scoreChange;
    }

    public String getChangeNote() {
        return changeNote;
    }

    public void setChangeNote(String changeNote) {
        this.changeNote = changeNote;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }
}
