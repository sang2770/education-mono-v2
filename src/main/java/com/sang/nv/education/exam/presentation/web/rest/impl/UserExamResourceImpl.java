package com.sang.nv.education.exam.presentation.web.rest.impl;

import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamReviewCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamReviewDoneRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.application.service.UserExamService;
import com.sang.nv.education.exam.domain.ExamReview;
import com.sang.nv.education.exam.domain.UserExam;
import com.sang.nv.education.exam.presentation.web.rest.UserExamResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Override
    public Response<ExamReview> review(String id, UserExamReviewCreateRequest request) {
        return Response.of(this.userExamService.review(id, request));
    }

    @Override
    public Response<List<ExamReview>> getAllReview(String id) {
        return Response.of(this.userExamService.getAllReview(id));
    }

    @Override
    public Response<ExamReview> receiveReview(String id, String reviewId) {
        return Response.of(this.userExamService.receiveReview(id, reviewId));
    }

    @Override
    public Response<ExamReview> doneReview(String id, String reviewId, UserExamReviewDoneRequest request) {
        return Response.of(this.userExamService.doneReview(id, reviewId, request));
    }

    @Override
    public Response<List<ExamReview>> getAllReviewByPeriodRoom(String roomId, String periodId) {
        return Response.of(this.userExamService.getAllReviewByPeriodRoom(roomId, periodId));
    }
}
