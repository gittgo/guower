package com.ourslook.guower.utils.xss;

import org.apache.commons.lang.StringUtils;

import com.ourslook.guower.utils.RRException;

/**
 * SQL过滤
 * 
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "create", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.toLowerCase().equalsIgnoreCase(keyword)){
                throw new RRException("包含非法字符");
            }
        }

        return str;
    }
}
