package com.basoft.core.ware.wechat.util;

import com.basoft.core.ware.wechat.domain.WeixinReturn;
import com.basoft.core.ware.wechat.domain.menu.Menu;
import com.basoft.core.ware.wechat.domain.menu.SelfmenuInfoReturn;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * 自定义菜单管理工具类
 */
public class WeixinMenuUtils {
	/**
	 * 自定义菜单创建接口
	 */
	private static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 自定义菜单查询接口
	 */
	private static final String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

	/**
	 * 自定义菜单删除接口
	 */
	private static final String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 获取自定义菜单配置接口
	 */
	private static final String GET_CURRENT_SELFMENU_INFO_URL = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN";

	private static String getInterfaceUrl(String url, String access_token) {
		return url.replaceAll("ACCESS_TOKEN", access_token);
	}

	/**
	 * 自定义菜单创建接口
	 * 
	 * @param access_token
	 * @param menu
	 */
	public static void createMenu(String access_token, Menu menu) {
		String url = getInterfaceUrl(MENU_CREATE_URL, access_token);
		JSONObject jsonObject = JSONObject.fromObject(menu);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 自定义菜单查询接口
	 * 
	 * @param access_token
	 * @return String
	 */
	public static String getMenu(String access_token) {
		String url = getInterfaceUrl(MENU_GET_URL, access_token);
		return HttpClientUtils.requestGet(url);
	}

	/**
	 * 自定义菜单删除接口
	 * 
	 * @param access_token
	 */
	public static void deleteMenu(String access_token) {
		String url = getInterfaceUrl(MENU_DELETE_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * <h1>获取自定义菜单配置接口</h1>
	 * 
	 * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，<br>
	 * 则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，<br>
	 * 则本接口返回运营者设置的菜单配置。
	 * 
	 * <h3>请注意：</h3>
	 * <ol>
	 * <li>
	 * 1、第三方平台开发者可以通过本接口，在旗下公众号将业务授权给你后，<br>
	 * 立即通过本接口检测公众号的自定义菜单配置，并通过接口再次给公众号 <br>
	 * 设置好自动回复规则，以提升公众号运营者的业务体验。
	 * </li>
	 * <li>
	 * 2、本接口与自定义菜单查询接口的不同之处在于，本接口无论公众号的接<br>
	 * 口是如何设置的，都能查询到接口，而自定义菜单查询接口则仅能查询到<br>
	 * 使用API设置的菜单配置。
	 * </li>
	 * <li>
	 * 3、认证/未认证的服务号/订阅号，以及接口测试号，均拥有该接口权限。
	 * </li>
	 * <li>
	 * 4、从第三方平台的公众号登录授权机制上来说，该接口从属于消息与菜单权限集。
	 * </li>
	 * <li>
	 * 5、本接口中返回的mediaID均为临时素材（通过素材管理-获取临时素材接口来获取这些素材），<br>
	 * 每次接口调用返回的mediaID都是临时的、不同的，在每次接口调用后3天有效，若需永久使用该<br>
	 * 素材，需使用素材管理接口中的永久素材。
	 * </li>
	 * </ol>
	 * @param access_token
	 * @return SelfmenuInfoReturn
	 */
	public static SelfmenuInfoReturn getCurrentSelfMenuInfo(String access_token) {
		String url = getInterfaceUrl(GET_CURRENT_SELFMENU_INFO_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		SelfmenuInfoReturn returns = new Gson().fromJson(result, SelfmenuInfoReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}
}