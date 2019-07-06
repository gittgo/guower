package com.ourslook.guower.utils;

import com.ourslook.guower.utils.Constant.StringConstant;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author dazer
 * 时间日期的工具类，适用于java1.8+
 * <p>
 * Created by bysocket on 16/8/23.
 * <p>
 * <a href="https://yq.aliyun.com/articles/59478">基于 JDK 8 time包的时间工具类 TimeUtil </a>
 * <a href="https://www.jianshu.com/p/6261f4162cfe">java8时间工具类 </a>
 * <a href="https://www.liaoxuefeng.com/article/00141939241051502ada88137694b62bfe844cd79e12c32000">如何在Java 8中愉快地处理日期和时间 </a>
 * @see DateUtils
 * @see DateUtils8Transform
 * @see Date
 * @see java.time.Instant 瞬间类，表示时间线上的一点（与 Date 类似）
 * @see java.time.Duration 持续时间，表示两个 Instant 之间的时间
 * @see java.time.Clock
 * @see java.time.LocalTime  和日期、时区无关
 * @see java.time.LocalDate  和时分秒无关
 * @see java.time.LocalDateTime 本地日期时间，表示一个日期和时间。
 * <p>
 * <p>
 * <p>
 * SQL -> Java
 * --------------------------
 * date -> LocalDate
 * time -> LocalTime
 * timestamp -> LocalDateTime
 */
public class DateUtils8 {
    /**
     * 默认时间格式
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.SHORT_DATE_PATTERN_LINE.formatter;

    /**
     * 无参数的构造函数,防止被实例化
     */
    private DateUtils8() {
    }

    /**
     * String 转化为 LocalDateTime
     *
     * @param timeStr 被转化的字符串 eg:2017-12-09
     * @return LocalDateTime
     */
    public static LocalDate parseDate(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return null;
        }
        if (timeStr.contains(StringConstant.WHITESAACE.getValue())) {
            timeStr = timeStr.split(StringConstant.WHITESAACE.getValue(), 1)[0];
        }
        return LocalDate.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * @param timeStr 待处理的字符串，eg:2017-12-09 19:25:56
     * @return
     */
    public static LocalDateTime parseDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, TimeFormat.LONG_DATE_PATTERN_LINE.formatter);
    }

    /**
     * String 转化为 LocalDateTime
     *
     * @param timeStr    被转化的字符串
     * @param timeFormat 转化的时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDate(String timeStr, TimeFormat timeFormat) {
        return LocalDateTime.parse(timeStr, timeFormat.formatter);
    }

    /**
     * LocalDateTime 转化为String yyyy-MM-dd
     *
     * @param time LocalDateTime
     * @return String
     */
    public static String parseDate(LocalDateTime time) {
        if (time == null) {
            return "-";
        }
        return DEFAULT_DATETIME_FORMATTER.format(time);

    }

    /**
     * yyyy-MM-dd
     */
    public static String parseDate(LocalDate time) {
        if (time == null) {
            return "-";
        }
        return DEFAULT_DATETIME_FORMATTER.format(time);

    }

    /**
     * LocalDateTime 时间转 String
     *
     * @param time   LocalDateTime
     * @param format 时间格式
     * @return String
     */
    public static String parseDate(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);

    }

    /**
     * 获取当前时间
     * eg:2017-12-09
     *
     * @return
     */
    public static String getCurrentDate() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间日期
     * eg: 2017-12-09 19:25:56
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return getCurrentDateTime(TimeFormat.LONG_DATE_PATTERN_LINE);
    }

    /**
     * 获取当前时间
     *
     * @param timeFormat 时间格式
     * @return
     */
    public static String getCurrentDateTime(TimeFormat timeFormat) {
        return timeFormat.formatter.format(LocalDateTime.now());
    }

    /**
     * 判断当前时间是否在开始和结束之间之内，包含start 和 end
     *
     * isBetween(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-04-13"))
     * LocalDateTime.parse("2018-01-01T00:01:01"), LocalDateTime.parse("2018-04-12T12:01:01"))
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean isBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return false;
        }
        start = start.isBefore(end) ? start : end;
        end = start.isBefore(end) ? end : start;
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(end) && now.isAfter(start) || start.isEqual(now) || end.isEqual(now)) {
            return true;
        }
        return false;
    }

    public static boolean isBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return false;
        }
        return isBetween(start.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime(),
                end.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime());
    }

    public static boolean isBetween(Date start, Date end) {
        if (start == null || end == null) {
            return false;
        }
        return isBetween(DateUtils8Transform.date2Java8DateTime(start), DateUtils8Transform.date2Java8DateTime(end));
    }

    public static boolean isBetweenByTimeStamp(LocalDateTime startTime, LocalDateTime endTime){
        if (XaUtils.isNotEmpty(startTime) && XaUtils.isNotEmpty(endTime)) {
            Timestamp startTimeStamp = Timestamp.valueOf(startTime);
            Timestamp endTimeStamp = Timestamp.valueOf(endTime);
            if (startTimeStamp.getTime() <= System.currentTimeMillis() && endTimeStamp.getTime() >= System.currentTimeMillis()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 内部枚举类
     *
     * @author xiaowen
     * @date 2016年11月1日 @ version 1.0
     */
    public enum TimeFormat {
        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
//        //获取当前时间
//        System.out.println(DateUtils8.getCurrentDate());
//
//        System.out.println(DateUtils8.getCurrentDateTime());
//
//        System.out.println(DateUtils8.parseDate("2017-12-09"));
//        System.out.println(DateUtils8.parseDateTime("2017-12-09 19:25:56"));
//        System.out.println(DateUtils8.parseDate("2017-12-09 19:25:56", TimeFormat.LONG_DATE_PATTERN_LINE).toString());
//
//        System.out.println(isBetween(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-04-13")));
//        System.out.println(isBetween(LocalDateTime.parse("2018-01-01T00:01:01"), LocalDateTime.parse("2018-04-12T12:01:01")));
    }
}
