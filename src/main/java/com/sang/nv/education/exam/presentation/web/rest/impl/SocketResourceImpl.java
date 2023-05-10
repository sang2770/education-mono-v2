package com.sang.nv.education.exam.presentation.web.rest.impl;

import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.room.SendExamToUserRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.application.service.RoomService;
import com.sang.nv.education.exam.application.service.UserExamService;
import com.sang.nv.education.exam.presentation.web.rest.SocketResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class SocketResourceImpl implements SocketResource {
    private final RoomService roomService;
    private final UserExamService userExamService;

    @Override
    public Response<Boolean> sendExam(@DestinationVariable String id, @Payload SendExamToUserRequest request) {
        log.info("socket {}", request);
        log.info("roomId {}", id);
        try {
            this.roomService.sendExam(id, request);
        } catch (Exception e) {
            log.error("sendExam", e);
            return Response.of(Boolean.FALSE);
        }
        return Response.of(Boolean.TRUE);
    }

    @Override
    public Response<UserExamResult> sendTest(@DestinationVariable String roomId, @DestinationVariable String id, @Payload UserExamCreateRequest request) {
        try {
            UserExamResult userExamResult = this.userExamService.send(roomId, id, request);
            return Response.of(userExamResult);
        } catch (RuntimeException e) {
            return Response.fail(e);
        }
    }
}
