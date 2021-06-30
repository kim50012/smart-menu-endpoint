package com.basoft.service.entity.customer.custShop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustShopExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CustShopExample() {
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

        public Criteria andCustPointIsNull() {
            addCriterion("CUST_POINT is null");
            return (Criteria) this;
        }

        public Criteria andCustPointIsNotNull() {
            addCriterion("CUST_POINT is not null");
            return (Criteria) this;
        }

        public Criteria andCustPointEqualTo(Integer value) {
            addCriterion("CUST_POINT =", value, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointNotEqualTo(Integer value) {
            addCriterion("CUST_POINT <>", value, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointGreaterThan(Integer value) {
            addCriterion("CUST_POINT >", value, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointGreaterThanOrEqualTo(Integer value) {
            addCriterion("CUST_POINT >=", value, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointLessThan(Integer value) {
            addCriterion("CUST_POINT <", value, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointLessThanOrEqualTo(Integer value) {
            addCriterion("CUST_POINT <=", value, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointIn(List<Integer> values) {
            addCriterion("CUST_POINT in", values, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointNotIn(List<Integer> values) {
            addCriterion("CUST_POINT not in", values, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointBetween(Integer value1, Integer value2) {
            addCriterion("CUST_POINT between", value1, value2, "custPoint");
            return (Criteria) this;
        }

        public Criteria andCustPointNotBetween(Integer value1, Integer value2) {
            addCriterion("CUST_POINT not between", value1, value2, "custPoint");
            return (Criteria) this;
        }

        public Criteria andFollowDtIsNull() {
            addCriterion("FOLLOW_DT is null");
            return (Criteria) this;
        }

        public Criteria andFollowDtIsNotNull() {
            addCriterion("FOLLOW_DT is not null");
            return (Criteria) this;
        }

        public Criteria andFollowDtEqualTo(Date value) {
            addCriterion("FOLLOW_DT =", value, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtNotEqualTo(Date value) {
            addCriterion("FOLLOW_DT <>", value, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtGreaterThan(Date value) {
            addCriterion("FOLLOW_DT >", value, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtGreaterThanOrEqualTo(Date value) {
            addCriterion("FOLLOW_DT >=", value, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtLessThan(Date value) {
            addCriterion("FOLLOW_DT <", value, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtLessThanOrEqualTo(Date value) {
            addCriterion("FOLLOW_DT <=", value, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtIn(List<Date> values) {
            addCriterion("FOLLOW_DT in", values, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtNotIn(List<Date> values) {
            addCriterion("FOLLOW_DT not in", values, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtBetween(Date value1, Date value2) {
            addCriterion("FOLLOW_DT between", value1, value2, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtNotBetween(Date value1, Date value2) {
            addCriterion("FOLLOW_DT not between", value1, value2, "followDt");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanIsNull() {
            addCriterion("FOLLOW_DT_CAN is null");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanIsNotNull() {
            addCriterion("FOLLOW_DT_CAN is not null");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanEqualTo(Date value) {
            addCriterion("FOLLOW_DT_CAN =", value, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanNotEqualTo(Date value) {
            addCriterion("FOLLOW_DT_CAN <>", value, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanGreaterThan(Date value) {
            addCriterion("FOLLOW_DT_CAN >", value, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanGreaterThanOrEqualTo(Date value) {
            addCriterion("FOLLOW_DT_CAN >=", value, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanLessThan(Date value) {
            addCriterion("FOLLOW_DT_CAN <", value, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanLessThanOrEqualTo(Date value) {
            addCriterion("FOLLOW_DT_CAN <=", value, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanIn(List<Date> values) {
            addCriterion("FOLLOW_DT_CAN in", values, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanNotIn(List<Date> values) {
            addCriterion("FOLLOW_DT_CAN not in", values, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanBetween(Date value1, Date value2) {
            addCriterion("FOLLOW_DT_CAN between", value1, value2, "followDtCan");
            return (Criteria) this;
        }

        public Criteria andFollowDtCanNotBetween(Date value1, Date value2) {
            addCriterion("FOLLOW_DT_CAN not between", value1, value2, "followDtCan");
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