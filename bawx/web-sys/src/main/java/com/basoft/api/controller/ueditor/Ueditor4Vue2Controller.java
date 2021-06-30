package com.basoft.api.controller.ueditor;

import com.baidu.ueditor.ActionEnter;
import com.basoft.api.controller.BaseController;
import com.basoft.api.utils.aws.AwsFileUtil;
import com.basoft.api.utils.tencent.TencentFileUtil;
import com.basoft.api.utils.ueditor.CustomActionEnter;
import com.basoft.api.utils.upload.UploadMultipartFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理关于UEditor插件相关的请求
 * for UEditor 1.4.3.3并适配VUE-cli2.x
 * 
 * @author yan_cunling
 */
@RestController
@CrossOrigin
@RequestMapping("/ueditor")
public class Ueditor4Vue2Controller extends BaseController{
	public static final Logger logger = LoggerFactory.getLogger(Ueditor4Vue2Controller.class);

	// 本地存储功能组件
	@Autowired
	UploadMultipartFileUtil uploadMultipartFileUtil;

	// 亚马逊存储功能组件 // 20190725AWS云存储停用
	// @Autowired
	// AwsFileUtil awsFileUtil;

	// 腾讯云存储功能组件
	@Autowired
	TencentFileUtil tencentFileUtil;

	//ueditor初始化配置路径
	@Value("${ueditor.init.configFile}")
	private String initConfigFilePath;
	
	// 1-自定义存储模式（适配SPRINGMVC存储） 0-UEDITOR组件自有存储模式（commons upload组件）
	@Value("${ueditor.file.saveMode}")
	private String saveMode;

	// 20190604添加server context path和域名到上传文件的Resource URL开头
	@Value("${server.context-path}")
	private String serverContextPath;

	@Value("${basoft.web.domain}")
	private String serverDomain;

	/**
	 * github ueditor wrap project跨域问题[备注：原生UEditor应该存在相同的问题]
	 *
	 * 问题表现：
	 * Refused to execute script from 'http://192.168.1.169:8003/bawx/ueditor/exec?action=config&callback=bd__editor__nsm05n'
	 * because its MIME type ('application/json') is not executable, and strict MIME type checking is enabled.
	 *
	 * 解决方案：
	 * （1）后端代码支持JSONP跨域解决方案，只要request传入callback，返回值就会进行JSONP封装
	 * （2）前端关键代码：dataType: isJsonp ? "jsonp" : ""。jsonp改为->JSONP。此次问题修复由前端来完成的
	 *
	 * 参考
	 * https://segmentfault.com/q/1010000004359113
	 */
	@RequestMapping(value = "/exec", produces="application/json;charset=utf-8")
	@ResponseBody
	public String exec(@RequestParam(value = "target", required = false) String target,
					   @RequestParam(value = "callback" ,required=false) String callback,
					   HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		//@SuppressWarnings("deprecation")
		//String rootPath = request.getRealPath("/resources/ueditor");
		//String rootPath = "ueditor/config.json";//文件输入流为空
		//String rootPath = "/ueditor/config.json";
		String rootPath = initConfigFilePath;
        logger.info("rootPath>>>>>>" + rootPath);
		logger.info("serverContextPath>>>>>>" + serverContextPath);

		String action = request.getParameter("action");
        logger.info("ueditor action is>>>>>>>>" + request.getParameter("action"));
		
		// 加载配置
		if("config".equals(action)) {
			return new ActionEnter(request, rootPath).exec();
		}
		
		if("1".equals(saveMode)) {// 自定义存储模式
			Map<String ,String> uploadParam = new HashMap<String, String>();
			
			uploadParam.put("uploadType", "ueditor");
			
			uploadParam.put("gLevel", "");
			
			String groupId = "ba";
			String shopId = "bawechat";
			if(getTokenSession() != null ) {
				groupId = String.valueOf(getTokenSession().getGroupId());
				shopId = String.valueOf(getShopId());
			}
			uploadParam.put("pLevel", groupId);
			uploadParam.put("sLevel", shopId);

			// 如果不是上传文件则不是MultipartHttpServletRequest请求，此时强转为类型转换异常
			MultipartFile file = null;
			if(action.startsWith("upload")){
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				file = multipartRequest.getFile("upfile");
			}

			// 20190725AWS云存储停用
			/*return new CustomActionEnter(request, rootPath, file,
					uploadMultipartFileUtil, awsFileUtil, uploadParam,
					serverContextPath, serverDomain, target).exec();*/

			return new CustomActionEnter(request, rootPath, file,
					uploadMultipartFileUtil, tencentFileUtil, uploadParam,
					serverContextPath, serverDomain, target).exec();
		} else {// UEDITOR自有存储模式
			return new ActionEnter(request, rootPath).exec();
		}
	}
}