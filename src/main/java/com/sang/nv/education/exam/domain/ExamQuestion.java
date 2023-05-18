package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.ExamQuestionCreateOrUpdateCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class ExamQuestion extends AuditableDomain {
    String id;
    Float point;
    String examId;
    String questionId;
    Question question;
    Boolean deleted;
    Boolean isStarted;


    public ExamQuestion(ExamQuestionCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.point = cmd.getPoint();
        this.deleted = Boolean.FALSE;
        this.examId = cmd.getExamId();
        this.questionId = cmd.getQuestionId();
    }

    public void updatePoint(Float point) {
        this.point = point;
    }


    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichQuestion(Question question) {
        this.question = question;
    }

    public void enrichIsStarted(Boolean isStarted) {
        this.isStarted = isStarted;
    }
}
