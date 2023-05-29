package com.sang.nv.education.exam.infrastructure.domainrepository;


import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.exam.domain.Answer;
import com.sang.nv.education.exam.domain.Question;
import com.sang.nv.education.exam.domain.QuestionFile;
import com.sang.nv.education.exam.domain.repository.QuestionDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.AnswerEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionFileEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.AnswerEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.QuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.QuestionFileEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.AnswerEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.QuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.QuestionFileEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import com.sang.nv.education.storage.application.service.StorageService;
import com.sang.nv.education.storage.domain.FileDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionDomainRepositoryImpl extends AbstractDomainRepository<Question, QuestionEntity, String> implements QuestionDomainRepository {
    private final QuestionEntityRepository questionEntityRepository;
    private final QuestionEntityMapper questionEntityMapper;
    private final AnswerEntityMapper answerEntityMapper;
    private final AnswerEntityRepository answerEntityRepository;
    private final QuestionFileEntityRepository questionFileEntityRepository;
    private final QuestionFileEntityMapper questionFileEntityMapper;
    private final StorageService storageService;

    public QuestionDomainRepositoryImpl(QuestionEntityRepository questionEntityRepository,
                                        QuestionEntityMapper questionEntityMapper, AnswerEntityMapper answerEntityMapper, AnswerEntityRepository answerEntityRepository, QuestionFileEntityRepository questionFileEntityRepository, QuestionFileEntityMapper questionFileEntityMapper, StorageService storageService) {
        super(questionEntityRepository, questionEntityMapper);
        this.questionEntityRepository = questionEntityRepository;
        this.questionEntityMapper = questionEntityMapper;
        this.answerEntityMapper = answerEntityMapper;
        this.answerEntityRepository = answerEntityRepository;
        this.questionFileEntityRepository = questionFileEntityRepository;
        this.questionFileEntityMapper = questionFileEntityMapper;
        this.storageService = storageService;
    }

    @Override
    public Question getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.QUESTION_NOT_EXISTED));
    }

    @Override
    public List<Question> getByIds(List<String> ids) {
        List<Question> questions = this.findAllByIds(ids);
        return this.enrichList(questions);
    }


    @Override
    protected List<Question> enrichList(List<Question> questionList) {
        List<String> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
        List<AnswerEntity> answerEntities = this.answerEntityRepository.findAllByQuestionIds(questionIds);
        questionList.forEach(question -> {
            List<Answer> answers = this.answerEntityMapper.toDomain(
                    answerEntities.stream().filter(item ->
                            Objects.equals(item.getQuestionId(), question.getId())).collect(Collectors.toList())
            );
            question.enrichAnswers(answers);
        });
        List<QuestionFile> questionFiles = this.questionFileEntityMapper.toDomain(this.questionFileEntityRepository.findAllByQuestionIds(questionIds));
        List<String> fileIds = questionFiles.stream().map(QuestionFile::getFileId).collect(Collectors.toList());
        List<FileDomain> fileDomains = this.storageService.getByIds(fileIds);
        questionFiles.forEach(questionFile -> {
            FileDomain fileDomain = fileDomains.stream().filter(item ->
                    Objects.equals(item.getId(), questionFile.getFileId())).findFirst().orElse(null);
            if (Objects.nonNull(fileDomain)) {
                questionFile.enrichViewUrl(fileDomain.getFilePath());
                questionFile.enrichFileName(fileDomain.getFileName());
            }
        });
        questionList.forEach(question -> {
            List<QuestionFile> fileList = questionFiles.stream().filter(item ->
                    Objects.equals(item.getQuestionId(), question.getId())).collect(Collectors.toList());
            question.enrichFile(fileList);
        });
        return questionList;
    }

    @Override
    public Question save(Question domain) {
        // save answer
        if (!CollectionUtils.isEmpty(domain.getAnswers())) {
            List<AnswerEntity> answerEntities = this.answerEntityMapper.toEntity(domain.getAnswers());
            this.answerEntityRepository.saveAll(answerEntities);
        }
        // save question file
        if (!CollectionUtils.isEmpty(domain.getQuestionFiles())) {
            List<QuestionFileEntity> questionFileEntities = this.questionFileEntityMapper.toEntity(domain.getQuestionFiles());
            this.questionFileEntityRepository.saveAll(questionFileEntities);
        }
        return super.save(domain);
    }

    @Override
    public List<Question> saveAll(List<Question> domains) {
        domains.forEach(question -> {
            // save answer
            if (!CollectionUtils.isEmpty(question.getAnswers())) {
                List<AnswerEntity> answerEntities = this.answerEntityMapper.toEntity(question.getAnswers());
                this.answerEntityRepository.saveAll(answerEntities);
            }
        });
        return super.saveAll(domains);
    }

    @Override
    protected Question enrich(Question question) {
        // enrich answer
        List<AnswerEntity> answerEntities = this.answerEntityRepository.findByQuestionId(question.getId());
        question.enrichAnswers(this.answerEntityMapper.toDomain(answerEntities));
        List<QuestionFile> questionFiles = this.questionFileEntityMapper.toDomain(this.questionFileEntityRepository.findAllByQuestionIds(List.of(question.getId())));
        List<String> fileIds = questionFiles.stream().map(QuestionFile::getFileId).collect(Collectors.toList());
        List<FileDomain> fileDomains = this.storageService.getByIds(fileIds);
        questionFiles.forEach(questionFile -> {
            FileDomain fileDomain = fileDomains.stream().filter(item ->
                    Objects.equals(item.getId(), questionFile.getFileId())).findFirst().orElse(null);
            if (Objects.nonNull(fileDomain)) {
                questionFile.enrichViewUrl(fileDomain.getFilePath());
                questionFile.enrichFileName(fileDomain.getFileName());
            }
        });
        question.enrichFile(questionFiles);
        return super.enrich(question);
    }


}

