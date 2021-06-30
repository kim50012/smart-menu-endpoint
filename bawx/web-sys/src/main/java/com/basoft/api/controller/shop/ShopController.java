package com.basoft.api.controller.shop;

import java.math.BigDecimal;

import com.basoft.service.definition.base.RedisService;
import com.basoft.service.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basoft.api.controller.BaseController;
import com.basoft.api.security.TokenSession;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.ware.wechat.domain.WechatAccount;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.shop.ShopWithBLOBs;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/shop")
public class ShopController extends BaseController {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private WechatService wechatService;

	@Autowired
	private RedisService redisService;

	/**
	 * 根据公众号id获取公众号信息
	 * 
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/{shopId}/info")
	public Echo<?> getShopInfo(@PathVariable String shopId) {
		// 验证公众号ID
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		ShopWithBLOBs shopWithBLOBs = shopService.getShopById(Long.parseLong(shopId));
		if (null == shopWithBLOBs) {
			throw new BizException(ErrorCode.SYS_EMPTY);
		}
		return new Echo<ShopWithBLOBs>(shopWithBLOBs);
		
		//字段标识：公众号分类-SCID 账户类型-AUTH_TYPE_ID 资讯功能-MAIN_GC_ID
	}
	
	/**
	 * 新增公众号
	 * 
	 * @param shopWithBLOBs
	 * @return
	 */
	@PostMapping(value = "/add")
	public Echo<?> addShop(@RequestBody ShopWithBLOBs shopWithBLOBs) {
		// 初始化公众号信息
		Long shopId = initShopInfo(shopWithBLOBs, getTokenSession());
		
		//新增对应的AppInfo信息-start
		WechatAccount wechatAccount = wechatService.generateAccount();
		AppInfoWithBLOBs appInfo = new AppInfoWithBLOBs();
		appInfo.setSysId(wechatAccount.getSysId());
		appInfo.setToken(wechatAccount.getToken());
		appInfo.setEncordingAesKey(wechatAccount.getEncordingAesKey());
		
		appInfo.setShopId(Long.valueOf(shopId));
		appInfo.setCompNm(shopWithBLOBs.getShopNm() == null ? "" : shopWithBLOBs.getShopNm());// 存储公众号名称
		
		appInfo.setAppId(shopWithBLOBs.getWxAppId() == null ? "" : shopWithBLOBs.getWxAppId());
		appInfo.setAppSecret(shopWithBLOBs.getWxAppSecret() == null ? "" : shopWithBLOBs.getWxAppSecret());
		appInfo.setOriginalAppId(shopWithBLOBs.getWxOpenidS());
		appInfo.setUrl(shopWithBLOBs.getWxApiUrl() == null ? "" : shopWithBLOBs.getWxApiUrl());

		//新增菜单权限
		// TODO 此处位置不对，没有事务，也不符合开发架构规范，另外业务也不通，还空指针
		// MenuAuthController mennu = new MenuAuthController();
		// mennu.insertMenuAuthAll(shopId);

		//AppInfo默认设置
		appInfo.setAccountType((byte)0);
		appInfo.setTransferCustomerService((byte)0);
		appInfo.setAccountStatus((byte)1);
		appInfo.setOpenBatchJob((byte)0);
		//新增对应的AppInfo信息-end

		// 新增公众号
		int i = shopService.addShop(shopWithBLOBs, appInfo);

		if (i == 0) {
			throw new BizException(ErrorCode.SYS_ERROR);
		}

		return new Echo<Integer>(i);
	}

	/**
	 * 初始化公众号信息
	 * 
	 * @param shop
	 * @param tokenSession
	 */
	private Long initShopInfo(ShopWithBLOBs shop, TokenSession tokenSession) {
		Long shopId = idService.generateShop();
		// 设置公众号ID
		shop.setShopId(shopId);
		// 公司id
		shop.setgCorpId(tokenSession.getGroupId());
		// 创建用户id
		shop.setCreatedUserId(tokenSession.getUserId());
		// 商城id
		shop.setMktId(0);
		// 是否商城的门户店铺
		shop.setMktIsMainshop((byte)0);
		// 管理员姓名
		shop.setLegalPersonNm("basoft");
		// 管理员身份证号码
		shop.setLegalPersonIdcard("basoft");
		// 主营商品ID
		shop.setMainGcId(0);
		// 店铺状态
		shop.setStsId((byte)1);
		// 统计相关
		shop.setSumSalesCnt((short)0);
		shop.setSumSalesQty((short)0);
		shop.setSumSalesAmt(new BigDecimal("0"));
		shop.setSumCollectedQty((short)0);
		shop.setAvgPraiseRate((byte)0);
		shop.setAvgGoodsDescScore((byte)0);
		shop.setAvgServiceScore((byte)0);
		shop.setAvgDeliveryRate((byte)0);
		
		return shopId;
	}

	/**
	 * 根据公众号id修改公众号信息
	 * 
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/{shopId}/update")
	public Echo<?> updateShop(@PathVariable String shopId, @RequestBody ShopWithBLOBs shopWithBLOBs) {
		// 验证公众号ID
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}

		// 设置公众号ID
		shopWithBLOBs.setShopId(Long.parseLong(shopId));
		
		// 查询对应的AppInfo的sysId
		AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(Long.valueOf(shopId));
		// 修改appInfo
		if(StringUtils.isNotBlank(shopWithBLOBs.getShopNm())) {
			appInfo.setCompNm(shopWithBLOBs.getShopNm());//存储公众号名称
		}
		if(StringUtils.isNotBlank(shopWithBLOBs.getWxAppId())) {
			appInfo.setAppId(shopWithBLOBs.getWxAppId());
		}
		if(StringUtils.isNotBlank(shopWithBLOBs.getWxAppSecret())) {
			appInfo.setAppSecret(shopWithBLOBs.getWxAppSecret());
		}
		if(StringUtils.isNotBlank(shopWithBLOBs.getWxOpenidS())) {
			appInfo.setOriginalAppId(shopWithBLOBs.getWxOpenidS());
		}
		if(StringUtils.isNotBlank(shopWithBLOBs.getWxApiUrl())) {
			appInfo.setUrl(shopWithBLOBs.getWxApiUrl());
		}

		// 更新公众号信息
		int i = shopService.updateShopById(shopWithBLOBs, appInfo);

		if (i == 0) {
			throw new BizException(ErrorCode.SYS_EMPTY);
		}

		return new Echo<Integer>(i);
	}
	
	/**
	 * 根据公众号id删除公众号信息
	 * 
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/{shopId}/delete")
	public Echo<?> deleteShop(@PathVariable String shopId) {
		// 验证公众号ID
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		
		// 查询对应的AppInfo的sysId
		AppInfo appInfo = shopService.getAppInfoByShopId(Long.valueOf(shopId));
		if(ObjectUtils.isEmpty(appInfo)) {
			//throw new BizException(ErrorCode.PARAM_MISSING);
			
			// 防止调用删除时空指针异常
			appInfo = new AppInfo();
			appInfo.setSysId("");
		}

		// 更新公众号
		int i = shopService.deleteShopById(Long.parseLong(shopId), appInfo.getSysId());

		if (i == 0) {
			throw new BizException(ErrorCode.SYS_EMPTY);
		}

		return new Echo<Integer>(i);
	}
	
	/**
	 * 根据公众号id开启公众号
	 * 
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/{shopId}/open")
	public Echo<?> openShop(@PathVariable String shopId) {
		// 验证公众号ID
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}

		// 开启公众号
		int i = shopService.openShopById(Long.parseLong(shopId));

		if (i == 0) {
			throw new BizException(ErrorCode.SYS_EMPTY);
		}

		return new Echo<Integer>(i);
	}
	
	/**
	 * 根据公众号id关闭公众号
	 * 
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/{shopId}/close")
	public Echo<?> closeShop(@PathVariable String shopId) {
		// 验证公众号ID
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}

		// 关闭公众号
		int i = shopService.closeShopById(Long.parseLong(shopId));

		if (i == 0) {
			throw new BizException(ErrorCode.SYS_EMPTY);
		}

		return new Echo<Integer>(i);
	}
	
	/**
	 * 为打开微信公众号管理主页面做数据准备
	 * 
	 * @param shopId
	 * @return
	 */
	@PostMapping(value = "/{shopId}/preopen")
	public Echo<?> preOpenShop(@PathVariable String shopId) {
		// 验证公众号ID
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}

		// shopId缓存
		//getTokenSession().setShopId(shopId);
		TokenSession tokenSession = (TokenSession)redisService.getTokenSession(getToken());
		tokenSession.setShopId(shopId);
		Boolean  res =  redisService.setTokenSession(getToken(),tokenSession);

		if(!res){
			throw new IllegalArgumentException("can not set to Redis");
		}

		log.info("ShopController>>preOpenShop::" + getTokenSession().toString());

		return new Echo<String>("OK");
	}
}