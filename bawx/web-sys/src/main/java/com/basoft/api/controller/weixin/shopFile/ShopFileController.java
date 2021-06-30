package com.basoft.api.controller.weixin.shopFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.wechat.shopFile.ShopFileVO;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.shop.ShopFileService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.dto.shop.ShopFileDto;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.param.shopFile.ShopFileQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:31 2018/4/10
 **/
@RestController
public class ShopFileController extends BaseController {
	@Autowired
	private IdService idService;

	@Autowired
	private ShopFileService shopFileService;

    /**
     * 图片列表查询
     * 
     * @param page
     * @param rows
     * @param fileType
     * @param fileName
     * @param msgIdWould 值为1则只查询已同步到微信公众平台的图片，否则查询所有图文消息
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/shopFileFindAll", method = RequestMethod.GET)
    public ApiJson<List> shopFileFindAll(@RequestParam(value = "page",defaultValue = "1") String page,
                                         @RequestParam(value = "rows",defaultValue = "20") String rows,
                                         @RequestParam(value = "fileType",defaultValue = "1") Byte fileType,
                                         @RequestParam(value = "fileName",defaultValue = "") String fileName,
										 @RequestParam(value = "msgIdWould",defaultValue = "0",required = false) String msgIdWould
                                         ){
		ApiJson<List> result = new ApiJson<>();
		try {
			ShopFileQueryParam param = new ShopFileQueryParam();
			param.setPage(Integer.parseInt(page));
			param.setRows(Integer.parseInt(rows));
			param.setFileType(fileType);
			param.setShopId(getShopId());
			param.setIsUse(BizConstants.MENU_STATE_ENABLE);
			param.setFileName(fileName);
			param.setMsgIdWould(msgIdWould);
			PageInfo<ShopFileDto> pageInfo = shopFileService.findAllByParam(param);
			if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
				result.setPage(pageInfo.getPageNum());
				result.setRecords((int) pageInfo.getTotal());
				result.setTotal(pageInfo.getPages());
				result.setRows(pageInfo.getList().stream().map(data -> new ShopFileVO(data)).collect(Collectors.toList()));
			} else {
				result.setPage(1);
				result.setRecords(0);
				result.setTotal(0);
				result.setRows(new ArrayList());
			}
			result.setErrorCode(0);
			result.setErrorMsg("Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
	/**
	 * 文件插入
	 * 
	 * @param entity
	 * @return
	 */
	/*@PostMapping(value = "/insertShopFile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int insertShopFile(@RequestBody ShopFile entity) {
		entity.setFileId(idService.generateShopWxFile());
		entity.setShopId(getShopId());
		entity.setCreatedDt(new Date());
		entity.setCreatedId(getUserId());
		return shopFileService.insertShopFile(entity);
	}*/
    // 文件上传时即入库，不需要此方法

	/**
	 * 修改文件名称
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping(value = "/updateShopFile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> updateShopFile(@RequestBody ShopFile entity) {
		entity.setShopId(getShopId());
		if (StringUtils.isBlank(entity.getFileNm()))
			throw new BizException(ErrorCode.PARAM_MISSING);
		entity.setModifiedId(getUserId());
		int result = shopFileService.updateShopFile(entity);
		return new Echo<Integer>(result);
	}


	/**
	 * @author Dong Xifu
	 * @Date 2018/4/26 下午1:31
	 * @describe 删除文件
	 * @param
	 * @return 9999-有图片占用不允许删除，1-删除成功
	 **/
	@RequestMapping(value = "/deleteShopFile", method = RequestMethod.GET)
	public Echo<?> deleteShopFile(@RequestParam(value = "fileId") String fileId) {
		if (StringUtils.isBlank(fileId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		int result = shopFileService.deleteShopFile(getShopId(), Long.valueOf(fileId));
		return new Echo<Integer>(result);
	}
}
