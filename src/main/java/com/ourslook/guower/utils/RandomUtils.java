package com.ourslook.guower.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;


/**
 * 1:封装各种生成唯一性ID算法的工具类.
 * <p>
 * 2:随机数算法
 *
 * @author dazer
 * @see RandomStringUtils
 * @see org.apache.commons.lang3.RandomUtils
 */
public class RandomUtils {

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 使用SecureRandom随机生成Integer.
     */
    public static Integer randomInteger() {
        return Math.abs(random.nextInt());
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成,并以大写形式返回
     */
    public static String msUuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }


    //获取16位随机数
    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private static String string = "0123456789";

    private static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }

    /**
     * 生成随机数量,[start,end]的闭区间
     * int
     *
     * @return
     */
    public static int getBeanNumber(int start, int end) {
        int temp = (int) (start + Math.random() * (end - start + 1));
        return temp;
    }

    public static double getBeanNumers(int min, int max) {
        return Math.random() * (max - min) + min;
    }

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * @param count 位数，如果是1就产生1位的数字，如果是2就产生2位数字，依次类推
     * @return
     * @Title: getRandomNumber
     * @Description: 获取随机数
     */
    public static String getRandomNumber(int count) {
        String result = "";
        for (int i = 0; i < count; i++) {
            int rand = (int) (Math.random() * 10);
            result += rand;
        }
        return result;
    }

    /**
     * @param count 位数，如果是1就产生1位的数字，如果是2就产生2位数字，依次类推
     * @return
     * @Title: getRandomNumberNo0Stat
     * @Description: 获取随机数
     */
    public static String getRandomNumberNo0Stat(int count) {
        int topNumber = (int) (Math.random() * 9) + 1;
        String result = "";
        for (int i = 0; i < count - 1; i++) {
            int rand = (int) (Math.random() * 10);
            result += rand;
        }
        return topNumber + result;
    }

    /**
     * @param length 字符串长度
     * @return
     * @Title: getRandomLetter
     * @Description: 获取随机大写字母字母
     */
    public static String getRandomLetter(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成订单号（10位）,避免首位为0的情况；长度不要改
     *
     * @return
     */
    public static String getRandomOrderNo() {
        return getBeanNumber(1, 9) + "" + getRandomString(9);
    }


    /**
     * 生成订单号流水号（24位）,避免首位为0的情况；长度不要改
     *
     * @return
     */
    public static String getRandomOrderLuihao() {
        return getBeanNumber(1, 9) + "" + getRandomString(23);
    }

    /**
     * 生成退款单号（10位）,避免首位为0的情况 orderRefuNo; 微信退款使用
     *
     * @return
     */
    public static String getRefuOrderNo() {
        return "1331" + getRandomNumberNo0Stat(6);
    }

    /**
     * 生成充值订单号（12位）,避免首位为0的情况 RechargeOrderNo; 充值订单号
     *
     * @return
     */
    public static String getRechargeOrderNo() {
        return "9999" + getRandomNumberNo0Stat(8);
    }

    /**
     * 生成提现订单号（14位）,避免首位为0的情况 WithdrawOrderNo; 提现订单号
     *
     * @return
     */
    public static String getWithdrawOrderNo() {
        return "6666" + getRandomNumberNo0Stat(10);
    }

    /**
     * 生成流水号（30位）,格式：当天日期[14位]+序列号[3至24位]，如：201603081000001
     *
     * @return
     */
    public static String getBatchOrderNo() {
        return DateUtils.getCurrentDateSimple() + RandomUtils.getRandomNumber(5);
    }

    /**
     * 生成配送订单号（12位）;长度不要改
     *
     * @return
     */
    public static String getDeliveryOrderNo() {
        return "PS" + getRandomNumberNo0Stat(10);
    }

    /**
     * 生成类似支付宝的首付款码（30位）;长度不要改
     * <p>
     * 三位前缀+5位数字+22位混合字母
     *
     * @return
     */
    public static String getMoneyQr() {
        return Constant.RandomContant.MONEY_QR_START + getRandomNumberNo0Stat(5) + generateRandomCharAndNumber(22);
    }

    /**
     * 生成固定长度的随机字符和数字
     *
     * @param len
     * @return
     */
    public static String generateRandomCharAndNumber(Integer len) {
        StringBuffer sb = new StringBuffer();
        for (Integer i = 0; i < len; i++) {
            int intRand = (int) (Math.random() * 52);
            int numValue = (int) (Math.random() * 10);
            char base = (intRand < 26) ? 'A' : 'a';
            char c = (char) (base + intRand % 26);
            if (numValue % 2 == 0) {
                sb.append(c);
            } else {
                sb.append(numValue);
            }
        }
        return sb.toString();
    }

    /**
     * 生成一定规则的token，32位随机字符串
     * 规则1：生成的字符串中字符位置遇3的倍数时
     *
     * @return
     */
    public static String generateToken() {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 32; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        //对生成的随机字符串做处理符合一定的规则
        return "";
    }

//    public static void main(String[] args) {
//        System.out.println("RandomUtils.getRandomOrderNo() = "  + getRandomOrderNo() + "");
//    }

}
