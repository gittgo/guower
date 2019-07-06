package com.ourslook.guower.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 常量
 *
 * @author dazer
 */
public class Constant {
    /**
     * 超级管理员 - 角色ID
     */
    public static final long SYS_ROLO_ID_SUPER_ADMIN = 1;

    /**
     * 平台管理员 - 角色ID
     */
    public static final long SYS_ROLO_ID_SUPER_ADMINID = 12;

    /**
     * 商家用户角色 - 角色ID
     */
    public static final long SYS_ROLO_ID_SUPER_BUSINESS = 13;

    /**
     * @see com.ourslook.guower.entity.common.TbUserEntity
     * @see com.ourslook.guower.entity.SysUserEntity
     * 两个用户是否进行同步
     */
    public static final boolean IS_SYNCHRONIZE_NORMAL_USER = true;

    /**
     * 所有h5的前缀
     */
    public static String h5Header = "<head><meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0,user-scalable=no\\\"/><style type=\\\"text/css\\\">img,table{max-width: 100% !important;}</style></head> ";

    /**
     * 项目前缀，根据项目自行修改
     */
    public static final String SERVIER_NAME = "guower";
    public static final String SERVIER_NAME_SUFFIX = "/guower";
    /**
     * 用来方便调试支付宝,微信支付回调地址
     * 0:公司内网 1:服务器
     */
    public static int ENV = 1;
    /**
     * http://1.80.219.21:8888/    http://david007.wicp.net:25307/  http://dazer88.free.ngrok.cc/
     */
    public static String SERVER_ADDRESS = "http://dazer88.free.ngrok.cc/";

    /**
     * 业务相关的类型值 start
     */

    /**
     * 积分获取途径类型
     * 1.注册 2.发布文章
     */
    public static class ScoreGetType {
        @ConstantName("注册")
        public static final int TYPE_SCORE_GET_REGISTER = 1;
        @ConstantName("发布文章")
        public static final int TYPE_SCORE_GET_NEWS = 2;

        public static String caseName(int value){
            if(value == TYPE_SCORE_GET_REGISTER) return "注册";
            else if (value == TYPE_SCORE_GET_NEWS) return "发布文章";
            return "";
        }
    }

    /**
     * 用户推送位置类型
     * 1.专栏作者，2.企业专栏，3.作者排行
     */
    public static class PushPositionType {
        @ConstantName("专栏作者")
        public static final int TYPE_PUSH_POSITION_ONE = 1;
        @ConstantName("企业专栏")
        public static final int TYPE_PUSH_POSITION_TWO = 2;
        @ConstantName("作者排行")
        public static final int TYPE_PUSH_POSITION_THREE = 3;

        public static String caseName(int value){
            if(value == TYPE_PUSH_POSITION_ONE) return "专栏作者";
            else if (value == TYPE_PUSH_POSITION_TWO) return "企业专栏";
            else if (value == TYPE_PUSH_POSITION_THREE) return "作者排行";
            return "";
        }
    }


    /**
     * 审核状态类型
     * 0.审核中，1.通过，2.未通过
     */
    public static class ExamineType {
        @ConstantName("通过")
        public static final int TYPE_EXAMINE_PASS = 1;
        @ConstantName("审核中")
        public static final int TYPE_EXAMINE_UNTREATED = 0;
        @ConstantName("未通过")
        public static final int TYPE_EXAMINE_NOT_PASS = 2;

        public static String caseName(int value){
            if(value == TYPE_EXAMINE_PASS) return "通过";
            else if (value == TYPE_EXAMINE_UNTREATED) return "审核中";
            else if (value == TYPE_EXAMINE_NOT_PASS) return "未通过";
            return "";
        }
    }


    /**
     * 用户认证类型
     * 1.企业认证 2.作者认证 3.媒体认证
     */
    public static class UserType {
        @ConstantName("企业认证")
        public static final int TYPE_USER_ENTERPRISE = 1;
        @ConstantName("作者认证")
        public static final int TYPE_USER_AUTHOR = 2;
        @ConstantName("媒体认证")
        public static final int TYPE_USER_MEDIA = 3;

        public static String caseName(int value){
            if(value == TYPE_USER_ENTERPRISE) return "企业认证";
            else if (value == TYPE_USER_AUTHOR) return "作者认证";
            else if (value == TYPE_USER_MEDIA) return "媒体认证";
            return "";
        }
    }



    /**
     * 广告类型
     */
    @ConstantName("广告类型")
    public static class AdvertisementType {
        @ConstantName("果味box")
        public static final int TYPE_ADVERTISING_GUOWER_BOX = 1;
        @ConstantName("合作内容")
        public static final int TYPE_ADVERTISING_COOPERATIVE_CONTENT = 2;
        @ConstantName("战略合作")
        public static final int TYPE_ADVERTISING_STRATEGIC_COOPERATION = 3;

        @ConstantName("首页顶部Button")
        public static final int TYPE_ADVERTISING_HOME_TOP_BUTTON= 11;
        @ConstantName("首页轮播广告")
        public static final int TYPE_ADVERTISING_HOME_BANNER = 12;
        @ConstantName("首页焦点Button")
        public static final int TYPE_ADVERTISING_HOME_FOCUS_BUTTON = 13;
        @ConstantName("首页右侧Button")
        public static final int TYPE_ADVERTISING_HOME_RIGHT_BUTTON = 14;
        @ConstantName("首页作者推荐")
        public static final int TYPE_ADVERTISING_HOME_AUTHOR = 15;
        @ConstantName("首页企业推荐")
        public static final int TYPE_ADVERTISING_HOME_ENTERPRISE = 16;

        @ConstantName("新闻轮播广告")
        public static final int TYPE_ADVERTISING_NEWS_BANNER = 21;
        @ConstantName("新闻栏目页视频广告")
        public static final int TYPE_ADVERTISING_NEWS_VIDEO = 22;
        @ConstantName("新闻栏目页右侧Button")
        public static final int TYPE_ADVERTISING_NEWS_RIGHT_BUTTON = 23;
        @ConstantName("文章详情右侧Button")
        public static final int TYPE_ADVERTISING_NEWS_DETAILS_RIGHT_BUTTON = 24;


        @ConstantName("快报栏目右侧Button")
        public static final int TYPE_ADVERTISING_FASTNEWS_RIGHT_BUTTON = 31;

        @ConstantName("专栏栏目页视频广告")
        public static final int TYPE_SPECIALCOLUMN_VIDEO = 32;

        @ConstantName("专栏栏目页右侧Button")
        public static final int TYPE_SPECIALCOLUMN_RIGHT_BUTTON = 33;

        @ConstantName("专栏轮播广告")
        public static final int TYPE_SPECIALCOLUMN_BANNER = 34;

        //下面是app端的
        @ConstantName("APP轮播")
        public static final int TYPE_ADVERTISING_APP_BANNER = 41;

        @ConstantName("二级页面banner")
        public static final int TYPE_ADVERTISING_TWO_LEVEL_PAGE_BANNER = 51;

        @ConstantName("APP详情页banner")
        public static final int TYPE_ADVERTISING_APP_DETAILS_BANNER = 61;

        public static String caseName(int value){
            if(value == TYPE_ADVERTISING_GUOWER_BOX) return "果味box";
            else if (value == TYPE_ADVERTISING_COOPERATIVE_CONTENT) return "合作内容";
            else if (value == TYPE_ADVERTISING_STRATEGIC_COOPERATION) return "战略合作";

            else if (value == TYPE_ADVERTISING_HOME_TOP_BUTTON) return "首页顶部Button";
            else if (value == TYPE_ADVERTISING_HOME_BANNER) return "首页轮播广告";
            else if (value == TYPE_ADVERTISING_HOME_FOCUS_BUTTON) return "首页焦点Button";
            else if (value == TYPE_ADVERTISING_HOME_RIGHT_BUTTON) return "首页右侧Button";
            else if (value == TYPE_ADVERTISING_HOME_AUTHOR) return "首页作者推荐";
            else if (value == TYPE_ADVERTISING_HOME_ENTERPRISE) return "首页企业推荐";

            else if (value == TYPE_ADVERTISING_NEWS_BANNER) return "新闻轮播广告";
            else if (value == TYPE_ADVERTISING_NEWS_VIDEO) return "新闻栏目页视频广告";
            else if (value == TYPE_ADVERTISING_NEWS_RIGHT_BUTTON) return "新闻栏目页右侧Button";
            else if (value == TYPE_ADVERTISING_NEWS_DETAILS_RIGHT_BUTTON) return "文章详情右侧Button";

            else if (value == TYPE_ADVERTISING_FASTNEWS_RIGHT_BUTTON) return "快报栏目右侧Button";
            else if (value == TYPE_SPECIALCOLUMN_VIDEO) return "专栏栏目页视频广告";
            else if (value == TYPE_SPECIALCOLUMN_RIGHT_BUTTON) return "专栏栏目页右侧Button";
            else if (value == TYPE_SPECIALCOLUMN_BANNER) return "专栏轮播广告";


            else if (value == TYPE_ADVERTISING_APP_BANNER) return "APP轮播";

            else if (value == TYPE_ADVERTISING_TWO_LEVEL_PAGE_BANNER) return "二级页面banner";

            else if (value == TYPE_ADVERTISING_APP_DETAILS_BANNER) return "APP详情页banner";

            return "";
        }

        /**
         * 推荐条数
         * @param value 广告类型
         * @return  该广告类型推荐条数
         */
        public static String getRecommendCount(int value){
            if(value == TYPE_ADVERTISING_GUOWER_BOX) return "1";
            else if (value == TYPE_ADVERTISING_COOPERATIVE_CONTENT) return "10";
            else if (value == TYPE_ADVERTISING_STRATEGIC_COOPERATION) return "6";

            else if (value == TYPE_ADVERTISING_HOME_TOP_BUTTON) return "5";
            else if (value == TYPE_ADVERTISING_HOME_BANNER) return "5";
            else if (value == TYPE_ADVERTISING_HOME_FOCUS_BUTTON) return "3";
            else if (value == TYPE_ADVERTISING_HOME_RIGHT_BUTTON) return "3";
            else if (value == TYPE_ADVERTISING_HOME_AUTHOR) return "1";
            else if (value == TYPE_ADVERTISING_HOME_ENTERPRISE) return "1";

            else if (value == TYPE_ADVERTISING_NEWS_BANNER) return "4";
            else if (value == TYPE_ADVERTISING_NEWS_VIDEO) return "1";
            else if (value == TYPE_ADVERTISING_NEWS_RIGHT_BUTTON) return "3";
            else if (value == TYPE_ADVERTISING_NEWS_DETAILS_RIGHT_BUTTON) return "3";

            else if (value == TYPE_ADVERTISING_FASTNEWS_RIGHT_BUTTON) return "3";
            else if (value == TYPE_SPECIALCOLUMN_VIDEO) return "1";
            else if (value == TYPE_SPECIALCOLUMN_RIGHT_BUTTON) return "3";
            else if (value == TYPE_SPECIALCOLUMN_BANNER) return "5";

            else if (value == TYPE_ADVERTISING_APP_BANNER) return "5";

            else if (value == TYPE_ADVERTISING_TWO_LEVEL_PAGE_BANNER) return "1";

            else if (value == TYPE_ADVERTISING_APP_DETAILS_BANNER) return "3";

            return "";
        }
    }

    /**
     * 货币兑换状态
     */
    @ConstantName("货币兑换状态")
    public static class ExchangeRecordStatus{
        @ConstantName("审核中")
        public static final int CHANGE_ING = 1;
        @ConstantName("兑换完成")
        public static final int CHANGE_OVER = 2;
    }

    /**
     * 文章发布类型
     */
    @ConstantName("文章发布类型")
    public static class NewsReleaseType {
        @ConstantName("作者")
        public static final int AUTHOR = 2;
        @ConstantName("后台")
        public static final int SYSTEM = 1;
    }


    /**
     * 业务相关的类型值 end
     */



    /**
     * @author eason.zt
     * @ClassName: Status
     * @Description: 常用状态类型 同 EnumType;历史遗留
     * @date 2014年8月13日 下午2:45:25
     */
    @ConstantName("全系统状态")
    public static class Status {
        @ConstantName("锁定、禁用")
        public static final int locked = -1;
        @ConstantName("无效")
        public static final int invalid = 0;
        @ConstantName("正常")
        public static final int valid = 1;
        @ConstantName("上架")
        public static final int publish = 2;
        @ConstantName("删除")
        public static final int delete = 3;
    }

    /**
     * 认证类型
     * 0,未认证，1：已申请  2：已认证    3：认证未通过
     */
    @ConstantName("用户认证类型")
    public static class IdentificationType {
        @ConstantName("未认证")
        public static final int IdentificationType_0 = 0;
        @ConstantName("已申请")
        public static final int IdentificationType_1 = 1;
        @ConstantName("已认证")
        public static final int IdentificationType_2 = 2;
        @ConstantName("认证未通过")
        public static final int IdentificationType_3 = 3;
    }

    public static class ConstantBean {
        private int code;
        private String name;

        public ConstantBean() {
        }

        public ConstantBean(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        private CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 设备类型  内部类
     */
    @SuppressWarnings("all")
    public enum DeviceConstant {
        IOS(1, "ios用户端"),
        IOS11(11, "ios商家端"),
        ANDROID(2, "android用户端"),
        ANDROID22(22, "android商家端"),
        //WEB(3, "web")
        ;

        private String name;
        private int code;

        private DeviceConstant(int code, String name) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        //为操作方便为初始值加getAll方法
        public static List<String> getAllDeviceName() {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < DeviceConstant.values().length; i++) {
                list.add(DeviceConstant.values()[i].name);
            }
            return list;
        }

        public static List<ConstantBean> getAllDevices() {
            List<ConstantBean> beans = new ArrayList<>();
            for (int i = 0; i < DeviceConstant.values().length; i++) {
                DeviceConstant constant = DeviceConstant.values()[i];
                beans.add(new ConstantBean(constant.code, constant.name));
            }
            return beans;
        }

        /**
         * 判断val是否是在(public static)常量中
         *
         * @param val 这里一般是死的数字
         * @return
         */
        public static boolean isConstantVal(int val) {
            return ReflectUtils.numberInClass(ConstantBean.class, val);
        }

        /**
         * @param val 不要写死的数字
         * @return
         */
        public static String getConstantName(int val) {
            String constantName = null;
            try {
                constantName = ReflectUtils.getContantName(ConstantBean.class, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return constantName;
        }
    }

    /**
     * 字符串常量
     */
    public enum StringConstant {
        /**
         * 空白符
         */
        WHITESAACE(" "),
        /**
         * 制表符 TAP
         */
        TAP("    "),
        /**
         * 英文逗号
         */
        DOT(","),
        /**
         * 中文逗号
         */
        DOT_CN("，"),
        /**
         * 分号
         */
        SEMICOLON(";"),
        /**
         * 中文分号
         */
        SEMICOLON_CN("；"),
        /**
         * 冒号
         */
        COLON("："),
        /**
         * 中文冒号
         */
        COLON_CN(":");

        private String value;

        private StringConstant(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 云通讯短信模板
     * <p>
     * 【XXXXXX】您的验证码为{1}，请于{2}分钟内正确输入，如非本人操作，请忽略此短信。
     */
    public enum YuntongxunSmsTemplate {

        COMMON("264124", "通用短信验证码");

        private String tcode;
        private String tname;

        YuntongxunSmsTemplate(String tcode, String tname) {
            this.tcode = tcode;
            this.tname = tname;
        }

        public String getTcode() {
            return tcode;
        }

        public String getTname() {
            return tname;
        }

        /**
         * 获取所有的名称
         */
        public static List<String> getAllNames() {
            List<String> names = new ArrayList<>();
            for (int i = 0; i < YuntongxunSmsTemplate.values().length; i++) {
                YuntongxunSmsTemplate company = YuntongxunSmsTemplate.values()[i];
                names.add(company.getTname());
            }
            return names;
        }
    }

    public static final class UrlConstant {
        public static final String TOLIAN_STOCK_PASS = "";
    }

    /**
     * @see com.ourslook.guower.utils.result.XaResult
     * @see com.ourslook.guower.utils.result.R
     */
    @ConstantName("全系统http状态码")
    public static final class Code {
        /**
         * @Fields ok : 成功
         */
        @ConstantName("成功")
        public static final int success = 0;
        /**
         * @Fields error : 失败
         */
        @ConstantName("失败")
        public static final int error = 500;
        /**
         * token失效
         */
        @ConstantName("token失效")
        public static final int code_failure_token = 101;

        @ConstantName("系统错误！")
        public static final int code_system_failure = 100001;

        @ConstantName("业务错误！")
        public static final int code_business_failure = 200001;

        @ConstantName("校验失败！")
        public static final int code_validation_failure = 300001;

        @ConstantName("数据库访问失败！")
        public static final int code_database_failure = 400001;

        @ConstantName("json写错误！")
        public static final int code_json_failure_wirte = 500001;

        @ConstantName("json读取错误！")
        public static final int code_json_failure_reade = 500002;

        @ConstantName("对象查找失败！")
        public static final int code_objectNotFand_failure = 600001;

        @ConstantName("参数异常！")
        public static final int code_request_parameter_failure = 700001;

        @ConstantName("请求参数缺失！")
        public static final int code_request_type_mismatch_failure = 700002;

        @ConstantName("socket错误！")
        public static final int code_socket_failure = 800001;

        @ConstantName("登录错误次数过多！")
        public static final int code_login_errors_failure = 900001;

        //================================================================
        /**
         * 如果程序报错之后，移动端要做操作对应的业务操作，才加上这个状态吗，否则不要加。
         */
        /**
         * 二维码无法识别
         */
        @ConstantName("业务错误-二维码无法识别！")
        public static final int code_qr_errors_failure = 100001;
        /**
         * 账号余额不足
         */
        @ConstantName("业务错误-账号余额不足！")
        public static final int code_account_money_failure = 100002;
        /**
         * 第三方账号没有绑定
         */
        @ConstantName("业务错误-第三方账号没有绑定！")
        public static final int code_account_three_nobingd_failure = 100003;
        /**
         * 抢单是没有加入团队
         */
        @ConstantName("业务错误-请加入团队之后再抢单！")
        public static final int code_send_grab_failure = 100004;
        /**
         * 收付款：二维码过期
         */
        @ConstantName("业务错误-收付款：二维码过期！")
        public static final int code_qr_expire_failure = 100005;
        /**
         * 收付款：重复收款
         */
        @ConstantName("业务错误-重复收款！")
        public static final int code_qr_duplicate_failure = 100006;
    }

    /**
     * @author eason.zt
     * @ClassName: Message
     * @Description: xaResult的message
     * @date 2014年8月15日 下午5:34:20
     */
    public static final class Message {
        /**
         * @Fields ok : 成功
         */
        public static final String success = "成功!";
        /**
         * @Fields error: 失败
         */
        public static final String error = "系统繁忙，请稍后重试!";
        public static final String error_method_no_impl = "方法未实现!";
        public static final String error_runtimeExc = "运行时错误!";
        public static final String object_not_find = "找不到要操作的记录!";
        public static final String object_not_config = "配置信息不完整,请添加!";
        public static final String object_property_not_find = "PropertyUtils实体属性没有发现!";
    }

    /**
     * 用于常量注释的注解
     * <p>
     * 方便别的地方可以直接获取到所有的常量值，方便操作
     *
     * @author duandazhi
     * @date 2017/1/5 下午4:39
     */
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConstantName {
        String value() default "";
    }

    /**
     *
     */
    @ConstantName("支付类型、收支渠道")
    public static class PayType {
        /**
         * 微信
         */
        @ConstantName("微信")
        public static final Integer WXPAY = 0;
        /**
         * 支付宝
         */
        @ConstantName("支付宝")
        public static final Integer ALIPAY = 1;
        /**
         * 银联
         */
        @Deprecated
        @ConstantName("银联(预留)")
        public static final Integer UNIONPAY = 2;
        /**
         * 工行支付-预留
         */
        @Deprecated
        @ConstantName("工行支付(预留)")
        public static final Integer ICBC_PAY = 3;
        /**
         * 用户端特有 /积分支付/零钱/平台
         * 钱包支付/积分支付；如果全部使用积分抵扣，支付方式就设置这个
         */
        @ConstantName("钱包支付")
        public static final Integer WALLET = 4;
    }

    /**
     * 订单状态
     *
     * @author zhanglin
     * date: 2015-12-01 16:41:24
     * //退款;退货;
     * 淘宝:待付款;待发货;待收货;待评价  (4种) 已取消(自动,被动)(交易关闭);已完成(交易完成);
     * 京东:待付款;待发货;待收货;待评价        已取消(自动,被动)(交易关闭);已完成(交易完成);
     * <p>
     * //酒店;门票/服务
     * 携程:待付款;      未出行;待点评;
     * 美团:待付款;      待使用;待点评;
     * 0:未支付,待支付,待付款;1:已支付(待消费、待发货、待入住);2:待收货(没有物流的没有此状态);3:待评价,已经离店;4: 已评价(已完成)(交易完成);5:已取消--已失效;(自动,被动)(交易关闭)
     */
    @ConstantName("订单类型")
    public static class OrderStatus {
        /**
         * 未支付,待支付,待付款
         */
        @ConstantName("待付款")
        public static final int UN_PAY = 0;

        /**
         * 待发货(待消费、待发货、待入住、已支付)
         */
        @ConstantName("待发货(外卖订单，这里叫做待接单)")
        public static final int PAYED = 1;

        /**
         * 待收货;已经发货；(没有物流/服务类)的没有此状态) (服务类订单：（游学/门票、住/酒店、服务的培训义工）)
         *
         * @deprecated
         */
        @Deprecated
        @ConstantName("待收货")
        public static final int UN_CONSIGNEE = 2;

        /**
         * 待评价,已经使用,已经消费,已经收货
         */
        @ConstantName("配送完成，待评价")
        public static final int UN_COMMENT = 3;

        /**
         * 已评价(已完成)(交易完成)
         */
        @ConstantName("交易完成")
        public static final int SUCCES_TRADE = 4;

        /**
         * 已取消--已失效;(自动,被动)(交易关闭)
         */
        @ConstantName("已取消")
        public static final int CLOSE_TRADE = 5;

        /**
         * 有退款 payRefund
         */
        @ConstantName("有退款")
        public static final int PAY_REFUND = 6;

        /**
         * 外卖订单多了一个接单的操作，
         */
        @ConstantName("商家已接单待配送 和 待取货 ")
        public static final int EXP_BUSINESS_RECEIVE = 11;

        /**
         * 发布配送之后配送中
         * 配送完成之后就是带评价
         */
        @ConstantName("配送中")
        public static final int EXP_ING = 12;

        /**
         * 配送完成待商家确认，才给配送员打钱
         */
        @ConstantName("配送完成待商家确认")
        public static final int EXP_END_CONFIRM = 13;

        /**
         * 把一个常量类转成Map<常量值，常量的中文注释>
         *
         * @return
         */
        public static Map<Integer, String> toMap() {
            Map<Integer, String> map = null;
            try {
                map = ReflectUtils.covetConstaintClassToMap(OrderStatus.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        /**
         * 判断val是否是在(public static)常量中
         *
         * @param val 这里一般是死的数字
         * @return
         */
        public static boolean isConstantVal(int val) {
            return ReflectUtils.numberInClass(OrderStatus.class, val);
        }

        /**
         * @param val 不要写死的数字
         * @return
         */
        public static String getConstantName(int val) {
            String constantName = null;
            try {
                constantName = ReflectUtils.getContantName(OrderStatus.class, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return constantName;
        }
    }

    /**
     * 自营快递配送状态：待取货，已取货/配送中、配送完成
     * 配送状态（  0等待配送员抢单  1配送中  2配送本人确认  3配送已确认（转钱） -1待商家发布配送）
     */
    @ConstantName("学有惠配送状态")
    public static class LogisticsStatus {
        @ConstantName("等待配送员抢单")
        public static final Integer LOGISTICS_0 = 90;
        @ConstantName("待取货/待配送")
        public static final Integer LOGISTICS_1 = 91;
        /**
         * 配送状态改为配送中，把原订单也改为配送中 【12：配送中】
         */
        @ConstantName("配送中")
        public static final Integer LOGISTICS_DELIVERY = 92;
        /**
         * 配送员确认配送完成
         */
        @ConstantName("配送员确认已送达")
        public static final Integer LOGISTICS_SUCESS = 93;

        /**
         * 商家/配送单发布学生 确认配送完成
         */
        @ConstantName("商家/配送单发布学生 确认已送达")
        public static final Integer PUBLISHER_LOGISTICS_SUCESS = 94;

    }

    /**
     * 配送交易明细状态：0已抢单未送达则配送金冻结；1已抢单并且已送达配送金激活
     */
    @ConstantName("配送交易明细状态")
    public static class IeStatus {
        @ConstantName("已抢单未送达则配送金冻结")
        public static final Integer IeStatus_0 = 0;
        @ConstantName("已抢单并且已送达配送金激活")
        public static final Integer IeStatus_1 = 1;
    }

    /**
     * 项目提现审核状态
     * <p>
     * 0待审核  1已审核  2驳回，参见：WithdrawStatus
     */
    @ConstantName("审核状态")
    public static class WithdrawStatus {
        /**
         * 线下支付 已提交|待审核
         */
        @ConstantName("已提交|待审核")
        public static final int OFFLINE_STATUS_1 = 0;
        /**
         * 审核通过
         */
        @ConstantName("审核通过")
        public static final int OFFLINE_STATUS_2 = 1;
        /**
         * 审核拒绝
         */
        @ConstantName("审核拒绝")
        public static final int OFFLINE_STATUS_3 = 2;
    }

    /**
     * 充值状态,0:待充值(未支付) 1：充值成功（支付成功）  2：充值关闭（后台自动关闭的充值订单） 3:充值失败
     *
     * @see //BssRechargeorderEntity#rechargeStatus
     */
    public static class WalletRechargeStatus {
        public static final Integer RECHARGE_ING = 0;
        public static final Integer RECHARGE_SUCESS = 1;
        public static final Integer RECHARGE_CLOSE = 2;
        public static final Integer RECHARGE_fail = 3;
    }

    /**
     * 预留，该项目不用，需用的项目放开
     */
    @Deprecated
    @ConstantName("发票类型【预留，该项目不用】")
    public static class BillTaxType {
        /**
         * 未发票
         */
        @ConstantName("未发票")
        public static final int Not_TAX = 0;
        /**
         * 审核通过
         */
        @ConstantName("普通发票")
        public static final int General_Tax = 1;
        /**
         * 审核拒绝
         */
        @ConstantName("增值税专用发票")
        public static final int AddValue_Tax = 2;
        /**
         * 电子发票
         */
        @ConstantName("电子发票")
        public static final int Ele_Tax = 3;
    }

    /**
     * 订单退款状态
     * <p>
     * 可以参见：
     * 溜达  ApiRefundOrderController  和  ApiMatchOrderController 活动、赛事取消订单
     * 红宝箱
     *
     * @author changlu
     * @date 2017/1/21 下午10:23
     * <p>
     * 退款（买家已付款）； 退货（卖家已发货）
     * 已经支付---进行退款
     * 已经发货---进行退货
     * <p>
     * //@see com.web.exiaodao.business.entity.Order
     * <p>
     * 退款申请已提交-->退款申请已通过/退款审核拒绝-->退款成功/退款失败
     * <p>
     * 退换货/款进度：发起退款、待审核、商家审核（同意|拒绝）、配送员取货、取件完成商家确认、退款|补单
     */
    @Deprecated
    @ConstantName("订单退货状态【预留，该项目不用】")
    public static class OrderRefundStatus {
        @ConstantName("退款申请已提交")
        public static final Integer REFUNDING = 80;
        /**
         * 审核-成功--
         * 审核-失败
         */
        @ConstantName("退款申请已通过/退款中")
        public static final Integer REFUNDSUCESS = 81;
        @ConstantName("退款审核拒绝")
        public static final Integer REFUNDFAIL = 82;

        @ConstantName("等待配送员取货")
        public static final Integer REFUND_EXPRESS_PICKER = 84;

        @ConstantName("等待商家确认收件")
        public static final Integer REFUND_EXPRESS_SUCCESS = 85;

        //退款成功(淘宝：退款成功，退款不一定到账了,银行的状态（提交银行处理-->银行受理退款-->退款到银行卡）);本app：退款成功，钱已经到账
        @ConstantName("退款成功，钱不一定到账")
        public static final Integer REFUND_SUCCESS = 86;
        //退款拒绝
        @ConstantName("退款拒绝")
        public static final Integer REFUND_REJECT = 87;
        @ConstantName("退款终止/取消")
        public static final Integer REFUND_FAIL = 88;
    }

    @ConstantName("红包使用状态【预留，该项目不用】")
    public static class CouponUseStatus {
        @ConstantName("未使用")
        public static final Integer NOT_USE = 0;

        @ConstantName("已使用")
        public static final Integer USE = 1;

        @ConstantName("已过期")
        public static final Integer EXPIRE = 2;
    }

    /**
     * 分享平台
     */
    @ConstantName("分享平台")
    public static class SharePlatformType {
        @ConstantName("微信")
        public static final int WEIXIN = 31;
        @ConstantName("微博")
        public static final int WEIBO = 32;
        @ConstantName("QQ")
        public static final int QQ = 33;
        @ConstantName("支付宝")
        public static final int ALIPAY = 34;

        /**
         * 判断val是否是在(public static)常量中
         *
         * @param val
         * @return
         */
        public static boolean isConstantVal(long val) {
            return ReflectUtils.numberInClass(SharePlatformType.class, val);
        }
    }

    /**
     * 推送类型
     *
     * @see com.ourslook.guower.utils.jpush.JPushMessgeUtil
     */
    @ConstantName("推送类型")
    public static class JpushMessageType {
        @ConstantName("系统消息")
        public static final int SYS_TO_USER = 10;
        @ConstantName("商家-您有外卖订单的推送")
        public static final int SELLER_ORDER = 1;
        @ConstantName("线下支付-用户扫码，发送商家支付成功消息")
        public static final int QR_TO_SELLER = 2;
        @ConstantName("线下支付-商家扫码，发送给用户商家收款成功的消息")
        public static final int QR_TO_USER = 3;
        @ConstantName("线下支付-商家扫码，发送给用户商家收款失败的消息")
        public static final int QR_TO_USER_ERROR = 33;
        @ConstantName("商家/用户-配送订单发布或加价，发送给配送员进行抢单的消息")
        public static final int GRAB_SEND_ORDER = 4;
        @ConstantName("用户点去唤醒，发送给受唤醒人进入首页邀请消费的消息")
        public static final int WAKE_SEND_USER = 5;
        @ConstantName("返佣消息")
        public static final int PUSH_TYPE_AREA = 6;
        @ConstantName("订单消息")
        public static final int PUSH_TYPE_ORDER =7;
    }

    /**
     * 配送订单的配送状态
     */
    @ConstantName("配送订单的配送状态")
    public static class SenderOrderPushStatus {
        @ConstantName("已经推送给本楼的人了")
        public static final Integer push_status_1 = 1;
        @ConstantName("已经推送给所有人了")
        public static final Integer push_status_2 = 2;
    }

    /**
     * 具体查看：《学有惠》需求沟通确认内容-2018.04.10.doc --> 3:用户端角色说明
     */
    @ConstantName("用户类型|角色类型")
    public static class UserTypes {
        /**
         * 普通下单用户
         * 无配送收益、团队合伙收益查看权限
         * 未加入任何团队时无法抢配送订单，需引导加入团队
         * 可发布配送订单
         */
        @ConstantName("普通用户|学生")
        public static final Integer USER_NORMAL_1 = 21;
        /**
         * 校园合伙团队的负责人（可有多个）
         * 可提现校园合伙人团队收益权限，提现成功后线下分配
         * 可发布配送订单或抢单
         */
        @ConstantName("普通用户-负责人团队")
        public static final Integer USER_NORMAL_2 = 22;
        /**
         * 负责平台的推广运维工作
         * 可查看校园合伙人团队收益，不可提现
         * 可发布配送订单或抢单
         */
        @ConstantName("普通用户-合伙人团队")
        public static final Integer USER_NORMAL_3 = 23;
        /**
         * 即骑兵
         * 可发布配送订单或抢单
         * 可提现配送收益
         */
        @ConstantName("普通用户-骑兵")
        public static final Integer USER_NORMAL_4 = 24;
        /**
         * 团体账户
         */
        @ConstantName("团体账户")
        public static final Integer USER_NORMAL_5 = 25;
        /**
         * 楼栋长团队
         */
        @ConstantName("楼栋长团队")
        public static final Integer USER_NORMAL_6 = 26;

        /**
         * 商家用户
         */
        @ConstantName("商家用户")
        public static final Integer USER_SELLER = 1;

        /**
         * 判断val是否是在(public static)常量中
         *
         * @param val
         * @return
         */
        public static boolean isConstantVal(int val) {
            return ReflectUtils.numberInClass(UserTypes.class, val);
        }
    }

    /**
     * 系统里面所有的配置，参考参数管理
     *
     * @see //ApiRestController#generateConfig() 自动生成所有配置的代码  api/rest/generateConfig
     * @see com.ourslook.guower.api.ApiCfgController#syscoflist
     * 使用：
     * String mode = sysConfigService.getValueByCode(Constant.SysConfigValue.CONFIG_AUTO_MODEL.getTcode());
     */
    public enum SysConfigValue {
        CONFIG_SERVICE_TEL("CONFIG_SERVICE_TEL", "客服电话"),
        CONFIG_MONEY_SCORE_RATE("CONFIG_MONEY_SCORE_RATE", "线下收款码积分最大使用比例，1：100%可以，用积分抵扣； 0.40： 是只有40%的可以积分来抵扣"),
        CONFIG_MONEY_SCORE_ORDER_RATE("CONFIG_MONEY_SCORE_ORDER_RATE", "下单积分使用最大比例，1： 100%可以用积分抵扣； 0.8：80%的钱可以用积分抵扣"),
        CONFIG_MONEY_QR_IN_RATE("CONFIG_MONEY_QR_IN_RATE", "付款码优惠比例（平台统一设置最低值，商户只能高于这个最低）"),
        CONFIG_MONEY_QR_EXPIRE_MIN("CONFIG_MONEY_QR_EXPIRE_MIN", "收款码-过期时间，单位分钟，默认3分钟"),
        CONFIG_MONEY_CASH_MIN("CONFIG_MONEY_CASH_MIN", "提现至少的金额，支付宝打款要手续费，这里设置提现限制，单元元"),
        CONFIG_MONEY_CASH_FEE_RATE("CONFIG_MONEY_CASH_FEE_RATE", "提现手续费，比例，0.005，都是小数"),
        CONFIG_MONEY_CASH_DEPOSIT("CONFIG_MONEY_CASH_DEPOSIT", "商家入驻保障金设，单位元，这里设置1万元"),
        CONFIG_MONEY_1TEAM_RATE("CONFIG_MONEY_1TEAM_RATE", "团队收益比例，小数（平台统一设置最低值，商户只能高于这个最低）"),
        CONFIG_MONEY_1RECOMMEND_RATE("CONFIG_MONEY_1RECOMMEND_RATE", "推荐积分比例，小数（平台统一设置最低值，商户只能高于这个最低）"),
        CONFIG_DISTRIBUTION_TYPE_10002("CONFIG_DISTRIBUTION_TYPE_10002", "配送最低费用：呼叫楼长0.5元"),
        CONFIG_DISTRIBUTION_TYPE_10001("CONFIG_DISTRIBUTION_TYPE_10001", "配送最低费用：配送到楼最低配送1.5元"),
        CONFIG_DISTRIBUTION_TYPE_10000("CONFIG_DISTRIBUTION_TYPE_10000", "配送最低费用：配送到寝最低配送2元"),
        CONFIG_ORDER_RECEVIE_MIN("CONFIG_ORDER_RECEVIE_MIN", "配送员配送完成，商家没有在规定的时间内确认完成，系统自动配送完成; 单位：min"),
        CONFIG_ORDER_CANCEL_MIN("CONFIG_ORDER_CANCEL_MIN", "订单有效时间，单位分钟，过了这个时间还没支付自动取消"),
        CONFIG_SENDORDER_RECEVIE_MIN("CONFIG_SENDORDER_RECEVIE_MIN", "配送员配送完成，发布学生没有在规定的时间内确认完成，系统自动配送完成; 单位：min");

        private String tcode;
        private String tname;

        SysConfigValue(String tcode, String tname) {
            this.tcode = tcode;
            this.tname = tname;
        }

        public String getTcode() {
            return tcode;
        }

        public String getTname() {
            return tname;
        }
    }

    /**
     * 现金奖励里面：只有负责人才能进行团队收益提现
     */
    @ConstantName("推广赚钱|我的分享")
    public static class ScoreType {
        @ConstantName("注册邀请成功奖励")
        public static final Integer REGISTER = 2;
        @ConstantName("推荐奖励")
        public static final Integer SHARE = 3;
        @ConstantName("购买积分")
        public static final Integer BUY = 4;
        @ConstantName("唤醒好友")
        public static final Integer IN_3 = 13;
    }


    /**
     * 收支交易类型
     */
    @ConstantName("收支交易类型")
    public static class IePayType {
        //收入
        @ConstantName("充值")
        public static final Integer IN_1 = 1;
        @ConstantName("配送得佣")
        public static final Integer IN_2 = 2;
        @ConstantName("购买积分")
        public static final Integer IN_3 = 3;
        @ConstantName("提现积分")
        public static final Integer IN_4 = 4;
        @ConstantName("注册邀请/绑定成功奖励")
        public static final Integer IN_5 = 5;
        @ConstantName("推荐奖励")
        public static final Integer IN_6 = 6;
        @ConstantName("唤醒好友奖励")
        public static final Integer IN_7 = 7;
        @ConstantName("退款")
        public static final Integer IN_8 = 8;
        @ConstantName("商家支付积分金额")
        public static final Integer IN_9 = 9;
        @ConstantName("团队积分")
        public static final Integer IN_10 = 10;
        @ConstantName("支付商家的钱")
        public static final Integer IN_11 = 211;
        @ConstantName("线下扫码返佣")
        public static final Integer IN_12 = 212;
        @ConstantName("删除待配送订单退配送费")
        public static final Integer IN_13 = 213;
        @ConstantName("提现审核驳回退余额")
        public static final Integer IN_14 = 214;
        @ConstantName("下单取消积分反还")
        public static final Integer IN_15 = 215;

        //支出
        @ConstantName("提现")
        public static final Integer OUT_1 = 13;
        @ConstantName("扫码收付款")
        public static final Integer OUT_2 = 10;
        @ConstantName("配送付佣")
        public static final Integer OUT_3 = 11;
        @ConstantName("订单支付")
        public static final Integer OUT_4 = 12;
        @ConstantName("订单支付使用积分抵扣")
        public static final Integer OUT_44 = 14;
        @ConstantName("退款")
        public static final Integer OUT_5 = 13;
    }

    /**
     * 交易明细中的收支类型
     */
    @ConstantName("交易明细中的收支类型")
    public static class IeType {
        @ConstantName("收入")
        public static final Integer IN = 1;
        @ConstantName("支出")
        public static final Integer OUT = -1;
    }

    /**
     * 交易所用的货币类型
     */
    @ConstantName("交易所用的货币类型")
    public static class Currency {
        @ConstantName("金钱")
        public static final Integer money = 0;
        @ConstantName("积分")
        public static final Integer integral = 1;
    }

    /**
     * 提现线下汇款状态
     */
    @ConstantName("提现下线汇款状态")
    public static class transferMoneyStatus {
        @ConstantName("待汇款")
        public static final Integer transfer_n = 0;
        @ConstantName("已汇款")
        public static final Integer transfer_y = 1;
    }

    /**
     * 配送订单，区分是个人订单还是商家订单
     */
    @ConstantName("配送订单，区分是个人订单还是商家订单")
    public static class SenderOrderType {
        @ConstantName("商家订单")
        public static final Integer SELLER = 1;
        @ConstantName("个人订单")
        public static final Integer PERSON = 2;
    }

    /**
     * 收款码、付款码类型
     * <p>
     * 收付码设置表，收款码没有金额的不会变，付款码一直会变；
     * 收款码：
     * HTTPS://QR.ALIPAY.COM/FKX066149VFLOLBLMYGAFA?t=1524478230223   收款码没钱
     * HTTPS://QR.ALIPAY.COM/FKX08897W5REBXN1UVWZ79 没有钱数
     * HTTPS://QR.ALIPAY.COM/FKX09208V04W3IVC6ZIY9B  有钱数目的
     * 条或者圆形付款码：
     * 281391261794383019
     * 281608651001314758
     * 288933948336047287
     */
    @ConstantName("收款码、付款码类型")
    public static class QrType {
        /**
         * 该项目预留
         * 付钱-条形码，类似支付宝的这种：281391261794383019、281608651001314758每隔几分钟就会变了，或者使用后就会变掉；281开头纯数字
         */
        @ConstantName("付钱-条形码")
        public static final Integer EXPENSES_QR_LINE_CODE = -1;
        /**
         * 付钱-圆形码,如：288933948336047287，288开头纯数字
         */
        @ConstantName("付钱-圆形码")
        public static final Integer EXPENSES_QR_CIRLE_CODE = -2;
        /**
         * 收钱-圆形码，设置的有钱，类似这种全部是字母：FKX09208V04W3IVC6ZIY9B有钱，没有设置钱：FKX08897W5REBXN1UVWZ79;没有设置钱,不会变
         */
        @ConstantName("收钱-圆形码，设置的有钱")
        public static final Integer INCOME_QR_CIRLE_CODE1 = 2;

        /**
         * @param val 不要写死的数字
         * @return
         */
        public static String getConstantName(int val) {
            String constantName = null;
            try {
                constantName = ReflectUtils.getContantName(QrType.class, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return constantName;
        }
    }

    /**
     * Distribution配送方式
     */
    @ConstantName("配送方式")
    public static class DistributionType {
        @ConstantName("配送到寝；店家—》寝室")
        public static final String D_TYPE1 = "10000";
        @ConstantName("配送到楼；店家—》 楼下")
        public static final String D_TYPE2 = "10001";
        @ConstantName("呼叫楼长；楼长从宿舍楼下送到宿舍楼里面")
        public static final String D_TYPE3 = "10002";
        @ConstantName("自取:  用户下单的时候选择的配送方式,用户自己到店家取货")
        public static final String D_TYPE4 = "10003";

        /**
         * 判断val是否是在(public static)常量中
         *
         * @param val 这里一般是死的数字
         * @return
         */
        public static boolean isConstantVal(String val) {
            return ReflectUtils.numberInClass(DistributionType.class, val);
        }
    }


    /**
     * 随机数长度产量, 项目里面用了的随机订单号码，全部在这里说明一下。
     *
     * @see RandomUtils#getRandomOrderNo
     * @see RandomUtils#getDeliveryOrderNo
     * @see RandomUtils#getRechargeOrderNo
     */
    public static class RandomContant {
        /**
         * 用户推荐CODE长度
         */
        public static int LENGTH_USER_RECOMMEND_CODE_STR_LENGTH = 9;
        /**
         * 收款二维码的长度
         */
        public static int LENGTH_MONEY_QR_LENGTH = 30;
        /**
         * 商品订单的长度
         */
        public static int LENGTH_ORDER_NORMAL_LENGTH = 10;
        /**
         * 充值订单的长度
         */
        public static int LENGTH_ORDER_RECHARGE_LENGTH = 12;
        public static String MONEY_QR_START = "xyh";
    }

     /* public static void main(String[] args) throws Exception {
        //获取常量上面的中文名称
        String name =  ReflectUtils.getContantName(Constant.Search_Share_FavType.class, Constant.Search_Share_FavType.SERVICE);
        //把一个常量类转出Map<常量值，常量的中文注释>
        Map<Integer, String> map = ReflectUtils.covetConstaintClassToMap(Constant.Search_Share_FavType.class);
    }*/

    /**
     * 小票机参数
     * 开放平台 http://www.mstching.com/home/open
     * 15805321233、tianchuang888
     * 连接设置小票打印机教程 https://blog.csdn.net/mstching/article/details/79643023
     */
    public static class TicketPrinterConfig {
        // 根域名
        public static String baseUrl = "http://www.open.mstching.com";
        // 申请的appkey
        public static String appId = "a21be02fab29";
        // 申请的appsecret
        public static String appSecret = "4174b0fab01e45e6cd41";
    }

    /**
     * 用户关系状态
     */
    @ConstantName("用户关系状态")
    public static class relationStatus {
        @ConstantName("没有上级、未醒")
        public static Integer relationStatus0 = 0;
        @ConstantName("没有上级、已醒")
        public static Integer relationStatus1 = 1;
        @ConstantName("有上级、未醒")
        public static Integer relationStatus2 = 2;
        @ConstantName("有上级、已醒")
        public static Integer relationStatus3 = 3;
    }
}
