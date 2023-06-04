package com.sang.nv.education.exam.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.exam.domain.ExamReview;
import com.sang.nv.education.exam.domain.QuestionFile;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamReviewEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionFileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamReviewEntityMapper extends EntityMapper<ExamReview, ExamReviewEntity> {
}
