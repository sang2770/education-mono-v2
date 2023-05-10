package com.sang.nv.education.exam.domain.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamUpdateCmd {
    Float totalPoint;
    String periodId;
    String subjectId;
    String subjectName;
    String periodName;
    String name;
    Long time;
    Long timeDelay;
    List<String> questionIds;
}
