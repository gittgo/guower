package com.ourslook.guower.service.user;

import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.entity.user.InfExamineEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 审核表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
public interface InfExamineService {

	InfExamineEntity queryObject(Integer id);
	
	List<InfExamineEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InfExamineEntity infExamine);

    void multiOperate(List<String> modelIds, Integer status);

	void update(InfExamineEntity infExamine);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	/**
	 * 认证审核（同步修改用户认证级别）
	 * @param examineCheck	审核结果实体类
	 */
	void examine(ExamineCheck examineCheck);

    /**
    * 导出成为excel
    */
    void exportsToExcels(List<InfExamineEntity> infExamines, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception;

	/**
	 * 根据userId获取用户认证
	 * @param userId
	 * @return
	 */
	InfExamineEntity queryObjectByUserId(Integer userId);

	/**
	 * 保存认证的同时要更新user表
	 * @param examineEntity
	 */
	void saveAndUpdateUser(InfExamineEntity examineEntity);
}
