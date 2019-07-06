package com.ourslook.guower.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Objects;

/**
 * 拼音工具类
 *
 * @author zhanglin
 *         2014-01-12 12:11:01
 */
public class PinyinUtil {

    /**
     * 将汉字转换为全拼
     * 中国富强   zhongguofuqiang
     * 中国 富强  hongguo fuqiang
     * china    china
     * @param src
     * @return String
     */
    public static String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        // System.out.println(t1.length);  
        String[] t2 = new String[t1.length];
        // System.out.println(t2.length);  
        // 设置汉字拼音输出的格式  
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符  
                // System.out.println(t1[i]);  
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    // 将汉字的几种全拼都存到t2数组中
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    // 取出该汉字全拼的第一种读音并连接到字符串t4后
                    t4 += t2[0];
                } else {
                    // 如果不是汉字字符，直接取出字符并连接到字符串t4后  
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t4;
    }

    /**
     * 提取每个汉字的首字母
     * <p>
     * 中国富强     -->zgfq
     * china       -->china
     * china japan --> china japan
     *
     * @param str
     * @return String
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 提取字符（中文或者英文）的首字母
     * china japan -->c
     * 中国 富强    ——>z
     * http://blog.csdn.net/zhongweijian/article/details/8064676
     * @param word 不能为空
     * @return String
     * @author duandazhi
     * @date 2017/1/13 下午12:48
     */
    public static char getPinYinFirstChar(String word) throws BadHanyuPinyinOutputFormatCombination {
        Objects.requireNonNull(word);
        char chineseCharacter = word.charAt(0);
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        // 输出的声调为数字：第一声为1，第二声为2，第三声为3，第四声为4 如：lu:4
        outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        // 输出拼音不带声调 如：lu:
        //outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 输出声调在拼音字母上 如：lǜ
        //outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        //ǜ的输出格式设置  'ü' 输出为 "u:"
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        //ǜ的输出格式设置  'ü' 输出为 "ü" in Unicode form
        //outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        //ǜ的输出格式设置  'ü' 输出为 "v"
        //outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        //输出拼音为大写
        outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        //输出拼音为小写
        //outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //汉字拼音
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chineseCharacter, outputFormat);
        if (pinyinArray == null) {
            return chineseCharacter;
        }
        //多音字输出，会返回多音字的格式
        for (String str : pinyinArray) {
            if (str != null && str.length() > 0) {
                return  str.toUpperCase().charAt(0);
            }
        }
        return chineseCharacter;
    }

    /**
     * 将字符串转换成ASCII码
     *
     * @param cnStr
     * @return String
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        // 将字符串转换成字节序列  
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // System.out.println(Integer.toHexString(bGBK[i] & 0xff));  
            // 将每个字符转换成ASCII码  
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff) + " ");
        }
        return strBuf.toString();
    }

//    public static void main(String[] args) throws Exception {
//        System.out.println(getPinYin("中国 富强"));//zgfq
//        System.out.println(getPinYinHeadChar("china"));//china
//        System.out.println(getPinYinHeadChar("china japan"));//china japan
//        System.out.println(getPinYinFirstChar("中国 富强"));//china japan
//    }
}
