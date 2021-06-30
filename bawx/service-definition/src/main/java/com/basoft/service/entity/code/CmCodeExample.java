package com.basoft.service.entity.code;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmCodeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CmCodeExample() {
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

        public Criteria andTbNmIsNull() {
            addCriterion("TB_NM is null");
            return (Criteria) this;
        }

        public Criteria andTbNmIsNotNull() {
            addCriterion("TB_NM is not null");
            return (Criteria) this;
        }

        public Criteria andTbNmEqualTo(String value) {
            addCriterion("TB_NM =", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmNotEqualTo(String value) {
            addCriterion("TB_NM <>", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmGreaterThan(String value) {
            addCriterion("TB_NM >", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmGreaterThanOrEqualTo(String value) {
            addCriterion("TB_NM >=", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmLessThan(String value) {
            addCriterion("TB_NM <", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmLessThanOrEqualTo(String value) {
            addCriterion("TB_NM <=", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmLike(String value) {
            addCriterion("TB_NM like", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmNotLike(String value) {
            addCriterion("TB_NM not like", value, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmIn(List<String> values) {
            addCriterion("TB_NM in", values, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmNotIn(List<String> values) {
            addCriterion("TB_NM not in", values, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmBetween(String value1, String value2) {
            addCriterion("TB_NM between", value1, value2, "tbNm");
            return (Criteria) this;
        }

        public Criteria andTbNmNotBetween(String value1, String value2) {
            addCriterion("TB_NM not between", value1, value2, "tbNm");
            return (Criteria) this;
        }

        public Criteria andFdNmIsNull() {
            addCriterion("FD_NM is null");
            return (Criteria) this;
        }

        public Criteria andFdNmIsNotNull() {
            addCriterion("FD_NM is not null");
            return (Criteria) this;
        }

        public Criteria andFdNmEqualTo(String value) {
            addCriterion("FD_NM =", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmNotEqualTo(String value) {
            addCriterion("FD_NM <>", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmGreaterThan(String value) {
            addCriterion("FD_NM >", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmGreaterThanOrEqualTo(String value) {
            addCriterion("FD_NM >=", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmLessThan(String value) {
            addCriterion("FD_NM <", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmLessThanOrEqualTo(String value) {
            addCriterion("FD_NM <=", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmLike(String value) {
            addCriterion("FD_NM like", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmNotLike(String value) {
            addCriterion("FD_NM not like", value, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmIn(List<String> values) {
            addCriterion("FD_NM in", values, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmNotIn(List<String> values) {
            addCriterion("FD_NM not in", values, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmBetween(String value1, String value2) {
            addCriterion("FD_NM between", value1, value2, "fdNm");
            return (Criteria) this;
        }

        public Criteria andFdNmNotBetween(String value1, String value2) {
            addCriterion("FD_NM not between", value1, value2, "fdNm");
            return (Criteria) this;
        }

        public Criteria andCdIdIsNull() {
            addCriterion("CD_ID is null");
            return (Criteria) this;
        }

        public Criteria andCdIdIsNotNull() {
            addCriterion("CD_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCdIdEqualTo(Integer value) {
            addCriterion("CD_ID =", value, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdNotEqualTo(Integer value) {
            addCriterion("CD_ID <>", value, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdGreaterThan(Integer value) {
            addCriterion("CD_ID >", value, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("CD_ID >=", value, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdLessThan(Integer value) {
            addCriterion("CD_ID <", value, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdLessThanOrEqualTo(Integer value) {
            addCriterion("CD_ID <=", value, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdIn(List<Integer> values) {
            addCriterion("CD_ID in", values, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdNotIn(List<Integer> values) {
            addCriterion("CD_ID not in", values, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdBetween(Integer value1, Integer value2) {
            addCriterion("CD_ID between", value1, value2, "cdId");
            return (Criteria) this;
        }

        public Criteria andCdIdNotBetween(Integer value1, Integer value2) {
            addCriterion("CD_ID not between", value1, value2, "cdId");
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

        public Criteria andIsDeleteEqualTo(Byte value) {
            addCriterion("IS_DELETE =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Byte value) {
            addCriterion("IS_DELETE <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Byte value) {
            addCriterion("IS_DELETE >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Byte value) {
            addCriterion("IS_DELETE >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Byte value) {
            addCriterion("IS_DELETE <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Byte value) {
            addCriterion("IS_DELETE <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Byte> values) {
            addCriterion("IS_DELETE in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Byte> values) {
            addCriterion("IS_DELETE not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Byte value1, Byte value2) {
            addCriterion("IS_DELETE between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Byte value1, Byte value2) {
            addCriterion("IS_DELETE not between", value1, value2, "isDelete");
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

        public Criteria andModifyDtIsNull() {
            addCriterion("MODIFY_DT is null");
            return (Criteria) this;
        }

        public Criteria andModifyDtIsNotNull() {
            addCriterion("MODIFY_DT is not null");
            return (Criteria) this;
        }

        public Criteria andModifyDtEqualTo(Date value) {
            addCriterion("MODIFY_DT =", value, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtNotEqualTo(Date value) {
            addCriterion("MODIFY_DT <>", value, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtGreaterThan(Date value) {
            addCriterion("MODIFY_DT >", value, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtGreaterThanOrEqualTo(Date value) {
            addCriterion("MODIFY_DT >=", value, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtLessThan(Date value) {
            addCriterion("MODIFY_DT <", value, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtLessThanOrEqualTo(Date value) {
            addCriterion("MODIFY_DT <=", value, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtIn(List<Date> values) {
            addCriterion("MODIFY_DT in", values, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtNotIn(List<Date> values) {
            addCriterion("MODIFY_DT not in", values, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtBetween(Date value1, Date value2) {
            addCriterion("MODIFY_DT between", value1, value2, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andModifyDtNotBetween(Date value1, Date value2) {
            addCriterion("MODIFY_DT not between", value1, value2, "modifyDt");
            return (Criteria) this;
        }

        public Criteria andCdTpIsNull() {
            addCriterion("CD_TP is null");
            return (Criteria) this;
        }

        public Criteria andCdTpIsNotNull() {
            addCriterion("CD_TP is not null");
            return (Criteria) this;
        }

        public Criteria andCdTpEqualTo(String value) {
            addCriterion("CD_TP =", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpNotEqualTo(String value) {
            addCriterion("CD_TP <>", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpGreaterThan(String value) {
            addCriterion("CD_TP >", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpGreaterThanOrEqualTo(String value) {
            addCriterion("CD_TP >=", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpLessThan(String value) {
            addCriterion("CD_TP <", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpLessThanOrEqualTo(String value) {
            addCriterion("CD_TP <=", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpLike(String value) {
            addCriterion("CD_TP like", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpNotLike(String value) {
            addCriterion("CD_TP not like", value, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpIn(List<String> values) {
            addCriterion("CD_TP in", values, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpNotIn(List<String> values) {
            addCriterion("CD_TP not in", values, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpBetween(String value1, String value2) {
            addCriterion("CD_TP between", value1, value2, "cdTp");
            return (Criteria) this;
        }

        public Criteria andCdTpNotBetween(String value1, String value2) {
            addCriterion("CD_TP not between", value1, value2, "cdTp");
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