package com.sang.nv.education.exam.presentation.web.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UpdateMemberInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UpdatePeriodInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.PeriodRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomSearchRequest;
import com.sang.nv.education.exam.domain.PeriodRoom;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.UserRoom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Room Resource")
@RequestMapping("/api")
public interface RoomResource {
    @ApiOperation(value = "Search Room")
    @GetMapping("/rooms")
    PagingResponse<Room> search(RoomSearchRequest request);

    @ApiOperation(value = "Create Room")
    @PostMapping("/rooms")
    Response<Room> create(@RequestBody RoomCreateOrUpdateRequest request);

    @ApiOperation(value = "Update Room")
    @PostMapping("/rooms/{id}/update")
    Response<Room> update(@PathVariable String id, @RequestBody RoomCreateOrUpdateRequest request);

    @ApiOperation(value = "Get Room by id")
    @GetMapping("/rooms/{id}")
    Response<Room> getById(@PathVariable String id);

    @ApiOperation(value = "Update Room")
    @PostMapping("/rooms/{id}/add-members")
//    @PreAuthorize("hasPermission(null, 'room:update_user')")
    Response<Room> addMember(@PathVariable String id, @RequestBody UpdateMemberInRoomRequest request) throws JsonProcessingException;


    @ApiOperation(value = "remove Member Room")
    @PostMapping("/rooms/{id}/remove-members")
    Response<Room> removeMember(@PathVariable String id, @RequestBody UpdateMemberInRoomRequest request);


    @ApiOperation(value = "remove exam Room")
    @PostMapping("/rooms/{id}/add-periods")
    Response<Room> addPeriod(@PathVariable String id, @RequestBody UpdatePeriodInRoomRequest request);


    @ApiOperation(value = "Update Room")
    @PostMapping("/rooms/{id}/remove-periods")
    Response<Room> removePeriod(@PathVariable String id, @RequestBody UpdatePeriodInRoomRequest request);

    @ApiOperation(value = "get User in room")
    @GetMapping("/rooms/{id}/users")
    PagingResponse<UserRoom> getUserInRoom(@PathVariable String id, UserRoomSearchRequest request);

    @ApiOperation(value = "get period in room")
    @GetMapping("/rooms/{id}/periods")
    PagingResponse<PeriodRoom> getPeriodInRoom(@PathVariable String id, PeriodRoomSearchRequest
            request);

    @ApiOperation(value = "Done period in room")
    @PostMapping("/rooms/{id}/periods/{periodId}/done")
    Response<PeriodRoom> donePeriodInRoom(@PathVariable String id, @PathVariable String periodId);


    @ApiOperation(value = "Send exam to user Room")
    @PostMapping("/rooms/{id}/send-exam-to-user")
    @PreAuthorize("hasPermission(null, 'room:send_exam')")
    Response<Void> sendExamToUser(@PathVariable String id, @RequestBody UserExamCreateRequest request);

    //    For client
    @ApiOperation(value = "Get My Room")
    @GetMapping("/rooms/my-room")
    PagingResponse<Room> getMyRoom(RoomSearchRequest request);

}
