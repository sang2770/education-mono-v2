package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.GroupQuestionCreateOrUpdateCmd;
import com.sang.nv.education.exam.infrastructure.persistence.entity.GroupQuestionEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class GroupQuestion extends AuditableDomain {
    String id;
    String name;
    String code;
    Boolean deleted;
    String subjectId;
    List<Question> questions;
    Subject subject;

    public GroupQuestion(GroupQuestionCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.deleted = Boolean.FALSE;
        this.code = cmd.getCode();
        this.subjectId = cmd.getSubjectId();
    }

    public GroupQuestion(GroupQuestionEntity cmd) {
        this.id = cmd.getId();
        this.code = cmd.getCode();
        this.name = cmd.getName();
        this.subjectId = cmd.getSubjectId();
        this.deleted = Boolean.FALSE;
    }

    public void update(GroupQuestionCreateOrUpdateCmd cmd) {
        this.name = cmd.getName();
        this.subjectId = cmd.getSubjectId();
        this.deleted = Boolean.FALSE;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichQuestions(List<Question> questionList) {
        this.questions = questionList;
    }

    public void enrichSubject(Subject subject) {
        this.subject = subject;
    }
}
