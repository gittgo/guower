package com.ourslook.guower.utils.result;

import com.ourslook.guower.utils.XaUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by duandazhi on 2017/1/9.
 * 配合nice-validator remove 验证 result
 * https://github.com/niceue/nice-validator
 * https://niceue.com/validator/
 * https://validator.niceue.com/docs/
 *
 * 客户端 远程校验 https://niceue.com/validator/demo/remote.php
 * 中间必须有空格
 * remote[post:cms/sysArea/checkArea, roleName]
 * remote[check/username.php, field1, field2]
 * @author dazer
 */
public class ValidateResult<K,V>  extends HashMap<K,V> implements Cloneable, Serializable {
    public static final String OK = "ok";
    public static final String ERROR = "error";

    public static final String OK_MSG = "可以使用";
    public static final String ERROR_MSG = "已存在";

    /**
     * {"OK":"可以使用"}
     */
    public static ValidateResult ok() {
        return ok(null);
    }

    public static ValidateResult ok(String okMsg) {
        ValidateResult<String,String> result = new ValidateResult<>();
        result.put(OK, XaUtils.isEmpty(okMsg) ? OK_MSG : okMsg);
        return result;
    }

    /**
     * {"ERROR":"已存在"}
     */
    public static ValidateResult error() {
        return error(null);
    }

    public static ValidateResult error(String errorMsg) {
        ValidateResult<String,String> result = new ValidateResult<>();
        result.put(ERROR, XaUtils.isEmpty(errorMsg) ? ERROR_MSG : errorMsg);
        return result;
    }
}
