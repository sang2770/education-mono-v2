package com.sang.nv.education.exam.domain.command;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
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
