package com.ourslook.guower.vo.business;

/**
 * 作者专栏排序专用vo
 */
public class AuthorSortVo {

    private Integer userId;

    private String nickName;

    private String headImg;

    private Integer userLevel;

    private Long totalBrowse;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Long getTotalBrowse() {
        return totalBrowse;
    }

    public void setTotalBrowse(Long totalBrowse) {
        this.totalBrowse = totalBrowse;
    }
}
