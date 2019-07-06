package com.ourslook.guower.vo.score;

import com.ourslook.guower.entity.score.InfCurrencyEntity;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;




/**
 * 货币表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@ApiModel(description = "货币表", parent = InfCurrencyEntity.class)
public class InfCurrencyVo extends InfCurrencyEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================
}
