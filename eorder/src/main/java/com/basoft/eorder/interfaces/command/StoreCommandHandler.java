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
     * 如果没有ID 就是新建
     * 如果有ID 就是更新
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

        // 新建商户
        if (id == null) {
            id = uidGenerator.generate(BusinessTypeEnum.STORE);

            // 验证商户管理员是否被占用
            long managerId = saveStore.getManagerId();
            List<Store> storeList = storeRepository.getStoreList(managerId);
            if (storeList != null && storeList.size() > 0) {
                throw new BizException(ErrorCode.STORE_MANAGER_EXIT);
            }
            // 查询User对象
            User user = userRepo.getUser(saveStore.getManagerId());

            // 存储商户和商户商品分类及商户标签的关联关系
            Store store = saveStore.build(new Store(), user, id);
            List<StoreCategory> storeCategoryList = saveStore.buildStoreCategoryList(store);
            if (storeCategoryList != null && storeCategoryList.size() > 0) {
                this.storeRepository.updateStoreCategoryMap(id, storeCategoryList);
            }

            // 保存商户
            Long storeId = this.storeRepository.saveStore(store);

            //如果允许默认新增一个餐桌
            if (Store.IS_SELFSERVICE_ALLOW == saveStore.getSelfserviceUseyn() && Store.IS_SELFSERVICE_ALLOW == saveStore.getIsSelfservice()) {
                SaveStoreTable storeTable = createStoreTableSilent(storeId);
                saveStoreTableSilent(storeTable, id);
            }
            return storeId;
        }
        // 修改商户
        else {
            // 验证商户管理员是否被占用
            long managerId = saveStore.getManagerId();
            List<Store> storeList = storeRepository.getStoreList(managerId);
            // 修改的管理员如果在管理商户，并且不是当前商户
            if (storeList != null && storeList.size() > 0) {
                for(Store store : storeList){
                    if(store.id().longValue() != id.longValue()){
                        throw new BizException(ErrorCode.STORE_MANAGER_EXIT);
                    }
                }
            }

            // 查询User对象
            User user = userRepo.getUser(saveStore.getManagerId());

            // 获取数据库中当前商户信息
            Store storeOld = storeRepository.getStore(id);

            // 处理计费信息。说明新增商户时不需要对计费信息进行额外处理。
            boolean isSaveChargeInfo = isSaveChargeInfo(saveStore, storeOld);
            logger.info("处理计费信息，是否进行计费信息保存>>>" + isSaveChargeInfo);
            if(isSaveChargeInfo){
                saveChargeInfo(storeOld, saveStore);
            }

            // 保存商户信息
            Store store = saveStore.build(storeOld, user, id);
            // 商户信息保存保持原计费信息不变
            store.setChargeType(storeOld.chargeType());
            store.setChargeRate(storeOld.chargeRate());
            store.setChargeFee(storeOld.chargeFee());
            Long storeId = this.storeRepository.updateStore(store).id();

            // 更新商户和商户商品分类及商户标签的关联关系
            List<StoreCategory> storeCategoryList = saveStore.buildStoreCategoryList(store);
            if (storeCategoryList != null && storeCategoryList.size() > 0) {//这部分如果最终定下来就要判断不可为空
                this.storeRepository.updateStoreCategoryMap(id, storeCategoryList);
            }

            //是否启用自取
            selfService(saveStore.getIsSelfservice(), saveStore.getSelfserviceUseyn(), id);

            return storeId;
        }
    }

    /**
     * 处理商户的计费信息
     *
     * @param saveStore 计费信息
     * @param storeOld
     */
    private boolean isSaveChargeInfo(SaveStore saveStore, Store storeOld) {
        // 第一步：待保存的明细信息
        int newChargeType = saveStore.getChargeType();
        double newChargeRate = saveStore.getChargeRate();
        double newChargeFee = saveStore.getChargeFee();

        // 第二步：获取次月的计费修改记录
        // 获取商户的所有计费信息修改记录
        final List<StoreChargeInfoRecord> recordList = storeRepository.getStoreChargeInfoRecord(storeOld.id());
        // 该商户不存在计费信息修改记录
        if (recordList == null || recordList.size() == 0) {
            return true;
        } else {
            // 获取次的计费修改记录
            String yearMonth = DateUtil.getFormatStr(LocalDateTime.now(), "yyyyMM");
            // 过滤出次月并且最后的有效版本（useYn为1且finalYn为1）
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
                    // 待保存的计费信息次月已经修改且生效了。
                    return false;
                } else {
                    return true;
                }
            } else {
                // 没有次月的计费修改记录，需要保存，返回true
                return true;
            }
        }
    }

    /**
     * 保存商户的计费信息
     *
     * @param store
     * @param saveStore
     */
    private void saveChargeInfo(Store store, SaveStore saveStore) {
        // 更新当前月的计费修改记录为不可用（userYn为0）且不是最终版（finalYn为0）
        storeRepository.updateStoreChargeInfo(store.id(),
                Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "yyyy")),
                Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "MM")));
        logger.info("更新商户的计费信息【更新至作废状态】!");

        // 保存新的计费信息
        StoreChargeInfoRecord storeChargeInfoRecord = saveStore.buildStoreChargeInfoRecord(store,saveStore);
        storeRepository.saveStoreChargeInfo(storeChargeInfoRecord);
        logger.info("保存商户的新的计费信息Success!");
    }

    /**
     * 封装自取餐厅table
     * @Param storeId
     * @return com.basoft.eorder.interfaces.command.SaveStoreTable
     * @author Dong Xifu
     * @date 2019/5/30 下午2:14
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
     * 启用禁用商店
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
     * 如果没有ID 就是新建;如果有ID 就是更新3,5
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
     * 批量修改banner状态
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
     * @describe 修改banner状态
     *
     * @param  saveBannerCommand
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2018/12/20 上午11:24
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
     * @describe  把将要被即将站用的序号重置序号  3，5  修改时 将3变成5。 5变成3 若没有3， 3不用变5
     *
     * @param  storeId, showIndex,resetIndex
     * @return void
     * @author Dong Xifu
     * @date 2019/1/5 下午6:31
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
     * @describe 批量删除banner
     *
     * @param  banner
     * @return int
     * @author Dong Xifu
     * @date 2018/12/20 下午4:45
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
     * @date 2019/5/30 下午2:12
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
     * @date 2019/5/30 下午2:10
     */
    public Long saveStoreTableBase(SaveStoreTable table,Long storeId) {
        Store store = storeRepository.getStore(storeId);

        if(table.getId() != null){
            //业务定义无更新操作
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
            logger.warn("桌号重复：：：" + storeTable.id());
            throw new BizException(ErrorCode.BIZ_EXCEPTION, "Table number repeat!");
        }

        logger.info("Start create new Table");

        Long tableId = uidGenerator.generate(BusinessTypeEnum.STORE_TABLE);
        StoreTable sTable = table.build(store, tableId);
       return storeRepository.saveTable(sTable);
    }

    /**
     * 查询餐桌数量
     *
     * @Param store
     * @return int
     * @author Dong Xifu
     * @date 2019/6/13 下午4:35
     */
    private int tableCount(Store store) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", store.id());
        param.put("status",StoreTable.Status.NORMAL);
        return storeTableQuery.getStoreTableCount(param);
    }

    private static final String TABLE_ID_PROP_NAME = "table_id";

    /**
     * @describe 批量新增table
     *
     * @param  table
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2018/12/24 下午1:06
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


        int sumTable = table.getEndNum()-table.getStartNum()+1;//待新增餐桌总数
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
     * @describe 批量删除 table
     *
     * @param  delStoreTable
     * @return int
     * @author Dong Xifu
     * @date 2018/12/24 下午5:19
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
     * 对应店主更新商店和管理人员信息-Manager CMS
     *
     * @param updateStore,context
     * @return store id
     * @author woonill
     */
    @CommandHandler.AutoCommandHandler(UpdateStore.NAME)
    @Transactional
    public Long updateStore(UpdateStore updateStore, CommandHandlerContext context) {
        // 构造新的门店信息
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = this.storeRepository.getStore(us.getStoreId());
        Store newStore = updateStore.build(store);

        // 修改manager user
        User manager = userRepo.getUser(store.manager().getId());
        User newUser = updateStore.buildManager(manager);
        this.userRepo.updateUser(newUser);

        // 更新商品分类及商户标签的关联关系(虽然修改商户只修改标签，并且是商户可以管理的标签，但是此处更新操作重新Insert所有Category)
        List<StoreCategory> storeCategoryList = updateStore.buildStoreCategoryList(store);
        if (storeCategoryList != null && storeCategoryList.size() > 0) {
            this.storeRepository.updateStoreCategoryMap(store.id(), storeCategoryList);
        }

        // 更新商户信息
        Long storeId = this.storeRepository.updateStore(newStore).id();

        selfService(updateStore.getIsSelfservice(), updateStore.getSelfserviceUseyn(), storeId);

        return storeId;
    }

    /**
     * 自助取餐
     *
     * @return void
     * @Param storeId
     * @Param updateStore
     * @author Dong Xifu
     * @date 2019/5/31 下午3:53
     */
    private void selfService(int isSelfservice, int selfserviceUseyn, Long storeId) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", storeId);
        param.put("isSilent", "1");
        //如果修改为允许外送新增一个餐桌
        int tableCount = storeTableQuery.getStoreTableCount(param);

        if (1 > tableCount && Store.IS_SELFSERVICE_ALLOW == isSelfservice && Store.IS_SELFSERVICE_ALLOW == selfserviceUseyn) {
            SaveStoreTable saveStoreTable = createStoreTableSilent(storeId);
            saveStoreTableSilent(saveStoreTable, storeId);
        } else if (1 == tableCount) {
            if (Store.IS_SELFSERVICE_BAN == isSelfservice || Store.IS_SELFSERVICE_BAN == selfserviceUseyn) {
                logger.info("静默餐桌禁止");
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
                logger.info("静默餐桌启用");
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
     * 批量生成餐桌的QRCode
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
        //step1: 获取登录用户所属商户ID
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        Store store = this.storeRepository.getStore(us.getStoreId());

        //step2: 传入参数：待下载二维码的桌号
        List<Long> tableIdList = generateTableQRCode.getTableIds();

        if (tableIdList != null && tableIdList.size() > 0) {

            //step3: 查询商户的所有桌子信息
            List<StoreTable> allTableList = storeRepository.getQrStoreTableList(store);
            if (allTableList != null && allTableList.size() > 0) {

                //step4: 过滤：从全部桌子里面过滤出传入桌子来，并进行参数比对，验证传入桌子是否都合法
                List<StoreTable> searchTableList = allTableList.stream().filter(table -> tableIdList.contains(table.id())).collect(Collectors.toList());
                if (searchTableList != null && tableIdList.size() == searchTableList.size()) {

                    //step5: 继续过滤：验证传入桌子都合法后，过滤出这些传入桌子没有QRCode的
                    List<StoreTable> noQrcodeList = searchTableList.stream().filter(table -> StringUtils.isBlank(table.getQrCodeId()) && StringUtils.isBlank(table.getTicket())).collect(Collectors.toList());
                    if (noQrcodeList == null || noQrcodeList.size() == 0) {
                        //step6-END 传入桌子都已经下载过二维码，则直接返回二维码信息
                        return searchTableList.stream().collect(Collectors.toMap(StoreTable::getId, table -> WechatAPI.getInstance().getShowqrcodeUrl() + table.getTicket(), (k1, k2) -> k1));
                    }

                    //step7：存在桌子没有二维码。桌子和二维码进行匹配，生成新的餐桌二维码
                    List<StoreTable> newTableList = wechatService.matchGenerateWechatQRCodes(noQrcodeList);

                    //step8 重新生成桌子列表：更新新生成的QRCODE的数据
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
     * ba确认收到钱
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
     * 如果没有ID 就是新建
     * 如果有ID 就是更新
     * </p>
     *
     * @param saveShipPoint
     * @return Banner ID
     */
    @CommandHandler.AutoCommandHandler(SaveShipPoint.NAME)
    @Transactional
    public Long saveShipPoint(SaveShipPoint saveShipPoint) {
        
        Long shipPointid = saveShipPoint.getShipPointid();

        // 新建
        if (shipPointid == null) {
        	shipPointid = uidGenerator.generate(BusinessTypeEnum.SHIP_POINT);
            ShipPoint shipPoint = saveShipPoint.build(shipPointid);
            return this.storeRepository.saveShipPoint(shipPoint);
        }
        // 修改
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
