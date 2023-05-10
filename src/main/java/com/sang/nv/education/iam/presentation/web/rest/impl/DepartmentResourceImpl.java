package com.sang.nv.education.iam.presentation.web.rest.impl;

import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.iam.application.dto.request.DepartmentCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.service.DepartmentService;
import com.sang.nv.education.iam.domain.Department;
import com.sang.nv.education.iam.presentation.web.rest.DepartmentResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DepartmentResourceImpl implements DepartmentResource {
    private final DepartmentService departmentService;

    @Override
    public PagingResponse<Department> search(BaseSearchRequest request) {
        return PagingResponse.of(this.departmentService.search(request));
    }

    @Override
    public Response<Department> create(DepartmentCreateOrUpdateRequest request) {
        return Response.of(this.departmentService.create(request));
    }

    @Override
    public Response<Department> update(String id, DepartmentCreateOrUpdateRequest request) {
        return Response.of(this.departmentService.update(id, request));
    }

    @Override
    public Response<Department> getById(String id) {
        return Response.of(this.departmentService.getById(id));
    }

    @Override
    public PagingResponse<Department> autoComplete(BaseSearchRequest request) {
        return PagingResponse.of(this.departmentService.autoComplete(request));
    }
}
