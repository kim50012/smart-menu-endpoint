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
 * @Description:图文消息
 * 
 * @Author:DongXifu
 * @Date Created in 上午11:49 2018/4/16
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
	 * 素材管理查询
	 * 
	 * @author Dong Xifu
	 * @Date 2018/4/16 上午11:54
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
	 * 插入图文消息头
	 * 
	 * @author Dong Xifu
	 * @Date 2018/4/19 下午3:35
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
     *@Date 2018/4/16 下午2:38
     *@describe 添加图文消息
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
	 * 以list形式插入素材
	 * 
	 * @author Dong Xifu
	 * @Date 2018/5/7 上午10:02
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
	 * 封装 ShopWxNewsHead
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
     *@Date 2018/4/16 下午1:07
     *@describe 添加或修改素材内容（废弃）
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
        boolean headFlag = false;//是否为首页
        return shopWxNewService.insertOrUpdateShopNewsItem(form, headFlag, edit);
    }
	
	/**
	 * 查询单个素材详情 newsId(查询单个的news item)
	 * 
	 * @param
	 * @return
	 * 
	 * @author Dong Xifu
	 * @Date 2018/4/19 下午4:09
	 */
    @Deprecated
	@RequestMapping(value = "/getShopWxNewsItemOne", method = RequestMethod.GET)
	public ShopWxNewsItemDetailVo getShopWxNewsItemOne(@RequestParam(value = "msgId") String msgId, @RequestParam(value = "newsId") String newsId) {
		ShopWxNewsItemWithBLOBs shopWxNewsItemOne = shopWxNewService.getShopWxNewsItemOne(getShopId(), Long.valueOf(msgId), Long.valueOf(newsId));
		return new ShopWxNewsItemDetailVo(shopWxNewsItemOne);
	}

	/**
	 * @describe 查询素材/文件详情（包含news head和所有的news item）
	 *
	 * @author Dong Xifu
	 * @Date 2018/4/17 上午9:31
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getShopWxNewsItem/{msgId}", method = RequestMethod.GET)
	public Echo<?> getShopWxNewsItem(@PathVariable String msgId, @RequestParam(value = "menuMsgType", defaultValue = "1") String menuMsgType) {
		ShopWxNewDto dto = new ShopWxNewDto();
		Long shopId = getShopId();
		// 图文消息
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
	
	/************************************************************以下为图文消息预览*********************************************************************/
	/**
	 * 根据公众号id查询具有预览图文消息权限的user列表
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
	 * 预览图文消息
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/previewNews",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> previewNews(@RequestBody PreviewNewsForm form){
		AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(getShopId());
		String token = wechatService.getAccessToken(appInfo);
		// String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
		
		// 处理图文消息的内容，包括图片、视频、音频
		Long msgId = form.getMsgId();
		Long shopId = getShopId();
		String userId = getUserId();
		int isForever = form.getIsForever();
		String mediaId = "";
		List<ShopWxNewsItemWithBLOBs> newsItemList = form.getNewsItemList();
		if(newsItemList!=null) {
			for (ShopWxNewsItemWithBLOBs bloBs : newsItemList) {
				// 上传图文消息内容中的图片
				String strCon = StringUtil.getImgSrc(bloBs.getMcontent(), token, webUploadPath, fileUrlKeyWord);
				// 将转化后的content存入Mcontentwechat
				bloBs.setMcontentwechat(strCon);
				bloBs.setShopId(getShopId());
				bloBs.setModifiedId(getUserId());
				bloBs.setMsgId(msgId);
			}
		}
		
		try {
			// 更新News，包括head和iteam,当然主要是更新item中的Mcontentwechat
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SHOP_ID", shopId);
			map.put("MSG_ID", msgId);
			map.put("MSG_NM", null);
			map.put("USER_ID", userId);
			map.put("newsItemList", newsItemList);
			materialService.modifyNewsMaterial(map);

			// 根据msgId和shopid获取最新的news，包括微信media等信息。
			List<Map<String, Object>> newsList = shopWxNewService.selectNewsListByMsgId(msgId, shopId);
			// 将Clob类型转为String
			ListUtil.convertClob2String(newsList);
			
			// 此处调用预览接口 start
			String openid = form.getOpenId();
			// massServcie.previewMassMessage(getShopId(), openid, newsList);
			if (isForever == 1) {
				 mediaId = massServcie.previewForeverMassMessageWithMediaId(getShopId(), openid, newsList);
			} else {
				 mediaId = massServcie.previewMassMessageWithMediaId(getShopId(), openid, newsList);
			}
			log.info("mediaId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mediaId);
			//此处调用预览接口 end
			
			// 预览后保存预览状态，包含预览返回得media id
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
	 * @Date 2018/4/17 下午3:50
	 * @describe 删除单个素材(删除单个的news item)
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/delShopWxNewsItemByKey", method = RequestMethod.GET)
	public int delShopWxNewsItem(@RequestParam(value = "newsId") String newsId) {
		long shopId = getShopId();
		return shopWxNewService.delShopWxNewsItem(Long.valueOf(newsId), shopId);
	}

	/**
	 * 删除整个素材(包含news head和其所有的news item)
	 * 
	 * @author Dong Xifu
	 * @Date 2018/5/5 下午2:13
	 * 
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value = "/delShopWxHead", method = RequestMethod.GET)
	public Echo<?> delShopWxHead(@RequestParam(value = "msgId") String msgId) {
		int result = shopWxNewService.delMsgId(Long.valueOf(msgId), getShopId());
		return new Echo<Integer>(result);
	}
	
	
	
	/************************************************************以下为图文消息群发 start*********************************************************************/
	/**
	 * @author Dong Xifu
	 * @Date 2018/5/9 下午5:30
	 * @describe 发送图文消息
	 * @param
	 * @return
	 */
	@PostMapping(value = "/sendNews",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> sendNews(@RequestBody SenNewsForm form){
		Integer sendType = form.getSendType();
		if (sendType== null || sendType == 0) {
			throw new BizException(ErrorCode.MSG_SEND_TYPE_NULL);
		}
		
		if (sendType == 3) {// 按选择的用户发送图文消息
			if (form.getCustList() == null || form.getCustList().size() < 1) {
				throw new BizException(ErrorCode.CUST_SYSID_LIST_NULL);
			}
		} else if (sendType== 2) {//按用户级别发送图文消息
			if (form.getGradeId() == null) {
				throw new BizException(ErrorCode.GRADEID_NOT_NULL);
			}
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 1、保存图文消息和发送用户到表shop_wx_news_cust【mysql】/wx_news_cust【oracle】
		form.setShopId(getShopId());
		shopWxNewService.sendNews(form);
		
		// 2、封装发送用户的openid
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
		// 全部发送，不需要查询TOUSER OPENID列表
		if(sendType == 2 || sendType == 3) {
			touser = shopWxNewService.queryOpenIdWxNewsCust(form);
			if (touser.isEmpty()) {
				throw new BizException(ErrorCode.SEND_USER_LSIT);
			}
		} else if(sendType == 1){
			is_to_all = true;
		}
		
		// 3、查询图文消息、校验图文信息、发送图文信息
		try {
			List<Map<String, Object>> newsList = shopWxNewService.selectNewsListByMsgId(form.getMsgId(), getShopId());
			ListUtil.convertClob2String(newsList);
			// 校验是否是已上传到微信服务器的图片
			boolean checkImgSrc = this.checkImgSrc(newsList);
			if(checkImgSrc){
				// 发送图文消息
				Map<String, Object> sendResultMap = massServcie.sendMassMessage(getShopId(), is_to_all, touser, newsList);
				
				// 对发送结果处理：发送结果包括图文上传结果mediaReturn和发送结果massReturn-start
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
				if(mediaReturn != null) {// mediaReturn为空，则是直接发送说明wxMsgId已存在，则不需要更新wxMsgId
					String mediaId = mediaReturn.getMedia_id();
					if(!StringUtils.isEmpty(mediaId)) {
						map.put("WX_MSG_ID", mediaId);
					}
				}
				
				map.put("WX_MSG_DATA_ID", massReturn != null ?massReturn.getMsg_data_id() : null);
				
				map.put("SEND_STS", 1);
				
				// 保存
				massServcie.saveSendResult(map);
				// 对发送结果处理：发送结果包括图文上传结果mediaReturn和发送结果massReturn-end
				
				returnMap.put("success", true);
			}else {
				returnMap.put("success", false);
				returnMap.put("err_msg", "有未上传的图片，请先发送预览然后再试");
			}
		}catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SHOP_ID",  getShopId());
			map.put("MSG_ID", form.getMsgId());
			map.put("WX_MSG_ERR", e.getMessage());
			// map.put("WX_MSG_ID", null);// 发送失败不更新，不清空WX_MSG_ID。不是不需要，是不能清空这个WX_MSG_ID。
			// map.put("WX_MSG_DATA_ID", 0);// 发送失败不更新，不清空WX_MSG_DATA_ID。不需要清空，没成功没有新的WX_MSG_DATA_ID返回，不需要更新。
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
	 * 校验是否是已上传到微信服务器的图片
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
			// 匹配到图片则检验图片的链接
			if (result_img) {
				while (result_img) {
					String str_img= m.group(2);
					Matcher m_src = p_src.matcher(str_img);
					if (m_src.find()) {
						if(!m_src.group(3).trim().startsWith("http://mmbiz.")){//此处判断图片是否来自微信
							logger.info("未上传图片URL = " + m_src.group(3).trim());
							flag = false;
							break;
						}
						result_img=m.find();
					}
				}
			} else {
				// 没有匹配到图片
				flag = true;
			}
		}

		return flag;
	}
	/************************************************************以下为图文消息群发 end*********************************************************************/
	
	/************************************************************以下为文本、图片、视频、音频消息群发 start***************************************************/
	@PostMapping(value = "/massSend",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> massSend(@RequestBody SenNewsForm form){
		// 目标发送类型 1-全部关注用户 2-按等级 3-选择用户
		Integer sendType = form.getSendType();
		if (sendType== null || sendType == 0) {
			throw new BizException(ErrorCode.MSG_SEND_TYPE_NULL);
		}
		
		if (sendType == 3) {// 按选择的用户发送消息
			if (form.getCustList() == null || form.getCustList().size() < 1) {
				throw new BizException(ErrorCode.CUST_SYSID_LIST_NULL);
			}
		} else if (sendType== 2) {//按用户级别发送消息
			if (form.getGradeId() == null) {
				throw new BizException(ErrorCode.GRADEID_NOT_NULL);
			}
		}
		
		// 定义返回结果
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// TODO 1、发送前记录发送内容
		
		// 2、封装发送用户的OPENID
		boolean is_to_all = false;
		List<String> touser = null;
		form.setShopId(getShopId());
		// 全部发送，不需要查询TOUSER OPENID列表
		if(sendType == 2 || sendType == 3) {
			touser = shopWxNewService.queryCustOpenIdList(form);
			if (touser == null || touser.isEmpty()) {
				throw new BizException(ErrorCode.SEND_USER_LSIT);
			}
		} else if(sendType == 1){
			is_to_all = true;
		}
		
		// 3、群发消息:图片、文本、音频、视频
		try {
			Map<String, Object> sendResultMap = massServcie.massSend(getShopId(), is_to_all, touser, form);
			
			// 对发送结果处理：发送结果包括图文上传结果mediaReturn和发送结果massReturn-start
			MassReturn massReturn = null;
			if(sendResultMap.get("MassReturn") != null) {
				massReturn = (MassReturn) sendResultMap.get("MassReturn");
			}
			
			// TODO 发送结果存储
			returnMap.put("success", true);
			returnMap.put("MassReturn", massReturn);
			return new Echo<Map<String, Object>>(returnMap);
		}catch (Exception e) {
			// TODO 发送失败结果存储
			
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
	/************************************************************以下为文本、图片、视频、音频消息群发 end***************************************************/

	/**
	 * 查询所有图文消息（供群发消息中选择使用或供微信自定义菜单、自动回复等使用）
	 * 
	 * @param page
	 * @param rows
	 * @param msgIdWould 值为1则只查询已同步到微信公众平台的图文消息，否则查询所有图文消息
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
	
	/************************************************************以下为图文统计***************************************************/
	/**
	 * 统计素材发送情况list(单篇)
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
	 * @describe 统计素材发送总数（昨日）
	 * @param  [startTime, endTime]
	 * @return com.basoft.core.web.vo.Echo<?>
	 * @author Dong Xifu
	 * @date 2018/5/16 上午9:47
	 */
	@RequestMapping(value = "/newsStatsDataSum", method = RequestMethod.GET)
	public ApiJson<?> newsStatsDataSum(){
        ApiJson<List> result = new ApiJson<>();

        List<WxIFMsgStasYesSumVo> list = new ArrayList<>();

        //昨日总阅读数
        NewsStatsDataDto detailSumYes = shopWxNewService.messageStatsDataSum(getShopId(),"-1");
        //前天总数
        NewsStatsDataDto detailSumBeforYes = shopWxNewService.messageStatsDataSum(getShopId(),"-2");

        //这周一至周日
        NewsStatsDataDto detailSumWeek = shopWxNewService.messageStatsDataSum(getShopId(),"7");

        //上周一至周日
        NewsStatsDataDto detailSumLastWeek = shopWxNewService.messageStatsDataSum(getShopId(),"14");


        int   scale   =   2;//设置位数
        int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.

        /********统计日增长数据*******start*****/
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
        /********统计日增长数据********end**/

        /********统计周增长数据*******start*****/
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
        /********统计周增长数据********end**/

        WxIFMsgStasYesSumVo e = new WxIFMsgStasYesSumVo();
        e.setName("图文总阅读次数");
        e.setValue(detailSumYes.getIntPageReadCount());
        e.setDayVariety(String.valueOf(intPageIncre)+"%");//日增长趋势
        e.setWeekVariety(String.valueOf(intPageIncreWeek)+"%" );//周增长趋势
        e.setMonthVariety("--");//月增长趋势
        list.add(e);
        WxIFMsgStasYesSumVo e1 = new WxIFMsgStasYesSumVo();
        e1.setName("原文阅读次数");
        e1.setValue(detailSumYes.getOriPageReadCount());
        e1.setDayVariety(String.valueOf(oriPageIncre)+"%");
        e1.setWeekVariety(String.valueOf(oriPageIncreWeek)+"%" );
        e1.setMonthVariety("--");
        list.add(e1);
        WxIFMsgStasYesSumVo e2 = new WxIFMsgStasYesSumVo();
        e2.setName("分享转发次数");
        e2.setValue(detailSumYes.getShareCount());
        e2.setDayVariety(String.valueOf(shareCountIncre)+"%");
        e2.setWeekVariety(String.valueOf(shareCountIncreWeek)+"%");
        e2.setMonthVariety("--");
        list.add(e2);
        WxIFMsgStasYesSumVo e3 = new WxIFMsgStasYesSumVo();
        e3.setName("微信收藏人数");
        e3.setValue(detailSumYes.getAddToFavUser());
        e3.setDayVariety(String.valueOf(favCountIncre)+"%");
        e3.setWeekVariety(String.valueOf(favCountIncreWeek)+"%");
        e3.setMonthVariety("--");
        list.add(e3);
        result.setRows(list);
        return result;
	}

	/**
	 * @describe 以天分组统计所有图文
	 * @param  [startTime,endTime]
	 * @return com.basoft.api.vo.ApiJson<java.util.List>
	 * @author Dong Xifu
	 * @date 2018/5/16 下午2:24
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
     * @describe //获取图文的原文阅读次数 朋友圈阅读次数 对话阅读次数 等等等(最多的那个统计)心电图，趋势图
     * @param  [startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author Dong Xifu
     * @date 2018/5/20 下午4:59
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
                 dataAll = DateTimeUtil.findDataAll(startTime, endTime,1);//查询两个日期之间的所有日期(天)
			}else if(timeType==1){
                 dataAll = DateTimeUtil.findHourAll();//查询一天之间的所有小时（小时）
            }
            list = setMsgStatisList(dataAll, list,timeType);//将空的日期设置默认值
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
     * @describe 排序setMsgStatisList
     * @param  [dataAll, list]
     * @return java.util.List<com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail>
     * @author Dong Xifu
     * @date 2018/5/20 下午6:04
     */
    private List<WxIfMessageStatsDetail> setMsgStatisList(ArrayList<String> dataAll, List<WxIfMessageStatsDetail> list,Byte timeType) {
        for (String str:dataAll) {
            WxIfMessageStatsDetail entity = new WxIfMessageStatsDetail();
            boolean flag = true;
                for(int i=0;i<list.size();i++){
                    if(timeType==0) {        //天
                        if (str.equals(list.get(i).getStatDate())) {
                            flag = false;
                        }

                    }else if(timeType==1){   //小时
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

		//排序
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
     * @describe 设置小时
     * @author Dong Xifu
     * @date 2018/5/23 下午1:14
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
     * @describe 封装 WxIfMessageStatsDetail
     * @param  [entity, date]
     * @return com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail
     * @author Dong Xifu
     * @date 2018/5/21 上午10:14
     */
    private WxIfMessageStatsDetail setMsgStatisEntity(WxIfMessageStatsDetail entity,String date) {
        entity.setStatDate(date);

        entity.setRefHour(date);

        entity.setIntPageReadUser(0);//图文页（点击群发图文卡片进入的页面）的阅读人数

        entity.setIntPageReadCount(0);//图文页的阅读次数

        entity.setOriPageReadUser(0);//原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0

        entity.setOriPageReadCount(0);//原文页的阅读次数

        entity.setIntPageFromSessionReadUser(0);//公众号会话阅读人数

        entity.setIntPageFromSessionReadCount(0);//公众号会话阅读次数

        entity.setIntPageFromFeedReadUser(0);//朋友圈阅读人数

        entity.setIntPageFromFeedReadCount(0);//朋友圈阅读次数

        entity.setIntPageFromFriendsReadUser(0);//好友转发阅读人数

        entity.setIntPageFromFriendsReadCount(0);//好友转发阅读次数

        entity.setIntPageFromOtherReadUser(0);// 其他场景阅读人数

        entity.setIntPageFromOtherReadCount(0);//其他场景阅读次数

        entity.setAddToFavUser(0);//收藏人数

        entity.setAddToFavCount(0);//收藏次数
        entity.setShareUser(0);//转发人数
        entity.setShareCount(0);//转发次数
        return entity;
    }

    /**
     * @describe //获取图文的原文阅读次数 朋友圈阅读次数 对话阅读次数 等等等(最多的那个统计)表格
     * @param  [startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author Dong Xifu
     * @date 2018/5/21 上午10:11
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
	 * 阅读来源分析
	 * 
	 * @param startTime
	 * @param endTime
	 * @param timeType
	 * @return com.basoft.core.web.vo.Echo<?>
	 * @author Dong Xifu
	 * @date 2018/5/21 上午10:39
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
