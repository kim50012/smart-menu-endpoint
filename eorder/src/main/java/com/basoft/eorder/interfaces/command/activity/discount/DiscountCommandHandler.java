package com.basoft.eorder.interfaces.command.activity.discount;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.domain.DiscountRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;
import com.basoft.eorder.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@CommandHandler.AutoCommandHandler("DiscountCommandHandler")
//@Transactional
public class DiscountCommandHandler {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private UidGenerator uidGenerator;

    public DiscountCommandHandler() {
    }

    public DiscountCommandHandler(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    /**
     * 如果没有ID 就是新建;如果有ID 就是更新
     *
     * @param saveDiscount
     * @param context
     * @return Banner ID
     */
    @CommandHandler.AutoCommandHandler(SaveDiscount.COMMAND_SAVE_NAME)
    // @Transactional
    public Long saveDiscount(SaveDiscount saveDiscount, CommandHandlerContext context) {
        // 会话信息
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);

        // 门店ID检查
        Long storeId = us.getStoreId();
        Store store = storeRepository.getStore(storeId);
        if (store == null) {
            throw new BizException(ErrorCode.STORE_NULL);
        }

        Long discId = saveDiscount.getDiscId();
        // 折扣ID为空，则新增
        if (discId == null) {
            discId = uidGenerator.generate(BusinessTypeEnum.ACTIVITY_DISCOUNT);
            // 折扣
            Discount discount = saveDiscount.build(store, discId);
            // 折扣明细
            final List<DiscountDetail> discountDetails =
                    saveDiscount
                            .getDetailList()
                            .stream()
                            .map((SaveDiscount.SaveDiscountDetail saveDiscountDetail) -> {
                                saveDiscountDetail.setDiscDetId(uidGenerator.generate(BusinessTypeEnum.ACTIVITY_DISCOUNT_DETAIL));
                                saveDiscountDetail.setCreatedUserId(storeId.toString());
                                return saveDiscountDetail.build(discount);
                            })
                            .collect(toList());
            // 新增
            int saveCount = discountRepository.saveDiscount(discount, discountDetails);
            //return Long.valueOf(saveCount);
            return discId;
        }

        // 修改折扣活动
        Discount discount = saveDiscount.build(store);
        // 折扣明细
        final List<DiscountDetail> discountDetails =
                saveDiscount
                        .getDetailList()
                        .stream()
                        .map((SaveDiscount.SaveDiscountDetail saveDiscountDetail) -> {
                            saveDiscountDetail.setDiscDetId(uidGenerator.generate(BusinessTypeEnum.ACTIVITY_DISCOUNT_DETAIL));
                            saveDiscountDetail.setCreatedUserId(storeId.toString());
                            return saveDiscountDetail.build(discount);
                        })
                        .collect(toList());
        return Long.valueOf(discountRepository.updateDiscount(saveDiscount.getIsReform(), discount, discountDetails));
    }
}