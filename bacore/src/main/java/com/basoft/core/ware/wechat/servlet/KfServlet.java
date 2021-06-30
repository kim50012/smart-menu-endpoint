package com.basoft.core.ware.wechat.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet implementation class KfServlet
 */
@WebServlet(name = "kf", urlPatterns = { "/kf" })
public class KfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final transient Log logger = LogFactory.getLog(KfServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KfServlet() {
		super();
		// TODO Auto-generated constructor stub
		logger.info("--------------KfServlet()---------------");
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		logger.info("--------------init()---------------");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		logger.info("--------------destroy()---------------");
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		logger.info("--------------getServletConfig()---------------");
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		logger.info("--------------getServletInfo()---------------");
		return null;
	}

	// /**
	// * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	// response)
	// */
	// protected void service(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// logger.info("--------------service()---------------");
	// // TODO Auto-generated method stub
	// }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("--------------doGet()---------------");
		response.setCharacterEncoding("UTF-8");
		String type = request.getParameter("t");
		String p1 = request.getParameter("p1");
		logger.info("p1=" + p1);
		logger.info("p1=" + new String(p1.getBytes("ISO8859-1"), "UTF-8")); // 解决中文乱码
		if ("1".equals(type)) {
			request.getRequestDispatcher("/weixin/kf/plugin1.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/weixin/kf/plugin2.jsp").forward(request, response);
		}
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("--------------doPost()---------------");
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("--------------doPut()---------------");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("--------------doDelete()---------------");
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("--------------doHead()---------------");
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("--------------doOptions()---------------");
	}

	/**
	 * @see HttpServlet#doTrace(HttpServletRequest, HttpServletResponse)
	 */
	protected void doTrace(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("--------------doTrace()---------------");
		// TODO Auto-generated method stub
	}
}
