package com.basoft.eorder.domain.events;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:09 2018/12/20
 **/
public class UpdateBannerEvent {

    private Long bannerId;

    public Long getBannerId() {
        return bannerId;
    }

    public UpdateBannerEvent(Long bannerId){
        this.bannerId = bannerId;
    }
}
