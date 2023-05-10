package com.sang.nv.education.exam.application.mapper;


import com.sang.commonmodel.mapper.BaseAutoMapper;
import com.sang.nv.education.exam.application.dto.request.BaseCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.PeriodCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.exam.ExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.exam.ExamUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.question.AnswerCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.question.GroupQuestionCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionCreateRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomCreateOrUpdateRequest;
import com.sang.nv.education.exam.domain.command.AnswerCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.BaseCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.ExamCreateCmd;
import com.sang.nv.education.exam.domain.command.ExamUpdateCmd;
import com.sang.nv.education.exam.domain.command.GroupQuestionCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.PeriodCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.QuestionCreateCmd;
import com.sang.nv.education.exam.domain.command.QuestionUpdateCmd;
import com.sang.nv.education.exam.domain.command.RoomCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.UserExamCreateCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ExamAutoMapper extends BaseAutoMapper {
    BaseCreateOrUpdateCmd from(BaseCreateOrUpdateRequest request);

    ExamCreateCmd from(ExamCreateRequest request);

    RoomCreateOrUpdateCmd from(RoomCreateOrUpdateRequest request);

    ExamUpdateCmd from(ExamUpdateRequest request);

    @Mapping(target = "extraAnswer", source = "extraAnswer", qualifiedByName = "answerConvert")
    QuestionUpdateCmd from(QuestionUpdateRequest request);

    @Mapping(target = "answerCreateOrUpdateCmdList", source = "answerCreateOrUpdateRequests", qualifiedByName = "answerConvert")
    QuestionCreateCmd from(QuestionCreateRequest request);

    PeriodCreateOrUpdateCmd from(PeriodCreateOrUpdateRequest request);

    GroupQuestionCreateOrUpdateCmd from(GroupQuestionCreateOrUpdateRequest request);

    UserExamCreateCmd from(UserExamCreateRequest request);

    @Named("answerConvert")
    public static List<AnswerCreateOrUpdateCmd> answerConvert(List<AnswerCreateOrUpdateRequest> answerCreateOrUpdateRequests) {
        if (CollectionUtils.isEmpty(answerCreateOrUpdateRequests)) {
            return new ArrayList<>();
        }
        return answerCreateOrUpdateRequests.stream().map(item ->
                AnswerCreateOrUpdateCmd.builder()
                        .content(item.getContent())
                        .status(item.getStatus()).build()).collect(Collectors.toList());
    }

}
