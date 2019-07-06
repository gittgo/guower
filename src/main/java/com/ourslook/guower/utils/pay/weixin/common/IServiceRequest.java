package com.ourslook.guower.utils.pay.weixin.common;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * User: rizenguo
 * Date: 2014/12/10
 * Time: 15:16
 * 这里定义服务层需要请求器标准接口
 */
@SuppressWarnings("all")
public interface IServiceRequest {

    /**
     * Service依赖的底层https请求器必须实现这么一个接口
     */
    public String sendPost(String api_url, Object xmlObj) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException;

    /**
     * Service依赖的底层https请求器必须实现这么一个接口
     */
    public String sendPostNoSSL(String api_url, Object xmlObj) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException;

    /**
     * 不做处理，直接发送
     */
    public String send(String api_url, Object data) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException;
}
