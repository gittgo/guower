package com.ourslook.guower.utils.validator;

import java.util.regex.Pattern;

/**
 * @author: zhangl
 * @Times: 2015-9-18下午07:37:09
 * 类的说明：验证数据
 **/
public class Validator {
	
	/**
     * 正则表达式：验证用户名	中英文、数字组成
     */
    public static final String REGEX_USERNAME = "^[\u4E00-\u9FA5A-Za-z0-9]+$";
 
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
 
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3-9]\\d{9}$";
    
    /**
     * 正则表达式：验证电话号码
     */
    public static final String REGEX_TELEPHONE = "^(?:(?:0\\d{2,3}[\\- ]?[1-9]\\d{6,7})|(?:[48]00[\\- ]?[1-9]\\d{6}))$";
 
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[X|x])$)";
 
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
 
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    
    /**
     * 正则表达式：验证日期格式  YYYY-MM-dd
     */
    public static final String REGEX_DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
    
    /**
     * 正则表达式：验证日期格式   YYYY-MM-dd HH:mm
     */
    public static final String REGEX_DATE_HOURSMIN = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}$";

    /**
     * 正则表达式：验证日期格式   YYYY-MM-dd HH:mm:ss
     */
    public static final String REGEX_DATE_SENCOND = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}:\\d{2}$";
    
    /**
     * 正则表达式：验证邮编格式
     */
    public static final String REGEX_POSTCODE = "^\\d{6}$";
    
    /**
     * 正则表达式：验证数字(整数)
     */
    public static final String REGEX_NUMBER = "^\\d+$";
    
    /**
     * 正则表达式：验证数字(小数)
     */
    public static final String REGEX_DOUBLE = "^(-?\\d+)(\\.\\d+)?$";
    
    /**
     * 正则表达式：验证 数字-数字，数字-数字
     */
    public static final String NUMBERANDNUMBERFORMAT = "^((\\d+-\\d+)?(,)?)+$";
 
    /**
     * 校验用户名
     * 
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }
 
    /**
     * 校验密码
     * 
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
 
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        boolean isInternationalMobil = false;
        //国际号码不进行正则校验
        if (isInternationalMobil) {
            return true;
        }
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
    
    /**
     * 校验电话号码
     * 
     * @param telephone
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isTelephone(String telephone) {
    	return Pattern.matches(REGEX_TELEPHONE, telephone);
    }
 
    /**
     * 校验邮箱
     * 
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
 
    /**
     * 判断一个字符串是否含有中文
     * 
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        if (chinese == null) return false;
        for (char c : chinese.toCharArray()) {
            // 有一个中文字符就返回
            if (isChinese(c)) return true;
        }
        return false;
    }

    /**
     * 判断一个字符是否是中文
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        // 根据字节码判断
        return c >= 0x4E00 &&  c <= 0x9FA5;
    }

    /**
     * 校验身份证
     * 
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
 
    /**
     * 校验URL
     * 
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }
 
    /**
     * 校验IP地址
     * 
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
    
    /**
     * 校验邮编
     * 
     * @param postcode
     * @return
     */
    public static boolean isPostCode(String postcode) {
    	return Pattern.matches(REGEX_POSTCODE, postcode);
    }
    
    /**
     * 校验日期格式	YYYY-MM-dd
     * 
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
    	return Pattern.matches(REGEX_DATE, date);
    }
    
    /**
     * 校验日期格式	YYYY-MM-dd HH:mm
     * 
     * @param date
     * @return
     */
    public static boolean isDateToMin(String date) {
    	return Pattern.matches(REGEX_DATE_HOURSMIN, date);
    }
    
    /**
     * 校验日期格式	YYYY-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static boolean isDateToSecond(String date) {
    	return Pattern.matches(REGEX_DATE_SENCOND, date);
    }
    
    /**
     * 校验数字(整数)
     * 
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
    	return Pattern.matches(REGEX_NUMBER, number);
    }
    
    /**
     * 校验数字(小数)
     * 
     * @param lnumber
     * @return
     */
    public static boolean isDouble(String lnumber) {
    	return Pattern.matches(REGEX_DOUBLE, lnumber);
    }
    
    /**
     * 校验数字数字-数字，数字-数字
     * 
     * @param number
     * @return
     */
    public static boolean isNumberAndNumber(String number) {
    	return Pattern.matches(NUMBERANDNUMBERFORMAT, number);
    }

    public static void main(String[] args) {
        System.out.println(isIPAddr("http://47.100.34.60:8080/guower/"));
    }
}
