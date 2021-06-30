package com.basoft.service.entity.wechat.shopWxNews;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ShopWxNewsCustExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopWxNewsCustExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andMsgIdIsNull() {
            addCriterion("MSG_ID is null");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNotNull() {
            addCriterion("MSG_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMsgIdEqualTo(Long value) {
            addCriterion("MSG_ID =", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotEqualTo(Long value) {
            addCriterion("MSG_ID <>", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThan(Long value) {
            addCriterion("MSG_ID >", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MSG_ID >=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThan(Long value) {
            addCriterion("MSG_ID <", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThanOrEqualTo(Long value) {
            addCriterion("MSG_ID <=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdIn(List<Long> values) {
            addCriterion("MSG_ID in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotIn(List<Long> values) {
            addCriterion("MSG_ID not in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdBetween(Long value1, Long value2) {
            addCriterion("MSG_ID between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotBetween(Long value1, Long value2) {
            addCriterion("MSG_ID not between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdIsNull() {
            addCriterion("CUST_SYS_ID is null");
            return (Criteria) this;
        }

        public Criteria andCustSysIdIsNotNull() {
            addCriterion("CUST_SYS_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCustSysIdEqualTo(Long value) {
            addCriterion("CUST_SYS_ID =", value, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdNotEqualTo(Long value) {
            addCriterion("CUST_SYS_ID <>", value, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdGreaterThan(Long value) {
            addCriterion("CUST_SYS_ID >", value, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdGreaterThanOrEqualTo(Long value) {
            addCriterion("CUST_SYS_ID >=", value, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdLessThan(Long value) {
            addCriterion("CUST_SYS_ID <", value, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdLessThanOrEqualTo(Long value) {
            addCriterion("CUST_SYS_ID <=", value, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdIn(List<Long> values) {
            addCriterion("CUST_SYS_ID in", values, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdNotIn(List<Long> values) {
            addCriterion("CUST_SYS_ID not in", values, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdBetween(Long value1, Long value2) {
            addCriterion("CUST_SYS_ID between", value1, value2, "custSysId");
            return (Criteria) this;
        }

        public Criteria andCustSysIdNotBetween(Long value1, Long value2) {
            addCriterion("CUST_SYS_ID not between", value1, value2, "custSysId");
            return (Criteria) this;
        }

        public Criteria andOpenidIsNull() {
            addCriterion("OPENID is null");
            return (Criteria) this;
        }

        public Criteria andOpenidIsNotNull() {
            addCriterion("OPENID is not null");
            return (Criteria) this;
        }

        public Criteria andOpenidEqualTo(String value) {
            addCriterion("OPENID =", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidNotEqualTo(String value) {
            addCriterion("OPENID <>", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidGreaterThan(String value) {
            addCriterion("OPENID >", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("OPENID >=", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidLessThan(String value) {
            addCriterion("OPENID <", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidLessThanOrEqualTo(String value) {
            addCriterion("OPENID <=", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidLike(String value) {
            addCriterion("OPENID like", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidNotLike(String value) {
            addCriterion("OPENID not like", value, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidIn(List<String> values) {
            addCriterion("OPENID in", values, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidNotIn(List<String> values) {
            addCriterion("OPENID not in", values, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidBetween(String value1, String value2) {
            addCriterion("OPENID between", value1, value2, "openid");
            return (Criteria) this;
        }

        public Criteria andOpenidNotBetween(String value1, String value2) {
            addCriterion("OPENID not between", value1, value2, "openid");
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

        public Criteria andSendStsIsNull() {
            addCriterion("SEND_STS is null");
            return (Criteria) this;
        }

        public Criteria andSendStsIsNotNull() {
            addCriterion("SEND_STS is not null");
            return (Criteria) this;
        }

        public Criteria andSendStsEqualTo(Integer value) {
            addCriterion("SEND_STS =", value, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsNotEqualTo(Integer value) {
            addCriterion("SEND_STS <>", value, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsGreaterThan(Integer value) {
            addCriterion("SEND_STS >", value, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEND_STS >=", value, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsLessThan(Integer value) {
            addCriterion("SEND_STS <", value, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsLessThanOrEqualTo(Integer value) {
            addCriterion("SEND_STS <=", value, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsIn(List<Integer> values) {
            addCriterion("SEND_STS in", values, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsNotIn(List<Integer> values) {
            addCriterion("SEND_STS not in", values, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsBetween(Integer value1, Integer value2) {
            addCriterion("SEND_STS between", value1, value2, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendStsNotBetween(Integer value1, Integer value2) {
            addCriterion("SEND_STS not between", value1, value2, "sendSts");
            return (Criteria) this;
        }

        public Criteria andSendDtIsNull() {
            addCriterion("SEND_DT is null");
            return (Criteria) this;
        }

        public Criteria andSendDtIsNotNull() {
            addCriterion("SEND_DT is not null");
            return (Criteria) this;
        }

        public Criteria andSendDtEqualTo(Date value) {
            addCriterionForJDBCDate("SEND_DT =", value, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtNotEqualTo(Date value) {
            addCriterionForJDBCDate("SEND_DT <>", value, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtGreaterThan(Date value) {
            addCriterionForJDBCDate("SEND_DT >", value, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("SEND_DT >=", value, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtLessThan(Date value) {
            addCriterionForJDBCDate("SEND_DT <", value, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("SEND_DT <=", value, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtIn(List<Date> values) {
            addCriterionForJDBCDate("SEND_DT in", values, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtNotIn(List<Date> values) {
            addCriterionForJDBCDate("SEND_DT not in", values, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("SEND_DT between", value1, value2, "sendDt");
            return (Criteria) this;
        }

        public Criteria andSendDtNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("SEND_DT not between", value1, value2, "sendDt");
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