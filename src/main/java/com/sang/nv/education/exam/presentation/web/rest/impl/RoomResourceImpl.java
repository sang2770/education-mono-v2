package com.sang.nv.education.exam.presentation.web.rest.impl;

import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UpdateMemberInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UpdatePeriodInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.PeriodRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomSearchRequest;
import com.sang.nv.education.exam.application.service.RoomService;
import com.sang.nv.education.exam.domain.PeriodRoom;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.UserRoom;
import com.sang.nv.education.exam.presentation.web.rest.RoomResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoomResourceImpl implements RoomResource {
    private RoomService RoomsService;

    @Override
    public PagingResponse<Room> search(RoomSearchRequest request) {
        return PagingResponse.of(this.RoomsService.search(request));
    }

    @Override
    public Response<Room> create(RoomCreateOrUpdateRequest request) {
        return Response.of(this.RoomsService.create(request));
    }

    @Override
    public Response<Room> update(String id, RoomCreateOrUpdateRequest request) {
        return Response.of(this.RoomsService.update(id, request));
    }

    @Override
    public Response<Room> getById(String id) {
        return Response.of(this.RoomsService.getById(id));
    }

    @Override
    public Response<Room> addMember(String id, UpdateMemberInRoomRequest request) {
        return Response.of(this.RoomsService.addMemberInRoom(id, request));
    }

    @Override
    public Response<Room> removeMember(String id, UpdateMemberInRoomRequest request) {
        return Response.of(this.RoomsService.removeMemberInRoom(id, request));
    }

    @Override
    public Response<Room> addPeriod(String id, UpdatePeriodInRoomRequest request) {
        return Response.of(this.RoomsService.addPeriodInRoom(id, request));
    }

    @Override
    public Response<Room> removePeriod(String id, UpdatePeriodInRoomRequest request) {
        return Response.of(this.RoomsService.removePeriodInRoom(id, request));
    }

    @Override
    public PagingResponse<UserRoom> getUserInRoom(String id, UserRoomSearchRequest request) {
        return PagingResponse.of(this.RoomsService.getUserInRoom(id, request));
    }

    @Override
    public PagingResponse<PeriodRoom> getPeriodInRoom(String id, PeriodRoomSearchRequest request) {
        return PagingResponse.of(this.RoomsService.getPeriodInRoom(id, request));
    }

    @Override
    public Response<Void> sendExamToUser(String id, UserExamCreateRequest request) {
        this.RoomsService.sendExamToUser(id, request);
        return Response.ok();
    }

    @Override
    public PagingResponse<Room> getMyRoom(RoomSearchRequest request) {
        return PagingResponse.of(this.RoomsService.getMyRoom(request));
    }
}
