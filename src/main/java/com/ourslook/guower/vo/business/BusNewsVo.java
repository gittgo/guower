package com.ourslook.guower.vo.business;

import com.ourslook.guower.entity.business.BusNewsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;




/**
 * 新闻文章表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2018-06-29 10:50:12
 */
@ApiModel(description = "新闻文章表", parent = BusNewsEntity.class)
public class BusNewsVo extends BusNewsEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
    //=============================关联查询的字段，请在下面书写========================

	@ApiModelProperty("新闻类型名")
	private String newsTypeName;

	@ApiModelProperty("作者名")
	private String authorName;

	@ApiModelProperty("作者头像")
	private String authorHeadPortrait;

	@ApiModelProperty("发布人名称")
	private String releaseUserName;

	public String getNewsTypeName() {
		return newsTypeName;
	}

	public void setNewsTypeName(String newsTypeName) {
		this.newsTypeName = newsTypeName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorHeadPortrait() {
		return authorHeadPortrait;
	}

	public void setAuthorHeadPortrait(String authorHeadPortrait) {
		this.authorHeadPortrait = authorHeadPortrait;
	}

	public String getReleaseUserName() {
		return releaseUserName;
	}

	public void setReleaseUserName(String releaseUserName) {
		this.releaseUserName = releaseUserName;
	}
}
