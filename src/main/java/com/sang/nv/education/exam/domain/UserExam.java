package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.IdUtils;
import com.sang.commonutil.StrUtils;
import com.sang.nv.education.exam.application.dto.request.UserExamInfoCreateRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.domain.command.UserExamCreateCmd;
import com.sang.nv.education.exam.domain.command.UserExamInfoCreateCmd;
import com.sang.nv.education.exam.infrastructure.support.enums.UserExamStatus;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
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
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class UserExam extends AuditableDomain {
    String id;
    String code;
    Float totalPoint;
    Float maxPoint;
    Instant timeEnd;
    Instant timeStart;
    Boolean deleted;
    String examId;
    String periodId;
    String roomId;
    String userId;
    Integer numberOutTab;
    UserExamStatus status;
    Long timeDelay;
    Long time;
    List<UserExamInfo> userExamInfos;

    Exam exam;
    UserExamResult userExamResult;
    User user;

    Room room;
    Period period;

    public UserExam(UserExamCreateCmd cmd) {
        this.id = IdUtils.nextId();
//        this.timeEnd = cmd.getTimeEnd();
//        this.timeStart = cmd.getTimeStart();
        this.maxPoint = cmd.getMaxPoint();
        this.examId = cmd.getExamId();
        this.code = cmd.getCode();
        this.roomId = cmd.getRoomId();
        this.periodId = cmd.getPeriodId();
        this.userExamInfos = new ArrayList<>();
        this.totalPoint = 0f;
        this.status = UserExamStatus.WAITING;
        this.timeDelay = cmd.getTimeDelay();
        this.time = cmd.getTime();
//        this.timeEnd = Instant.now();
        this.deleted = Boolean.FALSE;
        this.numberOutTab = 0;
        this.userId = cmd.getUserId();
    }

    public UserExam(UserExamCreateCmd cmd, List<ExamQuestion> examQuestions) {
        this.id = IdUtils.nextId();
        this.timeEnd = cmd.getTimeEnd();
        this.timeStart = cmd.getTimeStart();
        this.examId = cmd.getExamId();
        this.roomId = cmd.getRoomId();
        this.userId = cmd.getUserId();
        this.userExamInfos = new ArrayList<>();
        this.totalPoint = 0f;
        this.deleted = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(cmd.getUserExamInfoCreateRequests())) {
            this.validateExam(cmd.getUserExamInfoCreateRequests(), examQuestions);
        }
    }

    public void update(UserExamCreateCmd cmd, List<ExamQuestion> examQuestions) {
        this.timeEnd = Instant.now();
        this.status = UserExamStatus.DONE;
        this.numberOutTab = cmd.getNumberOutTab();
        this.totalPoint = 0f;
        if (!CollectionUtils.isEmpty(cmd.getUserExamInfoCreateRequests())) {
            this.validateExam(cmd.getUserExamInfoCreateRequests(), examQuestions);
        }
    }

    public void validateExam(List<UserExamInfoCreateRequest> userExamInfoCreateRequests, List<ExamQuestion> examQuestions) {
        userExamInfoCreateRequests.forEach(userExamInfoCreateRequest -> {
            Optional<ExamQuestion> optionalExamQuestion = examQuestions.stream().filter(question ->
                    Objects.equals(question.getQuestionId(), userExamInfoCreateRequest.getQuestionId())).findFirst();
            if (optionalExamQuestion.isEmpty()) {
                throw new ResponseException(NotFoundError.QUESTION_NOT_EXISTED);
            }
            Boolean statusExam = Boolean.FALSE;
            ExamQuestion examQuestion = optionalExamQuestion.get();
            if (Objects.nonNull(userExamInfoCreateRequest.getAnswerId()) && !StrUtils.isBlank(userExamInfoCreateRequest.getAnswerId())) {
                Optional<Answer> optionalAnswer = examQuestion.getQuestion().answers.stream().filter(answer ->
                        Objects.equals(answer.id, userExamInfoCreateRequest.getAnswerId())).findFirst();
                if (optionalAnswer.isEmpty()) {
                    throw new ResponseException(NotFoundError.ANSWER_NOT_EXISTED);
                }
                Answer answer = optionalAnswer.get();
                if (Boolean.TRUE.equals(answer.getStatus())) {
                    this.totalPoint += examQuestion.point;
                }
                statusExam = answer.getStatus();
            }

            this.userExamInfos.add(new UserExamInfo(UserExamInfoCreateCmd.builder()
                    .answerId(userExamInfoCreateRequest.getAnswerId())
                    .questionId(userExamInfoCreateRequest.getQuestionId())
                    .status(statusExam)
                    .point(examQuestion.getPoint())
                    .userExamId(this.id)
                    .build()));
        });
    }

    public void startTesting() {
        this.timeStart = Instant.now();
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichUserExamInfo(List<UserExamInfo> userExamInfos) {
        this.userExamInfos = userExamInfos;
    }

    public void updateStatus(UserExamStatus status) {
        this.status = status;
    }

    public void enrichUser(User user) {
        this.user = user;
    }

    public void enrichRoom(Room room) {
        this.room = room;
    }

    public void enrichPeriod(Period period) {
        this.period = period;
    }

    public void enrichExam(Exam exam) {
        this.exam = exam;
    }

    public void enrichUserExamResult(UserExamResult userExamResult) {
        this.userExamResult = userExamResult;
    }

    public void overTimeExam() {
        this.status = UserExamStatus.OVERTIME;
        this.timeEnd = Instant.now();
        this.totalPoint = 0f;
    }
}
