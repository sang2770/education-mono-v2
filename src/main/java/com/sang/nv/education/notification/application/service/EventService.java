package com.sang.nv.education.notification.application.service;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.notification.domain.Event;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    // fire base

    Event firebaseUpload(MultipartFile file) throws Exception;

    PageDTO<Event> search(BaseSearchRequest request);

    Event getById(String id);

    List<Event> uploadMultipleFile(List<MultipartFile> fileList);

    List<Event> getByIds(List<String> ids);
}
