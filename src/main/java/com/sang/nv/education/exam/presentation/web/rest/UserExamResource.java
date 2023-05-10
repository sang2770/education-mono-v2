package com.sang.nv.education.exam.presentation.web.rest;


import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.domain.UserExam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Exam Resource")
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
}
