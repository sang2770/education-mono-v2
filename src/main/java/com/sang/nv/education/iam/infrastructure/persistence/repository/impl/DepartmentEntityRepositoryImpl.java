package com.sang.nv.education.iam.infrastructure.persistence.repository.impl;

import com.sang.commonpersistence.repository.BaseRepositoryImpl;
import com.sang.nv.education.iam.infrastructure.persistence.entity.DepartmentEntity;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentEntityRepositoryImpl extends BaseRepositoryImpl {
    @Override
    public Class getClassName() {
        return DepartmentEntity.class;
    }
}
