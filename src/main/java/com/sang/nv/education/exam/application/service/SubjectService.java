package com.sang.nv.education.exam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.exam.application.dto.request.BaseCreateOrUpdateRequest;
import com.sang.nv.education.exam.domain.Subject;

public interface SubjectService {
    Subject create(BaseCreateOrUpdateRequest request);

    Subject update(String id, BaseCreateOrUpdateRequest request);

    PageDTO<Subject> search(BaseSearchRequest request);

    Subject getById(String id);

    void delete(String id);
}
