package com.ourslook.guower.vo.business;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;




/**
 * 广告表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@ApiModel(description = "广告表", parent = BusAdvertisementEntity.class)
public class BusAdvertisementVo extends BusAdvertisementEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================
}
