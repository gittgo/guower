package com.ourslook.guower.vo;

import com.ourslook.guower.entity.common.InfDictInfoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @see com.ourslook.guower.entity.common.InfDitcypeInfoEntity
 * 字典表
 */
@ApiModel(description = "字典表", parent = InfDictInfoEntity.class)
public class InfDictVo extends InfDictInfoEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典信息类型，关联字典类型ID")
    private String typeId;
    @ApiModelProperty("字典信息类型，关联字典类型code")
    private String typeCode;
    @ApiModelProperty("字典信息类型，关联字典类型name")
    private String typeName;
    @ApiModelProperty("字典信息全部信息")

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}


