package com.ourslook.guower.vo;

/**
 * 用户信息
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-05 15:21:55
 */

import com.ourslook.guower.entity.common.TbUserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author dazer
 * @see TbUserEntity
 */
@ApiModel(description = "用户model", parent = TbUserEntity.class)
public class TbUserVo extends TbUserEntity implements Serializable, Comparable<TbUserVo> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("优惠券总数")
    private Integer counpionsTotal;

    @ApiModelProperty("收藏总数")
    private Integer favoriteTotal;

    @ApiModelProperty("是否处于唤醒中，0待唤醒、1唤醒中、2已唤醒")
    private Integer isWaking;

    @ApiModelProperty("商家余额")
    private Double payEndBalance;

    @ApiModelProperty("保证金")
    private Long cashDeposit;

    @ApiModelProperty("二维码链接地址")
    private String qrCode;

    @ApiModelProperty("学校名称")
    private String schoolName;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int compareTo(TbUserVo o) {
        return super.getUserid().compareTo(o.getUserid());
    }

    public Integer getCounpionsTotal() {
        return counpionsTotal;
    }

    public void setCounpionsTotal(Integer counpionsTotal) {
        this.counpionsTotal = counpionsTotal;
    }

    public Integer getFavoriteTotal() {
        return favoriteTotal;
    }

    public void setFavoriteTotal(Integer favoriteTotal) {
        this.favoriteTotal = favoriteTotal;
    }

    public Integer getIsWaking() {
        return isWaking;
    }

    public void setIsWaking(Integer isWaking) {
        this.isWaking = isWaking;
    }

    public Double getPayEndBalance() {
        return payEndBalance;
    }

    public void setPayEndBalance(Double payEndBalance) {
        this.payEndBalance = payEndBalance;
    }

    public Long getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(Long cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

}
