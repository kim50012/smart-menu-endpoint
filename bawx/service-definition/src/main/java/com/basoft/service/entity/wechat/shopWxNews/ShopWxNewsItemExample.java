package com.basoft.service.entity.wechat.shopWxNews;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopWxNewsItemExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopWxNewsItemExample() {
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

        public Criteria andMsgIdIsNull() {
            addCriterion("MSG_ID is null");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNotNull() {
            addCriterion("MSG_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMsgIdEqualTo(Long value) {
            addCriterion("MSG_ID =", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotEqualTo(Long value) {
            addCriterion("MSG_ID <>", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThan(Long value) {
            addCriterion("MSG_ID >", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MSG_ID >=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThan(Long value) {
            addCriterion("MSG_ID <", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThanOrEqualTo(Long value) {
            addCriterion("MSG_ID <=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdIn(List<Long> values) {
            addCriterion("MSG_ID in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotIn(List<Long> values) {
            addCriterion("MSG_ID not in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdBetween(Long value1, Long value2) {
            addCriterion("MSG_ID between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotBetween(Long value1, Long value2) {
            addCriterion("MSG_ID not between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andNewsIdIsNull() {
            addCriterion("NEWS_ID is null");
            return (Criteria) this;
        }

        public Criteria andNewsIdIsNotNull() {
            addCriterion("NEWS_ID is not null");
            return (Criteria) this;
        }

        public Criteria andNewsIdEqualTo(Long value) {
            addCriterion("NEWS_ID =", value, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdNotEqualTo(Long value) {
            addCriterion("NEWS_ID <>", value, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdGreaterThan(Long value) {
            addCriterion("NEWS_ID >", value, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdGreaterThanOrEqualTo(Long value) {
            addCriterion("NEWS_ID >=", value, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdLessThan(Long value) {
            addCriterion("NEWS_ID <", value, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdLessThanOrEqualTo(Long value) {
            addCriterion("NEWS_ID <=", value, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdIn(List<Long> values) {
            addCriterion("NEWS_ID in", values, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdNotIn(List<Long> values) {
            addCriterion("NEWS_ID not in", values, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdBetween(Long value1, Long value2) {
            addCriterion("NEWS_ID between", value1, value2, "newsId");
            return (Criteria) this;
        }

        public Criteria andNewsIdNotBetween(Long value1, Long value2) {
            addCriterion("NEWS_ID not between", value1, value2, "newsId");
            return (Criteria) this;
        }

        public Criteria andMfileIdIsNull() {
            addCriterion("MFILE_ID is null");
            return (Criteria) this;
        }

        public Criteria andMfileIdIsNotNull() {
            addCriterion("MFILE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMfileIdEqualTo(Long value) {
            addCriterion("MFILE_ID =", value, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdNotEqualTo(Long value) {
            addCriterion("MFILE_ID <>", value, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdGreaterThan(Long value) {
            addCriterion("MFILE_ID >", value, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MFILE_ID >=", value, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdLessThan(Long value) {
            addCriterion("MFILE_ID <", value, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdLessThanOrEqualTo(Long value) {
            addCriterion("MFILE_ID <=", value, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdIn(List<Long> values) {
            addCriterion("MFILE_ID in", values, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdNotIn(List<Long> values) {
            addCriterion("MFILE_ID not in", values, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdBetween(Long value1, Long value2) {
            addCriterion("MFILE_ID between", value1, value2, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMfileIdNotBetween(Long value1, Long value2) {
            addCriterion("MFILE_ID not between", value1, value2, "mfileId");
            return (Criteria) this;
        }

        public Criteria andMauthorIsNull() {
            addCriterion("MAUTHOR is null");
            return (Criteria) this;
        }

        public Criteria andMauthorIsNotNull() {
            addCriterion("MAUTHOR is not null");
            return (Criteria) this;
        }

        public Criteria andMauthorEqualTo(String value) {
            addCriterion("MAUTHOR =", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorNotEqualTo(String value) {
            addCriterion("MAUTHOR <>", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorGreaterThan(String value) {
            addCriterion("MAUTHOR >", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorGreaterThanOrEqualTo(String value) {
            addCriterion("MAUTHOR >=", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorLessThan(String value) {
            addCriterion("MAUTHOR <", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorLessThanOrEqualTo(String value) {
            addCriterion("MAUTHOR <=", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorLike(String value) {
            addCriterion("MAUTHOR like", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorNotLike(String value) {
            addCriterion("MAUTHOR not like", value, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorIn(List<String> values) {
            addCriterion("MAUTHOR in", values, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorNotIn(List<String> values) {
            addCriterion("MAUTHOR not in", values, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorBetween(String value1, String value2) {
            addCriterion("MAUTHOR between", value1, value2, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMauthorNotBetween(String value1, String value2) {
            addCriterion("MAUTHOR not between", value1, value2, "mauthor");
            return (Criteria) this;
        }

        public Criteria andMtitleIsNull() {
            addCriterion("MTITLE is null");
            return (Criteria) this;
        }

        public Criteria andMtitleIsNotNull() {
            addCriterion("MTITLE is not null");
            return (Criteria) this;
        }

        public Criteria andMtitleEqualTo(String value) {
            addCriterion("MTITLE =", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleNotEqualTo(String value) {
            addCriterion("MTITLE <>", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleGreaterThan(String value) {
            addCriterion("MTITLE >", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleGreaterThanOrEqualTo(String value) {
            addCriterion("MTITLE >=", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleLessThan(String value) {
            addCriterion("MTITLE <", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleLessThanOrEqualTo(String value) {
            addCriterion("MTITLE <=", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleLike(String value) {
            addCriterion("MTITLE like", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleNotLike(String value) {
            addCriterion("MTITLE not like", value, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleIn(List<String> values) {
            addCriterion("MTITLE in", values, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleNotIn(List<String> values) {
            addCriterion("MTITLE not in", values, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleBetween(String value1, String value2) {
            addCriterion("MTITLE between", value1, value2, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMtitleNotBetween(String value1, String value2) {
            addCriterion("MTITLE not between", value1, value2, "mtitle");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicIsNull() {
            addCriterion("MSHOW_COVER_PIC is null");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicIsNotNull() {
            addCriterion("MSHOW_COVER_PIC is not null");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicEqualTo(Byte value) {
            addCriterion("MSHOW_COVER_PIC =", value, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicNotEqualTo(Byte value) {
            addCriterion("MSHOW_COVER_PIC <>", value, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicGreaterThan(Byte value) {
            addCriterion("MSHOW_COVER_PIC >", value, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicGreaterThanOrEqualTo(Byte value) {
            addCriterion("MSHOW_COVER_PIC >=", value, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicLessThan(Byte value) {
            addCriterion("MSHOW_COVER_PIC <", value, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicLessThanOrEqualTo(Byte value) {
            addCriterion("MSHOW_COVER_PIC <=", value, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicIn(List<Byte> values) {
            addCriterion("MSHOW_COVER_PIC in", values, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicNotIn(List<Byte> values) {
            addCriterion("MSHOW_COVER_PIC not in", values, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicBetween(Byte value1, Byte value2) {
            addCriterion("MSHOW_COVER_PIC between", value1, value2, "mshowCoverPic");
            return (Criteria) this;
        }

        public Criteria andMshowCoverPicNotBetween(Byte value1, Byte value2) {
            addCriterion("MSHOW_COVER_PIC not between", value1, value2, "mshowCoverPic");
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

        public Criteria andReadCntIsNull() {
            addCriterion("READ_CNT is null");
            return (Criteria) this;
        }

        public Criteria andReadCntIsNotNull() {
            addCriterion("READ_CNT is not null");
            return (Criteria) this;
        }

        public Criteria andReadCntEqualTo(Integer value) {
            addCriterion("READ_CNT =", value, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntNotEqualTo(Integer value) {
            addCriterion("READ_CNT <>", value, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntGreaterThan(Integer value) {
            addCriterion("READ_CNT >", value, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntGreaterThanOrEqualTo(Integer value) {
            addCriterion("READ_CNT >=", value, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntLessThan(Integer value) {
            addCriterion("READ_CNT <", value, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntLessThanOrEqualTo(Integer value) {
            addCriterion("READ_CNT <=", value, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntIn(List<Integer> values) {
            addCriterion("READ_CNT in", values, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntNotIn(List<Integer> values) {
            addCriterion("READ_CNT not in", values, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntBetween(Integer value1, Integer value2) {
            addCriterion("READ_CNT between", value1, value2, "readCnt");
            return (Criteria) this;
        }

        public Criteria andReadCntNotBetween(Integer value1, Integer value2) {
            addCriterion("READ_CNT not between", value1, value2, "readCnt");
            return (Criteria) this;
        }

        public Criteria andListImgIsNull() {
            addCriterion("LIST_IMG is null");
            return (Criteria) this;
        }

        public Criteria andListImgIsNotNull() {
            addCriterion("LIST_IMG is not null");
            return (Criteria) this;
        }

        public Criteria andListImgEqualTo(Integer value) {
            addCriterion("LIST_IMG =", value, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgNotEqualTo(Integer value) {
            addCriterion("LIST_IMG <>", value, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgGreaterThan(Integer value) {
            addCriterion("LIST_IMG >", value, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgGreaterThanOrEqualTo(Integer value) {
            addCriterion("LIST_IMG >=", value, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgLessThan(Integer value) {
            addCriterion("LIST_IMG <", value, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgLessThanOrEqualTo(Integer value) {
            addCriterion("LIST_IMG <=", value, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgIn(List<Integer> values) {
            addCriterion("LIST_IMG in", values, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgNotIn(List<Integer> values) {
            addCriterion("LIST_IMG not in", values, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgBetween(Integer value1, Integer value2) {
            addCriterion("LIST_IMG between", value1, value2, "listImg");
            return (Criteria) this;
        }

        public Criteria andListImgNotBetween(Integer value1, Integer value2) {
            addCriterion("LIST_IMG not between", value1, value2, "listImg");
            return (Criteria) this;
        }

        public Criteria andNewsTypeIsNull() {
            addCriterion("NEWS_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andNewsTypeIsNotNull() {
            addCriterion("NEWS_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andNewsTypeEqualTo(Integer value) {
            addCriterion("NEWS_TYPE =", value, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeNotEqualTo(Integer value) {
            addCriterion("NEWS_TYPE <>", value, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeGreaterThan(Integer value) {
            addCriterion("NEWS_TYPE >", value, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("NEWS_TYPE >=", value, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeLessThan(Integer value) {
            addCriterion("NEWS_TYPE <", value, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeLessThanOrEqualTo(Integer value) {
            addCriterion("NEWS_TYPE <=", value, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeIn(List<Integer> values) {
            addCriterion("NEWS_TYPE in", values, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeNotIn(List<Integer> values) {
            addCriterion("NEWS_TYPE not in", values, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeBetween(Integer value1, Integer value2) {
            addCriterion("NEWS_TYPE between", value1, value2, "newsType");
            return (Criteria) this;
        }

        public Criteria andNewsTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("NEWS_TYPE not between", value1, value2, "newsType");
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