package com.ourslook.guower.utils.office.excelutils;

/**
 * @author dazer
 * @Description: 转换接口
 * @date 2018/2/27 下午4:32
 *
 * @see ExcelHead.ExcelColumCellBigDecimal
 * @see ExcelHead.ExcelColumCellLocalDate
 * @see ExcelHead.ExcelColumCellLocalDateTime
 * @see ExcelHead.ExcelColumCellDate
 */
@FunctionalInterface
public interface ExcelColumCellInterface<T> {
    String covert(T t);
}