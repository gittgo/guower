package com.ourslook.guower.entity.score;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ourslook.guower.utils.jackson.CustomPhotoImageSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;




/**
 * 货币表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 12:55:04
 */
@ApiModel(description = "货币表")
public class InfCurrencyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Integer id;
    /**
     * 货币名称
     */
    @ApiModelProperty("货币名称")
    /**@NotBlank(message = "货币名称不能为空")*/
    /**@NotContainSpaces(message = "货币名称不能包含空格")*/
    @Length(message = "货币名称最长255个字符", max = 255)
    private String currencyName;
    /**
     * 图标
     */
    @ApiModelProperty("图标")
    /**@NotBlank(message = "图标不能为空")*/
    /**@NotContainSpaces(message = "图标不能包含空格")*/
    @Length(message = "图标最长255个字符", max = 255)
    private String image;
    /**
     * 积分价格
     */
    @ApiModelProperty("积分价格")
    private Integer score;
    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer count;
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
     * 排序时间
     */
    @ApiModelProperty("排序时间")
    private LocalDateTime sortTime;

    /**
     * 状态【1.正常上架中 0.已删除】
     */
    @ApiModelProperty("状态【1.正常上架中 0.已删除】")
    private Integer DelFlag;

        public void setId(Integer id) {
            this.id = id;
    	}

        public Integer getId() {
		return id;
	}
        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName != null ? (currencyName).trim() : null;
    	}

        public String getCurrencyName() {
		return currencyName;
	}
        public void setImage(String image) {
            this.image = image != null ? (image).trim() : null;
    	}

        @JsonSerialize(using = CustomPhotoImageSerialize.class)
        public String getImage() {
		return image;
	}
        public void setScore(Integer score) {
            this.score = score;
    	}

        public Integer getScore() {
		return score;
	}
        public void setCount(Integer count) {
            this.count = count;
    	}

        public Integer getCount() {
		return count;
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

        public String getReleaseUserName() {
            return releaseUserName;
        }

        public void setReleaseUserName(String releaseUserName) {
            this.releaseUserName = releaseUserName;
        }

    public Integer getDelFlag() {
        return DelFlag;
    }

    public void setDelFlag(Integer delFlag) {
        DelFlag = delFlag;
    }
}
