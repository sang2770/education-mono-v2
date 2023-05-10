package com.sang.nv.education.iam.presentation.web.rest;


import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.commonmodel.validator.anotations.ValidatePaging;
import com.sang.nv.education.iam.application.dto.request.DepartmentCreateOrUpdateRequest;
import com.sang.nv.education.iam.domain.Department;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Department Resource")
@RequestMapping("/api")
public interface DepartmentResource {
    @ApiOperation(value = "Search department")
    @GetMapping("/departments")
    PagingResponse<Department> search(@ValidatePaging(allowedSorts = {"lastModifiedAt", "createdAt", "code", "name", "phone"}) BaseSearchRequest request);

    @ApiOperation(value = "Create department")
    @PostMapping("/departments")
    Response<Department> create(@RequestBody DepartmentCreateOrUpdateRequest request);

    @ApiOperation(value = "Update department")
    @PostMapping("/departments/{id}/update")
    Response<Department> update(@PathVariable String id, @RequestBody DepartmentCreateOrUpdateRequest request);

    @ApiOperation(value = "Get department by id")
    @PostMapping("/departments/{id}")
    Response<Department> getById(@PathVariable String id);

    @ApiOperation(value = "AutoComplete department by id")
    @PostMapping("/departments/auto-complete")
    PagingResponse<Department> autoComplete(@ValidatePaging(allowedSorts = {"lastModifiedAt", "createdAt", "code", "name", "phone"})
                                            BaseSearchRequest request);
}
