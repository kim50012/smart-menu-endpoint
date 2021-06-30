package com.basoft.eorder.interfaces.controller.h5;

import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.batch.job.threads.MonthUpdateStoreChargeInfoThread;
import com.basoft.eorder.batch.job.threads.store.PGStoreMonthSettlementThreadManual;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.query.StoreQuery;
import com.basoft.eorder.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/wechat/api/manual")
@ResponseBody
public class SpringManulController extends CQRSAbstractController {
    @Autowired
    IStoreBatchService storeBatchService;

    @Autowired
    StoreQuery storeQuery;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    public SpringManulController(
            QueryHandler queryHandler,
            CommandHandleEngine handleEngine) {
        super(queryHandler, handleEngine);
    }

    /**
     * 手工对PG交易类商户进行月度结算
     * <p>
     * 每天手工获取限制30次
     * <p>
     * http://www.batechkorea.com/eorder//wechat/api/manual/pg_store_month_settle_manual?s1code=Kl_de_Ae98071ea&s1storeId=581363347573511175&s1offset=1&s1target=CS
     *
     * s1code 手工接口调用校验码
     * s1storeId 月度结算商户id TODO 查询商户，检查该商户是否为PG交易类商户。是继续进行结算，否则中断结算
     * s1offset 结算月份 取值1-12 当前月前推几个月
     * s1target 执行结果目标 取值 ZS-正式表 CS-手工测试表
     *
     * @return
     */
    @RequestMapping(value = "/pg_store_month_settle_manual", method = RequestMethod.GET)
    @ResponseBody
    public String pgStoreMonthSettleManual(HttpServletRequest request, HttpServletResponse response) {
        String sCode = request.getParameter("s1code");
        if (StringUtils.isNotBlank(sCode)) {
            if ("Kl_de_Ae98071ea".equals(sCode)) {
                log.info("<><><><><><><><><><>manual store month settle<><><><><><><><><><>");
                log.info("<><><><><><><><><><>执行时间<><><><><><><><><><>" + new Date());

                Long storeId = Long.valueOf(request.getParameter("s1storeId"));
                Store storeQuery = storeRepository.getStore(storeId);
                if(storeQuery == null){
                    return "Store is null!";
                }
                if(!CommonConstants.CASH_SETTLE_TYPE_PG.equals(storeQuery.cashSettleType())){
                    return "Store is not PG cash type!";
                }

                int offset = Integer.valueOf(request.getParameter("s1offset"));
                if (offset < 1 || offset > 12) {
                    return "offset error,must between 1 to 12";
                }

                String target = request.getParameter("s1target");
                if ("ZS".equals(target) || "CS".equals(target)) {
                    int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset, ChronoUnit.MONTHS), "yyyy"));
                    int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset, ChronoUnit.MONTHS), "MM"));

                    String monthStartTime = DateUtil.getFirstDayOfMonth(lastYear, lastMonth) + " 00:00:00";
                    String monthEndTime = DateUtil.getLastDayOfMonth(lastYear, lastMonth) + " 23:59:59";
                    log.info("【MANUAL】【PG交易类商户月度结算】商户结算月起始时间和截止时间::" + monthStartTime + ", " + monthEndTime);

                    Store store = new Store.Builder().id(storeId).build();

                    // 启动月度结算线程
                    try {
                        Thread thread = new Thread(new PGStoreMonthSettlementThreadManual(storeBatchService,
                                store, lastYear, lastMonth, 0, 0, monthStartTime, monthEndTime, target));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【MANUAL】【PG交易类商户月度结算】商户月度结算线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    return "Thread Started~";
                } else {
                    return "target error";
                }
            } else {
                return "You are wrong~";
            }
        }
        return "You are wrong~";
    }

    /**
     * 手工对所有商户结算信息（结算费率）进行计算
     * <p>
     * 每天手工获取限制30次
     * <p>
     * http://www.batechkorea.com/eorder//wechat/api/manual/all_store_charge_info_manual?s1code=Kl_de_Ae98071ea&s1storeId=581363347573511175&s1offset=1&s1target=CS
     *
     * s1code 手工接口调用校验码
     * s1storeId 商户id  空值则计算所有商户的结算费率
     * s1offset 结算月份 取值1-12 当前月前推几个月
     * s1target 执行结果目标 取值 ZS-正式表 CS-手工测试表
     *
     * @return
     */
    @RequestMapping(value = "/all_store_charge_info_manual", method = RequestMethod.GET)
    @ResponseBody
    public String allStoreChargeInfoManual(HttpServletRequest request, HttpServletResponse response) {
        String sCode = request.getParameter("s1code");
        if (StringUtils.isNotBlank(sCode)) {
            if ("Kl_de_Ae98071ea".equals(sCode)) {
                log.info("<><><><><><><><><><>manual all store charge info<><><><><><><><><><>");
                log.info("<><><><><><><><><><>执行时间<><><><><><><><><><>" + new Date());
                String storeIdStr = request.getParameter("s1storeId");
                List<Store> storeList = new ArrayList<Store>();
                if(StringUtils.isBlank(storeIdStr)){
                    // 查询所有有效商户
                    storeList = storeRepository.getUsingStoreList();
                    log.info("【MANUAL】【商户月度结算费率计算】所有待处理商户数量为：" + storeList.size());
                } else {
                    Long storeId = Long.valueOf(storeIdStr);
                    Store store = storeRepository.getStore(storeId);
                    if(store == null){
                        return "Store is null!";
                    }
                    storeList.add(store);
                    log.info("【MANUAL】【商户月度结算费率计算】指定处理商户数量为：" + storeList.size());
                }

                int offset = Integer.valueOf(request.getParameter("s1offset"));
                String target = request.getParameter("s1target");
                if (offset < 1 || offset > 12) {
                    return "offset error,must between 1 to 12";
                }

                if ("ZS".equals(target) || "CS".equals(target)) {
                    int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset, ChronoUnit.MONTHS), "yyyy"));
                    int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset, ChronoUnit.MONTHS), "MM"));
                    int year = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset-1, ChronoUnit.MONTHS), "yyyy"));
                    int month = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset-1, ChronoUnit.MONTHS), "MM"));
                    log.info("【MANUAL】【商户月度结算费率计算】年月信息lastYear（上月所在年），lastMonth（上月），year（当前月所在年），month（当前月）::" + lastYear + ", "
                            + lastMonth + ", " + year + ", " + month);

                    for (Store store : storeList) {
                        try {
                            Thread thread = new Thread(new MonthUpdateStoreChargeInfoThread(storeBatchService,
                                    store, lastYear, lastMonth, year, month));
                            thread.start();
                        } catch (Exception e) {
                            log.error("【MANUAL】【商户月度结算费率计算】商户月度结算信息计算线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                        }
                        // 休眠400毫秒，减轻数据库的压力
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return "Charge Info Thread Started~";
                } else {
                    return "target error";
                }
            } else {
                return "You are wrong~";
            }
        }
        return "You are wrong~";
    }

    /**
     * 手工对所有PG交易类商户进行月度结算
     * <p>
     * 每天手工获取限制30次
     * <p>
     * http://www.batechkorea.com/eorder//wechat/api/manual/all_pg_store_month_settle_manual?s1code=Kl_de_Ae98071ea&s1storeId=581363347573511175&s1offset=1&s1target=CS
     *
     * s1code 手工接口调用校验码
     * s1storeId 月度结算商户id 空值则对所有PG交易类商户进行月度结算
     * s1offset 结算月份 取值1-12 当前月前推几个月
     * s1target 执行结果目标 取值 ZS-正式表 CS-手工测试表
     *
     * @return
     */
    @RequestMapping(value = "/all_pg_store_month_settle_manual", method = RequestMethod.GET)
    @ResponseBody
    public String allStoreMonthSettleManual(HttpServletRequest request, HttpServletResponse response) {
        String sCode = request.getParameter("s1code");
        if (StringUtils.isNotBlank(sCode)) {
            if ("Kl_de_Ae98071ea".equals(sCode)) {
                log.info("<><><><><><><><><><>manual all store month settle<><><><><><><><><><>");
                log.info("<><><><><><><><><><>执行时间<><><><><><><><><><>" + new Date());
                String storeIdStr = request.getParameter("s1storeId");
                List<Store> storeList = new ArrayList<Store>();
                if(StringUtils.isBlank(storeIdStr)){
                    // 查询所有有效的PG交易类商户
                    storeList = storeRepository.getUsingPGOrBAStoreList(CommonConstants.CASH_SETTLE_TYPE_PG);
                    log.info("【MANUAL】【手工PG交易类商户月度结算】所有待处理商户数量为：" + storeList.size());
                } else {
                    Long storeId = Long.valueOf(storeIdStr);
                    Store store = storeRepository.getStore(storeId);

                    if(store == null){
                        return "Store is null!";
                    }
                    if(!CommonConstants.CASH_SETTLE_TYPE_PG.equals(store.cashSettleType())){
                        return "Store is not PG cash type!";
                    }

                    storeList.add(store);
                    log.info("【MANUAL】【手工PG交易类商户月度结算】指定处理商户数量为：" + storeList.size());
                }

                int offset = Integer.valueOf(request.getParameter("s1offset"));
                String target = request.getParameter("s1target");
                if (offset < 1 || offset > 12) {
                    return "offset error,must between 1 to 12";
                }

                if ("ZS".equals(target) || "CS".equals(target)) {
                    int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset, ChronoUnit.MONTHS), "yyyy"));
                    int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(offset, ChronoUnit.MONTHS), "MM"));

                    String monthStartTime = DateUtil.getFirstDayOfMonth(lastYear, lastMonth) + " 00:00:00";
                    String monthEndTime = DateUtil.getLastDayOfMonth(lastYear, lastMonth) + " 23:59:59";
                    log.info("【MANUAL】【手工PG交易类商户月度结算】商户结算月起始时间和截止时间::" + monthStartTime + ", " + monthEndTime);

                    for (Store store : storeList) {
                        try {
                            Thread thread = new Thread(new PGStoreMonthSettlementThreadManual(storeBatchService,
                                    store, lastYear, lastMonth, 0, 0, monthStartTime, monthEndTime, target));
                            thread.start();
                        } catch (Exception e) {
                            log.error("【MANUAL】【手工PG交易类商户月度结算】商户月度结算线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                        }
                        // 休眠400毫秒，减轻数据库的压力
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return "Thread Started~";
                } else {
                    return "target error";
                }
            } else {
                return "You are wrong~";
            }
        }
        return "You are wrong~";
    }
}