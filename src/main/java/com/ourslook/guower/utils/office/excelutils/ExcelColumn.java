package com.ourslook.guower.utils.office.excelutils;

import org.apache.poi.ss.usermodel.CellType;

/**
 * excel列信息
 *
 * @createTime: 2014-8-19 下午12:03:49
 * @author: zhanglin
 * @version: 0.1
 */
public class ExcelColumn {

    /**
     * 索引
     */
    private int index;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段显示名称
     */
    private String fieldDispName;

    /**
     * 字段类型
     */
    private CellType type;

    public ExcelColumn() {

    }

    public ExcelColumn(int index, String fieldName, String fieldDispName) {
        super();
        this.index = index;
        this.fieldName = fieldName;
        this.fieldDispName = fieldDispName;
    }

    /**
     *
     * @param index
     * @param fieldName
     * @param fieldDispName
     * @param cellType 输出Excel的类型
     */
    public ExcelColumn(int index, String fieldName, String fieldDispName, CellType cellType) {
        super();
        this.index = index;
        this.fieldName = fieldName;
        this.fieldDispName = fieldDispName;
        this.type = cellType;
    }

    public int getIndex() {
        return index;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDispName() {
        return fieldDispName;
    }

    public void setFieldDispName(String fieldDispName) {
        this.fieldDispName = fieldDispName;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "ExcelColumn [fieldDispName=" + fieldDispName + ", fieldName="
                + fieldName + ", index=" + index + ", type=" + type + "]";
    }
}
