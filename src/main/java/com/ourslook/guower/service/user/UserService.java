package com.ourslook.guower.service.user;


import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.vo.business.AuthorSortVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
public interface UserService {

	UserEntity queryObject(Integer id);
	
	List<UserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserEntity user);

    void multiOperate(List<String> modelIds, Integer status);

	void update(UserEntity user);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<UserEntity> users, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;

	/**
	 * 根据手机号查询用户
	 * @param mobile
	 * @return
	 */
	UserEntity queryByMobile(String mobile);

	/**
	 * 注册用户
	 * @param mobile
	 * @param password
	 * @param userType
	 * @return
	 */
	UserEntity registUser(String mobile, String password, Integer userType);

	/**
	 * 用户登录
	 * @param mobile 手机号
	 * @param password 密码
	 * @return
	 */
	UserEntity login(String mobile, String password);

    /**
     * 忘记密码
     * @param mobile
     * @param newPassrod
     */
	void forgetPwd(String mobile, String newPassrod);

    /**
     * 修改密码
     * @param userId
     * @param password
     */
	void updatePwd(Integer userId, String password);

	/**
	 * 查询作者排序，需要查询总浏览量，所以单独写service
	 * @return
	 */
	List<AuthorSortVo> queryAuthorSort(Map<String, Object> map);
}
