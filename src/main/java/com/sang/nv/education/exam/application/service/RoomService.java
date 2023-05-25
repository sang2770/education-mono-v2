package com.sang.nv.education.exam.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.exam.application.dto.request.UpdateMemberInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UpdatePeriodInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.PeriodRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.SendExamToUserRequest;
import com.sang.nv.education.exam.domain.PeriodRoom;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.UserRoom;

import java.util.List;

public interface RoomService {
    /**
     * Create Room
     *
     * @param request RoomCreateRequest
     * @return Room
     */
    Room create(RoomCreateOrUpdateRequest request);

    /**
     * Update Room
     *
     * @param request RoomCreateOrUpdateRequest
     * @return Room
     */
    Room update(String id, RoomCreateOrUpdateRequest request);

    /**
     * Search Room
     *
     * @param request RoomSearchRequest
     * @return PageDTO<Room>
     */
    PageDTO<Room> search(RoomSearchRequest request);

    /**
     * Get detail Room
     *
     * @param id String
     * @return Room
     */
    Room getById(String id);

    Room addMemberInRoom(String id, UpdateMemberInRoomRequest request) throws JsonProcessingException;

    Room removeMemberInRoom(String id, UpdateMemberInRoomRequest request);

    Room addPeriodInRoom(String id, UpdatePeriodInRoomRequest request);

    Room removePeriodInRoom(String id, UpdatePeriodInRoomRequest request);

    PageDTO<UserRoom> getUserInRoom(String id, UserRoomSearchRequest request);

    PageDTO<PeriodRoom> getPeriodInRoom(String id, PeriodRoomSearchRequest request);

    void sendExamToUser(String id, UserExamCreateRequest request);

    void sendExam(String id, SendExamToUserRequest request);

    /**
     * Search Room
     *
     * @param request RoomSearchRequest
     * @return PageDTO<Room>
     */
    PageDTO<Room> getMyRoom(RoomSearchRequest request);

//    For client

    List<Room> getByIds(List<String> ids);

    List<Room> getAll();

    List<String> getAllPeriodIdInRooms(List<String> roomIds);

    PeriodRoom donePeriodInRoom(String id, String periodId);
}
