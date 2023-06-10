package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewFileType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class ExamReviewFile extends AuditableDomain {
    String id;
    String examReviewId;
    String fileId;
    String viewUrl;
    String fileName;
    ExamReviewFileType type;
    Boolean deleted;

    public ExamReviewFile(String examReviewId, String fileId, ExamReviewFileType type){
        this.id = IdUtils.nextId();
        this.examReviewId = examReviewId;
        this.fileId = fileId;
        this.deleted = false;
        this.type = type;
    }
    public void enrichViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public void enrichFileName(String fileName) {
        this.fileName = fileName;
    }
}
