package com.basoft.eorder.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 4:10 下午 2019/11/27
 **/
@Data
public class StoreDynamic {
    private Long storeId;

    private int reviewCount;

    private BigDecimal reviewGrade;

    public static final class Builder {
        private Long storeId;

        private int reviewCount;

        private BigDecimal reviewGrade;

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder reviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }

        public Builder reviewGrade(BigDecimal reviewGrade) {
            this.reviewGrade = reviewGrade;
            return this;
        }

        public StoreDynamic build() {
            StoreDynamic storeDynamic = new StoreDynamic();
            storeDynamic.storeId = this.storeId;
            storeDynamic.reviewCount = this.reviewCount;
            storeDynamic.reviewGrade = this.reviewGrade;
            return storeDynamic;
        }
    }


}
