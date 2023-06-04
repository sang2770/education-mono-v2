package com.sang.nv.education.exam.presentation.web.rest;


import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamReviewDoneRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.domain.ExamReview;
import com.sang.nv.education.exam.domain.UserExam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "User Exam Resource")
@RequestMapping("/api")
public interface UserExamResource {
    @ApiOperation(value = "Create Exam")
    @PostMapping("/user-exams/room/{roomId}/user-exams/{id}/test")
    Response<UserExamResult> sendTest(@PathVariable String roomId, @PathVariable String id, @RequestBody UserExamCreateRequest request);

    @ApiOperation(value = "Get Exam by id")
    @GetMapping("/user-exams/{id}")
    Response<UserExam> getById(@PathVariable String id);

    @ApiOperation(value = "Get Exam by room")
    @GetMapping("/user-exams/period/{periodId}/get-by-exam/{examId}")
    Response<UserExam> getByExamIdAndPeriodId(@PathVariable String examId, @PathVariable String periodId);

    @ApiOperation(value = "Get User Exam by room")
    @GetMapping("/user-exams/{roomId}/get-user-exam/{periodId}")
    PagingResponse<UserExam> searchByRoomAndPeriod(@PathVariable String roomId, @PathVariable String periodId, UserRoomSearchRequest request);


    //    For Student
    @ApiOperation(value = "Get my Exam by room")
    @GetMapping("/user-exams/{roomId}/get-my-exam/{periodId}")
    PagingResponse<UserExam> getMyExamByRoomAndPeriod(@PathVariable String roomId, @PathVariable String periodId, UserRoomSearchRequest request);


    @ApiOperation(value = "Testing Exam by id")
    @GetMapping("/user-exams/{id}/testing")
    Response<UserExam> testingExam(@PathVariable String id);

    /**
     * Review
     */

    @ApiOperation(value = "Request review exam by Id")
    @PostMapping("/user-exams/{id}/review")
    Response<ExamReview> review(@PathVariable String id);

    @ApiOperation(value = "Get All review exam by Id")
    @GetMapping("/user-exams/{id}/get-all-review")
    Response<List<ExamReview>> getAllReview(@PathVariable String id, BaseSearchRequest request);

    @ApiOperation(value = "Get All review exam by room period")
    @GetMapping("/user-exams/{roomId}/get-all-review/{periodId}")
    Response<List<ExamReview>> getAllReviewByPeriodRoom(@PathVariable String roomId, @PathVariable String periodId,  BaseSearchRequest request);


    @ApiOperation(value = "Receive review exam by Id")
    @PostMapping("/user-exams/{id}/receive-review/{reviewId}")
    Response<ExamReview> receiveReview(@PathVariable String id, @PathVariable String reviewId);

    @ApiOperation(value = "Done review exam by Id")
    @PostMapping("/user-exams/{id}/done-review/{reviewId}")
    Response<ExamReview> doneReview(@PathVariable String id, @PathVariable String reviewId, @RequestBody UserExamReviewDoneRequest request);

}
