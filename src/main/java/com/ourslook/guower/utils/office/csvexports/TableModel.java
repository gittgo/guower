package com.ourslook.guower.utils.office.csvexports;

import com.ourslook.guower.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableModel {
    private static Logger logger = LoggerFactory.getLogger(TableModel.class);
    /**
     * 导出csv文件的名称
     */
    private String filename;
    /**
     * 列头部的显示名称
     */
    private List<String> headerDispalyNames = new ArrayList<String>();
    private List<String> headers = new ArrayList<String>();
    private List   data;
    /**
     *  导出附件说明信息，如：
     *  #起始日期：2018-03-20 00:00:00  终止日期：2018-03-20 23:59:59
     *  #交易金额合计：55笔，共60.00元
     *  #下载时间：2018-03-21 08:35:18
     */
    private String exportDateNotess;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void addHeaders(String... header) {
        if (header == null) {
            return;
        }
        for (String text : header) {
            if (text == null) {
                continue;
            }
            headers.add(text);
        }
    }

    public void addHeaderDispalyNames(String... headerDisplayName) {
        if (headerDisplayName == null) {
            return;
        }
        for (String text : headerDisplayName) {
            if (text == null) {
                continue;
            }
            headerDispalyNames.add(text);
        }
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getHeaderCount() {
        return headers.size();
    }

    public int getDataCount() {
        return data.size();
    }

    public String getHeaderDispalyName(int index) {
        return headerDispalyNames.get(index);
    }

    public String getHeader(int index) {
        return headers.get(index);
    }


    public String getValue(int i, int j) {
        String header = getHeader(j);
        Object object = data.get(i);

        if (object instanceof Map) {
            return this.getValueFromMap(object, header);
        } else {
            return this.getValueReflect(object, header);
        }
    }

    public String getValueReflect(Object instance, String fieldName) {
        try {
            String methodName = ReflectUtils.getGetterMethodName(instance,
                    fieldName);
            Object value = ReflectUtils.getMethodValue(instance, methodName);

            return (value == null) ? "" : value.toString();
        } catch (Exception ex) {
            logger.info("error", ex);

            return "";
        }
    }

    /**
     * get method value by filename.
     *
     * @param target
     *            Object
     * @param methodName
     *            method filename
     * @return object
     * @throws Exception
     *             ex
     */
    public static Object getMethodValue(Object target, String methodName)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Method method = target.getClass().getDeclaredMethod(methodName);

        return method.invoke(target);
    }

    public String getValueFromMap(Object instance, String fieldName) {
        Map<String, Object> map = (Map<String, Object>) instance;
        Object value = map.get(fieldName);

        return (value == null) ? "" : value.toString();
    }

    public String getExportDateNotess() {
        return exportDateNotess;
    }

    public void setExportDateNotess(String exportDateNotess) {
        this.exportDateNotess = exportDateNotess;
    }
}
