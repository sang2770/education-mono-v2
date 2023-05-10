package com.sang.nv.education.exam.application.dto.response;

//import com.sang.commonclient.domain.UserDTO;

import com.sang.nv.education.iam.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserExamResult {
    Float totalPoint;
    Instant timeEnd;
    Instant timeStart;
    Boolean deleted;
    String examId;
    String userId;
    User user;
    Long totalTimeUsed;
    Float totalTime;
    Float point;
    String userExamId;
    Instant createdAt;
}
