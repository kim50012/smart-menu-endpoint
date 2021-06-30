package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:03 2018/12/24
 **/
public class DelStoreTable implements Command {
    public static final String NAME = "delStoreTable";

    private Long storeId;

    private List<Long> storeTables;

    private int status;

    public enum Status{
        NORMAL(0),OPEN(1),CLOSED(2),DELETED(3);
        private int code;

        Status(int code){
            this.code = code;
        }

        public int code(){
            return this.code;
        }

        public static Status valueOf(Integer value) {
            for (Status e : Status.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }

    public int getStatus(){
        return this.status;
    }
    public void setStatus(int status){
        this.status = status;
    }


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public List<Long> getStoreTables() {
        return storeTables;
    }

    public void setStoreTables(List<Long> storeTables) {
        this.storeTables = storeTables;
    }
}
