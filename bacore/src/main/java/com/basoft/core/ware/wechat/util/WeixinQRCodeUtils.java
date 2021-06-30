package com.basoft.core.ware.wechat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.basoft.core.ware.wechat.domain.qrcode.QRTicketReturn;
import com.basoft.core.ware.wechat.domain.qrcode.ShortUrlReturn;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.sf.json.JSONObject;

/**
 * 账号管理接口工具类
 */
public class WeixinQRCodeUtils extends WeixinBaseUtils {
	private static final transient Log logger = LogFactory.getLog(WeixinQRCodeUtils.class);

	/**
	 * 创建二维码ticket接口
	 */
	private static final String QRCODE_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

	/**
	 * 通过ticket换取二维码接口
	 */
	private static final String SHOWQRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

	/**
	 * 长链接转短链接接口
	 */
	private static final String SHORTURL_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";

	public static class QRCodeSize {
		public static final int SIZE258 = 258;

		public static final int SIZE344 = 344;

		public static final int SIZE430 = 430;

		public static final int SIZE860 = 860;

		public static final int SIZE1280 = 1280;
	}

	/**
	 * 创建临时二维码ticket
	 * 注： 公众平台临时带参数二维码的有效期增加为30天 2015年12月02日跟新接口
	 * @param access_token
	 * @param scene_id
	 *            场景值ID，32位非0整型
	 * @param expire_seconds
	 *            该二维码有效时间，以秒为单位。 最大不超过604800（即7天）。   第1版本
	 *            该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。  第2版本 
	 * @return QRTicketReturn
	 */
	public static QRTicketReturn createQrcode(String access_token, Integer scene_id, Integer expire_seconds) {
		String url = getInterfaceUrl(QRCODE_CREATE_URL, access_token);
		// int expireSecondsLimit = 604800; //7天
		int expireSecondsLimit = 2592000; // 30天
		if (expire_seconds == null || expire_seconds < 0 || expire_seconds > expireSecondsLimit) {
			expire_seconds = expireSecondsLimit;
		}
		String postData = "{\"expire_seconds\": " + expire_seconds + ", \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + scene_id + "}}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		QRTicketReturn returns = new Gson().fromJson(result, QRTicketReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		String qrcodeUrl = returns.getQrcodeUrl();
		logger.info("==============qrcodeUrl================");
		logger.info(qrcodeUrl);
		logger.info("==============qrcodeUrl================");
		return returns;
	}

	/**
	 * 创建永久二维码ticket
	 * 
	 * @param access_token
	 * @param scene_id 场景值ID, 最大值为100000（目前参数只支持1--100000）
	 * @return QRTicketReturn
	 */
	public static QRTicketReturn createLimitQrcode(String access_token, int scene_id) {
		String url = getInterfaceUrl(QRCODE_CREATE_URL, access_token);
		String postData = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + scene_id + "}}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		QRTicketReturn returns = new Gson().fromJson(result, QRTicketReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 创建永久二维码ticket
	 * 
	 * @param access_token
	 * @param scene_str 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段
	 * @return QRTicketReturn
	 */
	public static QRTicketReturn createLimitQrcode(String access_token, String scene_str) {
		String url = getInterfaceUrl(QRCODE_CREATE_URL, access_token);
		String postData = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \" + scene_str + \"}}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		QRTicketReturn returns = new Gson().fromJson(result, QRTicketReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	public static String showQRCode(String ticket) {
		String url = "";
		try {
			ticket = URLEncoder.encode(ticket, "utf-8");
			url = SHOWQRCODE_URL.replaceAll("TICKET", ticket);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * 长链接转短链接接口
	 * 
	 * @param access_token 调用接口凭证
	 * @param longUrl 需要转换的长链接，支持http://、https://、weixin://wxpay 格式的url
	 * @return String short_url
	 */
	public static String toShortUrl(String access_token, String longUrl) {
		String url = getInterfaceUrl(SHORTURL_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("action", "long2short");
		jsonObject.put("long_url", longUrl);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		ShortUrlReturn returns = new Gson().fromJson(result, ShortUrlReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getShort_url();
	}

	/**
	 * 下载二维码
	 * 
	 * @param ticket String
	 * @param scene_id String
	 */
	public static String downloadQRCode(String ticket, String savePath, String scene_id) {
		String filePath = "";
		String filename = scene_id;
		try {
			ticket = URLEncoder.encode(ticket, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = SHOWQRCODE_URL.replaceAll("TICKET", ticket);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(url);
		logger.info("Executing Request: " + httpget.getRequestLine());
		try {
			CloseableHttpResponse response = httpClient.execute(httpget);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				Date expiresDate = new Date();
				Header expiresHeader = response.getFirstHeader("Expires");
				if (expiresHeader != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
					try {
						expiresDate = sdf.parse(expiresHeader.getValue());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.YEAR, 2200);
					expiresDate = calendar.getTime();
				}
				Header[] headers = response.getAllHeaders();
				logger.info("responseheader======================");
				for (Header header : headers) {
					logger.info(header.getName() + ": " + header.getValue());
				}
				logger.info("responseheader======================");
				logger.info("Expires data====" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiresDate));
				File parentPath = new File(savePath);
				if (!parentPath.exists()) {
					parentPath.mkdirs();
				}
				if (!savePath.endsWith("\\")) {
					savePath += "\\";
				}
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				InputStream input = null;
				try {
					input = entity.getContent();
					filePath = savePath + filename + ".jpg";
					File file = new File(filePath);
					FileOutputStream output = FileUtils.openOutputStream(file);
					try {
						IOUtils.copy(input, output);
					} finally {
						IOUtils.closeQuietly(output);
					}
				} finally {
					IOUtils.closeQuietly(input);
				}
				String info = String.format("下载媒体文件成功，filePath=" + filePath);
				logger.info(info);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} catch (IOException e) {
				throw e;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			String error = String.format("下载媒体文件失败：%s", e.getMessage());
			logger.info(error);
		} finally {
			// 关闭流并释放资源
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}

	/**
	 * @param contents 二维码内容
	 * @param qrCodeSize
	 * @param filepath
	 * @param filename
	 * @param logoFilePath
	 * @throws WriterException
	 * @throws IOException
	 */
	public static String makeQRCode(String contents, Integer qrCodeSize, String filepath, String filename, String logoFilePath) {
		String fileDir = "";
		try {
			int width = qrCodeSize; // 二维码图片宽度 300
			int height = width; // 二维码图片高度300
			String format = "png";// 二维码的图片格式 gif
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 内容所使用字符集编码
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
			// hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
			hints.put(EncodeHintType.MARGIN, 2);// 设置二维码边的空度，非负数
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, // 要编码的内容
					BarcodeFormat.QR_CODE, // 编码类型
					width, // 条形码的宽度
					height, // 条形码的高度
					hints);// 生成条形码时的一些配置,此项可选
			// 生成二维码
			File outputFile = new File(filepath);
			if (!outputFile.exists()) {
				outputFile.mkdirs();
			}
			if (!filepath.endsWith("\\")) {
				filepath += "\\";
			}
			outputFile = new File(filepath + filename);// 指定输出路径
			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile, logoFilePath);
			fileDir = filepath + filename;
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileDir;
	}
	
	public static void makeQRCode(String contents, Integer qrCodeSize, OutputStream stream, String logoFilePath) {
		try {
			int width = qrCodeSize; // 二维码图片宽度 300
			int height = width; // 二维码图片高度300
			String format = "png";// 二维码的图片格式 gif
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 内容所使用字符集编码
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
			// hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
			hints.put(EncodeHintType.MARGIN, 2);// 设置二维码边的空度，非负数
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, // 要编码的内容
					BarcodeFormat.QR_CODE, // 编码类型
					width, // 条形码的宽度
					height, // 条形码的高度
					hints);// 生成条形码时的一些配置,此项可选
			MatrixToImageWriter.writeToStream(bitMatrix, format, stream, null);
		} catch (WriterException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
