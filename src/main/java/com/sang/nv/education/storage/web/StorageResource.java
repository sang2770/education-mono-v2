package com.sang.nv.education.storage.web;

import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.storage.domain.FileDomain;
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
public interface StorageResource {
    @ApiOperation("Send Storage with attack")
    @PostMapping("/storages/upload")
    Response<FileDomain> uploadFileBase(@RequestParam("file") MultipartFile file) throws Exception;

    @ApiOperation("Find file by id")
    @GetMapping("/storages/find-by-id/{id}")
    Response<FileDomain> findById(@PathVariable String id);

    @ApiOperation("Find file by ids")
    @GetMapping("/storages/find-by-ids")
    Response<List<FileDomain>> findById(FindByIdsRequest request);


    @ApiOperation("Search file")
    @GetMapping("/storages/search")
    PagingResponse<FileDomain> search(BaseSearchRequest request);

    @ApiOperation("Save multiple file ")
    @PostMapping("/storages/upload-multiple")
    Response<List<FileDomain>> uploadMultiple(@RequestParam("files") List<MultipartFile> files);

}
