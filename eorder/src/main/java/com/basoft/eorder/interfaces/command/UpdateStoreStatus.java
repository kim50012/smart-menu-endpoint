package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Store;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:24 2019/1/7
 **/
public class UpdateStoreStatus implements Command {
    public static final String NAME = "updateStoreStatus";

    private List<Long> storeIds;

    private int status;

    public List<Long> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Store build(Store store) {
        return new Store.Builder()
                .id(store.id())
                .name(store.name())
                .city(store.contact().getCity())
                .detailAddr(store.contact().getDetailAddr())
                .email(store.contact().getEmail())
                .mobile(store.contact().getMobile())
                .ceoName(store.companyInfo().getCeo())
                .logoUrl(store.contact().getLogoUrl())
                .bizScope(store.companyInfo().getScope())
                .manager(store.manager())
                .status(this.status)
                .build();
    }
}
