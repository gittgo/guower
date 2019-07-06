package com.ourslook.guower.utils.result;


import com.ourslook.guower.utils.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;


/**
 * @Title: XaResult.java
 * @Package com.web.hhrz.base.entity
 * @Description: Service返回结果统一对象
 * @author eason.zt
 * @date 2014年8月13日 下午7:46:23
 * @version V1.0
 *
 * @see R
 * @see Constant.Code
 */
@SuppressWarnings("unchecked")
public class XaResult<T> {

	@ApiModelProperty(value = "code : 返回代码，0表示OK，其它的都有对应问题")
	private int code=Constant.Code.success;

	@ApiModelProperty(value = "msg : 如果code!=0,错误信息")
	private String msg ="";

	@ApiModelProperty(value = "如果code!=0,message的补充信息")
	private Object exception;

	@ApiModelProperty(value = "code为0时返回结果集")
	private T object=(T) new Object();

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getException() {
		return exception;
	}

	public void setException(Object exception) {
		this.exception = exception;
	}

	public T getObject() {
		return object;
	}


	public XaResult<T> setObject(T object) {
		this.object = object;
		return this;
	}

	public XaResult(String errorMsg){
		this.msg = errorMsg;
		this.code = 500;
		this.object=(T) new Object();
	}

	public XaResult(String errorMsg, int code){
		this.msg = errorMsg;
		this.code = code;
		this.object=(T) new Object();
	}

	public XaResult(T object){
		this.object=object;
	}

	public XaResult(){
		this.object=(T) new Object();
	}

	public XaResult<T> success(T object){
		this.code = 0;
		this.msg = "sucess";
		this.object = object;
		return this;
	}
	public XaResult<T> error(){
		this.code = 500;
		this.msg = "未知异常，请联系管理员";
		this.object=(T) new Object();
		return this;
	}
	public XaResult<T> error(String message){
		this.code = 500;
		this.msg = message;
		this.object=(T) new Object();
		return this;
	}
	public XaResult<T> error(String message, T t){
		this.code = 500;
		this.msg = message;
		this.object= t;
		return this;
	}
	public XaResult<T> error(String message, Boolean isArray){
		this.code = 500;
		this.msg = message;
		this.object= isArray ? (T) new ArrayList<>() : (T) new Object();
		return this;
	}
}


