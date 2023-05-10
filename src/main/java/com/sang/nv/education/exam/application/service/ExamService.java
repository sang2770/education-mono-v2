package com.sang.nv.education.exam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.exam.application.dto.request.exam.ExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.exam.ExamSearchRequest;
import com.sang.nv.education.exam.application.dto.request.exam.ExamUpdateRequest;
import com.sang.nv.education.exam.domain.Exam;

import java.util.List;

public interface ExamService {
    /**
     * Create exam
     *
     * @param request ExamCreateRequest
     * @return Exam
     */
    Exam create(ExamCreateRequest request);

    /**
     * Update exam
     *
     * @param request ExamCreateOrUpdateRequest
     * @return Exam
     */
    Exam update(String id, ExamUpdateRequest request);

    /**
     * Search exam
     *
     * @param request ExamSearchRequest
     * @return PageDTO<Exam>
     */
    PageDTO<Exam> search(ExamSearchRequest request);

    /**
     * Get detail exam
     *
     * @param id String
     * @return Exam
     */
    Exam getById(String id);

    PageDTO<Exam> getByRoomId(String roomId, ExamSearchRequest request);

    void addQuestionToExam(String examId, String questionId);

    void removeQuestionToExam(String examId, String questionId);

    Integer countExam(List<String> roomIds);
}
