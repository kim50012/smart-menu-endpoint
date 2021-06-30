package com.basoft.service.entity.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopExample() {
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

        public Criteria andShopNmIsNull() {
            addCriterion("SHOP_NM is null");
            return (Criteria) this;
        }

        public Criteria andShopNmIsNotNull() {
            addCriterion("SHOP_NM is not null");
            return (Criteria) this;
        }

        public Criteria andShopNmEqualTo(String value) {
            addCriterion("SHOP_NM =", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmNotEqualTo(String value) {
            addCriterion("SHOP_NM <>", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmGreaterThan(String value) {
            addCriterion("SHOP_NM >", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmGreaterThanOrEqualTo(String value) {
            addCriterion("SHOP_NM >=", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmLessThan(String value) {
            addCriterion("SHOP_NM <", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmLessThanOrEqualTo(String value) {
            addCriterion("SHOP_NM <=", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmLike(String value) {
            addCriterion("SHOP_NM like", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmNotLike(String value) {
            addCriterion("SHOP_NM not like", value, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmIn(List<String> values) {
            addCriterion("SHOP_NM in", values, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmNotIn(List<String> values) {
            addCriterion("SHOP_NM not in", values, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmBetween(String value1, String value2) {
            addCriterion("SHOP_NM between", value1, value2, "shopNm");
            return (Criteria) this;
        }

        public Criteria andShopNmNotBetween(String value1, String value2) {
            addCriterion("SHOP_NM not between", value1, value2, "shopNm");
            return (Criteria) this;
        }

        public Criteria andGCorpIdIsNull() {
            addCriterion("G_CORP_ID is null");
            return (Criteria) this;
        }

        public Criteria andGCorpIdIsNotNull() {
            addCriterion("G_CORP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andGCorpIdEqualTo(Integer value) {
            addCriterion("G_CORP_ID =", value, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdNotEqualTo(Integer value) {
            addCriterion("G_CORP_ID <>", value, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdGreaterThan(Integer value) {
            addCriterion("G_CORP_ID >", value, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("G_CORP_ID >=", value, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdLessThan(Integer value) {
            addCriterion("G_CORP_ID <", value, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdLessThanOrEqualTo(Integer value) {
            addCriterion("G_CORP_ID <=", value, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdIn(List<Integer> values) {
            addCriterion("G_CORP_ID in", values, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdNotIn(List<Integer> values) {
            addCriterion("G_CORP_ID not in", values, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdBetween(Integer value1, Integer value2) {
            addCriterion("G_CORP_ID between", value1, value2, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andGCorpIdNotBetween(Integer value1, Integer value2) {
            addCriterion("G_CORP_ID not between", value1, value2, "gCorpId");
            return (Criteria) this;
        }

        public Criteria andMktIdIsNull() {
            addCriterion("MKT_ID is null");
            return (Criteria) this;
        }

        public Criteria andMktIdIsNotNull() {
            addCriterion("MKT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMktIdEqualTo(Integer value) {
            addCriterion("MKT_ID =", value, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdNotEqualTo(Integer value) {
            addCriterion("MKT_ID <>", value, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdGreaterThan(Integer value) {
            addCriterion("MKT_ID >", value, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("MKT_ID >=", value, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdLessThan(Integer value) {
            addCriterion("MKT_ID <", value, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdLessThanOrEqualTo(Integer value) {
            addCriterion("MKT_ID <=", value, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdIn(List<Integer> values) {
            addCriterion("MKT_ID in", values, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdNotIn(List<Integer> values) {
            addCriterion("MKT_ID not in", values, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdBetween(Integer value1, Integer value2) {
            addCriterion("MKT_ID between", value1, value2, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIdNotBetween(Integer value1, Integer value2) {
            addCriterion("MKT_ID not between", value1, value2, "mktId");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopIsNull() {
            addCriterion("MKT_IS_MAINSHOP is null");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopIsNotNull() {
            addCriterion("MKT_IS_MAINSHOP is not null");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopEqualTo(Byte value) {
            addCriterion("MKT_IS_MAINSHOP =", value, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopNotEqualTo(Byte value) {
            addCriterion("MKT_IS_MAINSHOP <>", value, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopGreaterThan(Byte value) {
            addCriterion("MKT_IS_MAINSHOP >", value, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopGreaterThanOrEqualTo(Byte value) {
            addCriterion("MKT_IS_MAINSHOP >=", value, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopLessThan(Byte value) {
            addCriterion("MKT_IS_MAINSHOP <", value, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopLessThanOrEqualTo(Byte value) {
            addCriterion("MKT_IS_MAINSHOP <=", value, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopIn(List<Byte> values) {
            addCriterion("MKT_IS_MAINSHOP in", values, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopNotIn(List<Byte> values) {
            addCriterion("MKT_IS_MAINSHOP not in", values, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopBetween(Byte value1, Byte value2) {
            addCriterion("MKT_IS_MAINSHOP between", value1, value2, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andMktIsMainshopNotBetween(Byte value1, Byte value2) {
            addCriterion("MKT_IS_MAINSHOP not between", value1, value2, "mktIsMainshop");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSIsNull() {
            addCriterion("WX_OPENID_S is null");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSIsNotNull() {
            addCriterion("WX_OPENID_S is not null");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSEqualTo(String value) {
            addCriterion("WX_OPENID_S =", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSNotEqualTo(String value) {
            addCriterion("WX_OPENID_S <>", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSGreaterThan(String value) {
            addCriterion("WX_OPENID_S >", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSGreaterThanOrEqualTo(String value) {
            addCriterion("WX_OPENID_S >=", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSLessThan(String value) {
            addCriterion("WX_OPENID_S <", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSLessThanOrEqualTo(String value) {
            addCriterion("WX_OPENID_S <=", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSLike(String value) {
            addCriterion("WX_OPENID_S like", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSNotLike(String value) {
            addCriterion("WX_OPENID_S not like", value, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSIn(List<String> values) {
            addCriterion("WX_OPENID_S in", values, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSNotIn(List<String> values) {
            addCriterion("WX_OPENID_S not in", values, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSBetween(String value1, String value2) {
            addCriterion("WX_OPENID_S between", value1, value2, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxOpenidSNotBetween(String value1, String value2) {
            addCriterion("WX_OPENID_S not between", value1, value2, "wxOpenidS");
            return (Criteria) this;
        }

        public Criteria andWxIdSIsNull() {
            addCriterion("WX_ID_S is null");
            return (Criteria) this;
        }

        public Criteria andWxIdSIsNotNull() {
            addCriterion("WX_ID_S is not null");
            return (Criteria) this;
        }

        public Criteria andWxIdSEqualTo(String value) {
            addCriterion("WX_ID_S =", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSNotEqualTo(String value) {
            addCriterion("WX_ID_S <>", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSGreaterThan(String value) {
            addCriterion("WX_ID_S >", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSGreaterThanOrEqualTo(String value) {
            addCriterion("WX_ID_S >=", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSLessThan(String value) {
            addCriterion("WX_ID_S <", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSLessThanOrEqualTo(String value) {
            addCriterion("WX_ID_S <=", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSLike(String value) {
            addCriterion("WX_ID_S like", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSNotLike(String value) {
            addCriterion("WX_ID_S not like", value, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSIn(List<String> values) {
            addCriterion("WX_ID_S in", values, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSNotIn(List<String> values) {
            addCriterion("WX_ID_S not in", values, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSBetween(String value1, String value2) {
            addCriterion("WX_ID_S between", value1, value2, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxIdSNotBetween(String value1, String value2) {
            addCriterion("WX_ID_S not between", value1, value2, "wxIdS");
            return (Criteria) this;
        }

        public Criteria andWxNickNameIsNull() {
            addCriterion("WX_NICK_NAME is null");
            return (Criteria) this;
        }

        public Criteria andWxNickNameIsNotNull() {
            addCriterion("WX_NICK_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andWxNickNameEqualTo(String value) {
            addCriterion("WX_NICK_NAME =", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameNotEqualTo(String value) {
            addCriterion("WX_NICK_NAME <>", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameGreaterThan(String value) {
            addCriterion("WX_NICK_NAME >", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameGreaterThanOrEqualTo(String value) {
            addCriterion("WX_NICK_NAME >=", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameLessThan(String value) {
            addCriterion("WX_NICK_NAME <", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameLessThanOrEqualTo(String value) {
            addCriterion("WX_NICK_NAME <=", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameLike(String value) {
            addCriterion("WX_NICK_NAME like", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameNotLike(String value) {
            addCriterion("WX_NICK_NAME not like", value, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameIn(List<String> values) {
            addCriterion("WX_NICK_NAME in", values, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameNotIn(List<String> values) {
            addCriterion("WX_NICK_NAME not in", values, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameBetween(String value1, String value2) {
            addCriterion("WX_NICK_NAME between", value1, value2, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxNickNameNotBetween(String value1, String value2) {
            addCriterion("WX_NICK_NAME not between", value1, value2, "wxNickName");
            return (Criteria) this;
        }

        public Criteria andWxTypeIsNull() {
            addCriterion("WX_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andWxTypeIsNotNull() {
            addCriterion("WX_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andWxTypeEqualTo(Byte value) {
            addCriterion("WX_TYPE =", value, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeNotEqualTo(Byte value) {
            addCriterion("WX_TYPE <>", value, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeGreaterThan(Byte value) {
            addCriterion("WX_TYPE >", value, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("WX_TYPE >=", value, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeLessThan(Byte value) {
            addCriterion("WX_TYPE <", value, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeLessThanOrEqualTo(Byte value) {
            addCriterion("WX_TYPE <=", value, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeIn(List<Byte> values) {
            addCriterion("WX_TYPE in", values, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeNotIn(List<Byte> values) {
            addCriterion("WX_TYPE not in", values, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeBetween(Byte value1, Byte value2) {
            addCriterion("WX_TYPE between", value1, value2, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("WX_TYPE not between", value1, value2, "wxType");
            return (Criteria) this;
        }

        public Criteria andWxAppIdIsNull() {
            addCriterion("WX_APP_ID is null");
            return (Criteria) this;
        }

        public Criteria andWxAppIdIsNotNull() {
            addCriterion("WX_APP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andWxAppIdEqualTo(String value) {
            addCriterion("WX_APP_ID =", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotEqualTo(String value) {
            addCriterion("WX_APP_ID <>", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdGreaterThan(String value) {
            addCriterion("WX_APP_ID >", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("WX_APP_ID >=", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdLessThan(String value) {
            addCriterion("WX_APP_ID <", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdLessThanOrEqualTo(String value) {
            addCriterion("WX_APP_ID <=", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdLike(String value) {
            addCriterion("WX_APP_ID like", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotLike(String value) {
            addCriterion("WX_APP_ID not like", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdIn(List<String> values) {
            addCriterion("WX_APP_ID in", values, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotIn(List<String> values) {
            addCriterion("WX_APP_ID not in", values, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdBetween(String value1, String value2) {
            addCriterion("WX_APP_ID between", value1, value2, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotBetween(String value1, String value2) {
            addCriterion("WX_APP_ID not between", value1, value2, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretIsNull() {
            addCriterion("WX_APP_SECRET is null");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretIsNotNull() {
            addCriterion("WX_APP_SECRET is not null");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretEqualTo(String value) {
            addCriterion("WX_APP_SECRET =", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretNotEqualTo(String value) {
            addCriterion("WX_APP_SECRET <>", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretGreaterThan(String value) {
            addCriterion("WX_APP_SECRET >", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretGreaterThanOrEqualTo(String value) {
            addCriterion("WX_APP_SECRET >=", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretLessThan(String value) {
            addCriterion("WX_APP_SECRET <", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretLessThanOrEqualTo(String value) {
            addCriterion("WX_APP_SECRET <=", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretLike(String value) {
            addCriterion("WX_APP_SECRET like", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretNotLike(String value) {
            addCriterion("WX_APP_SECRET not like", value, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretIn(List<String> values) {
            addCriterion("WX_APP_SECRET in", values, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretNotIn(List<String> values) {
            addCriterion("WX_APP_SECRET not in", values, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretBetween(String value1, String value2) {
            addCriterion("WX_APP_SECRET between", value1, value2, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxAppSecretNotBetween(String value1, String value2) {
            addCriterion("WX_APP_SECRET not between", value1, value2, "wxAppSecret");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlIsNull() {
            addCriterion("WX_API_URL is null");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlIsNotNull() {
            addCriterion("WX_API_URL is not null");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlEqualTo(String value) {
            addCriterion("WX_API_URL =", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlNotEqualTo(String value) {
            addCriterion("WX_API_URL <>", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlGreaterThan(String value) {
            addCriterion("WX_API_URL >", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlGreaterThanOrEqualTo(String value) {
            addCriterion("WX_API_URL >=", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlLessThan(String value) {
            addCriterion("WX_API_URL <", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlLessThanOrEqualTo(String value) {
            addCriterion("WX_API_URL <=", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlLike(String value) {
            addCriterion("WX_API_URL like", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlNotLike(String value) {
            addCriterion("WX_API_URL not like", value, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlIn(List<String> values) {
            addCriterion("WX_API_URL in", values, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlNotIn(List<String> values) {
            addCriterion("WX_API_URL not in", values, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlBetween(String value1, String value2) {
            addCriterion("WX_API_URL between", value1, value2, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiUrlNotBetween(String value1, String value2) {
            addCriterion("WX_API_URL not between", value1, value2, "wxApiUrl");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenIsNull() {
            addCriterion("WX_API_TOKEN is null");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenIsNotNull() {
            addCriterion("WX_API_TOKEN is not null");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenEqualTo(String value) {
            addCriterion("WX_API_TOKEN =", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenNotEqualTo(String value) {
            addCriterion("WX_API_TOKEN <>", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenGreaterThan(String value) {
            addCriterion("WX_API_TOKEN >", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenGreaterThanOrEqualTo(String value) {
            addCriterion("WX_API_TOKEN >=", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenLessThan(String value) {
            addCriterion("WX_API_TOKEN <", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenLessThanOrEqualTo(String value) {
            addCriterion("WX_API_TOKEN <=", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenLike(String value) {
            addCriterion("WX_API_TOKEN like", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenNotLike(String value) {
            addCriterion("WX_API_TOKEN not like", value, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenIn(List<String> values) {
            addCriterion("WX_API_TOKEN in", values, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenNotIn(List<String> values) {
            addCriterion("WX_API_TOKEN not in", values, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenBetween(String value1, String value2) {
            addCriterion("WX_API_TOKEN between", value1, value2, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxApiTokenNotBetween(String value1, String value2) {
            addCriterion("WX_API_TOKEN not between", value1, value2, "wxApiToken");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowIsNull() {
            addCriterion("WX_IS_USE_QUIK_FOLLOW is null");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowIsNotNull() {
            addCriterion("WX_IS_USE_QUIK_FOLLOW is not null");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowEqualTo(Byte value) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW =", value, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowNotEqualTo(Byte value) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW <>", value, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowGreaterThan(Byte value) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW >", value, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowGreaterThanOrEqualTo(Byte value) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW >=", value, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowLessThan(Byte value) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW <", value, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowLessThanOrEqualTo(Byte value) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW <=", value, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowIn(List<Byte> values) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW in", values, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowNotIn(List<Byte> values) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW not in", values, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowBetween(Byte value1, Byte value2) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW between", value1, value2, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andWxIsUseQuikFollowNotBetween(Byte value1, Byte value2) {
            addCriterion("WX_IS_USE_QUIK_FOLLOW not between", value1, value2, "wxIsUseQuikFollow");
            return (Criteria) this;
        }

        public Criteria andCorpNmIsNull() {
            addCriterion("CORP_NM is null");
            return (Criteria) this;
        }

        public Criteria andCorpNmIsNotNull() {
            addCriterion("CORP_NM is not null");
            return (Criteria) this;
        }

        public Criteria andCorpNmEqualTo(String value) {
            addCriterion("CORP_NM =", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmNotEqualTo(String value) {
            addCriterion("CORP_NM <>", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmGreaterThan(String value) {
            addCriterion("CORP_NM >", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmGreaterThanOrEqualTo(String value) {
            addCriterion("CORP_NM >=", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmLessThan(String value) {
            addCriterion("CORP_NM <", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmLessThanOrEqualTo(String value) {
            addCriterion("CORP_NM <=", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmLike(String value) {
            addCriterion("CORP_NM like", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmNotLike(String value) {
            addCriterion("CORP_NM not like", value, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmIn(List<String> values) {
            addCriterion("CORP_NM in", values, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmNotIn(List<String> values) {
            addCriterion("CORP_NM not in", values, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmBetween(String value1, String value2) {
            addCriterion("CORP_NM between", value1, value2, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpNmNotBetween(String value1, String value2) {
            addCriterion("CORP_NM not between", value1, value2, "corpNm");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoIsNull() {
            addCriterion("CORP_LICENSE_NO is null");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoIsNotNull() {
            addCriterion("CORP_LICENSE_NO is not null");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoEqualTo(String value) {
            addCriterion("CORP_LICENSE_NO =", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoNotEqualTo(String value) {
            addCriterion("CORP_LICENSE_NO <>", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoGreaterThan(String value) {
            addCriterion("CORP_LICENSE_NO >", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoGreaterThanOrEqualTo(String value) {
            addCriterion("CORP_LICENSE_NO >=", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoLessThan(String value) {
            addCriterion("CORP_LICENSE_NO <", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoLessThanOrEqualTo(String value) {
            addCriterion("CORP_LICENSE_NO <=", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoLike(String value) {
            addCriterion("CORP_LICENSE_NO like", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoNotLike(String value) {
            addCriterion("CORP_LICENSE_NO not like", value, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoIn(List<String> values) {
            addCriterion("CORP_LICENSE_NO in", values, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoNotIn(List<String> values) {
            addCriterion("CORP_LICENSE_NO not in", values, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoBetween(String value1, String value2) {
            addCriterion("CORP_LICENSE_NO between", value1, value2, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andCorpLicenseNoNotBetween(String value1, String value2) {
            addCriterion("CORP_LICENSE_NO not between", value1, value2, "corpLicenseNo");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmIsNull() {
            addCriterion("LEGAL_PERSON_NM is null");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmIsNotNull() {
            addCriterion("LEGAL_PERSON_NM is not null");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmEqualTo(String value) {
            addCriterion("LEGAL_PERSON_NM =", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmNotEqualTo(String value) {
            addCriterion("LEGAL_PERSON_NM <>", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmGreaterThan(String value) {
            addCriterion("LEGAL_PERSON_NM >", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmGreaterThanOrEqualTo(String value) {
            addCriterion("LEGAL_PERSON_NM >=", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmLessThan(String value) {
            addCriterion("LEGAL_PERSON_NM <", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmLessThanOrEqualTo(String value) {
            addCriterion("LEGAL_PERSON_NM <=", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmLike(String value) {
            addCriterion("LEGAL_PERSON_NM like", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmNotLike(String value) {
            addCriterion("LEGAL_PERSON_NM not like", value, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmIn(List<String> values) {
            addCriterion("LEGAL_PERSON_NM in", values, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmNotIn(List<String> values) {
            addCriterion("LEGAL_PERSON_NM not in", values, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmBetween(String value1, String value2) {
            addCriterion("LEGAL_PERSON_NM between", value1, value2, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNmNotBetween(String value1, String value2) {
            addCriterion("LEGAL_PERSON_NM not between", value1, value2, "legalPersonNm");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardIsNull() {
            addCriterion("LEGAL_PERSON_IDCARD is null");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardIsNotNull() {
            addCriterion("LEGAL_PERSON_IDCARD is not null");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardEqualTo(String value) {
            addCriterion("LEGAL_PERSON_IDCARD =", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardNotEqualTo(String value) {
            addCriterion("LEGAL_PERSON_IDCARD <>", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardGreaterThan(String value) {
            addCriterion("LEGAL_PERSON_IDCARD >", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardGreaterThanOrEqualTo(String value) {
            addCriterion("LEGAL_PERSON_IDCARD >=", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardLessThan(String value) {
            addCriterion("LEGAL_PERSON_IDCARD <", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardLessThanOrEqualTo(String value) {
            addCriterion("LEGAL_PERSON_IDCARD <=", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardLike(String value) {
            addCriterion("LEGAL_PERSON_IDCARD like", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardNotLike(String value) {
            addCriterion("LEGAL_PERSON_IDCARD not like", value, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardIn(List<String> values) {
            addCriterion("LEGAL_PERSON_IDCARD in", values, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardNotIn(List<String> values) {
            addCriterion("LEGAL_PERSON_IDCARD not in", values, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardBetween(String value1, String value2) {
            addCriterion("LEGAL_PERSON_IDCARD between", value1, value2, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIdcardNotBetween(String value1, String value2) {
            addCriterion("LEGAL_PERSON_IDCARD not between", value1, value2, "legalPersonIdcard");
            return (Criteria) this;
        }

        public Criteria andContactNmIsNull() {
            addCriterion("CONTACT_NM is null");
            return (Criteria) this;
        }

        public Criteria andContactNmIsNotNull() {
            addCriterion("CONTACT_NM is not null");
            return (Criteria) this;
        }

        public Criteria andContactNmEqualTo(String value) {
            addCriterion("CONTACT_NM =", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmNotEqualTo(String value) {
            addCriterion("CONTACT_NM <>", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmGreaterThan(String value) {
            addCriterion("CONTACT_NM >", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmGreaterThanOrEqualTo(String value) {
            addCriterion("CONTACT_NM >=", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmLessThan(String value) {
            addCriterion("CONTACT_NM <", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmLessThanOrEqualTo(String value) {
            addCriterion("CONTACT_NM <=", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmLike(String value) {
            addCriterion("CONTACT_NM like", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmNotLike(String value) {
            addCriterion("CONTACT_NM not like", value, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmIn(List<String> values) {
            addCriterion("CONTACT_NM in", values, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmNotIn(List<String> values) {
            addCriterion("CONTACT_NM not in", values, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmBetween(String value1, String value2) {
            addCriterion("CONTACT_NM between", value1, value2, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactNmNotBetween(String value1, String value2) {
            addCriterion("CONTACT_NM not between", value1, value2, "contactNm");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoIsNull() {
            addCriterion("CONTACT_MOBILE_NO is null");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoIsNotNull() {
            addCriterion("CONTACT_MOBILE_NO is not null");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoEqualTo(String value) {
            addCriterion("CONTACT_MOBILE_NO =", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoNotEqualTo(String value) {
            addCriterion("CONTACT_MOBILE_NO <>", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoGreaterThan(String value) {
            addCriterion("CONTACT_MOBILE_NO >", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoGreaterThanOrEqualTo(String value) {
            addCriterion("CONTACT_MOBILE_NO >=", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoLessThan(String value) {
            addCriterion("CONTACT_MOBILE_NO <", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoLessThanOrEqualTo(String value) {
            addCriterion("CONTACT_MOBILE_NO <=", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoLike(String value) {
            addCriterion("CONTACT_MOBILE_NO like", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoNotLike(String value) {
            addCriterion("CONTACT_MOBILE_NO not like", value, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoIn(List<String> values) {
            addCriterion("CONTACT_MOBILE_NO in", values, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoNotIn(List<String> values) {
            addCriterion("CONTACT_MOBILE_NO not in", values, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoBetween(String value1, String value2) {
            addCriterion("CONTACT_MOBILE_NO between", value1, value2, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactMobileNoNotBetween(String value1, String value2) {
            addCriterion("CONTACT_MOBILE_NO not between", value1, value2, "contactMobileNo");
            return (Criteria) this;
        }

        public Criteria andContactEmailIsNull() {
            addCriterion("CONTACT_EMAIL is null");
            return (Criteria) this;
        }

        public Criteria andContactEmailIsNotNull() {
            addCriterion("CONTACT_EMAIL is not null");
            return (Criteria) this;
        }

        public Criteria andContactEmailEqualTo(String value) {
            addCriterion("CONTACT_EMAIL =", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailNotEqualTo(String value) {
            addCriterion("CONTACT_EMAIL <>", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailGreaterThan(String value) {
            addCriterion("CONTACT_EMAIL >", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailGreaterThanOrEqualTo(String value) {
            addCriterion("CONTACT_EMAIL >=", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailLessThan(String value) {
            addCriterion("CONTACT_EMAIL <", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailLessThanOrEqualTo(String value) {
            addCriterion("CONTACT_EMAIL <=", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailLike(String value) {
            addCriterion("CONTACT_EMAIL like", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailNotLike(String value) {
            addCriterion("CONTACT_EMAIL not like", value, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailIn(List<String> values) {
            addCriterion("CONTACT_EMAIL in", values, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailNotIn(List<String> values) {
            addCriterion("CONTACT_EMAIL not in", values, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailBetween(String value1, String value2) {
            addCriterion("CONTACT_EMAIL between", value1, value2, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactEmailNotBetween(String value1, String value2) {
            addCriterion("CONTACT_EMAIL not between", value1, value2, "contactEmail");
            return (Criteria) this;
        }

        public Criteria andContactQqIsNull() {
            addCriterion("CONTACT_QQ is null");
            return (Criteria) this;
        }

        public Criteria andContactQqIsNotNull() {
            addCriterion("CONTACT_QQ is not null");
            return (Criteria) this;
        }

        public Criteria andContactQqEqualTo(String value) {
            addCriterion("CONTACT_QQ =", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqNotEqualTo(String value) {
            addCriterion("CONTACT_QQ <>", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqGreaterThan(String value) {
            addCriterion("CONTACT_QQ >", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqGreaterThanOrEqualTo(String value) {
            addCriterion("CONTACT_QQ >=", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqLessThan(String value) {
            addCriterion("CONTACT_QQ <", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqLessThanOrEqualTo(String value) {
            addCriterion("CONTACT_QQ <=", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqLike(String value) {
            addCriterion("CONTACT_QQ like", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqNotLike(String value) {
            addCriterion("CONTACT_QQ not like", value, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqIn(List<String> values) {
            addCriterion("CONTACT_QQ in", values, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqNotIn(List<String> values) {
            addCriterion("CONTACT_QQ not in", values, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqBetween(String value1, String value2) {
            addCriterion("CONTACT_QQ between", value1, value2, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactQqNotBetween(String value1, String value2) {
            addCriterion("CONTACT_QQ not between", value1, value2, "contactQq");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgIsNull() {
            addCriterion("CONTACT_HEADIMG is null");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgIsNotNull() {
            addCriterion("CONTACT_HEADIMG is not null");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgEqualTo(String value) {
            addCriterion("CONTACT_HEADIMG =", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgNotEqualTo(String value) {
            addCriterion("CONTACT_HEADIMG <>", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgGreaterThan(String value) {
            addCriterion("CONTACT_HEADIMG >", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgGreaterThanOrEqualTo(String value) {
            addCriterion("CONTACT_HEADIMG >=", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgLessThan(String value) {
            addCriterion("CONTACT_HEADIMG <", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgLessThanOrEqualTo(String value) {
            addCriterion("CONTACT_HEADIMG <=", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgLike(String value) {
            addCriterion("CONTACT_HEADIMG like", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgNotLike(String value) {
            addCriterion("CONTACT_HEADIMG not like", value, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgIn(List<String> values) {
            addCriterion("CONTACT_HEADIMG in", values, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgNotIn(List<String> values) {
            addCriterion("CONTACT_HEADIMG not in", values, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgBetween(String value1, String value2) {
            addCriterion("CONTACT_HEADIMG between", value1, value2, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andContactHeadimgNotBetween(String value1, String value2) {
            addCriterion("CONTACT_HEADIMG not between", value1, value2, "contactHeadimg");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdIsNull() {
            addCriterion("ADR_PROVINCE_ID is null");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdIsNotNull() {
            addCriterion("ADR_PROVINCE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdEqualTo(Integer value) {
            addCriterion("ADR_PROVINCE_ID =", value, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdNotEqualTo(Integer value) {
            addCriterion("ADR_PROVINCE_ID <>", value, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdGreaterThan(Integer value) {
            addCriterion("ADR_PROVINCE_ID >", value, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ADR_PROVINCE_ID >=", value, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdLessThan(Integer value) {
            addCriterion("ADR_PROVINCE_ID <", value, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdLessThanOrEqualTo(Integer value) {
            addCriterion("ADR_PROVINCE_ID <=", value, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdIn(List<Integer> values) {
            addCriterion("ADR_PROVINCE_ID in", values, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdNotIn(List<Integer> values) {
            addCriterion("ADR_PROVINCE_ID not in", values, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdBetween(Integer value1, Integer value2) {
            addCriterion("ADR_PROVINCE_ID between", value1, value2, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrProvinceIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ADR_PROVINCE_ID not between", value1, value2, "adrProvinceId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdIsNull() {
            addCriterion("ADR_CITY_ID is null");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdIsNotNull() {
            addCriterion("ADR_CITY_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdEqualTo(Integer value) {
            addCriterion("ADR_CITY_ID =", value, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdNotEqualTo(Integer value) {
            addCriterion("ADR_CITY_ID <>", value, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdGreaterThan(Integer value) {
            addCriterion("ADR_CITY_ID >", value, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ADR_CITY_ID >=", value, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdLessThan(Integer value) {
            addCriterion("ADR_CITY_ID <", value, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdLessThanOrEqualTo(Integer value) {
            addCriterion("ADR_CITY_ID <=", value, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdIn(List<Integer> values) {
            addCriterion("ADR_CITY_ID in", values, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdNotIn(List<Integer> values) {
            addCriterion("ADR_CITY_ID not in", values, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdBetween(Integer value1, Integer value2) {
            addCriterion("ADR_CITY_ID between", value1, value2, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrCityIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ADR_CITY_ID not between", value1, value2, "adrCityId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdIsNull() {
            addCriterion("ADR_DISTRICT_ID is null");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdIsNotNull() {
            addCriterion("ADR_DISTRICT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdEqualTo(Integer value) {
            addCriterion("ADR_DISTRICT_ID =", value, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdNotEqualTo(Integer value) {
            addCriterion("ADR_DISTRICT_ID <>", value, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdGreaterThan(Integer value) {
            addCriterion("ADR_DISTRICT_ID >", value, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ADR_DISTRICT_ID >=", value, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdLessThan(Integer value) {
            addCriterion("ADR_DISTRICT_ID <", value, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdLessThanOrEqualTo(Integer value) {
            addCriterion("ADR_DISTRICT_ID <=", value, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdIn(List<Integer> values) {
            addCriterion("ADR_DISTRICT_ID in", values, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdNotIn(List<Integer> values) {
            addCriterion("ADR_DISTRICT_ID not in", values, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdBetween(Integer value1, Integer value2) {
            addCriterion("ADR_DISTRICT_ID between", value1, value2, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDistrictIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ADR_DISTRICT_ID not between", value1, value2, "adrDistrictId");
            return (Criteria) this;
        }

        public Criteria andAdrDetailIsNull() {
            addCriterion("ADR_DETAIL is null");
            return (Criteria) this;
        }

        public Criteria andAdrDetailIsNotNull() {
            addCriterion("ADR_DETAIL is not null");
            return (Criteria) this;
        }

        public Criteria andAdrDetailEqualTo(String value) {
            addCriterion("ADR_DETAIL =", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailNotEqualTo(String value) {
            addCriterion("ADR_DETAIL <>", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailGreaterThan(String value) {
            addCriterion("ADR_DETAIL >", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailGreaterThanOrEqualTo(String value) {
            addCriterion("ADR_DETAIL >=", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailLessThan(String value) {
            addCriterion("ADR_DETAIL <", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailLessThanOrEqualTo(String value) {
            addCriterion("ADR_DETAIL <=", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailLike(String value) {
            addCriterion("ADR_DETAIL like", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailNotLike(String value) {
            addCriterion("ADR_DETAIL not like", value, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailIn(List<String> values) {
            addCriterion("ADR_DETAIL in", values, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailNotIn(List<String> values) {
            addCriterion("ADR_DETAIL not in", values, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailBetween(String value1, String value2) {
            addCriterion("ADR_DETAIL between", value1, value2, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrDetailNotBetween(String value1, String value2) {
            addCriterion("ADR_DETAIL not between", value1, value2, "adrDetail");
            return (Criteria) this;
        }

        public Criteria andAdrZipIsNull() {
            addCriterion("ADR_ZIP is null");
            return (Criteria) this;
        }

        public Criteria andAdrZipIsNotNull() {
            addCriterion("ADR_ZIP is not null");
            return (Criteria) this;
        }

        public Criteria andAdrZipEqualTo(String value) {
            addCriterion("ADR_ZIP =", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipNotEqualTo(String value) {
            addCriterion("ADR_ZIP <>", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipGreaterThan(String value) {
            addCriterion("ADR_ZIP >", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipGreaterThanOrEqualTo(String value) {
            addCriterion("ADR_ZIP >=", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipLessThan(String value) {
            addCriterion("ADR_ZIP <", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipLessThanOrEqualTo(String value) {
            addCriterion("ADR_ZIP <=", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipLike(String value) {
            addCriterion("ADR_ZIP like", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipNotLike(String value) {
            addCriterion("ADR_ZIP not like", value, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipIn(List<String> values) {
            addCriterion("ADR_ZIP in", values, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipNotIn(List<String> values) {
            addCriterion("ADR_ZIP not in", values, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipBetween(String value1, String value2) {
            addCriterion("ADR_ZIP between", value1, value2, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrZipNotBetween(String value1, String value2) {
            addCriterion("ADR_ZIP not between", value1, value2, "adrZip");
            return (Criteria) this;
        }

        public Criteria andAdrTelIsNull() {
            addCriterion("ADR_TEL is null");
            return (Criteria) this;
        }

        public Criteria andAdrTelIsNotNull() {
            addCriterion("ADR_TEL is not null");
            return (Criteria) this;
        }

        public Criteria andAdrTelEqualTo(String value) {
            addCriterion("ADR_TEL =", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelNotEqualTo(String value) {
            addCriterion("ADR_TEL <>", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelGreaterThan(String value) {
            addCriterion("ADR_TEL >", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelGreaterThanOrEqualTo(String value) {
            addCriterion("ADR_TEL >=", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelLessThan(String value) {
            addCriterion("ADR_TEL <", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelLessThanOrEqualTo(String value) {
            addCriterion("ADR_TEL <=", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelLike(String value) {
            addCriterion("ADR_TEL like", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelNotLike(String value) {
            addCriterion("ADR_TEL not like", value, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelIn(List<String> values) {
            addCriterion("ADR_TEL in", values, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelNotIn(List<String> values) {
            addCriterion("ADR_TEL not in", values, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelBetween(String value1, String value2) {
            addCriterion("ADR_TEL between", value1, value2, "adrTel");
            return (Criteria) this;
        }

        public Criteria andAdrTelNotBetween(String value1, String value2) {
            addCriterion("ADR_TEL not between", value1, value2, "adrTel");
            return (Criteria) this;
        }

        public Criteria andScIdIsNull() {
            addCriterion("SC_ID is null");
            return (Criteria) this;
        }

        public Criteria andScIdIsNotNull() {
            addCriterion("SC_ID is not null");
            return (Criteria) this;
        }

        public Criteria andScIdEqualTo(Integer value) {
            addCriterion("SC_ID =", value, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdNotEqualTo(Integer value) {
            addCriterion("SC_ID <>", value, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdGreaterThan(Integer value) {
            addCriterion("SC_ID >", value, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("SC_ID >=", value, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdLessThan(Integer value) {
            addCriterion("SC_ID <", value, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdLessThanOrEqualTo(Integer value) {
            addCriterion("SC_ID <=", value, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdIn(List<Integer> values) {
            addCriterion("SC_ID in", values, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdNotIn(List<Integer> values) {
            addCriterion("SC_ID not in", values, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdBetween(Integer value1, Integer value2) {
            addCriterion("SC_ID between", value1, value2, "scId");
            return (Criteria) this;
        }

        public Criteria andScIdNotBetween(Integer value1, Integer value2) {
            addCriterion("SC_ID not between", value1, value2, "scId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdIsNull() {
            addCriterion("MAIN_GC_ID is null");
            return (Criteria) this;
        }

        public Criteria andMainGcIdIsNotNull() {
            addCriterion("MAIN_GC_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMainGcIdEqualTo(Integer value) {
            addCriterion("MAIN_GC_ID =", value, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdNotEqualTo(Integer value) {
            addCriterion("MAIN_GC_ID <>", value, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdGreaterThan(Integer value) {
            addCriterion("MAIN_GC_ID >", value, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("MAIN_GC_ID >=", value, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdLessThan(Integer value) {
            addCriterion("MAIN_GC_ID <", value, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdLessThanOrEqualTo(Integer value) {
            addCriterion("MAIN_GC_ID <=", value, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdIn(List<Integer> values) {
            addCriterion("MAIN_GC_ID in", values, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdNotIn(List<Integer> values) {
            addCriterion("MAIN_GC_ID not in", values, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdBetween(Integer value1, Integer value2) {
            addCriterion("MAIN_GC_ID between", value1, value2, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andMainGcIdNotBetween(Integer value1, Integer value2) {
            addCriterion("MAIN_GC_ID not between", value1, value2, "mainGcId");
            return (Criteria) this;
        }

        public Criteria andSgIdIsNull() {
            addCriterion("SG_ID is null");
            return (Criteria) this;
        }

        public Criteria andSgIdIsNotNull() {
            addCriterion("SG_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSgIdEqualTo(Integer value) {
            addCriterion("SG_ID =", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotEqualTo(Integer value) {
            addCriterion("SG_ID <>", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdGreaterThan(Integer value) {
            addCriterion("SG_ID >", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("SG_ID >=", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdLessThan(Integer value) {
            addCriterion("SG_ID <", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdLessThanOrEqualTo(Integer value) {
            addCriterion("SG_ID <=", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdIn(List<Integer> values) {
            addCriterion("SG_ID in", values, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotIn(List<Integer> values) {
            addCriterion("SG_ID not in", values, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdBetween(Integer value1, Integer value2) {
            addCriterion("SG_ID between", value1, value2, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("SG_ID not between", value1, value2, "sgId");
            return (Criteria) this;
        }

        public Criteria andStsIdIsNull() {
            addCriterion("STS_ID is null");
            return (Criteria) this;
        }

        public Criteria andStsIdIsNotNull() {
            addCriterion("STS_ID is not null");
            return (Criteria) this;
        }

        public Criteria andStsIdEqualTo(Byte value) {
            addCriterion("STS_ID =", value, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdNotEqualTo(Byte value) {
            addCriterion("STS_ID <>", value, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdGreaterThan(Byte value) {
            addCriterion("STS_ID >", value, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdGreaterThanOrEqualTo(Byte value) {
            addCriterion("STS_ID >=", value, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdLessThan(Byte value) {
            addCriterion("STS_ID <", value, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdLessThanOrEqualTo(Byte value) {
            addCriterion("STS_ID <=", value, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdIn(List<Byte> values) {
            addCriterion("STS_ID in", values, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdNotIn(List<Byte> values) {
            addCriterion("STS_ID not in", values, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdBetween(Byte value1, Byte value2) {
            addCriterion("STS_ID between", value1, value2, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsIdNotBetween(Byte value1, Byte value2) {
            addCriterion("STS_ID not between", value1, value2, "stsId");
            return (Criteria) this;
        }

        public Criteria andStsDescIsNull() {
            addCriterion("STS_DESC is null");
            return (Criteria) this;
        }

        public Criteria andStsDescIsNotNull() {
            addCriterion("STS_DESC is not null");
            return (Criteria) this;
        }

        public Criteria andStsDescEqualTo(String value) {
            addCriterion("STS_DESC =", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescNotEqualTo(String value) {
            addCriterion("STS_DESC <>", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescGreaterThan(String value) {
            addCriterion("STS_DESC >", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescGreaterThanOrEqualTo(String value) {
            addCriterion("STS_DESC >=", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescLessThan(String value) {
            addCriterion("STS_DESC <", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescLessThanOrEqualTo(String value) {
            addCriterion("STS_DESC <=", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescLike(String value) {
            addCriterion("STS_DESC like", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescNotLike(String value) {
            addCriterion("STS_DESC not like", value, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescIn(List<String> values) {
            addCriterion("STS_DESC in", values, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescNotIn(List<String> values) {
            addCriterion("STS_DESC not in", values, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescBetween(String value1, String value2) {
            addCriterion("STS_DESC between", value1, value2, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andStsDescNotBetween(String value1, String value2) {
            addCriterion("STS_DESC not between", value1, value2, "stsDesc");
            return (Criteria) this;
        }

        public Criteria andOpenedDtIsNull() {
            addCriterion("OPENED_DT is null");
            return (Criteria) this;
        }

        public Criteria andOpenedDtIsNotNull() {
            addCriterion("OPENED_DT is not null");
            return (Criteria) this;
        }

        public Criteria andOpenedDtEqualTo(Date value) {
            addCriterion("OPENED_DT =", value, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtNotEqualTo(Date value) {
            addCriterion("OPENED_DT <>", value, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtGreaterThan(Date value) {
            addCriterion("OPENED_DT >", value, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtGreaterThanOrEqualTo(Date value) {
            addCriterion("OPENED_DT >=", value, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtLessThan(Date value) {
            addCriterion("OPENED_DT <", value, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtLessThanOrEqualTo(Date value) {
            addCriterion("OPENED_DT <=", value, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtIn(List<Date> values) {
            addCriterion("OPENED_DT in", values, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtNotIn(List<Date> values) {
            addCriterion("OPENED_DT not in", values, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtBetween(Date value1, Date value2) {
            addCriterion("OPENED_DT between", value1, value2, "openedDt");
            return (Criteria) this;
        }

        public Criteria andOpenedDtNotBetween(Date value1, Date value2) {
            addCriterion("OPENED_DT not between", value1, value2, "openedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtIsNull() {
            addCriterion("CLOSED_DT is null");
            return (Criteria) this;
        }

        public Criteria andClosedDtIsNotNull() {
            addCriterion("CLOSED_DT is not null");
            return (Criteria) this;
        }

        public Criteria andClosedDtEqualTo(Date value) {
            addCriterion("CLOSED_DT =", value, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtNotEqualTo(Date value) {
            addCriterion("CLOSED_DT <>", value, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtGreaterThan(Date value) {
            addCriterion("CLOSED_DT >", value, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtGreaterThanOrEqualTo(Date value) {
            addCriterion("CLOSED_DT >=", value, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtLessThan(Date value) {
            addCriterion("CLOSED_DT <", value, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtLessThanOrEqualTo(Date value) {
            addCriterion("CLOSED_DT <=", value, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtIn(List<Date> values) {
            addCriterion("CLOSED_DT in", values, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtNotIn(List<Date> values) {
            addCriterion("CLOSED_DT not in", values, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtBetween(Date value1, Date value2) {
            addCriterion("CLOSED_DT between", value1, value2, "closedDt");
            return (Criteria) this;
        }

        public Criteria andClosedDtNotBetween(Date value1, Date value2) {
            addCriterion("CLOSED_DT not between", value1, value2, "closedDt");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdIsNull() {
            addCriterion("AUTH_TYPE_ID is null");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdIsNotNull() {
            addCriterion("AUTH_TYPE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdEqualTo(Byte value) {
            addCriterion("AUTH_TYPE_ID =", value, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdNotEqualTo(Byte value) {
            addCriterion("AUTH_TYPE_ID <>", value, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdGreaterThan(Byte value) {
            addCriterion("AUTH_TYPE_ID >", value, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdGreaterThanOrEqualTo(Byte value) {
            addCriterion("AUTH_TYPE_ID >=", value, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdLessThan(Byte value) {
            addCriterion("AUTH_TYPE_ID <", value, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdLessThanOrEqualTo(Byte value) {
            addCriterion("AUTH_TYPE_ID <=", value, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdIn(List<Byte> values) {
            addCriterion("AUTH_TYPE_ID in", values, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdNotIn(List<Byte> values) {
            addCriterion("AUTH_TYPE_ID not in", values, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdBetween(Byte value1, Byte value2) {
            addCriterion("AUTH_TYPE_ID between", value1, value2, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthTypeIdNotBetween(Byte value1, Byte value2) {
            addCriterion("AUTH_TYPE_ID not between", value1, value2, "authTypeId");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccIsNull() {
            addCriterion("AUTH_IS_SUCC is null");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccIsNotNull() {
            addCriterion("AUTH_IS_SUCC is not null");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccEqualTo(Byte value) {
            addCriterion("AUTH_IS_SUCC =", value, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccNotEqualTo(Byte value) {
            addCriterion("AUTH_IS_SUCC <>", value, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccGreaterThan(Byte value) {
            addCriterion("AUTH_IS_SUCC >", value, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccGreaterThanOrEqualTo(Byte value) {
            addCriterion("AUTH_IS_SUCC >=", value, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccLessThan(Byte value) {
            addCriterion("AUTH_IS_SUCC <", value, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccLessThanOrEqualTo(Byte value) {
            addCriterion("AUTH_IS_SUCC <=", value, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccIn(List<Byte> values) {
            addCriterion("AUTH_IS_SUCC in", values, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccNotIn(List<Byte> values) {
            addCriterion("AUTH_IS_SUCC not in", values, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccBetween(Byte value1, Byte value2) {
            addCriterion("AUTH_IS_SUCC between", value1, value2, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthIsSuccNotBetween(Byte value1, Byte value2) {
            addCriterion("AUTH_IS_SUCC not between", value1, value2, "authIsSucc");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1IsNull() {
            addCriterion("AUTH_IMG_CERT_1 is null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1IsNotNull() {
            addCriterion("AUTH_IMG_CERT_1 is not null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1EqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_1 =", value, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1NotEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_1 <>", value, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1GreaterThan(Long value) {
            addCriterion("AUTH_IMG_CERT_1 >", value, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1GreaterThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_1 >=", value, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1LessThan(Long value) {
            addCriterion("AUTH_IMG_CERT_1 <", value, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1LessThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_1 <=", value, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1In(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_1 in", values, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1NotIn(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_1 not in", values, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1Between(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_1 between", value1, value2, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert1NotBetween(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_1 not between", value1, value2, "authImgCert1");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2IsNull() {
            addCriterion("AUTH_IMG_CERT_2 is null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2IsNotNull() {
            addCriterion("AUTH_IMG_CERT_2 is not null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2EqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_2 =", value, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2NotEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_2 <>", value, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2GreaterThan(Long value) {
            addCriterion("AUTH_IMG_CERT_2 >", value, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2GreaterThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_2 >=", value, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2LessThan(Long value) {
            addCriterion("AUTH_IMG_CERT_2 <", value, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2LessThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_2 <=", value, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2In(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_2 in", values, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2NotIn(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_2 not in", values, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2Between(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_2 between", value1, value2, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert2NotBetween(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_2 not between", value1, value2, "authImgCert2");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3IsNull() {
            addCriterion("AUTH_IMG_CERT_3 is null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3IsNotNull() {
            addCriterion("AUTH_IMG_CERT_3 is not null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3EqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_3 =", value, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3NotEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_3 <>", value, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3GreaterThan(Long value) {
            addCriterion("AUTH_IMG_CERT_3 >", value, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3GreaterThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_3 >=", value, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3LessThan(Long value) {
            addCriterion("AUTH_IMG_CERT_3 <", value, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3LessThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_3 <=", value, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3In(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_3 in", values, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3NotIn(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_3 not in", values, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3Between(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_3 between", value1, value2, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert3NotBetween(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_3 not between", value1, value2, "authImgCert3");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4IsNull() {
            addCriterion("AUTH_IMG_CERT_4 is null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4IsNotNull() {
            addCriterion("AUTH_IMG_CERT_4 is not null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4EqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_4 =", value, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4NotEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_4 <>", value, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4GreaterThan(Long value) {
            addCriterion("AUTH_IMG_CERT_4 >", value, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4GreaterThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_4 >=", value, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4LessThan(Long value) {
            addCriterion("AUTH_IMG_CERT_4 <", value, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4LessThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_4 <=", value, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4In(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_4 in", values, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4NotIn(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_4 not in", values, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4Between(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_4 between", value1, value2, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert4NotBetween(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_4 not between", value1, value2, "authImgCert4");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5IsNull() {
            addCriterion("AUTH_IMG_CERT_5 is null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5IsNotNull() {
            addCriterion("AUTH_IMG_CERT_5 is not null");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5EqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_5 =", value, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5NotEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_5 <>", value, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5GreaterThan(Long value) {
            addCriterion("AUTH_IMG_CERT_5 >", value, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5GreaterThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_5 >=", value, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5LessThan(Long value) {
            addCriterion("AUTH_IMG_CERT_5 <", value, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5LessThanOrEqualTo(Long value) {
            addCriterion("AUTH_IMG_CERT_5 <=", value, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5In(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_5 in", values, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5NotIn(List<Long> values) {
            addCriterion("AUTH_IMG_CERT_5 not in", values, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5Between(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_5 between", value1, value2, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andAuthImgCert5NotBetween(Long value1, Long value2) {
            addCriterion("AUTH_IMG_CERT_5 not between", value1, value2, "authImgCert5");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicIsNull() {
            addCriterion("IMG_LOGO_PIC is null");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicIsNotNull() {
            addCriterion("IMG_LOGO_PIC is not null");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicEqualTo(Long value) {
            addCriterion("IMG_LOGO_PIC =", value, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicNotEqualTo(Long value) {
            addCriterion("IMG_LOGO_PIC <>", value, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicGreaterThan(Long value) {
            addCriterion("IMG_LOGO_PIC >", value, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicGreaterThanOrEqualTo(Long value) {
            addCriterion("IMG_LOGO_PIC >=", value, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicLessThan(Long value) {
            addCriterion("IMG_LOGO_PIC <", value, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicLessThanOrEqualTo(Long value) {
            addCriterion("IMG_LOGO_PIC <=", value, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicIn(List<Long> values) {
            addCriterion("IMG_LOGO_PIC in", values, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicNotIn(List<Long> values) {
            addCriterion("IMG_LOGO_PIC not in", values, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicBetween(Long value1, Long value2) {
            addCriterion("IMG_LOGO_PIC between", value1, value2, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoPicNotBetween(Long value1, Long value2) {
            addCriterion("IMG_LOGO_PIC not between", value1, value2, "imgLogoPic");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelIsNull() {
            addCriterion("IMG_LOGO_LABEL is null");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelIsNotNull() {
            addCriterion("IMG_LOGO_LABEL is not null");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelEqualTo(Long value) {
            addCriterion("IMG_LOGO_LABEL =", value, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelNotEqualTo(Long value) {
            addCriterion("IMG_LOGO_LABEL <>", value, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelGreaterThan(Long value) {
            addCriterion("IMG_LOGO_LABEL >", value, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelGreaterThanOrEqualTo(Long value) {
            addCriterion("IMG_LOGO_LABEL >=", value, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelLessThan(Long value) {
            addCriterion("IMG_LOGO_LABEL <", value, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelLessThanOrEqualTo(Long value) {
            addCriterion("IMG_LOGO_LABEL <=", value, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelIn(List<Long> values) {
            addCriterion("IMG_LOGO_LABEL in", values, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelNotIn(List<Long> values) {
            addCriterion("IMG_LOGO_LABEL not in", values, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelBetween(Long value1, Long value2) {
            addCriterion("IMG_LOGO_LABEL between", value1, value2, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgLogoLabelNotBetween(Long value1, Long value2) {
            addCriterion("IMG_LOGO_LABEL not between", value1, value2, "imgLogoLabel");
            return (Criteria) this;
        }

        public Criteria andImgBannerIsNull() {
            addCriterion("IMG_BANNER is null");
            return (Criteria) this;
        }

        public Criteria andImgBannerIsNotNull() {
            addCriterion("IMG_BANNER is not null");
            return (Criteria) this;
        }

        public Criteria andImgBannerEqualTo(Long value) {
            addCriterion("IMG_BANNER =", value, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerNotEqualTo(Long value) {
            addCriterion("IMG_BANNER <>", value, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerGreaterThan(Long value) {
            addCriterion("IMG_BANNER >", value, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerGreaterThanOrEqualTo(Long value) {
            addCriterion("IMG_BANNER >=", value, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerLessThan(Long value) {
            addCriterion("IMG_BANNER <", value, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerLessThanOrEqualTo(Long value) {
            addCriterion("IMG_BANNER <=", value, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerIn(List<Long> values) {
            addCriterion("IMG_BANNER in", values, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerNotIn(List<Long> values) {
            addCriterion("IMG_BANNER not in", values, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerBetween(Long value1, Long value2) {
            addCriterion("IMG_BANNER between", value1, value2, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgBannerNotBetween(Long value1, Long value2) {
            addCriterion("IMG_BANNER not between", value1, value2, "imgBanner");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkIsNull() {
            addCriterion("IMG_WATERMARK is null");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkIsNotNull() {
            addCriterion("IMG_WATERMARK is not null");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkEqualTo(Long value) {
            addCriterion("IMG_WATERMARK =", value, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkNotEqualTo(Long value) {
            addCriterion("IMG_WATERMARK <>", value, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkGreaterThan(Long value) {
            addCriterion("IMG_WATERMARK >", value, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkGreaterThanOrEqualTo(Long value) {
            addCriterion("IMG_WATERMARK >=", value, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkLessThan(Long value) {
            addCriterion("IMG_WATERMARK <", value, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkLessThanOrEqualTo(Long value) {
            addCriterion("IMG_WATERMARK <=", value, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkIn(List<Long> values) {
            addCriterion("IMG_WATERMARK in", values, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkNotIn(List<Long> values) {
            addCriterion("IMG_WATERMARK not in", values, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkBetween(Long value1, Long value2) {
            addCriterion("IMG_WATERMARK between", value1, value2, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImgWatermarkNotBetween(Long value1, Long value2) {
            addCriterion("IMG_WATERMARK not between", value1, value2, "imgWatermark");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeIsNull() {
            addCriterion("IMG_2D_BARCODE is null");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeIsNotNull() {
            addCriterion("IMG_2D_BARCODE is not null");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeEqualTo(Long value) {
            addCriterion("IMG_2D_BARCODE =", value, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeNotEqualTo(Long value) {
            addCriterion("IMG_2D_BARCODE <>", value, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeGreaterThan(Long value) {
            addCriterion("IMG_2D_BARCODE >", value, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeGreaterThanOrEqualTo(Long value) {
            addCriterion("IMG_2D_BARCODE >=", value, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeLessThan(Long value) {
            addCriterion("IMG_2D_BARCODE <", value, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeLessThanOrEqualTo(Long value) {
            addCriterion("IMG_2D_BARCODE <=", value, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeIn(List<Long> values) {
            addCriterion("IMG_2D_BARCODE in", values, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeNotIn(List<Long> values) {
            addCriterion("IMG_2D_BARCODE not in", values, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeBetween(Long value1, Long value2) {
            addCriterion("IMG_2D_BARCODE between", value1, value2, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andImg2dBarcodeNotBetween(Long value1, Long value2) {
            addCriterion("IMG_2D_BARCODE not between", value1, value2, "img2dBarcode");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionIsNull() {
            addCriterion("DESC_PAGE_CAPTION is null");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionIsNotNull() {
            addCriterion("DESC_PAGE_CAPTION is not null");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionEqualTo(String value) {
            addCriterion("DESC_PAGE_CAPTION =", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionNotEqualTo(String value) {
            addCriterion("DESC_PAGE_CAPTION <>", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionGreaterThan(String value) {
            addCriterion("DESC_PAGE_CAPTION >", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionGreaterThanOrEqualTo(String value) {
            addCriterion("DESC_PAGE_CAPTION >=", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionLessThan(String value) {
            addCriterion("DESC_PAGE_CAPTION <", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionLessThanOrEqualTo(String value) {
            addCriterion("DESC_PAGE_CAPTION <=", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionLike(String value) {
            addCriterion("DESC_PAGE_CAPTION like", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionNotLike(String value) {
            addCriterion("DESC_PAGE_CAPTION not like", value, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionIn(List<String> values) {
            addCriterion("DESC_PAGE_CAPTION in", values, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionNotIn(List<String> values) {
            addCriterion("DESC_PAGE_CAPTION not in", values, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionBetween(String value1, String value2) {
            addCriterion("DESC_PAGE_CAPTION between", value1, value2, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andDescPageCaptionNotBetween(String value1, String value2) {
            addCriterion("DESC_PAGE_CAPTION not between", value1, value2, "descPageCaption");
            return (Criteria) this;
        }

        public Criteria andSeoTagIsNull() {
            addCriterion("SEO_TAG is null");
            return (Criteria) this;
        }

        public Criteria andSeoTagIsNotNull() {
            addCriterion("SEO_TAG is not null");
            return (Criteria) this;
        }

        public Criteria andSeoTagEqualTo(String value) {
            addCriterion("SEO_TAG =", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagNotEqualTo(String value) {
            addCriterion("SEO_TAG <>", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagGreaterThan(String value) {
            addCriterion("SEO_TAG >", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagGreaterThanOrEqualTo(String value) {
            addCriterion("SEO_TAG >=", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagLessThan(String value) {
            addCriterion("SEO_TAG <", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagLessThanOrEqualTo(String value) {
            addCriterion("SEO_TAG <=", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagLike(String value) {
            addCriterion("SEO_TAG like", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagNotLike(String value) {
            addCriterion("SEO_TAG not like", value, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagIn(List<String> values) {
            addCriterion("SEO_TAG in", values, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagNotIn(List<String> values) {
            addCriterion("SEO_TAG not in", values, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagBetween(String value1, String value2) {
            addCriterion("SEO_TAG between", value1, value2, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSeoTagNotBetween(String value1, String value2) {
            addCriterion("SEO_TAG not between", value1, value2, "seoTag");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntIsNull() {
            addCriterion("SUM_SALES_CNT is null");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntIsNotNull() {
            addCriterion("SUM_SALES_CNT is not null");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntEqualTo(Short value) {
            addCriterion("SUM_SALES_CNT =", value, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntNotEqualTo(Short value) {
            addCriterion("SUM_SALES_CNT <>", value, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntGreaterThan(Short value) {
            addCriterion("SUM_SALES_CNT >", value, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntGreaterThanOrEqualTo(Short value) {
            addCriterion("SUM_SALES_CNT >=", value, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntLessThan(Short value) {
            addCriterion("SUM_SALES_CNT <", value, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntLessThanOrEqualTo(Short value) {
            addCriterion("SUM_SALES_CNT <=", value, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntIn(List<Short> values) {
            addCriterion("SUM_SALES_CNT in", values, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntNotIn(List<Short> values) {
            addCriterion("SUM_SALES_CNT not in", values, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntBetween(Short value1, Short value2) {
            addCriterion("SUM_SALES_CNT between", value1, value2, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesCntNotBetween(Short value1, Short value2) {
            addCriterion("SUM_SALES_CNT not between", value1, value2, "sumSalesCnt");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyIsNull() {
            addCriterion("SUM_SALES_QTY is null");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyIsNotNull() {
            addCriterion("SUM_SALES_QTY is not null");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyEqualTo(Short value) {
            addCriterion("SUM_SALES_QTY =", value, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyNotEqualTo(Short value) {
            addCriterion("SUM_SALES_QTY <>", value, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyGreaterThan(Short value) {
            addCriterion("SUM_SALES_QTY >", value, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyGreaterThanOrEqualTo(Short value) {
            addCriterion("SUM_SALES_QTY >=", value, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyLessThan(Short value) {
            addCriterion("SUM_SALES_QTY <", value, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyLessThanOrEqualTo(Short value) {
            addCriterion("SUM_SALES_QTY <=", value, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyIn(List<Short> values) {
            addCriterion("SUM_SALES_QTY in", values, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyNotIn(List<Short> values) {
            addCriterion("SUM_SALES_QTY not in", values, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyBetween(Short value1, Short value2) {
            addCriterion("SUM_SALES_QTY between", value1, value2, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesQtyNotBetween(Short value1, Short value2) {
            addCriterion("SUM_SALES_QTY not between", value1, value2, "sumSalesQty");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtIsNull() {
            addCriterion("SUM_SALES_AMT is null");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtIsNotNull() {
            addCriterion("SUM_SALES_AMT is not null");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtEqualTo(BigDecimal value) {
            addCriterion("SUM_SALES_AMT =", value, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtNotEqualTo(BigDecimal value) {
            addCriterion("SUM_SALES_AMT <>", value, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtGreaterThan(BigDecimal value) {
            addCriterion("SUM_SALES_AMT >", value, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("SUM_SALES_AMT >=", value, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtLessThan(BigDecimal value) {
            addCriterion("SUM_SALES_AMT <", value, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtLessThanOrEqualTo(BigDecimal value) {
            addCriterion("SUM_SALES_AMT <=", value, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtIn(List<BigDecimal> values) {
            addCriterion("SUM_SALES_AMT in", values, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtNotIn(List<BigDecimal> values) {
            addCriterion("SUM_SALES_AMT not in", values, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SUM_SALES_AMT between", value1, value2, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumSalesAmtNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SUM_SALES_AMT not between", value1, value2, "sumSalesAmt");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyIsNull() {
            addCriterion("SUM_COLLECTED_QTY is null");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyIsNotNull() {
            addCriterion("SUM_COLLECTED_QTY is not null");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyEqualTo(Short value) {
            addCriterion("SUM_COLLECTED_QTY =", value, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyNotEqualTo(Short value) {
            addCriterion("SUM_COLLECTED_QTY <>", value, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyGreaterThan(Short value) {
            addCriterion("SUM_COLLECTED_QTY >", value, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyGreaterThanOrEqualTo(Short value) {
            addCriterion("SUM_COLLECTED_QTY >=", value, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyLessThan(Short value) {
            addCriterion("SUM_COLLECTED_QTY <", value, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyLessThanOrEqualTo(Short value) {
            addCriterion("SUM_COLLECTED_QTY <=", value, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyIn(List<Short> values) {
            addCriterion("SUM_COLLECTED_QTY in", values, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyNotIn(List<Short> values) {
            addCriterion("SUM_COLLECTED_QTY not in", values, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyBetween(Short value1, Short value2) {
            addCriterion("SUM_COLLECTED_QTY between", value1, value2, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andSumCollectedQtyNotBetween(Short value1, Short value2) {
            addCriterion("SUM_COLLECTED_QTY not between", value1, value2, "sumCollectedQty");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateIsNull() {
            addCriterion("AVG_PRAISE_RATE is null");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateIsNotNull() {
            addCriterion("AVG_PRAISE_RATE is not null");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateEqualTo(Byte value) {
            addCriterion("AVG_PRAISE_RATE =", value, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateNotEqualTo(Byte value) {
            addCriterion("AVG_PRAISE_RATE <>", value, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateGreaterThan(Byte value) {
            addCriterion("AVG_PRAISE_RATE >", value, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateGreaterThanOrEqualTo(Byte value) {
            addCriterion("AVG_PRAISE_RATE >=", value, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateLessThan(Byte value) {
            addCriterion("AVG_PRAISE_RATE <", value, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateLessThanOrEqualTo(Byte value) {
            addCriterion("AVG_PRAISE_RATE <=", value, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateIn(List<Byte> values) {
            addCriterion("AVG_PRAISE_RATE in", values, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateNotIn(List<Byte> values) {
            addCriterion("AVG_PRAISE_RATE not in", values, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateBetween(Byte value1, Byte value2) {
            addCriterion("AVG_PRAISE_RATE between", value1, value2, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgPraiseRateNotBetween(Byte value1, Byte value2) {
            addCriterion("AVG_PRAISE_RATE not between", value1, value2, "avgPraiseRate");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreIsNull() {
            addCriterion("AVG_GOODS_DESC_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreIsNotNull() {
            addCriterion("AVG_GOODS_DESC_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreEqualTo(Byte value) {
            addCriterion("AVG_GOODS_DESC_SCORE =", value, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreNotEqualTo(Byte value) {
            addCriterion("AVG_GOODS_DESC_SCORE <>", value, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreGreaterThan(Byte value) {
            addCriterion("AVG_GOODS_DESC_SCORE >", value, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreGreaterThanOrEqualTo(Byte value) {
            addCriterion("AVG_GOODS_DESC_SCORE >=", value, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreLessThan(Byte value) {
            addCriterion("AVG_GOODS_DESC_SCORE <", value, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreLessThanOrEqualTo(Byte value) {
            addCriterion("AVG_GOODS_DESC_SCORE <=", value, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreIn(List<Byte> values) {
            addCriterion("AVG_GOODS_DESC_SCORE in", values, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreNotIn(List<Byte> values) {
            addCriterion("AVG_GOODS_DESC_SCORE not in", values, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreBetween(Byte value1, Byte value2) {
            addCriterion("AVG_GOODS_DESC_SCORE between", value1, value2, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgGoodsDescScoreNotBetween(Byte value1, Byte value2) {
            addCriterion("AVG_GOODS_DESC_SCORE not between", value1, value2, "avgGoodsDescScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreIsNull() {
            addCriterion("AVG_SERVICE_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreIsNotNull() {
            addCriterion("AVG_SERVICE_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreEqualTo(Byte value) {
            addCriterion("AVG_SERVICE_SCORE =", value, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreNotEqualTo(Byte value) {
            addCriterion("AVG_SERVICE_SCORE <>", value, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreGreaterThan(Byte value) {
            addCriterion("AVG_SERVICE_SCORE >", value, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreGreaterThanOrEqualTo(Byte value) {
            addCriterion("AVG_SERVICE_SCORE >=", value, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreLessThan(Byte value) {
            addCriterion("AVG_SERVICE_SCORE <", value, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreLessThanOrEqualTo(Byte value) {
            addCriterion("AVG_SERVICE_SCORE <=", value, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreIn(List<Byte> values) {
            addCriterion("AVG_SERVICE_SCORE in", values, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreNotIn(List<Byte> values) {
            addCriterion("AVG_SERVICE_SCORE not in", values, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreBetween(Byte value1, Byte value2) {
            addCriterion("AVG_SERVICE_SCORE between", value1, value2, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgServiceScoreNotBetween(Byte value1, Byte value2) {
            addCriterion("AVG_SERVICE_SCORE not between", value1, value2, "avgServiceScore");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateIsNull() {
            addCriterion("AVG_DELIVERY_RATE is null");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateIsNotNull() {
            addCriterion("AVG_DELIVERY_RATE is not null");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateEqualTo(Byte value) {
            addCriterion("AVG_DELIVERY_RATE =", value, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateNotEqualTo(Byte value) {
            addCriterion("AVG_DELIVERY_RATE <>", value, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateGreaterThan(Byte value) {
            addCriterion("AVG_DELIVERY_RATE >", value, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateGreaterThanOrEqualTo(Byte value) {
            addCriterion("AVG_DELIVERY_RATE >=", value, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateLessThan(Byte value) {
            addCriterion("AVG_DELIVERY_RATE <", value, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateLessThanOrEqualTo(Byte value) {
            addCriterion("AVG_DELIVERY_RATE <=", value, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateIn(List<Byte> values) {
            addCriterion("AVG_DELIVERY_RATE in", values, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateNotIn(List<Byte> values) {
            addCriterion("AVG_DELIVERY_RATE not in", values, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateBetween(Byte value1, Byte value2) {
            addCriterion("AVG_DELIVERY_RATE between", value1, value2, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andAvgDeliveryRateNotBetween(Byte value1, Byte value2) {
            addCriterion("AVG_DELIVERY_RATE not between", value1, value2, "avgDeliveryRate");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdIsNull() {
            addCriterion("CREATED_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdIsNotNull() {
            addCriterion("CREATED_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdEqualTo(String value) {
            addCriterion("CREATED_USER_ID =", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotEqualTo(String value) {
            addCriterion("CREATED_USER_ID <>", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdGreaterThan(String value) {
            addCriterion("CREATED_USER_ID >", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("CREATED_USER_ID >=", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdLessThan(String value) {
            addCriterion("CREATED_USER_ID <", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdLessThanOrEqualTo(String value) {
            addCriterion("CREATED_USER_ID <=", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdLike(String value) {
            addCriterion("CREATED_USER_ID like", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotLike(String value) {
            addCriterion("CREATED_USER_ID not like", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdIn(List<String> values) {
            addCriterion("CREATED_USER_ID in", values, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotIn(List<String> values) {
            addCriterion("CREATED_USER_ID not in", values, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdBetween(String value1, String value2) {
            addCriterion("CREATED_USER_ID between", value1, value2, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotBetween(String value1, String value2) {
            addCriterion("CREATED_USER_ID not between", value1, value2, "createdUserId");
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