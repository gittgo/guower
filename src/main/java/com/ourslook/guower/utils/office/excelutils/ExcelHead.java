package com.ourslook.guower.utils.office.excelutils;

import com.ourslook.guower.utils.DateUtils;
import com.ourslook.guower.utils.DateUtils8;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel头信息
 *
 * @createTime: 2014-8-19 下午12:03:49
 * @author: zhanglin
 * @version: 0.1
 */
public class ExcelHead {

    /**
     * 列信息
     */
    private List<ExcelColumn> columns;

    /**
     * 需要转换的列
     * key(String): columes 对应的列
     * value(Map) : 对应转换关系
     * eg:
     * Map<Integer, String> isReceive3 = new HashMap<>();
     * isReceive3.put(-1, "锁定");
     * isReceive3.put(0, "正常");
     * isReceive3.put(1, "有效");
     * isReceive3.put(2, "发布");
     * isReceive3.put(3, "已经删除");
     * excelColumnsConvertMap.put("realstatus", isReceive3);
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Map> columnsConvertMap;

    /**
     * 转换函数,对columnsConvertMap的补充
     * <p>
     * eg:
     * Map<String, ExcelColumCellInterface> excelColumnsConvertFunMap = new HashMap<>();
     * excelColumnsConvertFunMap.put("createtime", new ExcelHead.ExcelColumCellLocalDateTime());
     * excelColumnsConvertFunMap.put("userPayMoney", new ExcelHead.ExcelColumCellBigDecimal("元"));
     */
    private Map<String, ExcelColumCellInterface> columnsConvertFunMap;

    /**
     * 转换函数,对columnsConvertFunMap进一步补充
     */
    private Map<Class, ExcelColumCellInterface> columnsTypeConvertFunMap = new HashMap<>();

    /**
     * 头部所占用的行数
     */
    private int rowCount;

    /**
     * 头部所占用的列数
     */
    private int columnCount;

    /**
     * 导出信息说明，如：
     * <p>
     * #起始日期：2018-03-20 00:00:00  终止日期：2018-03-20 23:59:59
     * #交易金额合计：55笔，共60.00元
     * #下载时间：2018-03-21 08:35:18
     * <p>
     * 使用\n作为换行符号
     */
    private String exportDateNotes;

    public ExcelHead() {
        columnsTypeConvertFunMap.put(LocalDate.class, new ExcelColumCellLocalDate());
        columnsTypeConvertFunMap.put(LocalDateTime.class, new ExcelColumCellLocalDateTime());
        columnsTypeConvertFunMap.put(Date.class, new ExcelColumCellDate());
        columnsTypeConvertFunMap.put(BigDecimal.class, new ExcelColumCellBigDecimal());
    }

    public List<ExcelColumn> getColumns() {
        return columns;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumns(List<ExcelColumn> columns) {
        this.columns = columns;
        this.columnCount = (columns == null ? 0 : columns.size());
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    @SuppressWarnings("rawtypes")
    public Map<String, Map> getColumnsConvertMap() {
        return columnsConvertMap;
    }

    @SuppressWarnings("rawtypes")
    public void setColumnsConvertMap(Map<String, Map> columnsConvertMap) {
        this.columnsConvertMap = columnsConvertMap;
    }

    public Map<String, ExcelColumCellInterface> getColumnsConvertFunMap() {
        return columnsConvertFunMap;
    }

    public void setColumnsConvertFunMap(Map<String, ExcelColumCellInterface> columnsConvertFunMap) {
        this.columnsConvertFunMap = columnsConvertFunMap;
    }

    public String getExportDateNotes() {
        return exportDateNotes;
    }

    public void setExportDateNotes(String exportDateNotes) {
        this.exportDateNotes = exportDateNotes;
    }

    @Override
    public String toString() {
        return "ExcelHead [columnCount=" + columnCount + ", columns=" + columns
                + ", columnsConvertMap=" + columnsConvertMap + ", rowCount="
                + rowCount + "]";
    }

    public Map<Class, ExcelColumCellInterface> getColumnsTypeConvertFunMap() {
        return columnsTypeConvertFunMap;
    }

    public void setColumnsTypeConvertFunMap(Map<Class, ExcelColumCellInterface> columnsTypeConvertFunMap) {
        this.columnsTypeConvertFunMap = columnsTypeConvertFunMap;
    }

    /**
     * 默认实现-日期
     */
    public static class ExcelColumCellDate implements ExcelColumCellInterface<Date> {
        public ExcelColumCellDate() {
        }

        @Override
        public String covert(Date date) {
            return DateUtils.parseDateTime(date, DateUtils.YYYYMMDDHHMMSS);
        }
    }

    /**
     * 默认实现-日期
     */
    public static class ExcelColumCellLocalDateTime implements ExcelColumCellInterface<LocalDateTime> {
        public ExcelColumCellLocalDateTime() {
        }

        @Override
        public String covert(LocalDateTime date) {
            return DateUtils8.parseDate(date, DateUtils8.TimeFormat.LONG_DATE_PATTERN_LINE);
        }
    }

    /**
     * 默认实现-日期
     */
    public static class ExcelColumCellLocalDate implements ExcelColumCellInterface<LocalDate> {
        public ExcelColumCellLocalDate() {
        }

        @Override
        public String covert(LocalDate date) {
            return DateUtils8.parseDate(date);
        }
    }

    /**
     * 默认实现-金钱
     * @see com.ourslook.guower.utils.jackson.CustomDoubleSerialize
     */
    public static class ExcelColumCellBigDecimal implements ExcelColumCellInterface<BigDecimal> {
        private String unitStr = "";
        /**
         * 0.00 始终保留小数点收两位
         * #.## 没有小数就不显示
         */
        private String parrern = "0.00";

        public ExcelColumCellBigDecimal() {
        }

        public ExcelColumCellBigDecimal(String unitStr) {
            this.unitStr = unitStr;
        }

        public ExcelColumCellBigDecimal(String unitStr, String parrern) {
            this.unitStr = unitStr;
            this.parrern = parrern;
        }

        public String getUnitStr() {
            return unitStr;
        }

        public void setUnitStr(String unitStr) {
            this.unitStr = unitStr;
        }

        public String getParrern() {
            return parrern;
        }

        public void setParrern(String parrern) {
            this.parrern = parrern;
        }

        @Override
        public String covert(BigDecimal decimal) {
            //DecimalFormat df = new DecimalFormat("￥##.####");
            DecimalFormat df = new DecimalFormat(parrern);
            String money = null;
            try {
                money = df.format(decimal) + (unitStr == null ? "" : unitStr);
            } catch (Exception e) {
                money = "";
            }
            return money;
        }
    }
}
