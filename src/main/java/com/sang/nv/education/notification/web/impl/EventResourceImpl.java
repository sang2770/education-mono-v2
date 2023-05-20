package com.sang.nv.education.notification.web.impl;


import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.notification.application.service.EventService;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.web.EventResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class EventResourceImpl implements EventResource {

    private final EventService eventService;


    @Override
    public Response<Event> uploadFileBase(MultipartFile file) throws Exception {
        return Response.of(this.eventService.firebaseUpload(file));
    }

    @Override
    public Response<Event> findById(String id) {
        return Response.of(this.eventService.getById(id));
    }

    @Override
    public Response<List<Event>> findById(FindByIdsRequest request) {
        return Response.of(this.eventService.getByIds(request.getIds()));
    }

    @Override
    public PagingResponse<Event> search(BaseSearchRequest request) {
        return PagingResponse.of(this.eventService.search(request));
    }

    @Override
    public Response<List<Event>> uploadMultiple(List<MultipartFile> files) {
        return Response.of(this.eventService.uploadMultipleFile(files));
    }
}
