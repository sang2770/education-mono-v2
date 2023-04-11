package com.sang.nv.education.iam.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.iamdomain.Classes;
import com.sang.nv.education.iaminfrastructure.persistence.entity.ClassEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassesEntityMapper extends EntityMapper<Classes, ClassEntity> {
}
