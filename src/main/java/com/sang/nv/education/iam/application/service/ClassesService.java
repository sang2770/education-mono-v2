package com.sang.nv.education.iam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassSearchRequest;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassesCreateOrUpdateRequest;
import com.sang.nv.education.iam.domain.Classes;

import java.util.List;

public interface ClassesService {
    Classes create(ClassesCreateOrUpdateRequest request);

    Classes update(String id, ClassesCreateOrUpdateRequest request);

    PageDTO<Classes> search(ClassSearchRequest request);

    Classes getById(String id);

    List<Classes> findByDepartmentId(String departmentId);

    PageDTO<Classes> autoComplete(BaseSearchRequest request);
}
