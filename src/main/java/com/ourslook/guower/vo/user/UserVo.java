package com.ourslook.guower.vo.user;

import com.ourslook.guower.entity.user.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;




/**
 * 用户表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
@ApiModel(description = "用户表", parent = UserEntity.class)
public class UserVo extends UserEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================

	@ApiModelProperty("token ")
	private String token;

	@ApiModelProperty("认证状态")
	private Integer authStatus;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}
}
