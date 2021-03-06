package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.QRCodeGenerateService;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.file.FileService;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.Banner;
import com.basoft.eorder.domain.model.ShipPoint;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreCategory;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.interfaces.query.StoreTableDTO;
import com.basoft.eorder.interfaces.query.StoreTableQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.UidGenerator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author woonill
 * @since 2018/12/10
 */
@CommandHandler.AutoCommandHandler("StoreCommandHandler")
public class StoreCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private FileService fileService;
    @Autowired
    @Qualifier("wechatQRService")
    private QRCodeGenerateService wechatService;
    @Autowired
    private StoreTableQuery storeTableQuery;

    /**
     * Save Store
     * <p>
     * ????????????ID ????????????
     * ?????????ID ????????????
     * </p>
     *
     * @param saveStore
     * @return Banner ID
     */
    @CommandHandler.AutoCommandHandler(SaveStore.NAME)
    @Transactional
    public Long saveStore(SaveStore saveStore) {
        Long id = saveStore.getId();

        if (saveStore.getLongitude().compareTo(new BigDecimal(180)) == 1) {
            throw new BizException(ErrorCode.STORE_LONGITUDE_WRONG);
        }

        if (saveStore.getLatitude().compareTo(new BigDecimal(90)) == 1) {
            throw new BizException(ErrorCode.STORE_LONGITUDE_WRONG);
        }

        // ????????????
        if (id == null) {
            id = uidGenerator.generate(BusinessTypeEnum.STORE);

            // ????????????????????????????????????
            long managerId = saveStore.getManagerId();
            List<Store> storeList = storeRepository.getStoreList(managerId);
            if (storeList != null && storeList.size() > 0) {
                throw new BizException(ErrorCode.STORE_MANAGER_EXIT);
            }
            // ??????User??????
            User user = userRepo.getUser(saveStore.getManagerId());

            // ???????????????????????????????????????????????????????????????
            Store store = saveStore.build(new Store(), user, id);
            List<StoreCategory> storeCategoryList = saveStore.buildStoreCategoryList(store);
            if (storeCategoryList != null && storeCategoryList.size() > 0) {
                this.storeRepository.updateStoreCategoryMap(id, storeCategoryList);
            }

            // ????????????
            Long storeId = this.storeRepository.saveStore(store);

            //????????????????????????????????????
            if (Store.IS_SELFSERVICE_ALLOW == saveStore.getSelfserviceUseyn() && Store.IS_SELFSERVICE_ALLOW == saveStore.getIsSelfservice()) {
                SaveStoreTable storeTable = createStoreTableSilent(storeId);
                saveStoreTableSilent(storeTable, id);
            }
            return storeId;
        }
        // ????????????
        else {
            // ????????????????????????????????????
            long managerId = saveStore.getManagerId();
            List<Store> storeList = storeRepository.getStoreList(managerId);
            // ??????????????????????????????????????????????????????????????????
            if (storeList != null && storeList.size() > 0) {
                for(Store store : storeList){
                    if(store.id().longValue() != id.longValue()){
                        throw new BizException(ErrorCode.STORE_MANAGER_EXIT);
                    }
                }
            }

            // ??????User??????
            User user = userRepo.getUser(saveStore.getManagerId());

            // ????????????????????????????????????
            Store storeOld = storeRepository.getStore(id);

            // ???????????????????????????????????????????????????????????????????????????????????????
            boolean isSaveChargeInfo = isSaveChargeInfo(saveStore, storeOld);
            logger.info("???????????????????????????????????????????????????>>>" + isSaveChargeInfo);
            if(isSaveChargeInfo){
                saveChargeInfo(storeOld, saveStore);
            }

            // ??????????????????
            Store store = saveStore.build(storeOld, user, id);
            // ?????????????????????????????????????????????
            store.setChargeType(storeOld.chargeType());
            store.setChargeRate(storeOld.chargeRate());
            store.setChargeFee(storeOld.chargeFee());
            Long storeId = this.storeRepository.updateStore(store).id();

            // ???????????????????????????????????????????????????????????????
            List<StoreCategory> storeCategoryList = saveStore.buildStoreCategoryList(store);
            if (storeCategoryList != null && storeCategoryList.size() > 0) {//??????????????????????????????????????????????????????
                this.storeRepository.updateStoreCategoryMap(id, storeCategoryList);
            }

            //??????????????????
            selfService(saveStore.getIsSelfservice(), saveStore.getSelfserviceUseyn(), id);

            return storeId;
        }
    }

    /**
     * ???????????????????????????
     *
     * @param saveStore ????????????
     * @param storeOld
     */
    private boolean isSaveChargeInfo(SaveStore saveStore, Store storeOld) {
        // ????????????????????????????????????
        int newChargeType = saveStore.getChargeType();
        double newChargeRate = saveStore.getChargeRate();
        double newChargeFee = saveStore.getChargeFee();

        // ?????????????????????????????????????????????
        // ?????????????????????????????????????????????
        final List<StoreChargeInfoRecord> recordList = storeRepository.getStoreChargeInfoRecord(storeOld.id());
        // ??????????????????????????????????????????
        if (recordList == null || recordList.size() == 0) {
            return true;
        } else {
            // ??????????????????????????????
            String yearMonth = DateUtil.getFormatStr(LocalDateTime.now(), "yyyyMM");
            // ?????????????????????????????????????????????useYn???1???finalYn???1???
            List<StoreChargeInfoRecord> currentMonthRecords = recordList.stream()
                    .filter(storeChargeInfoRecord ->
                            (yearMonth.equals(storeChargeInfoRecord.getChargeYearMonth())
                                    && storeChargeInfoRecord.getUseYn() == 1
                                    && storeChargeInfoRecord.getFinalYn() == 1)
                    )
                    .collect(Collectors.toList());

            if (currentMonthRecords != null && currentMonthRecords.size() > 0) {
                StoreChargeInfoRecord currentMonthRecord = currentMonthRecords.get(0);
                int currentMonthChargeType = currentMonthRecord.getChargeType();
                double currentMonthChargeRate = currentMonthRecord.getChargeRate();
                double currentMonthChargeFee = currentMonthRecord.getChargeFee();
                if (newChargeType == currentMonthChargeType && String.valueOf(newChargeRate).equals(String.valueOf(currentMonthChargeRate)) && String.valueOf(newChargeFee).equals(String.valueOf(currentMonthChargeFee))) {
                    // ?????????????????????????????????????????????????????????
                    return false;
                } else {
                    return true;
                }
            } else {
                // ?????????????????????????????????????????????????????????true
                return true;
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param store
     * @param saveStore
     */
    private void saveChargeInfo(Store store, SaveStore saveStore) {
        // ???????????????????????????????????????????????????userYn???0????????????????????????finalYn???0???
        storeRepository.updateStoreChargeInfo(store.id(),
                Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "yyyy")),
                Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "MM")));
        logger.info("??????????????????????????????????????????????????????!");

        // ????????????????????????
        StoreChargeInfoRecord storeChargeInfoRecord = saveStore.buildStoreChargeInfoRecord(store,saveStore);
        storeRepository.saveStoreChargeInfo(storeChargeInfoRecord);
        logger.info("?????????????????????????????????Success!");
    }

    /**
     * ??????????????????table
     * @Param storeId
     * @return com.basoft.eorder.interfaces.command.SaveStoreTable
     * @author Dong Xifu
     * @date 2019/5/30 ??????2:14
     */
    private SaveStoreTable createStoreTableSilent(Long storeId) {
        SaveStoreTable storeTable = new SaveStoreTable();
        storeTable.setStoreId(storeId);
        storeTable.setNumber(0);
        storeTable.setTag("0");
        storeTable.setIsSilent(1);
        storeTable.setOrder(1);
        return storeTable;
    }

    /**
     * ??????????????????
     *
     * @param updateStoreStatus
     * @return
     */
    @CommandHandler.AutoCommandHandler(UpdateStoreStatus.NAME)
    @Transactional
    public int updateStoreStatus(UpdateStoreStatus updateStoreStatus) {
        return this.storeRepository.updateStoreStatus(updateStoreStatus);
    }

    /**
     * ????????????ID ????????????;?????????ID ????????????3,5
     *
     * @param saveBannerCommand
     * @param context
     * @return Banner ID
     */
    @CommandHandler.AutoCommandHandler(SaveBanner.NAME)
    @Transactional
    public Long saveBanner(SaveBanner saveBannerCommand, CommandHandlerContext context){
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Long storeId = us.getStoreId();
        Store store = storeRepository.getStore(storeId);
        if(store==null){
            throw new BizException(ErrorCode.STORE_NULL);
        }

        Long bannerId = saveBannerCommand.getId();
        if(bannerId == null){
            if (saveBannerCommand.getResetIndex()>0) {
                upBannerOrderReset(store.id(),saveBannerCommand.getShowIndex(),saveBannerCommand.getResetIndex());
            }
            bannerId = uidGenerator.generate(BusinessTypeEnum.BANNER);
            Banner banner = saveBannerCommand.build(store,bannerId);
            storeRepository.saveBanner(banner);
            return bannerId;
        }

        if (saveBannerCommand.getResetIndex()>0){
            upBannerOrderReset(store.id(),saveBannerCommand.getShowIndex(),saveBannerCommand.getResetIndex());
        }

        Banner banner = saveBannerCommand.build(store);
        return storeRepository.updateBanner(banner);
    }

    /**
     * ????????????banner??????
     *
     * @param banner
     * @return
     */
    @CommandHandler.AutoCommandHandler(BachUpdateBannerStatus.NAME_UPSTATUS_LIST)
    @Transactional
    public int upbnStatusList(BachUpdateBannerStatus banner) {
        //Store store = (Store) context.props().get("cms_store");
        /*if(store==null){
            new BizException(ErrorCode.STORE_NULL);
        }*/

        int result = storeRepository.updateBannerStatus(Integer.valueOf(banner.getStatus()), banner.getBannerList());
        return result;
    }


    /**
     * @describe ??????banner??????
     *
     * @param  saveBannerCommand
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2018/12/20 ??????11:24
     */
    @CommandHandler.AutoCommandHandler(SaveBanner.NAME_UPSTATUS)
    @Transactional
    public Long upBannerStatus(SaveBanner saveBannerCommand,CommandHandlerContext context){
        Long storeId = (Long)context.props().get("store_id");
        Store store = this.storeRepository.getStore(storeId);

        if(store==null){
            new BizException(ErrorCode.STORE_NULL);
        }
        Long bannerId = saveBannerCommand.getId();

        Map<String,Object> param = new HashedMap();
        param.put("id",bannerId);
        param.put("storeId",store.id());

        Banner banner = storeRepository.getBanner(param);
        int status = banner.getStatus();

        if(status==Banner.Status.OPEN.code()||status==Banner.Status.NORMAL.code()){
            status = Banner.Status.CLOSED.code();
            banner.setStatus(status);
        }
        else if(status==Banner.Status.CLOSED.code()){
            status = Banner.Status.OPEN.code();
            banner.setStatus(status);
        }
        banner.setOrder(saveBannerCommand.getShowIndex());
        return storeRepository.updateBanner(banner);
    }


    /**
     * @describe  ?????????????????????????????????????????????  3???5  ????????? ???3??????5??? 5??????3 ?????????3??? 3?????????5
     *
     * @param  storeId, showIndex,resetIndex
     * @return void
     * @author Dong Xifu
     * @date 2019/1/5 ??????6:31
     */
    private void upBannerOrderReset(Long storeId, int showIndex, int resetIndex) {
        Map<String, Object> param = new HashedMap();
        param.put("storeId", storeId);
        param.put("showIndex", showIndex);
        Banner banner = storeRepository.getBanner(param);
        if (banner != null) {
            //int maxShowIndex = sr.getMaxBannerOrder(storeId);
            banner.setOrder(resetIndex);
            storeRepository.updateBanner(banner);
        }
    }

    /**
     * @describe ????????????banner
     *
     * @param  banner
     * @return int
     * @author Dong Xifu
     * @date 2018/12/20 ??????4:45
     */
    @CommandHandler.AutoCommandHandler(BachUpdateBannerStatus.NAME_DELSTATUS_LIST)
    @Transactional
    public int delbnStatusList(BachUpdateBannerStatus banner) {
        int result = storeRepository.delBannerStatusList(banner.getBannerList());
        return result;
    }

    /**
     *
     * @Param storeId
     * @Param table
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2019/5/30 ??????2:12
     */
    public Long saveStoreTableSilent(SaveStoreTable table,Long storeId) {
       return saveStoreTableBase(table, storeId);
    }

    /**
     *
     * @param table command object
     * @return StoreTable ID
     */
    @CommandHandler.AutoCommandHandler(SaveStoreTable.NAME)
    @Transactional
    public Long saveStoreTable(SaveStoreTable table,CommandHandlerContext context){
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        return saveStoreTableBase(table, us.getStoreId());
    }

    /**
     *
     * @Param storeId
     * @Param table
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2019/5/30 ??????2:10
     */
    public Long saveStoreTableBase(SaveStoreTable table,Long storeId) {
        Store store = storeRepository.getStore(storeId);

        if(table.getId() != null){
            //???????????????????????????
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
        //return sr.updateTable(table.build(store,tableId));
        if(tableCount(store)>=store.tableCount())
            throw new BizException(ErrorCode.STORE_TABLE_FULL);
        List<StoreTable> tables = storeRepository.getStoreTableList(store);
        final StoreTable storeTable = tables.stream()
            .filter((StoreTable inStoreTable) -> {
                return inStoreTable.number() == table.getNumber()
                    && inStoreTable.tag().equalsIgnoreCase(table.getTag());
            })
            .findFirst()
            .orElseGet(() -> null);

        if (storeTable != null) {
            logger.warn("?????????????????????" + storeTable.id());
            throw new BizException(ErrorCode.BIZ_EXCEPTION, "Table number repeat!");
        }

        logger.info("Start create new Table");

        Long tableId = uidGenerator.generate(BusinessTypeEnum.STORE_TABLE);
        StoreTable sTable = table.build(store, tableId);
       return storeRepository.saveTable(sTable);
    }

    /**
     * ??????????????????
     *
     * @Param store
     * @return int
     * @author Dong Xifu
     * @date 2019/6/13 ??????4:35
     */
    private int tableCount(Store store) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", store.id());
        param.put("status",StoreTable.Status.NORMAL);
        return storeTableQuery.getStoreTableCount(param);
    }

    private static final String TABLE_ID_PROP_NAME = "table_id";

    /**
     * @describe ????????????table
     *
     * @param  table
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2018/12/24 ??????1:06
     */
    @CommandHandler.AutoCommandHandler(BatchGenerateStoreTable.NAME)
    @Transactional
    public int saveStoreTableList(BatchGenerateStoreTable table, CommandHandlerContext context) {
        logger.info("\n\n\n");
        logger.info("Start saveStoreTable List");

        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);



        Store store = storeRepository.getStore(us.getStoreId());
        if (store == null) {
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }


        int sumTable = table.getEndNum()-table.getStartNum()+1;//?????????????????????
        if(tableCount(store)+sumTable>=store.tableCount())
            throw new BizException(ErrorCode.STORE_TABLE_FULL);

        List<StoreTable> tables = storeRepository.getStoreTableList(store);
        Map<Long, SaveStoreTable> newTables = new HashMap<>();

        for (int i = table.getStartNum(); i <= table.getEndNum(); i++) {
            final int ii = i;
            final StoreTable table1 =
                    tables.stream()
                            .filter((StoreTable ss) -> {
                                return ss.tag().equalsIgnoreCase(table.getTag()) && ss.number() == ii;
                            })
                            .findFirst()
                            .orElseGet(() -> null);

            if (table1 == null) {
                SaveStoreTable storeTableBatch = new SaveStoreTable();
                Long tableId = uidGenerator.generate(BusinessTypeEnum.STORE_TABLE);

                storeTableBatch.setId(tableId);
                storeTableBatch.setStoreId(store.id());
                storeTableBatch.setNumber(i);
                storeTableBatch.setOrder(i);
                storeTableBatch.setMaxSeat(table.getMaxSeat());
                storeTableBatch.setTag(table.getTag());
                newTables.put(tableId, storeTableBatch);
            }
        }

        final List<StoreTable> tableList = newTables.values().stream()
                .map(new Function<SaveStoreTable, StoreTable>() {
                    @Override
                    public StoreTable apply(SaveStoreTable saveStoreTable) {
                        return saveStoreTable.build(store, saveStoreTable.getId());
                    }
                })
                .collect(Collectors.toList());

        return storeRepository.saveTableList(tableList);
    }

    /**
     * @describe ???????????? table
     *
     * @param  delStoreTable
     * @return int
     * @author Dong Xifu
     * @date 2018/12/24 ??????5:19
     */
    @CommandHandler.AutoCommandHandler(DelStoreTable.NAME)
    @Transactional
    public int delStoreTable(DelStoreTable delStoreTable, CommandHandlerContext context) {
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = storeRepository.getStore(us.getStoreId());
        //DelStoreTable delStoreTable = new DelStoreTable();
        delStoreTable.setStoreId(store.id());
        return storeRepository.deleteStoreTableBatch(delStoreTable);
    }

    /**
     * ?????????????????????????????????????????????-Manager CMS
     *
     * @param updateStore,context
     * @return store id
     * @author woonill
     */
    @CommandHandler.AutoCommandHandler(UpdateStore.NAME)
    @Transactional
    public Long updateStore(UpdateStore updateStore, CommandHandlerContext context) {
        // ????????????????????????
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = this.storeRepository.getStore(us.getStoreId());
        Store newStore = updateStore.build(store);

        // ??????manager user
        User manager = userRepo.getUser(store.manager().getId());
        User newUser = updateStore.buildManager(manager);
        this.userRepo.updateUser(newUser);

        // ????????????????????????????????????????????????(?????????????????????????????????????????????????????????????????????????????????????????????????????????Insert??????Category)
        List<StoreCategory> storeCategoryList = updateStore.buildStoreCategoryList(store);
        if (storeCategoryList != null && storeCategoryList.size() > 0) {
            this.storeRepository.updateStoreCategoryMap(store.id(), storeCategoryList);
        }

        // ??????????????????
        Long storeId = this.storeRepository.updateStore(newStore).id();

        selfService(updateStore.getIsSelfservice(), updateStore.getSelfserviceUseyn(), storeId);

        return storeId;
    }

    /**
     * ????????????
     *
     * @return void
     * @Param storeId
     * @Param updateStore
     * @author Dong Xifu
     * @date 2019/5/31 ??????3:53
     */
    private void selfService(int isSelfservice, int selfserviceUseyn, Long storeId) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", storeId);
        param.put("isSilent", "1");
        //?????????????????????????????????????????????
        int tableCount = storeTableQuery.getStoreTableCount(param);

        if (1 > tableCount && Store.IS_SELFSERVICE_ALLOW == isSelfservice && Store.IS_SELFSERVICE_ALLOW == selfserviceUseyn) {
            SaveStoreTable saveStoreTable = createStoreTableSilent(storeId);
            saveStoreTableSilent(saveStoreTable, storeId);
        } else if (1 == tableCount) {
            if (Store.IS_SELFSERVICE_BAN == isSelfservice || Store.IS_SELFSERVICE_BAN == selfserviceUseyn) {
                logger.info("??????????????????");
                StoreTableDTO tableDTO = storeTableQuery.getStoreTableByMap(param);
                if (tableDTO != null) {
                    DelStoreTable status = new DelStoreTable();
                    List<Long> longs = new LinkedList<>();
                    longs.add(tableDTO.getId());
                    status.setStoreTables(longs);
                    status.setStoreId(storeId);
                    status.setStatus(StoreTable.Status.DELETED.code());
                    storeRepository.deleteStoreTableBatch(status);
                }
            } else if (Store.IS_SELFSERVICE_ALLOW == isSelfservice && Store.IS_SELFSERVICE_ALLOW == selfserviceUseyn) {
                logger.info("??????????????????");
                StoreTableDTO tableDTO = storeTableQuery.getStoreTableByMap(param);
                if (tableDTO != null) {
                    if (StoreTable.Status.DELETED.code() == tableDTO.getStatus()) {
                        DelStoreTable status = new DelStoreTable();
                        List<Long> longs = new LinkedList<>();
                        longs.add(tableDTO.getId());
                        status.setStoreTables(longs);
                        status.setStoreId(storeId);
                        status.setStatus(StoreTable.Status.NORMAL.code());
                        storeRepository.deleteStoreTableBatch(status);
                    }
                }
            }
        }
    }

    public String buildQrcodeFileName(Long id, Long tableId) {
        return "qrcode-" + id + "-" + tableId;
    }

    public String toQRCodeContent(Long storeId, Long tableId) {
        return "http://" + tableId;
    }

    /**
     * ?????????????????????QRCode
     *
     * @param generateTableQRCode,context
     * @return store id
     * @author woonill
     */
    @CommandHandler.AutoCommandHandler(GenerateTableQRCode.NAME)
    @Transactional
    @ResponseBody
    public Map<Long, String> generateTableQRCode(
            GenerateTableQRCode generateTableQRCode,
            CommandHandlerContext context) {
        //step1: ??????????????????????????????ID
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = this.storeRepository.getStore(us.getStoreId());

        //step2: ??????????????????????????????????????????
        List<Long> tableIdList = generateTableQRCode.getTableIds();

        if (tableIdList != null && tableIdList.size() > 0) {

            //step3: ?????????????????????????????????
            List<StoreTable> allTableList = storeRepository.getQrStoreTableList(store);
            if (allTableList != null && allTableList.size() > 0) {

                //step4: ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                List<StoreTable> searchTableList = allTableList.stream().filter(table -> tableIdList.contains(table.id())).collect(Collectors.toList());
                if (searchTableList != null && tableIdList.size() == searchTableList.size()) {

                    //step5: ?????????????????????????????????????????????????????????????????????????????????QRCode???
                    List<StoreTable> noQrcodeList = searchTableList.stream().filter(table -> StringUtils.isBlank(table.getQrCodeId()) && StringUtils.isBlank(table.getTicket())).collect(Collectors.toList());
                    if (noQrcodeList == null || noQrcodeList.size() == 0) {
                        //step6-END ????????????????????????????????????????????????????????????????????????
                        return searchTableList.stream().collect(Collectors.toMap(StoreTable::getId, table -> WechatAPI.getInstance().getShowqrcodeUrl() + table.getTicket(), (k1, k2) -> k1));
                    }

                    //step7?????????????????????????????????????????????????????????????????????????????????????????????
                    List<StoreTable> newTableList = wechatService.matchGenerateWechatQRCodes(noQrcodeList);

                    //step8 ?????????????????????????????????????????????QRCODE?????????
                    final List<StoreTable> collect = searchTableList.stream().map(st -> {
                        if (StringUtils.isNotBlank(st.getQrCodeId()) && StringUtils.isNotBlank(st.getTicket())) {
                            return new StoreTable().createStoreTable(st).build();
                        } else {
                            StoreTable table = newTableList.stream().filter(newTable -> newTable.id() == st.id()).findFirst().get();
                            return new StoreTable().createStoreTable(table).build();
                        }
                    }).collect(Collectors.toList());

                    //step9-END
                    return collect.stream().collect(Collectors.toMap(StoreTable::getId, table -> WechatAPI.getInstance().getShowqrcodeUrl() + table.getTicket(), (k1, k2) -> k1));
                } else {
                    logger.error("<<<<<< searchTableList is null or search param tableIdList not equals searchTableList >>>>>>");
                    throw new BizException(ErrorCode.BIZ_EXCEPTION);
                }
            } else {
                logger.error("<<<<<< this store not have table!!!  store : [" + store.toString() + "] >>>>>>");
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        } else {
            logger.error("<<<<<< param is null >>>>>>");
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
    }

    /**
     * ba???????????????
     *
     * @param upSettleStatus
     * @return
     */
    @CommandHandler.AutoCommandHandler(UpSettleStatus.NAME)
    @Transactional
    public int upSettleStatus(UpSettleStatus upSettleStatus,CommandHandlerContext context) {

        if (StringUtils.isBlank(upSettleStatus.getClosingMonths())) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        upSettleStatus.setStatus(UpSettleStatus.CLOSSTATUS_RECEIVE);
        Map<String, Object> param = new HashMap<>();
        param.put("storeId",upSettleStatus.getStoreId());
        param.put("closingMonths", upSettleStatus.getClosingMonths());
        int count = orderRepository.getClosing(param);
        if (count < 1) {
            orderRepository.insertClosing(upSettleStatus);
        } else {
            orderRepository.upSettleStatus(upSettleStatus);
        }
        return orderRepository.upSettleStatus(upSettleStatus);
    }
    

    /**
     * Save ShipPoint
     * <p>
     * ????????????ID ????????????
     * ?????????ID ????????????
     * </p>
     *
     * @param saveShipPoint
     * @return Banner ID
     */
    @CommandHandler.AutoCommandHandler(SaveShipPoint.NAME)
    @Transactional
    public Long saveShipPoint(SaveShipPoint saveShipPoint) {
        
        Long shipPointid = saveShipPoint.getShipPointid();

        // ??????
        if (shipPointid == null) {
        	shipPointid = uidGenerator.generate(BusinessTypeEnum.SHIP_POINT);
            ShipPoint shipPoint = saveShipPoint.build(shipPointid);
            return this.storeRepository.saveShipPoint(shipPoint);
        }
        // ??????
        else {
        	ShipPoint shipPoint = saveShipPoint.build(shipPointid);
            return this.storeRepository.updateShipPoint(shipPoint).shipPointid();
        }
    }

    /**
     * Update ShipPoint status
     *
     * @param updateShipPointStatus
     * @return
     */
    @CommandHandler.AutoCommandHandler(UpdateShipPointStatus.NAME)
    @Transactional
    public int updateShipPointStatus(UpdateShipPointStatus updateShipPointStatus) {
        return this.storeRepository.updateShipPointStatus(updateShipPointStatus);
    }    
    
}
