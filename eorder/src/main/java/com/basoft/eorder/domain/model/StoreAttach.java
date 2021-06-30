package com.basoft.eorder.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 1:22 PM 12/2/19
 **/

@Data
public class StoreAttach {

    private Long storeAttachId;

    private Long storeId;

    private String contentId;

    private String contentUrl;

    private int displayOrder;

    private Boolean isDisplay;

    private int attachType;

    private Date createdDt;

    private String createdUserId;

    private Date modifiedDt;

    private String modifiedUserId;

}
