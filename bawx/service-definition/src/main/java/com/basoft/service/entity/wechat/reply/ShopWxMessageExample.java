package com.basoft.service.entity.wechat.reply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopWxMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopWxMessageExample() {
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

        public Criteria andMessageIdIsNull() {
            addCriterion("MESSAGE_ID is null");
            return (Criteria) this;
        }

        public Criteria andMessageIdIsNotNull() {
            addCriterion("MESSAGE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMessageIdEqualTo(Long value) {
            addCriterion("MESSAGE_ID =", value, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdNotEqualTo(Long value) {
            addCriterion("MESSAGE_ID <>", value, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdGreaterThan(Long value) {
            addCriterion("MESSAGE_ID >", value, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MESSAGE_ID >=", value, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdLessThan(Long value) {
            addCriterion("MESSAGE_ID <", value, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdLessThanOrEqualTo(Long value) {
            addCriterion("MESSAGE_ID <=", value, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdIn(List<Long> values) {
            addCriterion("MESSAGE_ID in", values, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdNotIn(List<Long> values) {
            addCriterion("MESSAGE_ID not in", values, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdBetween(Long value1, Long value2) {
            addCriterion("MESSAGE_ID between", value1, value2, "messageId");
            return (Criteria) this;
        }

        public Criteria andMessageIdNotBetween(Long value1, Long value2) {
            addCriterion("MESSAGE_ID not between", value1, value2, "messageId");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("SHOP_ID is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("SHOP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Long value) {
            addCriterion("SHOP_ID =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Long value) {
            addCriterion("SHOP_ID <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Long value) {
            addCriterion("SHOP_ID >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Long value) {
            addCriterion("SHOP_ID >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Long value) {
            addCriterion("SHOP_ID <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Long value) {
            addCriterion("SHOP_ID <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Long> values) {
            addCriterion("SHOP_ID in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Long> values) {
            addCriterion("SHOP_ID not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Long value1, Long value2) {
            addCriterion("SHOP_ID between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Long value1, Long value2) {
            addCriterion("SHOP_ID not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andMsgGroupIsNull() {
            addCriterion("MSG_GROUP is null");
            return (Criteria) this;
        }

        public Criteria andMsgGroupIsNotNull() {
            addCriterion("MSG_GROUP is not null");
            return (Criteria) this;
        }

        public Criteria andMsgGroupEqualTo(String value) {
            addCriterion("MSG_GROUP =", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupNotEqualTo(String value) {
            addCriterion("MSG_GROUP <>", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupGreaterThan(String value) {
            addCriterion("MSG_GROUP >", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupGreaterThanOrEqualTo(String value) {
            addCriterion("MSG_GROUP >=", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupLessThan(String value) {
            addCriterion("MSG_GROUP <", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupLessThanOrEqualTo(String value) {
            addCriterion("MSG_GROUP <=", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupLike(String value) {
            addCriterion("MSG_GROUP like", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupNotLike(String value) {
            addCriterion("MSG_GROUP not like", value, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupIn(List<String> values) {
            addCriterion("MSG_GROUP in", values, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupNotIn(List<String> values) {
            addCriterion("MSG_GROUP not in", values, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupBetween(String value1, String value2) {
            addCriterion("MSG_GROUP between", value1, value2, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andMsgGroupNotBetween(String value1, String value2) {
            addCriterion("MSG_GROUP not between", value1, value2, "msgGroup");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeIsNull() {
            addCriterion("SEND_FILE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeIsNotNull() {
            addCriterion("SEND_FILE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeEqualTo(Integer value) {
            addCriterion("SEND_FILE_TYPE =", value, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeNotEqualTo(Integer value) {
            addCriterion("SEND_FILE_TYPE <>", value, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeGreaterThan(Integer value) {
            addCriterion("SEND_FILE_TYPE >", value, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEND_FILE_TYPE >=", value, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeLessThan(Integer value) {
            addCriterion("SEND_FILE_TYPE <", value, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeLessThanOrEqualTo(Integer value) {
            addCriterion("SEND_FILE_TYPE <=", value, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeIn(List<Integer> values) {
            addCriterion("SEND_FILE_TYPE in", values, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeNotIn(List<Integer> values) {
            addCriterion("SEND_FILE_TYPE not in", values, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeBetween(Integer value1, Integer value2) {
            addCriterion("SEND_FILE_TYPE between", value1, value2, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andSendFileTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("SEND_FILE_TYPE not between", value1, value2, "sendFileType");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdIsNull() {
            addCriterion("MATERIAL_FILE_ID is null");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdIsNotNull() {
            addCriterion("MATERIAL_FILE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdEqualTo(Long value) {
            addCriterion("MATERIAL_FILE_ID =", value, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdNotEqualTo(Long value) {
            addCriterion("MATERIAL_FILE_ID <>", value, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdGreaterThan(Long value) {
            addCriterion("MATERIAL_FILE_ID >", value, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MATERIAL_FILE_ID >=", value, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdLessThan(Long value) {
            addCriterion("MATERIAL_FILE_ID <", value, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdLessThanOrEqualTo(Long value) {
            addCriterion("MATERIAL_FILE_ID <=", value, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdIn(List<Long> values) {
            addCriterion("MATERIAL_FILE_ID in", values, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdNotIn(List<Long> values) {
            addCriterion("MATERIAL_FILE_ID not in", values, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdBetween(Long value1, Long value2) {
            addCriterion("MATERIAL_FILE_ID between", value1, value2, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andMaterialFileIdNotBetween(Long value1, Long value2) {
            addCriterion("MATERIAL_FILE_ID not between", value1, value2, "materialFileId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdIsNull() {
            addCriterion("COVER_PAGE_ID is null");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdIsNotNull() {
            addCriterion("COVER_PAGE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdEqualTo(Integer value) {
            addCriterion("COVER_PAGE_ID =", value, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdNotEqualTo(Integer value) {
            addCriterion("COVER_PAGE_ID <>", value, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdGreaterThan(Integer value) {
            addCriterion("COVER_PAGE_ID >", value, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("COVER_PAGE_ID >=", value, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdLessThan(Integer value) {
            addCriterion("COVER_PAGE_ID <", value, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdLessThanOrEqualTo(Integer value) {
            addCriterion("COVER_PAGE_ID <=", value, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdIn(List<Integer> values) {
            addCriterion("COVER_PAGE_ID in", values, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdNotIn(List<Integer> values) {
            addCriterion("COVER_PAGE_ID not in", values, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdBetween(Integer value1, Integer value2) {
            addCriterion("COVER_PAGE_ID between", value1, value2, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andCoverPageIdNotBetween(Integer value1, Integer value2) {
            addCriterion("COVER_PAGE_ID not between", value1, value2, "coverPageId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIsNull() {
            addCriterion("SEND_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andSendTypeIsNotNull() {
            addCriterion("SEND_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andSendTypeEqualTo(Integer value) {
            addCriterion("SEND_TYPE =", value, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeNotEqualTo(Integer value) {
            addCriterion("SEND_TYPE <>", value, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeGreaterThan(Integer value) {
            addCriterion("SEND_TYPE >", value, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEND_TYPE >=", value, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeLessThan(Integer value) {
            addCriterion("SEND_TYPE <", value, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeLessThanOrEqualTo(Integer value) {
            addCriterion("SEND_TYPE <=", value, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeIn(List<Integer> values) {
            addCriterion("SEND_TYPE in", values, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeNotIn(List<Integer> values) {
            addCriterion("SEND_TYPE not in", values, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeBetween(Integer value1, Integer value2) {
            addCriterion("SEND_TYPE between", value1, value2, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("SEND_TYPE not between", value1, value2, "sendType");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNull() {
            addCriterion("SEND_TIME is null");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNotNull() {
            addCriterion("SEND_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andSendTimeEqualTo(Date value) {
            addCriterion("SEND_TIME =", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotEqualTo(Date value) {
            addCriterion("SEND_TIME <>", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThan(Date value) {
            addCriterion("SEND_TIME >", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("SEND_TIME >=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThan(Date value) {
            addCriterion("SEND_TIME <", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThanOrEqualTo(Date value) {
            addCriterion("SEND_TIME <=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeIn(List<Date> values) {
            addCriterion("SEND_TIME in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotIn(List<Date> values) {
            addCriterion("SEND_TIME not in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeBetween(Date value1, Date value2) {
            addCriterion("SEND_TIME between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotBetween(Date value1, Date value2) {
            addCriterion("SEND_TIME not between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andCreatedIdIsNull() {
            addCriterion("CREATED_ID is null");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdIsNull() {
            addCriterion("CALLCENTER_ID is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIdIsNotNull() {
            addCriterion("CREATED_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdIsNotNull() {
            addCriterion("CALLCENTER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedIdEqualTo(String value) {
            addCriterion("CREATED_ID =", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdEqualTo(String value) {
            addCriterion("CALLCENTER_ID =", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotEqualTo(String value) {
            addCriterion("CREATED_ID <>", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdNotEqualTo(String value) {
            addCriterion("CALLCENTER_ID <>", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdGreaterThan(String value) {
            addCriterion("CREATED_ID >", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdGreaterThan(String value) {
            addCriterion("CALLCENTER_ID >", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdGreaterThanOrEqualTo(String value) {
            addCriterion("CREATED_ID >=", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdLessThan(String value) {
            addCriterion("CREATED_ID <", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdLessThan(String value) {
            addCriterion("CALLCENTER_ID <", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdLessThanOrEqualTo(String value) {
            addCriterion("CREATED_ID <=", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdLessThanOrEqualTo(String value) {
            addCriterion("CALLCENTER_ID <=", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdLike(String value) {
            addCriterion("CREATED_ID like", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdLike(String value) {
            addCriterion("CALLCENTER_ID like", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotLike(String value) {
            addCriterion("CREATED_ID not like", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdNotLike(String value) {
            addCriterion("CALLCENTER_ID not like", value, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdIn(List<String> values) {
            addCriterion("CREATED_ID in", values, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdIn(List<String> values) {
            addCriterion("CALLCENTER_ID in", values, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotIn(List<String> values) {
            addCriterion("CREATED_ID not in", values, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdNotIn(List<String> values) {
            addCriterion("CALLCENTER_ID not in", values, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdBetween(String value1, String value2) {
            addCriterion("CREATED_ID between", value1, value2, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdBetween(String value1, String value2) {
            addCriterion("CALLCENTER_ID between", value1, value2, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotBetween(String value1, String value2) {
            addCriterion("CREATED_ID not between", value1, value2, "createdId");
            return (Criteria) this;
        }

        public Criteria andCallcenterIdNotBetween(String value1, String value2) {
            addCriterion("CALLCENTER_ID not between", value1, value2, "callcenterId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdIsNull() {
            addCriterion("MODIFIED_ID is null");
            return (Criteria) this;
        }

        public Criteria andModifiedIdIsNotNull() {
            addCriterion("MODIFIED_ID is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedIdEqualTo(String value) {
            addCriterion("MODIFIED_ID =", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdNotEqualTo(String value) {
            addCriterion("MODIFIED_ID <>", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdGreaterThan(String value) {
            addCriterion("MODIFIED_ID >", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdGreaterThanOrEqualTo(String value) {
            addCriterion("MODIFIED_ID >=", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdLessThan(String value) {
            addCriterion("MODIFIED_ID <", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdLessThanOrEqualTo(String value) {
            addCriterion("MODIFIED_ID <=", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdLike(String value) {
            addCriterion("MODIFIED_ID like", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdNotLike(String value) {
            addCriterion("MODIFIED_ID not like", value, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdIn(List<String> values) {
            addCriterion("MODIFIED_ID in", values, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdNotIn(List<String> values) {
            addCriterion("MODIFIED_ID not in", values, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdBetween(String value1, String value2) {
            addCriterion("MODIFIED_ID between", value1, value2, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andModifiedIdNotBetween(String value1, String value2) {
            addCriterion("MODIFIED_ID not between", value1, value2, "modifiedId");
            return (Criteria) this;
        }

        public Criteria andCreatedDtIsNull() {
            addCriterion("CREATED_DT is null");
            return (Criteria) this;
        }

        public Criteria andCreatedDtIsNotNull() {
            addCriterion("CREATED_DT is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedDtEqualTo(Date value) {
            addCriterion("CREATED_DT =", value, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtNotEqualTo(Date value) {
            addCriterion("CREATED_DT <>", value, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtGreaterThan(Date value) {
            addCriterion("CREATED_DT >", value, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATED_DT >=", value, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtLessThan(Date value) {
            addCriterion("CREATED_DT <", value, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtLessThanOrEqualTo(Date value) {
            addCriterion("CREATED_DT <=", value, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtIn(List<Date> values) {
            addCriterion("CREATED_DT in", values, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtNotIn(List<Date> values) {
            addCriterion("CREATED_DT not in", values, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtBetween(Date value1, Date value2) {
            addCriterion("CREATED_DT between", value1, value2, "createdDt");
            return (Criteria) this;
        }

        public Criteria andCreatedDtNotBetween(Date value1, Date value2) {
            addCriterion("CREATED_DT not between", value1, value2, "createdDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtIsNull() {
            addCriterion("MODIFIED_DT is null");
            return (Criteria) this;
        }

        public Criteria andModifiedDtIsNotNull() {
            addCriterion("MODIFIED_DT is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedDtEqualTo(Date value) {
            addCriterion("MODIFIED_DT =", value, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtNotEqualTo(Date value) {
            addCriterion("MODIFIED_DT <>", value, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtGreaterThan(Date value) {
            addCriterion("MODIFIED_DT >", value, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtGreaterThanOrEqualTo(Date value) {
            addCriterion("MODIFIED_DT >=", value, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtLessThan(Date value) {
            addCriterion("MODIFIED_DT <", value, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtLessThanOrEqualTo(Date value) {
            addCriterion("MODIFIED_DT <=", value, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtIn(List<Date> values) {
            addCriterion("MODIFIED_DT in", values, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtNotIn(List<Date> values) {
            addCriterion("MODIFIED_DT not in", values, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtBetween(Date value1, Date value2) {
            addCriterion("MODIFIED_DT between", value1, value2, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andModifiedDtNotBetween(Date value1, Date value2) {
            addCriterion("MODIFIED_DT not between", value1, value2, "modifiedDt");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("IS_DELETE is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("IS_DELETE is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Integer value) {
            addCriterion("IS_DELETE =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Integer value) {
            addCriterion("IS_DELETE <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Integer value) {
            addCriterion("IS_DELETE >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("IS_DELETE >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Integer value) {
            addCriterion("IS_DELETE <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("IS_DELETE <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Integer> values) {
            addCriterion("IS_DELETE in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Integer> values) {
            addCriterion("IS_DELETE not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Integer value1, Integer value2) {
            addCriterion("IS_DELETE between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Integer value1, Integer value2) {
            addCriterion("IS_DELETE not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdIsNull() {
            addCriterion("SEND_TYPE_ID is null");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdIsNotNull() {
            addCriterion("SEND_TYPE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdEqualTo(Integer value) {
            addCriterion("SEND_TYPE_ID =", value, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdNotEqualTo(Integer value) {
            addCriterion("SEND_TYPE_ID <>", value, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdGreaterThan(Integer value) {
            addCriterion("SEND_TYPE_ID >", value, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEND_TYPE_ID >=", value, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdLessThan(Integer value) {
            addCriterion("SEND_TYPE_ID <", value, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdLessThanOrEqualTo(Integer value) {
            addCriterion("SEND_TYPE_ID <=", value, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdIn(List<Integer> values) {
            addCriterion("SEND_TYPE_ID in", values, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdNotIn(List<Integer> values) {
            addCriterion("SEND_TYPE_ID not in", values, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdBetween(Integer value1, Integer value2) {
            addCriterion("SEND_TYPE_ID between", value1, value2, "sendTypeId");
            return (Criteria) this;
        }

        public Criteria andSendTypeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("SEND_TYPE_ID not between", value1, value2, "sendTypeId");
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