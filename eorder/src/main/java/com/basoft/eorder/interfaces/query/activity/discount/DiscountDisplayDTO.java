package com.basoft.eorder.interfaces.query.activity.discount;

/**
 * 折扣主页显示DTO
 *
 * @author Mentor
 * @version 1.0
 * @since 20190520
 */
public class DiscountDisplayDTO implements Comparable<DiscountDisplayDTO> {
    private Long discId;
    private String discName;
    private String discChannel;
    private Long custId;
    private Long storeId;
    private String discRate;
    private String startTime;
    private String endTime;
    private Long discDetId;
    private Long prodId;
    private Long prodSkuId;
    private String discPrice;
    private String discPriceChn;

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

    public Long getDiscDetId() {
        return discDetId;
    }

    public void setDiscDetId(Long discDetId) {
        this.discDetId = discDetId;
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

    public String getDiscPrice() {
        return discPrice;
    }

    public void setDiscPrice(String discPrice) {
        this.discPrice = discPrice;
    }

    public String getDiscPriceChn() {
        return discPriceChn;
    }

    public void setDiscPriceChn(String discPriceChn) {
        this.discPriceChn = discPriceChn;
    }

    /**
     * 重写比较方法
     *
     * @param discountDisplay
     * @return
     */
    @Override
    public int compareTo(DiscountDisplayDTO discountDisplay) {
        // 升序
        //return this.getStartTime().compareTo(discountDisplay.getStartTime());

        // 降序
        return discountDisplay.getStartTime().compareTo(this.getStartTime());
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
        private Long discDetId;
        private Long prodId;
        private Long prodSkuId;
        private String discPrice;
        private String discPriceChn;

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

        public Builder setDiscDetId(Long discDetId) {
            this.discDetId = discDetId;
            return this;
        }

        public Builder setProdId(Long prodId) {
            this.prodId = prodId;
            return this;
        }

        public Builder setProdSkuId(Long prodSkuId) {
            this.prodSkuId = prodSkuId;
            return this;
        }

        public Builder setDiscPrice(String discPrice) {
            this.discPrice = discPrice;
            return this;
        }

        public Builder setDiscPriceChn(String discPriceChn) {
            this.discPriceChn = discPriceChn;
            return this;
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

        public Long getDiscDetId() {
            return discDetId;
        }

        public Long getProdId() {
            return prodId;
        }

        public Long getProdSkuId() {
            return prodSkuId;
        }

        public String getDiscPrice() {
            return discPrice;
        }

        public String getDiscPriceChn() {
            return discPriceChn;
        }

        public DiscountDisplayDTO build() {
            DiscountDisplayDTO discountDisplayDTO = new DiscountDisplayDTO();
            discountDisplayDTO.discId = this.getDiscId();
            discountDisplayDTO.discName = this.getDiscName();
            discountDisplayDTO.discChannel = this.getDiscChannel();
            discountDisplayDTO.custId = this.getCustId();
            discountDisplayDTO.storeId = this.getStoreId();
            discountDisplayDTO.discRate = this.getDiscRate();
            discountDisplayDTO.startTime = this.getStartTime();
            discountDisplayDTO.endTime = this.getEndTime();
            discountDisplayDTO.discDetId = this.getDiscDetId();
            discountDisplayDTO.prodId = this.getProdId();
            discountDisplayDTO.prodSkuId = this.getProdSkuId();
            discountDisplayDTO.discPrice = this.getDiscPrice();
            discountDisplayDTO.discPriceChn = this.getDiscPriceChn();
            return discountDisplayDTO;
        }
    }
}