package com.sang.nv.education.iam.infrastructure.persistence.repository.impl;

import com.sang.commonpersistence.support.SqlUtils;
import com.sang.commonutil.StrUtils;
import com.sang.nv.education.iam.domain.query.ClassSearchQuery;
import com.sang.nv.education.iam.infrastructure.persistence.entity.ClassEntity;
import com.sang.nv.education.iam.infrastructure.persistence.repository.custom.ClassesEntityRepositoryCustom;
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
public class ClassesEntityRepositoryImpl implements ClassesEntityRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ClassEntity> search(ClassSearchQuery querySearch) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select U from ClassEntity U  ");
        sql.append(createWhereQuery(querySearch, values));
        sql.append(createOrderQuery(querySearch.getSortBy()));
        Query query = entityManager.createQuery(sql.toString(), ClassEntity.class);
        values.forEach(query::setParameter);
        query.setFirstResult((querySearch.getPageIndex() - 1) * querySearch.getPageSize());
        query.setMaxResults(querySearch.getPageSize());
        return query.getResultList();
    }

    @Override
    public Long count(ClassSearchQuery querySearch) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(U) from ClassEntity U ");
        sql.append(createWhereQuery(querySearch, values));
        Query query = entityManager.createQuery(sql.toString(), Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    StringBuilder createWhereQuery(ClassSearchQuery querySearch, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder(" where 1=1 ");
        if (!StrUtils.isBlank(querySearch.getKeyword())) {
            sql.append(" and ( U.name like :keyword )");
            values.put("keyword", SqlUtils.encodeKeyword(querySearch.getKeyword()));
        }
        if (!CollectionUtils.isEmpty(querySearch.getDepartmentIds())) {
            sql.append(" and U.departmentId in :departmentIds ");
            values.put("departmentIds", querySearch.getDepartmentIds());
        }
        if (!CollectionUtils.isEmpty(querySearch.getKeyIds())) {
            sql.append(" and U.keyId in :keyIds ");
            values.put("keyIds", querySearch.getKeyIds());
        }

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
