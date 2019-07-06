package com.ourslook.guower.utils.aop;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.entity.SysLogEntity;
import com.ourslook.guower.service.SysLogService;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.UrlEncode;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.LoggApi;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;


/**
 * 接口日志，切面处理类
 */
@Aspect
@Component
public class LoggApiAspect {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final int PARAMS_MAX = 20000;

    @Autowired
    private SysLogService sysLogService;

    // 定义Pointcut，Pointcut的名称 就是simplePointcut，此方法不能有返回值，该方法只是一个标示
    // @annotation 指定自定义注解
    @Pointcut("@annotation(com.ourslook.guower.utils.annotation.LoggApi)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    @SuppressWarnings("unChecked")
    public void saveSysLog(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysLogEntity sysLog = new SysLogEntity();
            LoggApi loggApi = method.getAnnotation(LoggApi.class);
            if (loggApi != null) {
                //注解上的描述
                sysLog.setOperation(loggApi.value());
            }

            //请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            sysLog.setMethod(className + "." + methodName + "()");

            //请求的参数
            Object[] args = joinPoint.getArgs();

            HttpServletRequest request = null;
            for (int i = 0; args != null && i < args.length; ++i) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    break;
                }
            }

            //封装参数
            if (args != null) {
                if (args.length > 0) {
                    if (request == null) {
                        try {
                            String params = JSON.toJSONString(args[0]);
                            sysLog.setParams(params.length() > PARAMS_MAX ? params.substring(PARAMS_MAX) : params);
                        } catch (Exception e) {
                            e.printStackTrace();
                            String params = args[0] + "";
                            sysLog.setParams(params.length() > PARAMS_MAX ? params.substring(PARAMS_MAX) : params);
                        }
                    } else {
                        try {
                            InputStream inputStream = request.getInputStream();
                            String params = IOUtils.toString(inputStream, "utf-8");
                            sysLog.setParams(params.length() > PARAMS_MAX ? params.substring(PARAMS_MAX) : params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (StringUtils.isBlank(sysLog.getParams())) {
                            try {
                                Map requestParams = request.getParameterMap();
                                String params = UrlEncode.getUrlParamsByMap(requestParams, true);
                                sysLog.setParams(params.length() > PARAMS_MAX ? params.substring(PARAMS_MAX) : params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            //获取request
            request = request == null ? ServletUtils.getHttpServletRequest() : request;
            //获取请求url
            sysLog.setUrl(request.getRequestURI());
            //设置IP地址
            sysLog.setIp(XaUtils.getClientIpAddress2(request));
            //设置设备信息
            sysLog.setDeviceRemark(XaUtils.getBroserDeviceRamark(request));
            sysLog.setSourceType(1);
            sysLog.setUserAgent(request.getHeader("User-Agent"));
            //设置设备系统版本号以及设备型号
            sysLog.setDeviceInfo(XaUtils.getMobileDeviceInfo(request.getHeader("User-Agent")));

            //用户名
            if (ShiroUtils.getUserEntity() != null) {
                String username = ShiroUtils.getUserEntity().getUsername();
                sysLog.setUsername(username);
            }

            sysLog.setCreateDate(new Date());
            //保存系统日志
            sysLogService.save(sysLog);

            logger.info("保存系统日志:" + sysLog.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
