package com.basoft.service.entity.customer.grade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GradeMstExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GradeMstExample() {
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

        public Criteria andGradeIdIsNull() {
            addCriterion("GRADE_ID is null");
            return (Criteria) this;
        }

        public Criteria andGradeIdIsNotNull() {
            addCriterion("GRADE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andGradeIdEqualTo(Long value) {
            addCriterion("GRADE_ID =", value, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdNotEqualTo(Long value) {
            addCriterion("GRADE_ID <>", value, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdGreaterThan(Long value) {
            addCriterion("GRADE_ID >", value, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("GRADE_ID >=", value, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdLessThan(Long value) {
            addCriterion("GRADE_ID <", value, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdLessThanOrEqualTo(Long value) {
            addCriterion("GRADE_ID <=", value, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdIn(List<Long> values) {
            addCriterion("GRADE_ID in", values, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdNotIn(List<Long> values) {
            addCriterion("GRADE_ID not in", values, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdBetween(Long value1, Long value2) {
            addCriterion("GRADE_ID between", value1, value2, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeIdNotBetween(Long value1, Long value2) {
            addCriterion("GRADE_ID not between", value1, value2, "gradeId");
            return (Criteria) this;
        }

        public Criteria andGradeNmIsNull() {
            addCriterion("GRADE_NM is null");
            return (Criteria) this;
        }

        public Criteria andGradeNmIsNotNull() {
            addCriterion("GRADE_NM is not null");
            return (Criteria) this;
        }

        public Criteria andGradeNmEqualTo(String value) {
            addCriterion("GRADE_NM =", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmNotEqualTo(String value) {
            addCriterion("GRADE_NM <>", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmGreaterThan(String value) {
            addCriterion("GRADE_NM >", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmGreaterThanOrEqualTo(String value) {
            addCriterion("GRADE_NM >=", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmLessThan(String value) {
            addCriterion("GRADE_NM <", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmLessThanOrEqualTo(String value) {
            addCriterion("GRADE_NM <=", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmLike(String value) {
            addCriterion("GRADE_NM like", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmNotLike(String value) {
            addCriterion("GRADE_NM not like", value, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmIn(List<String> values) {
            addCriterion("GRADE_NM in", values, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmNotIn(List<String> values) {
            addCriterion("GRADE_NM not in", values, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmBetween(String value1, String value2) {
            addCriterion("GRADE_NM between", value1, value2, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andGradeNmNotBetween(String value1, String value2) {
            addCriterion("GRADE_NM not between", value1, value2, "gradeNm");
            return (Criteria) this;
        }

        public Criteria andBaseQtyIsNull() {
            addCriterion("BASE_QTY is null");
            return (Criteria) this;
        }

        public Criteria andBaseQtyIsNotNull() {
            addCriterion("BASE_QTY is not null");
            return (Criteria) this;
        }

        public Criteria andBaseQtyEqualTo(Integer value) {
            addCriterion("BASE_QTY =", value, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyNotEqualTo(Integer value) {
            addCriterion("BASE_QTY <>", value, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyGreaterThan(Integer value) {
            addCriterion("BASE_QTY >", value, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("BASE_QTY >=", value, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyLessThan(Integer value) {
            addCriterion("BASE_QTY <", value, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyLessThanOrEqualTo(Integer value) {
            addCriterion("BASE_QTY <=", value, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyIn(List<Integer> values) {
            addCriterion("BASE_QTY in", values, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyNotIn(List<Integer> values) {
            addCriterion("BASE_QTY not in", values, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyBetween(Integer value1, Integer value2) {
            addCriterion("BASE_QTY between", value1, value2, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBaseQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("BASE_QTY not between", value1, value2, "baseQty");
            return (Criteria) this;
        }

        public Criteria andBasePriceIsNull() {
            addCriterion("BASE_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andBasePriceIsNotNull() {
            addCriterion("BASE_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andBasePriceEqualTo(BigDecimal value) {
            addCriterion("BASE_PRICE =", value, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceNotEqualTo(BigDecimal value) {
            addCriterion("BASE_PRICE <>", value, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceGreaterThan(BigDecimal value) {
            addCriterion("BASE_PRICE >", value, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("BASE_PRICE >=", value, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceLessThan(BigDecimal value) {
            addCriterion("BASE_PRICE <", value, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("BASE_PRICE <=", value, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceIn(List<BigDecimal> values) {
            addCriterion("BASE_PRICE in", values, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceNotIn(List<BigDecimal> values) {
            addCriterion("BASE_PRICE not in", values, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("BASE_PRICE between", value1, value2, "basePrice");
            return (Criteria) this;
        }

        public Criteria andBasePriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("BASE_PRICE not between", value1, value2, "basePrice");
            return (Criteria) this;
        }

        public Criteria andIsUseIsNull() {
            addCriterion("IS_USE is null");
            return (Criteria) this;
        }

        public Criteria andIsUseIsNotNull() {
            addCriterion("IS_USE is not null");
            return (Criteria) this;
        }

        public Criteria andIsUseEqualTo(Byte value) {
            addCriterion("IS_USE =", value, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseNotEqualTo(Byte value) {
            addCriterion("IS_USE <>", value, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseGreaterThan(Byte value) {
            addCriterion("IS_USE >", value, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseGreaterThanOrEqualTo(Byte value) {
            addCriterion("IS_USE >=", value, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseLessThan(Byte value) {
            addCriterion("IS_USE <", value, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseLessThanOrEqualTo(Byte value) {
            addCriterion("IS_USE <=", value, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseIn(List<Byte> values) {
            addCriterion("IS_USE in", values, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseNotIn(List<Byte> values) {
            addCriterion("IS_USE not in", values, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseBetween(Byte value1, Byte value2) {
            addCriterion("IS_USE between", value1, value2, "isUse");
            return (Criteria) this;
        }

        public Criteria andIsUseNotBetween(Byte value1, Byte value2) {
            addCriterion("IS_USE not between", value1, value2, "isUse");
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

        public Criteria andCreatedIdIsNull() {
            addCriterion("CREATED_ID is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIdIsNotNull() {
            addCriterion("CREATED_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedIdEqualTo(String value) {
            addCriterion("CREATED_ID =", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotEqualTo(String value) {
            addCriterion("CREATED_ID <>", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdGreaterThan(String value) {
            addCriterion("CREATED_ID >", value, "createdId");
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

        public Criteria andCreatedIdLessThanOrEqualTo(String value) {
            addCriterion("CREATED_ID <=", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdLike(String value) {
            addCriterion("CREATED_ID like", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotLike(String value) {
            addCriterion("CREATED_ID not like", value, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdIn(List<String> values) {
            addCriterion("CREATED_ID in", values, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotIn(List<String> values) {
            addCriterion("CREATED_ID not in", values, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdBetween(String value1, String value2) {
            addCriterion("CREATED_ID between", value1, value2, "createdId");
            return (Criteria) this;
        }

        public Criteria andCreatedIdNotBetween(String value1, String value2) {
            addCriterion("CREATED_ID not between", value1, value2, "createdId");
            return (Criteria) this;
        }

        public Criteria andEvCycleIsNull() {
            addCriterion("EV_CYCLE is null");
            return (Criteria) this;
        }

        public Criteria andEvCycleIsNotNull() {
            addCriterion("EV_CYCLE is not null");
            return (Criteria) this;
        }

        public Criteria andEvCycleEqualTo(Integer value) {
            addCriterion("EV_CYCLE =", value, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleNotEqualTo(Integer value) {
            addCriterion("EV_CYCLE <>", value, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleGreaterThan(Integer value) {
            addCriterion("EV_CYCLE >", value, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleGreaterThanOrEqualTo(Integer value) {
            addCriterion("EV_CYCLE >=", value, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleLessThan(Integer value) {
            addCriterion("EV_CYCLE <", value, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleLessThanOrEqualTo(Integer value) {
            addCriterion("EV_CYCLE <=", value, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleIn(List<Integer> values) {
            addCriterion("EV_CYCLE in", values, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleNotIn(List<Integer> values) {
            addCriterion("EV_CYCLE not in", values, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleBetween(Integer value1, Integer value2) {
            addCriterion("EV_CYCLE between", value1, value2, "evCycle");
            return (Criteria) this;
        }

        public Criteria andEvCycleNotBetween(Integer value1, Integer value2) {
            addCriterion("EV_CYCLE not between", value1, value2, "evCycle");
            return (Criteria) this;
        }

        public Criteria andExtendedIsNull() {
            addCriterion("EXTENDED is null");
            return (Criteria) this;
        }

        public Criteria andExtendedIsNotNull() {
            addCriterion("EXTENDED is not null");
            return (Criteria) this;
        }

        public Criteria andExtendedEqualTo(Integer value) {
            addCriterion("EXTENDED =", value, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedNotEqualTo(Integer value) {
            addCriterion("EXTENDED <>", value, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedGreaterThan(Integer value) {
            addCriterion("EXTENDED >", value, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedGreaterThanOrEqualTo(Integer value) {
            addCriterion("EXTENDED >=", value, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedLessThan(Integer value) {
            addCriterion("EXTENDED <", value, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedLessThanOrEqualTo(Integer value) {
            addCriterion("EXTENDED <=", value, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedIn(List<Integer> values) {
            addCriterion("EXTENDED in", values, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedNotIn(List<Integer> values) {
            addCriterion("EXTENDED not in", values, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedBetween(Integer value1, Integer value2) {
            addCriterion("EXTENDED between", value1, value2, "extended");
            return (Criteria) this;
        }

        public Criteria andExtendedNotBetween(Integer value1, Integer value2) {
            addCriterion("EXTENDED not between", value1, value2, "extended");
            return (Criteria) this;
        }

        public Criteria andBuyPointIsNull() {
            addCriterion("BUY_POINT is null");
            return (Criteria) this;
        }

        public Criteria andBuyPointIsNotNull() {
            addCriterion("BUY_POINT is not null");
            return (Criteria) this;
        }

        public Criteria andBuyPointEqualTo(Integer value) {
            addCriterion("BUY_POINT =", value, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointNotEqualTo(Integer value) {
            addCriterion("BUY_POINT <>", value, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointGreaterThan(Integer value) {
            addCriterion("BUY_POINT >", value, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointGreaterThanOrEqualTo(Integer value) {
            addCriterion("BUY_POINT >=", value, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointLessThan(Integer value) {
            addCriterion("BUY_POINT <", value, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointLessThanOrEqualTo(Integer value) {
            addCriterion("BUY_POINT <=", value, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointIn(List<Integer> values) {
            addCriterion("BUY_POINT in", values, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointNotIn(List<Integer> values) {
            addCriterion("BUY_POINT not in", values, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointBetween(Integer value1, Integer value2) {
            addCriterion("BUY_POINT between", value1, value2, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andBuyPointNotBetween(Integer value1, Integer value2) {
            addCriterion("BUY_POINT not between", value1, value2, "buyPoint");
            return (Criteria) this;
        }

        public Criteria andSellForCashIsNull() {
            addCriterion("SELL_FOR_CASH is null");
            return (Criteria) this;
        }

        public Criteria andSellForCashIsNotNull() {
            addCriterion("SELL_FOR_CASH is not null");
            return (Criteria) this;
        }

        public Criteria andSellForCashEqualTo(Integer value) {
            addCriterion("SELL_FOR_CASH =", value, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashNotEqualTo(Integer value) {
            addCriterion("SELL_FOR_CASH <>", value, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashGreaterThan(Integer value) {
            addCriterion("SELL_FOR_CASH >", value, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashGreaterThanOrEqualTo(Integer value) {
            addCriterion("SELL_FOR_CASH >=", value, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashLessThan(Integer value) {
            addCriterion("SELL_FOR_CASH <", value, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashLessThanOrEqualTo(Integer value) {
            addCriterion("SELL_FOR_CASH <=", value, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashIn(List<Integer> values) {
            addCriterion("SELL_FOR_CASH in", values, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashNotIn(List<Integer> values) {
            addCriterion("SELL_FOR_CASH not in", values, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashBetween(Integer value1, Integer value2) {
            addCriterion("SELL_FOR_CASH between", value1, value2, "sellForCash");
            return (Criteria) this;
        }

        public Criteria andSellForCashNotBetween(Integer value1, Integer value2) {
            addCriterion("SELL_FOR_CASH not between", value1, value2, "sellForCash");
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