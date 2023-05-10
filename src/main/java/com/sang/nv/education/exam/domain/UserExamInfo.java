package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.UserExamInfoCreateCmd;
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
public class UserExamInfo extends AuditableDomain {
    String id;
    String questionId;
    String answerId;
    Boolean status;
    Float point;
    Boolean deleted;

    String userExamId;


    public UserExamInfo(UserExamInfoCreateCmd cmd) {
        this.id = IdUtils.nextId();
        this.questionId = cmd.getQuestionId();
        this.answerId = cmd.getAnswerId();
        this.status = cmd.getStatus();
        this.userExamId = cmd.getUserExamId();
        this.point = this.status ? cmd.getPoint() : 0f;
        this.deleted = Boolean.FALSE;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
