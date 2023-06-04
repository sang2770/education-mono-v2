package com.sang.nv.education.exam.domain.repository;


import com.sang.nv.education.common.web.support.DomainRepository;
import com.sang.nv.education.exam.domain.ExamReview;

import java.util.List;

public interface ExamReviewDomainRepository extends DomainRepository<ExamReview, String> {
    List<ExamReview> getByUserExamId(String id, String keyword);

    Boolean checkExist(String id);

    List<ExamReview> getAllByPeriodRoom(String periodId, String roomId, String keyword);
}

