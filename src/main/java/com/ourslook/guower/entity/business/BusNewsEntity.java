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
 * 新闻文章表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@ApiModel(description = "新闻文章表")
public class BusNewsEntity implements Serializable {
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
     * 文章标签【2.NEW 1.HOT 0.无标签】
     */
    @ApiModelProperty("文章标签【2.NEW 1.HOT 0.无标签】")
    private Integer tag;
    /**
     * 内容标签1
     */
    @ApiModelProperty("内容标签1")
    /**@NotBlank(message = "内容标签1不能为空")*/
    /**@NotContainSpaces(message = "内容标签1不能包含空格")*/
    @Length(message = "内容标签1最长255个字符", max = 255)
    private String tag1;
    /**
     * 内容标签2
     */
    @ApiModelProperty("内容标签2")
    /**@NotBlank(message = "内容标签2不能为空")*/
    /**@NotContainSpaces(message = "内容标签2不能包含空格")*/
    @Length(message = "内容标签2最长255个字符", max = 255)
    private String tag2;
    /**
     * 内容标签3
     */
    @ApiModelProperty("内容标签3")
    /**@NotBlank(message = "内容标签3不能为空")*/
    /**@NotContainSpaces(message = "内容标签3不能包含空格")*/
    @Length(message = "内容标签3最长255个字符", max = 255)
    private String tag3;
    /**
     * 正文【详情】
     */
    @ApiModelProperty("正文【详情】")
    /**@NotBlank(message = "正文【详情】不能为空")*/
    /**@NotContainSpaces(message = "正文【详情】不能包含空格")*/
//    @Length(message = "正文【详情】最长65535个字符", max = 65535)
    private String mainText;
    /**
     * 文章类型
     */
    @ApiModelProperty("文章类型")
    /**@NotBlank(message = "文章类型不能为空")*/
    /**@NotContainSpaces(message = "文章类型不能包含空格")*/
    @Length(message = "文章类型最长255个字符", max = 255)
    private String newsType;
    /**
     * 阅读量
     */
    @ApiModelProperty("阅读量")
    private Integer lookTimes;
    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private Integer author;
    /**
     * 责任编辑
     */
    @ApiModelProperty("责任编辑")
    /**@NotBlank(message = "责任编辑不能为空")*/
    /**@NotContainSpaces(message = "责任编辑不能包含空格")*/
    @Length(message = "责任编辑最长255个字符", max = 255)
    private String responsibleEditor;
    /**
     * 发布类型【1.后台 2.作者】
     */
    @ApiModelProperty("发布类型【1.后台 2.作者】")
    private Integer releaseType;
    /**
     * 发布人
     */
    @ApiModelProperty("发布人")
    private Integer releaseUserId;
    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    private LocalDateTime releaseDate;
    /**
     * 是否为广告【1.广告  0.非广告】
     */
    @ApiModelProperty("是否为广告【1.广告  0.非广告】")
    private Integer isAdvertisement;
    /**
     * 是否上热点【1.热点  0.非热点】
     */
    @ApiModelProperty("是否上热点【1.热点  0.非热点】")
    private Integer isHotspot;
    /**
     * 是否发布【1.发布 0.未发布】
     */
    @ApiModelProperty("暂留")
    private Integer isRelease;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String newsRemarks2;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String newsRemarks3;
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
    /**
     * 审核类型【2.未通过 0.审核中 1.通过】
     */
    @ApiModelProperty("审核类型【2.未通过 0.审核中 1.通过】")
    private Integer examineType;

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
        public void setTag(Integer tag) {
            this.tag = tag;
    	}

        public Integer getTag() {
		return tag;
	}
        public void setTag1(String tag1) {
            this.tag1 = tag1 != null ? (tag1).trim() : null;
    	}

        public String getTag1() {
		return tag1;
	}
        public void setTag2(String tag2) {
            this.tag2 = tag2 != null ? (tag2).trim() : null;
    	}

        public String getTag2() {
		return tag2;
	}
        public void setTag3(String tag3) {
            this.tag3 = tag3 != null ? (tag3).trim() : null;
    	}

        public String getTag3() {
		return tag3;
	}
        public void setMainText(String mainText) {
            this.mainText = mainText != null ? (mainText).trim() : null;
    	}

        public String getMainText() {
		return mainText;
	}
        public void setNewsType(String newsType) {
            this.newsType = newsType != null ? (newsType).trim() : null;
    	}

        public String getNewsType() {
		return newsType;
	}
        public void setLookTimes(Integer lookTimes) {
            this.lookTimes = lookTimes;
    	}

        public Integer getLookTimes() {
		return lookTimes;
	}
        public void setAuthor(Integer author) {
            this.author = author;
    	}

        public Integer getAuthor() {
		return author;
	}
        public void setResponsibleEditor(String responsibleEditor) {
            this.responsibleEditor = responsibleEditor != null ? (responsibleEditor).trim() : null;
    	}

        public String getResponsibleEditor() {
		return responsibleEditor;
	}
        public void setReleaseType(Integer releaseType) {
            this.releaseType = releaseType;
    	}

        public Integer getReleaseType() {
		return releaseType;
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
        public void setIsAdvertisement(Integer isAdvertisement) {
            this.isAdvertisement = isAdvertisement;
    	}

        public Integer getIsAdvertisement() {
		return isAdvertisement;
	}
        public void setIsHotspot(Integer isHotspot) {
            this.isHotspot = isHotspot;
    	}

        public Integer getIsHotspot() {
		return isHotspot;
	}

        public void setNewsRemarks2(String newsRemarks2) {
            this.newsRemarks2 = newsRemarks2 != null ? (newsRemarks2).trim() : null;
    	}

        public String getNewsRemarks2() {
		return newsRemarks2;
	}
        public void setNewsRemarks3(String newsRemarks3) {
            this.newsRemarks3 = newsRemarks3 != null ? (newsRemarks3).trim() : null;
    	}

        public String getNewsRemarks3() {
		return newsRemarks3;
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
        public void setExamineType(Integer examineType) {
            this.examineType = examineType;
    	}

        public Integer getExamineType() {
		return examineType;
	}


    public Integer getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(Integer isRelease) {
        this.isRelease = isRelease;
    }
}
