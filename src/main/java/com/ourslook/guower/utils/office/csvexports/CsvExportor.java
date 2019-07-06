package com.ourslook.guower.utils.office.csvexports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lingo
 * @date 2018/3/19 下午7:23
 */
public interface CsvExportor {
    void export(HttpServletRequest request, HttpServletResponse response,
                TableModel tableModel) throws IOException;
}
