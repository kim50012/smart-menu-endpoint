package com.basoft.eorder.domain.model.activity.discount;

/**
 * Domain：折扣Discount
 *
 * @author Mentor
 * @version 1.0
 * @since 20190515
 */
public class Discount {
    private Long discId;
    private String discName;
    private String discChannel;
    private Long custId;
    private Long storeId;
    private String discRate;
    private String startTime;
    private String endTime;
    private String discStatus;
    private String useYn;
    private String createdDt;
    private String createdUserId;
    private String modifiedDt;
    private String modifiedUserId;

    /**
     * 状态定义：折扣活动的状态
     */
    public enum DiscountStatus {
        // 新建
        DISCOUNT_STATUS_NEW(1),
        // 发布
        DISCOUNT_STATUS_PUBLISH(2),
        // 下架
        DISCOUNT_STATUS_UNPUBLISH(3);

        private int code;

        DiscountStatus(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }

        public static DiscountStatus valueOf(Integer value) {
            for (DiscountStatus e : DiscountStatus.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }

    /**
     * 状态定义：是否可用
     */
    public enum UserYn {
        // 可用
        USER_Y(1),
        // 不可用
        USER_N(0);

        private int code;

        UserYn(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }

        public static UserYn valueOf(Integer value) {
            for (UserYn e : UserYn.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }

    public Long getDiscId() {
        return discId;
    }

    public String getDiscName() {
        return discName;
    }

    public String getDiscChannel() {
        return discChannel;
    }

    public Long getCustId() {
        return custId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getDiscRate() {
        return discRate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDiscStatus() {
        return discStatus;
    }

    public String getUseYn() {
        return useYn;
    }

    public String getCreatedDt() {
        return createdDt;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public String getModifiedDt() {
        return modifiedDt;
    }

    public String getModifiedUserId() {
        return modifiedUserId;
    }

    public void setDiscId(Long discId) {
        this.discId = discId;
    }

    public void setDiscName(String discName) {
        this.discName = discName;
    }

    public void setDiscChannel(String discChannel) {
        this.discChannel = discChannel;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setDiscRate(String discRate) {
        this.discRate = discRate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDiscStatus(String discStatus) {
        this.discStatus = discStatus;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public void setModifiedDt(String modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public static final class Builder {
        private Long discId;
        private String discName;
        private String discChannel;
        private Long custId;
        private Long storeId;
        private String discRate;
        private String startTime;
        private String endTime;
        private String discStatus;
        private String useYn;
        private String createdDt;
        private String createdUserId;
        private String modifiedDt;
        private String modifiedUserId;

        public Builder setDiscId(Long discId) {
            this.discId = discId;
            return this;
        }

        public Builder setDiscName(String discName) {
            this.discName = discName;
            return this;
        }

        public Builder setDiscChannel(String discChannel) {
            this.discChannel = discChannel;
            return this;
        }

        public Builder setCustId(Long custId) {
            this.custId = custId;
            return this;
        }

        public Builder setStoreId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder setDiscRate(String discRate) {
            this.discRate = discRate;
            return this;
        }

        public Builder setStartTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setDiscStatus(String discStatus) {
            this.discStatus = discStatus;
            return this;
        }

        public Builder setUseYn(String useYn) {
            this.useYn = useYn;
            return this;
        }

        public Builder setCreatedDt(String createdDt) {
            this.createdDt = createdDt;
            return this;
        }

        public Builder setCreatedUserId(String createdUserId) {
            this.createdUserId = createdUserId;
            return this;
        }

        public Builder setModifiedDt(String modifiedDt) {
            this.modifiedDt = modifiedDt;
            return this;
        }

        public Builder setModifiedUserId(String modifiedUserId) {
            this.modifiedUserId = modifiedUserId;
            return this;
        }

        public Discount build() {
            Discount discount = new Discount();
            discount.discId = this.discId;
            discount.discName = this.discName;
            discount.discChannel = this.discChannel;
            discount.custId = this.custId;
            discount.storeId = this.storeId;
            discount.discRate = this.discRate;
            discount.startTime = this.startTime;
            discount.endTime = this.endTime;
            discount.discStatus = this.discStatus;
            discount.useYn = this.useYn;
            discount.createdDt = this.createdDt;
            discount.createdUserId = this.createdUserId;
            discount.modifiedDt = this.modifiedDt;
            discount.modifiedUserId = this.modifiedUserId;
            return discount;
        }
    }
}
