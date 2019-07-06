package com.ourslook.guower.vo.business;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 首页广告位vo--只用于首页广告位显示
 */
public class HomeAdvertisementVo {

    @ApiModelProperty("首页头部广告位")
    private List<BusAdvertisementEntity> homeHead;

    @ApiModelProperty("首页右侧广告位")
    private List<BusAdvertisementEntity> homeRight;

    public List<BusAdvertisementEntity> getHomeHead() {
        return homeHead;
    }

    public void setHomeHead(List<BusAdvertisementEntity> homeHead) {
        this.homeHead = homeHead;
    }

    public List<BusAdvertisementEntity> getHomeRight() {
        return homeRight;
    }

    public void setHomeRight(List<BusAdvertisementEntity> homeRight) {
        this.homeRight = homeRight;
    }
}
