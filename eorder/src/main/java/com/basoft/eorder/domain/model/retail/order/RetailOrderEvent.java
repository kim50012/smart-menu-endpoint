package com.basoft.eorder.domain.model.retail.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetailOrderEvent {
    private Long orderId;

    private int eventType;

    private int eventInitiator;

    private int isMain;

    private String eventTime;

    private String eventName;

    private String eventTarget;

    private int eventResult;

    private String eventResultDesc;

    private Long servId;
}