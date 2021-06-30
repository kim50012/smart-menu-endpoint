package com.basoft.core.ware.wechat.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.util.WeixinQRCodeUtils;

/**
 * Servlet implementation class QrCodeImgServlet
 */
@WebServlet("/wx/qrimg")
public class QrCodeImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final transient Log logger = LogFactory.getLog(QrCodeImgServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QrCodeImgServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("======QrCodeImgServlet get=======");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("======QrCodeImgServlet post=======");
		// TODO Auto-generated method stub
		String code_url = request.getParameter("code_url");
		response.setContentType("image/png");
		WeixinQRCodeUtils.makeQRCode(code_url, WeixinQRCodeUtils.QRCodeSize.SIZE344, response.getOutputStream(), null);
	}
}