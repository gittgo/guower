package com.ourslook.guower.utils.aop;

import com.alibaba.fastjson.JSON;
import com.ourslook.guower.config.WebMvcConfig;
import com.ourslook.guower.entity.SysLogEntity;
import com.ourslook.guower.service.SysLogService;
import com.ourslook.guower.utils.ServletUtils;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.UrlEncode;
import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.annotation.LoggSys;
import jodd.io.StringOutputStream;
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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;


/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class LoggSysAspect {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final int PARAMS_MAX = 20000;

    @Autowired
    private SysLogService sysLogService;

    // 定义Pointcut，Pointcut的名称 就是simplePointcut，此方法不能有返回值，该方法只是一个标示
    // @annotation 指定自定义注解
    @Pointcut("@annotation(com.ourslook.guower.utils.annotation.LoggSys)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    @SuppressWarnings("all")
    public void saveSysLog(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysLogEntity sysLog = new SysLogEntity();
            LoggSys syslog = method.getAnnotation(LoggSys.class);
            if (syslog != null) {
                //注解上的描述
                sysLog.setOperation(syslog.value());
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

                            /**
                             * 这里不能使用 IOUtils.toString， 这里会关闭掉流，因此进行copy一份
                             *  方法1： String params = IOUtils.toString(inputStream, "utf-8");
                             */

                            /**
                             * 方法二
                             *
                             * StringOutputStream out = new StringOutputStream();
                             * IOUtils.copy(inputStream, out);
                             * String params =  out.toString();
                             */

                            /**
                             * 方法三
                             * @see WebMvcConfig#repeatlyReadFilterRegistration()
                             */
                            InputStream inStream = request.getInputStream();
                            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while ((len = inStream.read(buffer)) != -1) {
                                outSteam.write(buffer, 0, len);
                            }
                            outSteam.close();
                            //拦截器这里不能关闭流;否则controller 里面用不了.
                            //inStream.close();
                            String params = new String(outSteam.toByteArray(),"utf-8");


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
            //设置请求url
            sysLog.setUrl(request.getRequestURI());
            //设置IP地址
            sysLog.setIp(XaUtils.getClientIpAddress2(request));
            //设置设备信息
            sysLog.setDeviceRemark(XaUtils.getBroserDeviceRamark(request));
            //设置源类型为0
            sysLog.setSourceType(0);

            //用户名
            if (ShiroUtils.getUserEntity() != null) {
                String username = ShiroUtils.getUserEntity().getUsername();
                sysLog.setUsername(username);
            }

            sysLog.setCreateDate(new Date());

            //处理请求参数有多余双引号的问题
            if (StringUtils.isNotBlank(sysLog.getParams())) {
                if (sysLog.getParams().startsWith("\"")) {
                    sysLog.setParams(sysLog.getParams().substring(1));
                }
                if (sysLog.getParams().endsWith("\"")) {
                    sysLog.setParams(sysLog.getParams().substring(0, sysLog.getParams().length() - 1));
                }
            }

            //保存系统日志
            sysLogService.save(sysLog);

            logger.info("保存系统日志:" + sysLog.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
