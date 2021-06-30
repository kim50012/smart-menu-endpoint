package com.basoft.eorder.interfaces.command.order.common;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class OrderCommonUtil {
    /**
     * 验证商户支付信息是否正确
     * 验证字段:merchant_id,merchant_nm,gateway_pw,transid_type,currency
     *
     * @return void
     * @Param store
     * @author Dong Xifu
     * @date 2019/9/11 3:29 下午
     */
    public static void checkStorePay(Store store) {
        if (StringUtils.isBlank(store.merchantId())
                || StringUtils.isBlank(store.merchantNm())
                || StringUtils.isBlank(store.gatewayPw())
                || StringUtils.isBlank(store.currency())
        ) {
            log.error("▶▶▶▶▶▶▶▶▶▶店铺支付信息参数不全");
            throw new BizException(ErrorCode.STORE_PAYINFO_WRONG);
        }

        if (store.merchantId().length() < 10
                || store.merchantNm().length() < 2
                || store.gatewayPw().length() < 10
                || store.currency().length() < 2) {
            log.error("▶▶▶▶▶▶▶▶▶▶店铺支付信息错误");
            throw new BizException(ErrorCode.STORE_PAYINFO_WRONG);
        }
        if (!"BT".equals(store.transidType()) && !"BA".equals(store.transidType())) {
            log.error("▶▶▶▶▶▶▶▶▶transidType信息错误");
            throw new BizException(ErrorCode.STORE_PAYINFO_WRONG);
        }
    }
}
