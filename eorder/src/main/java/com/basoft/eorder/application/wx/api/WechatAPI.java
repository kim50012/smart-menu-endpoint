package com.basoft.eorder.application.wx.api;

import com.basoft.eorder.application.wx.http.HttpClientUtils;
import com.basoft.eorder.application.wx.http.WeixinErrorException;
import com.basoft.eorder.application.wx.model.DataItem;
import com.basoft.eorder.application.wx.model.QrcodeTicket;
import com.basoft.eorder.application.wx.model.SnsToken;
import com.basoft.eorder.application.wx.model.TemplateMessageReturn;
import com.basoft.eorder.application.wx.model.Token;
import com.basoft.eorder.application.wx.model.WeixinReturn;
import com.basoft.eorder.application.wx.model.wxuser.UserReturn;
import com.basoft.eorder.util.HttpClientUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

public class WechatAPI {
    private static final String BASE_URI = "https://api.weixin.qq.com";
    private static final String QRCODE_DOWNLOAD_URI = "https://mp.weixin.qq.com";
    private static final String PAY_WECHAT_URI = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // protected static final String MEDIA_URI = "http://file.api.weixin.qq.com";
    // protected static final String OAUTH_URI = "https://open.weixin.qq.com/connect/oauth2";

    private static RestTemplate restTemplate = HttpClientUtil.getInstance().restTemplate();

    // private String appid = "wx7202c9b1660cc48d";
    // private String secret = "c9f8e2371bd5e83efdb4a677a2ddfca7";

    private static class ApiInstance {
        private static final WechatAPI instance = new WechatAPI();
    }

    public static WechatAPI getInstance() {
        return ApiInstance.instance;
    }

    public Token token(String appid, String secret) {
        return restTemplate.postForObject(
                BASE_URI + "/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}",
                null,
                Token.class,
                appid, secret);
    }

    /**
     * 创建二维码
     *
     * @param access_token
     * @param qrcodeJson   json 数据 例如{"expire_seconds": 1800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
     * @return
     */
    private QrcodeTicket qrcodeCreate(String access_token, String qrcodeJson) {
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        HttpEntity<String> httpEntity = new HttpEntity<>(qrcodeJson, headers);
        ResponseEntity<QrcodeTicket> responseEntity = restTemplate.exchange(
                BASE_URI + "/cgi-bin/qrcode/create?access_token={access_token}",
                HttpMethod.POST,
                httpEntity,
                QrcodeTicket.class,
                access_token);
        return responseEntity.getBody();
    }

    /**
     * 创建二维码
     *
     * @param access_token
     * @param expire_seconds 该二维码有效时间，以秒为单位。 最大不超过1800秒。
     * @param action_name    二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
     * @param scene_id       场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * @return
     */
    private QrcodeTicket qrcodeCreate(String access_token, Integer expire_seconds, String action_name, long scene_id) {
        return qrcodeCreate(access_token, String.format("{" + (expire_seconds == null ? "%1$s" : "\"expire_seconds\": %1$s, ") + "\"action_name\": \"%2$s\", \"action_info\": {\"scene\": {\"scene_id\": %3$d}}}",
                expire_seconds == null ? "" : expire_seconds, action_name, scene_id));
    }

    /**
     * 创建临时二维码
     *
     * @param access_token
     * @param expire_seconds 不超过2592000秒
     * @param scene_id       场景值ID，32位非0整型
     * @return
     */
    public QrcodeTicket qrcodeCreateTemp(String access_token, int expire_seconds, long scene_id) {
        return qrcodeCreate(access_token, expire_seconds, "QR_SCENE", scene_id);
    }

    /**
     * 创建持久二维码
     *
     * @param access_token
     * @param scene_id     场景值ID 1-100000
     * @return
     */
    public QrcodeTicket qrcodeCreateFinal(String access_token, int scene_id) {
        return qrcodeCreate(access_token, null, "QR_LIMIT_SCENE", scene_id);
    }

    /**
     * 创建二维码
     *
     * @param access_token
     * @param expire_seconds 该二维码有效时间，以秒为单位。 最大不超过1800秒。
     * @param action_name    二维码类型，QR_STR_SCENE 为临时,QR_LIMIT_STR_SCENE 为永久
     * @param scene_str      场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * @return
     */
    private QrcodeTicket qrcodeCreate(String access_token, Integer expire_seconds, String action_name, String scene_str) {
        return qrcodeCreate(access_token, String.format("{" + (expire_seconds == null ? "%1$s" : "\"expire_seconds\": %1$s, ") + "\"action_name\": \"%2$s\", \"action_info\": {\"scene\": {\"scene_str\": \"%3$s\"}}}",
                expire_seconds == null ? "" : expire_seconds, action_name, scene_str));
    }

    /**
     * 创建临时二维码
     *
     * @param access_token
     * @param expire_seconds 不超过2592000秒
     * @param scene_str      场景值ID，32位非0整型
     * @return
     */
    public QrcodeTicket qrcodeCreateTemp(String access_token, int expire_seconds, String scene_str) {
        return qrcodeCreate(access_token, expire_seconds, "QR_STR_SCENE", scene_str);
    }

    /**
     * 创建持久二维码
     *
     * @param access_token
     * @param scene_str    场景值ID 1-100000
     * @return
     */
    public QrcodeTicket qrcodeCreateFinal(String access_token, String scene_str) {
        return qrcodeCreate(access_token, null, "QR_LIMIT_STR_SCENE", scene_str);
    }

    /**
     * 下载二维码
     * 视频文件不支持下载
     *
     * @param ticket 内部自动 UrlEncode
     * @return
     */
    public ResponseEntity<ByteArrayResource> showqrcode(String ticket) {
        try {
            return restTemplate.postForEntity(QRCODE_DOWNLOAD_URI + "/cgi-bin/showqrcode?ticket={ticket}",
                    null,
                    ByteArrayResource.class,
                    URLEncoder.encode(ticket, "utf-8"));
        } catch (UnsupportedEncodingException | RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getShowqrcodeUrl() {
        return QRCODE_DOWNLOAD_URI + "/cgi-bin/showqrcode?ticket=";
    }

    /**
     * 通过code换取网页授权access_token
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public SnsToken oauth2AccessToken(String appid, String secret, String code) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(BASE_URI + "/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code", null, String.class,
                appid, secret, code);
        // SnsToken token = restTemplate.postForObject(BASE_URI + "/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code", null, SnsToken.class,  appid, secret, code);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return objectMapper.readValue(responseEntity.getBody(), SnsToken.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getOpenId(String appid, String secret, String code) {
        // https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code={code}&grant_type=authorization_code", code, null, String.class);
        // SnsToken token = restTemplate.postForObject(BASE_URI + "/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code", null, SnsToken.class,  appid, secret, code);
        System.out.println(responseEntity.getBody());
    }

    /**
     * 客服接口 - 发送文本消息
     *
     * @param access_token 调用接口凭证
     * @param touser       普通用户openid
     * @param content      文本消息内容
     */
    //private static final String MESSAGE_CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    public WeixinReturn sendTextMsg(String access_token, String touser, String content) {
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + content + "\"}}";
        HttpEntity<String> httpEntity = new HttpEntity<>(postData, headers);
        ResponseEntity<WeixinReturn> responseEntity = restTemplate.exchange(
                BASE_URI + "/cgi-bin/message/custom/send?access_token={access_token}",
                HttpMethod.POST,
                httpEntity,
                WeixinReturn.class,
                access_token);
        return responseEntity.getBody();
    }

    /**
     * 发送模板消息<br>
     * 模板消息仅用于公众号向用户发送重要的服务通知，只能用于符合其要求的服务场景中，<br>
     * 如信用卡刷卡通知，商品购买成功通知等。不支持广告等营销类消息以及其它所有可能对用户造成骚扰的消息。<br>
     * <p>
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
    public static TemplateMessageReturn sendTemplateMessage(String access_token, String touser, String templateId, String linkUrl, String topcolor, Map<String, DataItem> data) {
        String url = BASE_URI + "/cgi-bin/message/template/send?access_token={access_token}";

        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", touser);
        jsonObject.put("template_id", templateId);
        if (!linkUrl.isEmpty()) {
            jsonObject.put("url", linkUrl);
        }
        jsonObject.put("topcolor", topcolor);
        jsonObject.put("data", data);
        String params = jsonObject.toString();

        HttpEntity<String> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<TemplateMessageReturn> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                TemplateMessageReturn.class,
                access_token);

        return responseEntity.getBody();
    }

    /**
     * 获取用户基本信息（包括UnionID机制）
     * <p>
     * 在关注者与公众号产生消息交互后，公众号可获得关注者的OpenID
     * （加密后的微信号，每个用户对每个公众号的OpenID是唯一的。
     * 对于不同公众号，同一用户的openid不同）。公众号可通过本接口来
     * 根据OpenID获取用户基本信息，包括昵称、头像、性别、所在城市、语言和关注时间。
     *
     * @param access_token 调用接口凭证
     * @param openid       普通用户的标识，对当前公众号唯一
     * @return UserReturn
     */
    public static UserReturn getUserInfoByOpenId(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token;
        url += "&openid=" + openid;
        // url += "&lang=" + "zh_CN";
        String result = HttpClientUtils.requestGet(url);
        UserReturn returns = new Gson().fromJson(result, UserReturn.class);
        if (returns.isError()) {
            throw new WeixinErrorException(returns);
        }
        return returns;
    }
}