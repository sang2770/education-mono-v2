package com.sang.nv.education.exam.presentation.web.rest.impl;

import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.application.service.UserExamService;
import com.sang.nv.education.exam.domain.UserExam;
import com.sang.nv.education.exam.presentation.web.rest.UserExamResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserExamResourceImpl implements UserExamResource {

    private final UserExamService userExamService;

    @Override
    public Response<UserExamResult> sendTest(String roomId, String id, UserExamCreateRequest request) {
        return Response.of(this.userExamService.send(roomId, id, request));
    }

    @Override
    public Response<UserExam> getById(String id) {
        return Response.of(this.userExamService.getById(id));
    }

    @Override
    public Response<UserExam> getByExamIdAndPeriodId(String examId, String periodId) {
        return Response.of(this.userExamService.getByExamIdAndPeriodId(examId, periodId));
    }

    @Override
    public PagingResponse<UserExam> searchByRoomAndPeriod(String roomId, String periodId, UserRoomSearchRequest request) {
        return PagingResponse.of(this.userExamService.searchByRoomAndPeriod(roomId, periodId, request));
    }

    @Override
    public PagingResponse<UserExam> getMyExamByRoomAndPeriod(String roomId, String periodId, UserRoomSearchRequest request) {
        return PagingResponse.of(this.userExamService.getMyExamByRoomAndPeriod(roomId, periodId, request));
    }

    @Override
    public Response<UserExam> testingExam(String id) {
        return Response.of(this.userExamService.testingExam(id));
    }
}
