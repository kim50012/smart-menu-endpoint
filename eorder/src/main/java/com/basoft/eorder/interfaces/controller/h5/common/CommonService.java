package com.basoft.eorder.interfaces.controller.h5.common;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.domain.WxAppInfoTableRepository;
import com.basoft.eorder.domain.model.WxAppInfoTable;
import com.basoft.eorder.interfaces.query.WxUserDTO;
import com.basoft.eorder.interfaces.query.WxUserQuery;
import com.basoft.eorder.util.RedisUtil;
import com.basoft.eorder.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 为Controller提供的公共的服务
 *
 * @author Mentor
 * @version 1.0
 * @Date 20200114
 **/
@Component
@Slf4j
public class CommonService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxUserQuery wxUserQuery;

    @Autowired
    private WxAppInfoTableRepository wxAppInfoTableRepository;

    @Autowired
    private UidGenerator uidGenerator;

    /**
     * 获取微信用户信息
     *
     * @param openId
     * @param shopId
     * @return com.basoft.eorder.interfaces.query.WxUserDTO
     */
    public WxUserDTO getWxUser(String openId, String shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("openId", openId);
        WxUserDTO wxUserDTO = wxUserQuery.getWxUser(param);
        // 出现该情况原因：一、关注时服务存在（网络或者服务停止）问题，没有同步到用户信息到本地
        if (wxUserDTO == null) {
            log.info("<><><><><><><>【用户信息重取】数据库无用户信息，开始从微信公众平台获取<><><><><>");

            // 获取access_token
            String access_token = Objects.toString(redisUtil.get("wx_token"), null);
            if (access_token == null) {
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
            log.info("<><><><><><><>【用户信息重取】access_token<><><><><>" + access_token);

            param.put("access_token", access_token);

            // shopId为null,数据库获取shopId
            if (shopId == null) {
                String sysId = "1";
                WxAppInfoTable wt = wxAppInfoTableRepository.getWxAppInfoTable(sysId);
                shopId = String.valueOf(wt.getShopId());
            }
            log.info("<><><><><><><>【用户信息重取】shopId<><><><><>" + shopId);
            param.put("shopId", shopId);

            // 生成微信用户CUST ID
            Long custId = uidGenerator.generate(BusinessTypeEnum.WX_CUST);
            log.info("<><><><><><><>【用户信息重取】custId<><><><><>" + custId);
            param.put("custId", custId);

            wxUserDTO = wxUserQuery.reGetWxUser(param);

            // 如果用户信息重取为空，此时原因：1：用户取消关注；2：网络不通，获取错误。
            // 提示用户重新尝试关注
            // TODO
            if (wxUserDTO == null) {
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        }

        Base64.Decoder decoder = Base64.getDecoder();
        String nickNm = "";
        try {
            nickNm = new String(decoder.decode(wxUserDTO.getNickname().getBytes()), "UTF-8");
        } catch (Exception e) {
            nickNm = wxUserDTO.getNickname();
        }
        wxUserDTO.setNickname(nickNm);
        return wxUserDTO;
    }
}
