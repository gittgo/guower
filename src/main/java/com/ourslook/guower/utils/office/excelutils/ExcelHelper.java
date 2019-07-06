package com.ourslook.guower.utils.office.excelutils;


import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.office.csvexports.CsvExportor;
import com.ourslook.guower.utils.office.csvexports.TableModel;
import jodd.bean.BeanUtil;
import jodd.datetime.JDateTime;
import jodd.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * excel帮助类
 *
 * @createTime: 2014-8-19 下午12:03:49
 * @author: zhanglin
 * @version: 0.1
 */
@SuppressWarnings("all")
public class ExcelHelper {

    private static ExcelHelper helper = null;

    private ExcelHelper() {

    }

    public static synchronized ExcelHelper getInstanse() {
        if (helper == null) {
            helper = new ExcelHelper();
        }
        return helper;
    }

    /**
     * 将Excel文件导入到list对象
     *
     * @param head 文件头信息
     * @param fis  导入的数据源  new FileInputStream(file)
     * @param cls  保存当前数据的对象
     * @return List
     * 2014-8-19 下午01:17:48
     * @auther zhanglin
     */
    @SuppressWarnings("rawtypes")
    public List importToObjectList(ExcelHead head, InputStream fis, Class cls) {
        List contents = null;
        // 根据excel生成list类型的数据
        List<List> rows;
        try {
            rows = excelFileConvertToList(fis);

            // 删除头信息
            for (int i = 0; i < head.getRowCount(); i++) {
                rows.remove(0);
            }

            // 将表结构转换成Map
            Map<Integer, String> excelHeadMap = convertExcelHeadToMap(head.getColumns());
            // 构建为对象
            contents = buildDataObject(excelHeadMap, head.getColumnsConvertMap(), rows, cls);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contents;
    }

    /**
     * 导出数据至Excel文件
     *
     * @param head     报表头信息
     *                 //@param excelHeadConvertMap   需要对数据进行特殊转换的列
     *                 //@param modelFile  模板Excel文件
     *                 //@param outputFile 导出文件
     * @param dataList 导入excel报表的数据来源
     * @return void
     * 2014-8-19 下午01:17:48
     * @auther zhanglin
     */
    @SuppressWarnings("rawtypes")
    public void exportExcelFile(HttpServletRequest request, HttpServletResponse response, ExcelHead head, List<?> dataList) {
        this.exportExcelFile(request, response, head, dataList, null);
    }

    /**
     * 导出数据到Cvs文件
     *
     * @param csvExportor 导出对象
     * @param request
     * @param response
     * @param head        头部数据对象不能为空
     * @param dataList    数据对象不能为空
     * @param fileName    文件名称，可以为空
     */
    @SuppressWarnings("all")
    public void exportCsvFile(CsvExportor csvExportor, HttpServletRequest request, HttpServletResponse response, ExcelHead head, List<?> dataList, String fileName) {
        TableModel tableModel = new TableModel();
        tableModel.setFilename(XaUtils.isNotBlank(fileName) ? fileName : LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
        tableModel.addHeaders();
        tableModel.setExportDateNotess(head.getExportDateNotes());
        //数据进行处理（格式化、取出无用的字段等处理）, data 可以是List<Object> or List<Map>
        List<Map<String, String>> data = new ArrayList<>();
        tableModel.setData(data);
        try {
            //创建一个工作区域
            List<ExcelColumn> excelColumns = head.getColumns();
            Map<String, Map> excelHeadConvertMap = head.getColumnsConvertMap();
            Map<String, ExcelColumCellInterface> columnsConvertFunMap = head.getColumnsConvertFunMap();
            // 将表结构转换成Map
            Map<Integer, String> excelHeadMap = convertExcelHeadToMap(excelColumns);

            // 生成导出数据
            for (int i = 0; i <= dataList.size(); i++) {
                Map<String, String> columMap = new HashMap<>();
                if (i != 0) {
                    data.add(columMap);
                }
                for (int j = 0; j < excelHeadMap.size(); j++) {
                    if (i == 0) {
                        //添加header key的值
                        tableModel.addHeaders(excelColumns.get(j).getFieldName());
                        //添加header 显示值
                        tableModel.addHeaderDispalyNames(excelColumns.get(j).getFieldDispName());
                    } else {
                        String fieldName = excelHeadMap.get(j);
                        if (fieldName != null) {
                            //普通的字段
                            if (!fieldName.contains(".")) {
                                Object valueObject = BeanUtil.getProperty(dataList.get(i - 1), fieldName);
                                // 如果存在需要转换的字段信息，则进行转换
                                if (excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
                                    Object covertValue = excelHeadConvertMap.get(fieldName).get(valueObject);
                                    if (covertValue == null) {
                                        valueObject = excelHeadConvertMap.get(fieldName).get(valueObject + "");
                                    } else {
                                        valueObject = covertValue;
                                    }
                                }
                                //转换函数
                                if (columnsConvertFunMap != null && columnsConvertFunMap.get(fieldName) != null) {
                                    valueObject = columnsConvertFunMap.get(fieldName).covert(valueObject);
                                }
                                columMap.put(fieldName, valueObject == null ? "" : String.valueOf(valueObject));
                            } else {
                                // duandazhi add
                                //类似shopvo.user这种；最多支持2级
                                String farentBeanName = fieldName.split("\\.")[0];
                                String subBeanName = fieldName.split("\\.")[1];
                                Object valueObject1 = BeanUtil.getProperty(dataList.get(i - 1), farentBeanName);
                                Object valueObject2 = BeanUtil.getProperty(valueObject1, subBeanName);
                                // 如果存在需要转换的字段信息，则进行转换
                                if (excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
                                    Object covertValue = excelHeadConvertMap.get(fieldName).get(valueObject2);
                                    if (covertValue == null) {
                                        valueObject2 = excelHeadConvertMap.get(fieldName).get(valueObject2 + "");
                                    } else {
                                        valueObject2 = covertValue;
                                    }
                                }
                                //转换函数
                                if (columnsConvertFunMap != null && columnsConvertFunMap.get(fieldName) != null) {
                                    valueObject2 = columnsConvertFunMap.get(fieldName).covert(valueObject2);
                                }
                                columMap.put(fieldName, valueObject2 == null ? "" : String.valueOf(valueObject2));
                            }
                        }
                    }
                }
            }

            csvExportor.export(request, response, tableModel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @SuppressWarnings("all")
    public void exportExcelFile(HttpServletRequest request, HttpServletResponse response, ExcelHead head, List<?> dataList, String fileName) {
        //创建excel
        /**
         * HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls；
         * XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx；
         * SXSSFWorkbook 从POI 3.8版本开始，提供了一种基于XSSF的低内存占用的API
         *
         * https://poi.apache.org/spreadsheet/quick-guide.html#NewLinesInCells
         */
        Workbook wb = new SXSSFWorkbook();
        try {
            //创建一个工作区域
            Sheet sheet = wb.createSheet();
            List<ExcelColumn> excelColumns = head.getColumns();
            Map<String, Map> excelHeadConvertMap = head.getColumnsConvertMap();
            Map<String, ExcelColumCellInterface> columnsConvertFunMap = head.getColumnsConvertFunMap();
            Map<Class, ExcelColumCellInterface> columnsTypeConvertFunMap = head.getColumnsTypeConvertFunMap();
            // 将表结构转换成Map
            Map<Integer, String> excelHeadMap = convertExcelHeadToMap(excelColumns);
            // 生成导出数据
            for (int i = 0; i <= dataList.size(); i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < excelHeadMap.size(); j++) {
                    if (i == 0) {
                        row.createCell(j).setCellValue(excelColumns.get(j).getFieldDispName());
                    } else {
                        String fieldName = excelHeadMap.get(j);
                        CellType type = excelColumns.get(j).getType();
                        if (fieldName != null) {
                            //普通的字段
                            if (!fieldName.contains(".")) {
                                Object valueObject = BeanUtil.getProperty(dataList.get(i - 1), fieldName);
                                // 如果存在需要转换的字段信息，则进行转换
                                boolean isConvert = false;
                                if (excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
                                    Object covertValue = excelHeadConvertMap.get(fieldName).get(valueObject);
                                    if (covertValue == null) {
                                        valueObject = excelHeadConvertMap.get(fieldName).get(valueObject + "");
                                    } else {
                                        valueObject = covertValue;
                                    }
                                    isConvert = true;
                                }
                                //转换函数
                                if (columnsConvertFunMap != null && columnsConvertFunMap.get(fieldName) != null) {
                                    valueObject = columnsConvertFunMap.get(fieldName).covert(valueObject);
                                    isConvert = true;
                                }
                                //还没有找到对应的转换器，使用默认的类型Class转换器
                                if (!isConvert && valueObject != null && valueObject != "") {
                                    Class valueObjectClass = valueObject.getClass();
                                    if (columnsTypeConvertFunMap != null && columnsTypeConvertFunMap.get(valueObjectClass) != null) {
                                        valueObject = columnsTypeConvertFunMap.get(valueObjectClass).covert(valueObject);
                                        isConvert = true;
                                    }
                                }
                                this.createCellAndFormat(wb, row, j, valueObject, type);
                            } else {
                                // duandazhi add
                                //类似shopvo.user这种；最多支持2级
                                String farentBeanName = fieldName.split("\\.")[0];
                                String subBeanName = fieldName.split("\\.")[1];
                                Object valueObject1 = BeanUtil.getProperty(dataList.get(i - 1), farentBeanName);
                                Object valueObject2 = BeanUtil.getProperty(valueObject1, subBeanName);
                                boolean isConvert = false;
                                // 如果存在需要转换的字段信息，则进行转换
                                if (excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
                                    Object covertValue = excelHeadConvertMap.get(fieldName).get(valueObject2);
                                    if (covertValue == null) {
                                        valueObject2 = excelHeadConvertMap.get(fieldName).get(valueObject2 + "");
                                    } else {
                                        valueObject2 = covertValue;
                                    }
                                    isConvert = true;
                                }
                                //转换函数
                                if (columnsConvertFunMap != null && columnsConvertFunMap.get(fieldName) != null) {
                                    valueObject2 = columnsConvertFunMap.get(fieldName).covert(valueObject2);
                                    isConvert = true;
                                }
                                //还没有找到对应的转换器，使用默认的类型Class转换器
                                if (!isConvert && valueObject2 != null && valueObject2 != "") {
                                    Class valueObjectClass = valueObject2.getClass();
                                    if (columnsTypeConvertFunMap != null && columnsTypeConvertFunMap.get(valueObjectClass) != null) {
                                        valueObject2 = columnsTypeConvertFunMap.get(valueObjectClass).covert(valueObject2);
                                        isConvert = true;
                                    }
                                }
                                Cell cell = CellUtil.createCell(row, j, valueObject2 == null ? "" : String.valueOf(valueObject2),
                                        null);
                                this.createCellAndFormat(wb, row, j, valueObject2, type);
                            }
                        }
                    }
                    sheet.setColumnWidth(j, (String.valueOf(Math.random()).getBytes().length) * 256);
                }
            }


            try {
                //增加筛选功能,sheetPos 如：B1、D2;  目前有问题，只有最后设置的一列有效
                String sheetPos = XaUtils.numToChar(0 + (excelHeadMap.size()- 1)) + "" + (head.getRowCount());
                CellRangeAddress address = CellRangeAddress.valueOf(sheetPos);
                sheet.setAutoFilter(address);
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }

            //表格底部的附加信息
            if (StringUtils.isNotBlank(head.getExportDateNotes())) {
                //------------------------------------------------------------------------------------
                String[] notes = head.getExportDateNotes().split("\n");
                for (int i = 0; i < notes.length; ++i) {
                    setFooterNote(wb, sheet, head, dataList, notes[i], i + 1);
                }
            }

            //设置默认文件名为当前时间：年月日时分秒
            if (fileName == null || "".equalsIgnoreCase(fileName.trim())) {
                fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            }

            response.reset();
            //改成输出excel文件
            response.setContentType(ServletUtils.EXCEL_TYPE);
            //设置下载头
            ServletUtils.setFileDownloadHeader(request, response, fileName + ".xlsx");
            //输出
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            IOUtils.closeQuietly(fileOut);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 将Excel文件内容转换为List对象
     *
     * @param fis excel文件
     * @return List<List>
     * 2014-8-19 下午01:17:48
     * @throws IOException
     * @auther zhanglin
     */
    @SuppressWarnings({"rawtypes", "unused"})
    public List<List> excelFileConvertToList(InputStream fis) throws Exception {
        Workbook wb = WorkbookFactory.create(fis);

        Sheet sheet = wb.getSheetAt(0);

        List<List> rows = new ArrayList<List>();
        for (Row row : sheet) {
            List<Object> cells = new ArrayList<Object>();
            // for (Cell cell : row) { //这种有bug,会出现问题，会跳过空行,导致数据错位！
            for (int j = 0; j < row.getLastCellNum(); ++j) {
                Cell cell = row.getCell(j);
                Object obj = null;

                //空白单元
                if (cell != null) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());

                    switch (cell.getCellTypeEnum()) {
                        case STRING:
                            obj = cell.getRichStringCellValue().getString();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                obj = new JDateTime(cell.getDateCellValue());
                            } else {
                                obj = cell.getNumericCellValue();
                            }
                            break;
                        case BOOLEAN:
                            obj = cell.getBooleanCellValue();
                            break;
                        case FORMULA:
                            obj = cell.getNumericCellValue();
                            break;
                        default:
                            obj = null;
                    }
                }

                cells.add(obj);
            }
            rows.add(cells);
        }
        return rows;
    }

    /**
     * 根据Excel生成数据对象
     *
     * @param excelHeadMap        表头信息
     * @param excelHeadConvertMap 需要特殊转换的单元
     * @param rows
     * @param cls
     * @return void
     * 2014-8-19 下午01:17:48
     * @auther zhanglin
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List buildDataObject(Map<Integer, String> excelHeadMap, Map<String, Map> excelHeadConvertMap, List<List> rows, Class cls) {
        List contents = new ArrayList();
        for (List list : rows) {
            // 如果当前第一列中无数据,则忽略当前行的数据
            if (XaUtils.isEmpty(list) || list.get(0) == null) {
                break;
            }
            // 当前行的数据放入map中,生成<fieldName, value>的形式
            Map<String, Object> rowMap = rowListToMap(excelHeadMap, excelHeadConvertMap, list);

            // 将当前行转换成对应的对象
            Object obj = null;
            try {
                obj = cls.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            BeanUtil.populateBean(obj, rowMap);
            contents.add(obj);
        }
        return contents;
    }

    /**
     * 将行转行成map,生成<fieldName, value>的形式
     *
     * @param excelHeadMap        表头信息
     * @param excelHeadConvertMap excelHeadConvertMap
     * @param list
     * @return Map<String       ,       Object>
     * 2014-8-19 下午01:17:48
     * @auther zhanglin
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Object> rowListToMap(Map<Integer, String> excelHeadMap, Map<String, Map> excelHeadConvertMap, List list) {
        Map<String, Object> rowMap = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            String fieldName = excelHeadMap.get(i);
            // 存在所定义的列
            if (fieldName != null) {
                Object value = list.get(i);
                if (excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
                    value = excelHeadConvertMap.get(fieldName).get(value);
                }
                rowMap.put(fieldName, value);
            }
        }
        return rowMap;
    }

    /**
     * 将报表结构转换成Map
     *
     * @param excelColumns
     * @return void
     * 2014-8-19 下午01:17:48
     * @auther zhanglin
     */
    private Map<Integer, String> convertExcelHeadToMap(List<ExcelColumn> excelColumns) {
        Map<Integer, String> excelHeadMap = new HashMap<>();
        for (ExcelColumn excelColumn : excelColumns) {
            if (StringUtil.isEmpty(excelColumn.getFieldName())) {
                continue;
            } else {
                excelHeadMap.put(excelColumn.getIndex(), excelColumn.getFieldName());
            }
        }
        return excelHeadMap;
    }

    /**
     * 创建cell并设置指定的格式,如果不要格式可以直接去掉CellUtil.createCell的最后一个参数
     */
    private Cell createCellAndFormat(Workbook wb, Row row, int column, Object value, CellType cellType) {
        CellStyle cellStyle = null;
        if (cellType != null && CellType.NUMERIC.equals(cellType)) {
            cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("2"));
            Cell cell = row.createCell(column);
            cell.setCellValue(NumberUtils.toDouble(value + "", 0));
            cell.setCellStyle(cellStyle);
            return cell;
        } else if (cellType != null && value != null && CellType.STRING.equals(cellType)
                && (value instanceof Date)) {
            cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("m/d/yy h:mm"));
            Cell cell = row.createCell(column);
            cell.setCellValue((Date) value);
            cell.setCellStyle(cellStyle);
            return cell;
        }
        return CellUtil.createCell(row, column, value == null ? "" : value + "");
    }

    /**
     * 设置页脚的注释内容，并设置字体颜色以及换行
     */
    private void setFooterNote(Workbook wb, Sheet sheet, ExcelHead head, List<?> dataList, String note, int notesIndex) {
        int startRow = head.getRowCount() + dataList.size() + 2 + notesIndex;
        int columeNum = head.getColumnCount();
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, columeNum));
        //第一行数据
        Row row = sheet.createRow(startRow);
        row.setHeightInPoints(20);
        Cell cell = row.createCell(0);
        {
            //设置字体颜色大小
            //生成单元格样式
            CellStyle cellStyle = wb.createCellStyle();
            //新建font实体
            Font hssfFont = wb.createFont();
            //设置字体颜色
            hssfFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
            //设置删除线   strikeout（删除线）
            hssfFont.setStrikeout(false);
            //设置是否斜体
            hssfFont.setItalic(false);
            //字体大小
            hssfFont.setFontHeightInPoints((short) 14);
            hssfFont.setFontName("楷体");
            //粗体
            hssfFont.setBold(true);
            //先设置为自动换行，换行符 /r/n
            cellStyle.setWrapText(true);
            //设置下滑线   1:有下滑线 0：没有
            hssfFont.setUnderline((byte) 0);
            cellStyle.setFont(hssfFont);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            //设置样式
            cell.setCellStyle(cellStyle);
        }
        cell.setCellValue(new XSSFRichTextString(note));
    }
}
