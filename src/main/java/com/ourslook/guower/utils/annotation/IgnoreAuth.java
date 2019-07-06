package com.ourslook.guower.utils.annotation;

import com.ourslook.guower.utils.interceptor.AuthorizationInterceptor;

import java.lang.annotation.*;

/**
 * 忽略Token验证
 *
 * @see AuthorizationInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

}
