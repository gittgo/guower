package com.ourslook.guower.entity.score;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;




/**
 * 兑换记录表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@ApiModel(description = "兑换记录表")
public class InfExchangeRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 货币id
     */
    @ApiModelProperty("货币id")
    private Integer currencyId;
    /**
     * 消耗积分
     */
    @ApiModelProperty("消耗积分")
    private Integer score;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;
    /**
     * 钱包地址
     */
    @ApiModelProperty("钱包地址")
    private String purseAddress;
    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private Integer sysUserId;
    /**
     * 状态【1.兑换中  2.已完成】
     */
    @ApiModelProperty("状态【1.兑换中  2.已完成】")
    private Integer state;

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

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getPurseAddress() {
        return purseAddress;
    }

    public void setPurseAddress(String purseAddress) {
        this.purseAddress = purseAddress;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
