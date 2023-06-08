package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.commonutil.StringPool;
import com.sang.nv.education.exam.domain.command.UserExamInfoCreateCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

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

    List<String> answerIds;


    public UserExamInfo(UserExamInfoCreateCmd cmd) {
        this.id = IdUtils.nextId();
        this.questionId = cmd.getQuestionId();
        if (!CollectionUtils.isEmpty(cmd.getAnswerIds())) {
            this.answerId = String.join(StringPool.COMMA, cmd.getAnswerIds());
        }
        this.status = cmd.getStatus();
        this.userExamId = cmd.getUserExamId();
        this.point = cmd.getStatus() ? cmd.getPoint() : 0f;
        this.deleted = Boolean.FALSE;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
    public void enrichAnswerIds() {
        if (Objects.nonNull(this.answerId)) {
            this.answerIds = List.of(this.answerId.split(StringPool.COMMA));
        }
    }
}
