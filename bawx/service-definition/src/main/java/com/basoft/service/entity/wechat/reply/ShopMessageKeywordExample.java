package com.basoft.service.entity.wechat.reply;

import java.util.ArrayList;
import java.util.List;

public class ShopMessageKeywordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopMessageKeywordExample() {
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

        public Criteria andKeywordIdIsNull() {
            addCriterion("KEYWORD_ID is null");
            return (Criteria) this;
        }

        public Criteria andKeywordIdIsNotNull() {
            addCriterion("KEYWORD_ID is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordIdEqualTo(Long value) {
            addCriterion("KEYWORD_ID =", value, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdNotEqualTo(Long value) {
            addCriterion("KEYWORD_ID <>", value, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdGreaterThan(Long value) {
            addCriterion("KEYWORD_ID >", value, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdGreaterThanOrEqualTo(Long value) {
            addCriterion("KEYWORD_ID >=", value, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdLessThan(Long value) {
            addCriterion("KEYWORD_ID <", value, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdLessThanOrEqualTo(Long value) {
            addCriterion("KEYWORD_ID <=", value, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdIn(List<Long> values) {
            addCriterion("KEYWORD_ID in", values, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdNotIn(List<Long> values) {
            addCriterion("KEYWORD_ID not in", values, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdBetween(Long value1, Long value2) {
            addCriterion("KEYWORD_ID between", value1, value2, "keywordId");
            return (Criteria) this;
        }

        public Criteria andKeywordIdNotBetween(Long value1, Long value2) {
            addCriterion("KEYWORD_ID not between", value1, value2, "keywordId");
            return (Criteria) this;
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

        public Criteria andKeywordIsNull() {
            addCriterion("KEYWORD is null");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNotNull() {
            addCriterion("KEYWORD is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordEqualTo(String value) {
            addCriterion("KEYWORD =", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotEqualTo(String value) {
            addCriterion("KEYWORD <>", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThan(String value) {
            addCriterion("KEYWORD >", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThanOrEqualTo(String value) {
            addCriterion("KEYWORD >=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThan(String value) {
            addCriterion("KEYWORD <", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThanOrEqualTo(String value) {
            addCriterion("KEYWORD <=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLike(String value) {
            addCriterion("KEYWORD like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotLike(String value) {
            addCriterion("KEYWORD not like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordIn(List<String> values) {
            addCriterion("KEYWORD in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotIn(List<String> values) {
            addCriterion("KEYWORD not in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordBetween(String value1, String value2) {
            addCriterion("KEYWORD between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotBetween(String value1, String value2) {
            addCriterion("KEYWORD not between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andTableNmIsNull() {
            addCriterion("TABLE_NM is null");
            return (Criteria) this;
        }

        public Criteria andTableNmIsNotNull() {
            addCriterion("TABLE_NM is not null");
            return (Criteria) this;
        }

        public Criteria andTableNmEqualTo(String value) {
            addCriterion("TABLE_NM =", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmNotEqualTo(String value) {
            addCriterion("TABLE_NM <>", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmGreaterThan(String value) {
            addCriterion("TABLE_NM >", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmGreaterThanOrEqualTo(String value) {
            addCriterion("TABLE_NM >=", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmLessThan(String value) {
            addCriterion("TABLE_NM <", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmLessThanOrEqualTo(String value) {
            addCriterion("TABLE_NM <=", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmLike(String value) {
            addCriterion("TABLE_NM like", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmNotLike(String value) {
            addCriterion("TABLE_NM not like", value, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmIn(List<String> values) {
            addCriterion("TABLE_NM in", values, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmNotIn(List<String> values) {
            addCriterion("TABLE_NM not in", values, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmBetween(String value1, String value2) {
            addCriterion("TABLE_NM between", value1, value2, "tableNm");
            return (Criteria) this;
        }

        public Criteria andTableNmNotBetween(String value1, String value2) {
            addCriterion("TABLE_NM not between", value1, value2, "tableNm");
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