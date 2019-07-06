package com.ourslook.guower.dao;

import com.ourslook.guower.entity.TokenEntity;
import org.springframework.stereotype.Component;

/**
 * 用户Token
 *
 */
public interface TokenDao extends BaseDao<TokenEntity> {
    
    TokenEntity queryByUserId(Long userId);

    TokenEntity queryByToken(String token);
	
}
