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
public class RetailProductGroupMapDTO extends RetailProductDTO {
    private Long productId;

    private Long prdGroupId;

    private int showIndex;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
