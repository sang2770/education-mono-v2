package com.sang.nv.education.exam.domain.repository;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.common.web.support.DomainRepository;
import com.sang.nv.education.exam.domain.UserExam;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserExamDomainRepository extends DomainRepository<UserExam, String> {
    UserExam findByExamIdAndPeriodId(String examId, String periodId);
//    UserExam findById(String id);

    PageDTO<UserExam> searchUserExam(String roomId, String periodId, String keyword, Pageable pageable);

    PageDTO<UserExam> getMyExam(String roomId, String periodId, List<String> userIds, Pageable pageable);
}

