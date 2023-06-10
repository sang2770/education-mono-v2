package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewFileType;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewStatus;
import com.sang.nv.education.iam.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class ExamReview extends AuditableDomain {
    String id;
    String code;
    String userId;
    String examId;
    String roomId;
    String periodId;
    String feedBack;
    String userExamId;
    String userExamCode;
    ExamReviewStatus status;
    List<ExamReviewFile> examReviewFiles;
    List<ExamReviewFile> examRequestFiles;
    Boolean deleted;
    Instant timeCompletedAt;
    User user;
    UserExam userExam;

    public ExamReview(UserExam cmd, String code, List<String> fileIds) {
        this.id = IdUtils.nextId();
        this.code = code;
        this.userId = cmd.getUserId();
        this.examId = cmd.getExamId();
        this.roomId = cmd.getRoomId();
        this.periodId = cmd.getPeriodId();
        this.status = ExamReviewStatus.NEW;
        this.userExamId = cmd.getId();
        this.deleted = false;
        this.userExamCode = cmd.getCode();
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileIds.forEach(fileId -> {
                ExamReviewFile examReviewFile = new ExamReviewFile(this.id, fileId, ExamReviewFileType.REQUEST);
                this.examReviewFiles.add(examReviewFile);
            });
        }
    }

    public void updateStatus(ExamReviewStatus status) {
        this.status = status;
    }

    public void complete(String feedBack, List<String> fileIds) {
        this.feedBack = feedBack;
        this.status = ExamReviewStatus.DONE;
        this.examReviewFiles = new ArrayList<>();
        this.timeCompletedAt = Instant.now();
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileIds.forEach(fileId -> {
                ExamReviewFile examReviewFile = new ExamReviewFile(this.id, fileId, ExamReviewFileType.REVIEW);
                this.examReviewFiles.add(examReviewFile);
            });
        }
    }

    public void enrichFile(List<ExamReviewFile> examReviewFiles) {
        this.examReviewFiles = examReviewFiles.stream().filter(examReviewFile ->
                examReviewFile.getType().equals(ExamReviewFileType.REVIEW)).collect(Collectors.toList());
        this.examRequestFiles = examReviewFiles.stream().filter(examReviewFile ->
                examReviewFile.getType().equals(ExamReviewFileType.REQUEST)).collect(Collectors.toList());

    }

    public void enrichUser(User user) {
        this.user = user;
    }

    public void enrichUserExam(UserExam userExam) {
        this.userExam = userExam;
    }
}
