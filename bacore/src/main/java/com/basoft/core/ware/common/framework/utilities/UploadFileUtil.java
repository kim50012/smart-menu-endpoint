package com.basoft.core.ware.common.framework.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class UploadFileUtil {
	public static final String ROOT = "/uploads";

	public static final String UPLOAD_FILE = "file";

	public static final String UPLOAD_GOODS = "goods";

	public static final String UPLOAD_SHOP = "shop";

	public static final String UPLOAD_AUTH = "auth";

	public static final String UPLOAD_LOGO = "logo";

	public static final String UPLOAD_WEIXIN = "weixin";

	public static final String UPLOAD_USER = "user";

	public static final String UPLOAD_PROMOTION = "promotion";

	public static final String UPLOAD_BLOG = "blog";

	public static final String UPLOAD_TEST = "test";

	public static final String UPLOAD_MATERIAL = "material";

	public static final String UPLOAD_QRCODE = "qrcode";

	public static final String UPLOAD_TMEP = "temp";

	private static String getRequestPath(HttpServletRequest request, String uploadType) {
		String path = "";
		if (UPLOAD_LOGO.equals(uploadType)) {
			path = ROOT + "/" + UPLOAD_LOGO + generateFilePath();
		} else if (UPLOAD_WEIXIN.equals(uploadType)) {
			SessionSkin sessionSkin = (SessionSkin) request.getSession().getAttribute(SessionUtils.SESSION_SKIN);
			if (sessionSkin != null) {
				path = ROOT + "/" + UPLOAD_WEIXIN + "/" + sessionSkin.getShopId() + generateFilePath();
			} else {
				path = ROOT + "/" + UPLOAD_WEIXIN + generateFilePath();
			}
		} else if (UPLOAD_MATERIAL.equals(uploadType)) {
			path = ROOT + "/" + UPLOAD_MATERIAL + generateFilePath();
		} else if (UPLOAD_QRCODE.equals(uploadType)) {
			String shopId = (String) request.getSession().getAttribute(SessionUtils.BACK_SHOP_ID_SESSION);
			if (StringUtils.isBlank(shopId)) {
				shopId = "0";
			}
			path = ROOT + "/" + UPLOAD_QRCODE + "/" + shopId + generateFilePath();
		} else {
			String shopId = (String) request.getSession().getAttribute(SessionUtils.BACK_SHOP_ID_SESSION);
			if (StringUtils.isBlank(shopId)) {
				shopId = "0";
			}
			path = ROOT + "/" + shopId + generateFilePath();
		}
		return path;
	}

	public static String getUploadUrl(HttpServletRequest request, String uploadType) {
		return getRequestPath(request, uploadType);
	}

	public static String getUploadPath(HttpServletRequest request, String uploadType) {
		String path = getRequestPath(request, uploadType);
		String filePath = request.getSession().getServletContext().getRealPath(path);
		log.info("filePath=====" + filePath);
		File target = new File(filePath);
		if (!target.exists()) {
			target.mkdirs();
		}
		return filePath;
	}

	/**
	 * 文件上传
	 * 
	 * @param request HttpServletRequest
	 * @param originalFile File 文件
	 * @param originalFilename String 原文件名
	 * @param uploadType String 上传类型
	 * @return
	 */
	public static UploadResult upload(HttpServletRequest request, File originalFile, String originalFilename, String uploadType) {
		UploadResult result = new UploadResult();
		String shopId = String.valueOf(request.getSession().getAttribute(SessionUtils.BACK_SHOP_ID_SESSION));
		String path = getRequestPath(request, uploadType);
		String filePath = request.getSession().getServletContext().getRealPath(path);
		File target = new File(filePath);
		if (!target.exists()) {
			target.mkdirs();
		}
		String destinationFilename = generateSystemFilename(shopId, originalFilename, uploadType);
		target = new File(filePath, destinationFilename);
		log.info("filePath===============" + filePath);
		log.info("path===================" + path);
		log.info("destinationFilename====" + destinationFilename);
		try {
			FileUtils.copyFile(originalFile, target);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.setRealPath(filePath + File.separator + destinationFilename);
		result.setOriginalFilename(originalFilename);
		result.setDestinationFilename(destinationFilename);
		result.setFileUrl(path + destinationFilename);
		result.setFileExt(getFileExt(originalFilename));
		result.setFileSize(originalFile.length());
		if (getSuffix(originalFilename).equalsIgnoreCase("png") || getSuffix(originalFilename).equalsIgnoreCase("jpg") || getSuffix(originalFilename).equalsIgnoreCase("jpeg")
				|| getSuffix(originalFilename).equalsIgnoreCase("gif")) {
			try {
				BufferedImage sourceImg = ImageIO.read(new FileInputStream(originalFile));
				result.setWidth(sourceImg.getWidth());
				result.setHeight(sourceImg.getHeight());
				sourceImg = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		originalFile = null;
		target = null;
		return result;
	}

	public static String generateFilePath() {
		return new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
	}

	public static final String generateSystemFilename(String shopId, String original, String str) {
		SimpleDateFormat sf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss_");
		String formatDate = sf.format(new Date());
		String random = generateNumberRandom(5);
		String ext = getFileExt(original);
		if (StringUtils.isBlank(shopId)) {
			shopId = "0";
		}
		return shopId + "_" + str + formatDate + random + getFileExt(original);
	}

	public static final String generateSystemFilename(String original, String str) {
		SimpleDateFormat sf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss_");
		String formatDate = sf.format(new Date());
		String random = generateNumberRandom(5);
		String ext = getFileExt(original);
		return str + formatDate + random + getFileExt(original);
	}

	public static String generateNumberRandom(int length) {
		int max = new Double(Math.pow(10, length)).intValue();
		int min = new Double(Math.pow(10, length - 1)).intValue();
		Random r = new Random();
		int xx = r.nextInt(max);
		while (xx < min) {
			xx = r.nextInt(max);
		}
		return String.valueOf(xx);
	}

	public static String getFileExt(String filename) {
		int position = filename.lastIndexOf(".");
		if (position < 0) {
			return "";
		}
		return filename.substring(position, filename.length());
	}

	public static String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		return suffix;
	}

	public static String getMimeType(File file) {
		String mimetype = "";
		if (file.exists()) {
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
				mimetype = "image/png";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpg")) {
				mimetype = "image/jpg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpeg")) {
				mimetype = "image/jpeg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("gif")) {
				mimetype = "image/gif";
			} else {
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		return mimetype;
	}

	public static void main(String[] args) {
		System.out.println(generateSystemFilename("你好.jpg", UPLOAD_SHOP));
	}
}
