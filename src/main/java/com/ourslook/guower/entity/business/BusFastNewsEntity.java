package com.ourslook.guower.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ourslook.guower.utils.result.XaResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * 快报表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@ApiModel(description = "快报表")
public class BusFastNewsEntity implements Serializable {
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
     * 正文【详情】
     */
    @ApiModelProperty("正文【详情】")
    /**@NotBlank(message = "正文【详情】不能为空")*/
    /**@NotContainSpaces(message = "正文【详情】不能包含空格")*/
    @Length(message = "正文【详情】最长65535个字符", max = 65535)
    private String mainText;
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
     * 果味指数
     */
    @ApiModelProperty("利好")
    private Integer good;
    /**
     * 果味指数
     */
    @ApiModelProperty("利空")
    private Integer bad;
    /**
     * 果味指数
     */
    @ApiModelProperty("果味指数")
    private Integer guowerIndex;
    /**
     * 是否为7*24小时快讯【0.默认快报  1.7*24小时快讯】
     */
    @ApiModelProperty("是否为7*24小时快讯【0.默认快报  1.7*24小时快讯】")
    private Integer isNewsFlash;
    /**
     * 阅读量
     */
    @ApiModelProperty("阅读量")
    private Integer lookTimes;

    /**
     * 鸵鸟id
     */
    @ApiModelProperty("鸵鸟id")
    private Integer tuoniaoId;
    /**
     * 哪个网站
     */
    @ApiModelProperty("某网站")
    private String outWeb;  // 1 鸵鸟

    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String fastnewsRemarks1;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String fastnewsRemarks2;
    /**
     * 暂留
     */
    @ApiModelProperty("暂留")
    /**@NotBlank(message = "暂留不能为空")*/
    /**@NotContainSpaces(message = "暂留不能包含空格")*/
    @Length(message = "暂留最长255个字符", max = 255)
    private String fastnewsRemarks3;

    private String week;

    private Map weixinapi;

    private String isPush;

    public Map<String,String> getWeixinapi() {
        return weixinapi;
    }

    public void setWeixinapi(Map weixinapi) {
        this.weixinapi = weixinapi;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

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
        public void setMainText(String mainText) {
            this.mainText = mainText != null ? (mainText).trim() : null;
    	}

        public String getMainText() {
		return mainText;
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
        public void setGuowerIndex(Integer guowerIndex) {
            this.guowerIndex = guowerIndex;
    	}

        public Integer getGuowerIndex() {
		return guowerIndex;
	}
        public void setIsNewsFlash(Integer isNewsFlash) {
            this.isNewsFlash = isNewsFlash;
    	}

        public Integer getIsNewsFlash() {
		return isNewsFlash;
	}
        public void setLookTimes(Integer lookTimes) {
            this.lookTimes = lookTimes;
    	}

        public Integer getLookTimes() {
		return lookTimes;
	}
        public void setFastnewsRemarks1(String fastnewsRemarks1) {
            this.fastnewsRemarks1 = fastnewsRemarks1 != null ? (fastnewsRemarks1).trim() : null;
    	}

        public String getFastnewsRemarks1() {
		return fastnewsRemarks1;
	}
        public void setFastnewsRemarks2(String fastnewsRemarks2) {
            this.fastnewsRemarks2 = fastnewsRemarks2 != null ? (fastnewsRemarks2).trim() : null;
    	}

        public String getFastnewsRemarks2() {
		return fastnewsRemarks2;
	}
        public void setFastnewsRemarks3(String fastnewsRemarks3) {
            this.fastnewsRemarks3 = fastnewsRemarks3 != null ? (fastnewsRemarks3).trim() : null;
    	}

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public String getFastnewsRemarks3() {
		return fastnewsRemarks3;
	}

        public String getReleaseUserName() {
            return releaseUserName;
        }

        public void setReleaseUserName(String releaseUserName) {
            this.releaseUserName = releaseUserName;
        }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTuoniaoId() {
        return tuoniaoId;
    }

    public void setTuoniaoId(Integer tuoniaoId) {
        this.tuoniaoId = tuoniaoId;
    }


    public String getOutWeb() {
        return outWeb;
    }

    public void setOutWeb(String outWeb) {
        this.outWeb = outWeb;
    }

    public String getIsPush() {
        return isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }
}
