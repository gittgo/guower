package com.ourslook.guower.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.sql.Clob;
import java.util.Date;

/**
 *  sql查询数据转换
 * @author dazer
 * @date 2017/12/4 下午2:10
 */ 
public class SqlResultUtil {

	public static int paraseObjToInt(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof BigInteger) {
			BigInteger bigInteger = (BigInteger) obj;
			return bigInteger.intValue();
		}
		return NumberUtils.toInt(obj+"",0);
	}

	public static long paraseObjToLong(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof BigInteger) {
			BigInteger bigInteger = (BigInteger) obj;
			return bigInteger.longValue();
		}
		return NumberUtils.toLong(obj+"",0);
	}

	public static float paraseObjToFloat(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof BigInteger) {
			BigInteger bigInteger = (BigInteger) obj;
			return bigInteger.longValue();
		}
		return NumberUtils.toFloat(obj+"",0);
	}

	public static double paraseObjToDouble(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof BigInteger) {
			BigInteger bigInteger = (BigInteger) obj;
			return bigInteger.longValue();
		}
		return NumberUtils.toDouble(obj+"",0);
	}

	public static String paraseObjToString(Object obj){
		if (obj == null) {
			return "";
		}
		if (obj instanceof BigInteger) {
			BigInteger bigInteger = (BigInteger) obj;
			return bigInteger.toString();
		}
		return String.valueOf(obj);
	}

	public static Boolean paraseObjToBool(Object obj){
		if(obj != null){
			try{
				return Boolean.parseBoolean(paraseObjToString(obj));
			}
			catch(Exception e){
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	public static String paraseClobToString(Clob clob){
		String reString = "";
		// 得到流
		try {
			java.io.Reader is = clob.getCharacterStream();
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
			reString = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reString;
	}

	public static Date paraseDateLongToDate(Object o){
		if(o != null){
			try{
				return new Date(paraseObjToLong(o) * 1000);
			}
			catch(Exception e){
				return null;
			}
		}
		return null;
	}

	public static Date paraseDateStringToDate(Object o){
		if(o != null){
			try{
				if(paraseObjToString(o).length() >= 19){
					return DateUtils.parseDateTime(paraseObjToString(o).substring(0,19));
				}
				else if(paraseObjToString(o).length() >= 10){
					return DateUtils.parseDateTime(paraseObjToString(o).substring(0,10));
				}
			}
			catch(Exception e){
				return null;
			}
		}
		return null;
	}

	public static String  paraseDateStringToString(Object o){
		if(o != null){
			try{
				if(paraseObjToString(o).length() >= 19){
					return paraseObjToString(o).substring(0,19);
				}
				else if(paraseObjToString(o).length() >= 10){
					return paraseObjToString(o).substring(0,10);
				}
			}
			catch(Exception e){
				return null;
			}
		}
		return null;
	}



}
