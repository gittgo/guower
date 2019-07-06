package com.ourslook.guower.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ourslook.guower.utils.jackson.CustomPhotoImageSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 审核表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
@ApiModel(description = "审核表")
public class InfExamineEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String userNickName;
    /**
     * 认证类型
     */
    @ApiModelProperty("认证类型")
    private Integer userType;
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    /**@NotBlank(message = "真实姓名不能为空")*/
    /**@NotContainSpaces(message = "真实姓名不能包含空格")*/
    @Length(message = "真实姓名最长255个字符", max = 255)
    private String userName;
    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    /**@NotBlank(message = "身份证号不能为空")*/
    /**@NotContainSpaces(message = "身份证号不能包含空格")*/
    @Length(message = "身份证号最长255个字符", max = 255)
    private String userIdCard;
    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    /**@NotBlank(message = "手机号码不能为空")*/
    /**@NotContainSpaces(message = "手机号码不能包含空格")*/
    @Length(message = "手机号码最长255个字符", max = 255)
    private String userTel;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    /**@NotBlank(message = "邮箱不能为空")*/
    /**@NotContainSpaces(message = "邮箱不能包含空格")*/
    @Length(message = "邮箱最长255个字符", max = 255)
    private String userEmail;
    /**
     * 证件照
     */
    @ApiModelProperty("证件照")
    /**@NotBlank(message = "证件照不能为空")*/
    /**@NotContainSpaces(message = "证件照不能包含空格")*/
    @Length(message = "证件照最长255个字符", max = 255)
    private String userCertificatesImage;
    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    /**@NotBlank(message = "企业名称不能为空")*/
    /**@NotContainSpaces(message = "企业名称不能包含空格")*/
    @Length(message = "企业名称最长255个字符", max = 255)
    private String enterpriseName;
    /**
     * 企业证件号
     */
    @ApiModelProperty("企业证件号")
    /**@NotBlank(message = "企业证件号不能为空")*/
    /**@NotContainSpaces(message = "企业证件号不能包含空格")*/
    @Length(message = "企业证件号最长255个字符", max = 255)
    private String enterpriseIdCard;
    /**
     * 营业执照
     */
    @ApiModelProperty("营业执照")
    /**@NotBlank(message = "营业执照不能为空")*/
    /**@NotContainSpaces(message = "营业执照不能包含空格")*/
    @Length(message = "营业执照最长255个字符", max = 255)
    private String enterpriseImage;
    /**
     * 审核结果【1.通过  0.审核中  2.未通过】
     */
    @ApiModelProperty("审核结果【1.通过  0.审核中  2.未通过】")
    private Integer result;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;
    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    private Integer sysUserId;
    /**
     * 审核人名称
     */
    @ApiModelProperty("审核人名称")
    private String sysUserName;
    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private LocalDateTime examineDate;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserName(String userName) {
        this.userName = userName != null ? (userName).trim() : null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserIdCard(String userIdCard) {
        this.userIdCard = userIdCard != null ? (userIdCard).trim() : null;
    }

    public String getUserIdCard() {
        return userIdCard;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel != null ? (userTel).trim() : null;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail != null ? (userEmail).trim() : null;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserCertificatesImage(String userCertificatesImage) {
        this.userCertificatesImage = userCertificatesImage != null ? (userCertificatesImage).trim() : null;
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getUserCertificatesImage() {
        return userCertificatesImage;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName != null ? (enterpriseName).trim() : null;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseIdCard(String enterpriseIdCard) {
        this.enterpriseIdCard = enterpriseIdCard != null ? (enterpriseIdCard).trim() : null;
    }

    public String getEnterpriseIdCard() {
        return enterpriseIdCard;
    }

    public void setEnterpriseImage(String enterpriseImage) {
        this.enterpriseImage = enterpriseImage != null ? (enterpriseImage).trim() : null;
    }

    @JsonSerialize(using = CustomPhotoImageSerialize.class)
    public String getEnterpriseImage() {
        return enterpriseImage;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    public LocalDateTime getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(LocalDateTime examineDate) {
        this.examineDate = examineDate;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

