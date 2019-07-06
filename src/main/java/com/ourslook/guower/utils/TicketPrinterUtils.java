package com.ourslook.guower.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * 小票打印机工具类
 * Created by ljt on 2018/5/24
 * @see Constant.TicketPrinterConfig 小票机静态参数
 */
public class TicketPrinterUtils {
    /**
     * 组装请求Url
     * @param action 请求地址（设备绑定请求地址为 /home/userbind）
     */
    public static String getUrl(String action) {
        return String.format("%s%s%s", Constant.TicketPrinterConfig.baseUrl, action, createParams());
    }

    /**
     * 创建通用参数
     */
    public static String createParams() {
        //获取随机数字字符串
        String nonce = getNonce();
        //获取时间戳
        String timestamp = getTimestamp();
        //加密字符串(appSecret对对机开发平台获得)
        String signStr = SignatureString(Constant.TicketPrinterConfig.appSecret, timestamp, nonce);
        //appid对对机开发平台申请的appKey
        return String.format("?appid=%s&nonce=%s&timestamp=%s&signature=%s", Constant.TicketPrinterConfig.appId, nonce, timestamp,
                signStr);
    }

    /**
     * 字符串转Base64
     */
    public static String StringToBase64(String data) {
        try {
            return Base64.getEncoder().encodeToString(data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加密字符串
     */
    public static String SignatureString(String appSecret, String timestamp, String nonce) {
        String[] arrTmp = { appSecret, timestamp, nonce };
        Arrays.sort(arrTmp);
        String tmpStr = String.join("", arrTmp);
        try {
            tmpStr = SHA1(tmpStr).toLowerCase();
        } catch (DigestException e) {
            e.printStackTrace();
        }
        return tmpStr;
    }

    /**
     * SHA1 安全加密算法
     *
     * @data 要加密的字符串
     * @return
     * @throws DigestException
     */
    public static String SHA1(String data) throws DigestException {
        try {
            // 指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(data.getBytes());
            // 获取字节数组
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
            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigestException("签名错误！");
        }
    }

    /**
     * 获取随机数字字符串
     */
    public static String getNonce() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000000));
    }

    /**
     * 获取时间戳
     */
    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 发送post请求
     * @param url 请求参数
     * @param data JSONObject
     *
     */
    public static String sendPost(String url, String data) throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response = "";
        try {
            URL httpUrl = null; // HTTP URL类 用这个类来创建连接
            // 创建URL
            httpUrl = new URL(url);
            // 建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);// 设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            // POST请求
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data);
            out.flush();
            // 读取响应
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response += lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }
}