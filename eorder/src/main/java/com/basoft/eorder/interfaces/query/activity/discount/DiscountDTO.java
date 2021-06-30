package com.basoft.eorder.interfaces.query.activity.discount;

import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;

import java.util.LinkedList;
import java.util.List;

/**
 * 折扣查询DTO
 *
 * @author Mentor
 * @version 1.0
 * @since 20190520
 */
public class DiscountDTO {
    // 折扣信息
    //private Discount discountInfo;
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
    // 折扣活动的产品明细列表
    private List<DiscountDetail> detailList = new LinkedList<>();

    public Long getDiscId() {
        return discId;
    }

    public void setDiscId(Long discId) {
        this.discId = discId;
    }

    public String getDiscName() {
        return discName;
    }

    public void setDiscName(String discName) {
        this.discName = discName;
    }

    public String getDiscChannel() {
        return discChannel;
    }

    public void setDiscChannel(String discChannel) {
        this.discChannel = discChannel;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getDiscRate() {
        return discRate;
    }

    public void setDiscRate(String discRate) {
        this.discRate = discRate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDiscStatus() {
        return discStatus;
    }

    public void setDiscStatus(String discStatus) {
        this.discStatus = discStatus;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(String modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public String getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public List<DiscountDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DiscountDetail> detailList) {
        this.detailList = detailList;
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

        public DiscountDTO build(Discount discount) {
            DiscountDTO discountDTO = new DiscountDTO();
            discountDTO.discId = discount.getDiscId();
            discountDTO.discName = discount.getDiscName();
            discountDTO.discChannel = discount.getDiscChannel();
            discountDTO.custId = discount.getCustId();
            discountDTO.storeId = discount.getStoreId();
            discountDTO.discRate = discount.getDiscRate();
            discountDTO.startTime = discount.getStartTime();
            discountDTO.endTime = discount.getEndTime();
            discountDTO.discStatus = discount.getDiscStatus();
            discountDTO.useYn = discount.getUseYn();
            discountDTO.createdDt = discount.getCreatedDt();
            discountDTO.createdUserId = discount.getCreatedUserId();
            discountDTO.modifiedDt = discount.getModifiedDt();
            discountDTO.modifiedUserId = discount.getModifiedUserId();
            return discountDTO;
        }
    }
}
