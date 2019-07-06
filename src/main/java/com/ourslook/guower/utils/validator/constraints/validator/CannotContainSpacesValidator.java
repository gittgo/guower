package com.ourslook.guower.utils.validator.constraints.validator;

import com.ourslook.guower.utils.validator.constraints.NotContainSpaces;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author dazer
 * @date 2017/12/9 下午12:41
 * @see org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
 */
public class CannotContainSpacesValidator implements ConstraintValidator<NotContainSpaces, String> {

    /**
     * 初始参数,获取注解中length的值
     */
    @Override
    public void initialize(NotContainSpaces arg0) {
        int length = arg0.length();
    }

    /**
     * 返回true:验证成功； false：验证失败
     * <code>
     *  constraintValidatorContext.disableDefaultConstraintViolation();//禁用默认的message的值
     *  //重新添加错误提示语句
     *  constraintValidatorContext
     *  .buildConstraintViolationWithTemplate("字符串不能为空").addConstraintViolation();
     * </code>
     */
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return true;
        }
        return !StringUtils.containsWhitespace(str);
    }

}