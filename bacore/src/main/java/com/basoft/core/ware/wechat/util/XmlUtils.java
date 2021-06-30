package com.basoft.core.ware.wechat.util;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.basoft.core.ware.wechat.domain.msg.resp.Article;
import com.basoft.core.ware.wechat.domain.msg.resp.Image;
import com.basoft.core.ware.wechat.domain.msg.resp.ImageMessage;
import com.basoft.core.ware.wechat.domain.msg.resp.Music;
import com.basoft.core.ware.wechat.domain.msg.resp.MusicMessage;
import com.basoft.core.ware.wechat.domain.msg.resp.NewsMessage;
import com.basoft.core.ware.wechat.domain.msg.resp.TextMessage;
import com.basoft.core.ware.wechat.domain.msg.resp.Video;
import com.basoft.core.ware.wechat.domain.msg.resp.VideoMessage;
import com.basoft.core.ware.wechat.domain.msg.resp.Voice;
import com.basoft.core.ware.wechat.domain.msg.resp.VoiceMessage;
import com.basoft.core.ware.wechat.domain.pay.PaymentNotify;
import com.basoft.core.ware.wechat.exception.InvalidSignException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * xml工具类
 */
public class XmlUtils {
	private static final transient Log logger = LogFactory.getLog(XmlUtils.class);

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml2Map(String xml) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Document document = DocumentHelper.parseText(xml);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
    
    
    /*
    public static UnifiedOrderResponse parseXml2UnifiedOrderResponse(String xml){
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", UnifiedOrderResponse.class);  
    	UnifiedOrderResponse returns = (UnifiedOrderResponse)xstream.fromXML(xml);
    	return returns;
    }
    
    public static PayRefundQueryResponse parseXml2PayRefundQueryResponse(String xml) {
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", PayRefundQueryResponse.class);  
    	PayRefundQueryResponse returns = (PayRefundQueryResponse)xstream.fromXML(xml);
    	return returns;
	}
    
    public static PayRefundResponse parseXml2PayRefundResponse(String xml) {
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", PayRefundResponse.class);  
    	PayRefundResponse returns = (PayRefundResponse)xstream.fromXML(xml);
    	return returns;
	}
    
    public static PayCloseOrderResponse parseXml2PayCloseOrderResponse(String xml) {
		// TODO Auto-generated method stub
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", PayCloseOrderResponse.class);  
    	PayCloseOrderResponse returns = (PayCloseOrderResponse)xstream.fromXML(xml);
    	return returns;
	}
    
    public static PayDownloadbillResponse parseXml2PayDownloadbillResponse(String xml) {
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", PayDownloadbillResponse.class);  
    	PayDownloadbillResponse returns = (PayDownloadbillResponse)xstream.fromXML(xml);
    	return returns;
	}
    
    public static OrderQueryResponse parseXml2OrderQueryResponse(String xml) {
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", OrderQueryResponse.class);  
    	OrderQueryResponse returns = (OrderQueryResponse)xstream.fromXML(xml);
    	return returns;
	}
    
    public static PaymentNotify parseXml2PaymentNotify(String xml){
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", PaymentNotify.class);  
    	PaymentNotify returns = (PaymentNotify)xstream.fromXML(xml);
    	return returns;
    }
    
    public static SendRedPackResponse parseXml2SendRedPackResponse(String xml) {
		// TODO Auto-generated method stub
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("xml", SendRedPackResponse.class);  
    	SendRedPackResponse returns = (SendRedPackResponse)xstream.fromXML(xml);
    	return returns;
	}*/
    
	public static <T> T parseXml2Obj(String xml, Class<T> clazz) {
		// TODO Auto-generated method stub
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("xml", clazz);
		T returns = (T) xstream.fromXML(xml);
		return returns;
	}

	public static XStream getMessageXStream() {
		return new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					// 对所有xml节点的转换都增加CDATA标记
					boolean cdata = true;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
					}

					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
	}

	/**
	 * 回复文本消息
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String messageToXml(TextMessage textMessage) {
		XStream xstream = getMessageXStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 回复图片消息
	 * 
	 * @param imageMessage
	 * @return
	 */
	public static String messageToXml(ImageMessage imageMessage) {
		XStream xstream = getMessageXStream();
		xstream.alias("xml", imageMessage.getClass());
		xstream.alias("Image", Image.class);
		return xstream.toXML(imageMessage);
	}

	/**
	 * 回复语音消息
	 * 
	 * @param voiceMessage
	 * @return
	 */
	public static String messageToXml(VoiceMessage voiceMessage) {
		XStream xstream = getMessageXStream();
		xstream.alias("xml", voiceMessage.getClass());
		xstream.alias("Voice", Voice.class);
		return xstream.toXML(voiceMessage);
	}

	/**
	 * 回复视频消息
	 * 
	 * @param videoMessage
	 * @return
	 */
	public static String messageToXml(VideoMessage videoMessage) {
		XStream xstream = getMessageXStream();
		xstream.alias("xml", videoMessage.getClass());
		xstream.alias("Voice", Video.class);
		return xstream.toXML(videoMessage);
	}

	/**
	 * 回复音乐消息
	 * 
	 * @param musicMessage
	 * @return
	 */
	public static String messageToXml(MusicMessage musicMessage) {
		XStream xstream = getMessageXStream();
		xstream.alias("xml", musicMessage.getClass());
		xstream.alias("Voice", Music.class);
		return xstream.toXML(musicMessage);
	}

	/**
	 * 回复图文消息
	 * 
	 * @param newsMessage
	 * @return
	 */
	public static String messageToXml(NewsMessage newsMessage) {
		XStream xstream = getMessageXStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	public static Map<String, String> checkXml(String xml) throws InvalidSignException {
		Map<String, String> map = new TreeMap<String, String>();
		Document document;
		try {
			document = DocumentHelper.parseText(xml);
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			List<Element> elementList = root.elements();
			// 遍历所有子节点
			for (Element e : elementList) {
				if (StringUtils.isNotEmpty(e.getText())) {
					map.put(e.getName(), e.getText());
				}
			}
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String key = "eX3fYhAbuJkLk80ZqF6sWmuzcDryulbR";
		String sign = map.get("sign");
		String sys_id = map.get("key");
		map.remove("sign");
		map.remove("key");
		System.out.println("============================================");
		String string1 = "";
		int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (i++ > 0) {
				string1 += "&";
			}
			string1 += entry.getKey() + "=" + entry.getValue();
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		System.out.println(string1);
		String stringSignTemp = string1 + "&key=" + key;
		logger.info("stringSignTemp==\n" + stringSignTemp);
		String newSign = EncryptUtils.MD5(stringSignTemp);
		logger.info("sys_id===" + sys_id);
		logger.info("key======" + key);
		logger.info("sign=====" + sign);
		logger.info("newSign==" + newSign);
		return map;
	}

    public static void main(String[] args) throws Exception {
    /*	String xml = "<notify><payment_type>1</payment_type><subject>测试20150421145726898</subject><trade_no>2015042100001000930069199526</trade_no><buyer_email>zhengtaifan@163.com</buyer_email><gmt_create>2015-04-21 14:57:39</gmt_create><notify_type>trade_status_sync</notify_type><quantity>1</quantity><out_trade_no>20150421145726898</out_trade_no><notify_time>2015-04-21 14:57:58</notify_time><seller_id>2088511923220870</seller_id><trade_status>TRADE_SUCCESS</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>0.01</total_fee><gmt_payment>2015-04-21 14:57:58</gmt_payment><seller_email>hzm@hsadchina.com</seller_email><price>0.01</price><buyer_id>2088102174401932</buyer_id><notify_id>02b54bc3cb7d733bb024833caf28e2ab76</notify_id><use_coupon>N</use_coupon></notify>";
    	
    	
    	XStream xstream = new XStream(new DomDriver());  
    	xstream.alias("notify", NotifyData.class);  
    	NotifyData returns = (NotifyData)xstream.fromXML(xml);
    	System.out.println(returns);*/
    	
    	
//    	ImageMessage imageMessage = new ImageMessage();
//    	imageMessage.setCreateTime(1111);
//    	imageMessage.setFromUserName("aaa");
//    	imageMessage.setToUserName("bbb");
//    	imageMessage.setMsgType("image");
//    	
//    	Image image = new Image();
//    	image.setMediaId("AAAAAAAAAAAAAAAAAAAADDFSF");
//    	
//    	imageMessage.setImage(image);
//    	String xml = messageToXml(imageMessage);
//    	
//    	System.out.println(xml);
    	
    /*	VoiceMessage voiceMessage = new VoiceMessage();
    	voiceMessage.setCreateTime(1111);
    	voiceMessage.setFromUserName("aaa");
    	voiceMessage.setToUserName("bbb");
    	voiceMessage.setMsgType("image");
    	Voice voice = new Voice();
    	voice.setMediaId("bbbbbbbbbbbbbbbb");
    	voiceMessage.setVoice(voice);
    	
    	System.out.println( messageToXml(voiceMessage));*/
    	
    /*	
    	String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg>"
    			+"<appid><![CDATA[wxf4ff15142f410758]]></appid><mch_id><![CDATA[1225226202]]></mch_id>"
    			+"<nonce_str><![CDATA[SC7uomr2N9it5ed8]]></nonce_str>"
    			+"<sign><![CDATA[2E98AFA036FE2C732D0B52A4D21808C7]]></sign>"
    			+"<aa><bb><![CDATA[2E98AFA036FE2C732D0B52A4D21808C7]]></bb></aa>"
    			+"<result_code><![CDATA[SUCCESS]]></result_code>"
    			+"<prepay_id><![CDATA[wx201502121947515e3ad276c10898190277]]></prepay_id>"
    			+"<trade_type><![CDATA[JSAPI]]></trade_type></xml>";
    	
		Map<String,String> result = parseXml2Map(xml);
		
		MapUtil.printMap2(result);
		*/
 
    	 String xml =   "<xml>																		"
    			+ "<appid><![CDATA[wxf4ff15142f410758]]></appid>								"
    			+ "<attach><![CDATA[total#@@#3000]]></attach>									"
    			+ "<bank_type><![CDATA[ICBC_CREDIT]]></bank_type>								"
    			+ "<cash_fee><![CDATA[1]]></cash_fee>											"
    			+ "<fee_type><![CDATA[CNY]]></fee_type>											"
    			+ "<is_subscribe><![CDATA[Y]]></is_subscribe>									"
    			+ "<key><![CDATA[8D6ED58C805242C8BDB129616163CB04]]></key>						"
    			+ "<mch_id><![CDATA[1225226202]]></mch_id>										"
    			+ "<nonce_str><![CDATA[hTK6QtNqp1VsPd0AfxG9mZ3MKlY4fCyo]]></nonce_str>			"
    			+ "<openid><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></openid>					"
    			+ "<out_trade_no><![CDATA[10015]]></out_trade_no>								"
    			+ "<result_code><![CDATA[SUCCESS]]></result_code>								"
    			+ "<return_code><![CDATA[SUCCESS]]></return_code>								"
    			+ "<sign><![CDATA[BD5BD6C6CF861DA6458967B13CCB42DC]]></sign>					"
    			+ "<time_end><![CDATA[20150303152822]]></time_end>								"
    			+ "<total_fee>1</total_fee>														"
    			+ "<trade_type><![CDATA[JSAPI]]></trade_type>									"
    			+ "<transaction_id><![CDATA[1008960276201503030024159657]]></transaction_id>	"
    			+ "</xml>																		";

     	/*PaymentNotify notify = parseXml2PaymentNotify(xml);
    	System.out.println(notify);*/
		PaymentNotify notify2 = parseXml2Obj(xml, PaymentNotify.class);
		System.out.println(notify2);
		// Map<String, String> map = parseXml2Map(xml);
		// Map<String,String> map = checkXml(xml);
	}
}
