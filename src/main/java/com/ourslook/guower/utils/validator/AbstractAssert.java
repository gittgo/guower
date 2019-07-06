package com.ourslook.guower.utils.validator;

import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.RRException;
import com.ourslook.guower.utils.validator.constraints.NotContainSpaces;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @see  Validator
 * @see  ValidatorUtils
 * @see  AbstractAssert
 * @see  NotContainSpaces
 */
public abstract class AbstractAssert {
    /**
     * @see Validator
     * @param isOk 是否成功, 这里是成功的条件
     * @param message 失败提示信息
     */
    public static void isOk(boolean isOk, String message, int code) {
        if (!isOk) {
            throw new RRException(message, code);
        }
    }

    public static void isOkUser(boolean isOk) {
        if (!isOk) {
            throw new RRException("用户信息获取失败！", Constant.Code.code_failure_token);
        }
    }

    public static void isBlank(String str, String message, int code) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message, code);
        }
    }

    public static void isNull(Object object, String message, int code) {
        if (object == null) {
            throw new RRException(message, code);
        }
    }

    //--------------------以下是简写---------------------------------


    public static void isBlank(String str, String message) {
        isBlank(str, message, Constant.Code.error);
    }

    public static void isNull(Object object, String message) {
        isNull(object, message, Constant.Code.error);
    }

    public static void isOk(boolean isOk, String message) {
        isOk(isOk, message, Constant.Code.error);
    }

}
