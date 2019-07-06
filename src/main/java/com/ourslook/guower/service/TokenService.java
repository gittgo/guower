package com.ourslook.guower.service;

import com.ourslook.guower.entity.TokenEntity;
import com.ourslook.guower.entity.common.TbUserEntity;
import com.ourslook.guower.entity.user.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户Token
 * 
 */
public interface TokenService {

	TokenEntity queryByUserId(Long userId);

	TokenEntity queryByToken(String token);

	/**
	 * 该方法在没有 IgnoreAuth 注解的api方法里面，肯定可以获取到值，不用在做存在性判断
	 * @param request
	 * @return
	 */
	long queryUseIdByRequest(HttpServletRequest request);

	/**
	 * 该方法在没有 IgnoreAuth 注解的api方法里面，肯定可以获取到值，不用在做存在性判断
	 * @param request
	 * @return
	 */
	UserEntity queryUserByRequest(HttpServletRequest request);

	void save(TokenEntity token);
	
	void update(TokenEntity token);

	/**
	 * 情况token
	 * @param userid
	 */
	void clearToken(Long userid);

	/**
	 * 生成token
	 * @param userId  用户ID
	 * @return        返回token相关信息
	 */
	Map<String, Object> createToken(Long userId);
}
