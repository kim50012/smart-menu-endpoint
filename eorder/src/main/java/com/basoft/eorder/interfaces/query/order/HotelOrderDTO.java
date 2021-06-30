package com.basoft.eorder.interfaces.query.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelOrderDTO {
    private Long id;

    private Long storeId;

    //预约开始日期 예약 시작일
    private String reseveDtfrom;

    //预约结束日期 예약 종료일
    private String reseveDtto;

    private Long prodSkuId;

    private String reserveDate;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
