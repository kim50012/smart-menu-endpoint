package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.model.wxuser.Cust;
import com.basoft.eorder.application.wx.model.wxuser.UserReturn;
import com.basoft.eorder.application.wx.model.wxuser.WXUser;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.WxUserDTO;
import com.basoft.eorder.interfaces.query.WxUserQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.IntegerUtils;
import com.basoft.eorder.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:13 2019/5/15
 **/
@Slf4j
@Component("WxUserQuery")
public class JdbcWxUserQueryImpl extends BaseRepository implements WxUserQuery {
    /**
     * 根据openid从数据库获取用户信息
     *
     * @param map
     * @return
     */
    @Override
    public WxUserDTO getWxUser(Map<String, Object> map) {
        // 查询关注状态的微信用户信息
        StringBuilder sql = new StringBuilder("select OPENID,NICKNAME,NICK_UNEMOJI,SEX,CITY,COUNTRY" +
                ",PROVINCE,HEADIMGURL,SUBSCRIBE as subscribe,SUBSCRIBE_TIME from bawechat.wx_user wu where wu.SUBSCRIBE = 1 ");
        String openId = Objects.toString(map.get("openId"), null);
        if (StringUtils.isNotBlank(openId)) {
            sql.append(" and wu.OPENID = :openId \n");
        }
        log.info(sql.toString());
        return this.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<WxUserDTO>(WxUserDTO.class));
    }

    /**
     * 根据openid从公众平台获取用户信息并插入到数据库
     *
     * @param map
     * @return
     */
    @Override
    public WxUserDTO reGetWxUser(Map<String, Object> map) {
        String access_token = Objects.toString(map.get("access_token"), null);
        String openId = Objects.toString(map.get("openId"), null);

        //从微信公众平台获取微信用户信息
        WechatAPI wechatAPI = WechatAPI.getInstance();
        UserReturn userReturn = wechatAPI.getUserInfoByOpenId(access_token, openId);

        // 值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
        if (userReturn.getSubscribe() == 0) {
            log.warn("<<<<<<【重取微信用户信息】该用户还没有关注公众号！！！>>>>>>");
            return null;
        }

        WXUser user = new WXUser();
        try {
            user.setOpenid(openId);
            user.setSysId("1");
            user.setSubscribe(userReturn.getSubscribe());
            String nickName = userReturn.getNickname();
            String nickBase64 = StringUtil.strToBase64(nickName);
            user.setNickname(nickBase64);
            user.setNickUnemoji(StringUtil.replaceEmoji(nickName));
            user.setSex(userReturn.getSex());
            user.setCity(userReturn.getCity());
            user.setCountry(userReturn.getCountry());
            user.setProvince(userReturn.getProvince());
            user.setLanguage(userReturn.getLanguage());
            user.setHeadimgurl(userReturn.getHeadimgurl());
            user.setSubscribe_time(DateUtil.secondsToDate(userReturn.getSubscribe_time()));
            user.setUnionid(userReturn.getUnionid());
            user.setGroupid(userReturn.getGroupid());
            user.setRemark(userReturn.getRemark());
            log.info("【重取微信用户信息】WXUser>>>>>>" + user.toString());
            insertWxUser(user);
            log.info("【重取微信用户信息】WXUser>>>>>>更新成功\n\n\n");

            Cust cust = new Cust();
            cust.setShopId(Long.valueOf((String) map.get("shopId")));
            cust.setCustSysId((Long) map.get("custId"));
            cust.setWxIfOpenidP(user.getOpenid());
            cust.setEmail("");
            cust.setMobileNo("");
            cust.setWxIfIsSubscribe((byte) user.getSubscribe());
            cust.setWxIfNickNm(nickBase64);
            cust.setWxIfSexId((byte) IntegerUtils.valueOf(user.getSex(), 0).intValue());
            cust.setWxIfLanguage(user.getLanguage());
            cust.setWxIfCountryNm(user.getCountry());
            cust.setWxIfProvinceNm(user.getProvince());
            cust.setWxIfCityNm(user.getCity());
            cust.setWxIfHeadimgurl(user.getHeadimgurl());
            cust.setWxIfSubscribeTime(user.getSubscribe_time());
            cust.setWxIfUnionid(user.getUnionid());
            cust.setWxSubscribeDt(user.getSubscribe_time());
            cust.setWxIfGroupid(Long.valueOf(user.getGroupid()));
            cust.setWxIfRemark(user.getRemark());
            cust.setWxIdP("");
            cust.setCustNickNm(nickBase64);
            cust.setWxIfNickUnemoji(StringUtil.replaceEmoji(nickName));
            cust.setCustRealNm("");
            cust.setPwd("");
            log.info("【重取微信用户信息】WXCust>>>>>>" + cust.toString());
            insertWxCust(cust);
            log.info("【重取微信用户信息】WXCust>>>>>>更新成功\n\n\n");

            // 迁入finally块
            /*// 构造WxUserDTO
            WxUserDTO wxUserDTO = new WxUserDTO();
            try {
                BeanUtils.copyProperties(wxUserDTO, user);
            } catch (IllegalAccessException e) {
                log.error("【重取微信用户信息】", e);
            } catch (InvocationTargetException e) {
                log.error("【重取微信用户信息】", e);
            }
            log.info("【重取微信用户信息】获取到的用户信息WxUserDTO>>>>>>" + wxUserDTO.toString());
            return wxUserDTO;*/

        } catch (Exception e) {
            log.error("【重取微信用户信息】更新微信用户信息到数据库出错：" + e.getMessage());
            log.error("【重取微信用户信息】更新微信用户信息到数据库出错：" + e.getMessage(), e);
        } finally {
            // 构造WxUserDTO
            WxUserDTO wxUserDTO = new WxUserDTO();
            try {
                BeanUtils.copyProperties(wxUserDTO, user);
            } catch (IllegalAccessException e) {
                log.error("【重取微信用户信息】", e);
                return null;
            } catch (InvocationTargetException e) {
                log.error("【重取微信用户信息】", e);
                return null;
            }
            log.info("【重取微信用户信息】获取到的用户信息WxUserDTO>>>>>>" + wxUserDTO.toString());
            return wxUserDTO;
        }
    }

    /**
     * insert wxuser
     *
     * @param wxUser
     */
    private void insertWxUser(WXUser wxUser) {
        String insertWxUserSQL = "insert into bawechat.wx_user\n" +
                "(\n" +
                "    openid\n" +
                "    , sys_id\n" +
                "    , unionid\n" +
                "    , nickname\n" +
                "    , nick_unemoji\n" +
                "    , sex\n" +
                "    , subscribe\n" +
                "    , is_first_subscribe\n" +
                "    , city\n" +
                "    , country\n" +
                "    , province\n" +
                "    , language\n" +
                "    , headimgurl\n" +
                "    , subscribe_time\n" +
                "    , groupid\n" +
                "    , remark\n" +
                "    , create_dt\n" +
                "    , update_dt\n" +
                ") \n" +
                "values \n" +
                "( \n" +
                "    :openid\n" +
                "    , :sysId\n" +
                "    , :unionid\n" +
                "    , :nickname\n" +
                "    , :nickUnemoji\n" +
                "    , :sex\n" +
                "    , 1\n" +
                "    , 'Y'\n" +
                "    , :city\n" +
                "    , :country\n" +
                "    , :province\n" +
                "    , :language\n" +
                "    , :headimgurl\n" +
                "    , :subscribe_time\n" +
                "    , :groupid\n" +
                "    , :remark\n" +
                "    , sysdate()\n" +
                "    , sysdate()\n" +
                ")\n" +
                "on duplicate key update\n" +
                "    unionid = :unionid\n" +
                "    , nickname = :nickname\n" +
                "    , nick_unemoji = :nickUnemoji\n" +
                "    , sex = :sex\n" +
                "    , subscribe = 1\n" +
                "    , is_first_subscribe = 'N'\n" +
                "    , city = :city\n" +
                "    , country = :country\n" +
                "    , province = :province\n" +
                "    , language = :language\n" +
                "    , headimgurl = :headimgurl\n" +
                "    , subscribe_time = :subscribe_time\n" +
                //"    , unsubscribe_time = :\n" +
                "    , groupid = :groupid\n" +
                "    , remark = :remark\n" +
                "    , update_dt = sysdate()";
        //log.info("insertWxUserSQL>>>>>>" + insertWxUserSQL);
        this.getNamedParameterJdbcTemplate().update(insertWxUserSQL, new BeanPropertySqlParameterSource(wxUser));
    }

    /**
     * insert cust
     *
     * @param cust
     */
    private void insertWxCust(Cust cust) {
        String insertWxCustSQL = "insert into bawechat.cust\n" +
                "(\n" +
                "    cust_sys_id\n" +
                "    , cust_login_id\n" +
                "    , wx_if_openid_p\n" +
                "    , email\n" +
                "    , mobile_no\n" +
                "    , wx_if_is_subscribe\n" +
                "    , wx_if_nick_nm\n" +
                "    , wx_if_nick_unemoji\n" +
                "    , wx_if_sex_id\n" +
                "    , wx_if_language\n" +
                "    , wx_if_country_nm\n" +
                "    , wx_if_province_nm\n" +
                "    , wx_if_city_nm\n" +
                "    , wx_if_headimgurl\n" +
                "    , wx_if_subscribe_time\n" +
                "    , wx_if_unionid\n" +
                "    , wx_if_groupid\n" +
                "    , wx_if_remark\n" +
                "    , wx_subscribe_dt\n" +
                "    , wx_id_p\n" +
                "    , cust_nick_nm\n" +
                "    , cust_real_nm\n" +
                "    , pwd\n" +
                "    , active_sts\n" +
                "    , modified_dt\n" +
                "    , created_dt\n" +
                ") \n" +
                "values \n" +
                "( \n" +
                "    :custSysId\n" +
                "    , :custLoginId\n" +
                "    , :wxIfOpenidP\n" +
                "    , :email\n" +
                "    , :mobileNo\n" +
                "    , :wxIfIsSubscribe\n" +
                "    , :wxIfNickNm\n" +
                "    , :wxIfNickUnemoji\n" +
                "    , :wxIfSexId\n" +
                "    , :wxIfLanguage\n" +
                "    , :wxIfCountryNm\n" +
                "    , :wxIfProvinceNm\n" +
                "    , :wxIfCityNm\n" +
                "    , :wxIfHeadimgurl\n" +
                "    , :wxIfSubscribeTime\n" +
                "    , :wxIfUnionid\n" +
                "    , :wxIfGroupid\n" +
                "    , :wxIfRemark\n" +
                "    , ifnull(:wxSubscribeDt, sysdate())\n" +
                "    , :wxIdP\n" +
                "    , :custNickNm\n" +
                "    , :custRealNm\n" +
                "    , ifnull(:pwd, '')\n" +
                "    , 1\n" +
                "    , sysdate()\n" +
                "    , sysdate()\n" +
                ")\n" +
                "on duplicate key update\n" +
                "    email = :email\n" +
                "    , mobile_no = :mobileNo\n" +
                "    , wx_if_is_subscribe = :wxIfIsSubscribe\n" +
                "    , wx_if_nick_nm = :wxIfNickNm\n" +
                "    , wx_if_nick_unemoji = :wxIfNickUnemoji\n" +
                "    , wx_if_sex_id = :wxIfSexId\n" +
                "    , wx_if_language = :wxIfLanguage\n" +
                "    , wx_if_country_nm = :wxIfCountryNm\n" +
                "    , wx_if_province_nm = :wxIfProvinceNm\n" +
                "    , wx_if_city_nm = :wxIfCityNm\n" +
                "    , wx_if_headimgurl = :wxIfHeadimgurl\n" +
                "    , wx_if_subscribe_time = :wxIfSubscribeTime\n" +
                "    , wx_if_unionid = :wxIfUnionid\n" +
                "    , wx_if_groupid = :wxIfGroupid\n" +
                "    , wx_if_remark = :wxIfRemark\n" +
                "    , wx_subscribe_dt = ifnull(:wxSubscribeDt, sysdate())\n" +
                "    , wx_id_p = :wxIdP\n" +
                "    , cust_nick_nm = :custNickNm\n" +
                "    , cust_real_nm = :custRealNm\n" +
                "    , pwd = ifnull(:pwd, '')\n" +
                "    , active_sts = 1\n" +
                "    , modified_dt = sysdate()";
        //log.info("insertWxUserSQL>>>>>>" + insertWxCustSQL);
        this.getNamedParameterJdbcTemplate().update(insertWxCustSQL, new BeanPropertySqlParameterSource(cust));
    }
}
