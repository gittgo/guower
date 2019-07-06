package com.ourslook.guower.vo;

import com.ourslook.guower.entity.common.InfVersionInfoEntity;
import com.ourslook.guower.utils.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * 版本更新
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-01-02 13:21:28
 */
@ApiModel(description = "版本更新", parent = InfVersionInfoEntity.class)
public class InfVersionInfoVo extends InfVersionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否强制更新转义")
    private String isforceupdatename;

    /**
     * 设备名称
     * @see InfVersionInfoEntity#device
     */
    @ApiModelProperty("设备中文名称")
    private String deviceName;

    public String getIsforceupdatename() {
        if(this.getIsforceupdate()==0){
            this.isforceupdatename = "否";
        }else if(this.getIsforceupdate()==1){
            this.isforceupdatename = "是";
        }else{
            this.isforceupdatename = "未知状态";
        }
        return isforceupdatename;
    }

    public void setIsforceupdatename(String isforceupdatename) {
        this.isforceupdatename = isforceupdatename;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
