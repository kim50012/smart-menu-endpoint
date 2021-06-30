package com.basoft.service.entity.wechat.wxMessage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WxMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WxMessageExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSysIdIsNull() {
            addCriterion("SYS_ID is null");
            return (Criteria) this;
        }

        public Criteria andSysIdIsNotNull() {
            addCriterion("SYS_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSysIdEqualTo(String value) {
            addCriterion("SYS_ID =", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdNotEqualTo(String value) {
            addCriterion("SYS_ID <>", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdGreaterThan(String value) {
            addCriterion("SYS_ID >", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdGreaterThanOrEqualTo(String value) {
            addCriterion("SYS_ID >=", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdLessThan(String value) {
            addCriterion("SYS_ID <", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdLessThanOrEqualTo(String value) {
            addCriterion("SYS_ID <=", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdLike(String value) {
            addCriterion("SYS_ID like", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdNotLike(String value) {
            addCriterion("SYS_ID not like", value, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdIn(List<String> values) {
            addCriterion("SYS_ID in", values, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdNotIn(List<String> values) {
            addCriterion("SYS_ID not in", values, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdBetween(String value1, String value2) {
            addCriterion("SYS_ID between", value1, value2, "sysId");
            return (Criteria) this;
        }

        public Criteria andSysIdNotBetween(String value1, String value2) {
            addCriterion("SYS_ID not between", value1, value2, "sysId");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNull() {
            addCriterion("MSG_ID is null");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNotNull() {
            addCriterion("MSG_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMsgIdEqualTo(String value) {
            addCriterion("MSG_ID =", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotEqualTo(String value) {
            addCriterion("MSG_ID <>", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThan(String value) {
            addCriterion("MSG_ID >", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThanOrEqualTo(String value) {
            addCriterion("MSG_ID >=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThan(String value) {
            addCriterion("MSG_ID <", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThanOrEqualTo(String value) {
            addCriterion("MSG_ID <=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLike(String value) {
            addCriterion("MSG_ID like", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotLike(String value) {
            addCriterion("MSG_ID not like", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdIn(List<String> values) {
            addCriterion("MSG_ID in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotIn(List<String> values) {
            addCriterion("MSG_ID not in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdBetween(String value1, String value2) {
            addCriterion("MSG_ID between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotBetween(String value1, String value2) {
            addCriterion("MSG_ID not between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andFromTypeIsNull() {
            addCriterion("FROM_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andFromTypeIsNotNull() {
            addCriterion("FROM_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andFromTypeEqualTo(Byte value) {
            addCriterion("FROM_TYPE =", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotEqualTo(Byte value) {
            addCriterion("FROM_TYPE <>", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeGreaterThan(Byte value) {
            addCriterion("FROM_TYPE >", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("FROM_TYPE >=", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLessThan(Byte value) {
            addCriterion("FROM_TYPE <", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLessThanOrEqualTo(Byte value) {
            addCriterion("FROM_TYPE <=", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeIn(List<Byte> values) {
            addCriterion("FROM_TYPE in", values, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotIn(List<Byte> values) {
            addCriterion("FROM_TYPE not in", values, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeBetween(Byte value1, Byte value2) {
            addCriterion("FROM_TYPE between", value1, value2, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("FROM_TYPE not between", value1, value2, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromUserIsNull() {
            addCriterion("FROM_USER is null");
            return (Criteria) this;
        }

        public Criteria andFromUserIsNotNull() {
            addCriterion("FROM_USER is not null");
            return (Criteria) this;
        }

        public Criteria andFromUserEqualTo(String value) {
            addCriterion("FROM_USER =", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserNotEqualTo(String value) {
            addCriterion("FROM_USER <>", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserGreaterThan(String value) {
            addCriterion("FROM_USER >", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserGreaterThanOrEqualTo(String value) {
            addCriterion("FROM_USER >=", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserLessThan(String value) {
            addCriterion("FROM_USER <", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserLessThanOrEqualTo(String value) {
            addCriterion("FROM_USER <=", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserLike(String value) {
            addCriterion("FROM_USER like", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserNotLike(String value) {
            addCriterion("FROM_USER not like", value, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserIn(List<String> values) {
            addCriterion("FROM_USER in", values, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserNotIn(List<String> values) {
            addCriterion("FROM_USER not in", values, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserBetween(String value1, String value2) {
            addCriterion("FROM_USER between", value1, value2, "fromUser");
            return (Criteria) this;
        }

        public Criteria andFromUserNotBetween(String value1, String value2) {
            addCriterion("FROM_USER not between", value1, value2, "fromUser");
            return (Criteria) this;
        }

        public Criteria andToUserIsNull() {
            addCriterion("TO_USER is null");
            return (Criteria) this;
        }

        public Criteria andToUserIsNotNull() {
            addCriterion("TO_USER is not null");
            return (Criteria) this;
        }

        public Criteria andToUserEqualTo(String value) {
            addCriterion("TO_USER =", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotEqualTo(String value) {
            addCriterion("TO_USER <>", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserGreaterThan(String value) {
            addCriterion("TO_USER >", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserGreaterThanOrEqualTo(String value) {
            addCriterion("TO_USER >=", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserLessThan(String value) {
            addCriterion("TO_USER <", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserLessThanOrEqualTo(String value) {
            addCriterion("TO_USER <=", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserLike(String value) {
            addCriterion("TO_USER like", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotLike(String value) {
            addCriterion("TO_USER not like", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserIn(List<String> values) {
            addCriterion("TO_USER in", values, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotIn(List<String> values) {
            addCriterion("TO_USER not in", values, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserBetween(String value1, String value2) {
            addCriterion("TO_USER between", value1, value2, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotBetween(String value1, String value2) {
            addCriterion("TO_USER not between", value1, value2, "toUser");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNull() {
            addCriterion("MSG_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNotNull() {
            addCriterion("MSG_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeEqualTo(String value) {
            addCriterion("MSG_TYPE =", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotEqualTo(String value) {
            addCriterion("MSG_TYPE <>", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThan(String value) {
            addCriterion("MSG_TYPE >", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThanOrEqualTo(String value) {
            addCriterion("MSG_TYPE >=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThan(String value) {
            addCriterion("MSG_TYPE <", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThanOrEqualTo(String value) {
            addCriterion("MSG_TYPE <=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLike(String value) {
            addCriterion("MSG_TYPE like", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotLike(String value) {
            addCriterion("MSG_TYPE not like", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIn(List<String> values) {
            addCriterion("MSG_TYPE in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotIn(List<String> values) {
            addCriterion("MSG_TYPE not in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeBetween(String value1, String value2) {
            addCriterion("MSG_TYPE between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotBetween(String value1, String value2) {
            addCriterion("MSG_TYPE not between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andReceiveDtIsNull() {
            addCriterion("RECEIVE_DT is null");
            return (Criteria) this;
        }

        public Criteria andReceiveDtIsNotNull() {
            addCriterion("RECEIVE_DT is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveDtEqualTo(Date value) {
            addCriterion("RECEIVE_DT =", value, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtNotEqualTo(Date value) {
            addCriterion("RECEIVE_DT <>", value, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtGreaterThan(Date value) {
            addCriterion("RECEIVE_DT >", value, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtGreaterThanOrEqualTo(Date value) {
            addCriterion("RECEIVE_DT >=", value, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtLessThan(Date value) {
            addCriterion("RECEIVE_DT <", value, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtLessThanOrEqualTo(Date value) {
            addCriterion("RECEIVE_DT <=", value, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtIn(List<Date> values) {
            addCriterion("RECEIVE_DT in", values, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtNotIn(List<Date> values) {
            addCriterion("RECEIVE_DT not in", values, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtBetween(Date value1, Date value2) {
            addCriterion("RECEIVE_DT between", value1, value2, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andReceiveDtNotBetween(Date value1, Date value2) {
            addCriterion("RECEIVE_DT not between", value1, value2, "receiveDt");
            return (Criteria) this;
        }

        public Criteria andMediaIdIsNull() {
            addCriterion("MEDIA_ID is null");
            return (Criteria) this;
        }

        public Criteria andMediaIdIsNotNull() {
            addCriterion("MEDIA_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMediaIdEqualTo(String value) {
            addCriterion("MEDIA_ID =", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotEqualTo(String value) {
            addCriterion("MEDIA_ID <>", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdGreaterThan(String value) {
            addCriterion("MEDIA_ID >", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdGreaterThanOrEqualTo(String value) {
            addCriterion("MEDIA_ID >=", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdLessThan(String value) {
            addCriterion("MEDIA_ID <", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdLessThanOrEqualTo(String value) {
            addCriterion("MEDIA_ID <=", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdLike(String value) {
            addCriterion("MEDIA_ID like", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotLike(String value) {
            addCriterion("MEDIA_ID not like", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdIn(List<String> values) {
            addCriterion("MEDIA_ID in", values, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotIn(List<String> values) {
            addCriterion("MEDIA_ID not in", values, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdBetween(String value1, String value2) {
            addCriterion("MEDIA_ID between", value1, value2, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotBetween(String value1, String value2) {
            addCriterion("MEDIA_ID not between", value1, value2, "mediaId");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNull() {
            addCriterion("PIC_URL is null");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNotNull() {
            addCriterion("PIC_URL is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrlEqualTo(String value) {
            addCriterion("PIC_URL =", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotEqualTo(String value) {
            addCriterion("PIC_URL <>", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThan(String value) {
            addCriterion("PIC_URL >", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("PIC_URL >=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThan(String value) {
            addCriterion("PIC_URL <", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThanOrEqualTo(String value) {
            addCriterion("PIC_URL <=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLike(String value) {
            addCriterion("PIC_URL like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotLike(String value) {
            addCriterion("PIC_URL not like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIn(List<String> values) {
            addCriterion("PIC_URL in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotIn(List<String> values) {
            addCriterion("PIC_URL not in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlBetween(String value1, String value2) {
            addCriterion("PIC_URL between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotBetween(String value1, String value2) {
            addCriterion("PIC_URL not between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andFormatIsNull() {
            addCriterion("FORMAT is null");
            return (Criteria) this;
        }

        public Criteria andFormatIsNotNull() {
            addCriterion("FORMAT is not null");
            return (Criteria) this;
        }

        public Criteria andFormatEqualTo(String value) {
            addCriterion("FORMAT =", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotEqualTo(String value) {
            addCriterion("FORMAT <>", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatGreaterThan(String value) {
            addCriterion("FORMAT >", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatGreaterThanOrEqualTo(String value) {
            addCriterion("FORMAT >=", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatLessThan(String value) {
            addCriterion("FORMAT <", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatLessThanOrEqualTo(String value) {
            addCriterion("FORMAT <=", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatLike(String value) {
            addCriterion("FORMAT like", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotLike(String value) {
            addCriterion("FORMAT not like", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatIn(List<String> values) {
            addCriterion("FORMAT in", values, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotIn(List<String> values) {
            addCriterion("FORMAT not in", values, "format");
            return (Criteria) this;
        }

        public Criteria andFormatBetween(String value1, String value2) {
            addCriterion("FORMAT between", value1, value2, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotBetween(String value1, String value2) {
            addCriterion("FORMAT not between", value1, value2, "format");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdIsNull() {
            addCriterion("THUMB_MEDIA_ID is null");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdIsNotNull() {
            addCriterion("THUMB_MEDIA_ID is not null");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdEqualTo(String value) {
            addCriterion("THUMB_MEDIA_ID =", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdNotEqualTo(String value) {
            addCriterion("THUMB_MEDIA_ID <>", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdGreaterThan(String value) {
            addCriterion("THUMB_MEDIA_ID >", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdGreaterThanOrEqualTo(String value) {
            addCriterion("THUMB_MEDIA_ID >=", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdLessThan(String value) {
            addCriterion("THUMB_MEDIA_ID <", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdLessThanOrEqualTo(String value) {
            addCriterion("THUMB_MEDIA_ID <=", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdLike(String value) {
            addCriterion("THUMB_MEDIA_ID like", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdNotLike(String value) {
            addCriterion("THUMB_MEDIA_ID not like", value, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdIn(List<String> values) {
            addCriterion("THUMB_MEDIA_ID in", values, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdNotIn(List<String> values) {
            addCriterion("THUMB_MEDIA_ID not in", values, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdBetween(String value1, String value2) {
            addCriterion("THUMB_MEDIA_ID between", value1, value2, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andThumbMediaIdNotBetween(String value1, String value2) {
            addCriterion("THUMB_MEDIA_ID not between", value1, value2, "thumbMediaId");
            return (Criteria) this;
        }

        public Criteria andLocationXIsNull() {
            addCriterion("LOCATION_X is null");
            return (Criteria) this;
        }

        public Criteria andLocationXIsNotNull() {
            addCriterion("LOCATION_X is not null");
            return (Criteria) this;
        }

        public Criteria andLocationXEqualTo(BigDecimal value) {
            addCriterion("LOCATION_X =", value, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXNotEqualTo(BigDecimal value) {
            addCriterion("LOCATION_X <>", value, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXGreaterThan(BigDecimal value) {
            addCriterion("LOCATION_X >", value, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("LOCATION_X >=", value, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXLessThan(BigDecimal value) {
            addCriterion("LOCATION_X <", value, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXLessThanOrEqualTo(BigDecimal value) {
            addCriterion("LOCATION_X <=", value, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXIn(List<BigDecimal> values) {
            addCriterion("LOCATION_X in", values, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXNotIn(List<BigDecimal> values) {
            addCriterion("LOCATION_X not in", values, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("LOCATION_X between", value1, value2, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationXNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("LOCATION_X not between", value1, value2, "locationX");
            return (Criteria) this;
        }

        public Criteria andLocationYIsNull() {
            addCriterion("LOCATION_Y is null");
            return (Criteria) this;
        }

        public Criteria andLocationYIsNotNull() {
            addCriterion("LOCATION_Y is not null");
            return (Criteria) this;
        }

        public Criteria andLocationYEqualTo(BigDecimal value) {
            addCriterion("LOCATION_Y =", value, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYNotEqualTo(BigDecimal value) {
            addCriterion("LOCATION_Y <>", value, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYGreaterThan(BigDecimal value) {
            addCriterion("LOCATION_Y >", value, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("LOCATION_Y >=", value, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYLessThan(BigDecimal value) {
            addCriterion("LOCATION_Y <", value, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYLessThanOrEqualTo(BigDecimal value) {
            addCriterion("LOCATION_Y <=", value, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYIn(List<BigDecimal> values) {
            addCriterion("LOCATION_Y in", values, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYNotIn(List<BigDecimal> values) {
            addCriterion("LOCATION_Y not in", values, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("LOCATION_Y between", value1, value2, "locationY");
            return (Criteria) this;
        }

        public Criteria andLocationYNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("LOCATION_Y not between", value1, value2, "locationY");
            return (Criteria) this;
        }

        public Criteria andScaleIsNull() {
            addCriterion("SCALE is null");
            return (Criteria) this;
        }

        public Criteria andScaleIsNotNull() {
            addCriterion("SCALE is not null");
            return (Criteria) this;
        }

        public Criteria andScaleEqualTo(Integer value) {
            addCriterion("SCALE =", value, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleNotEqualTo(Integer value) {
            addCriterion("SCALE <>", value, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleGreaterThan(Integer value) {
            addCriterion("SCALE >", value, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleGreaterThanOrEqualTo(Integer value) {
            addCriterion("SCALE >=", value, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleLessThan(Integer value) {
            addCriterion("SCALE <", value, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleLessThanOrEqualTo(Integer value) {
            addCriterion("SCALE <=", value, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleIn(List<Integer> values) {
            addCriterion("SCALE in", values, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleNotIn(List<Integer> values) {
            addCriterion("SCALE not in", values, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleBetween(Integer value1, Integer value2) {
            addCriterion("SCALE between", value1, value2, "scale");
            return (Criteria) this;
        }

        public Criteria andScaleNotBetween(Integer value1, Integer value2) {
            addCriterion("SCALE not between", value1, value2, "scale");
            return (Criteria) this;
        }

        public Criteria andLinkTitleIsNull() {
            addCriterion("LINK_TITLE is null");
            return (Criteria) this;
        }

        public Criteria andLinkTitleIsNotNull() {
            addCriterion("LINK_TITLE is not null");
            return (Criteria) this;
        }

        public Criteria andLinkTitleEqualTo(String value) {
            addCriterion("LINK_TITLE =", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleNotEqualTo(String value) {
            addCriterion("LINK_TITLE <>", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleGreaterThan(String value) {
            addCriterion("LINK_TITLE >", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleGreaterThanOrEqualTo(String value) {
            addCriterion("LINK_TITLE >=", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleLessThan(String value) {
            addCriterion("LINK_TITLE <", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleLessThanOrEqualTo(String value) {
            addCriterion("LINK_TITLE <=", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleLike(String value) {
            addCriterion("LINK_TITLE like", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleNotLike(String value) {
            addCriterion("LINK_TITLE not like", value, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleIn(List<String> values) {
            addCriterion("LINK_TITLE in", values, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleNotIn(List<String> values) {
            addCriterion("LINK_TITLE not in", values, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleBetween(String value1, String value2) {
            addCriterion("LINK_TITLE between", value1, value2, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andLinkTitleNotBetween(String value1, String value2) {
            addCriterion("LINK_TITLE not between", value1, value2, "linkTitle");
            return (Criteria) this;
        }

        public Criteria andFileIdIsNull() {
            addCriterion("FILE_ID is null");
            return (Criteria) this;
        }

        public Criteria andFileIdIsNotNull() {
            addCriterion("FILE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andFileIdEqualTo(Integer value) {
            addCriterion("FILE_ID =", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotEqualTo(Integer value) {
            addCriterion("FILE_ID <>", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThan(Integer value) {
            addCriterion("FILE_ID >", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("FILE_ID >=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThan(Integer value) {
            addCriterion("FILE_ID <", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThanOrEqualTo(Integer value) {
            addCriterion("FILE_ID <=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdIn(List<Integer> values) {
            addCriterion("FILE_ID in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotIn(List<Integer> values) {
            addCriterion("FILE_ID not in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdBetween(Integer value1, Integer value2) {
            addCriterion("FILE_ID between", value1, value2, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotBetween(Integer value1, Integer value2) {
            addCriterion("FILE_ID not between", value1, value2, "fileId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}