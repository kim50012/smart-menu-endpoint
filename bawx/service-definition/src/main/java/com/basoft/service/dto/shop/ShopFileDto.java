package com.basoft.service.dto.shop;

import com.basoft.service.entity.shop.ShopFile;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:56 2018/4/11
 **/

@Data
public class ShopFileDto extends ShopFile {

    private String fileTypeStr;

    private String isUseStr;
}
