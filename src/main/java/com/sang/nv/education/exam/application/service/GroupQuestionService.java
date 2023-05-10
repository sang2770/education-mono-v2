package com.sang.nv.education.exam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.exam.application.dto.request.question.GroupQuestionCreateOrUpdateRequest;
import com.sang.nv.education.exam.domain.GroupQuestion;

public interface GroupQuestionService {
    GroupQuestion create(GroupQuestionCreateOrUpdateRequest request);

    GroupQuestion update(String id, GroupQuestionCreateOrUpdateRequest request);

    PageDTO<GroupQuestion> search(BaseSearchRequest request);

    GroupQuestion getById(String id);

    void delete(String id);
}
