package com.ourslook.guower.utils.office.csvexports;

import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lingo
 * 导出文件成Csv格式
 */
public class DefaultCsvExportor implements CsvExportor {
    /**
     * 如果是GBK格式，excel打开不乱码；但是使用记事本可能乱码；只让用户用excel打开
     * 如果使用UTF-8的时候，excel打开乱码，但是记事本打开不乱吗
     */
    private String encoding = "GBK";

    @Override
    public void export(HttpServletRequest request,
                       HttpServletResponse response, TableModel tableModel)
            throws IOException {
        StringBuilder buff = new StringBuilder();

        //标题信息
        for (int i = 0; i < tableModel.getHeaderCount(); i++) {
            buff.append(encodingDot(tableModel.getHeaderDispalyName(i)));

            if (i != (tableModel.getHeaderCount() - 1)) {
                buff.append(",");
            }
        }

        buff.append("\n");

        //内容数据
        for (int i = 0; i < tableModel.getDataCount(); i++) {
            for (int j = 0; j < tableModel.getHeaderCount(); j++) {
                buff.append(encodingDot(tableModel.getValue(i, j)));

                if (j != (tableModel.getHeaderCount() - 1)) {
                    buff.append(",");
                }
            }

            buff.append("\n");
        }

        //表格底部的附加信息
        if (StringUtils.isNoneBlank(tableModel.getExportDateNotess())) {
            buff.append("------------------------------------------------------------------------------------");
            buff.append("\n");
            buff.append(tableModel.getExportDateNotess());
        }

        response.setContentType(ServletUtils.STREAM_TYPE);
        ServletUtils.setFileDownloadHeader(request, response,
                tableModel.getFilename() + ".csv");
        OutputStream out = response.getOutputStream();
        out.write(buff.toString().getBytes(encoding));
        out.flush();
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 处理数据中的英文逗号，替换成为中文逗号
     *
     * @param str
     * @return
     */
    private String encodingDot(String str) {
        if (str == null || "".equalsIgnoreCase(str)) {
            return "";
        }
        if (str.contains(Constant.StringConstant.DOT.getValue())) {
            //方式1
            //str = str.replaceAll(Constant.StringConstant.DOT.getValue(), Constant.StringConstant.DOT_CN.getValue());
            //方式2
            str = "\"" + str +"\"";
        }
        return str;
    }
}
