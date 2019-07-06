package com.ourslook.guower.utils;

import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 * 
 */
public class ShiroUtils {

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * SpringBoot集成shiro-redis遇到的问题 报强制转换错误
     * java.lang.ClassCastException: com.ourslook.guower.entity.SysUserEntity cannot be cast to com.ourslook.guower.entity.SysUserEntity
     * {@link BeanMapper}
     * {@link ReflectUtils}
     * 使用上面两种方式都失败，还是报错，类型转换异常，只能手工进行处理
	 * @return
	 */
	public static SysUserEntity getUserEntity() {
		Object subject =  SecurityUtils.getSubject().getPrincipal();
		SysUserEntity sysUserEntity = (SysUserEntity)subject;
        return sysUserEntity;
	}

	public static Long getUserId() {
		return getUserEntity().getUserId();
	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	
	public static String getKaptcha(String key) {
		Object kaptcha = getSessionAttribute(key);
		if(kaptcha == null){
			throw new RRException("验证码已失效");
		}
		getSession().removeAttribute(key);
		return kaptcha.toString();
	}

}
