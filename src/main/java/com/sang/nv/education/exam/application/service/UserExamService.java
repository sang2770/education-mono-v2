package com.sang.nv.education.exam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamReviewDoneRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.domain.ExamReview;
import com.sang.nv.education.exam.domain.UserExam;
import com.sang.nv.education.report.application.dto.request.UserExamReportRequest;

import java.util.List;

public interface UserExamService {
    /**
     * Create exam
     *
     * @param id
     * @param request ExamCreateRequest
     * @return Exam
     */
    UserExamResult send(String roomId, String id, UserExamCreateRequest request);

    /**
     * Get detail UserExam
     *
     * @param id String
     * @return UserExam
     */
    UserExam getById(String id);

    UserExam testingExam(String id);

    UserExam getByExamIdAndPeriodId(String examId, String periodId);

    PageDTO<UserExam> searchByRoomAndPeriod(String roomId, String periodId, UserRoomSearchRequest request);

    PageDTO<UserExam> getMyExamByRoomAndPeriod(String roomId, String periodId, UserRoomSearchRequest request);

    List<UserExamResult> statisticResult(UserExamReportRequest request);

    ExamReview review(String id);

    List<ExamReview> getAllReview(String id);

    ExamReview receiveReview(String id, String reviewId);


    ExamReview doneReview(String id, String reviewId, UserExamReviewDoneRequest request);

    List<ExamReview> getAllReviewByPeriodRoom(String roomId, String periodId);
}
