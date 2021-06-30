package com.basoft.service.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.List;

/**
 * ServletRequest工具类
 */
public class ServletRequestUtils {
	private static final transient Log logger = LogFactory.getLog(ServletRequestUtils.class);

	public static String readStreamParameter(HttpServletRequest request) {
		String returns = "";
		StringBuilder sb = new StringBuilder();
//		BufferedReader reader = null;
		try {


			final List<String> strings = IOUtils.readLines(request.getInputStream(), Charset.defaultCharset());
			for (String string : strings) {
				sb = sb.append(string);
			}
/*
			reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
//			returns = new String(sb.toString().getBytes("gbk"), "utf-8");
			returns = sb.toString();
*/
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取POST数据:" + e.getMessage());
		} finally {
/*
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("111==>" + e.getMessage());
				}
			}
*/
		}
		return returns;
	}
}
