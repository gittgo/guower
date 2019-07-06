package com.ourslook.guower.utils.spring;

import org.springframework.context.ApplicationContext;

/**
 * 保存ApplicationContext的单例.
 * @author dazer lingo
 */
public class SpringContextHolder {
    /** instance. */
    private static SpringContextHolder instance = new SpringContextHolder();

    /** ApplicationContext. */
    private ApplicationContext applicationContext;

    /**
     * get ApplicationContext.
     *
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * set ApplicationContext.
     *
     * @param applicationContext
     *            ApplicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * get instance.
     *
     * @return SpringContextHolder
     */
    public static SpringContextHolder getInstance() {
        return instance;
    }
}
