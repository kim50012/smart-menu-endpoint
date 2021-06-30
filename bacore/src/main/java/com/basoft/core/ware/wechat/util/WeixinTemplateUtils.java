package com.basoft.core.ware.wechat.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.basoft.core.ware.wechat.domain.WeixinReturn;
import com.basoft.core.ware.wechat.domain.template.DataItem;
import com.basoft.core.ware.wechat.domain.template.GetTemplateIdReturn;
import com.basoft.core.ware.wechat.domain.template.SendTemplateMessageReturn;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;

import net.sf.json.JSONObject;


/**
 * 发送消息- 模板消息接口工具类
 */
public class WeixinTemplateUtils extends WeixinBaseUtils {
	public static final Map<String, String> openidlist = new HashMap<String, String>();
	static {
		openidlist.put("8D6ED58C805242C8BDB129616163CB04", "o1yuEtw1PN5sc6W6Bxd7Q4hty2hY");
		openidlist.put("JFUN2LRULZT81XGK7OHBITHAU4KMWMU9", "oRMPGjncnxEf7h-YMt-1y4aD9fhg");
		openidlist.put("XKVYUYL8VLIUZU4E5UKTTBTHTHP8CNVC", "orWqcjvAVxJlV06t3Mt03E_mwc4Y");
		openidlist.put("XJIY2YYKITMJQ6FTYPTBIOEWWOXVVWWY", "oUPIGuHw5IPnjTpZHtItmGPHI3nU");
	}

	/**
	 * 设置所属行业接口
	 */
	private static final String TEMPLATE_API_SET_INDUSTRY_URL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";

	/**
	 * 获得模板ID接口
	 */
	private static final String TEMPLATE_API_ADD_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";

	/**
	 * 发送模板消息接口
	 */
	private static final String MESSAGE_TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	/** 
	*设置所属行业<br>
	*设置行业可在MP中完成，每月可修改行业1次，账号仅可使用所属行业中相关的模板，为方便第三方开发者，提供通过接口调用的方式来修改账号所属行业，具体如下：
	* @param access_token
	* @param industry_id1 公众号模板消息所属行业编号
	* @param industry_id2 公众号模板消息所属行业编号
	* <table>
	*   <tr><td>主行业</td><td>副行业</td><td>代码</td></tr>
	 * <tr><td>IT科技</td><td>互联网/电子商务</td><td>1</td></tr>
	 * <tr><td>IT科技</td><td>IT软件与服务</td><td>2</td></tr>
	 * <tr><td>IT科技</td><td>IT硬件与设备</td><td>3</td></tr>
	 * <tr><td>IT科技</td><td>电子技术</td><td>4</td></tr>
	 * <tr><td>IT科技</td><td>通信与运营商</td><td>5</td></tr>
	 * <tr><td>IT科技</td><td>网络游戏</td><td>6</td></tr>
	 * <tr><td>金融业</td><td>银行</td><td>7</td></tr>
	 * <tr><td>金融业</td><td>基金|理财|信托</td><td>8</td></tr>
	 * <tr><td>金融业</td><td>保险</td><td>9</td></tr>
	 * <tr><td>餐饮</td><td>餐饮</td><td>10</td></tr>
	 * <tr><td>酒店旅游</td><td>酒店</td><td>11</td></tr>
	 * <tr><td>酒店旅游</td><td>旅游</td><td>12</td></tr>
	 * <tr><td>运输与仓储</td><td>快递</td><td>13</td></tr>
	 * <tr><td>运输与仓储</td><td>物流</td><td>14</td></tr>
	 * <tr><td>运输与仓储</td><td>仓储</td><td>15</td></tr>
	 * <tr><td>教育</td><td>培训</td><td>16</td></tr>
	 * <tr><td>教育</td><td>院校</td><td>17</td></tr>
	 * <tr><td>政府与公共事业</td><td>学术科研</td><td>18</td></tr>
	 * <tr><td>政府与公共事业</td><td>交警</td><td>19</td></tr>
	 * <tr><td>政府与公共事业</td><td>博物馆</td><td>20</td></tr>
	 * <tr><td>政府与公共事业</td><td>公共事业|非盈利机构</td><td>21</td></tr>
	 * <tr><td>医药护理</td><td>医药医疗</td><td>22</td></tr>
	 * <tr><td>医药护理</td><td>护理美容</td><td>23</td></tr>
	 * <tr><td>医药护理</td><td>保健与卫生</td><td>24</td></tr>
	 * <tr><td>交通工具</td><td>汽车相关</td><td>25</td></tr>
	 * <tr><td>交通工具</td><td>摩托车相关</td><td>26</td></tr>
	 * <tr><td>交通工具</td><td>火车相关</td><td>27</td></tr>
	 * <tr><td>交通工具</td><td>飞机相关</td><td>28</td></tr>
	 * <tr><td>房地产</td><td>建筑</td><td>29</td></tr>
	 * <tr><td>房地产</td><td>物业</td><td>30</td></tr>
	 * <tr><td>消费品</td><td>消费品</td><td>31</td></tr>
	 * <tr><td>商业服务</td><td>法律</td><td>32</td></tr>
	 * <tr><td>商业服务</td><td>会展</td><td>33</td></tr>
	 * <tr><td>商业服务</td><td>中介服务</td><td>34</td></tr>
	 * <tr><td>商业服务</td><td>认证</td><td>35</td></tr>
	 * <tr><td>商业服务</td><td>审计</td><td>36</td></tr>
	 * <tr><td>文体娱乐</td><td>传媒</td><td>37</td></tr>
	 * <tr><td>文体娱乐</td><td>体育</td><td>38</td></tr>
	 * <tr><td>文体娱乐</td><td>娱乐休闲</td><td>39</td></tr>
	 * <tr><td>印刷</td><td>印刷</td><td>40</td></tr>
	 * <tr><td>其它</td><td>其它</td><td>41</td></tr>
	 * </table>
	*/
	public static void setIndustry(String access_token, Integer industry_id1, Integer industry_id2) {
		String url = getInterfaceUrl(TEMPLATE_API_SET_INDUSTRY_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("industry_id1", industry_id1);
		jsonObject.put("industry_id2", industry_id2);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}
	
	
	/**
	 * 获得模板ID<br>
	 * 从行业模板库选择模板到账号后台，获得模板ID的过程可在MP中完成。为方便第三方开发者，提供通过接口调用的方式来修改账号所属行业，具体如下：
	 * 
	 * @param access_token
	 * @param template_id_short 模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
	 *            <table>
	 *            <tr>
	 *            <td>主行业</td>
	 *            <td>副行业</td>
	 *            <td>代码</td>
	 *            </tr>
	 *            <tr>
	 *            <td>IT科技</td>
	 *            <td>互联网/电子商务</td>
	 *            <td>1</td>
	 *            </tr>
	 * 
	 *            </table>
	 * 
	 * @return
	 */
	public static String getTemplateId(String access_token, String template_id_short) {
		String url = getInterfaceUrl(TEMPLATE_API_ADD_TEMPLATE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("template_id_short", template_id_short);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetTemplateIdReturn returns = new Gson().fromJson(result, GetTemplateIdReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getTemplate_id();
	}
	
	/**
	 * 发送模板消息<br>
	 * 模板消息仅用于公众号向用户发送重要的服务通知，只能用于符合其要求的服务场景中，<br>
	 * 如信用卡刷卡通知，商品购买成功通知等。不支持广告等营销类消息以及其它所有可能对用户造成骚扰的消息。<br>
	 * 
	 * 关于使用规则，请注意：<br>
	 * 1、所有服务号都可以在功能->添加功能插件处看到申请模板消息功能的入口，但只有认证后的服务号才可以申请模板消息的使用权限并获得该权限；<br>
	 * 2、需要选择公众账号服务所处的2个行业，每月可更改1次所选行业；<br>
	 * 3、在所选择行业的模板库中选用已有的模板进行调用；<br>
	 * 4、每个账号可以同时使用10个模板。<br>
	 * 5、当前每个模板的日调用上限为100000次【2014年11月18日将接口调用频率从默认的日10000次提升为日100000次，可在MP登录后的开发者中心查看】。
	 * 
	 * @param access_token
	 * @param touser
	 * @param templateId
	 * @param linkUrl
	 * @param data
	 * @return Long
	 */
	public static Long sendTemplateMessage(String access_token, String touser, String templateId, String linkUrl, Map<String, DataItem> data) {
		return sendTemplateMessage(access_token, touser, templateId, linkUrl, "#ff0000", data);
	}
	
	
	/**
	 * 发送模板消息<br>
	 * 模板消息仅用于公众号向用户发送重要的服务通知，只能用于符合其要求的服务场景中，<br>
	 * 如信用卡刷卡通知，商品购买成功通知等。不支持广告等营销类消息以及其它所有可能对用户造成骚扰的消息。<br>
	 * 
	 * 关于使用规则，请注意：<br>
	 * 1、所有服务号都可以在功能->添加功能插件处看到申请模板消息功能的入口，但只有认证后的服务号才可以申请模板消息的使用权限并获得该权限；<br>
	 * 2、需要选择公众账号服务所处的2个行业，每月可更改1次所选行业；<br>
	 * 3、在所选择行业的模板库中选用已有的模板进行调用；<br>
	 * 4、每个账号可以同时使用10个模板。<br>
	 * 5、当前每个模板的日调用上限为100000次【2014年11月18日将接口调用频率从默认的日10000次提升为日100000次，可在MP登录后的开发者中心查看】。
	 * 
	 * @param access_token
	 * @param touser
	 * @param templateId
	 * @param linkUrl
	 * @param topcolor
	 * @param data
	 * @return Integer
	 */
	public static Long sendTemplateMessage(String access_token, String touser, String templateId, String linkUrl, String topcolor, Map<String, DataItem> data) {
		String url = getInterfaceUrl(MESSAGE_TEMPLATE_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser", touser);
		jsonObject.put("template_id", templateId);
		if (StringUtils.isNotEmpty(linkUrl)) {
			jsonObject.put("url", linkUrl);
		}
		jsonObject.put("topcolor", topcolor);
		jsonObject.put("data", data);
		String params = jsonObject.toString();
		params = params.replaceAll("@#!!#@", "");
		String result = HttpClientUtils.requestPost(url, params);
		SendTemplateMessageReturn returns = new Gson().fromJson(result, SendTemplateMessageReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getMsgid();
	}
}