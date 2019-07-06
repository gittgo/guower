package com.ourslook.guower.service.impl;

import com.ourslook.guower.dao.TokenDao;
import com.ourslook.guower.dao.common.TbUserDao;
import com.ourslook.guower.dao.user.UserDao;
import com.ourslook.guower.entity.TokenEntity;
import com.ourslook.guower.entity.common.TbUserEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.TokenService;
import com.ourslook.guower.utils.XaUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service("tokenService")
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenDao tokenDao;
	@Autowired
	private UserDao userDao;
	//7天后过期
	private final static Long EXPIRE = 3600L * 24L * 7L;

	@Override
	public TokenEntity queryByUserId(Long userId) {
		return tokenDao.queryByUserId(userId);
	}

	@Override
	public TokenEntity queryByToken(String token) {
		return tokenDao.queryByToken(token);
	}

	@Override
	public long queryUseIdByRequest(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (StringUtils.isBlank(token)) {
			token = request.getParameter("token");
		}
		return tokenDao.queryByToken(token).getUserId();
	}

	@Override
	public UserEntity queryUserByRequest(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (StringUtils.isBlank(token)) {
			token = request.getParameter("token");
		}
		if (XaUtils.isNotBlank(token)) {
			TokenEntity tokenEntity = tokenDao.queryByToken(token);
			if (tokenEntity != null) {
				long userid =  tokenDao.queryByToken(token).getUserId();
				return userDao.queryObject(userid);
			}
		}
		return null;

	}

	@Override
	public void save(TokenEntity token){
		tokenDao.save(token);
	}
	
	@Override
	public void update(TokenEntity token){
		tokenDao.update(token);
	}

	@Override
	public void clearToken(Long userid) {
		TokenEntity tokenEntity =  tokenDao.queryByUserId(userid);
		if (tokenEntity != null) {
			tokenEntity.setExpireTime(new Date());
			tokenEntity.setUpdateTime(new Date());
			tokenDao.update(tokenEntity);
		}
	}

	@Override
	public Map<String, Object> createToken(Long userId) {
		//生成一个token
		String token = UUID.randomUUID().toString();
		//当前时间
		Date now = new Date();

		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		TokenEntity tokenEntity = queryByUserId(userId);
		if(tokenEntity == null){
			tokenEntity = new TokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			update(tokenEntity);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("expire", EXPIRE);

		return map;
	}
}
