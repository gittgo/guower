package com.ourslook.guower.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ourslook.guower.utils.jackson.CustomPhotoImageSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;




/**
 * 广告表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@ApiModel(description = "广告表")
public class BusAdvertisementEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Integer id;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    /**@NotBlank(message = "标题不能为空")*/
    /**@NotContainSpaces(message = "标题不能包含空格")*/
    @Length(message = "标题最长255个字符", max = 255)
    private String title;
    /**
     * 副标题【简介】
     */
    @ApiModelProperty("副标题【简介】")
    /**@NotBlank(message = "副标题【简介】不能为空")*/
    /**@NotContainSpaces(message = "副标题【简介】不能包含空格")*/
    @Length(message = "副标题【简介】最长255个字符", max = 255)
    private String smallTitle;
    /**
     * 大图标
     */
    @ApiModelProperty("大图标")
    /**@NotBlank(message = "大图标不能为空")*/
    /**@NotContainSpaces(message = "大图标不能包含空格")*/
    @Length(message = "大图标最长255个字符", max = 255)
    private String image;
    /**
     * 小图标
     */
    @ApiModelProperty("小图标")
    /**@NotBlank(message = "小图标不能为空")*/
    /**@NotContainSpaces(message = "小图标不能包含空格")*/
    @Length(message = "小图标最长255个字符", max = 255)
    private String smallImage;
    /**
     * 正文【详情】
     */
    @ApiModelProperty("正文【详情】")
    /**@NotBlank(message = "正文【详情】不能为空")*/
    /**@NotContainSpaces(message = "正文【详情】不能包含空格")*/
    @Length(message = "正文【详情】最长65535个字符", max = 65535)
    private String mainText;
    /**
     * 广告类型
     */
    @ApiModelProperty("广告类型")
    /**@NotBlank(message = "广告类型不能为空")*/
    /**@NotContainSpaces(message = "广告类型不能包含空格")*/
    @Length(message = "广告类型最长255个字符", max = 255)
    private String advertisementType;
    /**
     * 阅读量
     */
    @ApiModelProperty("阅读量")
    private Integer lookTimes;
    /**
     * 发布人
     */
    @ApiModelProperty("发布人")
    private Integer releaseUserId;
    /**
     * 发布人名称
     */
    @ApiModelProperty("发布人名称")
    private String releaseUserName;
    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    private LocalDateTime releaseDate;
    /**
     * 跳转方式【1.链接  2.视频  3.富文本  4.新闻文章】
     */
    @ApiModelProperty("跳转方式【1.链接  2.视频  3.富文本  4.新闻文章】")
    private Integer jumpType;
    /**
     * 跳转地址
     */
    @ApiModelProperty("跳转地址")
    /**@NotBlank(message = "跳转地址不能为空")*/
    /**@NotContainSpaces(message = "跳转地址不能包含空格")*/
    @Length(message = "跳转地址最长255个字符", max = 255)
    private String jumpUrl;
    /**
     * 跳转新闻文章id
     */
    @ApiModelProperty("跳转新闻文章id")
    private Integer jumpNewsId;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String advertisemenRemarks1;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String advertisemenRemarks2;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String advertisemenRemarks3;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * 排序到期时间
     */
    @ApiModelProperty("排序到期时间")
    private LocalDateTime sortTime;

        public void setId(Integer id) {
            this.id = id;
    	}

        public Integer getId() {
		return id;
	}
        public void setTitle(String title) {
            this.title = title != null ? (title).trim() : null;
    	}

        public String getTitle() {
		return title;
	}
        public void setSmallTitle(String smallTitle) {
            this.smallTitle = smallTitle != null ? (smallTitle).trim() : null;
    	}

        public String getSmallTitle() {
		return smallTitle;
	}
        public void setImage(String image) {
            this.image = image != null ? (image).trim() : null;
    	}

        @JsonSerialize(using = CustomPhotoImageSerialize.class)
        public String getImage() {
		return image;
	}
        public void setSmallImage(String smallImage) {
            this.smallImage = smallImage != null ? (smallImage).trim() : null;
    	}

        @JsonSerialize(using = CustomPhotoImageSerialize.class)
        public String getSmallImage() {
		return smallImage;
	}
        public void setMainText(String mainText) {
            this.mainText = mainText != null ? (mainText).trim() : null;
    	}

        public String getMainText() {
		return mainText;
	}
        public void setAdvertisementType(String advertisementType) {
            this.advertisementType = advertisementType != null ? (advertisementType).trim() : null;
    	}

        public String getAdvertisementType() {
		return advertisementType;
	}
        public void setLookTimes(Integer lookTimes) {
            this.lookTimes = lookTimes;
    	}

        public Integer getLookTimes() {
		return lookTimes;
	}
        public void setReleaseUserId(Integer releaseUserId) {
            this.releaseUserId = releaseUserId;
    	}

        public Integer getReleaseUserId() {
		return releaseUserId;
	}
        public void setReleaseDate(LocalDateTime releaseDate) {
            this.releaseDate = releaseDate;
    	}

        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
        public LocalDateTime getReleaseDate() {
		return releaseDate;
	}
        public void setJumpType(Integer jumpType) {
            this.jumpType = jumpType;
    	}

        public Integer getJumpType() {
		return jumpType;
	}
        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl != null ? (jumpUrl).trim() : null;
    	}

        public String getJumpUrl() {
		return jumpUrl;
	}
        public void setJumpNewsId(Integer jumpNewsId) {
            this.jumpNewsId = jumpNewsId;
    	}

        public Integer getJumpNewsId() {
		return jumpNewsId;
	}
        public void setAdvertisemenRemarks1(String advertisemenRemarks1) {
            this.advertisemenRemarks1 = advertisemenRemarks1 != null ? (advertisemenRemarks1).trim() : null;
    	}

        public String getAdvertisemenRemarks1() {
		return advertisemenRemarks1;
	}
        public void setAdvertisemenRemarks2(String advertisemenRemarks2) {
            this.advertisemenRemarks2 = advertisemenRemarks2 != null ? (advertisemenRemarks2).trim() : null;
    	}

        public String getAdvertisemenRemarks2() {
		return advertisemenRemarks2;
	}
        public void setAdvertisemenRemarks3(String advertisemenRemarks3) {
            this.advertisemenRemarks3 = advertisemenRemarks3 != null ? (advertisemenRemarks3).trim() : null;
    	}

        public String getAdvertisemenRemarks3() {
		return advertisemenRemarks3;
	}
        public void setSort(Integer sort) {
            this.sort = sort;
    	}

        public Integer getSort() {
		return sort;
	}
        public void setSortTime(LocalDateTime sortTime) {
            this.sortTime = sortTime;
    	}

        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
        public LocalDateTime getSortTime() {
		return sortTime;
	}

        public String getReleaseUserName() {
            return releaseUserName;
        }

        public void setReleaseUserName(String releaseUserName) {
            this.releaseUserName = releaseUserName;
        }
}
