package com.ourslook.guower.entity.score;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;




/**
 * 单日积分获取上限表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@ApiModel(description = "单日积分获取上限表")
public class InfScoreDayMaxEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Integer id;
    /**
     * 获取途径
     */
    @ApiModelProperty("获取途径")
    private Integer getType;
    /**
     * 单次获取量
     */
    @ApiModelProperty("单次获取量")
    private Integer getNumber;
    /**
     * 单日获取上限
     */
    @ApiModelProperty("单日获取上限")
    private Integer getMax;

        public void setId(Integer id) {
            this.id = id;
    	}

        public Integer getId() {
		return id;
	}
        public void setGetType(Integer getType) {
            this.getType = getType;
    	}

        public Integer getGetType() {
		return getType;
	}
        public void setGetNumber(Integer getNumber) {
            this.getNumber = getNumber;
    	}

        public Integer getGetNumber() {
		return getNumber;
	}
        public void setGetMax(Integer getMax) {
            this.getMax = getMax;
    	}

        public Integer getGetMax() {
		return getMax;
	}
}
