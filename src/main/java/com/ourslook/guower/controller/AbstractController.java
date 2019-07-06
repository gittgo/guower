package com.ourslook.guower.controller;

import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.RRExceptionHandler;
import com.ourslook.guower.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller公共组件
 * @see RRExceptionHandler
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SysUserService sysUserService;
	
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
}
