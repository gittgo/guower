package com.ourslook.guower.utils;

import com.ourslook.guower.utils.validator.Validator;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ourslook.guower.utils.XaUtils.isNotEmpty;
import static org.springframework.util.StringUtils.isEmpty;


/**
 * 时间日期类
 * 其他可以参见
 * 时间处理工具
 *
 * @author zhanglin
 * DateTools
 * <p>
 * {@link org.apache.commons.lang.time.DateUtils } addYears addDays
 * {@link org.apache.commons.lang3.time.DateUtils } truncatedCompareTo  比较
 * {@link jodd.datetime.TimeUtil }
 * <p>
 * DateUtils使用DateUtils8{@link DateUtils8 }替换
 */
@SuppressWarnings({"all"})
public class DateUtils {

    private static SimpleDateFormat[] DATE_FORMATS = null;

    private DateUtils() {
    }

    static {
        final String[] possibleDateFormats = {
                "MM/dd/yyyy HH:mm:ss a",
                "EEE, dd MMM yyyy HH:mm:ss zzz", // RFC_822
                "EEE, dd MMM yyyy HH:mm:ss z", // RFC_822
                "EEE, dd MMM yyyy HH:mm zzzz",
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:sszzzz",
                "yyyy-MM-dd'T'HH:mm:ss z",
                "yyyy-MM-dd'T'HH:mm:ssz", // ISO_8601
                "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HHmmss.SSSz",
                "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
                "yyyy/MM/dd", "yyyy.MM.dd", "yyyy'年'MM'月'dd'日'",
                "EEE,dd MMM yyyy HH:mm:ss zzz", // 容错
                "EEE, dd MMM yyyy HH:mm:ss", // RFC_822
                "dd MMM yyyy HH:mm:ss zzz", // 容错
                "dd MM yyyy HH:mm:ss zzz", // 容错
                "EEE, dd MM yyyy HH:mm:ss", // RFC_822
                "dd MM yyyy HH:mm:ss", // 容错
                "EEE MMM dd HH:mm:ss zzz yyyy" // bokee 的时间格式 Tue Mar 28
                // 02:25:45 CST 2006
        };

        DATE_FORMATS = new SimpleDateFormat[possibleDateFormats.length];
        TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        for (int i = 0; i < possibleDateFormats.length; i++) {
            DATE_FORMATS[i] = new SimpleDateFormat(possibleDateFormats[i],
                    locale);
            DATE_FORMATS[i].setTimeZone(gmtTZ);
        }
    }

    public static final SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat YYYYMM = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat YYYYMMDDT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat YYYYlMMlDD = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat YYYYMMDDline = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat YYYYMMDDHHMMSSline = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat YYYYMMDDHHMMSSCn = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /***
     * 转换日期格式
     * @return
     */
    public static String parseDateTime(Date date, SimpleDateFormat format) {
        if (date == null) {
            return null;
        }
        return format.format(date);
    }

    /***
     * 转换日期格式
     * @return
     */
    public static String parseDateTime(Calendar calendar, SimpleDateFormat format) {

        if (calendar == null) {
            return null;
        }
        return format.format(calendar);
    }

    public static String parseDateTime(Long timeLong, SimpleDateFormat format) {
        if (timeLong == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeLong);
        return parseDateTime(calendar, format);
    }

    /**
     * 转换date
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static String parseDateTime(String dateStr, SimpleDateFormat format) {
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(format.parse(dateStr));
        } catch (ParseException e) {
            return null;
        }
        return format.format(result.getTime());
    }


    /**
     * 获取字符串的日期格式
     *
     * @param strdate 格式串
     * @return 格式化的格式串
     */
    public static Date parseDateTime(String strdate) {
        if (strdate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定格式字符串转日期
     *
     * @param s String类型日期
     *          //@param pattern 格式
     * @return 格式化的日期
     */
    public static Date parseDateTimeObj(String s, SimpleDateFormat sdf) {
        if (s == null) {
            return null;
        }
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将时间字符串格式化成一个日期(java.util.Date)
     *
     * @param dateStr   要格式化的日期字符串，如"2014-06-15 12:30:12"
     * @param formatStr 格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    public static Date formatDateString2Date(String dateStr, String formatStr) {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间字符串格式化成一个日期(java.util.Date)
     * <p>
     * //@param dateStr
     * 要格式化的日期字符串，如"2014-06-15 12:30:12"
     *
     * @param formatStr 格式化模板，如"yyyy-MM-dd HH:mm:ss"
     * @return the string
     */
    public static String formatDate2String(Date date, String formatStr) {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        String result = null;
        try {
            result = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将一个毫秒数时间转换为相应的时间格式
     *
     * @param longSecond
     * @return
     */
    public static String formateDateLongToString(long longSecond) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(longSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(gc.getTime());
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    public static String fomatDate(String date) {
        if (isNotEmpty(date)) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8);
        }
        return null;
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    public static String fomatLongDate(String date) {
        if (isNotEmpty(date)) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8) + " " + date.substring(8, 10) + ":"
                    + date.substring(10, 12) + ":" + date.substring(12, 14);
        }
        return null;
    }

    /**
     * 格式化时间.
     *
     * @param date the date
     * @return the string
     */
    public static String fomatDateTime2String(String date) {
        if (isNotEmpty(date)) {
            return date.replace("-", "").replace("DistanceUtils", "").replace(":", "")
                    .replace(" ", "");
        }
        return null;
    }

    public static String format(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }


    /**
     * 获取今天的时间
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(SimpleDateFormat format) {
        Calendar result = Calendar.getInstance();
        return format.format(result.getTime());
    }

    /**
     * 获取今天的时间 YYYY-MM-dd对应的 对象
     *
     * @return
     */
    public static Date getCurrentDateNoMin() {
        GregorianCalendar result = new GregorianCalendar();
        result.setTime(new Date());
        return result.getTime();
    }

    /**
     * 获取时间字符串
     * 格式：yyyyMMDDHHmmss
     *
     * @return
     */
    public static String getCurrentDateSimple() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间.
     * <p>
     * //@param currentDate
     * the current date
     *
     * @return
     * @throws
     */
    public static String getCurrentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间当作文件名称
     *
     * @return
     */
    public static String getCurrentDateAsFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前是周几
     *
     * @return 周几
     */
    public static String getWeek() {
        Calendar cld = Calendar.getInstance();
        int week = cld.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
        }
        return null;
    }

    /**
     * 计算两个日期之前的日期差
     *
     * @param date1 <String> yyyy-MM
     * @param date2 <String> yyyy-MM
     * @return int
     * @throws ParseException
     */
    public static int getMonthSpace(String date1, String date2)
            throws ParseException {

        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));

        result = c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH);

        return result == 0 ? 1 : Math.abs(result);
    }

    /**
     * 获取下一天.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parseKeyUuid exception
     */
    public static String getNextDateStr(String currentDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        String nextDate = sdf.format(calendar.getTime());
        return nextDate;
    }

    /**
     * 获取下年.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parseKeyUuid exception
     */
    public static String getNextYearDateStr(Date currentDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = sdf.parse(sdf.format(currentDate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        String nextDate = sdf.format(calendar.getTime());
        return nextDate;
    }

    /**
     * 获取上一天.
     *
     * @param currentDate the current date
     * @return the next date str
     * @throws ParseException the parseKeyUuid exception
     */
    public static String getYesterdayStr(String currentDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        String nextDate = sdf.format(calendar.getTime());
        return nextDate;
    }

    /**
     * 根据日期获取星期
     *
     * @param strdate
     * @return
     */
    public static String getWeekDayByDate(String strdate) {
        final String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        }
        return dayNames[dayOfWeek];
    }


    /**
     * 获得在指定时间上增加指定分钟数后的时间
     *
     * @param date   指定时间
     * @param minute 指定分钟数
     * @return 返回新的实践
     */
    public static Date addMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 根据时间加或减天数
     *
     * @param date 指定时间
     * @param day  天数
     * @return 添加day后的时间
     * @see org.apache.commons.lang3.time.DateUtils addDays
     */
    public static Date addDay(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return calendar.getTime();
    }

    /**
     * 根据时间加或减天数,忽略时分秒
     */
    public static Date addDayIgoreTime(Date date, Integer i) {
        date = addDay(date, i);
        return formatDateString2Date(formatDate2String(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * 根据时间【减】天数,忽略时分秒
     */
    public static Date addDayNegativeIgoreTime(Date date, Integer i) {
        date = addDay(date, -i);
        return formatDateString2Date(formatDate2String(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * 计算时间
     *
     * @param startDate
     * @param type type(月、周、日) month、week、day、hours
     * @param number
     * @return
     */
    public static Date calculateDate(Date startDate, String type, Integer number) {
        return DateUtils.parseDateTimeObj(calculateDate(DateUtils.format(startDate, DATE_TIME_PATTERN), type, number), DateUtils.YYYYMMDDHHMMSS);
    }

    /**
     * 计算时间
     *
     * @param startDate
     * @param type(月、周、日) month、week、day、hours
     * @return
     */
    public static String calculateDate(String startDate, String type, Integer number) {
        if (isEmpty(startDate)) {
            return null;
        }
        if (!(Validator.isDate(startDate) || Validator.isDateToSecond(startDate))) {
            return null;
        }
        String endDate = "";
        Calendar cal = Calendar.getInstance();

        String dateStr = startDate.split(" ")[0];
        String timeStr = startDate.split(" ")[1];
        String[] splits = (startDate.split(" ")[0]).split("-");

        if (splits.length <= 1) {
            cal.set(Integer.valueOf(splits[0]), Integer.valueOf(splits[1]) - 1, Integer.valueOf(splits[2]));
        } else {
            cal.set(Integer.valueOf(dateStr.split("-")[0]), Integer.valueOf(dateStr.split("-")[1]) - 1, Integer.valueOf(dateStr.split("-")[2]), Integer.valueOf(timeStr.split(":")[0]), Integer.valueOf(timeStr.split(":")[1]), Integer.valueOf(timeStr.split(":")[2]));
        }

        if ("month".equals(type)) {
            cal.add(Calendar.MONTH, number);
        } else if ("week".equals(type)) {
            cal.add(Calendar.WEEK_OF_MONTH, number);
        } else if ("day".equals(type)) {
            cal.add(Calendar.DAY_OF_MONTH, number);
        } else if ("hours".equals(type)) {
            cal.add(Calendar.HOUR_OF_DAY, number);
        }
        //输出日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        endDate = format.format(cal.getTime());
        return endDate;
    }

    /**
     * 计算两个时间之间相差的天数，忽略时分秒的
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int daysBetween(Date startTime, Date endTime) {
        if (isEmpty(startTime) || isEmpty(endTime)) {
            return 0;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDateString2Date(formatDate2String(startTime, "yyyy-MM-dd"), "yyyy-MM-dd"));
        long time1 = cal.getTimeInMillis();

        cal.setTime(formatDateString2Date(formatDate2String(endTime, "yyyy-MM-dd"), "yyyy-MM-dd"));
        long time2 = cal.getTimeInMillis();

        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个时间之间相差的天数
     *
     * @param startTime
     * @param endTime
     * @param isIgoreMin true:是否忽略分钟，如忽略，则 23.99 ~ 第二天1.00 是一天
     *                   false:是0天
     * @return
     */
    public static int daysBetween(Date startTime, Date endTime, boolean isIgoreMin) {
        if (isIgoreMin) {
            return daysBetween(startTime, endTime);
        } else {
            if (isEmpty(startTime) || isEmpty(endTime)) {
                return 0;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endTime);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        }
    }


    /**
     * 计算两个日期之间相差的天数; 如:距离生日的事件
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;
    }


    /**
     * 计算两个时间之间相差的分数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int minutesBetween(String startTime, String endTime) {
        if (isEmpty(startTime) || isEmpty(endTime)) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDateString2Date(startTime, "yyyy-MM-dd HH:mm:ss"));
        long time1 = cal.getTimeInMillis();
        cal.setTime(formatDateString2Date(endTime, "yyyy-MM-dd HH:mm:ss"));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 60);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int minutesBetween(Date startTime, Date endTime) {
        if (isEmpty(startTime) || isEmpty(endTime)) {
            return 0;
        }
        return minutesBetween(formatDate2String(startTime, "yyyy-MM-dd HH:mm:ss"),
                formatDate2String(endTime, "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 判断两个时间的时分是否相同
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean minutediff(String startTime, String endTime) {
        if (isEmpty(startTime) || isEmpty(endTime)) {
            return false;
        }
        String[] arr = startTime.split(" ")[1].split(":");
        String time = arr[0] + ":" + arr[1];
        String[] arr1 = endTime.split(" ")[1].split(":");
        String time1 = arr1[0] + ":" + arr1[1];
        return time.equals(time1);
    }

    /**
     * 判断两个时间的时分是否相同
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean hoursdiff(String startTime, String endTime) {
        if (isEmpty(startTime) || isEmpty(endTime)) {
            return false;
        }
        String[] arr = startTime.split(" ")[1].split(":");
        String time = arr[0];
        String[] arr1 = endTime.split(" ")[1].split(":");
        String time1 = arr1[0];
        return time.equals(time1);
    }


    /**
     * truncatedCompareTo
     * <p>
     * 判断日期的大小
     *
     * @param startTime
     * @param endTime
     * @return
     * @deprecated
     */
    public static boolean judgeDateGT(String startTime, String endTime) {
        if (isEmpty(startTime) || isEmpty(endTime)) {
            return false;
        }
        Date date1 = formatDateString2Date(startTime, "yyyy-MM-dd");
        Date date2 = formatDateString2Date(endTime, "yyyy-MM-dd");
        return date2.compareTo(date1) > 0 ? true : false;
    }

    /**
     * 日期格式判断
     *
     * @param sDate 传过来的date
     * @return 布尔
     */
    public static boolean isValidDate(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isLeapyear(int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.isLeapYear(year);
    }

    public static boolean isLeapyear(Date date) {
        int curYear = Integer.parseInt(DateUtils.parseDateTime(new Date(), DateUtils.YYYY));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.isLeapYear(curYear);
    }

    public static int getDaysInYear(Date date) {
        int days = 365;
        if (isLeapyear(date)) {
            days = 366;
        }
        return days;
    }

    /**
     * 获取这一年所有的工作日？？
     * @param year
     * @return
     */
    public static Map<String, List<Date>> getDatesByWeek(Integer year) {
        Calendar c_begin = new GregorianCalendar();
        Calendar c_end = new GregorianCalendar();
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] weeks = dfs.getWeekdays();
        c_begin.set(year, 0, 1); //Calendar的月从0-11，
        c_end.set(year, 11, 31); //Calendar的月从0-11，
        List<Date> scheduledDates = new ArrayList<Date>();
        Map<String, List<Date>> map = new HashMap<String, List<Date>>();
        int count = 1;
        c_end.add(Calendar.DAY_OF_YEAR, 1);  //结束日期下滚一天是为了包含最后一天

        while (c_begin.before(c_end)) {
            System.out.println("第" + count + "周  日期：" + new java.sql.Date(c_begin.getTime().getTime()) + "," + weeks[c_begin.get(Calendar.DAY_OF_WEEK)]);
            String DAY_OF_WEEK = weeks[c_begin.get(Calendar.DAY_OF_WEEK)];

            Date date = c_begin.getTime();
            if (!map.containsKey(DAY_OF_WEEK)) {
                map.put(DAY_OF_WEEK, new ArrayList<Date>());
            }
            date = parseDateTimeObj(parseDateTime(date, YYYYMMDD), DateUtils.YYYYMMDD);
            map.get(DAY_OF_WEEK).add(date);

            if (c_begin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                count++;
            }
            c_begin.add(Calendar.DAY_OF_YEAR, 1);
        }
        return map;
    }

    /**
     * 分钟变成小时，如60分钟-->1小时;  90分钟显示：1小时30分钟
     * @param duration 单位是分钟
     * @return
     */
    public static String getDurationText(int duration) {
        StringBuilder buff = new StringBuilder();
        int hour = duration / 3600;
        int minute = duration / 60 % 60;
        int second = duration % 60;
        if (hour > 0) {
            buff.append(hour).append("时");
        }
        if (minute > 0) {
            buff.append(minute).append("分");
        }
        if (second > 0) {
            buff.append(second).append("秒");
        }
        return buff.toString();
    }

    /**
     *  根据年、月 计算天数
     *  获取一个月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
