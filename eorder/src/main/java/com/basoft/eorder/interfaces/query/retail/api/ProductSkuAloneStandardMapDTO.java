package com.basoft.eorder.interfaces.query.retail.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSkuAloneStandardMapDTO {
    private int status;

    private Long productSkuId;

    private Long standardId;

    private Long standardItemId;

    private String stdNameChn;

    private String stdNameKor;

    private String stdNameEng;

    private int stdStatus;

    private String itemNameChn;

    private String itemNameKor;

    private String itemNameEng;

    private int itemStatus;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}