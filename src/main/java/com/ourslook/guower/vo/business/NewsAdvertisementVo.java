package com.ourslook.guower.vo.business;

import com.ourslook.guower.entity.business.BusAdvertisementEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 新闻页广告vo
 */
public class NewsAdvertisementVo {

    @ApiModelProperty("果味box")
    private List<BusAdvertisementEntity> guowerBox;

    @ApiModelProperty("新闻栏目页视频广告")
    private List<BusAdvertisementEntity> rightVideo;

    @ApiModelProperty("右侧广告")
    private List<BusAdvertisementEntity> rightButton;

    public List<BusAdvertisementEntity> getGuowerBox() {
        return guowerBox;
    }

    public void setGuowerBox(List<BusAdvertisementEntity> guowerBox) {
        this.guowerBox = guowerBox;
    }

    public List<BusAdvertisementEntity> getRightVideo() {
        return rightVideo;
    }

    public void setRightVideo(List<BusAdvertisementEntity> rightVideo) {
        this.rightVideo = rightVideo;
    }

    public List<BusAdvertisementEntity> getRightButton() {
        return rightButton;
    }

    public void setRightButton(List<BusAdvertisementEntity> rightButton) {
        this.rightButton = rightButton;
    }
}
