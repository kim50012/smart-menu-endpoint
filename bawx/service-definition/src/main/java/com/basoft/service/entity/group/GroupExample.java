package com.basoft.service.entity.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GroupExample() {
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

        public Criteria andGCorpNmIsNull() {
            addCriterion("G_CORP_NM is null");
            return (Criteria) this;
        }

        public Criteria andGCorpNmIsNotNull() {
            addCriterion("G_CORP_NM is not null");
            return (Criteria) this;
        }

        public Criteria andGCorpNmEqualTo(String value) {
            addCriterion("G_CORP_NM =", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmNotEqualTo(String value) {
            addCriterion("G_CORP_NM <>", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmGreaterThan(String value) {
            addCriterion("G_CORP_NM >", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmGreaterThanOrEqualTo(String value) {
            addCriterion("G_CORP_NM >=", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmLessThan(String value) {
            addCriterion("G_CORP_NM <", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmLessThanOrEqualTo(String value) {
            addCriterion("G_CORP_NM <=", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmLike(String value) {
            addCriterion("G_CORP_NM like", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmNotLike(String value) {
            addCriterion("G_CORP_NM not like", value, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmIn(List<String> values) {
            addCriterion("G_CORP_NM in", values, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmNotIn(List<String> values) {
            addCriterion("G_CORP_NM not in", values, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmBetween(String value1, String value2) {
            addCriterion("G_CORP_NM between", value1, value2, "gCorpNm");
            return (Criteria) this;
        }

        public Criteria andGCorpNmNotBetween(String value1, String value2) {
            addCriterion("G_CORP_NM not between", value1, value2, "gCorpNm");
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

        public Criteria andAdminUserIdIsNull() {
            addCriterion("ADMIN_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdIsNotNull() {
            addCriterion("ADMIN_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdEqualTo(String value) {
            addCriterion("ADMIN_USER_ID =", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdNotEqualTo(String value) {
            addCriterion("ADMIN_USER_ID <>", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdGreaterThan(String value) {
            addCriterion("ADMIN_USER_ID >", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("ADMIN_USER_ID >=", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdLessThan(String value) {
            addCriterion("ADMIN_USER_ID <", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdLessThanOrEqualTo(String value) {
            addCriterion("ADMIN_USER_ID <=", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdLike(String value) {
            addCriterion("ADMIN_USER_ID like", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdNotLike(String value) {
            addCriterion("ADMIN_USER_ID not like", value, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdIn(List<String> values) {
            addCriterion("ADMIN_USER_ID in", values, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdNotIn(List<String> values) {
            addCriterion("ADMIN_USER_ID not in", values, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdBetween(String value1, String value2) {
            addCriterion("ADMIN_USER_ID between", value1, value2, "adminUserId");
            return (Criteria) this;
        }

        public Criteria andAdminUserIdNotBetween(String value1, String value2) {
            addCriterion("ADMIN_USER_ID not between", value1, value2, "adminUserId");
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

        public Criteria andSortNoIsNull() {
            addCriterion("SORT_NO is null");
            return (Criteria) this;
        }

        public Criteria andSortNoIsNotNull() {
            addCriterion("SORT_NO is not null");
            return (Criteria) this;
        }

        public Criteria andSortNoEqualTo(Byte value) {
            addCriterion("SORT_NO =", value, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoNotEqualTo(Byte value) {
            addCriterion("SORT_NO <>", value, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoGreaterThan(Byte value) {
            addCriterion("SORT_NO >", value, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoGreaterThanOrEqualTo(Byte value) {
            addCriterion("SORT_NO >=", value, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoLessThan(Byte value) {
            addCriterion("SORT_NO <", value, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoLessThanOrEqualTo(Byte value) {
            addCriterion("SORT_NO <=", value, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoIn(List<Byte> values) {
            addCriterion("SORT_NO in", values, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoNotIn(List<Byte> values) {
            addCriterion("SORT_NO not in", values, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoBetween(Byte value1, Byte value2) {
            addCriterion("SORT_NO between", value1, value2, "sortNo");
            return (Criteria) this;
        }

        public Criteria andSortNoNotBetween(Byte value1, Byte value2) {
            addCriterion("SORT_NO not between", value1, value2, "sortNo");
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