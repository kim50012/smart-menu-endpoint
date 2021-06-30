package com.basoft.core.ware.wechat.servlet;

import com.basoft.core.ware.wechat.servlet.utils.GetWxOrderno;
import com.basoft.core.ware.wechat.servlet.utils.RequestHandler;
import com.basoft.core.ware.wechat.servlet.utils.TenpayUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Servlet implementation class QueryOrderServlet
 */
@Slf4j
@WebServlet("/orderServlet")
public class QueryOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unused" })
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 商户相关资料
		// 微信支付商户号
		String mch_id = "1228162602";
		String partnerkey = "HmVj7vFHBmxVOGIenqXGpknatOxZmm0R";
		String appsecret = "22728684c80b1ff8080610358066103b";
		String appid = "wxf25575d804608717";
		// 微信订单号(根据实际更换)lo
		String transaction_id = "1004480953201504090054080133";
		// 商户订单号(根据实际更换)
		String out_trade_no = "10148";
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// 随机数
		String nonce_str = strReq;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("transaction_id", transaction_id);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("nonce_str", nonce_str);
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appid, appsecret, partnerkey);
		String sign = reqHandler.createSign(packageParams);
		String xml="<xml>"+
				"<appid>"+appid+"</appid>"+
				"<mch_id>"+mch_id+"</mch_id>"+
				"<transaction_id>"+transaction_id+"</transaction_id>"+
				"<out_trade_no>"+out_trade_no+"</out_trade_no>"+
				"<nonce_str>"+nonce_str+"</nonce_str>"+
				"<sign><![CDATA["+sign+"]]></sign>"+
				"</xml>";
		String allParameters = "";
		try {
			allParameters = reqHandler.genPackage(packageParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 订单查询接口URL
		String URL = "https://api.mch.weixin.qq.com/pay/orderquery";
		try {
			Map map = new GetWxOrderno().getOrderNo(URL, xml);
			log.info("map=" + map);
			if (map.get("trade_state").equals("")) {
				request.setAttribute("ErrorMsg", "查询订单出错");
				response.sendRedirect("error.jsp");
			} else {
				// String return_code = (String) map.get("return_code");
				log.info("map=" + map);
				// 交易状态
				String back_trade_state = (String) map.get("trade_state");
				// 此买家是否关注公众号Y-关注，N-未关注
				String back_is_subscribe = "";
				back_is_subscribe = (String) map.get("is_subscribe");
				// 交易类型
				String back_trade_type = "";
				back_trade_type = (String) map.get("trade_type");
				// 总金额
				String back_total_fee = "";
				back_total_fee = (String) map.get("total_fee");
				// 微信订单号
				String back_transaction_id = "";
				back_transaction_id = (String) map.get("transaction_id");
				// 商户订单号
				String back_out_trade_no = "";
				back_out_trade_no = (String) map.get("out_trade_no");
				// 交易结束时间
				String back_time_end = "";
				back_time_end = (String) map.get("time_end");
				// 买家openid
				String back_openid = "";
				back_openid = (String) map.get("openid");
				request.setAttribute("back_trade_state", back_trade_state);
				request.setAttribute("back_is_subscribe", back_is_subscribe);
				request.setAttribute("back_trade_type", back_trade_type);
				request.setAttribute("back_total_fee", back_total_fee);
				request.setAttribute("back_transaction_id", back_transaction_id);
				request.setAttribute("back_out_trade_no", back_out_trade_no);
				request.setAttribute("back_time_end", back_time_end);
				request.setAttribute("back_openid", back_openid);
				log.info("back_trade_state=" + back_trade_state);
				log.info("back_is_subscribe=" + back_is_subscribe);
				log.info("back_trade_type=" + back_trade_type);
				log.info("back_total_fee=" + back_total_fee);
				log.info("back_transaction_id=" + back_transaction_id);
				log.info("back_out_trade_no=" + back_out_trade_no);
				log.info("back_time_end=" + back_time_end);
				log.info("back_openid=" + back_openid);
				request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return;
	}

	/**
	 * The doPost method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}