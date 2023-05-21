package com.sang.nv.education.notification.infrastructure.persistence.repository.impl;

import com.sang.commonutil.StrUtils;
import com.sang.nv.education.notification.domain.query.NotificationSearchQuery;
import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationEntity;
import com.sang.nv.education.notification.infrastructure.persistence.repository.custom.NotificationEntityRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class NotificationEntityRepositoryImpl implements NotificationEntityRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NotificationEntity> search(NotificationSearchQuery querySearch) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select U from NotificationEntity U  ");
        sql.append(createWhereQuery(querySearch, values));
        sql.append(createOrderQuery(querySearch.getSortBy()));
        Query query = entityManager.createQuery(sql.toString(), NotificationEntity.class);
        values.forEach(query::setParameter);
        query.setFirstResult((querySearch.getPageIndex() - 1) * querySearch.getPageSize());
        query.setMaxResults(querySearch.getPageSize());
        return query.getResultList();
    }

    @Override
    public Long count(NotificationSearchQuery querySearch) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(U) from NotificationEntity U ");
        sql.append(createWhereQuery(querySearch, values));
        Query query = entityManager.createQuery(sql.toString(), Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    StringBuilder createWhereQuery(NotificationSearchQuery querySearch, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE 1 = 1 ");
        if (Objects.nonNull(querySearch.getUserId()) && !StrUtils.isBlank(querySearch.getUserId())) {
            sql.append(" and e.targetId = :userId ");
            values.put("userId", querySearch.getUserId());
        }
        sql.append(" and e.isSend = true ");
        sql.append(" and e.deleted = false ");
        return sql;
    }

    public StringBuilder createOrderQuery(String sortBy) {
        StringBuilder hql = new StringBuilder(" ");
        if (StringUtils.hasLength(sortBy)) {
            hql.append(" order by U.").append(sortBy.replace(".", " "));
        } else {
            hql.append(" order by U.createdAt desc ");
        }
        return hql;
    }
}
