package com.basoft.service.impl.wechat.user;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import com.basoft.core.util.DateUtil;
import com.basoft.core.ware.common.framework.utilities.IntegerUtils;
import com.basoft.core.ware.wechat.domain.user.SessionMember;
import com.basoft.core.ware.wechat.domain.user.UserReturn;
import com.basoft.core.ware.wechat.domain.user.WXUser;
import com.basoft.core.ware.wechat.util.Constants;
import com.basoft.core.ware.wechat.util.WeixinUserUtils;
import com.basoft.service.dao.wechat.common.WechatMapper;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.user.WeixinUserService;
import com.basoft.service.entity.customer.cust.Cust;
import com.basoft.service.entity.wechat.appinfo.AppInfo;

@Service
public class WeixinUserServiceImpl implements WeixinUserService{
	@SuppressWarnings("unused")
	private final transient Log logger = LogFactory.getLog(this.getClass());

	@Resource
	private WechatService wechatService;
	
	@Resource
	private IdService idService;

	@Resource
	private WechatMapper wechatMapper;

	public SessionMember getSessionMemberById(String openid) {
		return wechatMapper.getSessionMemberById(openid);
	}

	public SessionMember getSessionMemberByCustId(Long userId) {
		return wechatMapper.getSessionMemberByCustId(userId);
	}

	/**
	 * 根据ID获取用户信息
	 * 
	 * @param openid
	 * @return
	 */
	public WXUser getUserInfoByOpenid(String openid) {
		return wechatMapper.getUserInfoByOpenid(openid);
	}

	/**
	 * 用户关注并同步用户信息
	 * 
	 * @param user
	 * @return
	 */
	public void userSubscribe(String token, WXUser user) throws Exception {
		UserReturn userReturn = WeixinUserUtils.getUserInfoById(token, user.getOpenid());
		
		// 值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
		if (userReturn.getSubscribe() == 0) {
			userUnsubscribe(user);
			return;
		}
		
		user.setSubscribe(userReturn.getSubscribe());
		String nickName = userReturn.getNickname();
		String nickBase64 = strToBase64(nickName);
		user.setNickname(nickBase64);

//		if (hasEmoji(nickName)) {
			user.setNickUnemoji(replaceEmoji(nickName));
//		} else {
//			user.setNickUnemoji(nickName);
//		}

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
		wechatMapper.userSubscribe(user);
		
		AppInfo appInfo = wechatService.selectAppInfoByKey(user.getSysId());
		
		Cust cust = new Cust();  
		cust.setShopId(appInfo.getShopId());
		cust.setCustSysId(idService.generateCust());
		// cust.setCustLoginId(custLoginId);
		cust.setWxIfOpenidP(user.getOpenid());
		cust.setEmail("");
		cust.setMobileNo("");
		cust.setWxIfIsSubscribe((byte)user.getSubscribe());
		cust.setWxIfNickNm(nickBase64);
		cust.setWxIfSexId((byte)IntegerUtils.valueOf(user.getSex(),0).intValue());
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

//		if (hasEmoji(nickName)) {
			cust.setWxIfNickUnemoji(replaceEmoji(nickName));
//		} else {
//			cust.setWxIfNickUnemoji(nickName);
//		}

		cust.setCustRealNm("");
		cust.setPwd("");
		wechatMapper.saveCust(cust);
	}
	
	/**
	 * 【批量定时任务】用户关注并同步用户信息
	 * @param shopId
	 * @param user
	 * @throws Exception
	 */
	public void userSubscribe(Long shopId, WXUser user, UserReturn userReturn) throws Exception {
		// 值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
		if (userReturn.getSubscribe() == 0) {
			userUnsubscribe(user);
			return;
		}
		user.setSubscribe(userReturn.getSubscribe());

        String nickName = userReturn.getNickname();
		String nickBase64 = strToBase64(nickName);
        user.setNickname(nickBase64);

//		if (hasEmoji(nickName)) {
			user.setNickUnemoji(replaceEmoji(nickName));
//		} else {
//			user.setNickUnemoji(nickName);
//		}
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
		wechatMapper.userSubscribe(user);
		
		Cust cust = new Cust();  
		cust.setShopId(shopId);
		cust.setCustSysId(idService.generateCust());
		// cust.setCustLoginId(custLoginId);
		cust.setWxIfOpenidP(user.getOpenid());
		cust.setEmail("");
		cust.setMobileNo("");
		cust.setWxIfIsSubscribe((byte)user.getSubscribe());
		cust.setWxIfNickNm(nickBase64);
		cust.setWxIfSexId((byte)IntegerUtils.valueOf(user.getSex(),0).intValue());
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

//		if (hasEmoji(nickName)) {
			cust.setWxIfNickUnemoji(replaceEmoji(nickName));
//		} else {
//			cust.setWxIfNickUnemoji(nickName);
//		}

		cust.setCustRealNm("");
		cust.setPwd("");
		wechatMapper.saveCust(cust);
	}

	/**
	 * 用户取消关注并同步信息
	 * 
	 * @param user
	 * @return
	 */
	public void userUnsubscribe(WXUser user) {
		user.setSubscribe(Constants.UserSubscribeType.UN_SUBSCRIBE);
		wechatMapper.userUnsubscribe(user);
	}

	/**
	 * 批量用户取消关注（更细用户关注状态）
	 * 
	 * @param sysId
	 * @param ifStartDate
	 */
	@Override
	public void batchUserUnsubscribe(String sysId, Date ifStartDate) throws Exception {
		wechatMapper.batchUserUnsubscribe(sysId, ifStartDate);
	}



	/**
	 * @Title String转base64
	 * @Param
	 * @return java.lang.String
	 * @author Dong Xifu
	 * @date 2019/5/10 下午3:06
	 */
	public static String strToBase64(String nickName){
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
		String base64Sign = null;
		try {
			base64Sign = base64.encodeToString(nickName.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return base64Sign;
    }


    /**
     * @Title 判断是否存在特殊字符串
     * @Param content
     * @return boolean
     * @author Dong Xifu
     * @date 2019/5/10 下午1:59
     */
    public static boolean hasEmoji(String content){
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if(matcher .find()){
            return true;
        }
        return false;
    }

    /**
     * @Title 替换字符串中的emoji字符
     * @Param str
     * @return java.lang.String
     * @author Dong Xifu
     * @date 2019/5/10 下午1:59
     */
    public static String replaceEmoji(String str) {
    	
    	return replaceEmojiNew(str);
    	
//        if (!hasEmoji(str)) {
//            return str;
//        } else {
//        	str = str.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", " ");
//            return str;
//        }


    }

    public static String replaceEmojiNew(String str)
    {
    	String returnStr = "";
        for(int i = 0 ; i < str.length() ; i++)
        {
            char ch = str.charAt(i);
            Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
            if(UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock) ||  
    	        	UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock) || 
    	            UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock) ||
    	            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(unicodeBlock) ||
    	            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(unicodeBlock) ||
    	            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B.equals(unicodeBlock) ||
    	            UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(unicodeBlock) ||
    	            UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT.equals(unicodeBlock) ||
    	            UnicodeBlock.HIRAGANA.equals(unicodeBlock) ||
    	            UnicodeBlock.KATAKANA.equals(unicodeBlock) ||
    	            UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(unicodeBlock) ||
    	            UnicodeBlock.BASIC_LATIN.equals(unicodeBlock))
            	returnStr = returnStr + ch;
        }
        return returnStr;
    }

	public List<Map<String, Object>> getListWxUser() {
		return wechatMapper.getListWxUser();
	}
}
