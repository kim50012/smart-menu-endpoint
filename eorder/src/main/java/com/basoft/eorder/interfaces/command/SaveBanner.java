package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Banner;
import com.basoft.eorder.domain.model.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SaveBanner implements Command {


    public static final String NAME = "saveBanner";

    public static final String NAME_UPSTATUS = "upBannerStatus";

    public static final String NAME_UP_SHOWINDEX = "updateBannerIndex";
    private Long id;
    private String name;
    private String imageUrl;

    private int showIndex;

    @JsonIgnore(value=false)
    private int resetIndex;

    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getResetIndex() {
        return resetIndex;
    }

    public void setResetIndex(int resetIndex) {
        this.resetIndex = resetIndex;
    }

    Banner build(Store store){
        return build(store,this.id);
    }


    Banner build(Store store,Long bannderId){
        return new Banner.Builder()
                .setId(bannderId)
                .setImageUrl(this.imageUrl)
                .setStoreId(store.id())
                .setName(this.name)
                .setStatus(this.status)
                .setOrder(this.showIndex)
                .build();

    }
}
