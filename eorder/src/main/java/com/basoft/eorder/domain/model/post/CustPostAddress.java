package com.basoft.eorder.domain.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Desc:CustPostAddress
 *
 * @author Mentor
 * @version 1.0
 * @date 2016
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustPostAddress {
    // 地址编号
    private Long addrId;

    // 客户ID
    private Long custId;

    // 客户的微信公众号openid
    private String wxOpenId;

    // 配送地址名称
    private String addrName;

    // 配送地址所在国家名称-中文
    private String countryChn;

    // 配送地址所在国家名称-韩文
    private String countryKor;

    // 配送地址所在国家名称-英文
    private String countryEng;

    // 配送地址所在国家编码
    private String countryCode;

    // 收货人
    private String consignee;

    // 手机号码的国家码
    private String mobileCountryCode;

    // 手机号码
    private String mobileNo;

    // 备选电话的国家码
    private String alternateMobileCountryCode;

    // 备选电话
    private String alternateMobileNo;

    // 配送地址所在地区全称，如北京市顺义区空港街道
    private String regionNameChn;

    // 配送地址所在地区全称，如北京市顺义区空港街道
    private String regionNameKor;

    // 配送地址所在地区全称，如北京市顺义区空港街道
    private String regionNameEng;

    // 配送地址所在地区全称之一级区域名称，如北京
    private String regionOneNameChn;

    // 配送地址所在地区全称之一级区域名称，如北京
    private String regionOneNameKor;

    // 配送地址所在地区全称之一级区域名称，如北京
    private String regionOneNameEng;

    // 配送地址所在地区全称之一级区域代码，如010
    private String regionOneCode;

    // 配送地址所在地区全称之二级区域名称，如顺义区
    private String regionTwoNameChn;

    // 配送地址所在地区全称之二级区域名称，如顺义区
    private String regionTwoNameKor;

    // 配送地址所在地区全称之二级区域名称，如顺义区
    private String regionTwoNameEng;

    // 配送地址所在地区全称之二级区域代码，如01025
    private String regionTwoCode;

    // 配送地址所在地区全称之三级区域名称，如空港街道
    private String regionThreeNameChn;

    // 配送地址所在地区全称之三级区域名称，如空港街道
    private String regionThreeNameKor;

    // 配送地址所在地区全称之三级区域名称，如空港街道
    private String regionThreeNameEng;

    // 配送地址所在地区全称之三级区域代码，如0102503
    private String regionThreeCode;

    // 配送地址所在地区全称之四级区域名称
    private String regionFourNameChn;

    // 配送地址所在地区全称之四级区域名称
    private String regionFourNameKor;

    // 配送地址所在地区全称之四级区域名称
    private String regionFourNameEng;

    // 配送地址所在地区全称之四级区域代码
    private String regionFourCode;

    // 配送地址所在地区全称之五级区域名称
    private String regionFiveNameChn;

    // 配送地址所在地区全称之五级区域名称
    private String regionFiveNameKor;

    // 配送地址所在地区全称之五级区域名称
    private String regionFiveNameEng;

    // 配送地址所在地区全称之五级区域代码
    private String regionFiveCode;

    // 详细地址-中文
    private String addrDetailChn;

    // 详细地址-韩文
    private String addrDetailKor;

    // 详细地址-英文
    private String addrDetailEng;

    // 邮编
    private String addrPostcode;

    // 电子邮箱
    private String email;

    // 配送地址标签，如家、公司、学校；可自定义添加标签，如老家
    private String tag;

    // 是否默认配送地址 1-是，0-否
    private int isDefault;

    // 配送地址状态 0-禁用，1-在用，2-删除
    private int status;

    // 状态时间
    private String statusTime;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;

    public boolean checkInput() {
        if (StringUtils.isBlank(this.consignee)) {
            return false;
        }

        if (StringUtils.isBlank(this.mobileNo)) {
            return false;
        }

        if (StringUtils.isBlank(this.countryChn) || StringUtils.isBlank(this.countryCode)) {
            return false;
        }

        if (StringUtils.isBlank(this.getConsignee())) {
            return false;
        }

        // 配送国家为中国则配送地区不允许为空，为韩国允许为空
        if("086".equals(this.countryCode)){
            if (StringUtils.isBlank(this.regionNameChn)) {
                return false;
            }
        }

        if (StringUtils.isBlank(this.addrDetailChn)) {
            return false;
        }
        return true;
    }
}