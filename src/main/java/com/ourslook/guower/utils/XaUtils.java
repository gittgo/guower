package com.ourslook.guower.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zl on 2016/10/13
 * 西安通用工具类
 *
 * @author dazer
 * @see StringUtilss
 * @see StringUtils
 * @see org.apache.commons.lang3.StringEscapeUtils
 * @see org.apache.commons.lang3.RandomStringUtils
 * @see UrlEncode
 * @see ServletUtils
 * @see PinyinUtil
 */
public class XaUtils {

    private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
    private static final double MAX_VALUE = 9999999999999.99D;


    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object pObj) {
        if (pObj == null) {
            return true;
        }
        if (pObj == "") {
            return true;
        }
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return true;
            }
        } else if (isArray(pObj)) {
            if (Array.getLength(pObj) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Object pObj) {
        if (pObj == null) {
            return false;
        }
        if (pObj == "") {
            return false;
        }
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return false;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return false;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return false;
            }
        } else if (isArray(pObj)) {
            if (Array.getLength(pObj) == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isArray(Object pObj) {
        if (pObj == null) {
            return false;
        }
        if (pObj.getClass().isArray()) {
            return true;
        }
        return false;
    }


    /**
     * 【tomcat】获取访问者真实IP
     * http://www.cnblogs.com/puyangsky/p/5555859.html
     */
    @Deprecated
    public static String getClientIpAddress(@NotNull HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取访问者真实IP
     * 经过nginx 代理也可以正常获取ip
     * <p>
     * request.getLocalAddr(),往往有问题
     *
     * @param request
     * @return
     */
    public static String getClientIpAddress2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String getCmsLoginedUserId() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if (XaUtils.isEmpty(session)) {
            return null;
        }
        String userId = (String) session.getAttribute("loginUserId");
        return XaUtils.isEmpty(userId) ? null : userId;
    }


    /**
     * (获取登录用户名)
     */
    public static String getCmsLoinedUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if (XaUtils.isEmpty(session)) {
            return null;
        }
        String userName = (String) session.getAttribute("loginUserName");
        return XaUtils.isEmpty(userName) ? null : userName;
    }

    public static String getCmsLoginedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = session.getAttribute("loginUserId") + "";
        if (isNotEmpty(userId)) {
            return userId;
        }
        return "";
    }

    /**
     * 往文件写数据
     */
    public static void WriteFile(String content, String filePath, String fileName) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File f1 = new File(filePath + File.separator + fileName);
        FileUtils.writeStringToFile(f1, content, "utf-8");
    }

    public static List<Long> getIds(String[] arr) {
        if (isEmpty(arr) || arr.length == 0) {
            return new ArrayList<>();
        }
        List<Long> idsList = new ArrayList<>();
        for (String str : arr) {
            if (isNotEmpty(str)) {
                idsList.add(Long.valueOf(str));
            }
        }
        return idsList;
    }

    /**
     * Boolean 使用前要进行判断,否则空指针异常
     * 可以参加 {@link org.apache.commons.lang3.BooleanUtils}
     * 页面返回1,2,3
     *
     * @param bool
     * @return
     */
    public static Integer getBool(Integer bool) {
        if (bool == 1) {
            return 1;
        }
        if (bool == 2) {
            return 2;
        }
        if (bool == 3) {
            return 3;
        }
        return null;
    }

    /**
     * 是否正确答案；true yes 1 都算是正确答案
     */
    public static boolean questionAnswerToBool(String answer) {
        if (answer == null) {
            return false;
        }
        if ("TRUE".equalsIgnoreCase(answer)
                || "YES".equalsIgnoreCase(answer)
                || "1".equalsIgnoreCase(answer)) {
            return true;
        }
        return false;
    }

    public static String getNutNullStr(Object o, String defaultValue) {
        if (isEmpty(o)) {
            return defaultValue;
        }
        return o.toString();
    }

    public static Long getNutNullLong(Object o, Long defaultValue) {
        if (isEmpty(o)) {
            return defaultValue;
        }
        return NumberUtils.toLong(o.toString(), defaultValue);
    }

    public static double getNutNullDouble(Object o, double defaultValue) {
        if (isEmpty(o)) {
            return defaultValue;
        }
        return NumberUtils.toDouble(o.toString(), defaultValue);
    }

    public static String getNutNullStr(Object o) {
        return getNutNullStr(o, "");
    }


    //获取子串,str可以是null
    public static String getSubStr(String str, int maxLength) {
        return isEmpty(str) ? "" : str.length() > maxLength ? str.substring(0, maxLength) : str;
    }

    public static boolean containsAny(String str, String[] searchChars) {
        if (isEmpty(str) || ArrayUtils.isEmpty(searchChars)) {
            return false;
        }
        for (String chars : searchChars) {
            if (StringUtils.equalsIgnoreCase(str, chars)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数据格式化.
     *
     * @param pattern the pattern
     *                //@param i
     *                the i
     * @return the string
     */
    public static String codeFormat(String pattern, Object value) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

    /**
     * 格式化金额.
     *
     * @param value the value
     * @return the string
     */
    public static String formatCurrency2String(Long value) {
        if (value == null || "0".equals(String.valueOf(value))) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value / 100.00);
    }

    /**
     * 格式化金额.
     *
     * @param priceFormat the price format
     * @return the long
     */
    public static Long formatCurrency2Long(String priceFormat) {
        BigDecimal bg = new BigDecimal(priceFormat);
        Long price = bg.multiply(new BigDecimal(100)).longValue();
        return price;
    }

    /**
     * 去除html标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签
        return htmlStr.trim(); //返回文本字符串
    }


    /**
     * 将xml字符串转化为实体对象
     *
     * @param <T>
     * @param xml
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2bean(String xml, Class<T> clazz) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return t;
    }


    /**
     * 数字转成字符，如1-->A;2--->B...
     *
     * @param num
     * @return
     */
    public static char numToChar(int num) {
        return (char) (64 + num);
    }

    /**
     * 获取字符的键盘值
     *
     * @param s
     * @return
     */
    private static Integer getKeyValue(char s) {
        Integer result = 0;
        switch (s) {
            case 'A':
                result = 65;
                break;
            case 'B':
                result = 66;
                break;
            case 'C':
                result = 67;
                break;
            case 'D':
                result = 68;
                break;
            case 'E':
                result = 69;
                break;
            case 'F':
                result = 70;
                break;
            case 'G':
                result = 71;
                break;
            case 'H':
                result = 72;
                break;
            case 'I':
                result = 73;
                break;
            case 'J':
                result = 74;
                break;
            case 'K':
                result = 75;
                break;
            case 'L':
                result = 76;
                break;
            case 'M':
                result = 77;
                break;
            case 'N':
                result = 78;
                break;
            case 'O':
                result = 79;
                break;
            case 'P':
                result = 80;
                break;
            case 'Q':
                result = 81;
                break;
            case 'R':
                result = 82;
                break;
            case 'S':
                result = 83;
                break;
            case 'T':
                result = 84;
                break;
            case 'U':
                result = 85;
                break;
            case 'V':
                result = 86;
                break;
            case 'W':
                result = 87;
                break;
            case 'X':
                result = 88;
                break;
            case 'Y':
                result = 89;
                break;
            case 'Z':
                result = 90;
                break;
            case 'a':
                result = 97;
                break;
            case 'b':
                result = 98;
                break;
            case 'c':
                result = 99;
                break;
            case 'd':
                result = 100;
                break;
            case 'e':
                result = 101;
                break;
            case 'f':
                result = 102;
                break;
            case 'g':
                result = 103;
                break;
            case 'h':
                result = 104;
                break;
            case 'i':
                result = 105;
                break;
            case 'j':
                result = 106;
                break;
            case 'k':
                result = 107;
                break;
            case 'l':
                result = 108;
                break;
            case 'm':
                result = 109;
                break;
            case 'n':
                result = 110;
                break;
            case 'o':
                result = 111;
                break;
            case 'p':
                result = 112;
                break;
            case 'q':
                result = 113;
                break;
            case 'r':
                result = 114;
                break;
            case 's':
                result = 115;
                break;
            case 't':
                result = 116;
                break;
            case 'u':
                result = 117;
                break;
            case 'v':
                result = 118;
                break;
            case 'w':
                result = 119;
                break;
            case 'x':
                result = 120;
                break;
            case 'y':
                result = 121;
                break;
            case 'z':
                result = 122;
                break;
            case '0':
                result = 48;
                break;
            case '1':
                result = 49;
                break;
            case '2':
                result = 50;
                break;
            case '3':
                result = 51;
                break;
            case '4':
                result = 52;
                break;
            case '5':
                result = 53;
                break;
            case '6':
                result = 54;
                break;
            case '7':
                result = 55;
                break;
            case '8':
                result = 56;
                break;
            case '9':
                result = 57;
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * @param str            待分割的字符串;可以为空
     * @param separatorChars 分割符号;可以为空;默认英文逗号,
     * @return
     * @author duandazhi
     * @date 2016/12/6 下午3:03
     */
    public static Long[] splitLong(String str, String separatorChars) {
        if (isEmpty(str)) {
            return null;
        }
        List<String> list = Arrays.asList(StringUtils.split(str,
                isEmpty(separatorChars) ? Constant.StringConstant.DOT.getValue() : separatorChars));
        Long[] longs = new Long[list.size()];
        int kk = 0;
        for (String ss : list) {
            longs[kk] = Long.parseLong(ss);
            kk++;
        }
        return longs;
    }

    /**
     * 防止sql注入
     * System.out.print("防SQL注入:"+StringEscapeUtils.escapeSql("1' or '1'='1"));;
     *
     * @param str
     * @return
     */
    public static String Sql(String str) {
        return " '" + StringEscapeUtils.escapeSql(str) + "' ";
    }


    public static String dealMobileStr(String mobileOldStr) {
        if (isEmpty(mobileOldStr)) {
            return null;
        }
        String str = mobileOldStr.replace("-", "").trim().replace(" ", "");
        if (str.length() > 11) {
            str = str.substring(11);
        }
        return str;
    }

    public static boolean isEmpty(String text) {
        return StringUtils.isEmpty(text);
    }

    /**
     * 判断是否为空和isEmpty类似，只是如果输入的内容是空白符号，如：\t \n \f \r 空格等，均视为空
     *
     * @param text
     * @return
     */
    public static boolean isBlank(String text) {
        return StringUtils.isBlank(text);
    }

    public static boolean isNotBlank(String text) {
        return StringUtils.isNotBlank(text);
    }

    /**
     * 形如：/work/android-project/hangzhuProject/OA/OA-project/safefy/target/guower/
     * 获取服务器上文项目根路径
     */
    public static String getWebRootPath(HttpServletRequest request) {
        String root = request.getSession(true).getServletContext().getRealPath("/");
        return root;
    }

    /**
     * /work/android-project/hangzhuProject/OA/OA-project/guowerUploadRoot/
     * 获取tomcat根目录下面的存放upload文件的地址;
     * 避免删除tomcat目录，对应文件丢失
     * <p>
     * 解决：
     * 在tomcat server.xml的<Host name="localhost" appBase="webapps">里面增加
     * <Context docBase="../guowerUploadRoot/upload" path="/guower/upload/"/>
     * <p>
     * tomcat/seftyUpload
     * 可以配置多个映射地址，但是实际地址如：guowerUploadRoot/upload目录 和 guowerUploadRoot/ueditor目录要提前建好
     *
     * @param virtalSuffix 要带有前缀，例如：/guower
     */
    public static String getWebUploadVirtalRootPath(HttpServletRequest request, String virtalSuffix) {

        //真实目录
        //return getWebRootPath(request);

        //虚目录
        String root = getWebRootPath(request);
        String parentRoot = new File(root).getParentFile().getParent();
        //String uploadVirtalRoot = parentRoot + "/guowerUploadRoot/";
        String uploadVirtalRoot = parentRoot + virtalSuffix + "UploadRoot/";
        try {
            new File(uploadVirtalRoot).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print("======创建upload目录失败!!=====");
        }
        return uploadVirtalRoot;
    }

    /**
     * 获取访问的设备的相关的信息
     * //DeviceType|OperatingSystem|BrowserType,如pc|android|chrome|ip1|ip2
     * Computer|Browser|Internet Explorer 11|Internet Explorer|WINDOWS_10|192.168.0.107
     *
     * @param request
     * @return
     */
    public static String getBroserDeviceRamark(HttpServletRequest request) {
        String ip2 = XaUtils.getClientIpAddress2(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        //DeviceType|OperatingSystem|BrowserType,如pc|android|chrome
        String deviceRamrk = operatingSystem.getDeviceType().getName() + "|" + browser.getBrowserType().getName() + "|" + browser.getName() + "|" + browser.getGroup().getName() + "|" + operatingSystem.toString() + "|" + ip2;
        return deviceRamrk;
    }

    /**
     * 跟useragent获取设备的系统以及手机型号
     * @param userAgent
     * @return
     */
    public static String getMobileDeviceInfo(String userAgent){
        if (XaUtils.isEmpty(userAgent)) {
            return "";
        }
        Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?);\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
        Matcher matcher = pattern.matcher(userAgent);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(1).trim() + "|" + matcher.group(2).trim();
        }
        return result;
    }

    public static Browser getBroserInfo(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        return browser;
    }

    public static OperatingSystem getBroserInfoOS(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem;
    }

    /**
     * 获取文件后缀.
     * 不包含.
     * <p>
     * 如：/Users/duandazhi/Desktop/360.exe  ---> exe
     * <p>
     * FilenameUtils.getExtension(file.getOriginalFilename())
     */
    public static String getFileSuffix(String name) {
        if (name == null) {
            return "";
        }
        if (name.indexOf(".") == -1) {
            return "";
        }

        String suffix = name.substring(name.lastIndexOf(".") + 1);

        return suffix.trim().toLowerCase();
    }

    public static String getFileExt(String name) {
        return getFileSuffix(name);
    }

    public static String getFileExtentsion(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    /**
     * 获取文件名称
     *
     * @param path
     * @return 如：path http://dl.360safe.com/360zip_setup_4.0.0.1050.exe --- > 360zip_setup_4.0.0.1050.exe
     */
    public static String getFileNames(String path) {
        return getFileNames(path, "/");
    }

    public static String getFileNames(String path, String regex) {
        if (path == null || path.equalsIgnoreCase("")) {
            return "";
        }
        String[] paths = path.split(regex);
        if (paths.length != 0) {
            return paths[paths.length - 1];
        }
        return "";
    }

    /**
     * /Users/duandazhi/Desktop/360.exe  变成  UsersduandazhiDesktop360.exe
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        String result = "";
        if (StringUtils.isNotBlank(path)) {
            result = path.replace("*", "").replace("|", "")
                    .replace("\\", "").replace(":", "")
                    .replace(">", "")
                    .replace("?", "")
                    .replace("<", "")
                    .replace("/", "").replace("\"", "");
        }
        return result;
    }


    /**
     * os.name:
     * windows or mac os x or mac os or linux
     *
     * @see OperatingSystem
     */
    public static boolean isWindows() {
        String osName = System.getProperty("os.name", "windows").toLowerCase();
        if (osName.contains("windows")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * linux:   file:/work/android-project/
     * windows: file:\\work\\android-project\\
     *
     * @return
     */
    public static String parseFilePath(String path) {
        if (path == null || path.trim().length() == 0) {
            return "";
        }
        if (isWindows()) {
            if (!path.contains("\\\\")) {
                return path.replaceAll("/", "\\\\");
            } else {
                return path;
            }
        } else {
            if (!path.contains("/")) {
                return path.replaceAll("\\\\", "/");
            } else {
                return path;
            }
        }
    }

    /**
     * 获取当前的操作系统
     * windows mac linux
     *
     * @return
     */
    public static OperatingSystem getOperatingSystem() {
        String osName = System.getProperty("os.name", "windows").toLowerCase();
        if (osName.toLowerCase().contains(OperatingSystem.WINDOWS.getName().toLowerCase())) {
            return OperatingSystem.WINDOWS;
        } else if (osName.toLowerCase().contains(OperatingSystem.MAC_OS.getName().toLowerCase())) {
            return OperatingSystem.MAC_OS;
        } else {
            return OperatingSystem.LINUX;
        }
    }


    /**
     * 金额转换,小写金钱转出大写
     * eg:98.76 -->  玖拾捌元柒角陆分
     * eg:12345678.9 -->  壹千贰佰叁拾肆万伍千陆佰柒拾捌元玖角整
     *
     * @param v
     * @return
     */
    public static String moneyTransform(double v) {
        if (v < 0 || v > XaUtils.MAX_VALUE) {
            return "参数非法!";
        }
        long l = Math.round(v * 100);
        if (l == 0) {
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * map抓换为xml报文
     * 手工处理，没有xml头
     *
     * @param map
     * @param sb
     */
    @SuppressWarnings("all")
    public static String mapToXMLTest2(Map map, StringBuilder sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value) {
                value = "";
            }
            if (value instanceof List) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXMLTest2(hm, sb);
                }
                sb.append("</" + key + ">");

            } else {
                if (value instanceof Map) {
                    sb.append("<" + key + ">");
                    mapToXMLTest2((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
            }
        }
        return sb.toString();
    }

   /* public static void main(String[] args) throws Exception {
        Map<String, String> data = new Hashtable<>();
        data.put("a", "b");
        System.out.println(mapToXml(data).trim());
    }
*/
}
