package com.ourslook.guower.utils.annotation;

import java.lang.annotation.*;

/**
 * 测试注解
 *
 * 要在接口和实现类上面都要加上注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestsAnno {
    String value() default "";
}
