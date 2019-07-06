package com.ourslook.guower.utils.aop;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.service.SysLogService;
import com.ourslook.guower.utils.annotation.TestsAnno;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * mysql等数据库表情支持，切面处理类
 *
 */
@Aspect
@Component
public class TestsAspect {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysLogService sysLogService;

    // 定义Pointcut，Pointcut的名称 就是simplePointcut，此方法不能有返回值，该方法只是一个标示
    // @annotation 指定自定义注解
    // execution 指定类路径
    @Pointcut(
            //"execution(* com.ourslook.guower.service..*.*(..)) && " +
            "@annotation(com.ourslook.guower.utils.annotation.TestsAnno)"
    )
    public void emojiPointCut() {
    }

    @Around("emojiPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        TestsAnno emoji = method.getAnnotation(TestsAnno.class);
        if(emoji != null){
            //注解上的描述
            emoji.value();
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        Object resultData = null;

        try {
            //获取返回值
            Class<?> obj=Class.forName(className);
            Object object = obj.newInstance();
            resultData = method.invoke(object);

            //请求的参数
            Object[] args = joinPoint.getArgs();
            String params = JSON.toJSONString(args[0]);

            logger.info("Emoji支持");

        } catch (Exception throwable) {
            throwable.printStackTrace();
        }

        joinPoint.proceed();

        return resultData + ":dazer";
    }

}
