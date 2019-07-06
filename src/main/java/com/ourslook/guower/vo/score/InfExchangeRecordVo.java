package com.ourslook.guower.vo.score;

import com.ourslook.guower.entity.score.InfExchangeRecordEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;




/**
 * 兑换记录表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@ApiModel(description = "兑换记录表", parent = InfExchangeRecordEntity.class)
public class InfExchangeRecordVo extends InfExchangeRecordEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================

	@ApiModelProperty("货币名称")
	private String currencyName;

	@ApiModelProperty("货币图标")
	private String currencyImg;

	@ApiModelProperty("用户名称")
	private String userName;

	@ApiModelProperty("操作人名称")
	private String sysUserName;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getCurrencyImg() {
        return currencyImg;
    }

    public void setCurrencyImg(String currencyImg) {
        this.currencyImg = currencyImg;
    }
}
