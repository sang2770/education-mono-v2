package com.sang.nv.education.exam.domain.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExamInfoCreateCmd {
    String questionId;
    List<String> answerIds;
    Boolean status;
    Float point;
    String userExamId;
}
