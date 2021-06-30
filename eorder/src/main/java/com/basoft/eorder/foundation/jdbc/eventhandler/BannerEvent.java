package com.basoft.eorder.foundation.jdbc.eventhandler;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:18 2018/12/20
 **/
public class BannerEvent {
    private int status;
    private Long id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
