package com.trackray.rest.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class VulnDTOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VulnDTOExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andTaskMd5IsNull() {
            addCriterion("task_md5 is null");
            return (Criteria) this;
        }

        public Criteria andTaskMd5IsNotNull() {
            addCriterion("task_md5 is not null");
            return (Criteria) this;
        }

        public Criteria andTaskMd5EqualTo(String value) {
            addCriterion("task_md5 =", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5NotEqualTo(String value) {
            addCriterion("task_md5 <>", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5GreaterThan(String value) {
            addCriterion("task_md5 >", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5GreaterThanOrEqualTo(String value) {
            addCriterion("task_md5 >=", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5LessThan(String value) {
            addCriterion("task_md5 <", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5LessThanOrEqualTo(String value) {
            addCriterion("task_md5 <=", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5Like(String value) {
            addCriterion("task_md5 like", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5NotLike(String value) {
            addCriterion("task_md5 not like", value, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5In(List<String> values) {
            addCriterion("task_md5 in", values, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5NotIn(List<String> values) {
            addCriterion("task_md5 not in", values, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5Between(String value1, String value2) {
            addCriterion("task_md5 between", value1, value2, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andTaskMd5NotBetween(String value1, String value2) {
            addCriterion("task_md5 not between", value1, value2, "taskMd5");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Integer value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andPayloadIsNull() {
            addCriterion("payload is null");
            return (Criteria) this;
        }

        public Criteria andPayloadIsNotNull() {
            addCriterion("payload is not null");
            return (Criteria) this;
        }

        public Criteria andPayloadEqualTo(String value) {
            addCriterion("payload =", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadNotEqualTo(String value) {
            addCriterion("payload <>", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadGreaterThan(String value) {
            addCriterion("payload >", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadGreaterThanOrEqualTo(String value) {
            addCriterion("payload >=", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadLessThan(String value) {
            addCriterion("payload <", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadLessThanOrEqualTo(String value) {
            addCriterion("payload <=", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadLike(String value) {
            addCriterion("payload like", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadNotLike(String value) {
            addCriterion("payload not like", value, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadIn(List<String> values) {
            addCriterion("payload in", values, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadNotIn(List<String> values) {
            addCriterion("payload not in", values, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadBetween(String value1, String value2) {
            addCriterion("payload between", value1, value2, "payload");
            return (Criteria) this;
        }

        public Criteria andPayloadNotBetween(String value1, String value2) {
            addCriterion("payload not between", value1, value2, "payload");
            return (Criteria) this;
        }

        public Criteria andMessageIsNull() {
            addCriterion("message is null");
            return (Criteria) this;
        }

        public Criteria andMessageIsNotNull() {
            addCriterion("message is not null");
            return (Criteria) this;
        }

        public Criteria andMessageEqualTo(String value) {
            addCriterion("message =", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotEqualTo(String value) {
            addCriterion("message <>", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageGreaterThan(String value) {
            addCriterion("message >", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageGreaterThanOrEqualTo(String value) {
            addCriterion("message >=", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageLessThan(String value) {
            addCriterion("message <", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageLessThanOrEqualTo(String value) {
            addCriterion("message <=", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageLike(String value) {
            addCriterion("message like", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotLike(String value) {
            addCriterion("message not like", value, "message");
            return (Criteria) this;
        }

        public Criteria andMessageIn(List<String> values) {
            addCriterion("message in", values, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotIn(List<String> values) {
            addCriterion("message not in", values, "message");
            return (Criteria) this;
        }

        public Criteria andMessageBetween(String value1, String value2) {
            addCriterion("message between", value1, value2, "message");
            return (Criteria) this;
        }

        public Criteria andMessageNotBetween(String value1, String value2) {
            addCriterion("message not between", value1, value2, "message");
            return (Criteria) this;
        }

        public Criteria andVulnIdIsNull() {
            addCriterion("vuln_id is null");
            return (Criteria) this;
        }

        public Criteria andVulnIdIsNotNull() {
            addCriterion("vuln_id is not null");
            return (Criteria) this;
        }

        public Criteria andVulnIdEqualTo(String value) {
            addCriterion("vuln_id =", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdNotEqualTo(String value) {
            addCriterion("vuln_id <>", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdGreaterThan(String value) {
            addCriterion("vuln_id >", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdGreaterThanOrEqualTo(String value) {
            addCriterion("vuln_id >=", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdLessThan(String value) {
            addCriterion("vuln_id <", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdLessThanOrEqualTo(String value) {
            addCriterion("vuln_id <=", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdLike(String value) {
            addCriterion("vuln_id like", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdNotLike(String value) {
            addCriterion("vuln_id not like", value, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdIn(List<String> values) {
            addCriterion("vuln_id in", values, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdNotIn(List<String> values) {
            addCriterion("vuln_id not in", values, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdBetween(String value1, String value2) {
            addCriterion("vuln_id between", value1, value2, "vulnId");
            return (Criteria) this;
        }

        public Criteria andVulnIdNotBetween(String value1, String value2) {
            addCriterion("vuln_id not between", value1, value2, "vulnId");
            return (Criteria) this;
        }

        public Criteria andAboutLinkIsNull() {
            addCriterion("about_link is null");
            return (Criteria) this;
        }

        public Criteria andAboutLinkIsNotNull() {
            addCriterion("about_link is not null");
            return (Criteria) this;
        }

        public Criteria andAboutLinkEqualTo(String value) {
            addCriterion("about_link =", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkNotEqualTo(String value) {
            addCriterion("about_link <>", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkGreaterThan(String value) {
            addCriterion("about_link >", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkGreaterThanOrEqualTo(String value) {
            addCriterion("about_link >=", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkLessThan(String value) {
            addCriterion("about_link <", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkLessThanOrEqualTo(String value) {
            addCriterion("about_link <=", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkLike(String value) {
            addCriterion("about_link like", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkNotLike(String value) {
            addCriterion("about_link not like", value, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkIn(List<String> values) {
            addCriterion("about_link in", values, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkNotIn(List<String> values) {
            addCriterion("about_link not in", values, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkBetween(String value1, String value2) {
            addCriterion("about_link between", value1, value2, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andAboutLinkNotBetween(String value1, String value2) {
            addCriterion("about_link not between", value1, value2, "aboutLink");
            return (Criteria) this;
        }

        public Criteria andVulnTypeIsNull() {
            addCriterion("vuln_type is null");
            return (Criteria) this;
        }

        public Criteria andVulnTypeIsNotNull() {
            addCriterion("vuln_type is not null");
            return (Criteria) this;
        }

        public Criteria andVulnTypeEqualTo(Integer value) {
            addCriterion("vuln_type =", value, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeNotEqualTo(Integer value) {
            addCriterion("vuln_type <>", value, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeGreaterThan(Integer value) {
            addCriterion("vuln_type >", value, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("vuln_type >=", value, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeLessThan(Integer value) {
            addCriterion("vuln_type <", value, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeLessThanOrEqualTo(Integer value) {
            addCriterion("vuln_type <=", value, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeIn(List<Integer> values) {
            addCriterion("vuln_type in", values, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeNotIn(List<Integer> values) {
            addCriterion("vuln_type not in", values, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeBetween(Integer value1, Integer value2) {
            addCriterion("vuln_type between", value1, value2, "vulnType");
            return (Criteria) this;
        }

        public Criteria andVulnTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("vuln_type not between", value1, value2, "vulnType");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterionForJDBCDate("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterionForJDBCDate("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterionForJDBCDate("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("create_time not between", value1, value2, "createTime");
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