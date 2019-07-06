package com.ourslook.guower.utils.validator.constraints;

import com.ourslook.guower.utils.validator.constraints.validator.CannotContainSpacesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义注解，不能包含空格
 * @see org.hibernate.validator.constraints.NotBlank
 * @see org.hibernate.validator.constraints.Email
 * @see org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
 * @see com.ourslook.guower.utils.validator.ValidatorUtils
 * @author dazer
 * @date 2017/12/9 下午12:35
 */
@Constraint(validatedBy = {CannotContainSpacesValidator.class}) //具体的实现
@Target( { java.lang.annotation.ElementType.METHOD,
        java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface NotContainSpaces {

    String message() default "{Cannot.contain.Spaces}"; //提示信息,可以写死,可以填写国际化的key

    int length() default 5;

    //下面这两个属性必须添加
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}