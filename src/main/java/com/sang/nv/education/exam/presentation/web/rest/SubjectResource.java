package com.sang.nv.education.exam.presentation.web.rest;


import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.BaseCreateOrUpdateRequest;
import com.sang.nv.education.exam.domain.Subject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Subject Resource")
@RequestMapping("/api")
public interface SubjectResource {
    @ApiOperation(value = "Search Subject")
    @GetMapping("/subjects")
    PagingResponse<Subject> search(BaseSearchRequest request);

    @ApiOperation(value = "Create Subject")
    @PostMapping("/subjects")
    Response<Subject> create(@RequestBody BaseCreateOrUpdateRequest request);

    @ApiOperation(value = "Update Subject")
    @PostMapping("/subjects/{id}/update")
    Response<Subject> update(@PathVariable String id, @RequestBody BaseCreateOrUpdateRequest request);

    @ApiOperation(value = "Get Subject by id")
    @GetMapping("/subjects/{id}")
    Response<Subject> getById(@PathVariable String id);

    @ApiOperation(value = "Delete subject")
    @PostMapping("/subjects/{id}/delete")
    Response<Void> delete(@PathVariable String id);

}
