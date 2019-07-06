package com.ourslook.guower.vo.business;

import java.util.List;

/**
 * @author libo
 * @date 2018/7/3
 *
 * 作者详情页，包含文章列表，作者信息，文章数量，总浏览量等
 */
public class AuthorNewsVo {

    private String nickName;

    private String headImg;

    private String signature;

    private Integer userLevel;

    private Integer newsTotal;

    private Integer browseTotal;

    private List<BusNewsVo> busNewsVos;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getNewsTotal() {
        return newsTotal;
    }

    public void setNewsTotal(Integer newsTotal) {
        this.newsTotal = newsTotal;
    }

    public Integer getBrowseTotal() {
        return browseTotal;
    }

    public void setBrowseTotal(Integer browseTotal) {
        this.browseTotal = browseTotal;
    }

    public List<BusNewsVo> getBusNewsVos() {
        return busNewsVos;
    }

    public void setBusNewsVos(List<BusNewsVo> busNewsVos) {
        this.busNewsVos = busNewsVos;
    }
}
