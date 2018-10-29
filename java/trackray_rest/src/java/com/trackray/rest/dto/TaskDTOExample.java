package com.trackray.rest.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TaskDTOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TaskDTOExample() {
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

        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
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

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(String value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(String value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(String value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(String value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(String value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(String value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLike(String value) {
            addCriterion("uid like", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotLike(String value) {
            addCriterion("uid not like", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<String> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<String> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(String value1, String value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(String value1, String value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNull() {
            addCriterion("task_name is null");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNotNull() {
            addCriterion("task_name is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNameEqualTo(String value) {
            addCriterion("task_name =", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotEqualTo(String value) {
            addCriterion("task_name <>", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThan(String value) {
            addCriterion("task_name >", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThanOrEqualTo(String value) {
            addCriterion("task_name >=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThan(String value) {
            addCriterion("task_name <", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThanOrEqualTo(String value) {
            addCriterion("task_name <=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLike(String value) {
            addCriterion("task_name like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotLike(String value) {
            addCriterion("task_name not like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameIn(List<String> values) {
            addCriterion("task_name in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotIn(List<String> values) {
            addCriterion("task_name not in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameBetween(String value1, String value2) {
            addCriterion("task_name between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotBetween(String value1, String value2) {
            addCriterion("task_name not between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTargetIsNull() {
            addCriterion("target is null");
            return (Criteria) this;
        }

        public Criteria andTargetIsNotNull() {
            addCriterion("target is not null");
            return (Criteria) this;
        }

        public Criteria andTargetEqualTo(String value) {
            addCriterion("target =", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotEqualTo(String value) {
            addCriterion("target <>", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetGreaterThan(String value) {
            addCriterion("target >", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetGreaterThanOrEqualTo(String value) {
            addCriterion("target >=", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetLessThan(String value) {
            addCriterion("target <", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetLessThanOrEqualTo(String value) {
            addCriterion("target <=", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetLike(String value) {
            addCriterion("target like", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotLike(String value) {
            addCriterion("target not like", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetIn(List<String> values) {
            addCriterion("target in", values, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotIn(List<String> values) {
            addCriterion("target not in", values, "target");
            return (Criteria) this;
        }

        public Criteria andTargetBetween(String value1, String value2) {
            addCriterion("target between", value1, value2, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotBetween(String value1, String value2) {
            addCriterion("target not between", value1, value2, "target");
            return (Criteria) this;
        }

        public Criteria andThreadIsNull() {
            addCriterion("thread is null");
            return (Criteria) this;
        }

        public Criteria andThreadIsNotNull() {
            addCriterion("thread is not null");
            return (Criteria) this;
        }

        public Criteria andThreadEqualTo(Integer value) {
            addCriterion("thread =", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadNotEqualTo(Integer value) {
            addCriterion("thread <>", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadGreaterThan(Integer value) {
            addCriterion("thread >", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadGreaterThanOrEqualTo(Integer value) {
            addCriterion("thread >=", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadLessThan(Integer value) {
            addCriterion("thread <", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadLessThanOrEqualTo(Integer value) {
            addCriterion("thread <=", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadIn(List<Integer> values) {
            addCriterion("thread in", values, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadNotIn(List<Integer> values) {
            addCriterion("thread not in", values, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadBetween(Integer value1, Integer value2) {
            addCriterion("thread between", value1, value2, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadNotBetween(Integer value1, Integer value2) {
            addCriterion("thread not between", value1, value2, "thread");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxIsNull() {
            addCriterion("spider_max is null");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxIsNotNull() {
            addCriterion("spider_max is not null");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxEqualTo(Integer value) {
            addCriterion("spider_max =", value, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxNotEqualTo(Integer value) {
            addCriterion("spider_max <>", value, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxGreaterThan(Integer value) {
            addCriterion("spider_max >", value, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxGreaterThanOrEqualTo(Integer value) {
            addCriterion("spider_max >=", value, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxLessThan(Integer value) {
            addCriterion("spider_max <", value, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxLessThanOrEqualTo(Integer value) {
            addCriterion("spider_max <=", value, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxIn(List<Integer> values) {
            addCriterion("spider_max in", values, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxNotIn(List<Integer> values) {
            addCriterion("spider_max not in", values, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxBetween(Integer value1, Integer value2) {
            addCriterion("spider_max between", value1, value2, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderMaxNotBetween(Integer value1, Integer value2) {
            addCriterion("spider_max not between", value1, value2, "spiderMax");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepIsNull() {
            addCriterion("spider_deep is null");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepIsNotNull() {
            addCriterion("spider_deep is not null");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepEqualTo(Integer value) {
            addCriterion("spider_deep =", value, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepNotEqualTo(Integer value) {
            addCriterion("spider_deep <>", value, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepGreaterThan(Integer value) {
            addCriterion("spider_deep >", value, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepGreaterThanOrEqualTo(Integer value) {
            addCriterion("spider_deep >=", value, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepLessThan(Integer value) {
            addCriterion("spider_deep <", value, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepLessThanOrEqualTo(Integer value) {
            addCriterion("spider_deep <=", value, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepIn(List<Integer> values) {
            addCriterion("spider_deep in", values, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepNotIn(List<Integer> values) {
            addCriterion("spider_deep not in", values, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepBetween(Integer value1, Integer value2) {
            addCriterion("spider_deep between", value1, value2, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andSpiderDeepNotBetween(Integer value1, Integer value2) {
            addCriterion("spider_deep not between", value1, value2, "spiderDeep");
            return (Criteria) this;
        }

        public Criteria andTimeMaxIsNull() {
            addCriterion("time_max is null");
            return (Criteria) this;
        }

        public Criteria andTimeMaxIsNotNull() {
            addCriterion("time_max is not null");
            return (Criteria) this;
        }

        public Criteria andTimeMaxEqualTo(Integer value) {
            addCriterion("time_max =", value, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxNotEqualTo(Integer value) {
            addCriterion("time_max <>", value, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxGreaterThan(Integer value) {
            addCriterion("time_max >", value, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_max >=", value, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxLessThan(Integer value) {
            addCriterion("time_max <", value, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxLessThanOrEqualTo(Integer value) {
            addCriterion("time_max <=", value, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxIn(List<Integer> values) {
            addCriterion("time_max in", values, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxNotIn(List<Integer> values) {
            addCriterion("time_max not in", values, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxBetween(Integer value1, Integer value2) {
            addCriterion("time_max between", value1, value2, "timeMax");
            return (Criteria) this;
        }

        public Criteria andTimeMaxNotBetween(Integer value1, Integer value2) {
            addCriterion("time_max not between", value1, value2, "timeMax");
            return (Criteria) this;
        }

        public Criteria andCookieIsNull() {
            addCriterion("cookie is null");
            return (Criteria) this;
        }

        public Criteria andCookieIsNotNull() {
            addCriterion("cookie is not null");
            return (Criteria) this;
        }

        public Criteria andCookieEqualTo(String value) {
            addCriterion("cookie =", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieNotEqualTo(String value) {
            addCriterion("cookie <>", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieGreaterThan(String value) {
            addCriterion("cookie >", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieGreaterThanOrEqualTo(String value) {
            addCriterion("cookie >=", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieLessThan(String value) {
            addCriterion("cookie <", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieLessThanOrEqualTo(String value) {
            addCriterion("cookie <=", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieLike(String value) {
            addCriterion("cookie like", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieNotLike(String value) {
            addCriterion("cookie not like", value, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieIn(List<String> values) {
            addCriterion("cookie in", values, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieNotIn(List<String> values) {
            addCriterion("cookie not in", values, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieBetween(String value1, String value2) {
            addCriterion("cookie between", value1, value2, "cookie");
            return (Criteria) this;
        }

        public Criteria andCookieNotBetween(String value1, String value2) {
            addCriterion("cookie not between", value1, value2, "cookie");
            return (Criteria) this;
        }

        public Criteria andRuleIsNull() {
            addCriterion("rule is null");
            return (Criteria) this;
        }

        public Criteria andRuleIsNotNull() {
            addCriterion("rule is not null");
            return (Criteria) this;
        }

        public Criteria andRuleEqualTo(String value) {
            addCriterion("rule =", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotEqualTo(String value) {
            addCriterion("rule <>", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleGreaterThan(String value) {
            addCriterion("rule >", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleGreaterThanOrEqualTo(String value) {
            addCriterion("rule >=", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleLessThan(String value) {
            addCriterion("rule <", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleLessThanOrEqualTo(String value) {
            addCriterion("rule <=", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleLike(String value) {
            addCriterion("rule like", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotLike(String value) {
            addCriterion("rule not like", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleIn(List<String> values) {
            addCriterion("rule in", values, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotIn(List<String> values) {
            addCriterion("rule not in", values, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleBetween(String value1, String value2) {
            addCriterion("rule between", value1, value2, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotBetween(String value1, String value2) {
            addCriterion("rule not between", value1, value2, "rule");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
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
            addCriterionForJDBCTime("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterionForJDBCTime("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterionForJDBCTime("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterionForJDBCTime("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterionForJDBCTime("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterionForJDBCTime("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("update_time not between", value1, value2, "updateTime");
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