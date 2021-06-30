package com.basoft.service.entity.wechat.shopMenu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopWxMenuExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ShopWxMenuExample() {
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

        public Criteria andMenuIdIsNull() {
            addCriterion("MENU_ID is null");
            return (Criteria) this;
        }

        public Criteria andMenuIdIsNotNull() {
            addCriterion("MENU_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMenuIdEqualTo(Long value) {
            addCriterion("MENU_ID =", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdNotEqualTo(Long value) {
            addCriterion("MENU_ID <>", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdGreaterThan(Long value) {
            addCriterion("MENU_ID >", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MENU_ID >=", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdLessThan(Long value) {
            addCriterion("MENU_ID <", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdLessThanOrEqualTo(Long value) {
            addCriterion("MENU_ID <=", value, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdIn(List<Long> values) {
            addCriterion("MENU_ID in", values, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdNotIn(List<Long> values) {
            addCriterion("MENU_ID not in", values, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdBetween(Long value1, Long value2) {
            addCriterion("MENU_ID between", value1, value2, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuIdNotBetween(Long value1, Long value2) {
            addCriterion("MENU_ID not between", value1, value2, "menuId");
            return (Criteria) this;
        }

        public Criteria andMenuNmIsNull() {
            addCriterion("MENU_NM is null");
            return (Criteria) this;
        }

        public Criteria andMenuNmIsNotNull() {
            addCriterion("MENU_NM is not null");
            return (Criteria) this;
        }

        public Criteria andMenuNmEqualTo(String value) {
            addCriterion("MENU_NM =", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmNotEqualTo(String value) {
            addCriterion("MENU_NM <>", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmGreaterThan(String value) {
            addCriterion("MENU_NM >", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmGreaterThanOrEqualTo(String value) {
            addCriterion("MENU_NM >=", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmLessThan(String value) {
            addCriterion("MENU_NM <", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmLessThanOrEqualTo(String value) {
            addCriterion("MENU_NM <=", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmLike(String value) {
            addCriterion("MENU_NM like", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmNotLike(String value) {
            addCriterion("MENU_NM not like", value, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmIn(List<String> values) {
            addCriterion("MENU_NM in", values, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmNotIn(List<String> values) {
            addCriterion("MENU_NM not in", values, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmBetween(String value1, String value2) {
            addCriterion("MENU_NM between", value1, value2, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuNmNotBetween(String value1, String value2) {
            addCriterion("MENU_NM not between", value1, value2, "menuNm");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdIsNull() {
            addCriterion("MENU_PARENT_ID is null");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdIsNotNull() {
            addCriterion("MENU_PARENT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdEqualTo(Long value) {
            addCriterion("MENU_PARENT_ID =", value, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdNotEqualTo(Long value) {
            addCriterion("MENU_PARENT_ID <>", value, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdGreaterThan(Long value) {
            addCriterion("MENU_PARENT_ID >", value, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MENU_PARENT_ID >=", value, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdLessThan(Long value) {
            addCriterion("MENU_PARENT_ID <", value, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdLessThanOrEqualTo(Long value) {
            addCriterion("MENU_PARENT_ID <=", value, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdIn(List<Long> values) {
            addCriterion("MENU_PARENT_ID in", values, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdNotIn(List<Long> values) {
            addCriterion("MENU_PARENT_ID not in", values, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdBetween(Long value1, Long value2) {
            addCriterion("MENU_PARENT_ID between", value1, value2, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuParentIdNotBetween(Long value1, Long value2) {
            addCriterion("MENU_PARENT_ID not between", value1, value2, "menuParentId");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeIsNull() {
            addCriterion("MENU_OP_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeIsNotNull() {
            addCriterion("MENU_OP_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeEqualTo(String value) {
            addCriterion("MENU_OP_TYPE =", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeNotEqualTo(String value) {
            addCriterion("MENU_OP_TYPE <>", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeGreaterThan(String value) {
            addCriterion("MENU_OP_TYPE >", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeGreaterThanOrEqualTo(String value) {
            addCriterion("MENU_OP_TYPE >=", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeLessThan(String value) {
            addCriterion("MENU_OP_TYPE <", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeLessThanOrEqualTo(String value) {
            addCriterion("MENU_OP_TYPE <=", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeLike(String value) {
            addCriterion("MENU_OP_TYPE like", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeNotLike(String value) {
            addCriterion("MENU_OP_TYPE not like", value, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeIn(List<String> values) {
            addCriterion("MENU_OP_TYPE in", values, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeNotIn(List<String> values) {
            addCriterion("MENU_OP_TYPE not in", values, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeBetween(String value1, String value2) {
            addCriterion("MENU_OP_TYPE between", value1, value2, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuOpTypeNotBetween(String value1, String value2) {
            addCriterion("MENU_OP_TYPE not between", value1, value2, "menuOpType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeIsNull() {
            addCriterion("MENU_MSG_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeIsNotNull() {
            addCriterion("MENU_MSG_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeEqualTo(Integer value) {
            addCriterion("MENU_MSG_TYPE =", value, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeNotEqualTo(Integer value) {
            addCriterion("MENU_MSG_TYPE <>", value, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeGreaterThan(Integer value) {
            addCriterion("MENU_MSG_TYPE >", value, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("MENU_MSG_TYPE >=", value, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeLessThan(Integer value) {
            addCriterion("MENU_MSG_TYPE <", value, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeLessThanOrEqualTo(Integer value) {
            addCriterion("MENU_MSG_TYPE <=", value, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeIn(List<Integer> values) {
            addCriterion("MENU_MSG_TYPE in", values, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeNotIn(List<Integer> values) {
            addCriterion("MENU_MSG_TYPE not in", values, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeBetween(Integer value1, Integer value2) {
            addCriterion("MENU_MSG_TYPE between", value1, value2, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuMsgTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("MENU_MSG_TYPE not between", value1, value2, "menuMsgType");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdIsNull() {
            addCriterion("MENU_OP_ID is null");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdIsNotNull() {
            addCriterion("MENU_OP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdEqualTo(Long value) {
            addCriterion("MENU_OP_ID =", value, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdNotEqualTo(Long value) {
            addCriterion("MENU_OP_ID <>", value, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdGreaterThan(Long value) {
            addCriterion("MENU_OP_ID >", value, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MENU_OP_ID >=", value, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdLessThan(Long value) {
            addCriterion("MENU_OP_ID <", value, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdLessThanOrEqualTo(Long value) {
            addCriterion("MENU_OP_ID <=", value, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdIn(List<Long> values) {
            addCriterion("MENU_OP_ID in", values, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdNotIn(List<Long> values) {
            addCriterion("MENU_OP_ID not in", values, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdBetween(Long value1, Long value2) {
            addCriterion("MENU_OP_ID between", value1, value2, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpIdNotBetween(Long value1, Long value2) {
            addCriterion("MENU_OP_ID not between", value1, value2, "menuOpId");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleIsNull() {
            addCriterion("MENU_OP_TITLE is null");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleIsNotNull() {
            addCriterion("MENU_OP_TITLE is not null");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleEqualTo(String value) {
            addCriterion("MENU_OP_TITLE =", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleNotEqualTo(String value) {
            addCriterion("MENU_OP_TITLE <>", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleGreaterThan(String value) {
            addCriterion("MENU_OP_TITLE >", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleGreaterThanOrEqualTo(String value) {
            addCriterion("MENU_OP_TITLE >=", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleLessThan(String value) {
            addCriterion("MENU_OP_TITLE <", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleLessThanOrEqualTo(String value) {
            addCriterion("MENU_OP_TITLE <=", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleLike(String value) {
            addCriterion("MENU_OP_TITLE like", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleNotLike(String value) {
            addCriterion("MENU_OP_TITLE not like", value, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleIn(List<String> values) {
            addCriterion("MENU_OP_TITLE in", values, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleNotIn(List<String> values) {
            addCriterion("MENU_OP_TITLE not in", values, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleBetween(String value1, String value2) {
            addCriterion("MENU_OP_TITLE between", value1, value2, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuOpTitleNotBetween(String value1, String value2) {
            addCriterion("MENU_OP_TITLE not between", value1, value2, "menuOpTitle");
            return (Criteria) this;
        }

        public Criteria andMenuStsIsNull() {
            addCriterion("MENU_STS is null");
            return (Criteria) this;
        }

        public Criteria andMenuStsIsNotNull() {
            addCriterion("MENU_STS is not null");
            return (Criteria) this;
        }

        public Criteria andMenuStsEqualTo(Byte value) {
            addCriterion("MENU_STS =", value, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsNotEqualTo(Byte value) {
            addCriterion("MENU_STS <>", value, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsGreaterThan(Byte value) {
            addCriterion("MENU_STS >", value, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsGreaterThanOrEqualTo(Byte value) {
            addCriterion("MENU_STS >=", value, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsLessThan(Byte value) {
            addCriterion("MENU_STS <", value, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsLessThanOrEqualTo(Byte value) {
            addCriterion("MENU_STS <=", value, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsIn(List<Byte> values) {
            addCriterion("MENU_STS in", values, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsNotIn(List<Byte> values) {
            addCriterion("MENU_STS not in", values, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsBetween(Byte value1, Byte value2) {
            addCriterion("MENU_STS between", value1, value2, "menuSts");
            return (Criteria) this;
        }

        public Criteria andMenuStsNotBetween(Byte value1, Byte value2) {
            addCriterion("MENU_STS not between", value1, value2, "menuSts");
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

        public Criteria andCreateIdIsNull() {
            addCriterion("CREATE_ID is null");
            return (Criteria) this;
        }

        public Criteria andCreateIdIsNotNull() {
            addCriterion("CREATE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCreateIdEqualTo(String value) {
            addCriterion("CREATE_ID =", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotEqualTo(String value) {
            addCriterion("CREATE_ID <>", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThan(String value) {
            addCriterion("CREATE_ID >", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_ID >=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThan(String value) {
            addCriterion("CREATE_ID <", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThanOrEqualTo(String value) {
            addCriterion("CREATE_ID <=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLike(String value) {
            addCriterion("CREATE_ID like", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotLike(String value) {
            addCriterion("CREATE_ID not like", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdIn(List<String> values) {
            addCriterion("CREATE_ID in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotIn(List<String> values) {
            addCriterion("CREATE_ID not in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdBetween(String value1, String value2) {
            addCriterion("CREATE_ID between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotBetween(String value1, String value2) {
            addCriterion("CREATE_ID not between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateDtIsNull() {
            addCriterion("CREATE_DT is null");
            return (Criteria) this;
        }

        public Criteria andCreateDtIsNotNull() {
            addCriterion("CREATE_DT is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDtEqualTo(Date value) {
            addCriterion("CREATE_DT =", value, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtNotEqualTo(Date value) {
            addCriterion("CREATE_DT <>", value, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtGreaterThan(Date value) {
            addCriterion("CREATE_DT >", value, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATE_DT >=", value, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtLessThan(Date value) {
            addCriterion("CREATE_DT <", value, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtLessThanOrEqualTo(Date value) {
            addCriterion("CREATE_DT <=", value, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtIn(List<Date> values) {
            addCriterion("CREATE_DT in", values, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtNotIn(List<Date> values) {
            addCriterion("CREATE_DT not in", values, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtBetween(Date value1, Date value2) {
            addCriterion("CREATE_DT between", value1, value2, "createDt");
            return (Criteria) this;
        }

        public Criteria andCreateDtNotBetween(Date value1, Date value2) {
            addCriterion("CREATE_DT not between", value1, value2, "createDt");
            return (Criteria) this;
        }

        public Criteria andModifyIdIsNull() {
            addCriterion("MODIFY_ID is null");
            return (Criteria) this;
        }

        public Criteria andModifyIdIsNotNull() {
            addCriterion("MODIFY_ID is not null");
            return (Criteria) this;
        }

        public Criteria andModifyIdEqualTo(String value) {
            addCriterion("MODIFY_ID =", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotEqualTo(String value) {
            addCriterion("MODIFY_ID <>", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdGreaterThan(String value) {
            addCriterion("MODIFY_ID >", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdGreaterThanOrEqualTo(String value) {
            addCriterion("MODIFY_ID >=", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLessThan(String value) {
            addCriterion("MODIFY_ID <", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLessThanOrEqualTo(String value) {
            addCriterion("MODIFY_ID <=", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLike(String value) {
            addCriterion("MODIFY_ID like", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotLike(String value) {
            addCriterion("MODIFY_ID not like", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdIn(List<String> values) {
            addCriterion("MODIFY_ID in", values, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotIn(List<String> values) {
            addCriterion("MODIFY_ID not in", values, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdBetween(String value1, String value2) {
            addCriterion("MODIFY_ID between", value1, value2, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotBetween(String value1, String value2) {
            addCriterion("MODIFY_ID not between", value1, value2, "modifyId");
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