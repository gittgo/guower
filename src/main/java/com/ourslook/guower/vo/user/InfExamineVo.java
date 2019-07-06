package com.ourslook.guower.vo.user;

import com.ourslook.guower.entity.user.InfExamineEntity;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;




/**
 * 审核表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
@ApiModel(description = "审核表", parent = InfExamineEntity.class)
public class InfExamineVo extends InfExamineEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================
}
