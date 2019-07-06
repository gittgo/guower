package com.ourslook.guower.utils;

import java.time.*;
import java.util.Date;

/**
 * LocalDate和Date的互换
 *
 * java.sql.Date、java.sql.Timestamp、java.util.Date这些类都不好用，很多方法都过时了。
 * Java8里面新出来了一些API，LocalDate、LocalTime、LocalDateTime 非常好用
 * 如果想要在JDBC中，使用Java8的日期LocalDate、LocalDateTime，则必须要求数据库驱动的版本不能低于4.2
 *
 * @author dazer
 * @date 2018/2/8 下午2:31
 *
 * @see DateUtils8
 * @see DateUtils
 */
public class DateUtils8Transform {

    /**
     * 01. java.util.Date --> java.time.LocalDateTime
     */
    public static LocalDateTime date2Java8DateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    /**
     * 02. java.util.Date --> java.time.LocalDate
     */
    public static LocalDate date2Java8Date(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    /**
     * 03. java.util.Date --> java.time.LocalTime
     */
    public static LocalTime date2Java8Time(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalTime localTime = localDateTime.toLocalTime();
        return localTime;
    }


    /**
     * 04. java.time.LocalDateTime --> java.util.Date
     */
    public static Date java8Date2Date(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }


    /**
     * 05. java.time.LocalDate --> java.util.Date
     */
    public static Date java8Date2Date(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    /**
     * 06. java.time.LocalTime --> java.util.Date
     */
    public static Date java8Date2Date(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    /**
     * java.sql.Date与java.util.Date的转化
     * @param sqlDate
     * @return
     */
    public static Date sqlDate2Date(java.sql.Date sqlDate) {
       return new java.util.Date(sqlDate.getTime());
    }

    public static Date date2SqlDate(Date date) {
        return new java.sql.Date(new java.util.Date().getTime());
    }
}
