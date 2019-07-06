package com.ourslook.guower.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class ImageToBase64 {

	/**
	 * 图片转base64
	 * 
	 * @param imgFilePath
	 *            图片路径
	 * @return
	 */
	public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * base64 转图片
	 * 
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, String imgFilePath) {
		// 对字节数组字符串进行Base64解码并生成图片
		// 图像数据为空
		boolean isok = false;
		if (StringUtils.isNotBlank(imgStr)) {
			try {
			 
				// Base64解码
				byte[] bytes = Base64.decodeBase64(imgStr);
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] < 0) {// 调整异常数据
						bytes[i] += 256;
					}
				}
				// 生成jpeg图片
				OutputStream out = new FileOutputStream(imgFilePath);
				out.write(bytes);
				out.flush();
				out.close();
				isok = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
		return isok;
	}
}
