package com.basoft.eorder.interfaces.controller.manager;

import cn.hutool.json.JSONObject;
import com.basoft.eorder.application.*;
import com.basoft.eorder.application.base.query.AdminQueryFacade;
import com.basoft.eorder.application.base.query.CategoryDTO;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
//import com.basoft.eorder.batch.job.EOrderSendEmailJob;
import com.basoft.eorder.domain.StoreOptionRepository;
import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.controller.h5.common.ExportExcelController;
import com.basoft.eorder.interfaces.query.*;
import com.basoft.eorder.interfaces.query.excel.StoreSettleDtlExcel;
import com.basoft.eorder.interfaces.query.excel.StoreSettleExcel;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.ExcelUtils;
import com.basoft.eorder.util.MailUtil;
import com.basoft.eorder.util.SetParamUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * @author BA
 */
@Slf4j
@Controller
@RequestMapping("/manager/api/v1")
@ResponseBody
public class SpringManagerController extends CQRSAbstractController {


	@Autowired
	private LoginService loginService;

	@Autowired
	private  AppConfigure appConfigure;

	private CategoryQuery categoryQuery;

	private AdminQueryFacade aqf;

	private StoreOptionRepository storeOptionRepository;

	@Autowired
	private StoreQuery storeQuery;
	@Autowired
	private InventoryHotelQuery inventoryHotelQuery;
	@Autowired
	private OrderQuery orderQuery;
	@Autowired
	private SettleQuery sq;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ExportExcelController excelController;
	@Autowired
	private static ConfigurableApplicationContext context;

	public SpringManagerController() {
	}

	@Autowired
	public SpringManagerController(QueryHandler queryHandler, CommandHandleEngine handleEngine, CategoryQuery categoryQuery, AdminQueryFacade queryFacade, StoreOptionRepository storeOptionRepository) {
		super(queryHandler, handleEngine);
		this.categoryQuery = categoryQuery;
		this.aqf = queryFacade;
		this.storeOptionRepository = storeOptionRepository;
	}

	@Override
	protected Object newQueryHandlerContext(HttpServletRequest request) {
		UserSession us = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		Map<String, Object> context = new HashMap<>();
		context.put(AppConfigure.BASOFT_USER_SESSION_PROP, us);
		return context;
	}

	@Override
	protected Map<String, Object> newCommandHandlerContextMap(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		Map<String, Object> props = new HashMap<>();
		props.put(AppConfigure.BASOFT_USER_SESSION_PROP, userSession);
		return props;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String getDefault() {
		return "welcome basoft";
	}

	@GetMapping("/category")
	@ResponseBody
	public Object queryCategoryExec(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		Long storeCode = userSession.getStoreId();
		CategoryDTO categoryDTO = aqf.getStoreCategory(storeCode);return categoryDTO.getChildren();
	}

	/**
	 * manager??????option
	 *
	 * @return java.lang.Object
	 * @author Dong Xifu
	 * @date 2019/7/5 ??????10:40
	 */
	@GetMapping("/option")
	@ResponseBody
	public Object queryOptionExec(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		final OptionDTO rootOption = aqf.getRootOption(String.valueOf(userSession.getStoreType()));
		return rootOption.getChildren();
	}

	@GetMapping("/storeOption")
	@ResponseBody
	public Object queryStoreOption(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		//final StoreOptionDTO rootOption = storeOptionRepository.getOptionDto(userSession.getStoreId());
		return storeOptionRepository.getOptionDto(userSession.getStoreId(),userSession.getStoreType());
	}

	@GetMapping("/userInfo")
	@ResponseBody
	public UserSession currentUser(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		if (userSession == null) {
			logger.info("??????????????????????????????");
			throw new BizException(ErrorCode.LOGIN_INVALID);
		}
		return userSession;
	}

	/**
	 * CMS MANAGER
	 * manager CMS????????????/manager APP????????????/SA???CA?????????APP????????????
	 *
	 * @param jsonObject
	 * @param request
	 * @param res
	 *
	 * @return com.basoft.eorder.domain.model.User
	 * @author Dong Xifu
	 * @since Date 2018/12/7 ??????1:14
	 */
	@PostMapping("/login")
	public void login(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse res) {
		String account = jsonObject.get("account").toString();
		String password = jsonObject.get("password").toString();
		log.info("account>>>>" + account);
		log.info("password>>>>" + password);
		// ??????????????????
		if (account == null || "".equals(account.trim()) || password == null || "".equals(password.trim())) {
			throw new BizException(ErrorCode.LOGIN_INVALID);
		}

		UserSession user = loginService.login(account);
		if (user == null) {
			throw new BizException(ErrorCode.LOGIN_USER_NULL);
		}

		// ???????????????????????????-start
		/*if (user.getStoreId() == null || user.getStoreId() == 0) {
			throw new BizException(ErrorCode.STORE_NULL_USER);
		}*/
		// 4?????????????????????????????????????????????
		if("3".equals(user.getAccountType())
				|| "5".equals(user.getBizType())
				// SA?????????
				|| (user.getAccountRole() != 0 && String.valueOf(user.getAccountRole()).startsWith("3"))
				// CA?????????
				|| (user.getAccountRole() != 0 && String.valueOf(user.getAccountRole()).startsWith("4"))){
			// ??????????????????????????????ID
			log.info("&&&&&&&&&&&&&&&&&&?????????????????????***************");
		}
		// ??????????????????
		else {
			if (user.getStoreId() == null || user.getStoreId() == 0) {
				throw new BizException(ErrorCode.STORE_NULL_USER);
			}
		}
		// ???????????????????????????-end

		if (user != null) {
			// ??????BCrypt?????????????????????????????????
			if (!BCrypt.checkpw(password, user.getPassword())) {
				throw new BizException(ErrorCode.LOGIN_ERROR);
			}
		}

		Cookie cookie = new Cookie("token", user.getToken());
		cookie.setPath("/");
		cookie.setMaxAge(60 * 5 * 60 * 10);

		res.addCookie(cookie);

		Map<String, String> respMap = new HashMap<>();
		respMap.put("token", user.getToken());
		respMap.put("loginUserName", user.getName());
		respMap.put("nextUrl", "/index.html");
		// ????????????
		//respMap.put("at", String.valueOf(user.getAccountType()));
		// ????????????
		//respMap.put("ar", String.valueOf(user.getAccountRole()));

		try {
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json");
			res.getWriter().write(this.toJson(respMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * ????????????excel
	 *
	 * @Param
	 * @return void
	 * @author Dong Xifu
	 * @date 2019/8/26 ??????6:20
	 */
	@RequestMapping(value = "/exportExcelOrder", method = RequestMethod.GET)
	public void exportExcel(HttpServletResponse response,HttpServletRequest request
		,@RequestParam(name="language")String language
		,@RequestParam(name="filName")String filName
		,@RequestParam(name="page",defaultValue = "1")int page
		,@RequestParam(name="size",defaultValue = "20")int size
		)  throws IOException {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		if (userSession == null) {
			logger.info("??????????????????????????????");
			throw new BizException(ErrorCode.LOGIN_INVALID);
		}

		Map<String, Object> param = SetParamUtil.setBaseParam(userSession.getStoreId(), page, size,new HashMap<>());
		List<OrderDTO> orderDTOList = orderQuery.getOrderListByMap(param);
		ExcelUtils.writeExcel(response,null,null, orderDTOList, OrderDTO.class,filName,language);
	}

	/**
	 * ??????manager????????????excel
	 *
	 * @Param
	 * @return void
	 * @author Dong Xifu
	 * @date 2019/8/26 ??????6:20
	 */
	@RequestMapping(value = "/storeSettlesExcel", method = RequestMethod.GET)
	public void storeSettles(HttpServletResponse response,HttpServletRequest request
		,@RequestParam(name="storeId")Long storeId
		,@RequestParam(name="startTime")String startTime
		,@RequestParam(name="endTime")String endTime
		,@RequestParam(name="language")String language
		,@RequestParam(name="page",defaultValue = "1")int page
		,@RequestParam(name="size",defaultValue = "2000000")int size
	) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
		Map<String, Object> param = SetParamUtil.setTimeParam(startTime,endTime,true,new HashMap<>());
		SetParamUtil.setBaseParam(storeId, page, size,param);
		param.put("currentTime",startTime);
		log.info("startTime="+String.valueOf(param.get("startTime")));
		log.info("endTime="+String.valueOf(param.get("endTime")));

		List<SettleDTO> settleList = sq.getStoreSettleList(param);
		List<StoreSettleExcel> storeSettleList = new LinkedList<>();
		for (SettleDTO dto : settleList) {
			StoreSettleExcel sse = new StoreSettleExcel();
			BeanUtils.copyProperties(sse, dto);
			storeSettleList.add(sse);
		}
		List<String> topList = excelController.getAdminSettleTopList(language,true,param);
		String month = String.valueOf(param.get("month"));
		StoreDTO storeDTO = storeQuery.getStoreById(storeId);
		String fileName = ExcelUtils.getFileName(language, storeDTO.getName()+"_"+month+"_",ExcelUtils.STORESETTLES_TYPE);
		String title = ExcelUtils.getTitle(language,month,"("+storeDTO.getName()+")",ExcelUtils.STORESETTLES_TYPE);
		ExcelUtils.writeExcel(response,title,topList,storeSettleList,StoreSettleExcel.class,fileName,language);
	}

	/**
	 * ??????manager??????????????????excel
	 *
	 * @Param
	 * @return void
	 * @author Dong Xifu
	 * @date 2019/8/26 ??????6:20
	 */
	@RequestMapping(value = "/storeDtlSettlesExcel", method = RequestMethod.GET)
	public void storeDtlSettles(HttpServletResponse response,HttpServletRequest request
		,@RequestParam(name="storeId")Long storeId
		,@RequestParam(name="startTime")String startTime
		,@RequestParam(name="endTime")String endTime
		,@RequestParam(name="language")String language
		,@RequestParam(name="page",defaultValue = "1")int page
		,@RequestParam(name="size",defaultValue = "2000000")int size
	) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
		Map<String, Object> param = SetParamUtil.setTimeParam(startTime,endTime,false,new HashMap<>());
		SetParamUtil.setBaseParam(storeId, page, size,param);
		param.put("currentTime",startTime);

		List<SettleDTO> settleDtoList = sq.getSettleDtlListByMap(param);
		List<StoreSettleDtlExcel> dtlList = new LinkedList<>();
		for (SettleDTO dto : settleDtoList) {
			StoreSettleDtlExcel sse = new StoreSettleDtlExcel();
			BeanUtils.copyProperties(sse, dto);
			dtlList.add(sse);
		}
		List<String> topList = excelController.getAdminSettleTopList(language,true,param);
		String month = String.valueOf(param.get("month"));
		StoreDTO storeDTO = storeQuery.getStoreById(storeId);
		String fileName = ExcelUtils.getFileName(language, storeDTO.getName()+"_"+month+"_",ExcelUtils.STORESETTLES_TYPE);
		String title = ExcelUtils.getTitle(language,month,"("+storeDTO.getName()+")",ExcelUtils.STORESETTLES_TYPE);

		ExcelUtils.writeExcel(response,title,topList,dtlList,StoreSettleDtlExcel.class,fileName,language);
	}

	/**
	 * ????????????
	 *
	 * @return void
	 * @Param
	 * @author Dong Xifu
	 * @date 2019/8/26 ??????6:20
	 */
	@RequestMapping(value = "/senEmail", method = RequestMethod.POST)
	public void senEmail(@RequestBody JSONObject jsonObject) throws Exception {
		String account = jsonObject.get("account").toString();
		String content = jsonObject.get("content").toString();
		String subject = jsonObject.get("subject").toString();

		MailUtil util = new MailUtil();
		util.sendMailTest(account, subject, content, appConfigure);
	}

	/**
	 * ????????????????????????
	 *
	 * @param request
	 * @param jsonObject
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendStoreSettleEmail", method = RequestMethod.POST)
	public void sendStoreSettleEmail(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws Exception {
		UserSession us = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		String account = jsonObject.get("account").toString();
		int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "yyyy"));
		int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "MM"));
//		Map<String, Object> param = EOrderSendEmailJob.getQueryParam();
//		param.put("storeId", us.getStoreId());
//		List<SettleDTO> settleList = sq.getAdminSettleList(param);
//		String emailTile = lastYear + "-" + lastMonth + " BA Place ????????? ????????????";

//		MailUtil util = new MailUtil();
//		if (settleList.size() > 0)
//			util.sendMail(account, emailTile, util.storeSettlehtml(settleList.get(0)), appConfigure);
	}


	/**
	 * CMS MANAGER
	 * ??????????????????????????????????????????????????????
	 *
	 * @param request
	 * @param res
	 *
	 * @return com.basoft.eorder.domain.model.User
	 */
	@GetMapping("/loginAuthInfo")
	@ResponseBody
	public Map<String, String> loginAuthInfo(HttpServletRequest request, HttpServletResponse res) {
		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		if (userSession == null) {
			throw new BizException(ErrorCode.SYS_TIMEOUT);
		}

		Map<String, String> respMap = new HashMap<>();
		// ????????????
		respMap.put("at", String.valueOf(userSession.getAccountType()));
		// ????????????
		respMap.put("ar", String.valueOf(userSession.getAccountRole()));

		return respMap;
	}

	/**
	 * MANAGER-??????????????????|??????????????????
	 *
	 * @param request
	 * @param type         ???????????? 1-?????? 2-?????? 3-?????? 4-??????
	 * @param functionType ????????????????????? 2-???????????? 3-????????????
	 * @param manageType ??????????????? 1-Admin CMS 2-Manager CMS 3-All CMS
	 * @return
	 */
	@GetMapping("/managertags")
	@ResponseBody
	public Object queryCategoryExec(HttpServletRequest request,
									@RequestParam(value = "type", defaultValue = "") String type,
									@RequestParam(value = "functionType", defaultValue = "") String functionType,
									@RequestParam(value = "manageType", defaultValue = "") String manageType) {
		return aqf.getTags(type, functionType, manageType);
	}

	/**
	 * CMS MANAGER FROM ADMIN CMS
	 * Admin?????????????????????Manager CMS
	 *
	 * @param jsonObject
	 * @param request
	 * @param res
	 *
	 * @return com.basoft.eorder.domain.model.User
	 * @author Dong Xifu
	 * @since Date 2018/12/7 ??????1:14
	 */
	@PostMapping("/singleLogin")
	public void singleLogin(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse res) {
		String storeId = jsonObject.get("storeId").toString();
		String managerId = jsonObject.get("managerId").toString();
		String adminAccessToken = jsonObject.get("adminAccessToken").toString();

		log.info("Admin CMS login to Manager CMS:::Parameter Value::::" + storeId + "||" + managerId + "||" + adminAccessToken);

		// ????????????????????????
		if (storeId == null || "".equals(storeId.trim())
				|| managerId == null || "".equals(managerId.trim())
				|| adminAccessToken == null || "".equals(adminAccessToken.trim())) {
			throw new BizException(ErrorCode.LOGIN_INVALID);
		}

		// ??????adminAccessToken?????????
		boolean isInvalid = loginService.checkAdminAccessToken(adminAccessToken);
		if (!isInvalid) {
			throw new BizException(ErrorCode.LOGIN_INVALID);
		}
		log.info("Admin CMS login to Manager CMS:::isInvalid::::" + isInvalid);


		// ??????storeId???managerId????????????
		boolean isMatching = loginService.checkStoreManager(storeId, managerId);
		if (!isMatching) {
			throw new BizException(ErrorCode.LOGIN_INVALID);
		}
		log.info("Admin CMS login to Manager CMS:::isMatching::::" + isMatching);

		UserSession user = loginService.loginManagerCMSByManagerId(managerId);
		if (user == null) {
			throw new BizException(ErrorCode.LOGIN_USER_NULL);
		}

		if (user.getStoreId() == null || user.getStoreId() == 0) {
			throw new BizException(ErrorCode.STORE_NULL_USER);
		}

		Cookie cookie = new Cookie("token", user.getToken());
		cookie.setPath("/");
		cookie.setMaxAge(60 * 5 * 60 * 10);

		res.addCookie(cookie);

		Map<String, String> respMap = new HashMap<>();
		respMap.put("token", user.getToken());
		respMap.put("nextUrl", "/index.html");

		try {
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json");
			res.getWriter().write(this.toJson(respMap));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * CMS MANAGER????????????
	 * ????????????
	 *
	 * @param jsonObject
	 * @param request
	 * @param res
	 */
	@PostMapping("/modp")
	public Map<String, String> modifyPassword(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse res) {
		String oldPassword = jsonObject.get("oop").toString();
		String newPassword = jsonObject.get("nep").toString();
		log.info("oldPassword>>>>" + oldPassword);
		log.info("newPassword>>>>" + newPassword);
		// ??????????????????
		if (oldPassword == null || "".equals(oldPassword.trim()) || newPassword == null || "".equals(newPassword.trim())) {
			throw new BizException(ErrorCode.PARAM_INVALID);
		}

		UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
		if (userSession == null) {
			throw new BizException(ErrorCode.SYS_TIMEOUT);
		}

		User user = userRepository.getUserByAccount(userSession.getAccount());
		log.info("???????????????Account???>>>" + userSession.getAccount());
		log.info("?????????????????????Account?????????????????????>>>" + user);
		if (user == null) {
			throw new BizException(ErrorCode.LOGIN_USER_NULL);
		} else {
			Map<String, String> respMap = new HashMap<>();
			// ??????BCrypt??????????????????????????????????????????
			if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
				// ???????????????
				respMap.put("isSucess", "0");
				respMap.put("operCode", "1001");
				respMap.put("operCodeDesc", "?????????????????????????????????");
				return respMap;
			} else {
				int i = userRepository.updateUser(user.getAccount(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));
				if (i >= 1) {
					respMap.put("isSucess", "1");
					respMap.put("operCode", "1002");
					respMap.put("operCodeDesc", "?????????????????????");
				} else {
					respMap.put("isSucess", "0");
					respMap.put("operCode", "1003");
					respMap.put("operCodeDesc", "?????????????????????");
				}
				return respMap;
			}
		}
	}
}