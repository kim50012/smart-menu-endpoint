package com.basoft.service.impl.wechat.shopWxNews;

import com.basoft.core.ware.wechat.domain.mass.*;
import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.ware.wechat.util.WeixinMassMessageUtils;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.service.dao.shop.ShopFileMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsCustMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsHeadMapper;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.shopWxNews.MassService;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import com.basoft.service.param.wechat.shopWxNews.SenNewsForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:36 2018/5/10
 **/
@Service
@Slf4j
@Configuration
public class MassServiceImpl implements MassService {
    @Autowired
    private ShopWxNewsHeadMapper shopWxNewsHeadMapper;

    @Autowired
    private ShopService shopService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private ShopFileMapper shopFileMapper;

    @Autowired
    private ShopWxNewsCustMapper shopWxNewsCustMapper;

    @Value("${basoft.web.upload-path}")
    private String webUploadPath;

    @Value("${static.resources.union.path}")
    private String staticUnionPath;

    /**
     * 预览群发图文消息-无法回
     *
     * @param shopId
     * @param openid
     * @param newsList
     * @return
     */
    @Deprecated
    public void previewMassMessage(Long shopId, String openid, List<Map<String, Object>> newsList) {
        AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(shopId);
        String token = wechatService.getAccessToken(appInfo);

        ////////////
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        /////////////

        NewsData newsData = new NewsData();
        List<Article> list = new ArrayList<Article>();
        for (int i = 0; i < newsList.size(); i++) {
            Map<String, Object> item = newsList.get(i);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            String mediaId = (String) item.get("MEDIA_ID");
            Long fileId = (Long) item.get("MFILE_ID");
            String fileUrl = (String) item.get("FILE_URL");
            String author = (String) item.get("MAUTHOR");
            String title = (String) item.get("MTITLE");
            String sourceUrl = (String) item.get("MSOURCE_URL");
            String content = (String) item.get("MCONTENTWECHAT");
            // String content = (String)item.get("MCONTENT");
            String mdigest = (String) item.get("MDIGEST");
            Integer mshowCoverPic = (Integer) item.get("MSHOW_COVER_PIC");
            log.info("old media id = " + mediaId);
            //mediaId失效，重新上传
            if (StringUtils.isEmpty(mediaId)) {
                log.info("webUploadPath=" + webUploadPath);
                log.info("fileUrl=" + fileUrl);
                log.info("token=" + token);
                // 转换访问路径为物理路径 - start
                String prefix = staticUnionPath.substring(0, staticUnionPath.lastIndexOf("/"));// "/res"
                int index = fileUrl.lastIndexOf(prefix) + prefix.length();
                fileUrl = fileUrl.substring(index);
                fileUrl = webUploadPath + fileUrl;
                // 转换访问路径为物理路径 - end
                MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", fileUrl);
                mediaId = mediaReturn.getMedia_id();

                /*****将文件表的mediaId赋值**start****/
                ShopFile shopFile = new ShopFile();
                shopFile.setFileId(fileId);
                shopFile.setShopId(shopId);
                shopFile.setMediaId(mediaId);
                shopFile.setMediaUpDt(new Date());
                shopFileMapper.updateByPrimaryKeySelective(shopFile);
                log.info("new media id = " + mediaId);
                /*****将文件表的mediaId赋值**end****/
            }

            Article a = new Article();
            a.setThumb_media_id(mediaId);
            a.setAuthor(author);
            a.setTitle(title);
            a.setContent_source_url(sourceUrl);
            a.setContent(content);
            a.setDigest(mdigest);
            a.setShow_cover_pic(mshowCoverPic + "");
            list.add(a);
        }
        newsData.setArticles(list);

        //上传图文消息素材【订阅号与服务号认证后均可用】
        MediaReturn mediaReturn = WeixinMassMessageUtils.uploadNews(token, newsData);
        if (mediaReturn.getType().equals("news")) {
            // 图文素材上传后的mediaid
            String tuwenMediaId = mediaReturn.getMedia_id();
            Long msgId = WeixinMassMessageUtils.previewMassMessage(token, openid, "mpnews", tuwenMediaId);
            log.info("msgId = " + msgId);
        }
    }

    /**
     * 预览永久素材群发-返回mediaId
     *
     * @param shopId
     * @param openid
     * @param newsList
     * @return
     */
    public String previewForeverMassMessageWithMediaId(Long shopId, String openid, List<Map<String, Object>> newsList) {
        AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(shopId);
        String token = wechatService.getAccessToken(appInfo);
        log.info("<<<<<<<<<<<<<<<<<<预览永久素材开始<<<<<<<<<<<<<<<<<<<<<<<");

        List<Article> articleList = getNewsData(shopId, token, newsList, true);//上传素材图片

        //上传临时图文消息素材【订阅号与服务号认证后均可用】
        String mediaId = WeixinMediaUtils.addNewsMaterial(token, articleList);

        if (StringUtils.isNotBlank(mediaId)) {
            // 图文素材上传后的mediaid
            log.info("foreverMediaId ===================================== " + mediaId);
            Long msgId = WeixinMassMessageUtils.previewMassMessage(token, openid, "mpnews", mediaId);
            log.info("msgId ===================================== " + msgId);
            if (mediaId == null) {
                mediaId = "";
            }
            return mediaId;
        }
        return "";
    }


    /**
     * 预览群发图文消息-返回mediaId
     *
     * @param shopId
     * @param openid
     * @param newsList
     * @return
     */
    public String previewMassMessageWithMediaId(Long shopId, String openid, List<Map<String, Object>> newsList) {
        AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(shopId);
        String token = wechatService.getAccessToken(appInfo);

        ////////////
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        /////////////

        NewsData newsData = new NewsData();
        List<Article> articleList = getNewsData(shopId, token, newsList, false);
        newsData.setArticles(articleList);

        //上传临时图文消息素材【订阅号与服务号认证后均可用】
        MediaReturn mediaReturn = WeixinMassMessageUtils.uploadNews(token, newsData);

        if (mediaReturn.getType().equals("news")) {
            // 图文素材上传后的mediaid
            String tuwenMediaId = mediaReturn.getMedia_id();
            log.info("tuwenMediaId ===================================== " + tuwenMediaId);
            Long msgId = WeixinMassMessageUtils.previewMassMessage(token, openid, "mpnews", tuwenMediaId);
            log.info("msgId ===================================== " + msgId);
            if (tuwenMediaId == null) {
                tuwenMediaId = "";
            }
            return tuwenMediaId;
        }
        return "";
    }

    /**
     * 上传素材图片到微信平台
     * 修改文件表mediaId
     * 返回素材的每个item项组装成list
     *
     * @param shopId
     * @param token
     * @param newsList
     * @return
     */
    private List<Article> getNewsData(Long shopId, String token, List<Map<String, Object>> newsList, Boolean isForever) {
        List<Article> list = new ArrayList<Article>();
        for (int i = 0; i < newsList.size(); i++) {
            Map<String, Object> item = newsList.get(i);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            String mediaId = (String) item.get("MEDIA_ID");
            Long fileId = (Long) item.get("MFILE_ID");
            String fileUrl = (String) item.get("FILE_URL");
            String author = (String) item.get("MAUTHOR");
            String title = (String) item.get("MTITLE");
            String sourceUrl = (String) item.get("MSOURCE_URL");
            String content = (String) item.get("MCONTENTWECHAT");
            //String content = (String)item.get("MCONTENT");
            String mdigest = (String) item.get("MDIGEST");
            Integer mshowCoverPic = (Integer) item.get("MSHOW_COVER_PIC");
            log.info("old media id = " + mediaId);
            //条目的缩略图mediaId失效，则重新上传
            if (StringUtils.isEmpty(mediaId)) {
                log.info("webUploadPath=" + webUploadPath);
                log.info("fileUrl=" + fileUrl);
                log.info("token=" + token);
                // 转换访问路径为物理路径 - start
                String prefix = staticUnionPath.substring(0, staticUnionPath.lastIndexOf("/"));// "/res"
                int index = fileUrl.lastIndexOf(prefix) + prefix.length();
                fileUrl = fileUrl.substring(index);
                fileUrl = webUploadPath + fileUrl;
                // 转换访问路径为物理路径 - end
                if (isForever) {
                    mediaId = WeixinMediaUtils.addMaterial(token, "image", fileUrl);//上传图片到微信平台(永久素材)
                } else {
                    MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", fileUrl);//上传图片到微信平台(临时素材)
                    mediaId = mediaReturn.getMedia_id();
                }

                /*****将文件表的mediaId赋值**start****/
                ShopFile shopFile = new ShopFile();
                shopFile.setFileId(fileId);
                shopFile.setShopId(shopId);
                shopFile.setMediaId(mediaId);
                shopFile.setMediaUpDt(new Date());
                shopFileMapper.updateByPrimaryKeySelective(shopFile);//赋值文件mediaId
                log.info("new media id = " + mediaId);
                /*****将文件表的mediaId赋值**end****/
            }

            Article a = new Article();
            a.setThumb_media_id(mediaId);
            a.setAuthor(author);
            a.setTitle(title);
            a.setContent_source_url(sourceUrl);
            a.setContent(content);
            a.setDigest(mdigest);
            a.setShow_cover_pic(mshowCoverPic + "");
            list.add(a);
        }
        return list;
    }

    /**
     * 群发信息
     *
     * @param shopId
     * @param is_to_all 是否全部发送
     * @param touser    用户列表
     * @param newsList  图文消息列表
     * @return MassRetuen
     */
    @Override
    public Map<String, Object> sendMassMessage(Long shopId, boolean is_to_all, List<String> touser, List<Map<String, Object>> newsList) {
        Map<String, Object> sendResultMap = new HashMap<String, Object>();
        String token = wechatService.getAccessToken(shopId);

        log.info("<<<<<<<<<<<<<<<<<<获取该图文消息在微信公众平台端的WX_MSG_ID<<<<<<<<<<<<<<<<<<<<<<<");
        // 获取当前图文素材上传到微信端wxMsgId，列表中肯定存在item，取第一条获得wxMsgId
        Map<String, Object> item0 = newsList.get(0);
        String wxMsgId = (String) item0.get("WX_MSG_ID");
        log.info("<<<<<<<<<<<<<<<<<<该图文消息在微信公众平台端的WX_MSG_ID为<<<<<<<<<<<<<<<<<<<<<<<<" + wxMsgId);

        // wxMsgId不存在说明该图文没有上传过微信公众平台，则上传发送
        if (StringUtils.isEmpty(wxMsgId)) {
            NewsData newsData = new NewsData();
            List<Article> list = new ArrayList<Article>();
            for (int i = 0; i < newsList.size(); i++) {
                Map<String, Object> item = newsList.get(i);
                log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                String mediaId = (String) item.get("MEDIA_ID");
                Long fileId = (Long) item.get("MFILE_ID");
                String fileUrl = (String) item.get("FILE_URL");
                String author = (String) item.get("MAUTHOR");
                String title = (String) item.get("MTITLE");
                String sourceUrl = (String) item.get("MSOURCE_URL");
                String content = (String) item.get("MCONTENTWECHAT");
                //String content = (String)item.get("MCONTENT");
                String mdigest = (String) item.get("MDIGEST");
                Integer mshowCoverPic = (Integer) item.get("MSHOW_COVER_PIC");
                log.info("old media id = " + mediaId);
                //条目的缩略图mediaId失效，则重新上传
                if (StringUtils.isEmpty(mediaId)) {
                    log.info("webUploadPath=" + webUploadPath);
                    log.info("fileUrl=" + fileUrl);
                    log.info("token=" + token);
                    // 转换访问路径为物理路径 - start
                    String prefix = staticUnionPath.substring(0, staticUnionPath.lastIndexOf("/"));// "/res"
                    int index = fileUrl.lastIndexOf(prefix) + prefix.length();
                    fileUrl = fileUrl.substring(index);
                    fileUrl = webUploadPath + fileUrl;
                    // 转换访问路径为物理路径 - end
                    MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", fileUrl);
                    mediaId = mediaReturn.getMedia_id();

                    /*****将文件表的mediaId赋值**start****/
                    ShopFile shopFile = new ShopFile();
                    shopFile.setFileId(fileId);
                    shopFile.setShopId(shopId);
                    shopFile.setMediaId(mediaId);
                    shopFile.setMediaUpDt(new Date());
                    shopFileMapper.updateByPrimaryKeySelective(shopFile);
                    log.info("new media id = " + mediaId);
                    /*****将文件表的mediaId赋值**end****/
                }

                Article a = new Article();
                a.setThumb_media_id(mediaId);
                a.setAuthor(author);
                a.setTitle(title);
                a.setContent_source_url(sourceUrl);
                a.setContent(content);
                a.setDigest(mdigest);
                a.setShow_cover_pic(mshowCoverPic + "");
                list.add(a);
            }

            newsData.setArticles(list);

            //上传图文消息素材【订阅号与服务号认证后均可用】
            MediaReturn mediaReturn = WeixinMassMessageUtils.uploadNews(token, newsData);
            sendResultMap.put("MediaReturn", mediaReturn);

            if (mediaReturn.getType().equals("news")) {
                NewsMassDomain news = new NewsMassDomain();
                news.setMpnews(new Media(mediaReturn.getMedia_id()));
                news.setTouser(touser);

                MassReturn massRetuen = null;
                if (is_to_all) {// 给全部关注用户（客户）全部发送
                    log.info("WeixinMassMessageUtils.sendMpnewsAll=============");
                    massRetuen = WeixinMassMessageUtils.sendMpnewsAll(token, true, null, mediaReturn.getMedia_id());
                } else {// 选择关注用户（客户）发送
                    log.info("WeixinMassMessageUtils.sendMass=============");
                    massRetuen = WeixinMassMessageUtils.sendMass(token, news);
                }
                sendResultMap.put("MassReturn", massRetuen);
                return sendResultMap;
            }
            return sendResultMap;
        } else {// wxMsgId存在则直接发送
            NewsMassDomain news = new NewsMassDomain();
            news.setMpnews(new Media(wxMsgId));
            news.setTouser(touser);
            MassReturn massRetuen = null;
            if (is_to_all) {// 给全部关注用户（客户）全部发送
                log.info("WeixinMassMessageUtils.sendMpnewsAll=============");
                massRetuen = WeixinMassMessageUtils.sendMpnewsAll(token, true, null, wxMsgId);
            } else {// 选择关注用户（客户）发送
                log.info("WeixinMassMessageUtils.sendMass=============");
                massRetuen = WeixinMassMessageUtils.sendMass(token, news);
            }
            sendResultMap.put("MassReturn", massRetuen);
            return sendResultMap;
        }
    }


    @Override
    //@Transactional(transactionManager = "primaryTransactionManager",rollbackFor = Exception.class)
    @Transactional
    public void saveSendResult(Map<String, Object> map) {
        shopWxNewsHeadMapper.saveSendResultHead(map);
        shopWxNewsCustMapper.saveSendResultNewsCust(map);
    }

    /**
     * 保存预览结果
     *
     * @param map
     */
    public void savePreviewResult(Map<String, Object> map) {
        shopWxNewsHeadMapper.saveSendResultHead(map);
    }

    /**
     * 群发消息：图片、文本、视频、音频
     *
     * @param shopId
     * @param is_to_all 是否对全部用户发送 false-否 true-是
     * @param touser    目标用户的OPENID列表
     * @param form
     * @return
     */
    public Map<String, Object> massSend(Long shopId, boolean is_to_all, List<String> touser, SenNewsForm form) {
        Map<String, Object> sendResultMap = new HashMap<String, Object>();
        // 获取token
        String token = wechatService.getAccessToken(shopId);
        // 按类别发送
        int sendFileType = form.getSendFileType();
        MassReturn massRetuen = null;
        if (sendFileType == BizConstants.MSG_TYPE_IMAGE) {
            // 群发图片消息
            if (is_to_all) {// 全部用户
                massRetuen = WeixinMassMessageUtils.sendImageAll(token, true, null, form.getSendMediaId());
            } else {// 指定用户
                ImageMassDomain imageMass = new ImageMassDomain();
                Media media = new Media();
                media.setMedia_id(form.getSendMediaId());
                imageMass.setImage(media);
                imageMass.setTouser(touser);
                log.info("Mass send IMAGE:::massSend:::WeixinMassMessageUtils.sendMass:::form.getSendMediaId():::=============" + form.getSendMediaId());
                massRetuen = WeixinMassMessageUtils.sendMass(token, imageMass);
            }
        } else if (sendFileType == BizConstants.MSG_TYPE_TEXT) {
            // 群发文本消息
            if (is_to_all) {// 全部用户
                massRetuen = WeixinMassMessageUtils.sendTextAll(token, true, null, form.getSendText());
            } else {// 指定用户
                TextMassDomian textMass = new TextMassDomian();
                Text text = new Text(form.getSendText());
                textMass.setText(text);
                textMass.setTouser(touser);
                log.info("Mass send TEXT:::massSend:::WeixinMassMessageUtils.sendMass:::form.getSendText():::=============" + form.getSendText());
                massRetuen = WeixinMassMessageUtils.sendMass(token, textMass);
            }
        } else if (sendFileType == BizConstants.MSG_TYPE_VOICE) {
            // 群发音频消息
            if (is_to_all) {// 全部用户
                massRetuen = WeixinMassMessageUtils.sendVoiceAll(token, true, null, form.getSendMediaId());
            } else {// 指定用户
                VoiceMassDomain voiceMass = new VoiceMassDomain();
                Media media = new Media();
                media.setMedia_id(form.getSendMediaId());
                voiceMass.setVoice(media);
                voiceMass.setTouser(touser);
                log.info("Mass send VOICE:::massSend:::WeixinMassMessageUtils.sendMass:::form.getSendMediaId():::=============" + form.getSendMediaId());
                massRetuen = WeixinMassMessageUtils.sendMass(token, voiceMass);
            }
        } else if (sendFileType == BizConstants.MSG_TYPE_VIDEO) {
            // 群发视频消息
            if (is_to_all) {// 全部用户
                massRetuen = WeixinMassMessageUtils.sendVideoAll(token, true, null, form.getSendMediaId());
            } else {// 指定用户
                VideoMassDomain videoMass = new VideoMassDomain();
                VideoMedia media = new VideoMedia();
                media.setMedia_id(form.getSendMediaId());
                videoMass.setVideo(media);
                videoMass.setTouser(touser);
                log.info("Mass send VOICE:::massSend:::WeixinMassMessageUtils.sendMass:::form.getSendMediaId():::=============" + form.getSendMediaId());
                massRetuen = WeixinMassMessageUtils.sendMass(token, videoMass);
            }
        }
        sendResultMap.put("MassReturn", massRetuen);
        return sendResultMap;
    }
}
