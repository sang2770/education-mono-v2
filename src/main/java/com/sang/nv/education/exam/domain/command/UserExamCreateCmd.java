package com.sang.nv.education.exam.domain.command;

import com.sang.nv.education.exam.application.dto.request.UserExamInfoCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExamCreateCmd {
    String code;
    Instant timeEnd;
    Instant timeStart;
    String examId;
    String periodId;
    String userId;
    String roomId;
    Float maxPoint;
    Long timeDelay;
    List<UserExamInfoCreateRequest> userExamInfoCreateRequests;

    Integer numberOutTab;

}
