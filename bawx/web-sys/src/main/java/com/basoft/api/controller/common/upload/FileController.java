package com.basoft.api.controller.common.upload;


import com.basoft.api.controller.BaseController;
import com.basoft.api.security.TokenSession;
import com.basoft.api.utils.upload.UploadMultipartFileUtil;
import com.basoft.api.utils.upload.UploadResult;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.util.DateUtil;
import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.shop.ShopFileService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.shop.ShopFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * 上传文件（图片、视频、音频等）
 * 
 * @author basoft
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class FileController extends BaseController {
	@Autowired
	UploadMultipartFileUtil uploadMultipartFileUtil;
	
	@Autowired
	ShopFileService shopFileService;
	
	@Autowired
	IdService idService;
	
	@Autowired
	WechatService wechatService;

	/**
	 * 上传文件 fileUpload - fu
	 * 上传文件到存储路径，数据库表中不记录文件信息
	 * 
	 * @param file 必传
	 * @param uploadType 业务类型  必传
	 * @param fileType 文件类型 非必传
	 * @param fileGroup 是否同步微信平台 非必传
	 * @return
	 */
	@RequestMapping(value = "/fu", method = RequestMethod.POST)
	public Echo<?> fileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "uploadType",required = true) String uploadType,
			@RequestParam(value = "fileType", required = false) int fileType,
			@RequestParam(value = "fileGroup", required = false) Byte fileGroup) {
		TokenSession tokenSession = getTokenSession();
		String groupId = String.valueOf(tokenSession.getGroupId());
		String shopId = tokenSession.getShopId();
		try {
			UploadResult result = uploadMultipartFileUtil.upload(request, file, uploadType, "", groupId, shopId);
			// System.out.println(result);
			logger.info(result.toString());
		} catch (IllegalStateException | IOException e) {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL, e);
		}
		return new Echo<String>("OK");
	}
	
	/**
	 * 上传文件并保存 fileUploadSave - fus
	 * 上传文件到存储路径，并且数据库表中记录文件信息
	 * 
	 * @param file 必传
	 * @param uploadType 业务类型  必传
	 * @param fileType 文件类型 必传
	 * @param fileGroup 是否同步微信平台 非必传
	 * @param isUse 是否停用 非必传
	 * @return
	 */
	@RequestMapping(value = "/fus", method = RequestMethod.POST)
	public Echo<?> fileUploadAndSave(@RequestParam("file") MultipartFile file,
			@RequestParam("uploadType") String uploadType,
			@RequestParam(value = "fileType", required = true) Byte fileType,
			@RequestParam(value = "fileGroup", required = false) Byte fileGroup,
			@RequestParam(value = "isUse",defaultValue = "1", required = false) Byte isUse) {
		TokenSession tokenSession = getTokenSession();
		String groupId = String.valueOf(tokenSession.getGroupId());
		String shopId = tokenSession.getShopId();

		// 1.接受并处理上传文件
		UploadResult result = null;
		try {
			result = uploadMultipartFileUtil.upload(request, file, uploadType, "", groupId, shopId);
			logger.info(result.toString());
		} catch (IllegalStateException | IOException e) {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL, e);
		}

		// 2.存储文件信息到数据库
		if (!ObjectUtils.isEmpty(result)) {
			ShopFile shopFile = new ShopFile();
			fillShopFile(result, shopFile, fileType, fileGroup, tokenSession.getUserId(), groupId, shopId,isUse);

			int i = shopFileService.addShopFile(shopFile);
			if (i > 0) {
				return new Echo<ShopFile>(shopFile);
			} else {
				return new Echo<Integer>(0);
			}
		} else {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL);
		}
	}
	
	/**
	 * 上传文件并保存并同步到微信平台，但是以临时素材的方式
	 * fileUploadAndSaveAndSyncByTempMode - fusst
	 * 
	 * @param file 必传
	 * @param uploadType 业务类型  必传
	 * @param fileType 文件类型 必传
	 * @param fileGroup 是否同步微信平台 必传
	 * @param isUse 是否停用 非必传
	 * @return
	 */
	@RequestMapping(value = "/fusst", method = RequestMethod.POST)
	public Echo<?> fileUploadAndSaveAndSyncByTempMode(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "uploadType", required = true) String uploadType,
			@RequestParam(value = "fileType", required = true) Byte fileType,
			@RequestParam(value = "fileGroup", required = true) Byte fileGroup,
			@RequestParam(value = "isUse", defaultValue = "1",required = false) Byte isUse) {
		TokenSession tokenSession = getTokenSession();
		String groupId = String.valueOf(tokenSession.getGroupId());
		String shopId = tokenSession.getShopId();
		
		// 1.接受并处理上传文件
		UploadResult result = null;
		try {
			result = uploadMultipartFileUtil.upload(request, file, uploadType, "", groupId, shopId);
			logger.info(result.toString());
		} catch (IllegalStateException | IOException e) {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL, e);
		}
		
		if (!ObjectUtils.isEmpty(result)) {
			ShopFile shopFile = new ShopFile();
			fillShopFile(result, shopFile, fileType, fileGroup, tokenSession.getUserId(), groupId, shopId,isUse);

			// 2.同步到微信平台
			if (fileGroup == 2) {
				String token = wechatService.getAccessToken(Long.valueOf(shopId));
				String type = "";
				if (fileType==1) {
					type = "image";
				} else if (fileType==2) {
					type = "voice";
				} else if (fileType==3) {
					type = "video";
				} else if (fileType==4) {
					type = "thumb";
				}
				MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, type, result.getRealPath());
				shopFile.setMediaId(mediaReturn.getMedia_id());
				shopFile.setMediaUpDt(DateUtil.secondsToDate(mediaReturn.getCreated_at()));
			}
			
			// 3.存储文件信息到数据库
			int i = shopFileService.addShopFile(shopFile);
			if (i > 0) {
				return new Echo<ShopFile>(shopFile);
			} else {
				return new Echo<Integer>(0);
			}
		} else {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL);
		}
	}
	
	/**
	 * 上传文件并保存并同步到微信平台，并且是以永久素材的方式
	 * fileUploadAndSaveAndSyncByPermMode - fussp
	 * 
	 * @param file 必传
	 * @param uploadType 业务类型  必传
	 * @param fileType 文件类型 必传
	 * @param fileGroup 是否同步微信平台 必传
	 * @param viedioTitle 视频素材标题-视频素材是必传
	 * @param viedioIntroduction 视频素材描述-视频素材是必传
	 * @param isUse 是否停用 非必传
	 * @return
	 */
	@RequestMapping(value = "/fussp", method = RequestMethod.POST)
	public Echo<?> fileUploadAndSaveAndSyncByPermMode(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "uploadType", required = true) String uploadType,
			@RequestParam(value = "fileType", required = true) Byte fileType,
			@RequestParam(value = "fileGroup", required = true) Byte fileGroup,
			@RequestParam(value = "viedioTitle", required = false) String viedioTitle,
			@RequestParam(value = "viedioIntroduction", required = false) String viedioIntroduction,
			@RequestParam(value = "isUse", defaultValue = "1",required = false) Byte isUse) {
		TokenSession tokenSession = getTokenSession();
		String groupId = String.valueOf(tokenSession.getGroupId());
		String shopId = tokenSession.getShopId();
		
		// 0.文件校验
		// TODO
		
		// 1.接受并处理上传文件
		UploadResult result = null;
		try {
			result = uploadMultipartFileUtil.upload(request, file, uploadType, "", groupId, shopId);
			logger.info(result.toString());
		} catch (IllegalStateException | IOException e) {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL, e);
		}
		
		if (!ObjectUtils.isEmpty(result)) {
			ShopFile shopFile = new ShopFile();
			fillShopFile(result, shopFile, fileType, fileGroup, tokenSession.getUserId(), groupId, shopId,isUse);

			// 2.同步到微信平台
			if (fileGroup == 2) {
				String token = wechatService.getAccessToken(Long.valueOf(shopId));
				String type = "";
				if (fileType==1) {
					type = "image";
				} else if (fileType==2) {
					type = "voice";
				} else if (fileType==3) {
					type = "video";
				} else if (fileType==4) {
					type = "thumb";
				} else {
					type = "image";
				}
				
				if("video".equals(type)) {
					// 视频素材上传微信公众平台
					if(StringUtils.isEmpty(viedioTitle)) {
						viedioTitle = "Video";
					}
					if(StringUtils.isEmpty(viedioIntroduction)) {
						viedioIntroduction = "Video";
					}
					String mediaId = WeixinMediaUtils.addVideoMaterial(token, result.getRealPath(), viedioTitle, viedioIntroduction);
					shopFile.setMediaId(mediaId);
					shopFile.setMediaUpDt(new Date());
				} else {
					// 非视频素材上传微信公众平台
					String mediaId = WeixinMediaUtils.addMaterial(token, type, result.getRealPath());
					shopFile.setMediaId(mediaId);
					shopFile.setMediaUpDt(new Date());
					// 如果是图片素材微信返回值还有一个URL
					if("image".equals(type)) {
						// TODO
					}
				}
			}
			
			// 3.存储文件信息到数据库
			int i = shopFileService.addShopFile(shopFile);
			if (i > 0) {
				return new Echo<ShopFile>(shopFile);
			} else {
				return new Echo<Integer>(0);
			}
		} else {
			throw new BizException(ErrorCode.SYS_UPLOAD_FAIL);
		}
	}

	/**
	 * 封装ShopFile
	 * 
	 * @param result
	 * @param shopFile
	 * @param fileType 文件类型
	 * @param fileGroup 
	 * @param userId 用户
	 * @param groupId 公司id
	 * @param shopId 微信公众号id
	 */
	private void fillShopFile(UploadResult result, ShopFile shopFile, Byte fileType, Byte fileGroup, String userId,
			String groupId, String shopId,Byte isUse) {
		shopFile.setFileNm(result.getOriginalFilename());
		shopFile.setFileSize(result.getFileSize());
		shopFile.setFileSysNm(result.getDestinationFilename());
		shopFile.setFileType((byte) fileType);

		shopFile.setIsUse(isUse);
		shopFile.setFullUrl(result.getFileUrl());
		shopFile.setCreatedId(userId);
		shopFile.setModifiedId(userId);
		shopFile.setShopId(shopId == null ? 1 : Long.parseLong(shopId));
		if(fileGroup != null) {
			shopFile.setFileGroup((byte) fileGroup);
		}
		shopFile.setImgWidth(result.getWidth());
		shopFile.setImgHeight(result.getHeight());
		shopFile.setCreatedDt(new Date());
		if(StringUtils.isNotBlank(groupId)) {
			shopFile.setCompId(Long.parseLong(groupId));
		}
		shopFile.setFileId(idService.generateShopFile());
	}
}