package com.sang.nv.education.report.application.mapper;


import com.sang.commonmodel.mapper.BaseAutoMapper;
import com.sang.nv.education.exam.domain.UserExam;
import com.sang.nv.education.report.application.dto.response.OutTabTopResponse;
import com.sang.nv.education.report.application.dto.response.UserTopResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportAutoMapper extends BaseAutoMapper {
    List<UserTopResponse> from(List<UserExam> userExams);

    List<OutTabTopResponse> fromUserExams(List<UserExam> values);

}
