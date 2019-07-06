package com.ourslook.guower.utils.result;

import com.ourslook.guower.utils.PageUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * @see XaResult
 * @see com.ourslook.guower.utils.Constant.Code
 */
@ApiModel(description = "R接口结果集")
public class R<T> extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "code : 返回代码，0表示OK，其它的都有对应问题")
    private int code = 0;

    @ApiModelProperty(value = "msg : 如果code!=1,错误信息")
    private String msg = "未知异常，请联系管理员";

    @ApiModelProperty(value = "如果code!=0,message的补充信息")
    private Object exception;

    @SuppressWarnings("unchecked")
    @ApiModelProperty(value = "code为1时返回结果集")
    private T data = (T) new Object();

    public R() {
        setCode(0);
    }

    public static <T> R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static <T> R error(String msg) {
        return error(500, msg);
    }

    public static <T> R error(Exception exception) {
        R r = error();
        r.setException(exception);
        return r;
    }

    public static <T> R error(int code, String msg) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> R ok(String msg) {
        R<T> r = new R<T>();
        r.setMsg(msg);
        return r;
    }

    public static <T> R ok(Map<String, Object> map) {
        R<T> r = new R<T>();
        r.putAll(map);
        return r;
    }

    public static <T> R ok() {
        return new R<T>();
    }

    /**
     * 和系统自带的 put方法一样，只是这里返回了map自己
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public R<T> putObj(String key, Object value) {
        switch (key) {
            case "code":
                this.put("code", (int)value);
                break;
            case "msg":
                this.put("msg", (String) value);
                break;
            case "data":
                this.put("data", (T) value);
                break;
            case "page":
                if (value instanceof PageUtils) {
                    PageUtils pageUtils = (PageUtils) value;
                    //数据专门为了layerui
                    super.put("layuiData", pageUtils.getList());
                    super.put("layuiCount", pageUtils.getTotalCount());
                }
                break;
            default:
        }
        super.put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        this.put("code", code);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        this.put("msg", msg);
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
        this.put("exception", exception);
    }

    public T getData() {
        return data;
    }

    public R<T> setData(T data) {
        this.data = data;
        this.put("data", data);
        return this;
    }
}
