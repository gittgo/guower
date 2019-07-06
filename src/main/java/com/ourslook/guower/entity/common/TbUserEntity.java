package com.ourslook.guower.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ourslook.guower.utils.jackson.CustomDoubleSerialize;
import com.ourslook.guower.utils.jackson.CustomPhotoImageSerialize;
import com.ourslook.guower.utils.validator.constraints.NotContainSpaces;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 用户表
 *
 * @JsonIgnore json忽略
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-04-25 14:24:52
 */
@ApiModel(description = "用户表")
public class TbUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long userid;
    /**
     * 用户名、昵称
     */
    @ApiModelProperty("用户名、昵称")
    @NotContainSpaces(message = "用户名不能包含空格")
    @Length(message = "用户名最长50个字符", max = 50)
    private String username;
    /**
     * 用户真实姓名
     */
    @ApiModelProperty("用户真实姓名")
    @NotContainSpaces(message = "用户真实姓名不能包含空格")
    @Length(message = "用户真实姓名最长20个字符", max = 20)
    private String name;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotContainSpaces(message = "密码不能包含空格")
    @Length(message = "密码最长200个字符", max = 200)
    private String password;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @NotContainSpaces(message = "手机号不能包含空格")
    @Length(message = "手机号最长11个字符", max = 11)
    private String mobile;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    @NotContainSpaces(message = "用户头像不能包含空格")
    @Length(message = "用户头像最长200个字符", max = 200)
    private String headportraitimg;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createtime;
    /**
     * 个性签名
     */
    @ApiModelProperty("个性签名")
    @NotContainSpaces(message = "个性签名不能包含空格")
    @Length(message = "个性签名最长50个字符", max = 50)
    private String signature;
    /**
     * 性别，0保密、1男、2女
     */
    @ApiModelProperty("性别，0保密、1男、2女")
    private Integer sex;
    /**
     * QQ登录账号
     */
    @ApiModelProperty("QQ登录账号")
    @NotContainSpaces(message = "QQ登录账号不能包含空格")
    @Length(message = "QQ登录账号最长100个字符", max = 100)
    private String qqacc;
    /**
     * 微信登录账号
     */
    @ApiModelProperty("微信登录账号")
    @NotContainSpaces(message = "微信登录账号不能包含空格")
    @Length(message = "微信登录账号最长100个字符", max = 100)
    private String weichatacc;
    /**
     * 新浪微信账号
     */
    @ApiModelProperty("新浪微信账号")
    @NotContainSpaces(message = "新浪微信账号不能包含空格")
    @Length(message = "新浪微信账号最长100个字符", max = 100)
    private String sinaacc;
    /**
     * 账户余额，最新的值，要每次去交易表查询最新的数据
     */
    @ApiModelProperty("账户余额，最新的值，要每次去交易表BssIncomeExpensesEntity查询最新的数据")
    private BigDecimal remaindermoney = BigDecimal.ZERO;
    /**
     * 会员编码，可做自己的体检码制
     */
    @ApiModelProperty("会员编码，可做自己的体检码制")
    @NotContainSpaces(message = "会员编码，可做自己的体检码制不能包含空格")
    @Length(message = "会员编码，可做自己的体检码制最长100个字符", max = 100)
    private String usercode;
    /**
     * 上级的推荐码
     */
    @ApiModelProperty("上级的推荐码")
    @NotContainSpaces(message = "上级的推荐码不能包含空格")
    @Length(message = "上级的推荐码最长100个字符", max = 100)
    private String recommendcode;
    /**
     * 用户认证类型(0,未认证，1：已经认证)
     */
    @ApiModelProperty("用户认证类型(0,未认证，1：已经认证)")
    private Integer identification;
    /**
     * 身份证正面照
     */
    @ApiModelProperty("身份证正面照")
    @NotContainSpaces(message = "身份证正面照不能包含空格")
    @Length(message = "身份证正面照最长300个字符", max = 300)
    private String idcardimg1;
    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    @NotContainSpaces(message = "身份证号码不能包含空格")
    @Length(message = "身份证号码最长18个字符", max = 18)
    private String idcard;
    /**
     * 身份证反面照
     */
    @ApiModelProperty("身份证反面照")
    @NotContainSpaces(message = "身份证反面照不能包含空格")
    @Length(message = "身份证反面照最长300个字符", max = 300)
    private String idcardimg2;
    /**
     * 组织机构代码证照片
     */
    @ApiModelProperty("组织机构代码证照片")
    @NotContainSpaces(message = "组织机构代码证照片不能包含空格")
    @Length(message = "组织机构代码证照片最长300个字符", max = 300)
    private String organizationimg2;
    /**
     * 营业执照照片
     */
    @ApiModelProperty("营业执照照片")
    @NotContainSpaces(message = "营业执照照片不能包含空格")
    @Length(message = "营业执照照片最长300个字符", max = 300)
    private String businesslicenseimg;
    /**
     * 税务登记证照片
     */
    @ApiModelProperty("税务登记证照片")
    @NotContainSpaces(message = "税务登记证照片不能包含空格")
    @Length(message = "税务登记证照片最长300个字符", max = 300)
    private String taxationimg;
    /**
     */
    @ApiModelProperty("学校ID ")
    @NotContainSpaces(message = "学校ID不能包含空格")
    @Length(message = "学校ID最长255个字符", max = 255)
    private String school;
    /**
     * 宿舍楼寝室号
     */
    @ApiModelProperty("宿舍楼寝室号")
    @NotContainSpaces(message = "宿舍楼寝室号不能包含空格")
    @Length(message = "宿舍楼寝室号最长255个字符", max = 255)
    private String studyHotelRoomNum;
    /**
     * 支付密码
     */
    @ApiModelProperty("支付密码")
    @NotContainSpaces(message = "支付密码不能包含空格")
    @Length(message = "支付密码最长200个字符", max = 200)
    private String payPassword;
    /**
     * 宿舍楼id
     */
    @ApiModelProperty("宿舍楼id")
    @NotContainSpaces(message = "宿舍楼不能包含空格")
    @Length(message = "宿舍楼最长255个字符", max = 255)
    private String studyHotel;

    /**
     * 宿舍楼
     */
    @ApiModelProperty("宿舍楼名称")
    @NotContainSpaces(message = "宿舍楼不能包含空格")
    @Length(message = "宿舍楼最长255个字符", max = 255)
    private String studyHotelName;
    /**
     * 专业
     */
    @ApiModelProperty("专业")
    @NotContainSpaces(message = "专业不能包含空格")
    @Length(message = "专业最长255个字符", max = 255)
    private String professional;
    /**
     * 用户提现使用的支付宝账号
     */
    @ApiModelProperty("用户提现使用的支付宝账号")
    @NotContainSpaces(message = "用户提现使用的支付宝账号不能包含空格")
    @Length(message = "用户提现使用的支付宝账号最长255个字符", max = 255)
    private String alipayAcount;
    /**
     * 预留，类似支付宝的收款码，有金额的每次都会变化，没金额的不变；HTTPS://QR.ALIPAY.COM/FKX08897W5REBXN1UVWZ79、HTTPS://QR.ALIPAY.COM/FKX09208V04W3IVC6ZIY9B
     */
    @ApiModelProperty("预留，类似支付宝的收款码，有金额的每次都会变化，没金额的不变；HTTPS://QR.ALIPAY.COM/FKX08897W5REBXN1UVWZ79、HTTPS://QR.ALIPAY.COM/FKX09208V04W3IVC6ZIY9B")
    private String alipayQrIn;
    /**
     * 类似支付宝动态的付款码，一般要用机器扫描
     */
    @ApiModelProperty("类似支付宝动态的付款码，一般要用机器扫描")
    @NotContainSpaces(message = "类似支付宝动态的付款码，一般要用机器扫描不能包含空格")
    @Length(message = "类似支付宝动态的付款码，一般要用机器扫描最长200个字符", max = 200)
    private String alipayQrOutDynamic;
    /**
     * 类似支付宝静态二维码，一般用于张贴，可以直接用手机扫
     */
    @ApiModelProperty("类似支付宝静态二维码，一般用于张贴，可以直接用手机扫")
    @NotContainSpaces(message = "类似支付宝静态二维码，一般用于张贴，可以直接用手机扫不能包含空格")
    @Length(message = "类似支付宝静态二维码，一般用于张贴，可以直接用手机扫最长200个字符", max = 200)
    private String alipayQrOutStatic;
    /**
     * 接单提示声音开关； 1,0
     */
    @ApiModelProperty("接单提示声音开关; 1,0")
    private Integer switchVoiceOrder;
    /**
     * 二维码收款提示声音开关; 1,0
     */
    @ApiModelProperty("二维码收款提示声音开关; 1,0")
    private Integer switchVoiceInQr;
    /**
     * @see com.ourslook.guower.utils.Constant.UserTypes
     * 用户类型，参常量：UserTypes；1：商家用户21：普通用户；22：普通用户-负责人团队；23：普通用户-合伙人团队；24:普通用户-楼栋长团队
     */
    @ApiModelProperty("用户类型，参常量：UserTypes；1：商家用户21：普通用户；22：普通用户-负责人团队；23：普通用户-合伙人团队；24:普通用户-楼栋长团队")
    private Integer userTypes;
    /**
     * 班级
     */
    @ApiModelProperty("班级")
    @NotContainSpaces(message = "班级不能包含空格")
    @Length(message = "班级最长200个字符", max = 200)
    private String extends1;
    /**
     * 扩展字段2-扩展字段2-账户积分，这里是用户积分,必须是整数； 具体数据要每次去交易表去最新的数据。
     */
    @ApiModelProperty("扩展字段2-账户积分，这里是用户积分,必须是整数；每次使用要去账号收支明细表查询最新的数据更细到用户信息")
    private BigDecimal extends2 = BigDecimal.ZERO;
    /**
     * 校区
     */
    @ApiModelProperty("校区")
    private String extends3;
    /**
     * 扩展字段4
     */
    @ApiModelProperty("扩展字段4")
    @NotContainSpaces(message = "扩展字段4不能包含空格")
    @Length(message = "扩展字段4最长200个字符", max = 200)
    private String extends4;
    /**
     * 扩展字段5
     */
    @ApiModelProperty("扩展字段5")
    @NotContainSpaces(message = "扩展字段5不能包含空格")
    @Length(message = "扩展字段5最长200个字符", max = 200)
    private String extends5;
    /**
     * 扩展字段6
     */
    @ApiModelProperty("扩展字段6")
    @NotContainSpaces(message = "扩展字段6不能包含空格")
    @Length(message = "扩展字段6最长200个字符", max = 200)
    private String extends6;
    /**
     * 扩展字段7
     */
    @ApiModelProperty("扩展字段7")
    @NotContainSpaces(message = "扩展字段7不能包含空格")
    @Length(message = "扩展字段7最长200个字符", max = 200)
    private String extends7;
    /**
     * 扩展字段8
     */
    @ApiModelProperty("扩展字段8")
    @NotContainSpaces(message = "扩展字段8不能包含空格")
    @Length(message = "扩展字段8最长200个字符", max = 200)
    private String extends8;

    /**
     * 关系状态
     *
     * 0没有上级、未被唤醒、
     * 1没有上级、已唤醒、
     * 2有上级、未被唤醒、
     * 3有上级、已唤醒
     */
    @ApiModelProperty("关系状态")
    @NotContainSpaces(message = "关系状态不能包含空格")
    @Length(message = "关系状态最长50个字符", max = 50)
    private String relationStatus;

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUsername(String username) {
        this.username = (username != null ? username.trim() : null);
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = (password != null ? password.trim() : null);
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = (name != null ? name.trim() : null);
    }

    public String getName() {
        return name;
    }

    public void setMobile(String mobile) {
        this.mobile = (mobile != null ? mobile.trim() : null);
    }

    public String getMobile() {
        return mobile;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setHeadportraitimg(String headportraitimg) {
        this.headportraitimg = (headportraitimg != null ? headportraitimg.trim() : null);
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getHeadportraitimg() {
        return headportraitimg;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setSignature(String signature) {
        this.signature = (signature != null ? signature.trim() : null);
    }

    public String getSignature() {
        return signature;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }

    public void setQqacc(String qqacc) {
        this.qqacc = (qqacc != null ? qqacc.trim() : null);
    }

    public String getQqacc() {
        return qqacc;
    }

    public void setWeichatacc(String weichatacc) {
        this.weichatacc = (weichatacc != null ? weichatacc.trim() : null);
    }

    public String getWeichatacc() {
        return weichatacc;
    }

    public void setSinaacc(String sinaacc) {
        this.sinaacc = (sinaacc != null ? sinaacc.trim() : null);
    }

    public String getSinaacc() {
        return sinaacc;
    }

    public void setRemaindermoney(BigDecimal remaindermoney) {
        this.remaindermoney = remaindermoney;
    }

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public BigDecimal getRemaindermoney() {
        return remaindermoney;
    }

    public void setUsercode(String usercode) {
        this.usercode = (usercode != null ? usercode.trim() : null);
    }

    public String getUsercode() {
        return usercode;
    }

    public void setRecommendcode(String recommendcode) {
        this.recommendcode = (recommendcode != null ? recommendcode.trim() : null);
    }

    public String getRecommendcode() {
        return recommendcode;
    }

    public void setIdentification(Integer identification) {
        this.identification = identification;
    }

    public Integer getIdentification() {
        return identification;
    }

    public void setIdcardimg1(String idcardimg1) {
        this.idcardimg1 = (idcardimg1 != null ? idcardimg1.trim() : null);
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getIdcardimg1() {
        return idcardimg1;
    }

    public void setIdcard(String idcard) {
        this.idcard = (idcard != null ? idcard.trim() : null);
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcardimg2(String idcardimg2) {
        this.idcardimg2 = (idcardimg2 != null ? idcardimg2.trim() : null);
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getIdcardimg2() {
        return idcardimg2;
    }

    public void setOrganizationimg2(String organizationimg2) {
        this.organizationimg2 = (organizationimg2 != null ? organizationimg2.trim() : null);
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getOrganizationimg2() {
        return organizationimg2;
    }

    public void setBusinesslicenseimg(String businesslicenseimg) {
        this.businesslicenseimg = (businesslicenseimg != null ? businesslicenseimg.trim() : null);
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getBusinesslicenseimg() {
        return businesslicenseimg;
    }

    public void setTaxationimg(String taxationimg) {
        this.taxationimg = (taxationimg != null ? taxationimg.trim() : null);
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getTaxationimg() {
        return taxationimg;
    }

    public void setSchool(String school) {
        this.school = (school != null ? school.trim() : null);
    }

    public String getSchool() {
        return school;
    }

    public void setStudyHotelRoomNum(String studyHotelRoomNum) {
        this.studyHotelRoomNum = (studyHotelRoomNum != null ? studyHotelRoomNum.trim() : null);
    }

    public String getStudyHotelRoomNum() {
        return studyHotelRoomNum;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = (payPassword != null ? payPassword.trim() : null);
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setStudyHotel(String studyHotel) {
        this.studyHotel = (studyHotel != null ? studyHotel.trim() : null);
    }

    public String getStudyHotel() {
        return studyHotel;
    }

    public void setProfessional(String professional) {
        this.professional = (professional != null ? professional.trim() : null);
    }

    public String getProfessional() {
        return professional;
    }

    public void setAlipayAcount(String alipayAcount) {
        this.alipayAcount = (alipayAcount != null ? alipayAcount.trim() : null);
    }

    public String getAlipayAcount() {
        return alipayAcount;
    }

    public void setAlipayQrIn(String alipayQrIn) {
        this.alipayQrIn = (alipayQrIn != null ? alipayQrIn.trim() : null);
    }

    public String getAlipayQrIn() {
        return alipayQrIn;
    }

    public void setAlipayQrOutDynamic(String alipayQrOutDynamic) {
        this.alipayQrOutDynamic = (alipayQrOutDynamic != null ? alipayQrOutDynamic.trim() : null);
    }

    public String getAlipayQrOutDynamic() {
        return alipayQrOutDynamic;
    }

    public void setAlipayQrOutStatic(String alipayQrOutStatic) {
        this.alipayQrOutStatic = (alipayQrOutStatic != null ? alipayQrOutStatic.trim() : null);
    }

    public String getAlipayQrOutStatic() {
        return alipayQrOutStatic;
    }

    public void setSwitchVoiceOrder(Integer switchVoiceOrder) {
        this.switchVoiceOrder = switchVoiceOrder;
    }

    public Integer getSwitchVoiceOrder() {
        return switchVoiceOrder;
    }

    public void setSwitchVoiceInQr(Integer switchVoiceInQr) {
        this.switchVoiceInQr = switchVoiceInQr;
    }

    public Integer getSwitchVoiceInQr() {
        return switchVoiceInQr;
    }

    public void setUserTypes(Integer userTypes) {
        this.userTypes = userTypes;
    }

    public Integer getUserTypes() {
        return userTypes;
    }

    public void setExtends1(String extends1) {
        this.extends1 = (extends1 != null ? extends1.trim() : null);
    }

    public String getExtends1() {
        return extends1;
    }

    public void setExtends2(BigDecimal extends2) {
        this.extends2 = extends2;
    }

    public BigDecimal getExtends2() {
        if (extends2 == null) {
            extends2 = BigDecimal.ZERO;
        }
        return extends2;
    }

    public void setExtends3(String extends3) {
        this.extends3 = (extends3 != null ? extends3.trim() : null);
    }

    public String getExtends3() {
        return extends3;
    }

    public void setExtends4(String extends4) {
        this.extends4 = (extends4 != null ? extends4.trim() : null);
    }

    public String getExtends4() {
        return extends4;
    }

    public void setExtends5(String extends5) {
        this.extends5 = (extends5 != null ? extends5.trim() : null);
    }

    public String getExtends5() {
        return extends5;
    }

    public void setExtends6(String extends6) {
        this.extends6 = (extends6 != null ? extends6.trim() : null);
    }

    public String getExtends6() {
        return extends6;
    }

    public void setExtends7(String extends7) {
        this.extends7 = (extends7 != null ? extends7.trim() : null);
    }

    public String getExtends7() {
        return extends7;
    }

    public void setExtends8(String extends8) {
        this.extends8 = (extends8 != null ? extends8.trim() : null);
    }

    public String getExtends8() {
        return extends8;
    }

    public String getStudyHotelName() {
        return studyHotelName;
    }

    public void setStudyHotelName(String studyHotelName) {
        this.studyHotelName = studyHotelName;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }
}
