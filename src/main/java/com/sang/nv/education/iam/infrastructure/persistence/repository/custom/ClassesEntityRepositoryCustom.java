package com.sang.nv.education.iam.infrastructure.persistence.repository.custom;


import com.sang.nv.education.iam.domain.query.ClassSearchQuery;
import com.sang.nv.education.iam.infrastructure.persistence.entity.ClassEntity;

import java.util.List;

public interface ClassesEntityRepositoryCustom {
    List<ClassEntity> search(ClassSearchQuery querySearch);

    Long count(ClassSearchQuery querySearch);

}
