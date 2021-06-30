package com.basoft.service.entity.wechat.appinfo;

import java.util.ArrayList;
import java.util.List;

public class AppInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AppInfoExample() {
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

        public Criteria andOriginalAppIdIsNull() {
            addCriterion("ORIGINAL_APP_ID is null");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdIsNotNull() {
            addCriterion("ORIGINAL_APP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdEqualTo(String value) {
            addCriterion("ORIGINAL_APP_ID =", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdNotEqualTo(String value) {
            addCriterion("ORIGINAL_APP_ID <>", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdGreaterThan(String value) {
            addCriterion("ORIGINAL_APP_ID >", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("ORIGINAL_APP_ID >=", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdLessThan(String value) {
            addCriterion("ORIGINAL_APP_ID <", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdLessThanOrEqualTo(String value) {
            addCriterion("ORIGINAL_APP_ID <=", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdLike(String value) {
            addCriterion("ORIGINAL_APP_ID like", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdNotLike(String value) {
            addCriterion("ORIGINAL_APP_ID not like", value, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdIn(List<String> values) {
            addCriterion("ORIGINAL_APP_ID in", values, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdNotIn(List<String> values) {
            addCriterion("ORIGINAL_APP_ID not in", values, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdBetween(String value1, String value2) {
            addCriterion("ORIGINAL_APP_ID between", value1, value2, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andOriginalAppIdNotBetween(String value1, String value2) {
            addCriterion("ORIGINAL_APP_ID not between", value1, value2, "originalAppId");
            return (Criteria) this;
        }

        public Criteria andCompNmIsNull() {
            addCriterion("COMP_NM is null");
            return (Criteria) this;
        }

        public Criteria andCompNmIsNotNull() {
            addCriterion("COMP_NM is not null");
            return (Criteria) this;
        }

        public Criteria andCompNmEqualTo(String value) {
            addCriterion("COMP_NM =", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmNotEqualTo(String value) {
            addCriterion("COMP_NM <>", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmGreaterThan(String value) {
            addCriterion("COMP_NM >", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmGreaterThanOrEqualTo(String value) {
            addCriterion("COMP_NM >=", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmLessThan(String value) {
            addCriterion("COMP_NM <", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmLessThanOrEqualTo(String value) {
            addCriterion("COMP_NM <=", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmLike(String value) {
            addCriterion("COMP_NM like", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmNotLike(String value) {
            addCriterion("COMP_NM not like", value, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmIn(List<String> values) {
            addCriterion("COMP_NM in", values, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmNotIn(List<String> values) {
            addCriterion("COMP_NM not in", values, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmBetween(String value1, String value2) {
            addCriterion("COMP_NM between", value1, value2, "compNm");
            return (Criteria) this;
        }

        public Criteria andCompNmNotBetween(String value1, String value2) {
            addCriterion("COMP_NM not between", value1, value2, "compNm");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("APP_ID is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("APP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("APP_ID =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("APP_ID <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("APP_ID >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("APP_ID >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("APP_ID <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("APP_ID <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("APP_ID like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("APP_ID not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("APP_ID in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("APP_ID not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("APP_ID between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("APP_ID not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppSecretIsNull() {
            addCriterion("APP_SECRET is null");
            return (Criteria) this;
        }

        public Criteria andAppSecretIsNotNull() {
            addCriterion("APP_SECRET is not null");
            return (Criteria) this;
        }

        public Criteria andAppSecretEqualTo(String value) {
            addCriterion("APP_SECRET =", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotEqualTo(String value) {
            addCriterion("APP_SECRET <>", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretGreaterThan(String value) {
            addCriterion("APP_SECRET >", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretGreaterThanOrEqualTo(String value) {
            addCriterion("APP_SECRET >=", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretLessThan(String value) {
            addCriterion("APP_SECRET <", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretLessThanOrEqualTo(String value) {
            addCriterion("APP_SECRET <=", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretLike(String value) {
            addCriterion("APP_SECRET like", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotLike(String value) {
            addCriterion("APP_SECRET not like", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretIn(List<String> values) {
            addCriterion("APP_SECRET in", values, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotIn(List<String> values) {
            addCriterion("APP_SECRET not in", values, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretBetween(String value1, String value2) {
            addCriterion("APP_SECRET between", value1, value2, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotBetween(String value1, String value2) {
            addCriterion("APP_SECRET not between", value1, value2, "appSecret");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("URL is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("URL is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("URL =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("URL <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("URL >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("URL >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("URL <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("URL <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("URL like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("URL not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("URL in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("URL not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("URL between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("URL not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andTokenIsNull() {
            addCriterion("TOKEN is null");
            return (Criteria) this;
        }

        public Criteria andTokenIsNotNull() {
            addCriterion("TOKEN is not null");
            return (Criteria) this;
        }

        public Criteria andTokenEqualTo(String value) {
            addCriterion("TOKEN =", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotEqualTo(String value) {
            addCriterion("TOKEN <>", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThan(String value) {
            addCriterion("TOKEN >", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThanOrEqualTo(String value) {
            addCriterion("TOKEN >=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThan(String value) {
            addCriterion("TOKEN <", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThanOrEqualTo(String value) {
            addCriterion("TOKEN <=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLike(String value) {
            addCriterion("TOKEN like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotLike(String value) {
            addCriterion("TOKEN not like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenIn(List<String> values) {
            addCriterion("TOKEN in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotIn(List<String> values) {
            addCriterion("TOKEN not in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenBetween(String value1, String value2) {
            addCriterion("TOKEN between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotBetween(String value1, String value2) {
            addCriterion("TOKEN not between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyIsNull() {
            addCriterion("ENCORDING_AES_KEY is null");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyIsNotNull() {
            addCriterion("ENCORDING_AES_KEY is not null");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyEqualTo(String value) {
            addCriterion("ENCORDING_AES_KEY =", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyNotEqualTo(String value) {
            addCriterion("ENCORDING_AES_KEY <>", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyGreaterThan(String value) {
            addCriterion("ENCORDING_AES_KEY >", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyGreaterThanOrEqualTo(String value) {
            addCriterion("ENCORDING_AES_KEY >=", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyLessThan(String value) {
            addCriterion("ENCORDING_AES_KEY <", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyLessThanOrEqualTo(String value) {
            addCriterion("ENCORDING_AES_KEY <=", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyLike(String value) {
            addCriterion("ENCORDING_AES_KEY like", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyNotLike(String value) {
            addCriterion("ENCORDING_AES_KEY not like", value, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyIn(List<String> values) {
            addCriterion("ENCORDING_AES_KEY in", values, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyNotIn(List<String> values) {
            addCriterion("ENCORDING_AES_KEY not in", values, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyBetween(String value1, String value2) {
            addCriterion("ENCORDING_AES_KEY between", value1, value2, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andEncordingAesKeyNotBetween(String value1, String value2) {
            addCriterion("ENCORDING_AES_KEY not between", value1, value2, "encordingAesKey");
            return (Criteria) this;
        }

        public Criteria andWechatNoIsNull() {
            addCriterion("WECHAT_NO is null");
            return (Criteria) this;
        }

        public Criteria andWechatNoIsNotNull() {
            addCriterion("WECHAT_NO is not null");
            return (Criteria) this;
        }

        public Criteria andWechatNoEqualTo(String value) {
            addCriterion("WECHAT_NO =", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoNotEqualTo(String value) {
            addCriterion("WECHAT_NO <>", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoGreaterThan(String value) {
            addCriterion("WECHAT_NO >", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoGreaterThanOrEqualTo(String value) {
            addCriterion("WECHAT_NO >=", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoLessThan(String value) {
            addCriterion("WECHAT_NO <", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoLessThanOrEqualTo(String value) {
            addCriterion("WECHAT_NO <=", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoLike(String value) {
            addCriterion("WECHAT_NO like", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoNotLike(String value) {
            addCriterion("WECHAT_NO not like", value, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoIn(List<String> values) {
            addCriterion("WECHAT_NO in", values, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoNotIn(List<String> values) {
            addCriterion("WECHAT_NO not in", values, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoBetween(String value1, String value2) {
            addCriterion("WECHAT_NO between", value1, value2, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andWechatNoNotBetween(String value1, String value2) {
            addCriterion("WECHAT_NO not between", value1, value2, "wechatNo");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNull() {
            addCriterion("ACCOUNT_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNotNull() {
            addCriterion("ACCOUNT_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeEqualTo(Byte value) {
            addCriterion("ACCOUNT_TYPE =", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotEqualTo(Byte value) {
            addCriterion("ACCOUNT_TYPE <>", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThan(Byte value) {
            addCriterion("ACCOUNT_TYPE >", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("ACCOUNT_TYPE >=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThan(Byte value) {
            addCriterion("ACCOUNT_TYPE <", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThanOrEqualTo(Byte value) {
            addCriterion("ACCOUNT_TYPE <=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIn(List<Byte> values) {
            addCriterion("ACCOUNT_TYPE in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotIn(List<Byte> values) {
            addCriterion("ACCOUNT_TYPE not in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeBetween(Byte value1, Byte value2) {
            addCriterion("ACCOUNT_TYPE between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("ACCOUNT_TYPE not between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceIsNull() {
            addCriterion("TRANSFER_CUSTOMER_SERVICE is null");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceIsNotNull() {
            addCriterion("TRANSFER_CUSTOMER_SERVICE is not null");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceEqualTo(Byte value) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE =", value, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceNotEqualTo(Byte value) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE <>", value, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceGreaterThan(Byte value) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE >", value, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceGreaterThanOrEqualTo(Byte value) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE >=", value, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceLessThan(Byte value) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE <", value, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceLessThanOrEqualTo(Byte value) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE <=", value, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceIn(List<Byte> values) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE in", values, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceNotIn(List<Byte> values) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE not in", values, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceBetween(Byte value1, Byte value2) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE between", value1, value2, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andTransferCustomerServiceNotBetween(Byte value1, Byte value2) {
            addCriterion("TRANSFER_CUSTOMER_SERVICE not between", value1, value2, "transferCustomerService");
            return (Criteria) this;
        }

        public Criteria andAccountStatusIsNull() {
            addCriterion("ACCOUNT_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andAccountStatusIsNotNull() {
            addCriterion("ACCOUNT_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andAccountStatusEqualTo(Byte value) {
            addCriterion("ACCOUNT_STATUS =", value, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusNotEqualTo(Byte value) {
            addCriterion("ACCOUNT_STATUS <>", value, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusGreaterThan(Byte value) {
            addCriterion("ACCOUNT_STATUS >", value, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("ACCOUNT_STATUS >=", value, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusLessThan(Byte value) {
            addCriterion("ACCOUNT_STATUS <", value, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusLessThanOrEqualTo(Byte value) {
            addCriterion("ACCOUNT_STATUS <=", value, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusIn(List<Byte> values) {
            addCriterion("ACCOUNT_STATUS in", values, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusNotIn(List<Byte> values) {
            addCriterion("ACCOUNT_STATUS not in", values, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusBetween(Byte value1, Byte value2) {
            addCriterion("ACCOUNT_STATUS between", value1, value2, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andAccountStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("ACCOUNT_STATUS not between", value1, value2, "accountStatus");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobIsNull() {
            addCriterion("OPEN_BATCH_JOB is null");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobIsNotNull() {
            addCriterion("OPEN_BATCH_JOB is not null");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobEqualTo(Byte value) {
            addCriterion("OPEN_BATCH_JOB =", value, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobNotEqualTo(Byte value) {
            addCriterion("OPEN_BATCH_JOB <>", value, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobGreaterThan(Byte value) {
            addCriterion("OPEN_BATCH_JOB >", value, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobGreaterThanOrEqualTo(Byte value) {
            addCriterion("OPEN_BATCH_JOB >=", value, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobLessThan(Byte value) {
            addCriterion("OPEN_BATCH_JOB <", value, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobLessThanOrEqualTo(Byte value) {
            addCriterion("OPEN_BATCH_JOB <=", value, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobIn(List<Byte> values) {
            addCriterion("OPEN_BATCH_JOB in", values, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobNotIn(List<Byte> values) {
            addCriterion("OPEN_BATCH_JOB not in", values, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobBetween(Byte value1, Byte value2) {
            addCriterion("OPEN_BATCH_JOB between", value1, value2, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andOpenBatchJobNotBetween(Byte value1, Byte value2) {
            addCriterion("OPEN_BATCH_JOB not between", value1, value2, "openBatchJob");
            return (Criteria) this;
        }

        public Criteria andInterfacedIsNull() {
            addCriterion("INTERFACED is null");
            return (Criteria) this;
        }

        public Criteria andInterfacedIsNotNull() {
            addCriterion("INTERFACED is not null");
            return (Criteria) this;
        }

        public Criteria andInterfacedEqualTo(Byte value) {
            addCriterion("INTERFACED =", value, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedNotEqualTo(Byte value) {
            addCriterion("INTERFACED <>", value, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedGreaterThan(Byte value) {
            addCriterion("INTERFACED >", value, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedGreaterThanOrEqualTo(Byte value) {
            addCriterion("INTERFACED >=", value, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedLessThan(Byte value) {
            addCriterion("INTERFACED <", value, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedLessThanOrEqualTo(Byte value) {
            addCriterion("INTERFACED <=", value, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedIn(List<Byte> values) {
            addCriterion("INTERFACED in", values, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedNotIn(List<Byte> values) {
            addCriterion("INTERFACED not in", values, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedBetween(Byte value1, Byte value2) {
            addCriterion("INTERFACED between", value1, value2, "interfaced");
            return (Criteria) this;
        }

        public Criteria andInterfacedNotBetween(Byte value1, Byte value2) {
            addCriterion("INTERFACED not between", value1, value2, "interfaced");
            return (Criteria) this;
        }

        public Criteria andIfUseridIsNull() {
            addCriterion("IF_USERID is null");
            return (Criteria) this;
        }

        public Criteria andIfUseridIsNotNull() {
            addCriterion("IF_USERID is not null");
            return (Criteria) this;
        }

        public Criteria andIfUseridEqualTo(String value) {
            addCriterion("IF_USERID =", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridNotEqualTo(String value) {
            addCriterion("IF_USERID <>", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridGreaterThan(String value) {
            addCriterion("IF_USERID >", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridGreaterThanOrEqualTo(String value) {
            addCriterion("IF_USERID >=", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridLessThan(String value) {
            addCriterion("IF_USERID <", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridLessThanOrEqualTo(String value) {
            addCriterion("IF_USERID <=", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridLike(String value) {
            addCriterion("IF_USERID like", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridNotLike(String value) {
            addCriterion("IF_USERID not like", value, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridIn(List<String> values) {
            addCriterion("IF_USERID in", values, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridNotIn(List<String> values) {
            addCriterion("IF_USERID not in", values, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridBetween(String value1, String value2) {
            addCriterion("IF_USERID between", value1, value2, "ifUserid");
            return (Criteria) this;
        }

        public Criteria andIfUseridNotBetween(String value1, String value2) {
            addCriterion("IF_USERID not between", value1, value2, "ifUserid");
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