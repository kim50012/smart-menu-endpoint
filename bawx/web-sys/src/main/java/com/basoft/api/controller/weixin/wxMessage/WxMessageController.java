package com.basoft.api.controller.weixin.wxMessage;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.wechat.wxMessage.WxMessageVo;
import com.basoft.core.constants.CoreConstants;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.ApiJson;
import com.basoft.service.definition.wechat.wxMessage.WxMessageService;
import com.basoft.service.dto.wechat.WxMessageDto;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:24 2018/5/8
 **/
@RestController
public class WxMessageController extends BaseController{
    @Autowired
    private WxMessageService wxMessageService;
    
    /**
     * 分页查询客户咨询的消息
     * 
     * @param page
     * @param rows
     * @param startTime
     * @param endTime
     * @param param
     * @return
     */
	@RequestMapping(value = "/finCustChatContent", method = RequestMethod.GET)
	public ApiJson<List<WxMessageVo>> finCustChatContent(@RequestParam(value = "page", defaultValue = "1") String page,
														@RequestParam(value = "rows", defaultValue = "20") String rows,
														@RequestParam(value = "startTime") String startTime,
														@RequestParam(value = "endTime") String endTime,
														@RequestParam(value = "param", defaultValue = "") String param) {
        // 校验参数
		if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		if (StringUtils.isBlank(endTime) && StringUtils.isNotBlank(endTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
			endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		}
        
		// 封装查询条件
        WxMessageQueryParam queryParam = new WxMessageQueryParam();
        queryParam.setShopId(getShopId());
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		queryParam.setParam(param);
		queryParam.setPage(Integer.valueOf(page));
		queryParam.setRows(Integer.valueOf(rows));
		
		ApiJson<List<WxMessageVo>> result = new ApiJson<>();
		PageInfo<WxMessageDto> pageInfo = wxMessageService.finCustChatContent(queryParam);
		if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
			result.setPage(pageInfo.getPageNum());
			result.setRecords((int) pageInfo.getTotal());
			result.setTotal(pageInfo.getPages());
			result.setRows(pageInfo.getList().stream().map(data -> new WxMessageVo(data)).collect(Collectors.toList()));
		} else {
			result.setPage(1);
			result.setRecords(0);
			result.setTotal(0);
			result.setRows(new ArrayList<WxMessageVo>());
		}
		result.setErrorCode(0);
		result.setErrorMsg("Success");
		return result;
	}
}
