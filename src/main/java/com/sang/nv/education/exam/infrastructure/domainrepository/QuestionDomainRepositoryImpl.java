package com.sang.nv.education.exam.infrastructure.domainrepository;


import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonweb.support.AbstractDomainRepository;
import com.sang.nv.education.exam.domain.Answer;
import com.sang.nv.education.exam.domain.Question;
import com.sang.nv.education.exam.domain.repository.QuestionDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.AnswerEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.AnswerEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.QuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.AnswerEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.QuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
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

    public QuestionDomainRepositoryImpl(QuestionEntityRepository questionEntityRepository,
                                        QuestionEntityMapper questionEntityMapper, AnswerEntityMapper answerEntityMapper, AnswerEntityRepository answerEntityRepository) {
        super(questionEntityRepository, questionEntityMapper);
        this.questionEntityRepository = questionEntityRepository;
        this.questionEntityMapper = questionEntityMapper;
        this.answerEntityMapper = answerEntityMapper;
        this.answerEntityRepository = answerEntityRepository;
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
        return questionList;
    }

    @Override
    public Question save(Question domain) {
        // save answer
        if (!CollectionUtils.isEmpty(domain.getAnswers()))
        {
            List<AnswerEntity> answerEntities = this.answerEntityMapper.toEntity(domain.getAnswers());
            this.answerEntityRepository.saveAll(answerEntities);
        }
        return super.save(domain);
    }

    @Override
    public List<Question> saveAll(List<Question> domains) {
        domains.forEach(question -> {
            // save answer
            if (!CollectionUtils.isEmpty(question.getAnswers()))
            {
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
        return super.enrich(question);
    }


}

