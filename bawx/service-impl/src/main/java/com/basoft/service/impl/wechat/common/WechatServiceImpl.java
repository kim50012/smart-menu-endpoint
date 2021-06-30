package com.basoft.service.impl.wechat.common;

import com.basoft.core.ware.wechat.domain.*;
import com.basoft.core.ware.wechat.domain.statistic.*;
import com.basoft.core.ware.wechat.exception.InvalidAccountException;
import com.basoft.core.ware.wechat.exception.WeixinAuthException;
import com.basoft.core.ware.wechat.util.RandomUtils;
import com.basoft.core.ware.wechat.util.WeixinMessageUtils;
import com.basoft.core.ware.wechat.util.WeixinUtils;
import com.basoft.service.dao.wechat.appinfo.AppInfoMapper;
import com.basoft.service.dao.wechat.common.WechatMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsHeadMapper;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHead;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHeadExample;
import com.basoft.service.enumerate.BatchEnum;
import com.basoft.service.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * WechatService
 *
 * @author basoft
 */
@Service
public class WechatServiceImpl implements WechatService {
    private final transient Log logger = LogFactory.getLog(this.getClass());

    private static int LOG_SIZE = 2000;

    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private WechatMapper wechatMapper;

    @Resource
    private ShopWxNewsHeadMapper shopWxNewsHeadMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 随机生成公众账号基本信息 包括（sysid， token， EncodingAESKey）
     *
     * @return
     */
    public WechatAccount generateAccount() {
        WechatAccount wechatAccount = new WechatAccount();
        String sysId = RandomUtils.generateRandomString(32).toUpperCase();
        String token = RandomUtils.generateRandomString();
        String encodingAESKey = RandomUtils.generateRandomString(43);
        wechatAccount.setSysId(sysId);
        wechatAccount.setToken(token);
        wechatAccount.setEncordingAesKey(encodingAESKey);
        return wechatAccount;
    }

    /**
     * 获取全部公众账号信息
     *
     * @return List<AppInfo> 公众账号信息List
     */
    public List<AppInfoWithBLOBs> selectAllAppInfoList() {
        return appInfoMapper.selectAllAppInfoList();
    }

    /**
     * 根据ID获取公众账号信息
     *
     * @param key ID
     * @return AppInfo 公众账号信息
     */
    public AppInfoWithBLOBs selectAppInfoByKey(String key) {
        try {
            AppInfoWithBLOBs appInfo = appInfoMapper.selectAppInfoByKey(key);
            if (appInfo == null) {
                logger.error("您没有操作公众账号的权限 key=" + key);
                throw new InvalidAccountException("您没有操作公众账号的权限");
            }
            return appInfo;
        } catch (InvalidAccountException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据shopId获取公众账号信息
     *
     * @param shopId
     * @return AppInfo 公众账号信息
     */
    public AppInfoWithBLOBs selectAppInfoByShopId(Long shopId) {
        try {
            AppInfoWithBLOBs appInfo = appInfoMapper.selectAppInfoByShopId(shopId);
            if (appInfo == null) {
                logger.error("您没有操作公众账号的权限 shopId=" + shopId);
                throw new InvalidAccountException("您没有操作公众账号的权限");
            }
            return appInfo;
        } catch (InvalidAccountException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据公众账号原始ID获取公众账号信息
     *
     * @param originalAppId
     * @return AppInfo 公众账号信息
     */
    public AppInfoWithBLOBs selectAppInfoByOriginalAppId(String originalAppId) {
        try {
            AppInfoWithBLOBs appInfo = appInfoMapper.selectAppInfoByOriginalAppId(originalAppId);
            if (appInfo == null) {
                logger.error("您没有操作公众账号的权限 originalAppId=" + originalAppId);
                throw new InvalidAccountException("您没有操作公众账号的权限");
            }
            return appInfo;
        } catch (InvalidAccountException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据公众账号appID获取公众账号信息
     *
     * @param appId 公众账号ID
     * @return AppInfo 公众账号信息
     */
    public AppInfoWithBLOBs selectAppInfoByAppId(String appId) {
        try {
            AppInfoWithBLOBs appInfo = appInfoMapper.selectAppInfoByAppId(appId);
            if (appInfo == null) {
                logger.error("您没有操作公众账号的权限 appId=" + appId);
                throw new InvalidAccountException("您没有操作公众账号的权限");
            }
            return appInfo;
        } catch (InvalidAccountException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据公众账号appID获取公众账号信息
     *
     * @param appId 公众账号ID
     * @return AppInfo 公众账号信息
     */
    public AppInfoWithBLOBs selectAppInfoByAppIdAndNoException(String appId) {
        return appInfoMapper.selectAppInfoByAppId(appId);
    }

    /**
     * 根据ID获取支付账号信息
     *
     * @param key ID
     * @return MchInfo 支付账号信息
     */
    public MchInfo selectMchInfoByKey(String key) {//TODO
        try {
            MchInfo mchInfo = wechatMapper.selectMchInfoByKey(key);
            if (mchInfo == null) {
                logger.error("您没有微信支付的权限 key=" + key);
                throw new InvalidAccountException("您没有微信支付的权限");
            }
            return mchInfo;
        } catch (InvalidAccountException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据shopId获取支付账号信息
     *
     * @param shopId
     * @return MchInfo 支付账号信息
     */
    public MchInfo selectMchInfoByShopId(Long shopId) {//TODO
        try {
            MchInfo mchInfo = wechatMapper.selectMchInfoByShopId(shopId);
            if (mchInfo == null) {
                logger.error("您没有微信支付的权限 shopId=" + shopId);
                throw new InvalidAccountException("您没有微信支付的权限");
            }
            return mchInfo;
        } catch (InvalidAccountException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 获取 access token
     *
     * @param appInfo
     * @return String access token
     */
    public String getAccessToken(AppInfo appInfo) {
        if (appInfo == null) {
            throw new WeixinAuthException(WeixinAuthException.APP_INFO_ERROR);
        }
        // 1.从数据库获取
        //String token = wechatMapper.getAccessToken(appInfo.getSysId());
        String token = (String) redisUtil.get("wx_token");
        logger.info("<><><><><><><><><>获取到的access_token::::" + token);

        if (StringUtils.isEmpty(token)) {
            logger.info("<><><><><><><><><><><><><><><><><><><><>");
            logger.info("access token expires,create new access token");
            token = WeixinUtils.getAccessToken(appInfo.getAppId(), appInfo.getAppSecret());
            logger.info("调用微信公众平台API获取到的access_token为=================+" + token);

            boolean toRedis = redisUtil.set("wx_token", token, 60 * 119);
            if (!toRedis) {
                logger.error("将access_token放入redis异常!!!");
            }

            wechatMapper.saveAccessToken(new AccessToken(appInfo.getSysId(), token));
        }
        return token;
    }


    /**
     * 获取 access token
     *
     * @param shopId
     * @return String access token
     */
    public String getAccessToken(Long shopId) {
        return getAccessToken(selectAppInfoByShopId(shopId));
    }

    /**
     * 获取 access token
     *
     * @param key
     * @return String access token
     */
    public String getAccessToken(String key) {
        return getAccessToken(selectAppInfoByKey(key));
    }

    /**
     * 更新access_token
     *
     * @param shopId
     * @return
     */
    @Override
    public String setAccessToken(Long shopId) {
        return setAccessToken(selectAppInfoByShopId(shopId));
    }

    /**
     * 更新access_token
     *
     * @param appInfo
     * @return
     */
    public String setAccessToken(AppInfo appInfo) {
        if (appInfo == null) {
            throw new WeixinAuthException(WeixinAuthException.APP_INFO_ERROR);
        }

        String token = WeixinUtils.getAccessToken(appInfo.getAppId(), appInfo.getAppSecret());
        logger.info("调用微信公众平台API获取到的access_token为=================+" + token);

        // 将access_token放入redis，有效期为119分钟
        boolean toRedis = redisUtil.set("wx_token", token, 60 * 119);
        if (!toRedis) {
            logger.error("将access_token放入redis异常!!!");
        }

        wechatMapper.saveAccessToken(new AccessToken(appInfo.getSysId(), token));

        return token;
    }

    /**
     * 获取 api ticket
     *
     * @param appInfo
     * @return String api ticket
     */
    public String getApiTicket(AppInfo appInfo) {
        if (appInfo == null) {
            throw new WeixinAuthException(WeixinAuthException.APP_INFO_ERROR);
        }

        // 1.从数据库获取
        // String ticket = wechatMapper.getApiTicket(appInfo.getSysId());

        String ticket = (String) redisUtil.get("wx_ticket");
        if (StringUtils.isEmpty(ticket)) {
            logger.info("<><><><><><><><><><><><><><><><><><><><>");
            logger.info("Ticket expires,create new ticket");
            ApiTicket apiTicket = WeixinUtils.getApiTicket(getAccessToken(appInfo));
            ticket = apiTicket.getTicket();
            logger.info("调用微信公众平台API获取到的API Ticket为=================+" + ticket);

            // 将api ticket放入redis，有效期为119分钟
            boolean toRedis = redisUtil.set("wx_ticket", ticket, 60 * 119);
            if (!toRedis) {
                logger.error("将api ticket放入redis异常!!!");
            }

            // wechatMapper.saveApiTicket(appInfo, apiTicket);
            // ticket = apiTicket.getTicket();
        }
        return ticket;
    }

    /**
     * 获取 api ticket
     *
     * @param shopId
     * @return String api ticket
     */
    public String getApiTicket(Long shopId) {
        return getApiTicket(selectAppInfoByShopId(shopId));
    }

    /**
     * 获取 api ticket
     *
     * @param key
     * @return String api ticket
     */
    public String getApiTicket(String key) {
        return getApiTicket(selectAppInfoByKey(key));
    }

    /**
     * 更新ApiTicket
     *
     * @param shopId
     * @return
     */
    public String setApiTicket(Long shopId) {
        return setApiTicket(selectAppInfoByShopId(shopId));
    }

    /**
     * 更新ApiTicket
     *
     * @param appInfo
     * @return
     */
    public String setApiTicket(AppInfo appInfo) {
        if (appInfo == null) {
            throw new WeixinAuthException(WeixinAuthException.APP_INFO_ERROR);
        }

        //String ticket = (String)redisUtil.get("wx_ticket");

        // 根据access_token获取API Ticket，如果access_token无效则重新获取
        ApiTicket apiTicket = WeixinUtils.getApiTicket(getAccessToken(appInfo));
        String ticket = apiTicket.getTicket();
        logger.info("调用微信公众平台API获取到的API Ticket为=================+" + ticket);

        // 将api ticket放入redis，有效期为119分钟
        boolean toRedis = redisUtil.set("wx_ticket", ticket, 60 * 119);
        if (!toRedis) {
            logger.error("将api ticket放入redis异常!!!");
        }

        // api ticket放入数据库
        //wechatMapper.saveApiTicket(appInfo, apiTicket);

        return ticket;
    }


    /**
     * @param executionLog
     */
    public void insertExecutionLog(ExecutionLog executionLog) {
        wechatMapper.insertExecutionLog(executionLog);
    }

    /**
     * @param weixinSessionPageExeLog
     */
    public void insertWeixinSessionPageExeLog(WeixinSessionPageExeLog weixinSessionPageExeLog) {
        wechatMapper.insertWeixinSessionPageExeLog(weixinSessionPageExeLog);
    }

    /**
     * @param weixinPageExecutionLog
     */
    public void insertWeixinPageExecutionLog(WeixinPageExecutionLog weixinPageExecutionLog) {
        wechatMapper.insertWeixinPageExecutionLog(weixinPageExecutionLog);
    }

    /**
     * @param map
     */
    public void insertMenuClickLogging(Map<String, Object> map) {
        wechatMapper.insertMenuClickLogging(map);
    }

    /**
     * 获取微信公众平台微信号
     *
     * @param shopId
     * @return
     */
    public String getWechatNoByShopId(Integer shopId) {
        return wechatMapper.selectWechatNoByShopId(shopId);
    }

    /**
     * 检查第三方公司接口账户的有效性
     *
     * @param appInfo 公众账号信息
     * @param hzmIfId 接口ID
     * @param hzmIfPw 接口密码
     */
	/*public void accountIsValid(AppInfo appInfo, String ifId, String ifPw) {
		if (!appInfo.getIfUserId().equals(ifId) || !appInfo.getIfPassword().equals(ifPw)) {
			throw new InvalidAccountException("账号信息不符");
		}
	}*/

    /**
     * 群发统计：图文阅读人数,分享人数,收藏人数等等(单篇图文统计)
     * 表：wx_if_message_stats_data
     *
     * @param list
     * @param shopId
     */
    @Override
    public void insertArticleSummary(List<ArticleSummary> list, Long shopId) {
        for (int i = 0; i < list.size(); i++) {
            ShopWxNewsHeadExample ex = new ShopWxNewsHeadExample();
            logger.info("wx_msg_id=" + list.get(i).getWx_msgid());
            ex.createCriteria().andShopIdEqualTo(shopId).andWxMsgIdEqualTo(list.get(i).getWx_msgid());
            List<ShopWxNewsHead> msgHeadList = shopWxNewsHeadMapper.selectByExampleWithBLOBs(ex);
            Long msgId = 1L;
			/*if (msgHeadList != null && msgHeadList.size() > 0) {
				msgId = msgHeadList.get(0).getMsgId();
			}*/
            if (msgId > 0) {
                String wxMsgid = list.get(i).getWx_msgid();
                if (StringUtils.isNotBlank(wxMsgid)) {
                    list.get(i).setMsgId(Long.valueOf(wxMsgid));
                }

                wechatMapper.insertArticleSummary(list.get(i));
                if (((i + 1) % LOG_SIZE == 0) || (i + 1 == list.size())) {
                    logger.info("insertArticleSummary DB inserted " + (i + 1) + " rows");
                }
            }
        }
    }

    public void updateTargetUser(ArticleDetail detail){
        wechatMapper.updateArticleTargetUser(detail);
    }



    /**
     * 群发统计： 图文阅读人数,分享人数,收藏人数等等(图文详细统计)
     * 表：WX_IF_MESSAGE_STATS_DETAIL
     *
     * @param userReadList
     * @param shopId
     * @param userShareList
     */
    @Override
    public void insertArticleSummaryStatics(List<ArticleSummary> userReadList, Long shopId, List<UserShare> userShareList) {
        ArticleGroupDetails details = new ArticleGroupDetails();
        // 封装昨日图文统计wx_if_message_stats_detail表
        for (ArticleSummary summary : userReadList) {
            // 封装details
            setAriticleDetails(summary, details, shopId);
        }
        // 进一步封装Share details
        setShareUser(details, userShareList);// 设置转发人数

        Long id = wechatMapper.getMaxWxIfMsgStasDetailId();
        details.setId(id + 1L);

        wechatMapper.insertArticleSummaryStatics(details);
        logger.info("insertWx_if_message_stats_detail DB inserted " + details + " ");
    }

    @Override
    public void insertArticleSummaryHourStatics(List<ArticleSummary> userReadHourList, Long shopId, List<UserShare> userShareList) {
        String hourArr[] = {"000", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100", "1200", "1300", "1400",
                "1500", "1600", "1700", "1800", "1900", "2000", "2100", "2200", "2300"};
        for (String hour : hourArr) {
            ArticleGroupDetails details = new ArticleGroupDetails();
            boolean insertFlag = false;
            for (ArticleSummary summary : userReadHourList) {
                if (hour.equals(summary.getRef_hour())) {
                    // 封装details
                    setAriticleDetails(summary, details, shopId);
                    // 设置转发人数
                    setShareUser(details, userShareList);
                    insertFlag = true;
                }
            }
            if (insertFlag == true) {
                Long id = wechatMapper.getMaxWxIfMsgStasDetailId();
                details.setId(id + 1L);
                wechatMapper.insertArticleSummaryStatics(details);
                logger.info("insertWx_if_message_stats_detail DB inserted " + details + " ");
            }

        }
    }

    /**
     * 将ArticleSummary转化为ArticleDetails
     *
     * @param summary
     * @param details
     * @param shopId
     * @return
     */
    public ArticleGroupDetails setAriticleDetails(ArticleSummary summary, ArticleGroupDetails details, Long shopId) {
        details.setShopId(shopId);
        details.setStat_date(summary.getRef_date());

        int intPageReadUser = details.getInt_page_read_user();
        int intPageReadCount = details.getInt_page_read_count();
        int oriPageReadUser = details.getOri_page_read_user();
        int oriPageReadCount = details.getOri_page_read_count();
        int add_to_fav_user = details.getAdd_to_fav_user();
        int add_to_fav_count = details.getAdd_to_fav_count();
        if (summary.getUser_source() == 0) {// 会话
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_from_session_read_user(summary.getInt_page_read_user());
            details.setInt_page_from_session_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        } else if (summary.getUser_source() == 1) {// 好友转发
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_from_friends_read_user(summary.getInt_page_read_user());
            details.setInt_page_from_friends_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        } else if (summary.getUser_source() == 2) {// 朋友圈
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_from_feed_read_user(summary.getInt_page_read_user());
            details.setInt_page_from_feed_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        } else if (summary.getUser_source() == 4) {// 历史消息
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_from_hist_msg_read_user(summary.getInt_page_read_user());
            details.setInt_page_from_hist_msg_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        } else if (summary.getUser_source() == 5) {// 其他场景阅读人数
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_from_other_read_user(summary.getInt_page_read_user());
            details.setInt_page_from_other_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        } else if (summary.getUser_source() == 6) {// 看一看
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_fromkanyikan_read_user(summary.getInt_page_read_user());
            details.setInt_page_fromkanyikan_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        } else if (summary.getUser_source() == 7) {// 搜一搜
            details.setRef_hour(summary.getRef_hour());
            details.setInt_page_souyisou_read_user(summary.getInt_page_read_user());
            details.setInt_page_souyisou_read_count(summary.getInt_page_read_count());
            intPageReadUser += summary.getInt_page_read_user();
            intPageReadCount += summary.getInt_page_read_count();
            oriPageReadUser += summary.getOri_page_read_user();
            oriPageReadCount += summary.getOri_page_read_count();
            add_to_fav_user += summary.getAdd_to_fav_user();
            add_to_fav_count += summary.getAdd_to_fav_count();
        }
        details.setInt_page_read_user(intPageReadUser);
        details.setInt_page_read_count(intPageReadCount);
        details.setOri_page_read_user(oriPageReadUser);
        details.setOri_page_read_count(oriPageReadCount);
        details.setAdd_to_fav_user(add_to_fav_user);
        details.setAdd_to_fav_count(add_to_fav_count);
        return details;
    }

    /**
     * 设置转发人数
     *
     * @param details
     * @param userShareList
     * @return
     */
    private ArticleGroupDetails setShareUser(ArticleGroupDetails details, List<UserShare> userShareList) {
        int shareCount = 0;
        int shareUser = 0;
        if (userShareList != null && userShareList.size() > 0) {
            for (UserShare wxIfUserShare : userShareList) {
                shareCount += wxIfUserShare.getShare_count();
                shareUser += wxIfUserShare.getShare_user();
            }
        }
        details.setShare_count(shareCount);
        details.setShare_user(shareUser);
        return details;
    }

    /**
     * 用户发送消息统计
     *
     * @param msgHourlist
     * @param shopId
     * @param timeType
     */
    @Override
    public void insertBatchStreamMsgList(List<WxIfStreamMsgStatsData> msgHourlist, Long shopId, Byte timeType) {
        for (WxIfStreamMsgStatsData wxIfStreamMsgStatsData : msgHourlist) {
            wxIfStreamMsgStatsData.setTime_Type(timeType);
            wxIfStreamMsgStatsData.setShopId(shopId);
            wechatMapper.insertBatchStreamMsgList(wxIfStreamMsgStatsData);
        }
    }

    /**
     * 用户发送消息周统计
     *
     * @param weekList
     * @param shopId
     * @param timeType
     */
    @Override
    public void insertBatchStreamMsgWeekList(List<WxIfStreamMsgStatsData> weekList, Long shopId, Byte timeType) {
        for (WxIfStreamMsgStatsData wxIfStreamMsgStatsData : weekList) {
            wxIfStreamMsgStatsData.setTime_Type(timeType);
            wxIfStreamMsgStatsData.setShopId(shopId);
            long id = wechatMapper.getMaxWxIfMsgWeekId();
            wxIfStreamMsgStatsData.setId(id + 1L);
            wechatMapper.insertBatchStreamMsgWeekList(wxIfStreamMsgStatsData);
        }
    }

    /**
     * 查询开启定时任务的AppInfo列表
     *
     * @return
     */
    public List<AppInfoWithBLOBs> selectBatchAppInfoList() {
        return appInfoMapper.selectBatchAppInfoList();
    }

    /**
     * 查询定时任务锁状态
     *
     * @return 锁状态 0-非加锁 1-加锁 其他值-非加锁状态。
     * <p>
     * false-非加锁  true-加锁
     */
    @Transactional
    public boolean queryWxBatchLock(String sysId, int batchType) {
        // 查询锁
        String isRun = wechatMapper.selectWxBatchLock(sysId, batchType);
        logger.info("isRun>>>>>>" + isRun);
        if (StringUtils.isEmpty(isRun)) {
            logger.info("<><><><><><><><><><><><><><><><><><><><>");
            logger.info("the lock of batch(batch type:" + batchType + ") is not exists,create new lock");
            // 新增锁-加锁状态
            wechatMapper.insertWxBatchLockStart(sysId, batchType, BatchEnum.valueOf(batchType).getExpires());
            return false;
        } else {
            // 解锁状态
            if ("0".equals(isRun)) {
                // 加锁并返回解锁状态0
                wechatMapper.updateWxBatchLockStart(sysId, batchType, BatchEnum.valueOf(batchType).getExpires());
                return false;
            } else if ("1".equals(isRun)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 更新定时任务锁状态为非加锁状态
     *
     * @return
     */
    public int updateWxBatchLockEnd(String sysId, int batchType) {
        return wechatMapper.updateWxBatchLockEnd(sysId, batchType);
    }

    /**
     * 扫描代理商二维码发送提示消息
     *
     * @param scanResult
     * @param fromUserName
     * @param token
     * @param userBindInfo
     */
    public void sendAgentQRCodeScanMsg(int scanResult, String fromUserName, String token, Map<String, Object> userBindInfo) {
        StringBuilder content = new StringBuilder();
        if (scanResult == 0) {
            content.append("扫码成功")
                    .append("\n")
                    //.append("提示：您已关注过该公众号，此次扫码无效！");
                    .append("提示：您已关注过该公众号");
        } else if (scanResult == 1) {
            content.append("扫码成功")
                    .append("\n")
                    //.append("提示：您已成功扫描过该二维码！");
                    .append("提示：您已关注过该公众号。");
        } else if (scanResult == 2) {
            content.append("扫码成功")
                    .append("\n")
                    //.append("提示：您已扫描过其他同类型二维码，此次扫码无效！");
                    .append("提示：您已关注过该公众号！");
        } else if (scanResult == 3) {
            content.append("扫码成功")
                    .append("\n")
                    .append("欢迎您，我们竭诚为您服务！");
        } else if (scanResult == 4) {
            content.append("扫码失败")
                    .append("\n")
                    .append("提示：请您重试！");
        } else if (scanResult == -1) {
            content.append("扫码失败")
                    .append("\n")
                    .append("提示：二维码状态异常！");
        }
        WeixinMessageUtils.sendTextMsg(token, fromUserName, content.toString());
    }

    /**
     * 验证二维码有效性
     *
     * @param agentId
     * @return
     */
    public boolean checkAgentQRCodeUseful(String agentId) {
        // 根据二维码中的代理商ID查询代理商的状态
        List<Map<String, Object>> agentList = wechatMapper.getAgentListByAgentId(agentId);
        if (agentList != null && agentList.size() > 0) {
            return true;
        }
        return false;
    }
}