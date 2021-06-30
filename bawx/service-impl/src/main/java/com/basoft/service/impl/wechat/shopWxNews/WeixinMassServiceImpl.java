package com.basoft.service.impl.wechat.shopWxNews;

import com.basoft.core.ware.wechat.domain.mass.*;
import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.ware.wechat.util.PropertiesUtils;
import com.basoft.core.ware.wechat.util.WeixinMassMessageUtils;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.service.dao.shop.ShopFileMapper;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.shopWxNews.WeixinMassService;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.entity.shop.ShopFileKey;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:42 2018/5/10
 **/
@Service
@Slf4j
public class WeixinMassServiceImpl implements WeixinMassService{

    @Autowired
    private WechatService wechatService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopFileMapper shopFileMapper;

    @Override
    public void previewMass(Long shopId, String openid, List<Map<String, Object>> newsList) {
        String mediaId = "";

        AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(shopId);
        String token = wechatService.getAccessToken(appInfo);

        ////////////
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        /////////////

        NewsData newsData = new NewsData();

        List<Article> list = new ArrayList<Article>();

        for (int i = 0; i < newsList.size(); i++) {
            Map<String,Object> item = newsList.get(i);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            mediaId = (String)item.get("MEDIA_ID");
            Long fileId = (Long)item.get("MFILE_ID");
            String fileUrl = (String)item.get("FILE_URL");
            String author = (String)item.get("MAUTHOR");
            String title = (String)item.get("MTITLE");
            String sourceUrl = (String)item.get("MSOURCE_URL");
            String content = (String)item.get("MCONTENTWECHAT");
//			String content = (String)item.get("MCONTENT");
            String mdigest = (String)item.get("MDIGEST");
            Integer mshowCoverPic = (Integer)item.get("MSHOW_COVER_PIC");
            log.info("old media id = " + mediaId);
            //mediaId失效，重新上传
            if(StringUtils.isEmpty(mediaId)){
                String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
                log.info("uploadBaseDir=" + uploadBaseDir);
                log.info("fileUrl=" + fileUrl);
                log.info("token=" + token);
                MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", uploadBaseDir + fileUrl);
                mediaId = mediaReturn.getMedia_id();
                //weixinMassDao.updateMediaId(fileId, mediaId);

                /*****将文件表的mediaId赋值**start****/
                ShopFileKey key = new ShopFileKey();
                key.setFileId(fileId);
                key.setShopId(shopId);
                ShopFile shopFile = shopFileMapper.selectByPrimaryKey(key);
                shopFile.setMediaId(mediaId);
                shopFile.setMediaUpDt(new Date());
                shopFileMapper.updateByPrimaryKeySelective(shopFile);
                /*****将文件表的mediaId赋值**end****/
                log.info("new media id = " + mediaId);

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
        MediaReturn mediaReturn = WeixinMassMessageUtils.uploadNews(token,newsData);

        if(mediaReturn.getType().equals("news")){

            mediaId = mediaReturn.getMedia_id();

            Long msgId = WeixinMassMessageUtils.previewMassMessage(token, openid, "mpnews", mediaId);

            log.info("msgId = " + msgId);

        }
    }

    /**
     * 群发信息
     * @param shopId
     * @param is_to_all 是否全部发送
     * @param touser 用户列表
     * @param newsList 图文消息列表
     * @return MassRetuen
     */
    public MassReturn sendMass(Long shopId,  boolean is_to_all,List<String> touser, List<Map<String, Object>> newsList){
        String mediaId = "";
        AppInfoWithBLOBs appInfo = shopService.getAppInfoByShopId(shopId);
        String token = wechatService.getAccessToken(appInfo);

        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        NewsData newsData = new NewsData();

        List<Article> list = new ArrayList<Article>();

        for (int i = 0; i < newsList.size(); i++) {
            Map<String,Object> item = newsList.get(i);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            mediaId = (String)item.get("MEDIA_ID");
            Long fileId = (Long)item.get("MFILE_ID");
            String fileUrl = (String)item.get("FILE_URL");
            String author = (String)item.get("MAUTHOR");
            String title = (String)item.get("MTITLE");
            String sourceUrl = (String)item.get("MSOURCE_URL");
            String content = (String)item.get("MCONTENTWECHAT");
//			String content = (String)item.get("MCONTENT");
            String mdigest = (String)item.get("MDIGEST");
            Integer mshowCoverPic = (Integer)item.get("MSHOW_COVER_PIC");
            log.info("old media id = " + mediaId);
            //mediaId失效，重新上传
            if(StringUtils.isEmpty(mediaId)){
                String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
                log.info("uploadBaseDir=" + uploadBaseDir);
                log.info("fileUrl=" + fileUrl);
                log.info("token=" + token);
                MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", uploadBaseDir + fileUrl);
                mediaId = mediaReturn.getMedia_id();

                /*****将文件表的mediaId赋值**start****/
                ShopFileKey key = new ShopFileKey();
                key.setFileId(fileId);
                key.setShopId(shopId);
                ShopFile shopFile = shopFileMapper.selectByPrimaryKey(key);
                shopFile.setMediaId(mediaId);
                shopFile.setMediaUpDt(new Date());
                shopFileMapper.updateByPrimaryKeySelective(shopFile);
                /*****将文件表的mediaId赋值**end****/

                log.info("new media id = " + mediaId);
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
        MediaReturn mediaReturn = WeixinMassMessageUtils.uploadNews(token,newsData);

        if(mediaReturn.getType().equals("news")){

            NewsMassDomain news = new NewsMassDomain();
            news.setMpnews(new Media(mediaReturn.getMedia_id()));
            news.setTouser(touser);

            MassReturn massRetuen = null;
            if(is_to_all){
                log.info("WeixinMassMessageUtils.sendMpnewsAll=============");
                massRetuen = WeixinMassMessageUtils.sendMpnewsAll(token, true, null, mediaReturn.getMedia_id());
            }else{
                log.info("WeixinMassMessageUtils.sendMass=============");
                massRetuen = WeixinMassMessageUtils.sendMass(token, news);
            }

            return massRetuen;

        }

        return null;

    }
}
