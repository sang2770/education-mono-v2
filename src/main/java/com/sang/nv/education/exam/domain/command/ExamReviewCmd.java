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
public class ExamReviewCmd {
    String code;
    String userId;
    String examId;
    String roomId;
    String periodId;
    List<String> fileIds;
}
