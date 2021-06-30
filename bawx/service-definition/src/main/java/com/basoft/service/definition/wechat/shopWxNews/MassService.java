package com.basoft.service.definition.wechat.shopWxNews;

import com.basoft.service.param.wechat.shopWxNews.SenNewsForm;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:35 2018/5/10
 **/
public interface MassService {
	/**
	 * 预览群发图文消息-预览不返回
	 * 
	 * @param shopId
	 * @param openid
	 * @param newsList
	 * @return
	 */
	@Deprecated
	public void previewMassMessage(Long shopId, String openid, List<Map<String, Object>> newsList);
	
	/**
	 * 预览群发图文消息-返回media id
	 * 
	 * @param shopId
	 * @param openid
	 * @param newsList
	 * @return
	 */
	public String previewMassMessageWithMediaId(Long shopId, String openid, List<Map<String, Object>> newsList);

	/**
	 * 预览永久图文群发-返回media id
	 *
	 * @param shopId
	 * @param openid
	 * @param newsList
	 * @return
	 */
	public String previewForeverMassMessageWithMediaId(Long shopId, String openid, List<Map<String, Object>> newsList);

	/**
	 * 群发图文消息
	 * 
	 * @param shopId
	 * @param is_to_all
	 * @param touser
	 * @param newsList
	 * @return
	 */
	public Map<String, Object> sendMassMessage(Long shopId, boolean is_to_all, List<String> touser, List<Map<String, Object>> newsList);

	/**
	 * 保存群发结果
	 * 
	 * @param map
	 */
	public void saveSendResult(Map<String, Object> map);

	/**
	 * 保存预览结果
	 * 
	 * @param map
	 */
	public void savePreviewResult(Map<String, Object> map);

	/**
	 * 群发消息：图片、文本、视频、音频
	 * 
	 * @param shopId
	 * @param is_to_all
	 * @param touser
	 * @param form
	 * @return
	 */
	public Map<String, Object> massSend(Long shopId, boolean is_to_all, List<String> touser, SenNewsForm form);
}
