package com.basoft.service.entity.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopFileExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopFileExample() {
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

        public Criteria andFileIdIsNull() {
            addCriterion("FILE_ID is null");
            return (Criteria) this;
        }

        public Criteria andFileIdIsNotNull() {
            addCriterion("FILE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andFileIdEqualTo(Long value) {
            addCriterion("FILE_ID =", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotEqualTo(Long value) {
            addCriterion("FILE_ID <>", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThan(Long value) {
            addCriterion("FILE_ID >", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThanOrEqualTo(Long value) {
            addCriterion("FILE_ID >=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThan(Long value) {
            addCriterion("FILE_ID <", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThanOrEqualTo(Long value) {
            addCriterion("FILE_ID <=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdIn(List<Long> values) {
            addCriterion("FILE_ID in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotIn(List<Long> values) {
            addCriterion("FILE_ID not in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdBetween(Long value1, Long value2) {
            addCriterion("FILE_ID between", value1, value2, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotBetween(Long value1, Long value2) {
            addCriterion("FILE_ID not between", value1, value2, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileNmIsNull() {
            addCriterion("FILE_NM is null");
            return (Criteria) this;
        }

        public Criteria andFileNmIsNotNull() {
            addCriterion("FILE_NM is not null");
            return (Criteria) this;
        }

        public Criteria andFileNmEqualTo(String value) {
            addCriterion("FILE_NM =", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmNotEqualTo(String value) {
            addCriterion("FILE_NM <>", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmGreaterThan(String value) {
            addCriterion("FILE_NM >", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmGreaterThanOrEqualTo(String value) {
            addCriterion("FILE_NM >=", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmLessThan(String value) {
            addCriterion("FILE_NM <", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmLessThanOrEqualTo(String value) {
            addCriterion("FILE_NM <=", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmLike(String value) {
            addCriterion("FILE_NM like", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmNotLike(String value) {
            addCriterion("FILE_NM not like", value, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmIn(List<String> values) {
            addCriterion("FILE_NM in", values, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmNotIn(List<String> values) {
            addCriterion("FILE_NM not in", values, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmBetween(String value1, String value2) {
            addCriterion("FILE_NM between", value1, value2, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileNmNotBetween(String value1, String value2) {
            addCriterion("FILE_NM not between", value1, value2, "fileNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmIsNull() {
            addCriterion("FILE_SYS_NM is null");
            return (Criteria) this;
        }

        public Criteria andFileSysNmIsNotNull() {
            addCriterion("FILE_SYS_NM is not null");
            return (Criteria) this;
        }

        public Criteria andFileSysNmEqualTo(String value) {
            addCriterion("FILE_SYS_NM =", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmNotEqualTo(String value) {
            addCriterion("FILE_SYS_NM <>", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmGreaterThan(String value) {
            addCriterion("FILE_SYS_NM >", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmGreaterThanOrEqualTo(String value) {
            addCriterion("FILE_SYS_NM >=", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmLessThan(String value) {
            addCriterion("FILE_SYS_NM <", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmLessThanOrEqualTo(String value) {
            addCriterion("FILE_SYS_NM <=", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmLike(String value) {
            addCriterion("FILE_SYS_NM like", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmNotLike(String value) {
            addCriterion("FILE_SYS_NM not like", value, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmIn(List<String> values) {
            addCriterion("FILE_SYS_NM in", values, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmNotIn(List<String> values) {
            addCriterion("FILE_SYS_NM not in", values, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmBetween(String value1, String value2) {
            addCriterion("FILE_SYS_NM between", value1, value2, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileSysNmNotBetween(String value1, String value2) {
            addCriterion("FILE_SYS_NM not between", value1, value2, "fileSysNm");
            return (Criteria) this;
        }

        public Criteria andFileTypeIsNull() {
            addCriterion("FILE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andFileTypeIsNotNull() {
            addCriterion("FILE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andFileTypeEqualTo(Byte value) {
            addCriterion("FILE_TYPE =", value, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeNotEqualTo(Byte value) {
            addCriterion("FILE_TYPE <>", value, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeGreaterThan(Byte value) {
            addCriterion("FILE_TYPE >", value, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("FILE_TYPE >=", value, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeLessThan(Byte value) {
            addCriterion("FILE_TYPE <", value, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeLessThanOrEqualTo(Byte value) {
            addCriterion("FILE_TYPE <=", value, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeIn(List<Byte> values) {
            addCriterion("FILE_TYPE in", values, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeNotIn(List<Byte> values) {
            addCriterion("FILE_TYPE not in", values, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeBetween(Byte value1, Byte value2) {
            addCriterion("FILE_TYPE between", value1, value2, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("FILE_TYPE not between", value1, value2, "fileType");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNull() {
            addCriterion("FILE_SIZE is null");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNotNull() {
            addCriterion("FILE_SIZE is not null");
            return (Criteria) this;
        }

        public Criteria andFileSizeEqualTo(Long value) {
            addCriterion("FILE_SIZE =", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotEqualTo(Long value) {
            addCriterion("FILE_SIZE <>", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThan(Long value) {
            addCriterion("FILE_SIZE >", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("FILE_SIZE >=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThan(Long value) {
            addCriterion("FILE_SIZE <", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThanOrEqualTo(Long value) {
            addCriterion("FILE_SIZE <=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeIn(List<Long> values) {
            addCriterion("FILE_SIZE in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotIn(List<Long> values) {
            addCriterion("FILE_SIZE not in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeBetween(Long value1, Long value2) {
            addCriterion("FILE_SIZE between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotBetween(Long value1, Long value2) {
            addCriterion("FILE_SIZE not between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFullUrlIsNull() {
            addCriterion("FULL_URL is null");
            return (Criteria) this;
        }

        public Criteria andFullUrlIsNotNull() {
            addCriterion("FULL_URL is not null");
            return (Criteria) this;
        }

        public Criteria andFullUrlEqualTo(String value) {
            addCriterion("FULL_URL =", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotEqualTo(String value) {
            addCriterion("FULL_URL <>", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlGreaterThan(String value) {
            addCriterion("FULL_URL >", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlGreaterThanOrEqualTo(String value) {
            addCriterion("FULL_URL >=", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlLessThan(String value) {
            addCriterion("FULL_URL <", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlLessThanOrEqualTo(String value) {
            addCriterion("FULL_URL <=", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlLike(String value) {
            addCriterion("FULL_URL like", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotLike(String value) {
            addCriterion("FULL_URL not like", value, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlIn(List<String> values) {
            addCriterion("FULL_URL in", values, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotIn(List<String> values) {
            addCriterion("FULL_URL not in", values, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlBetween(String value1, String value2) {
            addCriterion("FULL_URL between", value1, value2, "fullUrl");
            return (Criteria) this;
        }

        public Criteria andFullUrlNotBetween(String value1, String value2) {
            addCriterion("FULL_URL not between", value1, value2, "fullUrl");
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

        public Criteria andFileGroupIsNull() {
            addCriterion("FILE_GROUP is null");
            return (Criteria) this;
        }

        public Criteria andFileGroupIsNotNull() {
            addCriterion("FILE_GROUP is not null");
            return (Criteria) this;
        }

        public Criteria andFileGroupEqualTo(Byte value) {
            addCriterion("FILE_GROUP =", value, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupNotEqualTo(Byte value) {
            addCriterion("FILE_GROUP <>", value, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupGreaterThan(Byte value) {
            addCriterion("FILE_GROUP >", value, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupGreaterThanOrEqualTo(Byte value) {
            addCriterion("FILE_GROUP >=", value, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupLessThan(Byte value) {
            addCriterion("FILE_GROUP <", value, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupLessThanOrEqualTo(Byte value) {
            addCriterion("FILE_GROUP <=", value, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupIn(List<Byte> values) {
            addCriterion("FILE_GROUP in", values, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupNotIn(List<Byte> values) {
            addCriterion("FILE_GROUP not in", values, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupBetween(Byte value1, Byte value2) {
            addCriterion("FILE_GROUP between", value1, value2, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andFileGroupNotBetween(Byte value1, Byte value2) {
            addCriterion("FILE_GROUP not between", value1, value2, "fileGroup");
            return (Criteria) this;
        }

        public Criteria andMediaIdIsNull() {
            addCriterion("MEDIA_ID is null");
            return (Criteria) this;
        }

        public Criteria andMediaIdIsNotNull() {
            addCriterion("MEDIA_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMediaIdEqualTo(String value) {
            addCriterion("MEDIA_ID =", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotEqualTo(String value) {
            addCriterion("MEDIA_ID <>", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdGreaterThan(String value) {
            addCriterion("MEDIA_ID >", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdGreaterThanOrEqualTo(String value) {
            addCriterion("MEDIA_ID >=", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdLessThan(String value) {
            addCriterion("MEDIA_ID <", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdLessThanOrEqualTo(String value) {
            addCriterion("MEDIA_ID <=", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdLike(String value) {
            addCriterion("MEDIA_ID like", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotLike(String value) {
            addCriterion("MEDIA_ID not like", value, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdIn(List<String> values) {
            addCriterion("MEDIA_ID in", values, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotIn(List<String> values) {
            addCriterion("MEDIA_ID not in", values, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdBetween(String value1, String value2) {
            addCriterion("MEDIA_ID between", value1, value2, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaIdNotBetween(String value1, String value2) {
            addCriterion("MEDIA_ID not between", value1, value2, "mediaId");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtIsNull() {
            addCriterion("MEDIA_UP_DT is null");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtIsNotNull() {
            addCriterion("MEDIA_UP_DT is not null");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtEqualTo(Date value) {
            addCriterion("MEDIA_UP_DT =", value, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtNotEqualTo(Date value) {
            addCriterion("MEDIA_UP_DT <>", value, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtGreaterThan(Date value) {
            addCriterion("MEDIA_UP_DT >", value, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtGreaterThanOrEqualTo(Date value) {
            addCriterion("MEDIA_UP_DT >=", value, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtLessThan(Date value) {
            addCriterion("MEDIA_UP_DT <", value, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtLessThanOrEqualTo(Date value) {
            addCriterion("MEDIA_UP_DT <=", value, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtIn(List<Date> values) {
            addCriterion("MEDIA_UP_DT in", values, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtNotIn(List<Date> values) {
            addCriterion("MEDIA_UP_DT not in", values, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtBetween(Date value1, Date value2) {
            addCriterion("MEDIA_UP_DT between", value1, value2, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andMediaUpDtNotBetween(Date value1, Date value2) {
            addCriterion("MEDIA_UP_DT not between", value1, value2, "mediaUpDt");
            return (Criteria) this;
        }

        public Criteria andImgWidthIsNull() {
            addCriterion("IMG_WIDTH is null");
            return (Criteria) this;
        }

        public Criteria andImgWidthIsNotNull() {
            addCriterion("IMG_WIDTH is not null");
            return (Criteria) this;
        }

        public Criteria andImgWidthEqualTo(Integer value) {
            addCriterion("IMG_WIDTH =", value, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthNotEqualTo(Integer value) {
            addCriterion("IMG_WIDTH <>", value, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthGreaterThan(Integer value) {
            addCriterion("IMG_WIDTH >", value, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthGreaterThanOrEqualTo(Integer value) {
            addCriterion("IMG_WIDTH >=", value, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthLessThan(Integer value) {
            addCriterion("IMG_WIDTH <", value, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthLessThanOrEqualTo(Integer value) {
            addCriterion("IMG_WIDTH <=", value, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthIn(List<Integer> values) {
            addCriterion("IMG_WIDTH in", values, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthNotIn(List<Integer> values) {
            addCriterion("IMG_WIDTH not in", values, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthBetween(Integer value1, Integer value2) {
            addCriterion("IMG_WIDTH between", value1, value2, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgWidthNotBetween(Integer value1, Integer value2) {
            addCriterion("IMG_WIDTH not between", value1, value2, "imgWidth");
            return (Criteria) this;
        }

        public Criteria andImgHeightIsNull() {
            addCriterion("IMG_HEIGHT is null");
            return (Criteria) this;
        }

        public Criteria andImgHeightIsNotNull() {
            addCriterion("IMG_HEIGHT is not null");
            return (Criteria) this;
        }

        public Criteria andImgHeightEqualTo(Integer value) {
            addCriterion("IMG_HEIGHT =", value, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightNotEqualTo(Integer value) {
            addCriterion("IMG_HEIGHT <>", value, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightGreaterThan(Integer value) {
            addCriterion("IMG_HEIGHT >", value, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("IMG_HEIGHT >=", value, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightLessThan(Integer value) {
            addCriterion("IMG_HEIGHT <", value, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightLessThanOrEqualTo(Integer value) {
            addCriterion("IMG_HEIGHT <=", value, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightIn(List<Integer> values) {
            addCriterion("IMG_HEIGHT in", values, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightNotIn(List<Integer> values) {
            addCriterion("IMG_HEIGHT not in", values, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightBetween(Integer value1, Integer value2) {
            addCriterion("IMG_HEIGHT between", value1, value2, "imgHeight");
            return (Criteria) this;
        }

        public Criteria andImgHeightNotBetween(Integer value1, Integer value2) {
            addCriterion("IMG_HEIGHT not between", value1, value2, "imgHeight");
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

        public Criteria andCompIdEqualTo(Long value) {
            addCriterion("COMP_ID =", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdNotEqualTo(Long value) {
            addCriterion("COMP_ID <>", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdGreaterThan(Long value) {
            addCriterion("COMP_ID >", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdGreaterThanOrEqualTo(Long value) {
            addCriterion("COMP_ID >=", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdLessThan(Long value) {
            addCriterion("COMP_ID <", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdLessThanOrEqualTo(Long value) {
            addCriterion("COMP_ID <=", value, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdIn(List<Long> values) {
            addCriterion("COMP_ID in", values, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdNotIn(List<Long> values) {
            addCriterion("COMP_ID not in", values, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdBetween(Long value1, Long value2) {
            addCriterion("COMP_ID between", value1, value2, "compId");
            return (Criteria) this;
        }

        public Criteria andCompIdNotBetween(Long value1, Long value2) {
            addCriterion("COMP_ID not between", value1, value2, "compId");
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