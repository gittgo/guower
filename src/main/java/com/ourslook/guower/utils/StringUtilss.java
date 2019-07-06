package com.ourslook.guower.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @see XaUtils
 */
public class StringUtilss {

    public static String capitalize(String text) {
        return StringUtils.capitalize(text);
    }

    public static String substring(String text, int offset, int limit) {
        return StringUtils.substring(text, offset,
                limit);
    }

    public static String substringBefore(String text, String separator) {
        return StringUtils
                .substringBefore(text, separator);
    }

    public static String substringAfter(String text, String separator) {
        return StringUtils.substringAfter(text, separator);
    }

    /**
     * 字符串比较；忽略大小写和顺序
     * 比如：ABC 和 BaC 就是相等的
     *
     * @return
     */
    public static boolean equalsIgnoreCaseAndOrder(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        char[] chars1 = str1.toLowerCase().toCharArray();
        char[] chars2 = str2.toLowerCase().toCharArray();
        Arrays.sort(chars1);
        Arrays.sort(chars2);
        if (new String(chars1).equalsIgnoreCase(new String(chars2))) {
            return true;
        }
        return false;
    }

    public static String[] splitByWholeSeparator(String text, String separator) {
        return StringUtils.splitByWholeSeparator(text,
                separator);
    }

    public static String join(List list, String separator) {
        return StringUtils.join(list, separator);
    }

    public static String escapeHtml(String text) {
        return org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(text);
    }

    public static String unescapeHtml(String text) {
        return org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(text);
    }

    public static String escapeXml(String text) {
        return org.apache.commons.lang3.StringEscapeUtils.escapeXml11(text);
    }

    public static String unescapeXml(String text) {
        return org.apache.commons.lang3.StringEscapeUtils.unescapeXml(text);
    }

    public static String trimToEmpty(String text) {
        return StringUtils.trimToEmpty(text);
    }


    public static String trim(String text) {
        if (text == null) {
            return text;
        }

        // Unicode Character 'NO-BREAK SPACE' (U+00A0)
        text = text.replace("" + ((char) 160), " ");
        // Unicode Character 'ZERO WIDTH SPACE' (U+200B).
        text = text.replace("" + ((char) 8203), " ");

        text = StringUtils.trim(text);
        text = StringUtils.strip(text, "　");

        return text;
    }
}
