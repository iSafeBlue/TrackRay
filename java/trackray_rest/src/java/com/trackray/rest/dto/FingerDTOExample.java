package com.trackray.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class FingerDTOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FingerDTOExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andEnumKeyIsNull() {
            addCriterion("enum_key is null");
            return (Criteria) this;
        }

        public Criteria andEnumKeyIsNotNull() {
            addCriterion("enum_key is not null");
            return (Criteria) this;
        }

        public Criteria andEnumKeyEqualTo(String value) {
            addCriterion("enum_key =", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyNotEqualTo(String value) {
            addCriterion("enum_key <>", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyGreaterThan(String value) {
            addCriterion("enum_key >", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyGreaterThanOrEqualTo(String value) {
            addCriterion("enum_key >=", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyLessThan(String value) {
            addCriterion("enum_key <", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyLessThanOrEqualTo(String value) {
            addCriterion("enum_key <=", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyLike(String value) {
            addCriterion("enum_key like", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyNotLike(String value) {
            addCriterion("enum_key not like", value, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyIn(List<String> values) {
            addCriterion("enum_key in", values, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyNotIn(List<String> values) {
            addCriterion("enum_key not in", values, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyBetween(String value1, String value2) {
            addCriterion("enum_key between", value1, value2, "enumKey");
            return (Criteria) this;
        }

        public Criteria andEnumKeyNotBetween(String value1, String value2) {
            addCriterion("enum_key not between", value1, value2, "enumKey");
            return (Criteria) this;
        }

        public Criteria andFingerNameIsNull() {
            addCriterion("finger_name is null");
            return (Criteria) this;
        }

        public Criteria andFingerNameIsNotNull() {
            addCriterion("finger_name is not null");
            return (Criteria) this;
        }

        public Criteria andFingerNameEqualTo(String value) {
            addCriterion("finger_name =", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameNotEqualTo(String value) {
            addCriterion("finger_name <>", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameGreaterThan(String value) {
            addCriterion("finger_name >", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameGreaterThanOrEqualTo(String value) {
            addCriterion("finger_name >=", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameLessThan(String value) {
            addCriterion("finger_name <", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameLessThanOrEqualTo(String value) {
            addCriterion("finger_name <=", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameLike(String value) {
            addCriterion("finger_name like", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameNotLike(String value) {
            addCriterion("finger_name not like", value, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameIn(List<String> values) {
            addCriterion("finger_name in", values, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameNotIn(List<String> values) {
            addCriterion("finger_name not in", values, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameBetween(String value1, String value2) {
            addCriterion("finger_name between", value1, value2, "fingerName");
            return (Criteria) this;
        }

        public Criteria andFingerNameNotBetween(String value1, String value2) {
            addCriterion("finger_name not between", value1, value2, "fingerName");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andRegexIsNull() {
            addCriterion("regex is null");
            return (Criteria) this;
        }

        public Criteria andRegexIsNotNull() {
            addCriterion("regex is not null");
            return (Criteria) this;
        }

        public Criteria andRegexEqualTo(String value) {
            addCriterion("regex =", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexNotEqualTo(String value) {
            addCriterion("regex <>", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexGreaterThan(String value) {
            addCriterion("regex >", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexGreaterThanOrEqualTo(String value) {
            addCriterion("regex >=", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexLessThan(String value) {
            addCriterion("regex <", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexLessThanOrEqualTo(String value) {
            addCriterion("regex <=", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexLike(String value) {
            addCriterion("regex like", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexNotLike(String value) {
            addCriterion("regex not like", value, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexIn(List<String> values) {
            addCriterion("regex in", values, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexNotIn(List<String> values) {
            addCriterion("regex not in", values, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexBetween(String value1, String value2) {
            addCriterion("regex between", value1, value2, "regex");
            return (Criteria) this;
        }

        public Criteria andRegexNotBetween(String value1, String value2) {
            addCriterion("regex not between", value1, value2, "regex");
            return (Criteria) this;
        }

        public Criteria andMd5IsNull() {
            addCriterion("md5 is null");
            return (Criteria) this;
        }

        public Criteria andMd5IsNotNull() {
            addCriterion("md5 is not null");
            return (Criteria) this;
        }

        public Criteria andMd5EqualTo(String value) {
            addCriterion("md5 =", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5NotEqualTo(String value) {
            addCriterion("md5 <>", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5GreaterThan(String value) {
            addCriterion("md5 >", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5GreaterThanOrEqualTo(String value) {
            addCriterion("md5 >=", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5LessThan(String value) {
            addCriterion("md5 <", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5LessThanOrEqualTo(String value) {
            addCriterion("md5 <=", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5Like(String value) {
            addCriterion("md5 like", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5NotLike(String value) {
            addCriterion("md5 not like", value, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5In(List<String> values) {
            addCriterion("md5 in", values, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5NotIn(List<String> values) {
            addCriterion("md5 not in", values, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5Between(String value1, String value2) {
            addCriterion("md5 between", value1, value2, "md5");
            return (Criteria) this;
        }

        public Criteria andMd5NotBetween(String value1, String value2) {
            addCriterion("md5 not between", value1, value2, "md5");
            return (Criteria) this;
        }

        public Criteria andFingerTypeIsNull() {
            addCriterion("finger_type is null");
            return (Criteria) this;
        }

        public Criteria andFingerTypeIsNotNull() {
            addCriterion("finger_type is not null");
            return (Criteria) this;
        }

        public Criteria andFingerTypeEqualTo(Integer value) {
            addCriterion("finger_type =", value, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeNotEqualTo(Integer value) {
            addCriterion("finger_type <>", value, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeGreaterThan(Integer value) {
            addCriterion("finger_type >", value, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("finger_type >=", value, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeLessThan(Integer value) {
            addCriterion("finger_type <", value, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeLessThanOrEqualTo(Integer value) {
            addCriterion("finger_type <=", value, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeIn(List<Integer> values) {
            addCriterion("finger_type in", values, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeNotIn(List<Integer> values) {
            addCriterion("finger_type not in", values, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeBetween(Integer value1, Integer value2) {
            addCriterion("finger_type between", value1, value2, "fingerType");
            return (Criteria) this;
        }

        public Criteria andFingerTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("finger_type not between", value1, value2, "fingerType");
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