package com.basoft.api.utils.upload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 解读物理路径和访问路径：
 * 1、物理路径：
 * （1）webUploadPath为物理访问路径，配置项为basoft.web.upload-path，值如D:/temp/；
 * （2）absolutePath为webUploadPath+ROOT+定义的文件层级路径，
 * 比如 D:/temp/images/111/1/348915813615140872/2018/05/16/111_1_348915813615140872_20180516202016763_27404197.jpg
 * 2、访问路径
 * （1）staticUnionPath为访问路径，配置项为static.resources.union.path，值如/res/**
 * （2）relativePath为：staticUnionPath截取后面的/**后+ROOT+定义的文件层级路径，
 * 比如/res/images/111/1/348915813615140872/2018/05/16/111_1_348915813615140872_20180516202016763_27404197.jpg
 * 
 * @author basoft
 */
@Slf4j
@Configuration
@Component
public class UploadMultipartFileUtil {
	public static final String ROOT = "/images";

	@Value("${basoft.web.upload-path}")
    private String webUploadPath;
	
    @Value("${static.resources.union.path}")
    private String staticUnionPath;
	
	/**
	 * @param uploadType
	 *            业务类型
	 * @param layer
	 *            层级
	 * @param grandpa
	 *            第一层
	 * @param parent
	 *            第二层
	 * @param son
	 *            第三层
	 * @return
	 */
	private Map<String, String> getRequestPath(String uploadType, int layer, String grandpa, String parent,
			String son) {
		grandpa = grandpa == null || "".equals(grandpa) ? "g" : grandpa;
		parent = parent == null || "".equals(parent) ? "p" : parent;
		son = son == null || "".equals(son) ? "s" : son;
		uploadType = uploadType == null || "".equals(uploadType) ? "typeCommon" : uploadType;
		
		String generateFilePath = new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
		
		String absolutePath = "", relativePath = "";
		if (layer == 3) {
			absolutePath = webUploadPath + ROOT + "/" + uploadType + "/" + grandpa + "/" + parent + "/" + son + generateFilePath;
			relativePath = ROOT + "/" + uploadType + "/" + grandpa + "/" + parent + "/" + son + generateFilePath;
		} else if (layer == 2) {
			absolutePath = webUploadPath + ROOT + "/" + uploadType + "/" + parent + "/" + son + generateFilePath;
			relativePath = ROOT + "/" + uploadType + "/" + parent + "/" + son + generateFilePath;
		} else if (layer == 1) {
			absolutePath = webUploadPath + ROOT + "/" + uploadType + "/" + son + generateFilePath;
			relativePath = ROOT + "/" + uploadType + "/" + son + generateFilePath;
		} else if (layer == 0) {
			absolutePath = webUploadPath + ROOT + "/" + uploadType + generateFilePath;
			relativePath = ROOT + "/" + uploadType + generateFilePath;
		}
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("absolutePath", absolutePath);
		// m.put("relativePath", relativePath);
		m.put("relativePath", staticUnionPath.substring(0, staticUnionPath.lastIndexOf("/")) + relativePath);
		return m;
	}

	/**
	 * @param request
	 *            HttpServletRequest
	 * @param file
	 *            MultipartFile文件流
	 * @param uploadType
	 *            上传类型
	 * @param grandpa
	 *            存储层级-第一级
	 * @param parent
	 *            存储层级-第二级
	 * @param son
	 *            存储层级-第三级
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public UploadResult upload(HttpServletRequest request, MultipartFile file, String uploadType, String grandpa,
			String parent, String son) throws IllegalStateException, IOException {
		// 文件为空或大小为0，直接返回null
		if (file == null || file.getSize() == 0) {
			return null;
		}
		
		// 上传文件原名
		String originalFilename = file.getOriginalFilename();
		log.info("上传文件原名originalFilename===============" + originalFilename);
		
		// 生成上传文件的存储路径
		Map<String, String> m = getRequestPath(uploadType, 2, grandpa, parent, son);
		String path = m.get("absolutePath");
		log.info("生成上传文件的存储路径path===============" + path);

		//path已经为真实的物理路径，不需要再转了
		/*// 转为真实的物理路径
		String filePath = request.getSession().getServletContext().getRealPath(path);
		log.info("生成上传文件真实的物理路径filePath===============" + filePath);*/

		// 判断文件夹是否存在
		File target = new File(path);
		if (!target.exists()) {
			target.mkdirs();
		}

		// 上传的文件的新文件名
		String destinationFilename = generateSystemFilename(uploadType, 2, grandpa, parent, son, originalFilename);
		log.info("上传的文件的新文件名destinationFilename====" + destinationFilename);
		
		// 存储文件-将流转为目标文件
		target = new File(path, destinationFilename);
		file.transferTo(target);

		// 返回处理结果
		UploadResult result = new UploadResult();
		result.setRealPath(path + File.separator + destinationFilename);
		result.setOriginalFilename(originalFilename);
		result.setDestinationFilename(destinationFilename);
		result.setFileUrl(m.get("relativePath") + destinationFilename);
		result.setFileExt(getFileExt(originalFilename));
		result.setFileSize(file.getSize());
		// 读取原始图片的宽度和高度（像素）
		if (getSuffix(originalFilename).equalsIgnoreCase("png") || getSuffix(originalFilename).equalsIgnoreCase("jpg")
				|| getSuffix(originalFilename).equalsIgnoreCase("jpeg")
				|| getSuffix(originalFilename).equalsIgnoreCase("gif")) {
			try {
				BufferedImage sourceImg = ImageIO.read(new FileInputStream(target));
				result.setWidth(sourceImg.getWidth());
				result.setHeight(sourceImg.getHeight());
				sourceImg = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 释放资源，减少内存消耗
		file = null;
		target = null;

		return result;
	}

	/**
	 * 生成文件名
	 * 
	 * @param uploadType
	 * @param layer
	 * @param grandpa
	 * @param parent
	 * @param son
	 * @param originalFilename
	 * @return
	 */
	public static final String generateSystemFilename(String uploadType, int layer, String grandpa, String parent,
			String son, String originalFilename) {
		// 层级处理
		grandpa = grandpa == null || "".equals(grandpa)? "g" : grandpa;
		parent = parent == null || "".equals(parent)? "p" : parent;
		son = son == null || "".equals(son)? "s" : son;
		// 日期时间戳
		//SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String formatDate = sf.format(new Date());
		// 随机数
		String random = generateNumberRandom(8);
		// 文件扩展名
		String ext = getFileExt(originalFilename);

		String fileName = "";
		if (layer == 3) {
			fileName = uploadType + "_" + grandpa + "_" + parent + "_" + son + "_" + formatDate + "_" + random + ext;
		} else if (layer == 2) {
			fileName = uploadType + "_" + parent + "_" + son + "_" + formatDate + "_" + random + ext;
		} else if (layer == 1) {
			fileName = uploadType + "_" + son + "_" + formatDate + "_" + random + ext;
		} else if (layer == 0) {
			fileName = uploadType + "_" + formatDate + "_" + random + ext;
		}
		return fileName;
	}

	public static final String generateSystemFilename(String original, String uploadType) {
		//SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String formatDate = sf.format(new Date());
		String random = generateNumberRandom(8);
		String ext = getFileExt(original);
		return uploadType +"_"+ formatDate + "_" + random + ext;
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

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileExt(String filename) {
		int position = filename.lastIndexOf(".");
		if (position < 0) {
			return "";
		}
		return filename.substring(position, filename.length());
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		return suffix;
	}

	/**
	 * 获取文件对应的Mime类型
	 * 
	 * @param filename
	 * @return
	 */
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
		System.out.println(generateSystemFilename("hello.jpg", "test"));
		System.out.println(generateSystemFilename("test",3,"l1","l2","l3","hello.jpg"));
	}
}
