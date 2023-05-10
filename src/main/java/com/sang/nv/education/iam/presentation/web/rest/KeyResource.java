package com.sang.nv.education.iam.presentation.web.rest;


import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.commonmodel.validator.anotations.ValidatePaging;
import com.sang.nv.education.iam.application.dto.request.KeyCreateOrUpdateRequest;
import com.sang.nv.education.iam.domain.Key;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Key Resource")
@RequestMapping("/api")
public interface KeyResource {
    @ApiOperation(value = "Search Key")
    @GetMapping("/keys")
    PagingResponse<Key> search(@ValidatePaging(allowedSorts = {"name"}) BaseSearchRequest request);

    @ApiOperation(value = "Create Key")
    @PostMapping("/keys")
    Response<Key> create(@RequestBody KeyCreateOrUpdateRequest request);

    @ApiOperation(value = "Update Key")
    @PostMapping("/keys/{id}/update")
    Response<Key> update(@PathVariable String id, @RequestBody KeyCreateOrUpdateRequest request);

    @ApiOperation(value = "Get Key by id")
    @GetMapping("/keys/{id}")
    Response<Key> getById(@PathVariable String id);
}
