package com.basoft.service.entity.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("USER_ID =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("USER_ID <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("USER_ID >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("USER_ID >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("USER_ID <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("USER_ID <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("USER_ID like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("USER_ID not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("USER_ID in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("USER_ID not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("USER_ID between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("USER_ID not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNickNmIsNull() {
            addCriterion("USER_NICK_NM is null");
            return (Criteria) this;
        }

        public Criteria andUserNickNmIsNotNull() {
            addCriterion("USER_NICK_NM is not null");
            return (Criteria) this;
        }

        public Criteria andUserNickNmEqualTo(String value) {
            addCriterion("USER_NICK_NM =", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmNotEqualTo(String value) {
            addCriterion("USER_NICK_NM <>", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmGreaterThan(String value) {
            addCriterion("USER_NICK_NM >", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmGreaterThanOrEqualTo(String value) {
            addCriterion("USER_NICK_NM >=", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmLessThan(String value) {
            addCriterion("USER_NICK_NM <", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmLessThanOrEqualTo(String value) {
            addCriterion("USER_NICK_NM <=", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmLike(String value) {
            addCriterion("USER_NICK_NM like", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmNotLike(String value) {
            addCriterion("USER_NICK_NM not like", value, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmIn(List<String> values) {
            addCriterion("USER_NICK_NM in", values, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmNotIn(List<String> values) {
            addCriterion("USER_NICK_NM not in", values, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmBetween(String value1, String value2) {
            addCriterion("USER_NICK_NM between", value1, value2, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserNickNmNotBetween(String value1, String value2) {
            addCriterion("USER_NICK_NM not between", value1, value2, "userNickNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmIsNull() {
            addCriterion("USER_REAL_NM is null");
            return (Criteria) this;
        }

        public Criteria andUserRealNmIsNotNull() {
            addCriterion("USER_REAL_NM is not null");
            return (Criteria) this;
        }

        public Criteria andUserRealNmEqualTo(String value) {
            addCriterion("USER_REAL_NM =", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmNotEqualTo(String value) {
            addCriterion("USER_REAL_NM <>", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmGreaterThan(String value) {
            addCriterion("USER_REAL_NM >", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmGreaterThanOrEqualTo(String value) {
            addCriterion("USER_REAL_NM >=", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmLessThan(String value) {
            addCriterion("USER_REAL_NM <", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmLessThanOrEqualTo(String value) {
            addCriterion("USER_REAL_NM <=", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmLike(String value) {
            addCriterion("USER_REAL_NM like", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmNotLike(String value) {
            addCriterion("USER_REAL_NM not like", value, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmIn(List<String> values) {
            addCriterion("USER_REAL_NM in", values, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmNotIn(List<String> values) {
            addCriterion("USER_REAL_NM not in", values, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmBetween(String value1, String value2) {
            addCriterion("USER_REAL_NM between", value1, value2, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andUserRealNmNotBetween(String value1, String value2) {
            addCriterion("USER_REAL_NM not between", value1, value2, "userRealNm");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUIsNull() {
            addCriterion("WX_OPENID_U is null");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUIsNotNull() {
            addCriterion("WX_OPENID_U is not null");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUEqualTo(String value) {
            addCriterion("WX_OPENID_U =", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUNotEqualTo(String value) {
            addCriterion("WX_OPENID_U <>", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUGreaterThan(String value) {
            addCriterion("WX_OPENID_U >", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUGreaterThanOrEqualTo(String value) {
            addCriterion("WX_OPENID_U >=", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidULessThan(String value) {
            addCriterion("WX_OPENID_U <", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidULessThanOrEqualTo(String value) {
            addCriterion("WX_OPENID_U <=", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidULike(String value) {
            addCriterion("WX_OPENID_U like", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUNotLike(String value) {
            addCriterion("WX_OPENID_U not like", value, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUIn(List<String> values) {
            addCriterion("WX_OPENID_U in", values, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUNotIn(List<String> values) {
            addCriterion("WX_OPENID_U not in", values, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUBetween(String value1, String value2) {
            addCriterion("WX_OPENID_U between", value1, value2, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxOpenidUNotBetween(String value1, String value2) {
            addCriterion("WX_OPENID_U not between", value1, value2, "wxOpenidU");
            return (Criteria) this;
        }

        public Criteria andWxIdUIsNull() {
            addCriterion("WX_ID_U is null");
            return (Criteria) this;
        }

        public Criteria andWxIdUIsNotNull() {
            addCriterion("WX_ID_U is not null");
            return (Criteria) this;
        }

        public Criteria andWxIdUEqualTo(String value) {
            addCriterion("WX_ID_U =", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUNotEqualTo(String value) {
            addCriterion("WX_ID_U <>", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUGreaterThan(String value) {
            addCriterion("WX_ID_U >", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUGreaterThanOrEqualTo(String value) {
            addCriterion("WX_ID_U >=", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdULessThan(String value) {
            addCriterion("WX_ID_U <", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdULessThanOrEqualTo(String value) {
            addCriterion("WX_ID_U <=", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdULike(String value) {
            addCriterion("WX_ID_U like", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUNotLike(String value) {
            addCriterion("WX_ID_U not like", value, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUIn(List<String> values) {
            addCriterion("WX_ID_U in", values, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUNotIn(List<String> values) {
            addCriterion("WX_ID_U not in", values, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUBetween(String value1, String value2) {
            addCriterion("WX_ID_U between", value1, value2, "wxIdU");
            return (Criteria) this;
        }

        public Criteria andWxIdUNotBetween(String value1, String value2) {
            addCriterion("WX_ID_U not between", value1, value2, "wxIdU");
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

        public Criteria andQqIdIsNull() {
            addCriterion("QQ_ID is null");
            return (Criteria) this;
        }

        public Criteria andQqIdIsNotNull() {
            addCriterion("QQ_ID is not null");
            return (Criteria) this;
        }

        public Criteria andQqIdEqualTo(String value) {
            addCriterion("QQ_ID =", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotEqualTo(String value) {
            addCriterion("QQ_ID <>", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdGreaterThan(String value) {
            addCriterion("QQ_ID >", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdGreaterThanOrEqualTo(String value) {
            addCriterion("QQ_ID >=", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLessThan(String value) {
            addCriterion("QQ_ID <", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLessThanOrEqualTo(String value) {
            addCriterion("QQ_ID <=", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLike(String value) {
            addCriterion("QQ_ID like", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotLike(String value) {
            addCriterion("QQ_ID not like", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdIn(List<String> values) {
            addCriterion("QQ_ID in", values, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotIn(List<String> values) {
            addCriterion("QQ_ID not in", values, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdBetween(String value1, String value2) {
            addCriterion("QQ_ID between", value1, value2, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotBetween(String value1, String value2) {
            addCriterion("QQ_ID not between", value1, value2, "qqId");
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

        public Criteria andDeptIsNull() {
            addCriterion("DEPT is null");
            return (Criteria) this;
        }

        public Criteria andDeptIsNotNull() {
            addCriterion("DEPT is not null");
            return (Criteria) this;
        }

        public Criteria andDeptEqualTo(String value) {
            addCriterion("DEPT =", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptNotEqualTo(String value) {
            addCriterion("DEPT <>", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptGreaterThan(String value) {
            addCriterion("DEPT >", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptGreaterThanOrEqualTo(String value) {
            addCriterion("DEPT >=", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptLessThan(String value) {
            addCriterion("DEPT <", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptLessThanOrEqualTo(String value) {
            addCriterion("DEPT <=", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptLike(String value) {
            addCriterion("DEPT like", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptNotLike(String value) {
            addCriterion("DEPT not like", value, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptIn(List<String> values) {
            addCriterion("DEPT in", values, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptNotIn(List<String> values) {
            addCriterion("DEPT not in", values, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptBetween(String value1, String value2) {
            addCriterion("DEPT between", value1, value2, "dept");
            return (Criteria) this;
        }

        public Criteria andDeptNotBetween(String value1, String value2) {
            addCriterion("DEPT not between", value1, value2, "dept");
            return (Criteria) this;
        }

        public Criteria andCompIdIsNull() {
            addCriterion("COMP_ID is null");
            return (Criteria) this;
        }

        public Criteria andCompIdIsNotNull() {
            addCriterion("COMP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCompIdEqualTo(Integer value) {
            addCriterion("COMP_ID =", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdNotEqualTo(Integer value) {
            addCriterion("COMP_ID <>", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdGreaterThan(Integer value) {
            addCriterion("COMP_ID >", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("COMP_ID >=", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdLessThan(Integer value) {
            addCriterion("COMP_ID <", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdLessThanOrEqualTo(Integer value) {
            addCriterion("COMP_ID <=", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdIn(List<Integer> values) {
            addCriterion("COMP_ID in", values, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdNotIn(List<Integer> values) {
            addCriterion("COMP_ID not in", values, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdBetween(Integer value1, Integer value2) {
            addCriterion("COMP_ID between", value1, value2, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdNotBetween(Integer value1, Integer value2) {
            addCriterion("COMP_ID not between", value1, value2, "compId");
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

        public Criteria andShopIdEqualTo(Integer value) {
            addCriterion("SHOP_ID =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Integer value) {
            addCriterion("SHOP_ID <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Integer value) {
            addCriterion("SHOP_ID >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("SHOP_ID >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Integer value) {
            addCriterion("SHOP_ID <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Integer value) {
            addCriterion("SHOP_ID <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Integer> values) {
            addCriterion("SHOP_ID in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Integer> values) {
            addCriterion("SHOP_ID not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Integer value1, Integer value2) {
            addCriterion("SHOP_ID between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Integer value1, Integer value2) {
            addCriterion("SHOP_ID not between", value1, value2, "shopId");
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

        public Criteria andSexIsNull() {
            addCriterion("SEX is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("SEX is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(Integer value) {
            addCriterion("SEX =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(Integer value) {
            addCriterion("SEX <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(Integer value) {
            addCriterion("SEX >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(Integer value) {
            addCriterion("SEX >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(Integer value) {
            addCriterion("SEX <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(Integer value) {
            addCriterion("SEX <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<Integer> values) {
            addCriterion("SEX in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<Integer> values) {
            addCriterion("SEX not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(Integer value1, Integer value2) {
            addCriterion("SEX between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(Integer value1, Integer value2) {
            addCriterion("SEX not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andImgIdIsNull() {
            addCriterion("IMG_ID is null");
            return (Criteria) this;
        }

        public Criteria andImgIdIsNotNull() {
            addCriterion("IMG_ID is not null");
            return (Criteria) this;
        }

        public Criteria andImgIdEqualTo(Integer value) {
            addCriterion("IMG_ID =", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdNotEqualTo(Integer value) {
            addCriterion("IMG_ID <>", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdGreaterThan(Integer value) {
            addCriterion("IMG_ID >", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("IMG_ID >=", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdLessThan(Integer value) {
            addCriterion("IMG_ID <", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdLessThanOrEqualTo(Integer value) {
            addCriterion("IMG_ID <=", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdIn(List<Integer> values) {
            addCriterion("IMG_ID in", values, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdNotIn(List<Integer> values) {
            addCriterion("IMG_ID not in", values, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdBetween(Integer value1, Integer value2) {
            addCriterion("IMG_ID between", value1, value2, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("IMG_ID not between", value1, value2, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgNmIsNull() {
            addCriterion("IMG_NM is null");
            return (Criteria) this;
        }

        public Criteria andImgNmIsNotNull() {
            addCriterion("IMG_NM is not null");
            return (Criteria) this;
        }

        public Criteria andImgNmEqualTo(String value) {
            addCriterion("IMG_NM =", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmNotEqualTo(String value) {
            addCriterion("IMG_NM <>", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmGreaterThan(String value) {
            addCriterion("IMG_NM >", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmGreaterThanOrEqualTo(String value) {
            addCriterion("IMG_NM >=", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmLessThan(String value) {
            addCriterion("IMG_NM <", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmLessThanOrEqualTo(String value) {
            addCriterion("IMG_NM <=", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmLike(String value) {
            addCriterion("IMG_NM like", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmNotLike(String value) {
            addCriterion("IMG_NM not like", value, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmIn(List<String> values) {
            addCriterion("IMG_NM in", values, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmNotIn(List<String> values) {
            addCriterion("IMG_NM not in", values, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmBetween(String value1, String value2) {
            addCriterion("IMG_NM between", value1, value2, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgNmNotBetween(String value1, String value2) {
            addCriterion("IMG_NM not between", value1, value2, "imgNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmIsNull() {
            addCriterion("IMG_SYS_NM is null");
            return (Criteria) this;
        }

        public Criteria andImgSysNmIsNotNull() {
            addCriterion("IMG_SYS_NM is not null");
            return (Criteria) this;
        }

        public Criteria andImgSysNmEqualTo(String value) {
            addCriterion("IMG_SYS_NM =", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmNotEqualTo(String value) {
            addCriterion("IMG_SYS_NM <>", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmGreaterThan(String value) {
            addCriterion("IMG_SYS_NM >", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmGreaterThanOrEqualTo(String value) {
            addCriterion("IMG_SYS_NM >=", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmLessThan(String value) {
            addCriterion("IMG_SYS_NM <", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmLessThanOrEqualTo(String value) {
            addCriterion("IMG_SYS_NM <=", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmLike(String value) {
            addCriterion("IMG_SYS_NM like", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmNotLike(String value) {
            addCriterion("IMG_SYS_NM not like", value, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmIn(List<String> values) {
            addCriterion("IMG_SYS_NM in", values, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmNotIn(List<String> values) {
            addCriterion("IMG_SYS_NM not in", values, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmBetween(String value1, String value2) {
            addCriterion("IMG_SYS_NM between", value1, value2, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSysNmNotBetween(String value1, String value2) {
            addCriterion("IMG_SYS_NM not between", value1, value2, "imgSysNm");
            return (Criteria) this;
        }

        public Criteria andImgSizeIsNull() {
            addCriterion("IMG_SIZE is null");
            return (Criteria) this;
        }

        public Criteria andImgSizeIsNotNull() {
            addCriterion("IMG_SIZE is not null");
            return (Criteria) this;
        }

        public Criteria andImgSizeEqualTo(Integer value) {
            addCriterion("IMG_SIZE =", value, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeNotEqualTo(Integer value) {
            addCriterion("IMG_SIZE <>", value, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeGreaterThan(Integer value) {
            addCriterion("IMG_SIZE >", value, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("IMG_SIZE >=", value, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeLessThan(Integer value) {
            addCriterion("IMG_SIZE <", value, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeLessThanOrEqualTo(Integer value) {
            addCriterion("IMG_SIZE <=", value, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeIn(List<Integer> values) {
            addCriterion("IMG_SIZE in", values, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeNotIn(List<Integer> values) {
            addCriterion("IMG_SIZE not in", values, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeBetween(Integer value1, Integer value2) {
            addCriterion("IMG_SIZE between", value1, value2, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("IMG_SIZE not between", value1, value2, "imgSize");
            return (Criteria) this;
        }

        public Criteria andImgUrlIsNull() {
            addCriterion("IMG_URL is null");
            return (Criteria) this;
        }

        public Criteria andImgUrlIsNotNull() {
            addCriterion("IMG_URL is not null");
            return (Criteria) this;
        }

        public Criteria andImgUrlEqualTo(String value) {
            addCriterion("IMG_URL =", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotEqualTo(String value) {
            addCriterion("IMG_URL <>", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlGreaterThan(String value) {
            addCriterion("IMG_URL >", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlGreaterThanOrEqualTo(String value) {
            addCriterion("IMG_URL >=", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlLessThan(String value) {
            addCriterion("IMG_URL <", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlLessThanOrEqualTo(String value) {
            addCriterion("IMG_URL <=", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlLike(String value) {
            addCriterion("IMG_URL like", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotLike(String value) {
            addCriterion("IMG_URL not like", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlIn(List<String> values) {
            addCriterion("IMG_URL in", values, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotIn(List<String> values) {
            addCriterion("IMG_URL not in", values, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlBetween(String value1, String value2) {
            addCriterion("IMG_URL between", value1, value2, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotBetween(String value1, String value2) {
            addCriterion("IMG_URL not between", value1, value2, "imgUrl");
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

        public Criteria andUserAuthIsNull() {
            addCriterion("USER_AUTH is null");
            return (Criteria) this;
        }

        public Criteria andUserAuthIsNotNull() {
            addCriterion("USER_AUTH is not null");
            return (Criteria) this;
        }

        public Criteria andUserAuthEqualTo(String value) {
            addCriterion("USER_AUTH =", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthNotEqualTo(String value) {
            addCriterion("USER_AUTH <>", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthGreaterThan(String value) {
            addCriterion("USER_AUTH >", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthGreaterThanOrEqualTo(String value) {
            addCriterion("USER_AUTH >=", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthLessThan(String value) {
            addCriterion("USER_AUTH <", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthLessThanOrEqualTo(String value) {
            addCriterion("USER_AUTH <=", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthLike(String value) {
            addCriterion("USER_AUTH like", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthNotLike(String value) {
            addCriterion("USER_AUTH not like", value, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthIn(List<String> values) {
            addCriterion("USER_AUTH in", values, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthNotIn(List<String> values) {
            addCriterion("USER_AUTH not in", values, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthBetween(String value1, String value2) {
            addCriterion("USER_AUTH between", value1, value2, "userAuth");
            return (Criteria) this;
        }

        public Criteria andUserAuthNotBetween(String value1, String value2) {
            addCriterion("USER_AUTH not between", value1, value2, "userAuth");
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