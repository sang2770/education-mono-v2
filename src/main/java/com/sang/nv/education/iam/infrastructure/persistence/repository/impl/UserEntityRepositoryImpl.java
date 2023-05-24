package com.sang.nv.education.iam.infrastructure.persistence.repository.impl;


import com.sang.commonpersistence.support.SqlUtils;
import com.sang.commonutil.StrUtils;
import com.sang.nv.education.iam.domain.query.UserSearchQuery;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iam.infrastructure.persistence.repository.custom.UserEntityRepositoryCustom;
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
public class UserEntityRepositoryImpl implements UserEntityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserEntity> search(UserSearchQuery querySearch) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select U from UserEntity U  ");
        if (!CollectionUtils.isEmpty(querySearch.getRoleIds())) {
            sql.append("left join UserRoleEntity UR on UR.userId = U.id ");
        }
        if (!CollectionUtils.isEmpty(querySearch.getRoomIds())) {
            sql.append(" left join UserRoomEntity UB on UB.userId = U.id ");
        }
        sql.append(createWhereQuery(querySearch, values));
        sql.append(createOrderQuery(querySearch.getSortBy()));
        Query query = entityManager.createQuery(sql.toString(), UserEntity.class);
        values.forEach(query::setParameter);
        query.setFirstResult((querySearch.getPageIndex() - 1) * querySearch.getPageSize());
        query.setMaxResults(querySearch.getPageSize());
        return query.getResultList();
    }

    @Override
    public Long countUser(UserSearchQuery querySearch) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(U) from UserEntity U ");
        if (!CollectionUtils.isEmpty(querySearch.getRoleIds())) {
            sql.append(" left join UserRoleEntity UR on UR.userId = U.id ");
        }
        if (!CollectionUtils.isEmpty(querySearch.getRoomIds())) {
            sql.append(" left join UserRoomEntity UB on UB.userId = U.id ");
        }
        sql.append(createWhereQuery(querySearch, values));
        if (!CollectionUtils.isEmpty(querySearch.getRoomIds())) {
            sql.append(" and UB.deleted = false ");
        }
        Query query = entityManager.createQuery(sql.toString(), Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    StringBuilder createWhereQuery(UserSearchQuery querySearch, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder(" where 1=1 ");
        if (!StrUtils.isBlank(querySearch.getKeyword())) {
            sql.append(" and ( U.username like :keyword" +
                    " or U.email like :keyword" +
                    " or U.fullName like :keyword" +
                    " or U.phoneNumber like :keyword  ) ");
            values.put("keyword", SqlUtils.encodeKeyword(querySearch.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(querySearch.getStatuses())) {
            sql.append(" and U.status IN :statuses ");
            values.put("statuses", querySearch.getStatuses());
        }

        if (!CollectionUtils.isEmpty(querySearch.getRoleIds())) {
            sql.append(" and UR.roleId IN :roleIds ");
            values.put("roleIds", querySearch.getRoleIds());
        }
        if (!CollectionUtils.isEmpty(querySearch.getUserTypes())) {
            sql.append(" and U.userType IN :userTypes ");
            values.put("userTypes", querySearch.getUserTypes());
        }
        if (!CollectionUtils.isEmpty(querySearch.getClassIds())) {
            sql.append(" and U.classId IN :classIds ");
            values.put("classIds", querySearch.getClassIds());
        }
        if (!CollectionUtils.isEmpty(querySearch.getRoomIds())) {
            sql.append(" and UB.roomId IN :roomIds ");
            values.put("roomIds", querySearch.getRoomIds());
        }
        return sql;
    }

    public StringBuilder createOrderQuery(String sortBy) {
        StringBuilder hql = new StringBuilder(" ");
        if (StringUtils.hasLength(sortBy)) {
            hql.append(" order by U.").append(sortBy.replace(".", " "));
        } else {
            hql.append(" order by U.lastModifiedAt desc ");
        }
        return hql;
    }
}
