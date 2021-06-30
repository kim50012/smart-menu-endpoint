package com.basoft.api.controller.healthcheck;

import com.basoft.api.controller.BaseController;
import com.basoft.core.util.DateTimeUtil;
import com.basoft.service.batch.wechat.thread.WxUpTokenThread;
import com.basoft.service.definition.healthcheck.HealthCheckService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import com.basoft.service.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/res")
public class HealthCheckController extends BaseController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HealthCheckService healthCheckService;

    @Autowired
    private WechatService wechatService;

    /**
     * 系统健康性检查接口
     * 附带检查Redis
     *
     * @return
     */
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public String healthCheck(HttpServletRequest request, HttpServletResponse response) {
        String checkRedis = request.getParameter("cr");
        logger.debug("【<><><>】Health Checked!checkRedis=【" + checkRedis + "】");
        if (checkRedis != null && "20190723CheckRedis".equals(checkRedis)) {
            boolean isSet = redisUtil.set("20190723CheckRedis", "Gaga From Moon~", 5);
            if (isSet) {
                return "Interface and Redis is all ok! Success info::" + redisUtil.get("20190723CheckRedis");
            } else {
                return "Interface is OK,but redis is bad!";
            }
        }
        return "Interface,it's health~";
    }

    /**
     * 系统健康性检查接口
     * 附带检查Database
     *
     * @return
     */
    @RequestMapping(value = "/healthdb", method = RequestMethod.GET)
    @ResponseBody
    public String healthCheckDB(HttpServletRequest request, HttpServletResponse response) {
        String checkDatabase = request.getParameter("cd");
        logger.debug("【<><><>】Health Checked!checkDatabase=【" + checkDatabase + "】");
        if (checkDatabase != null && "20190723CheckDB".equals(checkDatabase)) {
            // 查询数据库
            try {
                String result = healthCheckService.getData();
                return "Interface and Database is all ok! Success info::" + result;
            } catch (Exception e) {
                log.error("Check Database Error", e);
                return "Interface is OK,but Database is bad!";
            }
        }
        return "Interface,it's health~";
    }

    /**
     * 手工获取或更新wechat access_token
     * 每天手工获取限制30次
     * http://admin.batechkorea.com/bawx/res/wechat_access_token_20190816?s1code=Kl_de_Ae98071ea
     *
     * @return
     */
    @RequestMapping(value = "/wechat_access_token_20190816", method = RequestMethod.GET)
    @ResponseBody
    public String wechatAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String sCode = request.getParameter("s1code");
        if (StringUtils.isNotBlank(sCode)) {
            if ("Kl_de_Ae98071ea".equals(sCode)) {
                log.info("<><><><><><><><><><>manual get wechat access token<><><><><><><><><><>");
                log.info("<><><><><><><><><><>执行时间<><><><><><><><><><>" + new Date());

                // 增加数量限制-start
                String manualKey = DateTimeUtil.format(new Date()) + "_MANUAL_WCT";
                log.info("<><><><><><><><><><>缓存key<><><><><><><><><><>" + manualKey);
                int currentCount = (int) redisUtil.get(manualKey);
                if (currentCount > 30) {
                    return "Reach the limit of quantity~";
                }

                // 数量增1
                currentCount += 1;

                // 有效截止时间点
                Calendar dayEndTime = Calendar.getInstance();
                dayEndTime.set(Calendar.HOUR_OF_DAY, 23);
                dayEndTime.set(Calendar.MINUTE, 59);
                dayEndTime.set(Calendar.SECOND, 59);
                // dayEndTime.set(Calendar.MILLISECOND, 0);

                redisUtil.set(manualKey, currentCount, (dayEndTime.getTimeInMillis() - System.currentTimeMillis()) / 1000);
                // 增加数量限制-end

                // 启动线程进行获取
                List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
                for (AppInfoWithBLOBs bloBs : appInfoList) {
                    Thread thread = new Thread(new WxUpTokenThread(wechatService, bloBs.getSysId()));
                    thread.start();
                }
            } else {
                return "You are wrong~";
            }
        } else {
            return "You are wrong~";
        }
        return "GETTING";
    }

    public static void main(String[] args) {
        Calendar dayEndTime= Calendar.getInstance();
        dayEndTime.set(Calendar.HOUR_OF_DAY, 23);
        dayEndTime.set(Calendar.MINUTE, 59);
        dayEndTime.set(Calendar.SECOND, 59);
        long se = (dayEndTime.getTimeInMillis() - System.currentTimeMillis())/1000;
        System.out.println(se);
    }
}