package com.basoft.eorder.interfaces.query;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 4:00 PM 12/2/19
 **/
@Data
public class StoreAttachDTO {

    private Long storeAttachId;

    private Long storeId;

    private String contentId;

    private String contentUrl;

    private int displayOrder;

    private Boolean isDisplay;

    private int attachType;
}
