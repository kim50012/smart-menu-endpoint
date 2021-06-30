package com.basoft.service.entity.customer.cust;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CustExample() {
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

        public Criteria andCustLoginIdIsNull() {
            addCriterion("CUST_LOGIN_ID is null");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdIsNotNull() {
            addCriterion("CUST_LOGIN_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdEqualTo(String value) {
            addCriterion("CUST_LOGIN_ID =", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdNotEqualTo(String value) {
            addCriterion("CUST_LOGIN_ID <>", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdGreaterThan(String value) {
            addCriterion("CUST_LOGIN_ID >", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdGreaterThanOrEqualTo(String value) {
            addCriterion("CUST_LOGIN_ID >=", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdLessThan(String value) {
            addCriterion("CUST_LOGIN_ID <", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdLessThanOrEqualTo(String value) {
            addCriterion("CUST_LOGIN_ID <=", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdLike(String value) {
            addCriterion("CUST_LOGIN_ID like", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdNotLike(String value) {
            addCriterion("CUST_LOGIN_ID not like", value, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdIn(List<String> values) {
            addCriterion("CUST_LOGIN_ID in", values, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdNotIn(List<String> values) {
            addCriterion("CUST_LOGIN_ID not in", values, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdBetween(String value1, String value2) {
            addCriterion("CUST_LOGIN_ID between", value1, value2, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andCustLoginIdNotBetween(String value1, String value2) {
            addCriterion("CUST_LOGIN_ID not between", value1, value2, "custLoginId");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPIsNull() {
            addCriterion("WX_IF_OPENID_P is null");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPIsNotNull() {
            addCriterion("WX_IF_OPENID_P is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPEqualTo(String value) {
            addCriterion("WX_IF_OPENID_P =", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPNotEqualTo(String value) {
            addCriterion("WX_IF_OPENID_P <>", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPGreaterThan(String value) {
            addCriterion("WX_IF_OPENID_P >", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_OPENID_P >=", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPLessThan(String value) {
            addCriterion("WX_IF_OPENID_P <", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_OPENID_P <=", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPLike(String value) {
            addCriterion("WX_IF_OPENID_P like", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPNotLike(String value) {
            addCriterion("WX_IF_OPENID_P not like", value, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPIn(List<String> values) {
            addCriterion("WX_IF_OPENID_P in", values, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPNotIn(List<String> values) {
            addCriterion("WX_IF_OPENID_P not in", values, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPBetween(String value1, String value2) {
            addCriterion("WX_IF_OPENID_P between", value1, value2, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andWxIfOpenidPNotBetween(String value1, String value2) {
            addCriterion("WX_IF_OPENID_P not between", value1, value2, "wxIfOpenidP");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("EMAIL is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("EMAIL is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("EMAIL =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("EMAIL <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("EMAIL >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("EMAIL >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("EMAIL <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("EMAIL <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("EMAIL like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("EMAIL not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("EMAIL in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("EMAIL not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("EMAIL between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("EMAIL not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andMobileNoIsNull() {
            addCriterion("MOBILE_NO is null");
            return (Criteria) this;
        }

        public Criteria andMobileNoIsNotNull() {
            addCriterion("MOBILE_NO is not null");
            return (Criteria) this;
        }

        public Criteria andMobileNoEqualTo(String value) {
            addCriterion("MOBILE_NO =", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotEqualTo(String value) {
            addCriterion("MOBILE_NO <>", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoGreaterThan(String value) {
            addCriterion("MOBILE_NO >", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoGreaterThanOrEqualTo(String value) {
            addCriterion("MOBILE_NO >=", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLessThan(String value) {
            addCriterion("MOBILE_NO <", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLessThanOrEqualTo(String value) {
            addCriterion("MOBILE_NO <=", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLike(String value) {
            addCriterion("MOBILE_NO like", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotLike(String value) {
            addCriterion("MOBILE_NO not like", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoIn(List<String> values) {
            addCriterion("MOBILE_NO in", values, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotIn(List<String> values) {
            addCriterion("MOBILE_NO not in", values, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoBetween(String value1, String value2) {
            addCriterion("MOBILE_NO between", value1, value2, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotBetween(String value1, String value2) {
            addCriterion("MOBILE_NO not between", value1, value2, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeIsNull() {
            addCriterion("WX_IF_IS_SUBSCRIBE is null");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeIsNotNull() {
            addCriterion("WX_IF_IS_SUBSCRIBE is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeEqualTo(Byte value) {
            addCriterion("WX_IF_IS_SUBSCRIBE =", value, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeNotEqualTo(Byte value) {
            addCriterion("WX_IF_IS_SUBSCRIBE <>", value, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeGreaterThan(Byte value) {
            addCriterion("WX_IF_IS_SUBSCRIBE >", value, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeGreaterThanOrEqualTo(Byte value) {
            addCriterion("WX_IF_IS_SUBSCRIBE >=", value, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeLessThan(Byte value) {
            addCriterion("WX_IF_IS_SUBSCRIBE <", value, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeLessThanOrEqualTo(Byte value) {
            addCriterion("WX_IF_IS_SUBSCRIBE <=", value, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeIn(List<Byte> values) {
            addCriterion("WX_IF_IS_SUBSCRIBE in", values, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeNotIn(List<Byte> values) {
            addCriterion("WX_IF_IS_SUBSCRIBE not in", values, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeBetween(Byte value1, Byte value2) {
            addCriterion("WX_IF_IS_SUBSCRIBE between", value1, value2, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfIsSubscribeNotBetween(Byte value1, Byte value2) {
            addCriterion("WX_IF_IS_SUBSCRIBE not between", value1, value2, "wxIfIsSubscribe");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmIsNull() {
            addCriterion("WX_IF_NICK_NM is null");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmIsNotNull() {
            addCriterion("WX_IF_NICK_NM is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmEqualTo(String value) {
            addCriterion("WX_IF_NICK_NM =", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmNotEqualTo(String value) {
            addCriterion("WX_IF_NICK_NM <>", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmGreaterThan(String value) {
            addCriterion("WX_IF_NICK_NM >", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_NICK_NM >=", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmLessThan(String value) {
            addCriterion("WX_IF_NICK_NM <", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_NICK_NM <=", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmLike(String value) {
            addCriterion("WX_IF_NICK_NM like", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmNotLike(String value) {
            addCriterion("WX_IF_NICK_NM not like", value, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmIn(List<String> values) {
            addCriterion("WX_IF_NICK_NM in", values, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmNotIn(List<String> values) {
            addCriterion("WX_IF_NICK_NM not in", values, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmBetween(String value1, String value2) {
            addCriterion("WX_IF_NICK_NM between", value1, value2, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfNickNmNotBetween(String value1, String value2) {
            addCriterion("WX_IF_NICK_NM not between", value1, value2, "wxIfNickNm");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdIsNull() {
            addCriterion("WX_IF_SEX_ID is null");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdIsNotNull() {
            addCriterion("WX_IF_SEX_ID is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdEqualTo(Byte value) {
            addCriterion("WX_IF_SEX_ID =", value, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdNotEqualTo(Byte value) {
            addCriterion("WX_IF_SEX_ID <>", value, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdGreaterThan(Byte value) {
            addCriterion("WX_IF_SEX_ID >", value, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdGreaterThanOrEqualTo(Byte value) {
            addCriterion("WX_IF_SEX_ID >=", value, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdLessThan(Byte value) {
            addCriterion("WX_IF_SEX_ID <", value, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdLessThanOrEqualTo(Byte value) {
            addCriterion("WX_IF_SEX_ID <=", value, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdIn(List<Byte> values) {
            addCriterion("WX_IF_SEX_ID in", values, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdNotIn(List<Byte> values) {
            addCriterion("WX_IF_SEX_ID not in", values, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdBetween(Byte value1, Byte value2) {
            addCriterion("WX_IF_SEX_ID between", value1, value2, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfSexIdNotBetween(Byte value1, Byte value2) {
            addCriterion("WX_IF_SEX_ID not between", value1, value2, "wxIfSexId");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageIsNull() {
            addCriterion("WX_IF_LANGUAGE is null");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageIsNotNull() {
            addCriterion("WX_IF_LANGUAGE is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageEqualTo(String value) {
            addCriterion("WX_IF_LANGUAGE =", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageNotEqualTo(String value) {
            addCriterion("WX_IF_LANGUAGE <>", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageGreaterThan(String value) {
            addCriterion("WX_IF_LANGUAGE >", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_LANGUAGE >=", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageLessThan(String value) {
            addCriterion("WX_IF_LANGUAGE <", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_LANGUAGE <=", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageLike(String value) {
            addCriterion("WX_IF_LANGUAGE like", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageNotLike(String value) {
            addCriterion("WX_IF_LANGUAGE not like", value, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageIn(List<String> values) {
            addCriterion("WX_IF_LANGUAGE in", values, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageNotIn(List<String> values) {
            addCriterion("WX_IF_LANGUAGE not in", values, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageBetween(String value1, String value2) {
            addCriterion("WX_IF_LANGUAGE between", value1, value2, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfLanguageNotBetween(String value1, String value2) {
            addCriterion("WX_IF_LANGUAGE not between", value1, value2, "wxIfLanguage");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmIsNull() {
            addCriterion("WX_IF_COUNTRY_NM is null");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmIsNotNull() {
            addCriterion("WX_IF_COUNTRY_NM is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmEqualTo(String value) {
            addCriterion("WX_IF_COUNTRY_NM =", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmNotEqualTo(String value) {
            addCriterion("WX_IF_COUNTRY_NM <>", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmGreaterThan(String value) {
            addCriterion("WX_IF_COUNTRY_NM >", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_COUNTRY_NM >=", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmLessThan(String value) {
            addCriterion("WX_IF_COUNTRY_NM <", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_COUNTRY_NM <=", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmLike(String value) {
            addCriterion("WX_IF_COUNTRY_NM like", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmNotLike(String value) {
            addCriterion("WX_IF_COUNTRY_NM not like", value, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmIn(List<String> values) {
            addCriterion("WX_IF_COUNTRY_NM in", values, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmNotIn(List<String> values) {
            addCriterion("WX_IF_COUNTRY_NM not in", values, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmBetween(String value1, String value2) {
            addCriterion("WX_IF_COUNTRY_NM between", value1, value2, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCountryNmNotBetween(String value1, String value2) {
            addCriterion("WX_IF_COUNTRY_NM not between", value1, value2, "wxIfCountryNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmIsNull() {
            addCriterion("WX_IF_PROVINCE_NM is null");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmIsNotNull() {
            addCriterion("WX_IF_PROVINCE_NM is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmEqualTo(String value) {
            addCriterion("WX_IF_PROVINCE_NM =", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmNotEqualTo(String value) {
            addCriterion("WX_IF_PROVINCE_NM <>", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmGreaterThan(String value) {
            addCriterion("WX_IF_PROVINCE_NM >", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_PROVINCE_NM >=", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmLessThan(String value) {
            addCriterion("WX_IF_PROVINCE_NM <", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_PROVINCE_NM <=", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmLike(String value) {
            addCriterion("WX_IF_PROVINCE_NM like", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmNotLike(String value) {
            addCriterion("WX_IF_PROVINCE_NM not like", value, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmIn(List<String> values) {
            addCriterion("WX_IF_PROVINCE_NM in", values, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmNotIn(List<String> values) {
            addCriterion("WX_IF_PROVINCE_NM not in", values, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmBetween(String value1, String value2) {
            addCriterion("WX_IF_PROVINCE_NM between", value1, value2, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfProvinceNmNotBetween(String value1, String value2) {
            addCriterion("WX_IF_PROVINCE_NM not between", value1, value2, "wxIfProvinceNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmIsNull() {
            addCriterion("WX_IF_CITY_NM is null");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmIsNotNull() {
            addCriterion("WX_IF_CITY_NM is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmEqualTo(String value) {
            addCriterion("WX_IF_CITY_NM =", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmNotEqualTo(String value) {
            addCriterion("WX_IF_CITY_NM <>", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmGreaterThan(String value) {
            addCriterion("WX_IF_CITY_NM >", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_CITY_NM >=", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmLessThan(String value) {
            addCriterion("WX_IF_CITY_NM <", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_CITY_NM <=", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmLike(String value) {
            addCriterion("WX_IF_CITY_NM like", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmNotLike(String value) {
            addCriterion("WX_IF_CITY_NM not like", value, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmIn(List<String> values) {
            addCriterion("WX_IF_CITY_NM in", values, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmNotIn(List<String> values) {
            addCriterion("WX_IF_CITY_NM not in", values, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmBetween(String value1, String value2) {
            addCriterion("WX_IF_CITY_NM between", value1, value2, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfCityNmNotBetween(String value1, String value2) {
            addCriterion("WX_IF_CITY_NM not between", value1, value2, "wxIfCityNm");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlIsNull() {
            addCriterion("WX_IF_HEADIMGURL is null");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlIsNotNull() {
            addCriterion("WX_IF_HEADIMGURL is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlEqualTo(String value) {
            addCriterion("WX_IF_HEADIMGURL =", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlNotEqualTo(String value) {
            addCriterion("WX_IF_HEADIMGURL <>", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlGreaterThan(String value) {
            addCriterion("WX_IF_HEADIMGURL >", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_HEADIMGURL >=", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlLessThan(String value) {
            addCriterion("WX_IF_HEADIMGURL <", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_HEADIMGURL <=", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlLike(String value) {
            addCriterion("WX_IF_HEADIMGURL like", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlNotLike(String value) {
            addCriterion("WX_IF_HEADIMGURL not like", value, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlIn(List<String> values) {
            addCriterion("WX_IF_HEADIMGURL in", values, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlNotIn(List<String> values) {
            addCriterion("WX_IF_HEADIMGURL not in", values, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlBetween(String value1, String value2) {
            addCriterion("WX_IF_HEADIMGURL between", value1, value2, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfHeadimgurlNotBetween(String value1, String value2) {
            addCriterion("WX_IF_HEADIMGURL not between", value1, value2, "wxIfHeadimgurl");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeIsNull() {
            addCriterion("WX_IF_SUBSCRIBE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeIsNotNull() {
            addCriterion("WX_IF_SUBSCRIBE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeEqualTo(Date value) {
            addCriterion("WX_IF_SUBSCRIBE_TIME =", value, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeNotEqualTo(Date value) {
            addCriterion("WX_IF_SUBSCRIBE_TIME <>", value, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeGreaterThan(Date value) {
            addCriterion("WX_IF_SUBSCRIBE_TIME >", value, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("WX_IF_SUBSCRIBE_TIME >=", value, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeLessThan(Date value) {
            addCriterion("WX_IF_SUBSCRIBE_TIME <", value, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeLessThanOrEqualTo(Date value) {
            addCriterion("WX_IF_SUBSCRIBE_TIME <=", value, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeIn(List<Date> values) {
            addCriterion("WX_IF_SUBSCRIBE_TIME in", values, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeNotIn(List<Date> values) {
            addCriterion("WX_IF_SUBSCRIBE_TIME not in", values, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeBetween(Date value1, Date value2) {
            addCriterion("WX_IF_SUBSCRIBE_TIME between", value1, value2, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfSubscribeTimeNotBetween(Date value1, Date value2) {
            addCriterion("WX_IF_SUBSCRIBE_TIME not between", value1, value2, "wxIfSubscribeTime");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidIsNull() {
            addCriterion("WX_IF_UNIONID is null");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidIsNotNull() {
            addCriterion("WX_IF_UNIONID is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidEqualTo(String value) {
            addCriterion("WX_IF_UNIONID =", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidNotEqualTo(String value) {
            addCriterion("WX_IF_UNIONID <>", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidGreaterThan(String value) {
            addCriterion("WX_IF_UNIONID >", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_UNIONID >=", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidLessThan(String value) {
            addCriterion("WX_IF_UNIONID <", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_UNIONID <=", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidLike(String value) {
            addCriterion("WX_IF_UNIONID like", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidNotLike(String value) {
            addCriterion("WX_IF_UNIONID not like", value, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidIn(List<String> values) {
            addCriterion("WX_IF_UNIONID in", values, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidNotIn(List<String> values) {
            addCriterion("WX_IF_UNIONID not in", values, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidBetween(String value1, String value2) {
            addCriterion("WX_IF_UNIONID between", value1, value2, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfUnionidNotBetween(String value1, String value2) {
            addCriterion("WX_IF_UNIONID not between", value1, value2, "wxIfUnionid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidIsNull() {
            addCriterion("WX_IF_GROUPID is null");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidIsNotNull() {
            addCriterion("WX_IF_GROUPID is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidEqualTo(Long value) {
            addCriterion("WX_IF_GROUPID =", value, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidNotEqualTo(Long value) {
            addCriterion("WX_IF_GROUPID <>", value, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidGreaterThan(Long value) {
            addCriterion("WX_IF_GROUPID >", value, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidGreaterThanOrEqualTo(Long value) {
            addCriterion("WX_IF_GROUPID >=", value, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidLessThan(Long value) {
            addCriterion("WX_IF_GROUPID <", value, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidLessThanOrEqualTo(Long value) {
            addCriterion("WX_IF_GROUPID <=", value, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidIn(List<Long> values) {
            addCriterion("WX_IF_GROUPID in", values, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidNotIn(List<Long> values) {
            addCriterion("WX_IF_GROUPID not in", values, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidBetween(Long value1, Long value2) {
            addCriterion("WX_IF_GROUPID between", value1, value2, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfGroupidNotBetween(Long value1, Long value2) {
            addCriterion("WX_IF_GROUPID not between", value1, value2, "wxIfGroupid");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkIsNull() {
            addCriterion("WX_IF_REMARK is null");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkIsNotNull() {
            addCriterion("WX_IF_REMARK is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkEqualTo(String value) {
            addCriterion("WX_IF_REMARK =", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkNotEqualTo(String value) {
            addCriterion("WX_IF_REMARK <>", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkGreaterThan(String value) {
            addCriterion("WX_IF_REMARK >", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_REMARK >=", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkLessThan(String value) {
            addCriterion("WX_IF_REMARK <", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_REMARK <=", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkLike(String value) {
            addCriterion("WX_IF_REMARK like", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkNotLike(String value) {
            addCriterion("WX_IF_REMARK not like", value, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkIn(List<String> values) {
            addCriterion("WX_IF_REMARK in", values, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkNotIn(List<String> values) {
            addCriterion("WX_IF_REMARK not in", values, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkBetween(String value1, String value2) {
            addCriterion("WX_IF_REMARK between", value1, value2, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxIfRemarkNotBetween(String value1, String value2) {
            addCriterion("WX_IF_REMARK not between", value1, value2, "wxIfRemark");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtIsNull() {
            addCriterion("WX_SUBSCRIBE_DT is null");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtIsNotNull() {
            addCriterion("WX_SUBSCRIBE_DT is not null");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtEqualTo(Date value) {
            addCriterion("WX_SUBSCRIBE_DT =", value, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtNotEqualTo(Date value) {
            addCriterion("WX_SUBSCRIBE_DT <>", value, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtGreaterThan(Date value) {
            addCriterion("WX_SUBSCRIBE_DT >", value, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtGreaterThanOrEqualTo(Date value) {
            addCriterion("WX_SUBSCRIBE_DT >=", value, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtLessThan(Date value) {
            addCriterion("WX_SUBSCRIBE_DT <", value, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtLessThanOrEqualTo(Date value) {
            addCriterion("WX_SUBSCRIBE_DT <=", value, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtIn(List<Date> values) {
            addCriterion("WX_SUBSCRIBE_DT in", values, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtNotIn(List<Date> values) {
            addCriterion("WX_SUBSCRIBE_DT not in", values, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtBetween(Date value1, Date value2) {
            addCriterion("WX_SUBSCRIBE_DT between", value1, value2, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxSubscribeDtNotBetween(Date value1, Date value2) {
            addCriterion("WX_SUBSCRIBE_DT not between", value1, value2, "wxSubscribeDt");
            return (Criteria) this;
        }

        public Criteria andWxIdPIsNull() {
            addCriterion("WX_ID_P is null");
            return (Criteria) this;
        }

        public Criteria andWxIdPIsNotNull() {
            addCriterion("WX_ID_P is not null");
            return (Criteria) this;
        }

        public Criteria andWxIdPEqualTo(String value) {
            addCriterion("WX_ID_P =", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPNotEqualTo(String value) {
            addCriterion("WX_ID_P <>", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPGreaterThan(String value) {
            addCriterion("WX_ID_P >", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPGreaterThanOrEqualTo(String value) {
            addCriterion("WX_ID_P >=", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPLessThan(String value) {
            addCriterion("WX_ID_P <", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPLessThanOrEqualTo(String value) {
            addCriterion("WX_ID_P <=", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPLike(String value) {
            addCriterion("WX_ID_P like", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPNotLike(String value) {
            addCriterion("WX_ID_P not like", value, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPIn(List<String> values) {
            addCriterion("WX_ID_P in", values, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPNotIn(List<String> values) {
            addCriterion("WX_ID_P not in", values, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPBetween(String value1, String value2) {
            addCriterion("WX_ID_P between", value1, value2, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andWxIdPNotBetween(String value1, String value2) {
            addCriterion("WX_ID_P not between", value1, value2, "wxIdP");
            return (Criteria) this;
        }

        public Criteria andCustNickNmIsNull() {
            addCriterion("CUST_NICK_NM is null");
            return (Criteria) this;
        }

        public Criteria andCustNickNmIsNotNull() {
            addCriterion("CUST_NICK_NM is not null");
            return (Criteria) this;
        }

        public Criteria andCustNickNmEqualTo(String value) {
            addCriterion("CUST_NICK_NM =", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmNotEqualTo(String value) {
            addCriterion("CUST_NICK_NM <>", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmGreaterThan(String value) {
            addCriterion("CUST_NICK_NM >", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmGreaterThanOrEqualTo(String value) {
            addCriterion("CUST_NICK_NM >=", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmLessThan(String value) {
            addCriterion("CUST_NICK_NM <", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmLessThanOrEqualTo(String value) {
            addCriterion("CUST_NICK_NM <=", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmLike(String value) {
            addCriterion("CUST_NICK_NM like", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmNotLike(String value) {
            addCriterion("CUST_NICK_NM not like", value, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmIn(List<String> values) {
            addCriterion("CUST_NICK_NM in", values, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmNotIn(List<String> values) {
            addCriterion("CUST_NICK_NM not in", values, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmBetween(String value1, String value2) {
            addCriterion("CUST_NICK_NM between", value1, value2, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustNickNmNotBetween(String value1, String value2) {
            addCriterion("CUST_NICK_NM not between", value1, value2, "custNickNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmIsNull() {
            addCriterion("CUST_REAL_NM is null");
            return (Criteria) this;
        }

        public Criteria andCustRealNmIsNotNull() {
            addCriterion("CUST_REAL_NM is not null");
            return (Criteria) this;
        }

        public Criteria andCustRealNmEqualTo(String value) {
            addCriterion("CUST_REAL_NM =", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmNotEqualTo(String value) {
            addCriterion("CUST_REAL_NM <>", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmGreaterThan(String value) {
            addCriterion("CUST_REAL_NM >", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmGreaterThanOrEqualTo(String value) {
            addCriterion("CUST_REAL_NM >=", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmLessThan(String value) {
            addCriterion("CUST_REAL_NM <", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmLessThanOrEqualTo(String value) {
            addCriterion("CUST_REAL_NM <=", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmLike(String value) {
            addCriterion("CUST_REAL_NM like", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmNotLike(String value) {
            addCriterion("CUST_REAL_NM not like", value, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmIn(List<String> values) {
            addCriterion("CUST_REAL_NM in", values, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmNotIn(List<String> values) {
            addCriterion("CUST_REAL_NM not in", values, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmBetween(String value1, String value2) {
            addCriterion("CUST_REAL_NM between", value1, value2, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andCustRealNmNotBetween(String value1, String value2) {
            addCriterion("CUST_REAL_NM not between", value1, value2, "custRealNm");
            return (Criteria) this;
        }

        public Criteria andPwdIsNull() {
            addCriterion("PWD is null");
            return (Criteria) this;
        }

        public Criteria andPwdIsNotNull() {
            addCriterion("PWD is not null");
            return (Criteria) this;
        }

        public Criteria andPwdEqualTo(String value) {
            addCriterion("PWD =", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotEqualTo(String value) {
            addCriterion("PWD <>", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdGreaterThan(String value) {
            addCriterion("PWD >", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdGreaterThanOrEqualTo(String value) {
            addCriterion("PWD >=", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLessThan(String value) {
            addCriterion("PWD <", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLessThanOrEqualTo(String value) {
            addCriterion("PWD <=", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLike(String value) {
            addCriterion("PWD like", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotLike(String value) {
            addCriterion("PWD not like", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdIn(List<String> values) {
            addCriterion("PWD in", values, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotIn(List<String> values) {
            addCriterion("PWD not in", values, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdBetween(String value1, String value2) {
            addCriterion("PWD between", value1, value2, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotBetween(String value1, String value2) {
            addCriterion("PWD not between", value1, value2, "pwd");
            return (Criteria) this;
        }

        public Criteria andActiveStsIsNull() {
            addCriterion("ACTIVE_STS is null");
            return (Criteria) this;
        }

        public Criteria andActiveStsIsNotNull() {
            addCriterion("ACTIVE_STS is not null");
            return (Criteria) this;
        }

        public Criteria andActiveStsEqualTo(String value) {
            addCriterion("ACTIVE_STS =", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsNotEqualTo(String value) {
            addCriterion("ACTIVE_STS <>", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsGreaterThan(String value) {
            addCriterion("ACTIVE_STS >", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsGreaterThanOrEqualTo(String value) {
            addCriterion("ACTIVE_STS >=", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsLessThan(String value) {
            addCriterion("ACTIVE_STS <", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsLessThanOrEqualTo(String value) {
            addCriterion("ACTIVE_STS <=", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsLike(String value) {
            addCriterion("ACTIVE_STS like", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsNotLike(String value) {
            addCriterion("ACTIVE_STS not like", value, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsIn(List<String> values) {
            addCriterion("ACTIVE_STS in", values, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsNotIn(List<String> values) {
            addCriterion("ACTIVE_STS not in", values, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsBetween(String value1, String value2) {
            addCriterion("ACTIVE_STS between", value1, value2, "activeSts");
            return (Criteria) this;
        }

        public Criteria andActiveStsNotBetween(String value1, String value2) {
            addCriterion("ACTIVE_STS not between", value1, value2, "activeSts");
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

        public Criteria andWxIfImgUrlIsNull() {
            addCriterion("WX_IF_IMG_URL is null");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlIsNotNull() {
            addCriterion("WX_IF_IMG_URL is not null");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlEqualTo(String value) {
            addCriterion("WX_IF_IMG_URL =", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlNotEqualTo(String value) {
            addCriterion("WX_IF_IMG_URL <>", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlGreaterThan(String value) {
            addCriterion("WX_IF_IMG_URL >", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlGreaterThanOrEqualTo(String value) {
            addCriterion("WX_IF_IMG_URL >=", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlLessThan(String value) {
            addCriterion("WX_IF_IMG_URL <", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlLessThanOrEqualTo(String value) {
            addCriterion("WX_IF_IMG_URL <=", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlLike(String value) {
            addCriterion("WX_IF_IMG_URL like", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlNotLike(String value) {
            addCriterion("WX_IF_IMG_URL not like", value, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlIn(List<String> values) {
            addCriterion("WX_IF_IMG_URL in", values, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlNotIn(List<String> values) {
            addCriterion("WX_IF_IMG_URL not in", values, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlBetween(String value1, String value2) {
            addCriterion("WX_IF_IMG_URL between", value1, value2, "wxIfImgUrl");
            return (Criteria) this;
        }

        public Criteria andWxIfImgUrlNotBetween(String value1, String value2) {
            addCriterion("WX_IF_IMG_URL not between", value1, value2, "wxIfImgUrl");
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