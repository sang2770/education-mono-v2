package com.sang.nv.education.iam.presentation.web.rest.impl;

import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassSearchRequest;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassesCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.service.ClassesService;
import com.sang.nv.education.iam.domain.Classes;
import com.sang.nv.education.iam.presentation.web.rest.ClassesResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClassesResourceImpl implements ClassesResource {
    private final ClassesService classesService;

    @Override
    public PagingResponse<Classes> search(ClassSearchRequest request) {
        return PagingResponse.of(this.classesService.search(request));
    }

    @Override
    public Response<Classes> create(ClassesCreateOrUpdateRequest request) {
        return Response.of(this.classesService.create(request));
    }

    @Override
    public Response<Classes> update(String id, ClassesCreateOrUpdateRequest request) {
        return Response.of(this.classesService.update(id, request));
    }

    @Override
    public Response<Classes> getById(String id) {
        return Response.of(this.classesService.getById(id));
    }

    @Override
    public Response<List<Classes>> findByDepartmentId(String departmentId) {
        return Response.of(this.classesService.findByDepartmentId(departmentId));
    }

    @Override
    public PagingResponse<Classes> autoComplete(BaseSearchRequest request) {
        return PagingResponse.of(this.classesService.autoComplete(request));
    }
}
