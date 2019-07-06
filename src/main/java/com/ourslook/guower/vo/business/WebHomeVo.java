package com.ourslook.guower.vo.business;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import com.ourslook.guower.entity.business.BusFastNewsEntity;
import com.ourslook.guower.entity.common.InfDictInfoEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * web端首页vo
 */
public class WebHomeVo {

    @ApiModelProperty("首页banner大")
    List<BusAdvertisementEntity> homeBanner;

    @ApiModelProperty("首页banner小")
    List<BusAdvertisementEntity> homeSmaillBanner;

    @ApiModelProperty("新闻类型")
    List<InfDictInfoEntity> newsType;

    @ApiModelProperty("作者专栏")
    List<Map<String, Object>> authorList;

    @ApiModelProperty("企业专栏")
    List<Map<String, Object>> companyList;

    @ApiModelProperty("7*24快讯")
    List<BusFastNewsEntity> fastNews;

    public List<BusAdvertisementEntity> getHomeBanner() {
        return homeBanner;
    }

    public void setHomeBanner(List<BusAdvertisementEntity> homeBanner) {
        this.homeBanner = homeBanner;
    }

    public List<BusAdvertisementEntity> getHomeSmaillBanner() {
        return homeSmaillBanner;
    }

    public void setHomeSmaillBanner(List<BusAdvertisementEntity> homeSmaillBanner) {
        this.homeSmaillBanner = homeSmaillBanner;
    }

    public List<InfDictInfoEntity> getNewsType() {
        return newsType;
    }

    public void setNewsType(List<InfDictInfoEntity> newsType) {
        this.newsType = newsType;
    }

    public List<Map<String, Object>> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Map<String, Object>> authorList) {
        this.authorList = authorList;
    }

    public List<Map<String, Object>> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Map<String, Object>> companyList) {
        this.companyList = companyList;
    }

    public List<BusFastNewsEntity> getFastNews() {
        return fastNews;
    }

    public void setFastNews(List<BusFastNewsEntity> fastNews) {
        this.fastNews = fastNews;
    }
}
