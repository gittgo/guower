package com.ourslook.guower.config;

import com.ourslook.guower.utils.XaUtils;
import com.ourslook.guower.utils.dfs.DefaultDfsClientImpl;
import com.ourslook.guower.utils.dfs.FastDFSClientImpl;
import com.ourslook.guower.utils.encryptdir.AppSignature;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

/**
 * @see RedisProperties
 * @see ServerProperties
 */
@ConfigurationProperties(prefix = OurslookConfig.ROOT_CONFIG_PREFIX)
public class OurslookConfig implements Ordered , EnvironmentAware {
    /**
     * 系统内部的一些公共配置，前缀，共有配置请不要随意修改
     */
    protected static final String ROOT_CONFIG_PREFIX = "ourslook";
    /**
     * 对于api是否开启安全访问，默认false
     *
     * @see AppSignature
     */
    private boolean isSecurityAcess;
    /**
     * 是否开启redis session
     *
     * @see ShiroConfig
     * //@see ShiroCasConfiguration
     */
    private boolean redisSessionEnable;
    /**
     * 是否开启fastDfs,默认false
     *
     * @see FastDFSClientImpl
     * @see DefaultDfsClientImpl
     */
    private boolean fastdfsEnable;

    /**
     * 是否使用tomcat虚目录，如果使用jenkins自动发布，建议开启
     * 开启之后的设置：
     * 1：tomcat在要设置
     * 2：查看
     *
     * @see XaUtils#getWebUploadVirtalRootPath(HttpServletRequest, String)
     */
    private boolean tomcatVirtalPath;

    /**
     * 是否开启发送短信功能
     * 开启：发送真实短信
     * 关闭：发送短信logo日志
     */
    private boolean openShortMsg;

    /**
     * cas相关
     * casServerUrlPrefix cas服务器地址 eg:http://zjcbl.com:82/cas
     * casclientUrlPrefix  //cas client 地址 eg:http://zjcbl.com:82/safety
     * casLoginUrl
     * casLogoutUrl
     * casclientCasFilterUrlPattern  //访问 /shiro-cas
     * casclientCasService  //访问 http://localhost:8090/safety/shiro-cas
     * casclientLoginUrl  // client 登录地址 eg:http://zjcbl.com:82/cas/login?service=http://localhost:81/safety/shiro-cas
     */
    private String casclientUrlPrefix;
    private String casLoginUrl;
    private String casLogoutUrl;
    private String casclientCasFilterUrlPattern;
    private String casclientCasService;
    private String casServerUrlPrefix;
    private String casclientLoginUrl;

    //=================================================================================================================
    public boolean isSecurityAcess() {
        return isSecurityAcess;
    }

    public void setSecurityAcess(boolean securityAcess) {
        isSecurityAcess = securityAcess;
    }

    public boolean isRedisSessionEnable() {
        return redisSessionEnable;
    }

    public void setRedisSessionEnable(boolean redisSessionEnable) {
        this.redisSessionEnable = redisSessionEnable;
    }

    public boolean isFastdfsEnable() {
        return fastdfsEnable;
    }

    public void setFastdfsEnable(boolean fastdfsEnable) {
        this.fastdfsEnable = fastdfsEnable;
    }

    public boolean isTomcatVirtalPath() {
        return tomcatVirtalPath;
    }

    public void setTomcatVirtalPath(boolean tomcatVirtalPath) {
        this.tomcatVirtalPath = tomcatVirtalPath;
    }

    public boolean isOpenShortMsg() {
        return openShortMsg;
    }

    public void setOpenShortMsg(boolean openShortMsg) {
        this.openShortMsg = openShortMsg;
    }

    public String getCasclientUrlPrefix() {
        return casclientUrlPrefix;
    }

    public void setCasclientUrlPrefix(String casclientUrlPrefix) {
        this.casclientUrlPrefix = casclientUrlPrefix;
    }

    public String getCasLoginUrl() {
        return casLoginUrl;
    }

    public void setCasLoginUrl(String casLoginUrl) {
        this.casLoginUrl = casLoginUrl;
    }

    public String getCasLogoutUrl() {
        return casLogoutUrl;
    }

    public void setCasLogoutUrl(String casLogoutUrl) {
        this.casLogoutUrl = casLogoutUrl;
    }

    public String getCasclientCasFilterUrlPattern() {
        return casclientCasFilterUrlPattern;
    }

    public void setCasclientCasFilterUrlPattern(String casclientCasFilterUrlPattern) {
        this.casclientCasFilterUrlPattern = casclientCasFilterUrlPattern;
    }

    public String getCasclientCasService() {
        return casclientCasService;
    }

    public void setCasclientCasService(String casclientCasService) {
        this.casclientCasService = casclientCasService;
    }

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasclientLoginUrl() {
        return casclientLoginUrl;
    }

    public void setCasclientLoginUrl(String casclientLoginUrl) {
        this.casclientLoginUrl = casclientLoginUrl;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}
