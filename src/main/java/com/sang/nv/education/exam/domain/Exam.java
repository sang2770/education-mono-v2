package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.ExamCreateCmd;
import com.sang.nv.education.exam.domain.command.ExamQuestionCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.ExamUpdateCmd;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

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
public class Exam extends AuditableDomain {
    String id;
    Float totalPoint;
    Integer numberQuestion;
    String periodId;
    String subjectId;
    String subjectName;
    String periodName;
    String name;
    String code;
    Boolean deleted;
    //  Thời gian làm bài tính bằng phút
    Long time;

    Long timeDelay;

    List<Question> questions;
    List<ExamQuestion> examQuestions;

    Boolean isStarted;

    public Exam(ExamCreateCmd cmd, List<Question> questions) {
        this.id = IdUtils.nextId();
        this.subjectName = cmd.getSubjectName();
        this.name = cmd.getName();
        this.totalPoint = cmd.getTotalPoint();
        this.periodId = cmd.getPeriodId();
        this.periodName = cmd.getPeriodName();
        this.subjectId = cmd.getSubjectId();
        this.deleted = Boolean.FALSE;
        this.time = cmd.getTime();
        this.timeDelay = cmd.getTimeDelay();
        this.code = cmd.getCode();
        this.examQuestions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmd.getQuestionIds())) {
            this.updateQuestion(cmd.getQuestionIds(), questions);
            this.calculatePoint();
        }
    }

    public void addQuestion(String questionId) {
        this.getExamQuestions()
                .add(new ExamQuestion(ExamQuestionCreateOrUpdateCmd.builder().examId(this.id).questionId(id).build()));
        this.calculatePoint();

    }

    public void removeQuestion(String questionId) {
        Optional<ExamQuestion> question = this.getExamQuestions().stream().filter(item -> Objects.equals(item.questionId, questionId)).findFirst();
        if (question.isEmpty()) {
            throw new ResponseException(NotFoundError.QUESTION_NOT_EXISTED);
        }
        this.getExamQuestions().remove(question.get());
        this.calculatePoint();
    }

    public void update(ExamUpdateCmd cmd, List<Question> questions) {
        this.totalPoint = cmd.getTotalPoint();
        this.periodId = cmd.getPeriodId();
        this.subjectId = cmd.getSubjectId();
        this.subjectName = cmd.getSubjectName();
        this.periodName = cmd.getPeriodName();
        this.name = cmd.getName();
        this.time = cmd.getTime();
        this.timeDelay = cmd.getTimeDelay();
        this.deleted = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(cmd.getQuestionIds())) {
            this.updateQuestion(cmd.getQuestionIds(), questions);
            this.calculatePoint();
        }
    }

    public void updateQuestion(List<String> questionIds, List<Question> questions) {
        this.examQuestions.forEach(ExamQuestion::deleted);
        questionIds.forEach(questionId -> {
            Optional<Question> question = questions.stream().filter(item -> Objects.equals(item.getId(), questionId) && Objects.equals(this.subjectId, item.subjectId)).findFirst();
            if (question.isEmpty()) {
                throw new ResponseException(NotFoundError.QUESTION_NOT_EXISTED);
            }
            Optional<ExamQuestion> optionalExamQuestion = this.examQuestions.stream()
                    .filter(item -> Objects.equals(item.getQuestionId(), questionId)).findFirst();
            if (optionalExamQuestion.isEmpty()) {
                this.examQuestions.add(new ExamQuestion(
                        ExamQuestionCreateOrUpdateCmd.builder().examId(this.id).questionId(questionId).build()));
            } else {
                optionalExamQuestion.get().unDelete();
            }
        });
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void enrichExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }

    public void calculatePoint() {
        Float point = this.totalPoint / this.examQuestions.size();
        this.examQuestions.forEach(examQuestion -> {
            examQuestion.updatePoint(point);
        });
        this.numberQuestion = this.examQuestions.size();
    }

    public void enrichIsStarted(Boolean isStarted) {
        this.isStarted = isStarted;
    }
}
