package com.sang.nv.education.exam.infrastructure.persistence.repository.custom.impl;

import com.sang.commonpersistence.support.SqlUtils;
import com.sang.commonutil.StrUtils;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.query.QuestionSearchQuery;
import com.sang.nv.education.exam.infrastructure.persistence.repository.custom.QuestionEntityRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionEntityRepositoryImpl implements QuestionEntityRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionEntity> search(QuestionSearchQuery query) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select EN from ").append(this.getClassName().getSimpleName()).append(" EN");
        if (!StrUtils.isBlank(query.getExamId())) {
            sql.append(" left join ExamQuestionEntity EQ on EQ.questionId = EN.id ");
        }
        sql.append(createWhereQuery(query, values));
        sql.append(createOrderQuery(query.getSortBy()));
        Query queryDB = entityManager.createQuery(sql.toString(), QuestionEntity.class);
        values.forEach(queryDB::setParameter);
        queryDB.setFirstResult((query.getPageIndex() - 1) * query.getPageSize());
        queryDB.setMaxResults(query.getPageSize());
        return queryDB.getResultList();
    }

    @Override
    public Long count(QuestionSearchQuery query) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(EN) from ").append(this.getClassName().getSimpleName()).append(" EN");
        if (!StrUtils.isBlank(query.getExamId())) {
            sql.append(" left join ExamQuestionEntity EQ on EQ.questionId = EN.id ");
        }
        sql.append(createWhereQuery(query, values));
        Query queryDB = entityManager.createQuery(sql.toString(), Long.class);
        values.forEach(queryDB::setParameter);
        return (Long) queryDB.getSingleResult();
    }

    public StringBuilder createWhereQuery(QuestionSearchQuery query, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        sql.append(" where EN.deleted = false ");

        if (!StrUtils.isBlank(query.getKeyword())) {
            sql.append(" and ( EN.name like :keyword )");
            values.put("keyword", SqlUtils.encodeKeyword(query.getKeyword()));
        }
        if (!CollectionUtils.isEmpty(query.getQuestionLevels())) {
            sql.append(" and ( EN.questionLevel in :questionLevels )");
            values.put("questionLevels", query.getQuestionLevels());
        }
        if (!StrUtils.isBlank(query.getExamId())) {
            sql.append(" and ( EQ.examId = :examId )");
            values.put("examId", query.getExamId());
        }
        if (!CollectionUtils.isEmpty(query.getSubjectIds())) {
            sql.append(" and ( EN.subjectId in :subjectIds )");
            values.put("subjectIds", query.getSubjectIds());
        }
        if (!CollectionUtils.isEmpty(query.getGroupIds())) {
            sql.append(" and ( EN.groupId in :groupIds )");
            values.put("groupIds", query.getGroupIds());
        }
        return sql;
    }

    public StringBuilder createOrderQuery(String sortBy) {
        StringBuilder hql = new StringBuilder(" ");
        if (StringUtils.hasLength(sortBy)) {
            hql.append(" order by EN.").append(sortBy.replace(".", " "));
        } else {
            hql.append(" order by EN.createdAt desc ");
        }
        return hql;
    }

    private Class getClassName() {
        return QuestionEntity.class;
    }
}
