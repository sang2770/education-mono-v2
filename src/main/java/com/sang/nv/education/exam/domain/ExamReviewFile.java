package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
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
    Boolean deleted;

    public void enrichViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public void enrichFileName(String fileName) {
        this.fileName = fileName;
    }
}
