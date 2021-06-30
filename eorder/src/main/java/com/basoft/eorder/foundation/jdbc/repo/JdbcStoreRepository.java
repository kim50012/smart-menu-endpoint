package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Banner;
import com.basoft.eorder.domain.model.QRCode;
import com.basoft.eorder.domain.model.ShipPoint;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreAttach;
import com.basoft.eorder.domain.model.StoreCategory;
import com.basoft.eorder.domain.model.StoreDynamic;
import com.basoft.eorder.domain.model.StoreExtend;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.domain.model.inventory.hotel.StoreDayPrice;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.domain.model.topic.StoreTopic;
import com.basoft.eorder.foundation.jdbc.eventhandler.BannerEvent;
import com.basoft.eorder.interfaces.command.DelStoreTable;
import com.basoft.eorder.interfaces.command.UpdateShipPointStatus;
import com.basoft.eorder.interfaces.command.UpdateStoreStatus;
import com.basoft.eorder.util.UidGenerator;
import com.google.common.collect.Maps;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Transactional
public class JdbcStoreRepository extends BaseRepository implements StoreRepository {
    private BaseService baseService;
    @Autowired
    private UidGenerator uidGenerator;

    JdbcStoreRepository(){}

    @Autowired
    public JdbcStoreRepository(DataSource ds,BaseService baserService){
        super(ds);
        this.baseService = baserService;
    }

    @Override
    public Store getStore(Long id) {
        final List<Store> stores = this.getJdbcTemplate().query(
                "select s.*,"+
                        "  u.name as manager_name,u.id as managerIdd " +
                        " from store s " +
                        "  join user u on s.manager_id = u.id" +
                        " where s.id = ?",
                new Object[]{id},
                new RowMapper<Store>() {
            @Override
            public Store mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Store.Builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .city(resultSet.getString("city"))
                        .detailAddr(resultSet.getString("detail_addr"))
                        .mobile(resultSet.getString("mobile"))
                        .ceoName(resultSet.getString("ceo_name"))
                        .bizScope(resultSet.getString("biz_scope"))
                        .manager(new User.Builder().id(resultSet.getLong("managerIdd")).name(resultSet.getString("manager_name")).build())
                        .merchantId(resultSet.getString("merchant_id"))
                		.merchantNm(resultSet.getString("merchant_nm"))
                		.gatewayPw(resultSet.getString("gateway_pw"))
                		.paymentMethod(resultSet.getString("payment_method"))
                		.currency(resultSet.getString("currency"))
                		.transidType(resultSet.getString("transid_type"))
                		.appid(resultSet.getString("appid"))
                		.mchId(resultSet.getString("mch_id"))
                		.subAppid(resultSet.getString("sub_appid"))
                		.subMchid(resultSet.getString("sub_mch_id"))
                		.apiKey(resultSet.getString("api_key"))
                		.storeType(resultSet.getInt("store_type"))
                        .isSelfservice(resultSet.getInt("IS_SELFSERVICE"))
                        .isDelivery(resultSet.getInt("IS_DELIVERY"))
                        .selfserviceUseyn(resultSet.getInt("SELFSERVICE_USEYN"))
                        .deliveryUseyn(resultSet.getInt("DELIVERY_USEYN"))
                        .isPaySet(resultSet.getInt("IS_PAY_SET"))
                        .isOpening(resultSet.getInt("IS_OPENING"))
                        .isSegmented(resultSet.getInt("IS_SEGMENTED"))
                        .morningSt(resultSet.getString("MORNING_ST"))
                        .morningEt(resultSet.getString("MORNING_ET"))
                        .noonSt(resultSet.getString("NOON_ST"))
                        .noonEt(resultSet.getString("NOON_ET"))
                        .eveningSt(resultSet.getString("EVENING_ST"))
                        .eveningEt(resultSet.getString("EVENING_ET"))
                        .afternoonSt(resultSet.getString("AFTERNOON_ST"))
                        .afternoonEt(resultSet.getString("AFTERNOON_ET"))
                        .midnightSt(resultSet.getString("MIDNIGHT_ST"))
                        .midnightEt(resultSet.getString("MIDNIGHT_ET"))
                        .chargeType(resultSet.getInt("CHARGE_TYPE"))
                        .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                        .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                        .tableCount(resultSet.getInt("TABLE_COUNT"))
                        .cashSettleType(resultSet.getString("cash_settle_type"))
                        .prodPriceType(resultSet.getInt("prod_price_type"))
                        .build();
            }
        });
        return stores.isEmpty() ? null : stores.get(0);
    }

    @Override
    public void saveBanner(Banner banner) {
        this.getJdbcTemplate().update("insert into banners (id,name,image_path,store_id,show_index,status)" +
                "   values(?,?,?,?,?,?)",new Object[]{
                banner.id(),
                banner.name(),
                banner.imageUrl(),
                banner.storeId(),
                banner.order(),
                banner.getStatus()
        });
    }

    @Override
    public int getMaxBannerOrder(Long storeId) {
        Map<String,Object> param = new HashedMap();
        param.put("storeId",storeId);
        String sql = "select ifnull(max(show_index),0) as showIndex from banners where store_id= :storeId ";
        Integer maxShowIndex = this.getNamedParameterJdbcTemplate().queryForObject(sql,param, Integer.class);
        return maxShowIndex;
    }

    @Override
    public Long saveStore(Store store) {
        this.getJdbcTemplate().update("insert into store (id,name,city,mobile,ceo_name,biz_scope,manager_id," +
                "detail_addr,detail_addr_chn,shop_hour,description,description_chn," +
                "longitude,latitude,status,merchant_id,merchant_nm,gateway_pw,transid_type,store_type," +
                "IS_SELFSERVICE,IS_DELIVERY,SELFSERVICE_USEYN,DELIVERY_USEYN,IS_PAY_SET,IS_OPENING,IS_SEGMENTED,MORNING_ST," +
                "MORNING_ET,NOON_ST,NOON_ET,EVENING_ST,EVENING_ET,AFTERNOON_ST,AFTERNOON_ET,MIDNIGHT_ST,MIDNIGHT_ET," +
                "CHARGE_TYPE,CHARGE_RATE,CHARGE_FEE,TABLE_COUNT,SETTLE_RATE_PRICE_ON,CASH_SETTLE_TYPE,PROD_PRICE_TYPE,ST_BANK_NAME,ST_BANK_ACC,ST_BANK_ACC_NAME,IS_JOIN)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?  ,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,?, ?,?,?,?,?,?,?)",
            new Object[]{
                store.id(),
                    store.name(),
                    store.contact().getCity(),
                    // store.contact().getEmail(),
                    store.contact().getMobile(),
                    store.companyInfo().getCeo(),
                    store.companyInfo().getScope(),
                    store.manager().getId(),
                    store.contact().getDetailAddr(),
                    store.contact().getDetailAddrChn(),
                    store.companyInfo().getShopHour(),
                    store.getDescription(),
                    store.getDescriptionChn(),
                    store.longitude(),
                    store.latitude(),
                    store.getStatus(),
                    store.merchantId(),
                    store.merchantNm(),
                    store.gatewayPw(),
                    store.transidType(),
                    store.storeType(),
                    store.isSelfservice(),
                    store.isDelivery(),
                    store.selfserviceUseyn(),
                    store.deliveryUseyn(),
                    store.isPaySet(),
                    1,
                    store.isSegmented(),
                    store.morningSt(),
                    store.morningEt(),
                    store.noonSt(),
                    store.noonEt(),
                    store.eveningSt(),
                    store.eveningEt(),
                    store.afternoonSt(),
                    store.afternoonEt(),
                    store.midnightSt(),
                    store.midnightEt(),
                    store.chargeType(),
                    store.chargeRate(),
                    store.chargeFee(),
                    store.tableCount(),
                    store.chargeRatePriceOn(),
                    store.cashSettleType(),
                    store.prodPriceType(),
                    store.stBankName(),
                    store.stBankAcc(),
                    store.stBankAccName(),
                    store.isJoin()

            });

        saveStoreChild(store);//保存storeExtend,storeAttach,storeTopicList

        return store.id();
    }

    private void saveStoreChild(Store store) {
        List<StoreExtend> storeExtendList = store.storeExtendList();
        List<StoreAttach> storeAttachList = store.storeAttachList();
        List<StoreTopic> topicList = store.topicList();
        if(storeExtendList!=null&&storeExtendList.size()>0){
            saveStoreExtend(store.id(),storeExtendList);
        }
        if (storeAttachList != null && storeAttachList.size() > 0) {
            saveStoreAttach(store.id(), storeAttachList);
        }
        if (topicList != null && topicList.size() > 0) {
            saveStoreTopic(store.id(), topicList);
        }
    }

    private void saveStoreTopic(Long storeId, List<StoreTopic> topicList) {
        String delSql = "delete from store_topic  where store_id = ?";
        this.getJdbcTemplate().update(delSql, new Object[]{storeId});

        String saveSql = "insert into store_topic (store_id, TP_ID, CREATE_TIME,\n" +
            "CREATE_USER)\n" +
            "values(?,?,now(),?)";

        final int[] ints = this.getJdbcTemplate().batchUpdate(saveSql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    int k=1;
                    StoreTopic storeTopic = topicList.get(i);
                    preparedStatement.setLong(k++, storeTopic.getStoreId());
                    preparedStatement.setLong(k++, storeTopic.getTpId());
                    preparedStatement.setString(k++,storeTopic.getCreateUser());
                }

                @Override
                public int getBatchSize() {
                    return topicList.size();
                }
            });
    }

    private void saveStoreAttach(Long storeId, List<StoreAttach> storeAttachList) {

        String delSql = "delete from store_attach  where store_id = ?";
        this.getJdbcTemplate().update(delSql, new Object[]{storeId});

        String saveSql = "insert into store_attach (STORE_ATTACH_ID, STORE_ID, CONTENT_ID,\n" +
            "CONTENT_URL, DISPLAY_ORDER, IS_DISPLAY,\n" +
            "ATTACH_TYPE, CREATED_DT)\n" +
            "values(?,?,?,?,?,?,?,now())";

        final int[] ints = this.getJdbcTemplate().batchUpdate(saveSql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long attachId = uidGenerator.generate(BusinessTypeEnum.STORE_ATTACH);
                    int k=1;
                    StoreAttach storeAttach = storeAttachList.get(i);
                    preparedStatement.setLong(k++, attachId);
                    preparedStatement.setLong(k++, storeAttach.getStoreId());
                    preparedStatement.setString(k++, storeAttach.getContentId());
                    preparedStatement.setString(k++, storeAttach.getContentUrl());
                    preparedStatement.setInt(k++, storeAttach.getDisplayOrder());
                    preparedStatement.setBoolean(k++, true);
                    preparedStatement.setInt(k++, storeAttach.getAttachType());
                }

                @Override
                public int getBatchSize() {
                    return storeAttachList.size();
                }
            });
    }

    private void saveStoreExtend(Long storeId,List<StoreExtend> storeExtendList) {
        String delSql = "delete from store_extend  where store_id = ?";
        this.getJdbcTemplate().update(delSql, new Object[]{storeId});

        String saveSql = "insert into store_extend (STORE_ID,FD_GROUP_NM,FD_GROUP_ID,FD_NAME,FD_ID," +
            "FD_VAL_NAME,FD_VAL_CODE,STATUS,CREATE_TIME,CREATE_USER)" +
            "values(?,?,?,?,?,?,?,?,now(),?)";
        final int[] ints = this.getJdbcTemplate().batchUpdate(saveSql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    int k=1;
                    StoreExtend storeExtend = storeExtendList.get(i);
                    preparedStatement.setLong(k++, storeExtend.getStoreId());
                    preparedStatement.setString(k++, storeExtend.getFdGroupNm());
                    preparedStatement.setString(k++, storeExtend.getFdGroupId());
                    preparedStatement.setString(k++, storeExtend.getFdName());
                    preparedStatement.setString(k++, storeExtend.getFdId());
                    preparedStatement.setString(k++, storeExtend.getFdValName());
                    preparedStatement.setString(k++, storeExtend.getFdValCode());
                    preparedStatement.setInt(k++, 1);
                    preparedStatement.setString(k++, "ba");
                }

                @Override
                public int getBatchSize() {
                    return storeExtendList.size();
                }
            });
    }

    @Override
    public Long deleteStore(Long storeId) {
        this.getJdbcTemplate().update("delete from store where id = ?",new Object[]{storeId});
        return storeId;
    }

    @Override
    public int updateStoreStatus(UpdateStoreStatus storeStatus) {
        this.getJdbcTemplate().batchUpdate("update  store set status = ? where id= ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long id = storeStatus.getStoreIds().get(i);
                    preparedStatement.setLong(1,storeStatus.getStatus());
                    preparedStatement.setLong(2,id);
                }

                @Override
                public int getBatchSize() {
                    return storeStatus.getStoreIds().size();
                }
            }
        );
        return storeStatus.getStoreIds().size();
    }

    @Override
    public Banner getBanner(Map<String,Object> param) {
        StringBuilder sql = new StringBuilder();
            sql.append("select * from banners where 1=1");

        Long id = NumberUtils.toLong(Objects.toString(param.get("id"), null));
        Long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String showIndex = Objects.toString(param.get("showIndex"), null);

        if (id>0) {
            sql.append(" and id = :id \n");
        }

        if (storeId>0) {
            sql.append(" and store_id = :storeId \n");
        }

        if (StringUtils.isNotBlank(showIndex)) {
            sql.append(" and show_index = :showIndex \n");
        }

        final List<Banner> bannerList = this.getNamedParameterJdbcTemplate().query(
            sql.toString(),param, new RowMapper<Banner>() {
                @Override
                public Banner mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Banner.Builder()
                        .setId(resultSet.getLong("id"))
                        .setStoreId(resultSet.getLong("store_id"))
                        .setName(resultSet.getString("name"))
                        .setImageUrl(resultSet.getString("image_path"))
                        .setOrder(resultSet.getInt("show_index"))
                        .setStatus(resultSet.getInt("status"))
                        .build();
                }
            });
        return  bannerList.isEmpty() ? null : bannerList.get(0);
    }

    @Override
    public Long updateBanner(Banner banner) {
        this.getJdbcTemplate().update("update banners set name=?,image_path=?,store_id=?,show_index=?,status=? where id = ?",
                new Object[]{
                        banner.name(),
                        banner.imageUrl(),
                        banner.storeId(),
                        banner.order(),
                        banner.getStatus(),
                        banner.id()
                });
        return banner.id();
    }

    @Override
    public int updateBannerStatus(int status,List<BannerEvent> list) {
        for (BannerEvent event:list) {
            event.setStatus(status);
        }

        String sql = "update banners set status=? where id=?";

         this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
             @Override
             public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                 BannerEvent event =  list.get(i);
                 preparedStatement.setInt(1,event.getStatus());
                 preparedStatement.setLong(2,event.getId());
             }

             @Override
             public int getBatchSize() {
                 return list.size();
             }
         });
        return 0;
    }

    @Override
    public int delBannerStatusList(List<BannerEvent> bannerList) {
        String sql = "delete from banners where id=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                BannerEvent event =  bannerList.get(i);
                preparedStatement.setLong(1,event.getId());
            }

            @Override
            public int getBatchSize() {
                return bannerList.size();
            }
        });
        return 0;
    }

    @Override
    public Long updateTable(StoreTable sTable) {
        this.getJdbcTemplate().update("update store_table set store_id=?,number=?,tag=?,show_index=?,qrcode_image_url=? where id =?"
        ,new Object[]{
                sTable.store().id(),
                        sTable.number(),
                        sTable.tag(),
                        sTable.order(),
                        sTable.qrCodeImagePath(),
                        sTable.id()
                });
        return sTable.id();
    }

    @Override
    public List<Long> updateStoreTableList(List<StoreTable> list) {
        this.getNamedParameterJdbcTemplate().batchUpdate("update store_table set qrcode_image_url = :qrCodeImagePath, ticket = :ticket, qrcode_id = :qrCodeId where id = :id", SqlParameterSourceUtils.createBatch(list.toArray()));
        return list.stream().map(StoreTable::id).collect(Collectors.toList());
    }

    @Override
    public Long saveTable(StoreTable sTable) {
        this.getJdbcTemplate().update("insert into store_table (id,store_id,number,show_index,qrcode_image_url,max_number_seat,tag,IS_SILENT) " +
                "   values(?,?,?,?,?,?,?,?)",
                new Object[]{
                    sTable.id(),
                        sTable.store().id(),
                        sTable.number(),
                        sTable.order(),
                        sTable.qrCodeImagePath(),
                        sTable.maxNumberOfSeat(),
                        sTable.tag(),
                        sTable.getIsSilent()
                });
        return sTable.id();
    }

    @Override
    public List<StoreTable> getStoreTableList(Store store) {
        final List<StoreTable> tableList = this.getJdbcTemplate().query(
            "select * from store_table where  store_id = ? and status !=3 ",new Object[]{store.id()}, new RowMapper<StoreTable>() {
                @Override
                public StoreTable mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new StoreTable.Builder()
                        .setId(resultSet.getLong("id"))
                        .setNumber(resultSet.getInt("number"))
                        .setOrder(resultSet.getInt("show_index"))
                        .setQrCodeId(resultSet.getString("qrcode_id"))
                        .setQrCodeImagePath(resultSet.getString("qrcode_image_url"))
                        .setMaxNumberOfSeat(resultSet.getInt("max_number_seat"))
                        .setTag(resultSet.getString("tag"))
                        .setSceneId(resultSet.getInt("table_no"))
                        .setTicket(resultSet.getString("ticket"))
                        .build();
                }
            });
        return  tableList.isEmpty() ? Collections.emptyList() : tableList;
    }

    @Override
    public List<StoreTable> getQrStoreTableList(Store store) {
        final List<StoreTable> tableList = this.getJdbcTemplate().query(
                "select st.id, st.number, st.show_index, q.qrcode_id, st.store_id, st.max_number_seat, st.tag, st.table_no, q.qrcode_url qrcode_image_url, q.qrcode_ticket ticket from store_table st left join qrcode q on st.qrcode_id = q.qrcode_id and (q.action_name in ('QR_LIMIT_SCENE', 'QR_LIMIT_STR_SCENE')  or q.action_name in ('QR_SCENE', 'QR_STR_SCENE') and q.expire_dts >= adddate(current_timestamp(), INTERVAL 600 SECOND)) where  st.store_id = ? and status !=3 ",new Object[]{store.id()}, new RowMapper<StoreTable>() {
                    @Override
                    public StoreTable mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new StoreTable.Builder()
                                .setId(resultSet.getLong("id"))
                                .setNumber(resultSet.getInt("number"))
                                .setOrder(resultSet.getInt("show_index"))
                                .setStore(new Store.Builder().id(resultSet.getLong("store_id")).build())
                                .setQrCodeId(resultSet.getString("qrcode_id"))
                                .setQrCodeImagePath(resultSet.getString("qrcode_image_url"))
                                .setMaxNumberOfSeat(resultSet.getInt("max_number_seat"))
                                .setTag(resultSet.getString("tag"))
                                .setSceneId(resultSet.getInt("table_no"))
                                .setTicket(resultSet.getString("ticket"))
                                .build();
                    }
                });
        return  tableList.isEmpty() ? Collections.emptyList() : tableList;
    }

    @Override
    public StoreTable getStoreTable(StoreTable store) {
        final List<StoreTable> tableList = this.getJdbcTemplate().query(
            "select * from store_table where  store_id = ? AND tag = ? and number = ?",new Object[]{store.store().id(),store.tag(),store.number()}, new RowMapper<StoreTable>() {
                @Override
                public StoreTable mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new StoreTable.Builder()
                        .setId(resultSet.getLong("id"))
                        .setNumber(resultSet.getInt("number"))
                        .setOrder(resultSet.getInt("show_index"))
                        .setQrCodeImagePath(resultSet.getString("qrcode_image_url"))
                        .setMaxNumberOfSeat(resultSet.getInt("max_number_seat"))
                        .setTag(resultSet.getString("tag"))
                        .build();
                }
            });
        return  tableList.isEmpty() ? null : tableList.get(0);
    }



    @Override
    public void deleteStoreTable(Long tableId) {
        this.getJdbcTemplate().update("delete from store_table where id = ?",new Object[]{tableId});
    }

    @Override
    public int saveTableList(List<StoreTable> saveList) {
        final int[] ints = this.getJdbcTemplate().batchUpdate("insert into store_table (id,store_id,number,show_index,qrcode_image_url,max_number_seat,tag)values(?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        StoreTable storeTableBatch = saveList.get(i);
                        preparedStatement.setLong(1, storeTableBatch.id());
                        preparedStatement.setLong(2, storeTableBatch.store().id());
                        preparedStatement.setInt(3, storeTableBatch.number());
                        preparedStatement.setInt(4, storeTableBatch.order());
                        preparedStatement.setString(5, storeTableBatch.qrCodeImagePath());
                        preparedStatement.setInt(6, storeTableBatch.maxNumberOfSeat());
                        preparedStatement.setString(7, storeTableBatch.tag());
                    }

                    @Override
                    public int getBatchSize() {
                        return saveList.size();
                    }
                });

        return saveList.size();
    }

    @Override
    public int deleteStoreTableBatch(DelStoreTable delStoreTable) {
        String sql = "update store_table set status = ? where id = ? and store_id = ?";
        // String sql = "DELETE from store_table where store_id = ? and id= ?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long id = delStoreTable.getStoreTables().get(i);
                    preparedStatement.setInt(1,delStoreTable.getStatus());
                    preparedStatement.setLong(2,id);
                    preparedStatement.setLong(3,delStoreTable.getStoreId());
                }

                @Override
                public int getBatchSize() {
                    return delStoreTable.getStoreTables().size();
                }
            }
        );
        return delStoreTable.getStoreTables().size();
    }

    /**
     * 保存Store和Category的关联关系（商品分类和商家标签）
     *
     * @param storeId
     * @param storeCategoryList
     */
    @Override
    @Transactional
    public void updateStoreCategoryMap(Long storeId, List<StoreCategory> storeCategoryList) {
        //Long storeId = storeCategoryList.get(0).getStoreId();
        int delCount = this.getJdbcTemplate().update("delete from store_category_map where store_id = ?", new Object[]{storeId});
        if (delCount >= 0) {
            this.getJdbcTemplate().batchUpdate("insert into store_category_map (store_id,category_id,FUNCTION_TYPE,MANAGE_TYPE) values(?,?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            StoreCategory storeCategory = storeCategoryList.get(i);
                            preparedStatement.setLong(1, storeId);
                            preparedStatement.setLong(2, storeCategory.getCategoryId());
                            preparedStatement.setInt(3, storeCategory.getFunctionType());
                            preparedStatement.setInt(4, storeCategory.getManageType());
                        }

                        @Override
                        public int getBatchSize() {
                            return storeCategoryList.size();
                        }
                    });
        }
    }

    /**
     * 查询门店的关联目录（商品分类和商店标签）
     *
     * @param store 门店对象
     * @return
     */
    @Deprecated
    @Override
    public List<Category> getStoreCategory(Store store) {
        Objects.requireNonNull(store);
        List<Long> cids = this.getJdbcTemplate().queryForList("select category_id from store_category_map where store_id =?", new Object[]{store.id()}, Long.class);

        //此部分如果Category多的话会产生负载，要考虑向后性能
        Category rootCategory = this.baseService.getRootCategory();
        List<Category> categoryList = cids.stream()
                .map((oId) -> rootCategory.getCategory(oId))
                .filter((ca) -> ca != null)
                .collect(Collectors.toList());
        return categoryList;
    }

    /**
     * 查询门店的关联目录（商品分类和商店标签）
     *
     * @param storeId 门店ID
     * @return
     */
    @Override
    public List<Category> getStoreCategory(Long storeId) {
        List<Long> cids = this.getJdbcTemplate().queryForList("select category_id from store_category_map where store_id =?", new Object[]{storeId}, Long.class);

        //此部分如果Category多的话会产生负载，要考虑向后性能
        Category rootCategory = this.baseService.getRootCategory();
        List<Category> categoryList = cids.stream()
                .map((oId) -> rootCategory.getCategory(oId))
                .filter((ca) -> ca != null)
                .collect(Collectors.toList());
        return categoryList;
    }

    @Override
    public List<QRCode> getQRCodeListByMap(Map<String, Object> param) {
        StringBuilder sb = new StringBuilder()
                .append("select q.qrcode_id as qrcodeId,q.action_name as actionName,q.expire_seconds as expireSeconds,q.expire_dts as expireDts,q.scene_id as sceneId,q.scene_str as sceneStr,q.qrcode_ticket as qrcodeTicket,q.qrcode_url as qrcodeUrl, q.store_id as storeId from qrcode q where q.store_id = :storeId\n");

        String exists = Objects.toString(param.get("exists"), null);
        if (StringUtils.isNotBlank(exists)) {
            sb.append(" and not exists(select 1 from store_table st where st.qrcode_id = q.qrcode_id and st.status <> 3) ");
        }

        // 如果param.get("size")为null,则size设置为0
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        if (size > 0) {
            sb.append(" limit :size ");
        }

        return this.getNamedParameterJdbcTemplate().query(sb.toString(), param, new BeanPropertyRowMapper<>(QRCode.class));
    }

    @Override
    public void insertQrcodeList(List<QRCode> addQrcodeList) {
        this.getNamedParameterJdbcTemplate().batchUpdate("insert into qrcode ( qrcode_id, action_name, expire_seconds, expire_dts, scene_id, scene_str, qrcode_ticket, qrcode_url, store_id) values ( :qrcodeId, :actionName, :expireSeconds, :expireDts, :sceneId, :sceneStr, :qrcodeTicket, :qrcodeUrl, :storeId)", SqlParameterSourceUtils.createBatch(addQrcodeList.toArray()));
    }

    @Override
    public int getQRCodeCountByStoreId(long storeId, List<String> actionNms) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("actionNms", actionNms);
        return this.getNamedParameterJdbcTemplate().queryForObject("select count(*) from qrcode q where q.store_id = :storeId and q.action_name in ( :actionNms )", param, Integer.class);
    }

    @Override
    public List<Store> getStoreList(long managerId) {
        String sql = "select id,name,manager_id,city from store where manager_id=? ";
        final List<Store> storeList = this.getJdbcTemplate().query(sql, new Object[]{managerId}, new RowMapper<Store>() {
            @Override
            public Store mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Store.Builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .managerId(resultSet.getLong("manager_id"))
                        .build();
            }

        });
        return storeList;
    }

    /**
     * Manager CMS修改店铺
     *
     * @param store
     * @return
     */
    @Override
    public Store updateStore(Store store) {
        /*this.getJdbcTemplate().update("update store set name=?,city=?,email=?,mobile=?,logo_url=?,ceo_name=?," +
                "biz_scope=?,detail_addr=?,detail_addr_chn=?,description=?,description_chn=?," +
                "shop_hour=?,manager_id=?,merchant_id=?,status=?,merchant_nm=?,gateway_pw=?," +
                "transid_type=?,longitude=?,latitude=?,store_type=?, " +
                "IS_SELFSERVICE=?,IS_DELIVERY=?,SELFSERVICE_USEYN=?,DELIVERY_USEYN=?, " +
                "IS_PAY_SET=?,IS_OPENING=?,IS_SEGMENTED=?,MORNING_ST=?," +
                "MORNING_ET=?,NOON_ST=?,NOON_ET=?,EVENING_ST=?,EVENING_ET=?,AFTERNOON_ST=?," +
                "AFTERNOON_ET=?,MIDNIGHT_ST=?,MIDNIGHT_ET=?,CHARGE_TYPE=?,CHARGE_RATE=?,CHARGE_FEE=?,TABLE_COUNT=?"+
                " where id = ?",
                new Object[]{
                        store.name(),
                        store.contact().getCity(),
                        store.contact().getEmail(),
                        store.contact().getMobile(),
                        store.contact().getLogoUrl(),
                        store.companyInfo().getCeo(),
                        store.companyInfo().getScope(),
                        store.contact().getDetailAddr(),
                        store.contact().getDetailAddrChn(),
                        store.getDescription(),
                        store.getDescriptionChn(),
                        store.companyInfo().getShopHour(),
                        store.manager().getId(),
                        store.merchantId(),
                        store.getStatus(),
                        store.merchantNm(),
                        store.gatewayPw(),
                        store.transidType(),
                        store.longitude(),
                        store.latitude(),
                        store.storeType(),
                        store.isSelfservice(),
                        store.isDelivery(),
                        store.selfserviceUseyn(),
                        store.deliveryUseyn(),
                        store.isPaySet(),
                        store.isOpening(),
                        store.isSegmented(),
                        store.morningSt(),
                        store.morningEt(),
                        store.noonSt(),
                        store.noonEt(),
                        store.eveningSt(),
                        store.eveningEt(),
                        store.afternoonSt(),
                        store.afternoonEt(),
                        store.midnightSt(),
                        store.midnightEt(),
                        store.chargeType(),
                        store.chargeRate(),
                        store.chargeFee(),
                        store.tableCount(),
                        store.id()
                });*/

        this.getJdbcTemplate().update("update store set name=?,city=?,email=?,mobile=?,logo_url=?,ceo_name=?," +
                        "biz_scope=?,detail_addr=?,detail_addr_chn=?,description=?,description_chn=?," +
                        "shop_hour=?,manager_id=?,merchant_id=?,merchant_nm=?,gateway_pw=?," +
                        "transid_type=?,longitude=?,latitude=?,store_type=?, " +
                        "IS_SELFSERVICE=?,IS_DELIVERY=?,SELFSERVICE_USEYN=?,DELIVERY_USEYN=?, " +
                        "IS_PAY_SET=?,IS_OPENING=?,IS_SEGMENTED=?,MORNING_ST=?," +
                        "MORNING_ET=?,NOON_ST=?,NOON_ET=?,EVENING_ST=?,EVENING_ET=?,AFTERNOON_ST=?," +
                        "AFTERNOON_ET=?,MIDNIGHT_ST=?,MIDNIGHT_ET=?,CHARGE_TYPE=?,CHARGE_RATE=?,CHARGE_FEE=?,TABLE_COUNT=?,"+
                        "CASH_SETTLE_TYPE=?,PROD_PRICE_TYPE=?,ST_BANK_NAME=?,ST_BANK_ACC=?,ST_BANK_ACC_NAME=?,IS_JOIN=?,SETTLE_RATE_PRICE_ON=?\n"+
                        "where id = ?",
                new Object[]{
                        store.name(),
                        store.contact().getCity(),
                        store.contact().getEmail(),
                        store.contact().getMobile(),
                        store.contact().getLogoUrl(),
                        store.companyInfo().getCeo(),
                        store.companyInfo().getScope(),
                        store.contact().getDetailAddr(),
                        store.contact().getDetailAddrChn(),
                        store.getDescription(),
                        store.getDescriptionChn(),
                        store.companyInfo().getShopHour(),
                        store.manager().getId(),
                        store.merchantId(),
                        store.merchantNm(),
                        store.gatewayPw(),
                        store.transidType(),
                        store.longitude(),
                        store.latitude(),
                        store.storeType(),
                        store.isSelfservice(),
                        store.isDelivery(),
                        store.selfserviceUseyn(),
                        store.deliveryUseyn(),
                        store.isPaySet(),
                        store.isOpening(),
                        store.isSegmented(),
                        store.morningSt(),
                        store.morningEt(),
                        store.noonSt(),
                        store.noonEt(),
                        store.eveningSt(),
                        store.eveningEt(),
                        store.afternoonSt(),
                        store.afternoonEt(),
                        store.midnightSt(),
                        store.midnightEt(),
                        store.chargeType(),
                        store.chargeRate(),
                        store.chargeFee(),
                        store.tableCount(),
                        store.cashSettleType(),
                        store.prodPriceType(),
                        store.stBankName(),
                        store.stBankAcc(),
                        store.stBankAccName(),
                        store.isJoin(),
                        store.chargeRatePriceOn(),
                        store.id()
                });
        saveStoreChild(store);//保存storeExtend,storeAttach,storeTopicList

        return store;
    }

    @Override
    public Long saveShipPoint(ShipPoint shipPoint) {
        this.getJdbcTemplate().update("insert into ship_point (ship_point_id, ship_point_nm, area_id, addr, addr_cn, lat, lon, phone_no, cmt, cmt_cn, status)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?)",
            new Object[]{
            		shipPoint.shipPointid(),
            		shipPoint.shipPointnm(),
            		shipPoint.areaId(),
            		shipPoint.addr(),
            		shipPoint.addrCn(),
            		shipPoint.lat(),
            		shipPoint.lon(),
            		shipPoint.phoneNo(),
            		shipPoint.cmt(),
            		shipPoint.cmtCn(),
            		shipPoint.status()
            });
        return shipPoint.shipPointid();
    }

    @Override
    public Long deleteShipPoint(Long shipPointid) {
        this.getJdbcTemplate().update("delete from ship_point where ship_point_id = ?",new Object[]{shipPointid});
        return shipPointid;
    }

    @Override
    public int updateShipPointStatus(UpdateShipPointStatus shipPointStatus) {
        this.getJdbcTemplate().batchUpdate("update ship_point set status = ? where ship_point_id = ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long id = shipPointStatus.getShipPointIds().get(i);
                    preparedStatement.setLong(1,shipPointStatus.getStatus());
                    preparedStatement.setLong(2,id);
                }

                @Override
                public int getBatchSize() {
                    return shipPointStatus.getShipPointIds().size();
                }
            }
        );
        return shipPointStatus.getShipPointIds().size();
    }    
    

    @Override
    public ShipPoint updateShipPoint(ShipPoint shipPoint) {
        this.getJdbcTemplate().update(""
        		+ "update ship_point set \n"
        		+ "ship_point_nm=? ,\n" 
        		+ "area_id=? ,\n"  
        		+ "addr=? ,\n" 
        		+ "addr_cn=? ,\n" 
        		+ "lat=? ,\n" 
        		+ "lon=? ,\n"
        		+ "phone_no=? ,\n" 
        		+ "cmt=? ,\n"
        		+ "cmt_cn=? ,\n" 
        		+ "status=? \n"
        		+ "where ship_point_id = ?",
                new Object[]{
                		shipPoint.shipPointnm(),
                		shipPoint.areaId(),
                		//shipPoint.areaName(),
                		shipPoint.addr(),
                		shipPoint.addrCn(),
                		shipPoint.lat(),
                		shipPoint.lon(),
                		shipPoint.phoneNo(),
                		shipPoint.cmt(),
                		shipPoint.cmtCn(),
                		shipPoint.status(),
                		shipPoint.shipPointid()
                });

        return shipPoint;
    }    
    

    @Override
    public ShipPoint getShipPoint(Long shipPointid) {
        final List<ShipPoint> shipPoints = this.getJdbcTemplate().query(
                "SELECT\n" + 
	                "        t1.ship_point_id\n" + 
	                "        ,t1.ship_point_nm\n" + 
	                "        ,t1.area_id\n" + 
	                "        ,(select a.area_nm from area a where a.area_cd = t1.area_id) as area_name\n" + 
	                "        ,t1.addr\n" + 
	                "        ,t1.addr_cn\n" + 
	                "        ,t1.lat\n" + 
	                "        ,t1.lon\n" + 
	                "        ,t1.phone_no\n" + 
	                "        ,t1.cmt\n" + 
	                "        ,t1.cmt_cn\n" + 
	                "        ,t1.status\n" + 
	                "FROM    ship_point t1" +
	                "WHERE   t1.ship_point_id = ?",
                new Object[]{shipPointid},
                new RowMapper<ShipPoint>() {
            @Override
            public ShipPoint mapRow(ResultSet resultSet, int i) throws SQLException {
                return new ShipPoint.Builder()
                        .shipPointid(resultSet.getLong("ship_point_id"))
                        .shipPointnm(resultSet.getString("ship_point_nm"))
                        .areaId(resultSet.getString("area_id"))
                        .areaName(resultSet.getString("area_name"))
                        .addr(resultSet.getString("addr"))
                        .addrCn(resultSet.getString("addr_cn"))
                        .lat(resultSet.getString("lat"))
                        .lon(resultSet.getString("lon"))
                        .phoneNo(resultSet.getString("phone_no"))
                        .cmt(resultSet.getString("cmt"))
                        .cmtCn(resultSet.getString("cmt_cn"))
                        .status(resultSet.getInt("status"))
                        .build();
            }
        });
        return shipPoints.isEmpty() ? null : shipPoints.get(0);
    }

    /**
     * Admin CMS中单点登录Manager CMS时对storeId和managerId匹配性进行检查
     * 改自当前类的getStore(Long id)方法。
     *
     * @param storeId
     * @param managerId
     * @return
     * @see Store getStore(Long id)
     */
    public Store checkStoreManager(String storeId, String managerId) {
        final List<Store> stores = this.getJdbcTemplate().query(
                "select s.*," +
                        "  u.name as manager_name,u.id as managerIdd " +
                        " from store s " +
                        "  join user u on s.manager_id = u.id" +
                        " where s.id = ? and s.manager_id = ?",
                new Object[]{storeId, managerId},
                new RowMapper<Store>() {
                    @Override
                    public Store mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Store.Builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .city(resultSet.getString("city"))
                                .detailAddr(resultSet.getString("detail_addr"))
                                .mobile(resultSet.getString("mobile"))
                                .ceoName(resultSet.getString("ceo_name"))
                                .bizScope(resultSet.getString("biz_scope"))
                                .manager(new User.Builder().id(resultSet.getLong("managerIdd")).name(resultSet.getString("manager_name")).build())
                                .merchantId(resultSet.getString("merchant_id"))
                                .merchantNm(resultSet.getString("merchant_nm"))
                                .gatewayPw(resultSet.getString("gateway_pw"))
                                .paymentMethod(resultSet.getString("payment_method"))
                                .currency(resultSet.getString("currency"))
                                .transidType(resultSet.getString("transid_type"))
                                .appid(resultSet.getString("appid"))
                                .mchId(resultSet.getString("mch_id"))
                                .subAppid(resultSet.getString("sub_appid"))
                                .subMchid(resultSet.getString("sub_mch_id"))
                                .apiKey(resultSet.getString("api_key"))
                                .storeType(resultSet.getInt("store_type"))
                                .isSelfservice(resultSet.getInt("IS_SELFSERVICE"))
                                .isDelivery(resultSet.getInt("IS_DELIVERY"))
                                .selfserviceUseyn(resultSet.getInt("SELFSERVICE_USEYN"))
                                .deliveryUseyn(resultSet.getInt("DELIVERY_USEYN"))
                                .isPaySet(resultSet.getInt("IS_PAY_SET"))
                                .isOpening(resultSet.getInt("IS_OPENING"))
                                .isSegmented(resultSet.getInt("IS_SEGMENTED"))
                                .morningSt(resultSet.getString("MORNING_ST"))
                                .morningEt(resultSet.getString("MORNING_ET"))
                                .noonSt(resultSet.getString("NOON_ST"))
                                .noonEt(resultSet.getString("NOON_ET"))
                                .eveningSt(resultSet.getString("EVENING_ST"))
                                .eveningEt(resultSet.getString("EVENING_ET"))
                                .afternoonSt(resultSet.getString("AFTERNOON_ST"))
                                .afternoonEt(resultSet.getString("AFTERNOON_ET"))
                                .midnightSt(resultSet.getString("MIDNIGHT_ST"))
                                .midnightEt(resultSet.getString("MIDNIGHT_ET"))
                                .chargeType(resultSet.getInt("CHARGE_TYPE"))
                                .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                                .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                                .tableCount(resultSet.getInt("TABLE_COUNT"))
                                .build();
                    }
                });
        return stores.isEmpty() ? null : stores.get(0);
    }

    /**
     * 根据商户ID查询商户的计费信息修改记录
     *
     * @param storeId
     * @return
     */
    public List<StoreChargeInfoRecord> getStoreChargeInfoRecord(Long storeId) {
        String sql = "SELECT SCIR_ID, STORE_ID, CHARGE_YEAR_MONTH, CHARGE_YEAR, CHARGE_MONTH, CHARGE_TYPE, CHARGE_RATE, CHARGE_FEE, USE_YN, FINAL_YN, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER FROM STORE_CHARGE_INFO_RECORD WHERE STORE_ID = ?";
        List<StoreChargeInfoRecord> storeChargeInfoRecordList = this.getJdbcTemplate().query(sql, new Object[]{storeId}, new RowMapper<StoreChargeInfoRecord>() {
            @Override
            public StoreChargeInfoRecord mapRow(ResultSet resultSet, int i) throws SQLException {
                return new StoreChargeInfoRecord.Builder()
                        .scirID(resultSet.getLong("SCIR_ID"))
                        .storeID(resultSet.getLong("STORE_ID"))
                        .chargeYearMonth(resultSet.getString("CHARGE_YEAR_MONTH"))
                        .chargeYear(resultSet.getInt("CHARGE_YEAR"))
                        .chargeMonth(resultSet.getInt("CHARGE_MONTH"))
                        .chargeType(resultSet.getInt("CHARGE_TYPE"))
                        .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                        .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                        .useYn(resultSet.getInt("USE_YN"))
                        .finalYn(resultSet.getInt("FINAL_YN"))
                        .createTime(resultSet.getString("CREATE_TIME"))
                        .createUser(resultSet.getString("CREATE_USER"))
                        .updateTime(resultSet.getString("UPDATE_TIME"))
                        .updateUser(resultSet.getString("UPDATE_USER"))
                        .build();
            }
        });
        return storeChargeInfoRecordList;
    }

    /**
     * 根据商户ID和指定年月查询商户的计费信息修改记录
     *
     * @param storeId
     * @return
     */
    public List<StoreChargeInfoRecord> getStoreChargeInfoRecord(Long storeId, Integer year, Integer month) {
        String sql = "SELECT SCIR_ID, STORE_ID, CHARGE_YEAR_MONTH, CHARGE_YEAR, CHARGE_MONTH, CHARGE_TYPE, CHARGE_RATE, CHARGE_FEE, USE_YN, FINAL_YN, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER FROM STORE_CHARGE_INFO_RECORD WHERE STORE_ID = ? AND CHARGE_YEAR = ? AND CHARGE_MONTH = ?";
        List<StoreChargeInfoRecord> storeChargeInfoRecordList = this.getJdbcTemplate().query(sql,
                new Object[]{storeId, year, month},
                new RowMapper<StoreChargeInfoRecord>() {
                    @Override
                    public StoreChargeInfoRecord mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new StoreChargeInfoRecord.Builder()
                                .scirID(resultSet.getLong("SCIR_ID"))
                                .storeID(resultSet.getLong("STORE_ID"))
                                .chargeYearMonth(resultSet.getString("CHARGE_YEAR_MONTH"))
                                .chargeYear(resultSet.getInt("CHARGE_YEAR"))
                                .chargeMonth(resultSet.getInt("CHARGE_MONTH"))
                                .chargeType(resultSet.getInt("CHARGE_TYPE"))
                                .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                                .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                                .useYn(resultSet.getInt("USE_YN"))
                                .finalYn(resultSet.getInt("FINAL_YN"))
                                .createTime(resultSet.getString("CREATE_TIME"))
                                .createUser(resultSet.getString("CREATE_USER"))
                                .updateTime(resultSet.getString("UPDATE_TIME"))
                                .updateUser(resultSet.getString("UPDATE_USER"))
                                .build();
                    }
                });
        return storeChargeInfoRecordList;
    }

    /**
     * 更新指定年月计费信息无效
     *
     * @param storeId
     * @param year
     * @param month
     */
    public void updateStoreChargeInfo(Long storeId, Integer year, Integer month) {
        this.getJdbcTemplate().update(
                "update STORE_CHARGE_INFO_RECORD set USE_YN=0, FINAL_YN=0 where STORE_ID = ? AND CHARGE_YEAR = ? AND CHARGE_MONTH = ?",
                new Object[]{storeId, year, month});
    }

    /**
     * 保存最新设定的次月计费信息
     *
     * @param storeChargeInfoRecord
     */
    public void saveStoreChargeInfo(StoreChargeInfoRecord storeChargeInfoRecord) {
        this.getJdbcTemplate().update("insert into STORE_CHARGE_INFO_RECORD (STORE_ID,CHARGE_YEAR_MONTH,CHARGE_YEAR,CHARGE_MONTH,CHARGE_TYPE,CHARGE_RATE,CHARGE_FEE,USE_YN,FINAL_YN,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                "   values(?,?,?,?,?,?,?,?,?,now(),'ba',now(),'ba')", new Object[]{
                storeChargeInfoRecord.getStoreID(),
                storeChargeInfoRecord.getChargeYearMonth(),
                storeChargeInfoRecord.getChargeYear(),
                storeChargeInfoRecord.getChargeMonth(),
                storeChargeInfoRecord.getChargeType(),
                storeChargeInfoRecord.getChargeRate(),
                storeChargeInfoRecord.getChargeFee(),
                storeChargeInfoRecord.getUseYn(),
                storeChargeInfoRecord.getFinalYn()
        });
    }

    /**
     * 获取非删除的商户列表
     *
     * @return
     */
    public List<Store> getUsingStoreList() {
        String sql = "select * from store where status!=3";
        // String sql = "select * from store where status!=3 AND id=581363347573511175";
        final List<Store> storeList = this.getJdbcTemplate().query(
                sql,
                new Object[]{},
                new RowMapper<Store>() {
                    @Override
                    public Store mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Store.Builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .city(resultSet.getString("city"))
                                .detailAddr(resultSet.getString("detail_addr"))
                                .mobile(resultSet.getString("mobile"))
                                .ceoName(resultSet.getString("ceo_name"))
                                .bizScope(resultSet.getString("biz_scope"))
                                .merchantId(resultSet.getString("merchant_id"))
                                .merchantNm(resultSet.getString("merchant_nm"))
                                .gatewayPw(resultSet.getString("gateway_pw"))
                                .paymentMethod(resultSet.getString("payment_method"))
                                .currency(resultSet.getString("currency"))
                                .transidType(resultSet.getString("transid_type"))
                                .appid(resultSet.getString("appid"))
                                .mchId(resultSet.getString("mch_id"))
                                .subAppid(resultSet.getString("sub_appid"))
                                .subMchid(resultSet.getString("sub_mch_id"))
                                .apiKey(resultSet.getString("api_key"))
                                .storeType(resultSet.getInt("store_type"))
                                .isSelfservice(resultSet.getInt("IS_SELFSERVICE"))
                                .isDelivery(resultSet.getInt("IS_DELIVERY"))
                                .selfserviceUseyn(resultSet.getInt("SELFSERVICE_USEYN"))
                                .deliveryUseyn(resultSet.getInt("DELIVERY_USEYN"))
                                .isPaySet(resultSet.getInt("IS_PAY_SET"))
                                .isOpening(resultSet.getInt("IS_OPENING"))
                                .isSegmented(resultSet.getInt("IS_SEGMENTED"))
                                .morningSt(resultSet.getString("MORNING_ST"))
                                .morningEt(resultSet.getString("MORNING_ET"))
                                .noonSt(resultSet.getString("NOON_ST"))
                                .noonEt(resultSet.getString("NOON_ET"))
                                .eveningSt(resultSet.getString("EVENING_ST"))
                                .eveningEt(resultSet.getString("EVENING_ET"))
                                .afternoonSt(resultSet.getString("AFTERNOON_ST"))
                                .afternoonEt(resultSet.getString("AFTERNOON_ET"))
                                .midnightSt(resultSet.getString("MIDNIGHT_ST"))
                                .midnightEt(resultSet.getString("MIDNIGHT_ET"))
                                .chargeType(resultSet.getInt("CHARGE_TYPE"))
                                .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                                .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                                .tableCount(resultSet.getInt("TABLE_COUNT"))
                                .build();
                    }
                });
        return storeList;
    }

    /**
     * 获取非删除的PG交易类或BA交易类商户列表
     *
     * @param cashSettleType
     * @return
     */
    public List<Store> getUsingPGOrBAStoreList(String cashSettleType){
        StringBuilder sql = new StringBuilder("select * from store where status!=3");
        if("PG".equals(cashSettleType)){
            sql.append(" and cash_settle_type='PG'");
        } else if("BA".equals(cashSettleType)){
            sql.append(" and cash_settle_type='BA'");
        }

        final List<Store> storeList = this.getJdbcTemplate().query(
                sql.toString(),
                new Object[]{},
                new RowMapper<Store>() {
                    @Override
                    public Store mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Store.Builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .city(resultSet.getString("city"))
                                .detailAddr(resultSet.getString("detail_addr"))
                                .mobile(resultSet.getString("mobile"))
                                .ceoName(resultSet.getString("ceo_name"))
                                .bizScope(resultSet.getString("biz_scope"))
                                .merchantId(resultSet.getString("merchant_id"))
                                .merchantNm(resultSet.getString("merchant_nm"))
                                .gatewayPw(resultSet.getString("gateway_pw"))
                                .paymentMethod(resultSet.getString("payment_method"))
                                .currency(resultSet.getString("currency"))
                                .transidType(resultSet.getString("transid_type"))
                                .appid(resultSet.getString("appid"))
                                .mchId(resultSet.getString("mch_id"))
                                .subAppid(resultSet.getString("sub_appid"))
                                .subMchid(resultSet.getString("sub_mch_id"))
                                .apiKey(resultSet.getString("api_key"))
                                .storeType(resultSet.getInt("store_type"))
                                .isSelfservice(resultSet.getInt("IS_SELFSERVICE"))
                                .isDelivery(resultSet.getInt("IS_DELIVERY"))
                                .selfserviceUseyn(resultSet.getInt("SELFSERVICE_USEYN"))
                                .deliveryUseyn(resultSet.getInt("DELIVERY_USEYN"))
                                .isPaySet(resultSet.getInt("IS_PAY_SET"))
                                .isOpening(resultSet.getInt("IS_OPENING"))
                                .isSegmented(resultSet.getInt("IS_SEGMENTED"))
                                .morningSt(resultSet.getString("MORNING_ST"))
                                .morningEt(resultSet.getString("MORNING_ET"))
                                .noonSt(resultSet.getString("NOON_ST"))
                                .noonEt(resultSet.getString("NOON_ET"))
                                .eveningSt(resultSet.getString("EVENING_ST"))
                                .eveningEt(resultSet.getString("EVENING_ET"))
                                .afternoonSt(resultSet.getString("AFTERNOON_ST"))
                                .afternoonEt(resultSet.getString("AFTERNOON_ET"))
                                .midnightSt(resultSet.getString("MIDNIGHT_ST"))
                                .midnightEt(resultSet.getString("MIDNIGHT_ET"))
                                .chargeType(resultSet.getInt("CHARGE_TYPE"))
                                .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                                .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                                .tableCount(resultSet.getInt("TABLE_COUNT"))
                                .cashSettleType(resultSet.getString("cash_settle_type"))
                                .prodPriceType(resultSet.getInt("prod_price_type"))
                                .stBankName(resultSet.getString("st_bank_name"))
                                .stBankAcc(resultSet.getString("st_bank_acc"))
                                .stBankAccName(resultSet.getString("st_bank_acc_name"))
                                .build();
                    }
                });
        return storeList;
    }

    /**
     * 同步计费信息到Store表
     *
     * @param id
     * @param chargeType
     * @param chargeRate
     * @param chargeFee
     * @return
     */
    public Long saveCurMonthMainChargeInfo(Long id, int chargeType, double chargeRate, double chargeFee) {
        this.getJdbcTemplate().update("update store set CHARGE_TYPE=?,CHARGE_RATE=?,CHARGE_FEE=? where id = ?",
                new Object[]{
                        chargeType,
                        chargeRate,
                        chargeFee,
                        id
                });
        return id;
    }

    /**
     * 查询商户指定年月的计费信息
     *
     * @param storeId
     * @param lastYear
     * @param lastMonth
     * @return
     */
    public StoreChargeInfoRecord getStoreMonthChargeInfo(Long storeId, int lastYear, int lastMonth) {
        String sql = "SELECT SCIR_ID, STORE_ID, CHARGE_YEAR_MONTH, CHARGE_YEAR, CHARGE_MONTH, CHARGE_TYPE, CHARGE_RATE, CHARGE_FEE, USE_YN, FINAL_YN, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER FROM STORE_CHARGE_INFO_RECORD WHERE FINAL_YN = 1 AND USE_YN IN (2,3) AND STORE_ID = ? AND CHARGE_YEAR = ? AND CHARGE_MONTH = ?";
        final List<StoreChargeInfoRecord> records = this.getJdbcTemplate().query(
                sql,
                new Object[]{storeId, lastYear, lastMonth},
                new RowMapper<StoreChargeInfoRecord>() {
                    @Override
                    public StoreChargeInfoRecord mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new StoreChargeInfoRecord.Builder()
                                .scirID(resultSet.getLong("SCIR_ID"))
                                .storeID(resultSet.getLong("STORE_ID"))
                                .chargeYearMonth(resultSet.getString("CHARGE_YEAR_MONTH"))
                                .chargeYear(resultSet.getInt("CHARGE_YEAR"))
                                .chargeMonth(resultSet.getInt("CHARGE_MONTH"))
                                .chargeType(resultSet.getInt("CHARGE_TYPE"))
                                .chargeRate(resultSet.getDouble("CHARGE_RATE"))
                                .chargeFee(resultSet.getDouble("CHARGE_FEE"))
                                .useYn(resultSet.getInt("USE_YN"))
                                .finalYn(resultSet.getInt("FINAL_YN"))
                                .createTime(resultSet.getString("CREATE_TIME"))
                                .createUser(resultSet.getString("CREATE_USER"))
                                .updateTime(resultSet.getString("UPDATE_TIME"))
                                .updateUser(resultSet.getString("UPDATE_USER"))
                                .build();
                    }
                });
        return records.isEmpty() ? null : records.get(0);
    }

    /**
     * 定格结算日期所在月的商户计费信息-to useYn=2 finalYn=1
     *
     * @param storeChargeInfoRecord
     */
    public void updateStoreCharge(StoreChargeInfoRecord storeChargeInfoRecord) {
        this.getJdbcTemplate().update("update STORE_CHARGE_INFO_RECORD set USE_YN=? where FINAL_YN=1 AND USE_YN=1 AND STORE_ID = ? AND CHARGE_YEAR = ? AND CHARGE_MONTH = ?",
                new Object[]{
                        storeChargeInfoRecord.getUseYn(),
                        storeChargeInfoRecord.getStoreID(),
                        storeChargeInfoRecord.getChargeYear(),
                        storeChargeInfoRecord.getChargeMonth()
                });
    }

    @Override
    public void saveStoreDailyGrade(Store store,Double avgRevClass,int count) {
        StoreDynamic storeDynamic = new StoreDynamic.Builder().storeId(store.id()).reviewGrade(new BigDecimal(avgRevClass).setScale(1,BigDecimal.ROUND_HALF_UP)).reviewCount(count).build();

        this.getJdbcTemplate().update("replace into STORE_DYNAMIC (STORE_ID,REVIEW_COUNT,REVIEW_GRADE,CREATE_TIME,CREATE_USER)" +
            "   values(?,?,?,now(),'ba')", new Object[]{
            storeDynamic.getStoreId(),
            storeDynamic.getReviewCount(),
            storeDynamic.getReviewGrade()
        });
    }

    @Override
    public int batchSaveStoreDayPrice(List<StoreDayPrice> storeDayPrices,String acount) {
        String sql = "insert into store_day_price (STORE_ID\n" +
            "      , INV_DATE, MIN_PRICE,MAX_PRICE,CREATE_TIME,CREATE_USER)\n" +
            "    values(?,?,?,?,now(),?)\n"+
            "on duplicate key\n" +
            "    update \n" +
//            "    STORE_ID = ?\n"+
//            "    ,PROD_ID = ?\n"+
//            "    ,PROD_SKU_ID = ?\n"+
            "    INV_DATE = ?\n"+
            ",MIN_PRICE = ?\n"+
            ",MAX_PRICE = ?\n"+
            ",update_time = now()"+
            ",update_user = ?";
        final int[] ints = this.getJdbcTemplate().batchUpdate(sql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    StoreDayPrice storeDayPrice = storeDayPrices.get(i);
                    int k = 1;
                    preparedStatement.setLong(k++, storeDayPrice.getStoreId());
                   // preparedStatement.setLong(k++, storeDayPrice.getProdId());
                   // preparedStatement.setLong(k++, storeDayPrice.getProdSkuId());
                    preparedStatement.setString(k++, storeDayPrice.getInvDate());
                    preparedStatement.setLong(k++, storeDayPrice.getMinPrice()==null?0L:storeDayPrice.getMinPrice());
                    preparedStatement.setLong(k++, storeDayPrice.getMaxPrice()==null?0L:storeDayPrice.getMaxPrice());
                   // preparedStatement.setLong(k++, storeDayPrice.getAvgPrice());
                   // preparedStatement.setLong(k++, storeDayPrice.getMaxPrice());
                    preparedStatement.setString(k++, acount);
                   // preparedStatement.setLong(k++, storeDayPrice.getStoreId());
                   // preparedStatement.setLong(k++, storeDayPrice.getProdId());
                    //preparedStatement.setLong(k++,  storeDayPrice.getProdSkuId());
                    preparedStatement.setString(k++, storeDayPrice.getInvDate());
                    preparedStatement.setLong(k++, storeDayPrice.getMinPrice()==null?0L:storeDayPrice.getMinPrice());
                    preparedStatement.setLong(k++, storeDayPrice.getMaxPrice()==null?0L:storeDayPrice.getMaxPrice());
                    preparedStatement.setString(k++, acount);
                }

                @Override
                public int getBatchSize() {
                    return storeDayPrices.size();
                }
            });

        return storeDayPrices.size();
    }

}
