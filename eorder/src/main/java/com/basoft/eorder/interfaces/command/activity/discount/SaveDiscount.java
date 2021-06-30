package com.basoft.eorder.interfaces.command.activity.discount;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Discount Parameter Domain.
 * Implements Command interface.
 *
 * @author Mentor
 * @version 1.0
 * @since 20190515
 */
@SuppressWarnings("unused")
public class SaveDiscount implements Command {
    public static final String COMMAND_SAVE_NAME = "saveDiscount";

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
    private List<SaveDiscountDetail> detailList = new LinkedList<>();

    // 是否需要重做产品明细 y-重做 n-不重做
    private String isReform;

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

    public List<SaveDiscountDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<SaveDiscountDetail> detailList) {
        this.detailList = detailList;
    }

    public String getIsReform() {
        return isReform;
    }

    public void setIsReform(String isReform) {
        this.isReform = isReform;
    }

    public Discount build(Store store) {
        return build(store, this.discId);
    }

    public Discount build(Store store, Long discId) {
        return new Discount.Builder()
                .setDiscId(discId)
                .setDiscName(this.discName)
                .setDiscChannel(this.discChannel)
                .setCustId(this.custId)
                .setStoreId(store.id())
                .setDiscRate(this.discRate)
                //.setStartTime(this.startTime)
                //.setEndTime(this.endTime)
                .setStartTime(DateFormatUtils.format(Long.valueOf(this.startTime), "yyyy-MM-dd HH:mm:ss"))
                .setEndTime(DateFormatUtils.format(Long.valueOf(this.endTime), "yyyy-MM-dd HH:mm:ss"))
                .setDiscStatus(this.discStatus)
                .setUseYn(this.useYn)
                .setCreatedDt(this.createdDt)
                .setCreatedUserId(this.createdUserId)
                .setModifiedDt(this.modifiedDt)
                .setModifiedUserId(this.modifiedUserId)
                .build();
    }

    public static final class SaveDiscountDetail {
        // 折扣活动的明细ID
        private Long discDetId;

        // 折扣活动
        private Long discId;

        // 活动产品
        private Long prodId;

        // 活动产品规格
        private Long prodSkuId;

        // 所属客户
        private Long custId;

        // 店铺
        private Long storeId;

        // 活动折扣后价格
        private BigDecimal discPrice;

        // 创建时间
        private String createdDt;

        // 创建人
        private String createdUserId;

        public Long getDiscDetId() {
            return discDetId;
        }

        public void setDiscDetId(Long discDetId) {
            this.discDetId = discDetId;
        }

        public Long getDiscId() {
            return discId;
        }

        public void setDiscId(Long discId) {
            this.discId = discId;
        }

        public Long getProdId() {
            return prodId;
        }

        public void setProdId(Long prodId) {
            this.prodId = prodId;
        }

        public Long getProdSkuId() {
            return prodSkuId;
        }

        public void setProdSkuId(Long prodSkuId) {
            this.prodSkuId = prodSkuId;
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

        public BigDecimal getDiscPrice() {
            return discPrice;
        }

        public void setDiscPrice(BigDecimal discPrice) {
            this.discPrice = discPrice;
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

        DiscountDetail build(Discount discount) {
            return new DiscountDetail.Builder()
                    .setDiscDetId(this.discDetId)
                    // discId、custId、storeId取自discount对象
                    //.setDiscId(this.discId)
                    .setProdId(this.prodId)
                    .setProdSkuId(this.prodSkuId)
                    //.setCustId(this.custId)
                    //.setStoreId(this.storeId)
                    .setDiscPrice(this.discPrice)
                    .setCreatedDt(this.createdDt)
                    .setCreatedUserId(this.getCreatedUserId())
                    .build(discount);
        }
    }
}