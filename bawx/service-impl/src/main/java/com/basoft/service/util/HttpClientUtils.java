package com.basoft.service.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.basoft.core.ware.wechat.exception.HttpClientException;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;

import net.sf.json.JSONObject;

/** 
*  HttpClient4工具类
*/
public class HttpClientUtils {
	private static final transient Log logger = LogFactory.getLog(HttpClientUtils.class);
	
	/**
	 * @param url
	 * @param params
	 * @return
	 */
	public static String requestGet(String url, List<NameValuePair> params) {
		String returns = "";
		// CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 创建HttpGet
			HttpGet httpGet = new HttpGet(url);
			// 设置参数
			if(params != null && !params.isEmpty()){
				String sparams = EntityUtils.toString(new UrlEncodedFormEntity(params));
				httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + sparams));
			}
			logger.info("Executing Request: " + httpGet.getRequestLine());
			// 执行get请求
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					logger.info("Response content: " + returns);
					// logger.info("开始Response content转码");
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					// returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					// logger.info("Response content: " + returns);
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
		} finally {
			// 关闭连接，释放资源
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returns;
	}
	
	public static String requestGet(String url) {
		 return requestGet(url, null);
	}
	
	public static String requestPost(String url, List<NameValuePair> params) {
		String returns = "";
		// CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// httpClient.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
			// httpClient.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			// httpClient.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			// httpClient.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,HTTP.UTF_8);
			// 创建HttpPost
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			// 设置post编码
			// httpPost.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, HTTP.UTF_8);
			// List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			// //提交两个参数及值
			// nvps.add(new BasicNameValuePair("grant_type", "client_credential"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			// 设置报文头
			// httpPost.setHeader("Content-Type", "text/xml;charset=" + HTTP.UTF_8);
			// 执行post请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					logger.info("Response content:" + returns);
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returns;
	}
	
	public static String requestPost(String url, JSONObject jsonObject) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			// 创建HttpPost
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			logger.info("POST Data: " + jsonObject.toString());
			// 设置post编码
			// httpPost.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, HTTP.UTF_8);
			// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpPost.setEntity(new StringEntity(jsonObject.toString(), Consts.UTF_8));
			// 设置报文头
			// httpPost.setHeader("Content-Type", "text/xml;charset=" + HTTP.UTF_8);
			// 执行post请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			/*
			 * Header[] headers = response.getAllHeaders(); for (Header header : headers) {
			 * logger.info(header.getName() +"===>" + header.getValue()); }
			 */
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					// returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);//20180813-导致乱码-注释掉
					// logger.info("Response content:" + returns);//20180813-导致乱码-注释掉
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returns;
	}
	
	public static String requestPost2(String url, JSONObject jsonObject) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			// 创建HttpPost
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			logger.info("POST Data: " + jsonObject.toString());
			// 设置post编码
			// httpPost.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, HTTP.UTF_8);
			// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpPost.setEntity(new StringEntity(jsonObject.toString(), Consts.UTF_8));
			// 设置报文头
			// httpPost.setHeader("Content-Type", "text/xml;charset=" + HTTP.UTF_8);
			// 执行post请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			/*
			 * Header[] headers = response.getAllHeaders(); for (Header header : headers) {
			 * logger.info(header.getName() +"===>" + header.getValue()); }
			 */
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity, Consts.UTF_8);
					logger.info("Response content:" + returns);
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returns;
	}
	
	public static String requestPost(String url, String postParams) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			// 创建HttpPost
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			// 设置post编码
			// httpPost.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			// httpPost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, HTTP.UTF_8);
			// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpPost.setEntity(new StringEntity(postParams, Consts.UTF_8));
			// 设置报文头
			// httpPost.setHeader("Content-Type", "text/xml;charset=" + HTTP.UTF_8);
			// 执行post请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					logger.info("Response content:" + returns);
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returns;
	}
	
	public static String requestPost(String url) {
		return requestPost(url, "");
	}
	
	public static String uploadFile(String url, String fileUrl) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			File file = new File(fileUrl);
			/*** 4.3之前 **/
			// MultipartEntity multipartEntity = new MultipartEntity();
			// multipartEntity.addPart("media", new FileBody(file));
			// multipartEntity.addPart("desc", new StringBody(file.getName(),
			// ContentType.MULTIPART_FORM_DATA));
			/*** 4.3之前 end **/
			/*** 4.3之后 **/
			StringBody fileName = new StringBody(file.getName(), ContentType.MULTIPART_FORM_DATA);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)// 以浏览器兼容模式运行，防止文件名乱码
					.addPart("media", new FileBody(file)).addPart("desc", fileName);
			HttpEntity multipartEntity = builder.build();
			/*** 4.3之后 **/
			httpPost.setEntity(multipartEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				HttpEntity entity = response.getEntity();
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					// logger.info("ContentLength=" + entity.getContentLength());
					logger.info("Response content:" + returns);
				}
				// 销毁
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
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
		return returns;
	}
	
	/**
	 * 上传永久素材到微信服务器
	 * 
	 * @param url
	 * @param type 媒体文件类型，分别有图片（image）、语音（voice）、缩略图（thumb）
	 * @param fileUrl 本地文件路径
	 * @return
	 */
	public static String uploadMaterial(String url, String type, String fileUrl) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			File file = new File(fileUrl);
			if (!file.exists()) {
				throw new WeixinErrorException("文件不存在，请确认 fileUrl = " + fileUrl);
			}
			FileBody mediaBody = new FileBody(file);
			// System.out.println("contentLength=" + mediaBody.getContentLength());
			// System.out.println("filename=" + mediaBody.getFilename());
			// System.out.println("mediaType=" + mediaBody.getMediaType());
			// System.out.println("mimeType=" + mediaBody.getMimeType());
			// System.out.println("charset=" + mediaBody.getCharset());
			// System.out.println("subType=" + mediaBody.getSubType());
			// System.out.println("transferEncoding=" + mediaBody.getTransferEncoding());
			// System.out.println("contentType=" + mediaBody.getContentType());
			/*** 4.3之后 **/
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addPart("media", mediaBody);
			builder.addPart("type", new StringBody(type, ContentType.MULTIPART_FORM_DATA));
			HttpEntity multipartEntity = builder.build();
			/*** 4.3之后 **/
			httpPost.setEntity(multipartEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				HttpEntity entity = response.getEntity();
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					// logger.info("ContentLength=" + entity.getContentLength());
					logger.info("Response content:" + returns);
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new WeixinErrorException("上传失败! err_msg:" + e.getMessage());
		} finally {
			// httpClient.getConnectionManager().shutdown();
			// 关闭流并释放资源
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returns;
	}

	/** 
	* 上传视频永久素材到微信服务器
	* @param url 
	* @param type 媒体文件类型 ：视频（video）
	* @param fileUrl 本地文件路径
	* @param title 视频素材的标题
	* @param introduction 视频素材的描述
	* @return
	*/
	public static String uploadVideoMaterial(String url, String type, String fileUrl, String title,
			String introduction) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			File file = new File(fileUrl);
			if (!file.exists()) {
				throw new WeixinErrorException("文件不存在，请确认 fileUrl = " + fileUrl);
			}
			FileBody mediaBody = new FileBody(file);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("title", title);
			jsonObject.put("introduction", introduction);
			// logger.info("description=" + jsonObject.toString());
			/*** 4.3之后 **/
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addPart("media", mediaBody);
			builder.addPart("type", new StringBody(type, ContentType.MULTIPART_FORM_DATA));
			builder.addPart("description", new StringBody(jsonObject.toString(), ContentType.MULTIPART_FORM_DATA));
			HttpEntity multipartEntity = builder.build();
			/*** 4.3之后 **/
			httpPost.setEntity(multipartEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				HttpEntity entity = response.getEntity();
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					// logger.info("ContentLength=" + entity.getContentLength());
					logger.info("Response content:" + returns);
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new WeixinErrorException("上传失败! err_msg:" + e.getMessage());
		} finally {
			// httpClient.getConnectionManager().shutdown();
			// 关闭流并释放资源
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returns;
	}
	
	
	/** 
	*上传图文消息内的图片获取URL
	*
	* @param url
	* @param fileUrl 图片文件地址
	* @return
	*/
	public static String uploadImg(String url, String fileUrl) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost httpPost = new HttpPost(url);
			logger.info("Executing Request: " + httpPost.getRequestLine());
			File file = new File(fileUrl);
			/*** 4.3之前 **/
			// MultipartEntity multipartEntity = new MultipartEntity();
			// multipartEntity.addPart("media", new FileBody(file));
			// multipartEntity.addPart("desc", new StringBody(file.getName(),
			// ContentType.MULTIPART_FORM_DATA));
			/*** 4.3之前 end **/
			/*** 4.3之后 **/
			// StringBody fileName = new StringBody(file.getName(),
			// ContentType.MULTIPART_FORM_DATA);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)// 以浏览器兼容模式运行，防止文件名乱码
					.addPart("media", new FileBody(file));
			// .addPart("desc", fileName)；
			HttpEntity multipartEntity = builder.build();
			/*** 4.3之后 **/
			httpPost.setEntity(multipartEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				HttpEntity entity = response.getEntity();
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					// logger.info("ContentLength=" + entity.getContentLength());
					logger.info("Response content:" + returns);
				}
				// 销毁
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
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
		return returns;
	}

	/*@SuppressWarnings("unchecked")
	public static String requestSSL(String url, Map<String,Object> paramsMap) {
		String returns = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			// Secure Protocol implementation.
			SSLContext ctx = SSLContext.getInstance("SSL");
			// Implementation of a trust manager for X509 certificates
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
				public X509Certificate[] getAcceptedIssuers() { return null; }
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);

			ClientConnectionManager ccm = httpClient.getConnectionManager();
			// register https protocol in httpclient's scheme registry
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));

			// HttpGet httpget = new
			// HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxf4ff15142f410758&secret=a8b1ec6488389a5e1be2f4941a8b9f3f");
			HttpGet httpGet = new HttpGet(url);
			
			if(paramsMap != null && !paramsMap.isEmpty()){
				HttpParams params = httpClient.getParams();
				for(Map.Entry<String, Object> entry:paramsMap.entrySet()){    
				     logger.info(entry.getKey()+"--->"+entry.getValue());    
				     params.setParameter(entry.getKey(),entry.getValue());
				} 
				httpGet.setParams(params);
			}
			logger.info("REQUEST:" + httpGet.getURI());
			ResponseHandler responseHandler = new BasicResponseHandler();
			returns = httpClient.execute(httpGet, responseHandler);
			logger.info(returns);
			// Create a response handler
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (ClientProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returns;
	}*/
	
	/*public static String requestSSL(String url) {
		return requestSSL(url,null);
	}*/
	

	public static String requestPost(String url, JSONObject jsonObject, String token) {
		String returns = "";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			// 创建HttpPost
			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(new StringEntity(jsonObject.toString(), Consts.UTF_8));
			httpPost.setHeader("token", token);
			// 执行post请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			/*
			 * Header[] headers = response.getAllHeaders(); for (Header header : headers) {
			 * logger.info(header.getName() +"===>" + header.getValue()); }
			 */
			try {
				logger.info("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ response logging ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				logger.info("Response Status: " + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容的长度
					// logger.info("Response content lenght:" + entity.getContentLength());
					returns = EntityUtils.toString(entity);
					// 解决HttpClient获取中文乱码 ，用String对象进行转码
					// returns = new String(returns.getBytes(Consts.ISO_8859_1), Consts.UTF_8);//20180813-导致乱码-注释掉
					// logger.info("Response content:" + returns);//20180813-导致乱码-注释掉
				}
				EntityUtils.consume(entity);
				logger.info("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ response logging ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpClientException(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returns;
	}	
	
}
