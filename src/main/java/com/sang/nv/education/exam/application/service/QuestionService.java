package com.sang.nv.education.exam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.exam.application.dto.request.question.GroupQuestionRandomRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionCreateRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionUpdateRequest;
import com.sang.nv.education.exam.application.dto.response.ImportQuestionResult;
import com.sang.nv.education.exam.domain.Question;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface QuestionService {
    /**
     * Create base price
     *
     * @param request QuestionCreateRequest
     * @return Question
     */
    Question create(QuestionCreateRequest request);

    /**
     * Update base price
     *
     * @param request QuestionCreateOrUpdateRequest
     * @return Question
     */
    Question update(String id, QuestionUpdateRequest request);

    void delete(String id);

    /**
     * Search base price
     *
     * @param request QuestionSearchRequest
     * @return PageDTO<Question>
     */
    PageDTO<Question> search(QuestionSearchRequest request);

    /**
     * Get detail base price
     *
     * @param id String
     * @return Question
     */
    Question getById(String id);

    List<Question> getRandomQuestionByGroup(GroupQuestionRandomRequest request);

    void downloadTemplateImportQuestions(HttpServletResponse response);

    ImportQuestionResult importQuestion(MultipartFile file, HttpServletResponse response);
}
