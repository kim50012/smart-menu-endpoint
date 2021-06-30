package com.basoft.eorder.interfaces.command.inventory.hotel;


import com.basoft.eorder.application.*;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.batch.job.threads.HotelPriceThread;
import com.basoft.eorder.domain.InventoryHotelRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;
import com.basoft.eorder.interfaces.query.ProductDTO;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.ProductSkuDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * @Author:DongXifu
 * @Description:
 * @Date
 **/
@CommandHandler.AutoCommandHandler("InventoryHotelCommandHandler")
@Transactional
public class InventoryHotelCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private InventoryHotelRepository inventoryHotelRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductQuery productQuery;

    @Autowired
    private InventoryHotelQuery inventoryHotelQuery;


    /**
     * 新建或修改酒店库存
     *
     * @param saveInventoryHotel
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveInventoryHotel.NAME)
    @Transactional
    public List<InventoryHotelDTO.FailMsg> SaveInventoryHotel(SaveInventoryHotel saveInventoryHotel, CommandHandlerContext context) {
        logger.debug("Start SaveInventoryHotel -------");
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        //验证产品和规格是否有效
        validProductAndSku(saveInventoryHotel, us);
        //执行新增或修改
        return addOrUpInventHotel(us, saveInventoryHotel);
    }

    /**
     * 将新增和修改的list拆分出来并入库
     * 返回不合格的list(库存数量小于剩余量的房间)
     *
     * @return
     * @Param
     * @author Dong Xifu
     * @date 2019/8/16 下午5:31
     */
    private List<InventoryHotelDTO.FailMsg> addOrUpInventHotel(UserSession us, SaveInventoryHotel saveInventoryHotel) {
        List<InventoryHotelDTO.FailMsg> failDateList = new LinkedList<>();
        try {
            //客户端待新增日期
            ArrayList<String> dataAll = DateUtil.findDataAll(saveInventoryHotel.getStartTime(), saveInventoryHotel.getEndTime(), 1);
            //客户端需要新增或修改的房间
            for (SaveInventoryHotel.ProductAndSku proAndSku : saveInventoryHotel.getProductAndSkuList()) {
                //该店铺当前房间已有的库存
                List<InventoryHotelDTO> inventoryHotelList = getInventoryHotelList(saveInventoryHotel, proAndSku, us);
                //已使用库存大于库存总数的日期删除并返回客户端
                failDateList = addFailList(saveInventoryHotel, inventoryHotelList, failDateList, dataAll);
                //最后一步执行新增或修改库存和价格
                saveInventoryList(us, dataAll, inventoryHotelList, saveInventoryHotel, proAndSku);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return failDateList;
    }

    /**
     * 查询该店铺当前房间已有的库存
     *
     * @return java.util.List<InventoryHotelDTO>
     * @Param
     * @author Dong Xifu
     * @date 2019/11/20 5:50 下午
     */
    private List<InventoryHotelDTO> getInventoryHotelList(SaveInventoryHotel save
            , SaveInventoryHotel.ProductAndSku proAndSku, UserSession us) {
        Map<String, Object> param = new HashMap<>();
        param.put("startTime", save.getStartTime());
        param.put("endTime", save.getEndTime());
        param.put("storeId", us.getStoreId());
        param.put("prodId", proAndSku.getProdId());
        param.put("prodSkuId", proAndSku.getProdSkuId());
        return inventoryHotelQuery.getInventoryHotelList(param);
    }

    /**
     * 插入failDateList
     * 查询店铺已有库存
     *
     * @param saveInventoryHotel
     * @param inventoryHotelList
     * @param failDateList
     * @param dataAll
     * @return failDateList
     */
    private List<InventoryHotelDTO.FailMsg> addFailList(SaveInventoryHotel saveInventoryHotel
            , List<InventoryHotelDTO> inventoryHotelList
            , List<InventoryHotelDTO.FailMsg> failDateList
            , List<String> dataAll
    ) {
        inventoryHotelList.stream().map(i -> {
            if (saveInventoryHotel.getInvTotal() != null) {
                /******验证已使用总数是否大于库存总数并把 不符合的放入failDateList中********/
                if (saveInventoryHotel.getInvTotal() < Integer.valueOf(i.getInvUsed())) {
                    dataAll.remove(i.getInvDate());
                    InventoryHotelDTO.FailMsg failMsg = new InventoryHotelDTO.FailMsg();
                    failMsg.setProductNmKor(i.getProductNmKor());
                    failMsg.setProductNmChn(i.getProductNmChn());
                    failMsg.setInvDate(i.getInvDate());
                    failMsg.setInvUsed(i.getInvUsed());
                    failMsg.setErroMsgCode("1");
                    failDateList.add(failMsg);
                }
            }

            return i;
        }).collect(Collectors.toList());

        return failDateList;
    }

    /**
     * 封装List<InventoryHotel>
     * 新增或修改库存或价格
     *
     * @return
     * @Param
     * @author Dong Xifu
     * @date 2019/8/16 下午5:31
     */
    private void saveInventoryList(UserSession us, ArrayList<String> dataAll, List<InventoryHotelDTO> inventoryHotelList, SaveInventoryHotel savehotel
            , SaveInventoryHotel.ProductAndSku productAndSku) {
        List<InventoryHotel> addList = new LinkedList<>();
        List<InventoryHotel> updateList = new LinkedList<>();
        List<String> specDataList = new LinkedList<>();
        Long prodId = productAndSku.getProdId();
        Long skuId = productAndSku.getProdSkuId();

        //如果是所有日期就不用遍历
        if (savehotel.getSpecDate().length == 7) {
            specDataList.addAll(dataAll);
        } else {
            dataAll.stream().forEach(date -> {
                int howDay = 0;
                try {
                    howDay = DateUtil.dayForWeek(date);//判断当前日期是星期几
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int arr : savehotel.getSpecDate()) {
                    if (howDay == arr) {
                        specDataList.add(date);
                    }
                }
            });
        }

        //该店铺该产品已有的日期库存
        List<String> dateList = inventoryHotelList.stream().map(InventoryHotelDTO::getInvDate).collect(Collectors.toList());

        //前端传入的与已有日期库存的取交集用来修改
        List<String> intersection = specDataList.stream().filter(item -> dateList.contains(item)).collect(toList());
        for (String date : intersection) {

            InventoryHotel inventoryHotel = savehotel.build(prodId, skuId, 0L, date, us);
            updateList.add(inventoryHotel);
        }
        if (updateList != null && updateList.size() > 0) {
            savehotel.setUpdateUser(us.getAccount());
            //如果修改到手价 将折扣价置为-1
            inventoryHotelList.stream().forEach(i -> {
                updateList.stream().forEach(u -> {
                    if (i.getInvDate().equals(u.getInvDate())) {
                        if (new BigDecimal(i.getPriceSettle()).compareTo(u.getPriceSettle()) != 0) {
                            u.setDisPrice(new BigDecimal(-1));
                        }
                    }
                });
            });

            int updateNum = inventoryHotelRepository.upInventoryHotels(updateList);
            logger.info("酒店库存修改条数->" + updateNum);
        }

        //前端传入的与已有的日期库存取差集用来新增
        List<String> reduce = specDataList.stream().filter(item -> !dateList.contains(item)).collect(toList());
        for (String date : reduce) {
            Long invId = uidGenerator.generate(BusinessTypeEnum.INVENTORY_HOTEL);
            InventoryHotel inventoryHotel = savehotel.build(prodId, skuId, invId, date, us);
            addList.add(inventoryHotel);
        }

        if (addList != null && addList.size() > 0) {
            savehotel.setCreateUser(us.getAccount());
            int insertNum = inventoryHotelRepository.saveInventoryHotels(addList);
            logger.info("酒店库存新增条数->" + insertNum);
        }
        Thread thread = new Thread(new HotelPriceThread(us, storeRepository, inventoryHotelQuery));
        thread.start();
    }

    /**
     * 校验是否是有效的产品和规格
     *
     * @return void
     * @Param
     * @author Dong Xifu
     * @date 2019/11/20 5:08 下午
     */
    private void validProductAndSku(SaveInventoryHotel saveInventoryHotel, UserSession us) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", us.getStoreId());
        param.put("isDeposit", 0);
        List<ProductDTO> productList = productQuery.getProductListByMap(param);
        List<ProductSkuDTO> skuDTOList = productQuery.getProductSkuListByMap(param);
        for (SaveInventoryHotel.ProductAndSku productAndSku : saveInventoryHotel.getProductAndSkuList()) {
            final ProductDTO proDto = productList.stream()
                    .filter((ProductDTO inProDto) -> {
                        return inProDto.getId().longValue() == productAndSku.getProdId().longValue();
                    }).findFirst().orElseGet(() -> null);
            if (proDto == null) {
                throw new BizException(ErrorCode.BIZ_EXCEPTION, "invalid product");
            }

            final ProductSkuDTO skuDTO = skuDTOList.stream()
                    .filter((ProductSkuDTO inSkuDto) -> {
                        return inSkuDto.getId().longValue() == productAndSku.getProdSkuId().longValue();
                    }).findFirst().orElseGet(() -> null);

            if (skuDTO == null) {
                throw new BizException(ErrorCode.BIZ_EXCEPTION, "invalid proSku");
            }

        }
    }

}

