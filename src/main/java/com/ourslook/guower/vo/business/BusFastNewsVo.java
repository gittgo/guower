package com.ourslook.guower.vo.business;

import com.ourslook.guower.entity.business.BusFastNewsEntity;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;




/**
 * 快报表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@ApiModel(description = "快报表", parent = BusFastNewsEntity.class)
public class BusFastNewsVo extends BusFastNewsEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================
}
