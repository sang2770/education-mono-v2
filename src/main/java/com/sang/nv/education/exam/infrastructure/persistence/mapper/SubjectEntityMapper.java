package com.sang.nv.education.exam.infrastructure.persistence.mapper;

import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.exam.domain.Subject;
import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectEntityMapper extends EntityMapper<Subject, SubjectEntity> {
}
