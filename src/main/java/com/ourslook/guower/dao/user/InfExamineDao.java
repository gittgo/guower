package com.ourslook.guower.dao.user;


import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.entity.user.InfExamineEntity;

/**
 * 审核表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 *
 */
public interface InfExamineDao extends BaseDao<InfExamineEntity> {

    void examine(ExamineCheck examineCheck);

    InfExamineEntity queryObjectByUserId(Integer userId);
}
