package com.sang.nv.education.storage.application.service;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.storage.domain.FileDomain;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    // fire base

    FileDomain firebaseUpload(MultipartFile file) throws Exception;

    PageDTO<FileDomain> search(BaseSearchRequest request);

    FileDomain getById(String id);

    List<FileDomain> uploadMultipleFile(List<MultipartFile> fileList);

    List<FileDomain> getByIds(List<String> ids);
}
