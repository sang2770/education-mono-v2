package com.sang.nv.education.exam.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.exam.domain.ExamReviewFile;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamReviewFileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamReviewFileEntityMapper extends EntityMapper<ExamReviewFile, ExamReviewFileEntity> {
}
