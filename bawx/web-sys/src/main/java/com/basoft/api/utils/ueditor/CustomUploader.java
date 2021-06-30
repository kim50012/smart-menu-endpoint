package com.basoft.api.utils.ueditor;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.Base64Uploader;
import com.basoft.api.utils.upload.UploadMultipartFileUtil;
import com.basoft.api.utils.upload.UploadResult;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//@Slf4j
public class CustomUploader{

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private HttpServletRequest request = null;

	private Map<String, Object> conf = null;

	private MultipartFile file;

	private UploadMultipartFileUtil uploadMultipartFileUtil;

	private Map<String, String> uploadParam;

	private String serverDomain;

	private String serverContextPath;

	public CustomUploader(HttpServletRequest request, Map<String, Object> conf,
						  MultipartFile file, UploadMultipartFileUtil uploadMultipartFileUtil,
						  Map<String, String> uploadParam, String serverContextPath, String serverDomain) {
		this.request = request;
		this.conf = conf;
		this.file = file;
		this.uploadMultipartFileUtil = uploadMultipartFileUtil;
		this.uploadParam = uploadParam;
		this.serverDomain = serverDomain;
		this.serverContextPath = serverContextPath;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = save();
		}
		return state;
	}
	
	private final State save() {
		// 验证请求类型是否合法
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		
		// 判断文件数据有无
		if (file == null) {
			return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
		}
		
		// 判断文件类型是否允许
		String originalFilename = file.getOriginalFilename();
		String suffix = FileType.getSuffixByFilename(originalFilename);
		if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
			return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
		}
		
		// 文件大小
		long maxSize = ((Long) conf.get("maxSize")).longValue();
		if (file.getSize() > maxSize) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}
		
		// 上传文件
		UploadResult result = null;
		try {
			result = uploadMultipartFileUtil.upload(request, file, uploadParam.get("uploadType"), uploadParam.get("gLevel"), 
					uploadParam.get("pLevel"), uploadParam.get("sLevel"));
			log.info(result.toString());
		} catch (IllegalStateException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
		
		State state = new BaseState(true);
		state.putInfo( "size", file.getSize() );
		state.putInfo( "title", result.getDestinationFilename() );

		// 20190604添加server context path到上传文件的Resource URL开头
		//state.putInfo("url", result.getFileUrl());
		state.putInfo("url", serverDomain + serverContextPath + result.getFileUrl());

		state.putInfo("type", suffix);
		state.putInfo("original", result.getOriginalFilename());
		return state;
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}