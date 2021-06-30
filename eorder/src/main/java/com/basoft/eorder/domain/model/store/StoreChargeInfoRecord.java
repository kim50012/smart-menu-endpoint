package com.basoft.eorder.domain.model.store;

import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.command.SaveStore;
import com.basoft.eorder.util.DateUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 商戶計費信息
 *
 * @author Mentor
 * @version 1.0
 * @since 20190819
 */
public class StoreChargeInfoRecord {
    @JsonSerialize
    private Long scirID;

    @JsonSerialize
    private Long storeID;

    private String chargeYearMonth;

    private int chargeYear;

    private int chargeMonth;

    private int chargeType;

    private double chargeRate;

    private double chargeFee;

    private int useYn;

    private int finalYn;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;

    public Long getScirID() {
        return scirID;
    }

    public void setScirID(Long scirID) {
        this.scirID = scirID;
    }

    public Long getStoreID() {
        return storeID;
    }

    public void setStoreID(Long storeID) {
        this.storeID = storeID;
    }

    public String getChargeYearMonth() {
        return chargeYearMonth;
    }

    public void setChargeYearMonth(String chargeYearMonth) {
        this.chargeYearMonth = chargeYearMonth;
    }

    public int getChargeYear() {
        return chargeYear;
    }

    public void setChargeYear(int chargeYear) {
        this.chargeYear = chargeYear;
    }

    public int getChargeMonth() {
        return chargeMonth;
    }

    public void setChargeMonth(int chargeMonth) {
        this.chargeMonth = chargeMonth;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(double chargeFee) {
        this.chargeFee = chargeFee;
    }

    public int getUseYn() {
        return useYn;
    }

    public void setUserYn(int useYn) {
        this.useYn = useYn;
    }

    public int getFinalYn() {
        return finalYn;
    }

    public void setFinalYn(int finalYn) {
        this.finalYn = finalYn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public static Builder newBuild(Store store, SaveStore saveStore) {
        return new Builder()
                .storeID(store.id())
                // 次月-年月
                .chargeYearMonth(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "yyyyMM"))
                // 次月-年
                .chargeYear(Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "yyyy")))
                // 次月-月
                .chargeMonth(Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "MM")))
                .chargeType(saveStore.getChargeType())
                .chargeRate(saveStore.getChargeRate())
                .chargeFee(saveStore.getChargeFee())
                .useYn(1)
                .finalYn(1);
                //.createTime(storeChargeInfoRecord.createTime)
                //.createUser(storeChargeInfoRecord.createUser)
                //.updateTime(storeChargeInfoRecord.updateTime)
                //.updateUser(storeChargeInfoRecord.updateUser);
    }

    public static final class Builder {
        private Long scirID;

        private Long storeID;

        private String chargeYearMonth;

        private int chargeYear;

        private int chargeMonth;

        private int chargeType;

        private double chargeRate;

        private double chargeFee;

        private int useYn;

        private int finalYn;

        private String createTime;

        private String createUser;

        private String updateTime;

        private String updateUser;

        public Builder scirID(Long scirID) {
            this.scirID = scirID;
            return this;
        }

        public Builder storeID(Long storeID) {
            this.storeID = storeID;
            return this;
        }

        public Builder chargeYearMonth(String chargeYearMonth) {
            this.chargeYearMonth = chargeYearMonth;
            return this;
        }

        public Builder chargeYear(int chargeYear) {
            this.chargeYear = chargeYear;
            return this;
        }

        public Builder chargeMonth(int chargeMonth) {
            this.chargeMonth = chargeMonth;
            return this;
        }

        public Builder chargeType(int chargeType) {
            this.chargeType = chargeType;
            return this;
        }

        public Builder chargeRate(double chargeRate) {
            this.chargeRate = chargeRate;
            return this;
        }

        public Builder chargeFee(double chargeFee) {
            this.chargeFee = chargeFee;
            return this;
        }

        public Builder useYn(int useYn) {
            this.useYn = useYn;
            return this;
        }

        public Builder finalYn(int finalYn) {
            this.finalYn = finalYn;
            return this;
        }

        public Builder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder createUser(String createUser) {
            this.createUser = createUser;
            return this;
        }

        public Builder updateTime(String updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder updateUser(String updateUser) {
            this.updateUser = updateUser;
            return this;
        }

        public StoreChargeInfoRecord build() {
            StoreChargeInfoRecord storeChargeInfoRecord = new StoreChargeInfoRecord();
            storeChargeInfoRecord.scirID = this.scirID;
            storeChargeInfoRecord.storeID = this.storeID;
            storeChargeInfoRecord.chargeYearMonth = this.chargeYearMonth;
            storeChargeInfoRecord.chargeYear = this.chargeYear;
            storeChargeInfoRecord.chargeMonth = this.chargeMonth;
            storeChargeInfoRecord.chargeType = this.chargeType;
            storeChargeInfoRecord.chargeRate = this.chargeRate;
            storeChargeInfoRecord.chargeFee = this.chargeFee;
            storeChargeInfoRecord.useYn = this.useYn;
            storeChargeInfoRecord.finalYn = this.finalYn;
            storeChargeInfoRecord.createTime = this.createTime;
            storeChargeInfoRecord.createUser = this.createUser;
            storeChargeInfoRecord.updateTime = this.updateTime;
            storeChargeInfoRecord.updateUser = this.updateUser;
            return storeChargeInfoRecord;
        }
    }

    @Override
    public String toString() {
        return "StoreChargeInfoRecord{" +
                "scirID=" + scirID +
                ", storeID=" + storeID +
                ", chargeYearMonth='" + chargeYearMonth + '\'' +
                ", chargeYear=" + chargeYear +
                ", chargeMonth=" + chargeMonth +
                ", chargeType=" + chargeType +
                ", chargeRate=" + chargeRate +
                ", chargeFee=" + chargeFee +
                ", useYn=" + useYn +
                ", finalYn=" + finalYn +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}