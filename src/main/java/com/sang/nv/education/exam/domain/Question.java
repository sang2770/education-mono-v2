package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.AnswerCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.QuestionCreateCmd;
import com.sang.nv.education.exam.domain.command.QuestionUpdateCmd;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.support.enums.QuestionLevel;
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
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Question extends AuditableDomain {
    String id;
    String groupId;
    String subjectId;
    String title;
    Boolean deleted;
    QuestionLevel level;
    List<Answer> answers;
    List<String> questionFileIds;
    List<QuestionFile> questionFiles;


    public Question(QuestionCreateCmd cmd) {
        this.id = IdUtils.nextId();
        this.title = cmd.getTitle();
        this.deleted = Boolean.FALSE;
        this.groupId = cmd.getGroupId();
        this.answers = new ArrayList<>();
        this.subjectId = cmd.getSubjectId();
        this.level = cmd.getQuestionLevel();
        if (!CollectionUtils.isEmpty(cmd.getQuestionFileIds())) {
            this.questionFileIds = cmd.getQuestionFileIds();
            this.questionFiles = cmd.getQuestionFileIds().stream().map(item -> new QuestionFile(this.id, item)).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(cmd.getAnswerCreateOrUpdateCmdList())) {
            this.answers = cmd.getAnswerCreateOrUpdateCmdList().stream().map(item -> new Answer(this.id, item)).collect(Collectors.toList());
        }
    }

    public Question(QuestionEntity cmd) {
        this.title = cmd.getTitle();
        this.groupId = cmd.getId();
        this.deleted = Boolean.FALSE;
    }

    public void update(QuestionUpdateCmd cmd) {
        this.title = cmd.getTitle();
        this.deleted = Boolean.FALSE;
        this.answers.forEach(Answer::deleted);
        this.level = cmd.getQuestionLevel();
        cmd.getAnswers().forEach(item -> {
            Optional<Answer> optionalAnswer = this.answers.stream().filter(answer -> Objects.equals(item.id, answer.id)).findFirst();
            if (optionalAnswer.isPresent()) {
                Answer answer = optionalAnswer.get();
                answer.unDelete();
                answer.update(AnswerCreateOrUpdateCmd.builder().content(item.content).status(item.status).build());
            }
        });
        if (!CollectionUtils.isEmpty(cmd.getExtraAnswer())) {
            cmd.getExtraAnswer().forEach(item -> {
                this.answers.add(new Answer(this.id, item));
            });
        }
        if (!CollectionUtils.isEmpty(cmd.getQuestionFileIds())) {
            this.questionFileIds = cmd.getQuestionFileIds();
            this.questionFiles.forEach(QuestionFile::deleted);
            cmd.getQuestionFileIds().stream().forEach(item -> {
                Optional<QuestionFile> optionalQuestionFile = this.questionFiles.stream().filter(questionFile -> Objects.equals(item, questionFile.fileId)).findFirst();
                if (optionalQuestionFile.isPresent()) {
                    QuestionFile questionFile = optionalQuestionFile.get();
                    questionFile.unDelete();
                } else {
                    this.questionFiles.add(new QuestionFile(this.id, item));
                }
            });
        }
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void enrichFile(List<QuestionFile> questionFiles) {
        this.questionFileIds = questionFiles.stream().map(QuestionFile::getFileId).collect(Collectors.toList());
        this.questionFiles = questionFiles;
    }
}
