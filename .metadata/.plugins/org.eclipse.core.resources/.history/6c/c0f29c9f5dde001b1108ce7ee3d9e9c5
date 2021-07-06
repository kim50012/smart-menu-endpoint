package com.basoft.eorder.batch.job;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.batch.job.threads.SendEamilSettleThread;
import com.basoft.eorder.batch.lock.BatchJobUtil;
import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.interfaces.query.SettleDTO;
import com.basoft.eorder.interfaces.query.SettleQuery;
import com.basoft.eorder.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:53 下午 2019/11/22
 **/
@Slf4j
@Component
public class EOrderSendEmailJob {
    private static final String SEND_EMAIL_KEY = "batch_send_email";

    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private SettleQuery settlequery;
    @Autowired
    private AppConfigure appConfigure;


    /**
     * 每月5号上午8点
     * 发送店铺每月结算邮件
     */
    //@Scheduled(cron = "0 0 8 5 * ?")
    private void senEmail() {
        log.info("<><><><><><><><><>><><><><>发送邮件定时任务启动<><><><><><><><><>><><><><>");
        RLock lock = redissonUtil.getRLock(SEND_EMAIL_KEY);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "yyyy"));
                int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "MM"));

                log.info("【发送邮件定时任务】年月信息lastMonth（上月），" + lastMonth + "上月所在年：" + lastYear);
                // 封装查询条件
                Map<String, Object> param = getQueryParam();
                //店铺每月结算列表
                List<SettleDTO> settleList = settlequery.getAdminSettleList(param);
                log.info("【发送邮件定时任务】待处理商户数量为：" + settleList.size());
                String emailTile = lastYear + "-" + lastMonth + " BA Place 플랫폼 사용내역";
                settleList = settleList.stream().filter(s -> StringUtils.isNotBlank(s.getEmail())).collect(Collectors.toList());
                for (SettleDTO dto : settleList) {
                    try {
                        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                        Pattern p = Pattern.compile(regEx1);
                        Matcher m = p.matcher(dto.getEmail());

                        if (m.matches()) {
                            Thread thread = new Thread(new SendEamilSettleThread(appConfigure, dto, emailTile));
                            thread.start();
                        }
                    } catch (Exception e) {
                        log.error("【发送邮件定时任务】商户处理线程异常，[商户ID]=" + dto.getStoreId() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【发送邮件定时任务】没有获取到定时任务执行锁:{},ThreadName :{}", SEND_EMAIL_KEY, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【发送邮件定时任务】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            BatchJobUtil.finalBatch(getLock, lock, "【发送邮件，定时任务】",SEND_EMAIL_KEY);
        }
        log.info("<><><><><><><><><>><><><><>发送邮件定时任务主线程执行结束<><><><><><><><><>><><><><>");
    }

    public static Map<String,Object> getQueryParam() {
        int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "yyyy"));
        int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "MM"));
        String startTime = DateUtil.getFirstDayOfMonth(lastYear, lastMonth);
        String endTime = DateUtil.getLastDayOfMonth(lastYear, lastMonth);
        // 封装查询条件
        Map<String, Object> param = new HashMap<>();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        return param;
    }

}
