package com.sang.nv.education.exam.infrastructure.persistence.repository.custom.impl;

import com.sang.commonpersistence.support.SqlUtils;
import com.sang.commonutil.StrUtils;
import com.sang.nv.education.exam.infrastructure.persistence.entity.RoomEntity;
import com.sang.nv.education.exam.infrastructure.persistence.query.RoomSearchQuery;
import com.sang.nv.education.exam.infrastructure.persistence.repository.custom.RoomEntityRepositoryCustom;
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
public class RoomEntityRepositoryImpl implements RoomEntityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RoomEntity> search(RoomSearchQuery query) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select EN from ").append(this.getClassName().getSimpleName()).append(" EN");
        if (!CollectionUtils.isEmpty(query.getUserIds())) {
            sql.append(" join UserRoomEntity UR on UR.roomId = EN.id and UR.userId in :userIds");
            values.put("userIds", query.getUserIds());

        }
        sql.append(createWhereQuery(query, values));
        sql.append(createOrderQuery(query.getSortBy()));
        Query queryDB = entityManager.createQuery(sql.toString(), RoomEntity.class);
        values.forEach(queryDB::setParameter);
        queryDB.setFirstResult((query.getPageIndex() - 1) * query.getPageSize());
        queryDB.setMaxResults(query.getPageSize());
        return queryDB.getResultList();
    }

    @Override
    public Long count(RoomSearchQuery query) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(EN) from ").append(this.getClassName().getSimpleName()).append(" EN");
        if (!CollectionUtils.isEmpty(query.getUserIds())) {
            sql.append(" join UserRoomEntity UR on UR.roomId = EN.id and UR.userId in :userIds ");
            values.put("userIds", query.getUserIds());
        }
        sql.append(createWhereQuery(query, values));
        Query queryDB = entityManager.createQuery(sql.toString(), Long.class);
        values.forEach(queryDB::setParameter);
        return (Long) queryDB.getSingleResult();
    }

    public StringBuilder createWhereQuery(RoomSearchQuery query, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        sql.append(" where EN.deleted = false ");

        if (!StrUtils.isBlank(query.getKeyword())) {
            sql.append(" and ( EN.name like :keyword )");
            values.put("keyword", SqlUtils.encodeKeyword(query.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(query.getSubjectIds())) {
            sql.append(" and ( EN.subjectId in :subjectIds )");
            values.put("subjectIds", query.getSubjectIds());
        }

        if (!CollectionUtils.isEmpty(query.getIds())) {
            sql.append(" and ( EN.id in :roomIds )");
            values.put("roomIds", query.getIds());
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
        return RoomEntity.class;
    }
}
