package com.sang.nv.education.notification.web;

import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.notification.domain.Event;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "Storage resource")
@RequestMapping("/api")
public interface EventResource {
    @ApiOperation("Send Storage with attack")
    @PostMapping("/storages/upload")
    Response<Event> uploadFileBase(@RequestParam("file") MultipartFile file) throws Exception;

    @ApiOperation("Find file by id")
    @GetMapping("/storages/find-by-id/{id}")
    Response<Event> findById(@PathVariable String id);

    @ApiOperation("Find file by ids")
    @GetMapping("/storages/find-by-ids")
    Response<List<Event>> findById(FindByIdsRequest request);


    @ApiOperation("Search file")
    @GetMapping("/storages/search")
    PagingResponse<Event> search(BaseSearchRequest request);

    @ApiOperation("Save multiple file ")
    @PostMapping("/storages/upload-multiple")
    Response<List<Event>> uploadMultiple(@RequestParam("files") List<MultipartFile> files);

}
