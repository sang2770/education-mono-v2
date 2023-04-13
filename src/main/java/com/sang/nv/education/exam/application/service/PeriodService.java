package com.sang.nv.education.exam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.exam.application.dto.request.PeriodCreateOrUpdateRequest;
import com.sang.nv.education.exam.domain.Period;

public interface PeriodService {
    Period create(PeriodCreateOrUpdateRequest request);
    Period update(String id, PeriodCreateOrUpdateRequest request);
    PageDTO<Period> search(BaseSearchRequest request);
    Period getById(String id);

    void delete(String id);
}
