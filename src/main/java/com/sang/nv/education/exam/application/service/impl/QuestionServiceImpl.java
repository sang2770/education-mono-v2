package com.sang.nv.education.exam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.exam.application.dto.request.question.GroupQuestionRandomRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionCreateRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.QuestionUpdateRequest;
import com.sang.nv.education.exam.application.dto.response.ImportQuestionResult;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapper;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapperQuery;
import com.sang.nv.education.exam.application.service.ExamExcelService;
import com.sang.nv.education.exam.application.service.QuestionService;
import com.sang.nv.education.exam.domain.Answer;
import com.sang.nv.education.exam.domain.ExamQuestion;
import com.sang.nv.education.exam.domain.GroupQuestion;
import com.sang.nv.education.exam.domain.Question;
import com.sang.nv.education.exam.domain.command.QuestionCreateCmd;
import com.sang.nv.education.exam.domain.command.QuestionUpdateCmd;
import com.sang.nv.education.exam.domain.repository.QuestionDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.AnswerEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.GroupQuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.UserExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.AnswerEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamQuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.GroupQuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.QuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.query.QuestionSearchQuery;
import com.sang.nv.education.exam.infrastructure.persistence.repository.*;
import com.sang.nv.education.exam.infrastructure.support.enums.QuestionLevel;
import com.sang.nv.education.exam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionEntityRepository questionEntityRepository;
    private final GroupQuestionEntityRepository groupQuestionEntityRepository;
    private final ExamAutoMapper examAutoMapper;
    private final ExamAutoMapperQuery examAutoMapperQuery;
    private final QuestionDomainRepository questionDomainRepository;
    private final QuestionEntityMapper questionEntityMapper;
    private final GroupQuestionEntityMapper groupQuestionEntityMapper;

    private final AnswerEntityRepository answerEntityRepository;
    private final AnswerEntityMapper answerEntityMapper;
    private final ExamExcelService examExcelService;
    private final ExamQuestionEntityRepository examQuestionEntityRepository;
    private final ExamQuestionEntityMapper examQuestionEntityMapper;
    private final UserExamEntityRepository userExamEntityRepository;
    public QuestionServiceImpl(QuestionEntityRepository questionEntityRepository,
                               GroupQuestionEntityRepository groupQuestionEntityRepository, ExamAutoMapper examAutoMapper,
                               ExamAutoMapperQuery examAutoMapperQuery, QuestionDomainRepository questionDomainRepository,
                               QuestionEntityMapper questionEntityMapper,
                               GroupQuestionEntityMapper groupQuestionEntityMapper, AnswerEntityRepository answerEntityRepository, AnswerEntityMapper answerEntityMapper, ExamExcelService examExcelService, ExamQuestionEntityRepository examQuestionEntityRepository, ExamQuestionEntityMapper examQuestionEntityMapper, UserExamEntityRepository userExamEntityRepository) {
        this.questionEntityRepository = questionEntityRepository;
        this.groupQuestionEntityRepository = groupQuestionEntityRepository;
        this.examAutoMapper = examAutoMapper;
        this.examAutoMapperQuery = examAutoMapperQuery;
        this.questionDomainRepository = questionDomainRepository;
        this.questionEntityMapper = questionEntityMapper;
        this.groupQuestionEntityMapper = groupQuestionEntityMapper;
        this.answerEntityRepository = answerEntityRepository;
        this.answerEntityMapper = answerEntityMapper;
        this.examExcelService = examExcelService;
        this.examQuestionEntityRepository = examQuestionEntityRepository;
        this.examQuestionEntityMapper = examQuestionEntityMapper;
        this.userExamEntityRepository = userExamEntityRepository;
    }

    @Override
    public Question create(QuestionCreateRequest request) {
        QuestionCreateCmd cmd = this.examAutoMapper.from(request);
        GroupQuestionEntity groupQuestionEntity = this.findGroupById(request.getGroupId());
        cmd.setSubjectId(groupQuestionEntity.getSubjectId());
        Question Question = new Question(cmd);
        this.questionDomainRepository.save(Question);
        return Question;
    }

    @Override
    public Question update(String id, QuestionUpdateRequest request) {
        QuestionUpdateCmd cmd = this.examAutoMapper.from(request);
        Optional<Question> optionalQuestion = this.questionDomainRepository.findById(id);
        if (optionalQuestion.isEmpty()) {
            throw new ResponseException(NotFoundError.QUESTION_NOT_EXISTED);
        }
        this.findGroupById(request.getGroupId());
        Question Question = optionalQuestion.get();
        Question.update(cmd);
        this.questionDomainRepository.save(Question);
        return Question;
    }

    @Override
    public void delete(String id) {
        Question question = this.questionDomainRepository.getById(id);
        question.deleted();
        this.questionDomainRepository.save(question);
    }

    @Override
    public PageDTO<Question> search(QuestionSearchRequest request) {
        QuestionSearchQuery query = this.examAutoMapperQuery.from(request);
        Long count = this.questionEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<Question> questions = this.questionEntityMapper.toDomain(this.questionEntityRepository.search(query));
        this.enrichList(questions);
        return PageDTO.of(questions, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public Question getById(String id) {
        return this.questionDomainRepository.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.QUESTION_NOT_EXISTED));
    }

    @Override
    public List<Question> getRandomQuestionByGroup(GroupQuestionRandomRequest request) {
        GroupQuestion groupQuestion = this.groupQuestionEntityMapper.toDomain(this.findGroupById(request.getGroupId()));
        List<QuestionEntity> questionEntities = this.questionEntityRepository.findAllByGroupIds(List.of(groupQuestion.getId()));
        List<Question> questions = new ArrayList<>();
        if (Objects.nonNull(request.getNumberHighQuestion()) && request.getNumberHighQuestion() > 0) {
            questions.addAll(this.getQuestionByLevel(questionEntities, QuestionLevel.HIGH, request.getNumberHighQuestion()));
        }
        if (Objects.nonNull(request.getNumberMediumQuestion()) && request.getNumberMediumQuestion() > 0) {
            questions.addAll(this.getQuestionByLevel(questionEntities, QuestionLevel.MEDIUM, request.getNumberMediumQuestion()));
        }
        if (Objects.nonNull(request.getNumberLowQuestion()) && request.getNumberLowQuestion() > 0) {
            questions.addAll(this.getQuestionByLevel(questionEntities, QuestionLevel.LOW, request.getNumberLowQuestion()));
        }
        this.enrichQuestions(questions);
        return questions;
    }

    @Override
    public void downloadTemplateImportQuestions(HttpServletResponse response) {
        this.examExcelService.downloadQuestionTemplate(response);
    }

    @Override
    public ImportQuestionResult importQuestion(MultipartFile file, HttpServletResponse response) {
        return this.examExcelService.importQuestions(file, response);
    }

    private void enrichQuestions(List<Question> questions) {
        List<String> questionIds = questions.stream().map(Question::getId).collect(Collectors.toList());
        List<AnswerEntity> answerEntities = this.answerEntityRepository.findAllByQuestionIds(questionIds);
        questions.forEach(question -> {
            List<Answer> answers = this.answerEntityMapper.toDomain(
                    answerEntities.stream().filter(item ->
                            Objects.equals(item.getQuestionId(), question.getId())).collect(Collectors.toList())
            );
            question.enrichAnswers(answers);
        });
    }

    private List<Question> getQuestionByLevel(List<QuestionEntity> questionEntities, QuestionLevel questionLevel, Integer number) {
        List<QuestionEntity> questions = questionEntities.stream().filter(questionEntity ->
                Objects.equals(questionEntity.getLevel(), questionLevel)).collect(Collectors.toList());
        if (questions.size() < number) {
            switch (questionLevel) {
                case HIGH:
                    throw new ResponseException(BadRequestError.NUMBER_QUESTION_HIGH_INVALID);
                case MEDIUM:
                    throw new ResponseException(BadRequestError.NUMBER_QUESTION_MEDIUM_INVALID);
                case LOW:
                    throw new ResponseException(BadRequestError.NUMBER_QUESTION_INVALID);
            }
        } else if (questions.size() == number) {
            return this.questionEntityMapper.toDomain(questions);
        }

        return this.questionEntityMapper.toDomain(IntStream.generate(() -> new Random().nextInt(questions.size()))
                .limit(number)
                .mapToObj(questions::get)
                .collect(Collectors.toList()));
    }

    private GroupQuestionEntity findGroupById(String groupId) {
        Optional<GroupQuestionEntity> groupQuestionEntity = this.groupQuestionEntityRepository.findById(groupId);
        if (groupQuestionEntity.isEmpty()) {
            throw new ResponseException(NotFoundError.GROUP_NOT_EXISTED);
        }
        return groupQuestionEntity.get();
    }


    private void enrichList(List<Question> questionList) {
        List<String> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
        List<AnswerEntity> answerEntities = this.answerEntityRepository.findAllByQuestionIds(questionIds);
        questionList.forEach(question -> {
            List<Answer> answers = this.answerEntityMapper.toDomain(
                    answerEntities.stream().filter(item ->
                            Objects.equals(item.getQuestionId(), question.getId())).collect(Collectors.toList())
            );
            question.enrichAnswers(answers);
        });
        this.checkQuestionStarted(questionList);
    }

    private void checkQuestionStarted(List<Question> questions) {
        List<String> questionIds = questions.stream().map(Question::getId).collect(Collectors.toList());
        List<ExamQuestion> examQuestions = this.examQuestionEntityMapper.toDomain(this.examQuestionEntityRepository.findAllByQuestionIds(questionIds));
        this.checkExamIsStarted(examQuestions);
        questions.forEach(question -> {
            Optional<ExamQuestion> optionalExamQuestion = examQuestions.stream()
                    .filter(examQuestion -> Objects.equals(examQuestion.getQuestionId(), question.getId()))
                    .findFirst();
            if (optionalExamQuestion.isPresent()) {
                question.enrichIsStarted(true);
            }
        });
    }
    private void checkExamIsStarted(List<ExamQuestion> examQuestions) {
        List<String> examIds = examQuestions.stream().map(ExamQuestion::getId).collect(Collectors.toList());
        List<UserExamEntity> userExamEntities = this.userExamEntityRepository.findAllByExamId(examIds);
        examQuestions.forEach(exam -> {
            Optional<UserExamEntity> optionalUserExamEntity = userExamEntities.stream()
                    .filter(userExamEntity -> Objects.equals(userExamEntity.getExamId(), exam.getId()))
                    .findFirst();
            if (optionalUserExamEntity.isPresent()) {
                exam.enrichIsStarted(true);
            }
        });
    }


}
