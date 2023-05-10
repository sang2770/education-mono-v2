package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
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
public class QuestionFile extends AuditableDomain {
    String id;
    String questionId;
    String fileId;
    String viewUrl;
    String fileName;
    Boolean deleted;

    public QuestionFile(String questionId, String fileId) {
        this.id = IdUtils.nextId();
        this.questionId = questionId;
        this.fileId = fileId;
        this.deleted = false;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public void enrichFileName(String fileName) {
        this.fileName = fileName;
    }
}
