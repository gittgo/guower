package com.ourslook.guower.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Created by dazer on 2017/9/14.
 * @author dazer lingo
 * 在ApplicationContext环境外获取bean的工具类.
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {

    /**
     * 向ApplicationContextHolder里设置ApplicationContext.
     *
     * @param applicationContext
     *            applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextHolder.getInstance().setApplicationContext(
                applicationContext);
    }

    /**
     * 获得ApplicationContext.
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return SpringContextHolder.getInstance().getApplicationContext();
    }

    /**
     * 根据class获得bean.
     *
     * @param clz
     *            Class
     * @return T
     */
    public static <T> T getBean(Class<T> clz) {
        return SpringContextHolder.getInstance().getApplicationContext()
                .getBean(clz);
    }

    /**
     * 根据id获得bean.
     *
     * @param id
     *            String
     * @return T
     */
    public static <T> T getBean(String id) {
        return (T) SpringContextHolder.getInstance()
                .getApplicationContext().getBean(id);
    }
}
