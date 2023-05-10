package com.sang.nv.education.exam.presentation.web.rest;


import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.PeriodCreateOrUpdateRequest;
import com.sang.nv.education.exam.domain.Period;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Period Resource")
@RequestMapping("/api")
public interface PeriodResource {
    @ApiOperation(value = "Search Period")
    @GetMapping("/periods")
    PagingResponse<Period> search(BaseSearchRequest request);

    @ApiOperation(value = "Create Period")
    @PostMapping("/periods")
    Response<Period> create(@RequestBody PeriodCreateOrUpdateRequest request);

    @ApiOperation(value = "Update Period")
    @PostMapping("/periods/{id}/update")
    Response<Period> update(@PathVariable String id, @RequestBody PeriodCreateOrUpdateRequest request);

    @ApiOperation(value = "Get Period by id")
    @GetMapping("/periods/{id}")
    Response<Period> getById(@PathVariable String id);

    @ApiOperation(value = "Delete Period")
    @PostMapping("/periods/{id}/delete")
    Response<Void> delete(@PathVariable String id);
}
