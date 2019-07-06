package com.ourslook.guower.task;

import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 * <p>
 * <p>
 * sysTask为spring bean的名称
 * <p>
 * XA_SCHEDULE_JOB
 * XA_SCHEDULE_JOB_LOG
 * QRTZ_TRIGGERS
 * QRTZ_JOB_DETAILS
 * <p>
 * 配置查看:
 *
 * @see com.ourslook.guower.config.QuartzConfig
 */
@Component("sysTask")
public class SysTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;

    /**
     * 系统测试方法1
     */
    @Deprecated
    public void test(String params) {
        logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SysUserEntity user = sysUserService.queryObject(1L);
        logger.debug(ToStringBuilder.reflectionToString(user));
    }

    /**
     * 系统测试方法2
     */
    @Deprecated()
    public void test2() {
        logger.info("我是不带参数的test2方法，正在被执行");
    }
}
