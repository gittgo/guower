package com.ourslook.guower.dao.user;


import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.vo.business.AuthorSortVo;

import java.util.List;
import java.util.Map;

/**
 * 用户表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 *
 */
public interface UserDao extends BaseDao<UserEntity> {

    UserEntity queryByMobile(String mobile);

    List<AuthorSortVo> queryAuthorSort(Map<String, Object> map);
	
}
