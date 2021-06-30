package com.basoft.service.definition.wechat.shopWxNews;

import com.basoft.core.ware.wechat.domain.mass.MassReturn;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:38 2018/5/10
 **/
public interface WeixinMassService {

    /**
     * 预览群发图文消息
     * @param shopId
     * @param openid
     * @param newsList 图文列表
     * @return
     */
    public void previewMass(Long shopId, String openid, List<Map<String, Object>> newsList);

    /**
     * 群发信息
     * @param shopId
     * @param is_to_all 是否全部发送
     * @param touser 用户列表
     * @param newsList 图文消息列表
     * @return MassRetuen
     */
    public MassReturn sendMass(Long shopId, boolean is_to_all, List<String> touser, List<Map<String, Object>> newsList);

}
