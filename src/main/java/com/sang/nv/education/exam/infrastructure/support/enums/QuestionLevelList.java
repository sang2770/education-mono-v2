package com.sang.nv.education.exam.infrastructure.support.enums;

import lombok.Getter;

@Getter
public enum QuestionLevelList {
    HIGH(QuestionLevel.HIGH, "Cao"),
    MEDIUM(QuestionLevel.MEDIUM, "Trung bình"),
    LOW(QuestionLevel.LOW, "Thấp")
    ;
    QuestionLevel code;
    String name;
    QuestionLevelList(QuestionLevel level, String name){
        this.code = level;
        this.name = name;
    }
}
