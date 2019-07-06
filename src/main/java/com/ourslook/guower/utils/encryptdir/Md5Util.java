package com.ourslook.guower.utils.encryptdir;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author dazer
 * @date 2018/1/10 下午2:17
 */
public class Md5Util {


    /**
     * 下载文件MD5校验;系统密码md5加密；同一个字符串md5之后的值是一样的
     * 现在已经不安全，方法和 getMd5 同
     *
     * @param convertStr
     * @return
     */
    public static String getMD5String(String convertStr) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(convertStr.toString().getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    public static String getMd5(String text) {
        try {
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(text.getBytes("UTF-8"));

            byte[] bytes = md.digest();
            int j = bytes.length;
            char[] c = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte b = bytes[i];
                c[k++] = hexDigits[b >>> 4 & 0xf];
                c[k++] = hexDigits[b & 0xf];
            }

            return new String(c);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    //SHA1 加密算法
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 对密码进行md5加密,并返回密文和salt，包含在User对象中
     *
     * @param username 用户名
     * @param password 密码
     * @return 密文
     */
    public static String getPassword(String username, String password, String salt) {
        return new Md5Hash(password, username + salt, 2).toHex();
    }

    /**
     * 通过username,password,salt,校验密文是否匹配 ，校验规则其实在配置文件中，这里为了清晰，写下来
     *
     * @param username      用户名
     * @param password      原密码
     * @param salt          盐
     * @param md5cipherText 密文
     * @return boolean
     */
    public static boolean checkPassword(String username, String password, String salt, String md5cipherText) {
        String password_cipherText = new Md5Hash(password, username + salt, 2).toHex();
        return md5cipherText.equals(password_cipherText);
    }

}
