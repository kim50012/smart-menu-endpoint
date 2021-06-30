package com.basoft.core.ware.wechat.consant;

public enum TemplateMessageAdvice {
	CUST_LEVEL_CHANGE("OPENTM204539634",1,"客户等级变更通知","/front/customer/myPage.htm","恭喜，您的会员等级已升级！","感谢您的使用"),
	MILEAGE_CHANGE("OPENTM201838927",2,"积分变动通知","/front/customer/myPage.htm","恭喜您，获得本店的奖励积分！","感谢您的使用"),
	RED_PACK_SEND("OPENTM201477950",3,"红包领取提醒","","",""),
	VOUCHER_MATURITY_REMIND("TM00770",4,"优惠券到期提醒","/front/customer/myPage.htm","",""),
	COURSE_APPLY_RESULT("TM00186",5,"报名结果通知","/front/human/applyMessage/entryDetail.htm","",""),
	COURSE_APPROVED("OPENTM206165287",6,"活动报名成功通知","/front/human/applyMessage/entryDetail.htm","",""),
	NOTICE_PUBLISH("OPENTM204737328",7,"公告发布通知","","",""),
	TASK_COMPLETE("OPENTM204600544",8,"任务完成通知","","",""),
	NEW_MEMBER_JOIN("OPENTM207940882",9,"新会员加入提醒","","",""),
	PURCHASE_SUCCESS_NOTICE("TM00247",10,"购买成功通知","","",""),
	MEMBER_BINDING_NOTICE("OPENTM201500859",11,"绑定会员通知","","",""),
	EVENT_FINISHED_NOTICE("OPENTM200731023",12,"活动结束通知","","",""),
	WINNING_RESULT_NOTICE("OPENTM204632492",13,"中奖结果通知","","","");
	
	private String id;
	private int type;
	private String title;
	private String url;
	private String first;
	private String remark;
	
	/**
	 * 创建一个新的实例 TemplateShortId.
	 *
	 * @param id
	 * @param type
	 * @param title
	 * @param url
	 * @param first
	 * @param remark
	 */
	private TemplateMessageAdvice(String id,Integer type, String title, String url, String first, String remark) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.url = url;
		this.first = first;
		this.remark = remark;
	}

	/*public static String getDesc(String id) {
		for (TemplateMessageID c : TemplateMessageID.values()) {
			if (c.getId().equals(id)) {
				return c.desc;
			}
		}
		return "";
	}*/
	
	public String getTitle() {
		return title;
	}

	public int getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getFirst() {
		return first;
	}

	public String getRemark() {
		return remark;
	}

	public static void main(String[] args) {
		TemplateMessageAdvice id = TemplateMessageAdvice.CUST_LEVEL_CHANGE;
		System.out.println(id.getId());
	}
}
