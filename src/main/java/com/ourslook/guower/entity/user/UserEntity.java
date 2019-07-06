package com.ourslook.guower.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 用户表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:31:12
 */
@ApiModel(description = "用户表")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /**
     * 
     */
    @ApiModelProperty("")
    private Integer id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    /**@NotBlank(message = "用户名不能为空")*/
    /**@NotContainSpaces(message = "用户名不能包含空格")*/
    @Length(message = "用户名最长255个字符", max = 255)
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    /**@NotBlank(message = "密码不能为空")*/
    /**@NotContainSpaces(message = "密码不能包含空格")*/
    @Length(message = "密码最长255个字符", max = 255)
    private String password;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    /**@NotBlank(message = "手机号不能为空")*/
    /**@NotContainSpaces(message = "手机号不能包含空格")*/
    @Length(message = "手机号最长11个字符", max = 11)
    private String tel;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    /**@NotBlank(message = "昵称不能为空")*/
    /**@NotContainSpaces(message = "昵称不能包含空格")*/
    @Length(message = "昵称最长255个字符", max = 255)
    private String nickName;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    /**@NotBlank(message = "用户头像不能为空")*/
    /**@NotContainSpaces(message = "用户头像不能包含空格")*/
    @Length(message = "用户头像最长255个字符", max = 255)
    private String headPortrait;
    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Integer score;
    /**
     * 用户类型【1.app  2.web】
     */
    @ApiModelProperty("用户类型【1.app  2.web】")
    private Integer userType;
    /**
     * 用户等级【1.企业认证  2.作者认证  3.媒体认证】
     */
    @ApiModelProperty("用户等级【1.企业认证  2.作者认证  3.媒体认证】")
    private Integer userLevel;
    /**
     * 个性签名
     */
    @ApiModelProperty("个性签名")
    /**@NotBlank(message = "个性签名不能为空")*/
    /**@NotContainSpaces(message = "个性签名不能包含空格")*/
    @Length(message = "个性签名最长255个字符", max = 255)
    private String signature;
    /**
     * 性别【0.未知  1,男  2.女】
     */
    @ApiModelProperty("性别【0.未知  1,男  2.女】")
    private Integer sex;
    /**
     * 推送位置【1.专栏作者  2.企业专栏  3.作者排行】
     */
    @ApiModelProperty("推送位置【1.专栏作者  2.企业专栏  3.作者排行】")
    /**@NotBlank(message = "推送位置【1.专栏作者  2.企业专栏  3.作者排行】不能为空")*/
    /**@NotContainSpaces(message = "推送位置【1.专栏作者  2.企业专栏  3.作者排行】不能包含空格")*/
    @Length(message = "推送位置【1.专栏作者  2.企业专栏  3.作者排行】最长255个字符", max = 255)
    private String pushPosition;
    /**
     * 文章数
     */
    @ApiModelProperty("文章数")
    private Integer newsNumber;
    /**
     * 总浏览量
     */
    @ApiModelProperty("总浏览量")
    private Integer totalBrowsing;
    /**
     * 专栏作者排序值
     */
    @ApiModelProperty("专栏作者排序值")
    private Integer pushOneSort;
    /**
     * 企业专栏排序值
     */
    @ApiModelProperty("企业专栏排序值")
    private Integer pushTwoSort;
    /**
     * 作者排行排序值
     */
    @ApiModelProperty("作者排行排序值")
    private Integer pushThreeSort;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer state;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String userRemarks2;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String userRemarks3;

        public void setId(Integer id) {
            this.id = id;
    	}

        public Integer getId() {
		return id;
	}
        public void setUserName(String userName) {
            this.userName = userName != null ? (userName).trim() : null;
    	}

        public String getUserName() {
		return userName;
	}
        @JsonIgnore
        public void setPassword(String password) {
            this.password = password != null ? (password).trim() : null;
    	}

        public String getPassword() {
		return password;
	}
        public void setTel(String tel) {
            this.tel = tel != null ? (tel).trim() : null;
    	}

        public String getTel() {
		return tel;
	}
        public void setNickName(String nickName) {
            this.nickName = nickName != null ? (nickName).trim() : null;
    	}

        public String getNickName() {
		return nickName;
	}
        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait != null ? (headPortrait).trim() : null;
    	}

        public String getHeadPortrait() {
		return headPortrait;
	}
        public void setScore(Integer score) {
            this.score = score;
    	}

        public Integer getScore() {
		return score;
	}
        public void setUserType(Integer userType) {
            this.userType = userType;
    	}

        public Integer getUserType() {
		return userType;
	}
        public void setUserLevel(Integer userLevel) {
            this.userLevel = userLevel;
    	}

        public Integer getUserLevel() {
		return userLevel;
	}
        public void setSignature(String signature) {
            this.signature = signature != null ? (signature).trim() : null;
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
        public void setPushPosition(String pushPosition) {
            this.pushPosition = pushPosition != null ? (pushPosition).trim() : null;
    	}

        public String getPushPosition() {
		return pushPosition;
	}
        public void setNewsNumber(Integer newsNumber) {
            this.newsNumber = newsNumber;
    	}

        public Integer getNewsNumber() {
		return newsNumber;
	}
        public void setTotalBrowsing(Integer totalBrowsing) {
            this.totalBrowsing = totalBrowsing;
    	}

        public Integer getTotalBrowsing() {
		return totalBrowsing;
	}
        public void setPushOneSort(Integer pushOneSort) {
            this.pushOneSort = pushOneSort;
    	}

        public Integer getPushOneSort() {
		return pushOneSort;
	}
        public void setPushTwoSort(Integer pushTwoSort) {
            this.pushTwoSort = pushTwoSort;
    	}

        public Integer getPushTwoSort() {
		return pushTwoSort;
	}
        public void setPushThreeSort(Integer pushThreeSort) {
            this.pushThreeSort = pushThreeSort;
    	}

        public Integer getPushThreeSort() {
		return pushThreeSort;
	}
        public void setState(Integer state) {
            this.state = state;
    	}

        public Integer getState() {
		return state;
	}

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setUserRemarks2(String userRemarks2) {
            this.userRemarks2 = userRemarks2 != null ? (userRemarks2).trim() : null;
    	}

        public String getUserRemarks2() {
		return userRemarks2;
	}
        public void setUserRemarks3(String userRemarks3) {
            this.userRemarks3 = userRemarks3 != null ? (userRemarks3).trim() : null;
    	}

        public String getUserRemarks3() {
		return userRemarks3;
	}
}
