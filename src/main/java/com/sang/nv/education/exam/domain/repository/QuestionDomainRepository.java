package com.sang.nv.education.exam.domain.repository;


import com.sang.commonweb.support.DomainRepository;
import com.sang.nv.education.exam.domain.Question;

import java.util.List;

public interface QuestionDomainRepository extends DomainRepository<Question, String> {
    List<Question> getByIds(List<String> ids);
}

