package com.ourslook.guower.utils.pay.unionpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

/**
 * https://open.unionpay.com/ajweb/help/file/techFile?productId=3
 * https://open.unionpay.com/ajweb/product
 * https://merchant.unionpay.com/join/
 * <p>
 * 原文件：BaseConfig
 * 原文件：DemoBase
 *
 * 全程参照：可以参见 手机控件支付开发包（安卓版,里面有app和后台的代码;
 */
@SuppressWarnings("all")
public class UnionpayConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全渠道固定值
     *
     * @see SDKConfig#loadPropertiesFromPath(String)  搜 prod
     */
    public static final String UNIONPAY_VERSION = SDKConfig.getConfig().getVersion();
    public static final String UNIONPAY_ENCODING = "UTF-8";
    public static final String UNIONPAY_MERID = UnionpayConfig.merId;
    /**
     * 证书使用的环境，取值 dev 或者 prod
     */
    public static final String UNIONPAY_ENVIORMENT = "dev";
    public static final String UNIONPAY_CERT_PATH = "classpath:config/unionpay/";

    /**
     * 商户号
     */
    //public static final String merId = "310420173990077";   //正式环境账号
    //public static final String merId = "777290058110097";    //测试环境账号
    public static final String merId = "777290058110048";    //测试环境账号;默认商户号仅作为联调测试使用，正式上线还请使用正式申请的商户号

    /**
     * 加载银联相关的配置文件
     * @param request
     *
     * @see SpringBootMyApplication#loadUnionpay()
     * @see com.ourslook.guower.utils.pay.unionpay.SDKConfig#loadPropertiesFromPath(String)
     */
    public static void loadPropertiesByRequest() throws FileNotFoundException {
        /**
         * @see CertUtil#init()  查看时是否初始化成功
         * @see CertUtil#getSignCertId
         */
        /**
         * 请求银联接入地址，获取证书文件，证书路径等相关参数初始化到SDKConfig类中
         * 在java main 方式运行时必须每次都执行加载
         * 如果是在web应用开发里,这个方法可使用监听的方式写入缓存,无须在这出现
         */
        //这里已经将加载属性文件的方法挪到了web/AutoLoadServlet.java中
        //SDKConfig.getConfig().loadPropertiesFromSrc(); //从classpath加载acp_sdk.properties文件
        //从classpath加载acp_sdk.properties文件
        if (SDKConfig.getConfig().getProperties() == null) {
            //String base_url = request.getSession().getServletContext().getRealPath("/") + "unionpay" + File.separator + "dev";

            File certFile = ResourceUtils.getFile(UNIONPAY_CERT_PATH + UNIONPAY_ENVIORMENT);

            SDKConfig.getConfig().loadPropertiesFromPath(certFile.getAbsolutePath());
        }
    }


    /**
     * 获取请求参数中所有的信息
     *
     * @see com.ourslook.guower.utils.UrlEncode
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

    /**
     * 组装请求，返回报文字符串用于显示
     *
     * @param data
     * @return
     */
    public static String genHtmlResult(Map<String, String> data) {

        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if ("respCode".equals(key)) {
                sf.append("<b>" + key + SDKConstants.EQUAL + value + "</br></b>");
            } else {
                sf.append(key + SDKConstants.EQUAL + value + "</br>");
            }
        }
        return sf.toString();
    }

}
