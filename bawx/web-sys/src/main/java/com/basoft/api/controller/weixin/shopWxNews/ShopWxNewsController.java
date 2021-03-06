package com.basoft.api.controller.weixin.shopWxNews;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.wechat.shopFile.ShopFileVO;
import com.basoft.api.vo.wechat.shopWxNews.*;
import com.basoft.core.constants.CoreConstants;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.util.DateTimeUtil;
import com.basoft.core.util.ListUtil;
import com.basoft.core.ware.common.framework.utilities.StringUtil;
import com.basoft.core.ware.wechat.domain.mass.MassReturn;
import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.shop.ShopFileService;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.shopWxNews.MassService;
import com.basoft.service.definition.wechat.shopWxNews.MaterialService;
import com.basoft.service.definition.wechat.shopWxNews.ShopWxNewService;
import com.basoft.service.dto.wechat.shopWxNew.NewsStatsDataDto;
import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHead;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import com.basoft.service.param.wechat.shopWxNews.PreviewNewsForm;
import com.basoft.service.param.wechat.shopWxNews.SenNewsForm;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsForm;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsQueryParam;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description:????????????
 * 
 * @Author:DongXifu
 * @Date Created in ??????11:49 2018/4/16
 */
@Configuration
@RestController
@Slf4j
public class ShopWxNewsController extends BaseController{
    @Autowired
    private IdService idService;

    @Autowired
    private ShopWxNewService shopWxNewService;

    @Autowired
    private ShopFileService shopFileService;
	@Autowired
	private WechatService wechatService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private MassService massServcie;
	
	@Value("${basoft.web.upload-path}")
    private String webUploadPath;
	
	@Value("${static.resources.union.path}")
    private String fileUrlKeyWord;

	/**
	 * ??????????????????
	 * 
	 * @author Dong Xifu
	 * @Date 2018/4/16 ??????11:54
	 * 
	 * @param page
	 * @param rows
	 * @param param
	 * @param newsType
	 * @return
	 */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/shopWxMatterFindAll",method = RequestMethod.GET)
    public ApiJson<List> shopWxMatterFindAll(@RequestParam(value = "page",defaultValue = "1" )String page,
                                       @RequestParam(value = "rows",defaultValue = "20" )String rows,
                                       @RequestParam(value = "fileName",defaultValue = "" )String param,
                                       @RequestParam(value = "newsType",defaultValue = "" )String newsType){

        ShopWxNewsQueryParam queryParam = new ShopWxNewsQueryParam();
        queryParam.setShopId(getShopId());
        queryParam.setPage(Integer.valueOf(page));
        queryParam.setRows(Integer.valueOf(rows));
        queryParam.setParam(param);
        queryParam.setNewsType(newsType);
        ApiJson<List> result = new ApiJson<>();

        try {
            PageInfo<ShopWxNewDto> pageInfo = shopWxNewService.matterFindAll(queryParam);
            for (ShopWxNewDto dto:pageInfo.getList()) {
                ShopWxNewDto entity = shopWxNewService.getShopWxNewsHead(dto.getMsgId(),getShopId());
                if(entity!=null) {
                    dto.setMtitle(entity.getMtitle());
                    dto.setFullUrl(entity.getFullUrl());
                    dto.setNewsType(entity.getNewsType());
                    dto.setNewsTypeStr(entity.getNewsTypeStr());
                    dto.setMdigest(entity.getMdigest());
                    dto.setMsourceUrl(entity.getMsourceUrl());
                    dto.setReadCnt(entity.getReadCnt());
                    dto.setMshowCoverPic(entity.getMshowCoverPic());
                }
            }
            if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                result.setPage(pageInfo.getPageNum());
                result.setRecords((int) pageInfo.getTotal());
                result.setTotal(pageInfo.getPages());
                result.setRows(pageInfo.getList().stream().map(data -> new ShopWxNewsVo(data)).collect(Collectors.toList()));
            } else {
                result.setPage(1);
                result.setRecords(0);
                result.setTotal(0);
                result.setRows(null);
            }

            result.setErrorCode(0);
            result.setErrorMsg("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * ?????????????????????
	 * 
	 * @author Dong Xifu
	 * @Date 2018/4/19 ??????3:35
	 * 
	 * @param bloBs
	 * @return
	 */
    @Deprecated
	private Map<String, Object> insertNewsHead(ShopWxNewsItemWithBLOBs bloBs) {
		Map<String, Object> map = new HashMap<String, Object>();
		ShopWxNewsForm form = new ShopWxNewsForm();
		
		ShopWxNewsHead shopWxNewsHead = new ShopWxNewsHead();
		shopWxNewsHead.setShopId(getShopId());
		shopWxNewsHead.setMsgNm(BizConstants.SHOP_WX_NEWS_MSGNM);
		shopWxNewsHead.setCreatedId(getUserId());
		shopWxNewsHead.setCreatedDt(new Date());
		shopWxNewsHead.setIsDelete(BizConstants.IS_DELETE_N);
		
		form.setShopWxNewHead(shopWxNewsHead);
		Long msgId = idService.generateNewsHead();
		Long newsId = idService.generateNewsItem();
		map.put("msgId", msgId);
		map.put("headFlag", false);
		String edit = "edit";
		if (bloBs.getMsgId() < 1 && bloBs.getNewsId() < 1) {
			edit = "add";
			shopWxNewsHead.setMsgId(msgId);
			bloBs.setMsgId(msgId);
			bloBs.setNewsId(newsId);
			map.put("headFlag", true);
			map.put("edit", edit);
			bloBs.setCreatedDt(new Date());
			bloBs.setCreatedId(getUserId());
		} else if (bloBs.getNewsId() < 1 && bloBs.getMsgId() > 1) {
			edit = "add";
			bloBs.setNewsId(newsId);
			bloBs.setCreatedDt(new Date());
			bloBs.setCreatedId(getUserId());
		}
		map.put("edit", edit);
		bloBs.setShopId(getShopId());
		// bloBs.setMshowCoverPic((byte)1);
		bloBs.setModifiedId(getUserId());
		bloBs.setModifiedDt(new Date());
		bloBs.setIsDelete(BizConstants.IS_DELETE_N);
		form.setShopWxNewsItemWithBLOBs(bloBs);
		map.put("form", form);
		return map;
	}
	
    /**
     *@author Dong Xifu
     *@Date 2018/4/16 ??????2:38
     *@describe ??????????????????
     *@param
     *@return
     **/
    @Deprecated
	@PostMapping(value = "/insertOrUpdateShopNewsHead", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ShopWxNewsItemDetailVo insertOrUpdateShopNewsHead(@RequestBody ShopWxNewsItemWithBLOBs bloBs) {
		Map<String, Object> map = insertNewsHead(bloBs);
		
		Boolean headFlag = (Boolean) map.get("headFlag");
		ShopWxNewsForm form = (ShopWxNewsForm) map.get("form");
		String edit = (String) map.get("edit");
		shopWxNewService.insertOrUpdateShopNewsItem(form, headFlag, edit);
		return new ShopWxNewsItemDetailVo(form.getShopWxNewsItemWithBLOBs());
	}

	/**
	 * ???list??????????????????
	 * 
	 * @author Dong Xifu
	 * @Date 2018/5/7 ??????10:02
	 * 
	 * @param
	 * @return
	 */
	@PostMapping(value = "/insertOrUpdateShopNews", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> insertOrUpdateShopNews(@Validated @RequestBody ShopWxNewsForm form) {
		Long msgId = form.getMsgId();
		form.setShopId(getShopId());
		form.setUserId(getUserId());
		Boolean headFlag = false;
		if (msgId == 0 || msgId == null) {
			headFlag = true;
			msgId = idService.generateNewsHead();
			form.setMsgId(msgId);
			ShopWxNewsHead newsHead = setNewsHead(form);
			form.setShopWxNewHead(newsHead);
		}
		int result = shopWxNewService.insertOrUpdateShopNewsByList(form, headFlag);
		return new Echo<Integer>(result);
	}
	
	/**
	 * ?????? ShopWxNewsHead
	 *
	 * @param
	 * @return
	 */
	private ShopWxNewsHead setNewsHead(ShopWxNewsForm form) {
		ShopWxNewsHead shopWxNewsHead = new ShopWxNewsHead();
		shopWxNewsHead.setShopId(getShopId());
		shopWxNewsHead.setMsgId(form.getMsgId());
		shopWxNewsHead.setMsgNm(BizConstants.SHOP_WX_NEWS_MSGNM);
		shopWxNewsHead.setCreatedId(getUserId());
		shopWxNewsHead.setCreatedDt(new Date());
		shopWxNewsHead.setIsDelete(BizConstants.IS_DELETE_N);
		return shopWxNewsHead;
	}

    /**
     *@author Dong Xifu
     *@Date 2018/4/16 ??????1:07
     *@describe ???????????????????????????????????????
     *@param
     *@return
     **/
	@Deprecated
    @PostMapping(value = "/insertOrUpdateShopNewsItem",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int insertOrUpdateShopNewsItem(@RequestBody ShopWxNewsItemWithBLOBs bloBs) {
        ShopWxNewsForm form = new ShopWxNewsForm();
        bloBs.setShopId(getShopId());
        bloBs.setMsgId(bloBs.getMsgId());
        String edit = "edit";
        if(bloBs.getNewsId()<1){
            bloBs.setNewsId(idService.generateNewsItem());
            edit = "add";
        }
        bloBs.setCreatedDt(new Date());
        bloBs.setCreatedId(getUserId());
        bloBs.setIsDelete(BizConstants.IS_DELETE_N);
        form.setShopWxNewsItemWithBLOBs(bloBs);
        boolean headFlag = false;//???????????????
        return shopWxNewService.insertOrUpdateShopNewsItem(form, headFlag, edit);
    }
	
	/**
	 * ???????????????????????? newsId(???????????????news item)
	 * 
	 * @param
	 * @return
	 * 
	 * @author Dong Xifu
	 * @Date 2018/4/19 ??????4:09
	 */
    @Deprecated
	@RequestMapping(value = "/getShopWxNewsItemOne", method = RequestMethod.GET)
	public ShopWxNewsItemDetailVo getShopWxNewsItemOne(@RequestParam(value = "msgId") String msgId, @RequestParam(value = "newsId") String newsId) {
		ShopWxNewsItemWithBLOBs shopWxNewsItemOne = shopWxNewService.getShopWxNewsItemOne(getShopId(), Long.valueOf(msgId), Long.valueOf(newsId));
		return new ShopWxNewsItemDetailVo(shopWxNewsItemOne);
	}

	/**
	 * @describe ????????????/?????????????????????news head????????????news item???
	 *
	 * @author Dong Xifu
	 * @Date 2018/4/17 ??????9:31
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getShopWxNewsItem/{msgId}", method = RequestMethod.GET)
	public Echo<?> getShopWxNewsItem(@PathVariable String msgId, @RequestParam(value = "menuMsgType", defaultValue = "1") String menuMsgType) {
		ShopWxNewDto dto = new ShopWxNewDto();
		Long shopId = getShopId();
		// ????????????
		if (menuMsgType.equals("1")) {
			if (StringUtils.isBlank(msgId) || "null".equals(msgId)) {
				return new Echo<Integer>(0);
			}
			dto = shopWxNewService.getShopWxNewsItem(Long.valueOf(msgId), shopId);
		} else {
			ShopFile shopFileBykey = shopFileService.getShopFileBykey(shopId, Long.valueOf(msgId));
			if (shopFileBykey == null) {
				throw new BizException(ErrorCode.SYS_EMPTY);
			}
			return new Echo<>(new ShopFileVO(shopFileBykey));
		}
		return new Echo<>(new ShopWxNewsItemVo(dto));
	}
	
	/************************************************************???????????????????????????*********************************************************************/
	/**
	 * ???????????????id???????????????????????????????????????user??????
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getShopNewsPreviewUsers", method = RequestMethod.GET)
	public Echo<?> getShopNewsPreviewUsers() {
		Long shopId = getShopId();
		List<Map<String, Object>> userList =  shopWxNewService.selectNewsPreviewUsers(shopId);
		return new Echo<>(userList);
	}

	
	/**
	 * ??????????????????
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/previewNews",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> previewNews(@RequestBody PreviewNewsForm form){
		AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(getShopId());
		String token = wechatService.getAccessToken(appInfo);
		// String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
		
		// ????????????????????????????????????????????????????????????
		Long msgId = form.getMsgId();
		Long shopId = getShopId();
		String userId = getUserId();
		int isForever = form.getIsForever();
		String mediaId = "";
		List<ShopWxNewsItemWithBLOBs> newsItemList = form.getNewsItemList();
		if(newsItemList!=null) {
			for (ShopWxNewsItemWithBLOBs bloBs : newsItemList) {
				// ????????????????????????????????????
				String strCon = StringUtil.getImgSrc(bloBs.getMcontent(), token, webUploadPath, fileUrlKeyWord);
				// ???????????????content??????Mcontentwechat
				bloBs.setMcontentwechat(strCon);
				bloBs.setShopId(getShopId());
				bloBs.setModifiedId(getUserId());
				bloBs.setMsgId(msgId);
			}
		}
		
		try {
			// ??????News?????????head???iteam,?????????????????????item??????Mcontentwechat
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SHOP_ID", shopId);
			map.put("MSG_ID", msgId);
			map.put("MSG_NM", null);
			map.put("USER_ID", userId);
			map.put("newsItemList", newsItemList);
			materialService.modifyNewsMaterial(map);

			// ??????msgId???shopid???????????????news???????????????media????????????
			List<Map<String, Object>> newsList = shopWxNewService.selectNewsListByMsgId(msgId, shopId);
			// ???Clob????????????String
			ListUtil.convertClob2String(newsList);
			
			// ???????????????????????? start
			String openid = form.getOpenId();
			// massServcie.previewMassMessage(getShopId(), openid, newsList);
			if (isForever == 1) {
				 mediaId = massServcie.previewForeverMassMessageWithMediaId(getShopId(), openid, newsList);
			} else {
				 mediaId = massServcie.previewMassMessageWithMediaId(getShopId(), openid, newsList);
			}
			log.info("mediaId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mediaId);
			//???????????????????????? end
			
			// ???????????????????????????????????????????????????media id
			if(mediaId != null && !"".equals(mediaId)) {
				Map<String, Object> previeResultMap = new HashMap<String, Object>();
				previeResultMap.put("SHOP_ID",  shopId);
				previeResultMap.put("MSG_ID", msgId);
				map.put("WX_MSG_ERR", "preview success");
				map.put("WX_MSG_ID", mediaId);
				map.put("WX_MSG_DATA_ID", 0);
				massServcie.savePreviewResult(map);
			}

			return  new Echo<Integer>(1);
		}catch (Exception e){
			e.printStackTrace();
			return  new Echo<Integer>(0);
		}
	}
	
	/**
	 * @author Dong Xifu
	 * @Date 2018/4/17 ??????3:50
	 * @describe ??????????????????(???????????????news item)
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/delShopWxNewsItemByKey", method = RequestMethod.GET)
	public int delShopWxNewsItem(@RequestParam(value = "newsId") String newsId) {
		long shopId = getShopId();
		return shopWxNewService.delShopWxNewsItem(Long.valueOf(newsId), shopId);
	}

	/**
	 * ??????????????????(??????news head???????????????news item)
	 * 
	 * @author Dong Xifu
	 * @Date 2018/5/5 ??????2:13
	 * 
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value = "/delShopWxHead", method = RequestMethod.GET)
	public Echo<?> delShopWxHead(@RequestParam(value = "msgId") String msgId) {
		int result = shopWxNewService.delMsgId(Long.valueOf(msgId), getShopId());
		return new Echo<Integer>(result);
	}
	
	
	
	/************************************************************??????????????????????????? start*********************************************************************/
	/**
	 * @author Dong Xifu
	 * @Date 2018/5/9 ??????5:30
	 * @describe ??????????????????
	 * @param
	 * @return
	 */
	@PostMapping(value = "/sendNews",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> sendNews(@RequestBody SenNewsForm form){
		Integer sendType = form.getSendType();
		if (sendType== null || sendType == 0) {
			throw new BizException(ErrorCode.MSG_SEND_TYPE_NULL);
		}
		
		if (sendType == 3) {// ????????????????????????????????????
			if (form.getCustList() == null || form.getCustList().size() < 1) {
				throw new BizException(ErrorCode.CUST_SYSID_LIST_NULL);
			}
		} else if (sendType== 2) {//?????????????????????????????????
			if (form.getGradeId() == null) {
				throw new BizException(ErrorCode.GRADEID_NOT_NULL);
			}
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 1??????????????????????????????????????????shop_wx_news_cust???mysql???/wx_news_cust???oracle???
		form.setShopId(getShopId());
		shopWxNewService.sendNews(form);
		
		// 2????????????????????????openid
		/*List<String> touser = new ArrayList<String>();
		List<String> userlist = shopWxNewService.queryOpenIdWxNewsCust(form);
		if (userlist.isEmpty()) {
			throw new BizException(ErrorCode.SEND_USER_LSIT);
		}
		for (String item : userlist) {
			touser.add(item);
		}*/ //-v1
		boolean is_to_all = false;
		List<String> touser = new ArrayList<String>();
		// ??????????????????????????????TOUSER OPENID??????
		if(sendType == 2 || sendType == 3) {
			touser = shopWxNewService.queryOpenIdWxNewsCust(form);
			if (touser.isEmpty()) {
				throw new BizException(ErrorCode.SEND_USER_LSIT);
			}
		} else if(sendType == 1){
			is_to_all = true;
		}
		
		// 3???????????????????????????????????????????????????????????????
		try {
			List<Map<String, Object>> newsList = shopWxNewService.selectNewsListByMsgId(form.getMsgId(), getShopId());
			ListUtil.convertClob2String(newsList);
			// ???????????????????????????????????????????????????
			boolean checkImgSrc = this.checkImgSrc(newsList);
			if(checkImgSrc){
				// ??????????????????
				Map<String, Object> sendResultMap = massServcie.sendMassMessage(getShopId(), is_to_all, touser, newsList);
				
				// ????????????????????????????????????????????????????????????mediaReturn???????????????massReturn-start
				MediaReturn mediaReturn = null;
				
				MassReturn massReturn = null;
				
				if(sendResultMap.get("MediaReturn") != null) {
					mediaReturn = (MediaReturn) sendResultMap.get("MediaReturn");
				}
				
				if(sendResultMap.get("MassReturn") != null) {
					massReturn = (MassReturn) sendResultMap.get("MassReturn");
				}
	
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("SHOP_ID",  getShopId());
				
				map.put("MSG_ID", form.getMsgId());
				
				// map.put("WX_MSG_ERR", "send success");
				map.put("WX_MSG_ERR", "send success"+"msgId:["+ massReturn.getMsg_id() +"]");
				
				// map.put("WX_MSG_ID", massReturn.getMsg_id());
				if(mediaReturn != null) {// mediaReturn?????????????????????????????????wxMsgId??????????????????????????????wxMsgId
					String mediaId = mediaReturn.getMedia_id();
					if(!StringUtils.isEmpty(mediaId)) {
						map.put("WX_MSG_ID", mediaId);
					}
				}
				
				map.put("WX_MSG_DATA_ID", massReturn != null ?massReturn.getMsg_data_id() : null);
				
				map.put("SEND_STS", 1);
				
				// ??????
				massServcie.saveSendResult(map);
				// ????????????????????????????????????????????????????????????mediaReturn???????????????massReturn-end
				
				returnMap.put("success", true);
			}else {
				returnMap.put("success", false);
				returnMap.put("err_msg", "??????????????????????????????????????????????????????");
			}
		}catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SHOP_ID",  getShopId());
			map.put("MSG_ID", form.getMsgId());
			map.put("WX_MSG_ERR", e.getMessage());
			// map.put("WX_MSG_ID", null);// ?????????????????????????????????WX_MSG_ID??????????????????????????????????????????WX_MSG_ID???
			// map.put("WX_MSG_DATA_ID", 0);// ?????????????????????????????????WX_MSG_DATA_ID??????????????????????????????????????????WX_MSG_DATA_ID???????????????????????????
			map.put("SEND_STS", 2);
		 
			massServcie.saveSendResult(map);
		 
			String em = e.getMessage();
			if (em.contains("45028") || em.contains("has no masssend quota hint")) {
				// em = "[45028]You has no masssend quota!";
				em = "You has no masssend quota!";
			}
			returnMap.put("success", false);
			returnMap.put("err_msg", em);
		}
		return new Echo<Map<String, Object>>(returnMap);
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * @param newsList
	 * @return
	 */
	private boolean checkImgSrc(List<Map<String, Object>> newsList){
		boolean flag = true;
		Pattern p=Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
		Pattern p_src = Pattern.compile("( src| SRC)=(\"|\')(.*?)(\"|\')");

		for (int i = 0; i < newsList.size(); i++) {
			Matcher m=p.matcher(String.valueOf(newsList.get(i).get("MCONTENTWECHAT")));
			boolean result_img=m.find();
			// ???????????????????????????????????????
			if (result_img) {
				while (result_img) {
					String str_img= m.group(2);
					Matcher m_src = p_src.matcher(str_img);
					if (m_src.find()) {
						if(!m_src.group(3).trim().startsWith("http://mmbiz.")){//????????????????????????????????????
							logger.info("???????????????URL = " + m_src.group(3).trim());
							flag = false;
							break;
						}
						result_img=m.find();
					}
				}
			} else {
				// ?????????????????????
				flag = true;
			}
		}

		return flag;
	}
	/************************************************************??????????????????????????? end*********************************************************************/
	
	/************************************************************?????????????????????????????????????????????????????? start***************************************************/
	@PostMapping(value = "/massSend",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> massSend(@RequestBody SenNewsForm form){
		// ?????????????????? 1-?????????????????? 2-????????? 3-????????????
		Integer sendType = form.getSendType();
		if (sendType== null || sendType == 0) {
			throw new BizException(ErrorCode.MSG_SEND_TYPE_NULL);
		}
		
		if (sendType == 3) {// ??????????????????????????????
			if (form.getCustList() == null || form.getCustList().size() < 1) {
				throw new BizException(ErrorCode.CUST_SYSID_LIST_NULL);
			}
		} else if (sendType== 2) {//???????????????????????????
			if (form.getGradeId() == null) {
				throw new BizException(ErrorCode.GRADEID_NOT_NULL);
			}
		}
		
		// ??????????????????
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// TODO 1??????????????????????????????
		
		// 2????????????????????????OPENID
		boolean is_to_all = false;
		List<String> touser = null;
		form.setShopId(getShopId());
		// ??????????????????????????????TOUSER OPENID??????
		if(sendType == 2 || sendType == 3) {
			touser = shopWxNewService.queryCustOpenIdList(form);
			if (touser == null || touser.isEmpty()) {
				throw new BizException(ErrorCode.SEND_USER_LSIT);
			}
		} else if(sendType == 1){
			is_to_all = true;
		}
		
		// 3???????????????:?????????????????????????????????
		try {
			Map<String, Object> sendResultMap = massServcie.massSend(getShopId(), is_to_all, touser, form);
			
			// ????????????????????????????????????????????????????????????mediaReturn???????????????massReturn-start
			MassReturn massReturn = null;
			if(sendResultMap.get("MassReturn") != null) {
				massReturn = (MassReturn) sendResultMap.get("MassReturn");
			}
			
			// TODO ??????????????????
			returnMap.put("success", true);
			returnMap.put("MassReturn", massReturn);
			return new Echo<Map<String, Object>>(returnMap);
		}catch (Exception e) {
			// TODO ????????????????????????
			
			String em = e.getMessage();
			if (em.contains("45028") || em.contains("has no masssend quota hint")) {
				// em = "[45028]You has no masssend quota!";
				em = "You has no masssend quota!";
			}
			returnMap.put("success", false);
			returnMap.put("err_msg", em);
			return new Echo<Map<String, Object>>(returnMap);
		}
	}
	/************************************************************?????????????????????????????????????????????????????? end***************************************************/

	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * 
	 * @param page
	 * @param rows
	 * @param msgIdWould ??????1??????????????????????????????????????????????????????????????????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/findWxNewsItemAll", method = RequestMethod.GET)
	public ApiJson<List<?>> findWxNewsItemAll(@RequestParam(value = "page", defaultValue = "1") String page,
										   @RequestParam(value = "rows", defaultValue = "20") String rows,
										   @RequestParam(value = "msgIdWould", defaultValue = "0",required = false) String msgIdWould) {
		ShopWxNewsQueryParam queryParam = new ShopWxNewsQueryParam();
		queryParam.setShopId(getShopId());
		queryParam.setPage(Integer.valueOf(page));
		queryParam.setRows(Integer.valueOf(rows));
		queryParam.setMsgIdWould(msgIdWould);
		ApiJson<List<?>> result = new ApiJson<>();
		try {
			PageInfo<ShopWxNewDto> pageInfo = shopWxNewService.matterFindAll(queryParam);
			for (ShopWxNewDto dto : pageInfo.getList()) {
				ShopWxNewDto dtoChild = shopWxNewService.getShopWxNewsItem(dto.getMsgId(), getShopId());
				dto.setDto(dtoChild.getDto());
				dto.setShopWxNewsItemChild(dtoChild.getShopWxNewsItemChild());
			}
			if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
				result.setPage(pageInfo.getPageNum());
				result.setRecords((int) pageInfo.getTotal());
				result.setTotal(pageInfo.getPages());
				result.setRows(pageInfo.getList().stream().map(data -> new ShopWxNewsItemVo(data)).collect(Collectors.toList()));
			} else {
				result.setPage(1);
				result.setRecords(0);
				result.setTotal(0);
				result.setRows(new ArrayList<Object>());
			}
			result.setErrorCode(0);
			result.setErrorMsg("Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/************************************************************?????????????????????***************************************************/
	/**
	 * ????????????????????????list(??????)
	 * 
	 * @param startTime
	 * @param endTime
	 * @return com.basoft.api.vo.ApiJson<java.util.List>
	 */
	@RequestMapping(value = "/newsStatsDataList", method = RequestMethod.GET)
	public ApiJson<List<?>> newsStatsDataList(@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime) {
		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		
		ApiJson<List<?>> result = new ApiJson<>();
		try {
			List<NewsStatsDataDto> dataDtoList = shopWxNewService.messageStatsDataList(getShopId(), startTime, endTime);
			if (dataDtoList != null && CollectionUtils.isNotEmpty(dataDtoList)) {
				result.setRows(dataDtoList.stream().map(data -> new NewsStatsDataVo(data)).collect(Collectors.toList()));
				result.setRecords(dataDtoList.size());
			} else {
				result.setRows(new ArrayList<Object>());
				result.setRecords(0);
			}
			result.setErrorCode(0);
			result.setErrorMsg("Success");
		} catch (Exception e) {
			result.setErrorCode(1);
			result.setErrorMsg("Error");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @describe ????????????????????????????????????
	 * @param  [startTime, endTime]
	 * @return com.basoft.core.web.vo.Echo<?>
	 * @author Dong Xifu
	 * @date 2018/5/16 ??????9:47
	 */
	@RequestMapping(value = "/newsStatsDataSum", method = RequestMethod.GET)
	public ApiJson<?> newsStatsDataSum(){
        ApiJson<List> result = new ApiJson<>();

        List<WxIFMsgStasYesSumVo> list = new ArrayList<>();

        //??????????????????
        NewsStatsDataDto detailSumYes = shopWxNewService.messageStatsDataSum(getShopId(),"-1");
        //????????????
        NewsStatsDataDto detailSumBeforYes = shopWxNewService.messageStatsDataSum(getShopId(),"-2");

        //??????????????????
        NewsStatsDataDto detailSumWeek = shopWxNewService.messageStatsDataSum(getShopId(),"7");

        //??????????????????
        NewsStatsDataDto detailSumLastWeek = shopWxNewService.messageStatsDataSum(getShopId(),"14");


        int   scale   =   2;//????????????
        int   roundingMode   =   4;//???????????????????????????????????????????????????????????????????????????.

        /********?????????????????????*******start*****/
        float intPageIncre = (float)(detailSumYes.getIntPageReadCount()-detailSumBeforYes.getIntPageReadCount())/(detailSumBeforYes.getIntPageReadCount()==0?1:detailSumBeforYes.getIntPageReadCount())*100;
        BigDecimal bdIntPage  =  new   BigDecimal((double)intPageIncre);
        bdIntPage  =  bdIntPage.setScale(scale,roundingMode);
        intPageIncre   =   bdIntPage.floatValue();

        float oriPageIncre = (float)(detailSumYes.getOriPageReadCount()-detailSumBeforYes.getOriPageReadCount())/(detailSumBeforYes.getOriPageReadCount()==0?1:detailSumBeforYes.getOriPageReadCount())*100;
        BigDecimal bdOri  =  new   BigDecimal((double)oriPageIncre);
        bdOri  =  bdOri.setScale(scale,roundingMode);
        oriPageIncre   =   bdOri.floatValue();


        float shareCountIncre = (float)(detailSumYes.getShareCount()-detailSumBeforYes.getShareCount())/(detailSumBeforYes.getShareCount()==0?1:detailSumBeforYes.getShareCount())*100;
        BigDecimal shareCt  =  new  BigDecimal((double)shareCountIncre);
        shareCt  =  shareCt.setScale(scale,roundingMode);
        shareCountIncre   =   shareCt.floatValue();

        float favCountIncre = (float)(detailSumYes.getAddToFavUser()-detailSumBeforYes.getAddToFavUser())/(detailSumBeforYes.getAddToFavUser()==0?1:detailSumBeforYes.getAddToFavUser())*100;
        BigDecimal favCt  =  new  BigDecimal((double)favCountIncre);
        favCt  =  favCt.setScale(scale,roundingMode);
        favCountIncre   =   favCt.floatValue();
        /********?????????????????????********end**/

        /********?????????????????????*******start*****/
        float intPageIncreWeek = (float)(detailSumWeek.getIntPageReadCount()-detailSumLastWeek.getIntPageReadCount())/(detailSumLastWeek.getIntPageReadCount()==0?1:detailSumLastWeek.getIntPageReadCount())*100;
        BigDecimal bdIntPageWeek  =  new   BigDecimal((double)intPageIncre);
        bdIntPageWeek  =  bdIntPageWeek.setScale(scale,roundingMode);
        intPageIncreWeek   =   bdIntPageWeek.floatValue();

        float oriPageIncreWeek = (float)(detailSumWeek.getOriPageReadCount()-detailSumLastWeek.getOriPageReadCount())/(detailSumLastWeek.getOriPageReadCount()==0?1:detailSumLastWeek.getOriPageReadCount())*100;
        BigDecimal bdOriWeek  =  new   BigDecimal((double)oriPageIncreWeek);
        bdOriWeek  =  bdOriWeek.setScale(scale,roundingMode);
        oriPageIncreWeek   =   bdOriWeek.floatValue();


        float shareCountIncreWeek = (float)(detailSumWeek.getShareCount()-detailSumLastWeek.getShareCount())/(detailSumLastWeek.getShareCount()==0?1:detailSumLastWeek.getShareCount())*100;
        BigDecimal shareCtWeek  =  new  BigDecimal((double)shareCountIncreWeek);
        shareCtWeek  =  shareCtWeek.setScale(scale,roundingMode);
        shareCountIncreWeek   =   shareCtWeek.floatValue();

        float favCountIncreWeek = (float)(detailSumWeek.getAddToFavUser()-detailSumLastWeek.getAddToFavUser())/(detailSumLastWeek.getAddToFavUser()==0?1:detailSumLastWeek.getAddToFavUser())*100;
        BigDecimal favCtWeek  =  new  BigDecimal((double)favCountIncreWeek);
        favCtWeek  =  favCtWeek.setScale(scale,roundingMode);
        favCountIncreWeek   =   favCtWeek.floatValue();
        /********?????????????????????********end**/

        WxIFMsgStasYesSumVo e = new WxIFMsgStasYesSumVo();
        e.setName("?????????????????????");
        e.setValue(detailSumYes.getIntPageReadCount());
        e.setDayVariety(String.valueOf(intPageIncre)+"%");//???????????????
        e.setWeekVariety(String.valueOf(intPageIncreWeek)+"%" );//???????????????
        e.setMonthVariety("--");//???????????????
        list.add(e);
        WxIFMsgStasYesSumVo e1 = new WxIFMsgStasYesSumVo();
        e1.setName("??????????????????");
        e1.setValue(detailSumYes.getOriPageReadCount());
        e1.setDayVariety(String.valueOf(oriPageIncre)+"%");
        e1.setWeekVariety(String.valueOf(oriPageIncreWeek)+"%" );
        e1.setMonthVariety("--");
        list.add(e1);
        WxIFMsgStasYesSumVo e2 = new WxIFMsgStasYesSumVo();
        e2.setName("??????????????????");
        e2.setValue(detailSumYes.getShareCount());
        e2.setDayVariety(String.valueOf(shareCountIncre)+"%");
        e2.setWeekVariety(String.valueOf(shareCountIncreWeek)+"%");
        e2.setMonthVariety("--");
        list.add(e2);
        WxIFMsgStasYesSumVo e3 = new WxIFMsgStasYesSumVo();
        e3.setName("??????????????????");
        e3.setValue(detailSumYes.getAddToFavUser());
        e3.setDayVariety(String.valueOf(favCountIncre)+"%");
        e3.setWeekVariety(String.valueOf(favCountIncreWeek)+"%");
        e3.setMonthVariety("--");
        list.add(e3);
        result.setRows(list);
        return result;
	}

	/**
	 * @describe ??????????????????????????????
	 * @param  [startTime,endTime]
	 * @return com.basoft.api.vo.ApiJson<java.util.List>
	 * @author Dong Xifu
	 * @date 2018/5/16 ??????2:24
	 */
    @RequestMapping(value = "/msgStatsDataListGroupDay", method = RequestMethod.GET)
    public ApiJson<List> msgStatsDataListGroupDay(@RequestParam(value = "startTime")String startTime,
                                            @RequestParam(value = "endTime")String endTime){
        if(StringUtils.isBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        if(StringUtils.isBlank(endTime)&&StringUtils.isNotBlank(startTime)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }

        ApiJson<List> result = new ApiJson<>();
        try{
            List<NewsStatsDataDto> dtoList =  shopWxNewService.msgStatsDataListGroupDay(getShopId(),startTime,endTime);
        if (dtoList != null && CollectionUtils.isNotEmpty(dtoList)){
            result.setRecords(dtoList.size());
            result.setRows(dtoList);
        }else{
            result.setRecords(0);
            result.setRows(new ArrayList());
        }
            result.setErrorCode(0);
            result.setErrorMsg("Success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * @describe //????????????????????????????????? ????????????????????? ?????????????????? ?????????(?????????????????????)?????????????????????
     * @param  [startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author Dong Xifu
     * @date 2018/5/20 ??????4:59
     */
    @RequestMapping(value = "/wxIfMessageStatsDetailChart", method = RequestMethod.GET)
    public ApiJson<List> wxIfMessageStatsDetailChart(@RequestParam(value = "startTime") String startTime,
                                                     @RequestParam(value = "endTime") String endTime,
                                                     @RequestParam(value = "timeType",defaultValue = "0",required = false) Byte timeType) {
        if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        if (StringUtils.isBlank(endTime) && StringUtils.isNotBlank(startTime)) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }
        ApiJson<List> result = new ApiJson<>();
		WxMessageQueryParam queryParam = new WxMessageQueryParam();
		queryParam.setShopId(getShopId());
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		queryParam.setTimeType(timeType);
        try {
            List<WxIfMessageStatsDetail> list = shopWxNewService.wxIfMessageStatsDetail(queryParam);
            ArrayList<String> dataAll = new ArrayList<>();
            if(timeType==0){
                 dataAll = DateTimeUtil.findDataAll(startTime, endTime,1);//???????????????????????????????????????(???)
			}else if(timeType==1){
                 dataAll = DateTimeUtil.findHourAll();//?????????????????????????????????????????????
            }
            list = setMsgStatisList(dataAll, list,timeType);//??????????????????????????????
            if (list != null && list.size() > 0) {
                result.setRows(list.stream().map(data -> new WxIfMessageStatsDetailVo(data)).collect(Collectors.toList()));
                result.setRecords(list.size());
            } else {
                result.setRows(new ArrayList());
                result.setRecords(0);
            }
            result.setErrorCode(0);
            result.setErrorMsg("Success");
        } catch (Exception e) {
            result.setErrorCode(1);
            result.setErrorMsg("fail");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @describe ??????setMsgStatisList
     * @param  [dataAll, list]
     * @return java.util.List<com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail>
     * @author Dong Xifu
     * @date 2018/5/20 ??????6:04
     */
    private List<WxIfMessageStatsDetail> setMsgStatisList(ArrayList<String> dataAll, List<WxIfMessageStatsDetail> list,Byte timeType) {
        for (String str:dataAll) {
            WxIfMessageStatsDetail entity = new WxIfMessageStatsDetail();
            boolean flag = true;
                for(int i=0;i<list.size();i++){
                    if(timeType==0) {        //???
                        if (str.equals(list.get(i).getStatDate())) {
                            flag = false;
                        }

                    }else if(timeType==1){   //??????
                        if (str.equals(list.get(i).getRefHour())) {
                            flag = false;
                        }
                    }
                }

            if(flag){
                entity = setMsgStatisEntity(entity,str);
                list.add(entity);
            }
        }

        if(timeType==1){
            list = setHour(list);
        }

		//??????
        Collections.sort(list,(o1, o2) -> {
			int res = 0;
			if (0==timeType) {
				res = o1.getStatDate().compareTo(o2.getStatDate());
			} else {
				res = o1.getRefHour().compareTo(o2.getRefHour());
			}
			if (res > 0) {
				return 1;
			} else if (res == 0) {
				return 0;
			}
			return -1;
		});



        return list;
    }

    /**
     * @param  [list]
     * @return java.util.List<com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail>
     * @describe ????????????
     * @author Dong Xifu
     * @date 2018/5/23 ??????1:14
     */
    private List<WxIfMessageStatsDetail> setHour(List<WxIfMessageStatsDetail> list) {
        for (WxIfMessageStatsDetail detail : list) {
            if("000".equals(detail.getRefHour())){
                detail.setRefHour("00:00");
            }else if("100".equals(detail.getRefHour())){
                detail.setRefHour("01:00");
            }else if("200".equals(detail.getRefHour())){
                detail.setRefHour("02:00");
            }else if("300".equals(detail.getRefHour())){
                detail.setRefHour("03:00");
            }else if("400".equals(detail.getRefHour())){
                detail.setRefHour("04:00");
            }else if("500".equals(detail.getRefHour())){
                detail.setRefHour("05:00");
            }else if("600".equals(detail.getRefHour())){
                detail.setRefHour("06:00");
            }else if("700".equals(detail.getRefHour())){
                detail.setRefHour("07:00");
            }else if("800".equals(detail.getRefHour())){
                detail.setRefHour("08:00");
            }else if("900".equals(detail.getRefHour())){
                detail.setRefHour("09:00");
            }else if("1000".equals(detail.getRefHour())){
                detail.setRefHour("10:00");
            }else if("1100".equals(detail.getRefHour())){
                detail.setRefHour("11:00");
            }else if("1200".equals(detail.getRefHour())){
                detail.setRefHour("12:00");
            }else if("1300".equals(detail.getRefHour())){
                detail.setRefHour("13:00");
            }else if("1400".equals(detail.getRefHour())){
                detail.setRefHour("14:00");
            }else if("1500".equals(detail.getRefHour())){
                detail.setRefHour("15:00");
            }else if("1600".equals(detail.getRefHour())){
                detail.setRefHour("16:00");
            }else if("1700".equals(detail.getRefHour())){
                detail.setRefHour("17:00");
            }else if("1800".equals(detail.getRefHour())){
                detail.setRefHour("18:00");
            }else if("1900".equals(detail.getRefHour())){
                detail.setRefHour("19:00");
            }else if("2000".equals(detail.getRefHour())){
                detail.setRefHour("20:00");
            }else if("2100".equals(detail.getRefHour())){
                detail.setRefHour("21:00");
            }else if("2200".equals(detail.getRefHour())){
                detail.setRefHour("22:00");
            }else if("2300".equals(detail.getRefHour())){
                detail.setRefHour("23:00");
            }
        }
        return list;
    }

    /**
     * @describe ?????? WxIfMessageStatsDetail
     * @param  [entity, date]
     * @return com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail
     * @author Dong Xifu
     * @date 2018/5/21 ??????10:14
     */
    private WxIfMessageStatsDetail setMsgStatisEntity(WxIfMessageStatsDetail entity,String date) {
        entity.setStatDate(date);

        entity.setRefHour(date);

        entity.setIntPageReadUser(0);//?????????????????????????????????????????????????????????????????????

        entity.setIntPageReadCount(0);//????????????????????????

        entity.setOriPageReadUser(0);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????0

        entity.setOriPageReadCount(0);//????????????????????????

        entity.setIntPageFromSessionReadUser(0);//???????????????????????????

        entity.setIntPageFromSessionReadCount(0);//???????????????????????????

        entity.setIntPageFromFeedReadUser(0);//?????????????????????

        entity.setIntPageFromFeedReadCount(0);//?????????????????????

        entity.setIntPageFromFriendsReadUser(0);//????????????????????????

        entity.setIntPageFromFriendsReadCount(0);//????????????????????????

        entity.setIntPageFromOtherReadUser(0);// ????????????????????????

        entity.setIntPageFromOtherReadCount(0);//????????????????????????

        entity.setAddToFavUser(0);//????????????

        entity.setAddToFavCount(0);//????????????
        entity.setShareUser(0);//????????????
        entity.setShareCount(0);//????????????
        return entity;
    }

    /**
     * @describe //????????????????????????????????? ????????????????????? ?????????????????? ?????????(?????????????????????)??????
     * @param  [startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author Dong Xifu
     * @date 2018/5/21 ??????10:11
     */
    @RequestMapping(value = "/wxIfMessageStatsDetailTable", method = RequestMethod.GET)
    public ApiJson<List> wxIfMessageStatsDetailTable(@RequestParam(value = "startTime")String startTime,
                                                     @RequestParam(value = "endTime")String endTime,
                                                     @RequestParam(value = "timeType",defaultValue = "0",required = false)Byte timeType,
													 @RequestParam(value = "page",defaultValue = "1" )String page,
													 @RequestParam(value = "rows",defaultValue = "20" )String rows) {
        if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        if (StringUtils.isBlank(endTime) && StringUtils.isNotBlank(startTime)) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }
        ApiJson<List> result = new ApiJson<>();
		WxMessageQueryParam queryParam = new WxMessageQueryParam();
		queryParam.setShopId(getShopId());
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		queryParam.setTimeType(timeType);
        try {
            List<WxIfMessageStatsDetail> list = shopWxNewService.wxIfMessageStatsDetail(queryParam);

			if(timeType==1)
            	list = setHour(list);
            if (list != null && list.size() > 0) {
                result.setRows(list.stream().map(data->new WxIfMessageStatsDetailVo(data)).collect(Collectors.toList()));
                result.setRecords(list.size());
            } else {
                result.setRows(new ArrayList());
                result.setRecords(0);
            }
            result.setErrorCode(0);
            result.setErrorMsg("Success");
        } catch (Exception e) {
            result.setErrorCode(1);
            result.setErrorMsg("fail");
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * ??????????????????
	 * 
	 * @param startTime
	 * @param endTime
	 * @param timeType
	 * @return com.basoft.core.web.vo.Echo<?>
	 * @author Dong Xifu
	 * @date 2018/5/21 ??????10:39
	 */
	@RequestMapping(value = "/wxIfMessageStatsSum", method = RequestMethod.GET)
	public Echo<?> wxIfMessageStatsSum(@RequestParam(value = "startTime") String startTime,
									@RequestParam(value = "endTime") String endTime,
									@RequestParam(value = "timeType", defaultValue = "0", required = false) String timeType) {
		/*if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		if (StringUtils.isBlank(endTime) && StringUtils.isNotBlank(startTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}*/
		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
			endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		}
		
		WxIfMessageStatsDetail detailSum = shopWxNewService.wxIfMessageStatsSum(getShopId(), startTime, endTime, timeType);
		return new Echo<>(new WxIfMessageStatsDetailVo(detailSum));
	}
}
