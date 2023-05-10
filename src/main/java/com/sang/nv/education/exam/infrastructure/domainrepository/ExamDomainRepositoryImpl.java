package com.sang.nv.education.exam.infrastructure.domainrepository;


import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.exam.domain.Exam;
import com.sang.nv.education.exam.domain.ExamQuestion;
import com.sang.nv.education.exam.domain.Question;
import com.sang.nv.education.exam.domain.repository.ExamDomainRepository;
import com.sang.nv.education.exam.domain.repository.QuestionDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamQuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamQuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamQuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamDomainRepositoryImpl extends AbstractDomainRepository<Exam, ExamEntity, String> implements ExamDomainRepository {
    private final ExamEntityRepository examEntityRepository;
    private final ExamEntityMapper examEntityMapper;
    private final ExamQuestionEntityMapper examQuestionEntityMapper;
    private final ExamQuestionEntityRepository examQuestionEntityRepository;
    private final QuestionDomainRepository questionDomainRepository;

    public ExamDomainRepositoryImpl(ExamEntityRepository ExamEntityRepository,
                                    ExamEntityMapper ExamEntityMapper,
                                    ExamQuestionEntityMapper examQuestionEntityMapper,
                                    ExamQuestionEntityRepository examQuestionEntityRepository,
                                    QuestionDomainRepository questionDomainRepository) {
        super(ExamEntityRepository, ExamEntityMapper);
        this.examEntityRepository = ExamEntityRepository;
        this.examEntityMapper = ExamEntityMapper;
        this.examQuestionEntityMapper = examQuestionEntityMapper;
        this.examQuestionEntityRepository = examQuestionEntityRepository;
        this.questionDomainRepository = questionDomainRepository;
    }

    @Override
    public Exam getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.EXAM_NOT_EXISTED));
    }

    @Override
    public Exam save(Exam domain) {
        if (!CollectionUtils.isEmpty(domain.getExamQuestions())) {
            List<ExamQuestionEntity> examQuestionEntities = this.examQuestionEntityMapper.toEntity(domain.getExamQuestions());
            this.examQuestionEntityRepository.saveAll(examQuestionEntities);
        }
        return super.save(domain);
    }

    @Override
    public List<Exam> saveAll(List<Exam> domains) {
        domains.forEach(exam -> {
            if (!CollectionUtils.isEmpty(exam.getExamQuestions())) {
                List<ExamQuestionEntity> examQuestionEntities = this.examQuestionEntityMapper.toEntity(exam.getExamQuestions());
                this.examQuestionEntityRepository.saveAll(examQuestionEntities);
            }
        });
        return super.saveAll(domains);
    }

    @Override
    protected Exam enrich(Exam exam) {
        // enrich answer
        List<ExamQuestion> examQuestions = this.examQuestionEntityMapper.toDomain(
                this.examQuestionEntityRepository.findByExamId(exam.getId()));
        List<String> questionIds = examQuestions.stream().map(ExamQuestion::getQuestionId).collect(Collectors.toList());
        List<Question> questions = this.questionDomainRepository.getByIds(questionIds);
        if (!CollectionUtils.isEmpty(examQuestions)) {
            examQuestions.forEach(examQuestion -> {
                Question question = questions.stream().filter(item -> Objects.equals(examQuestion.getQuestionId(), item.getId())).findFirst().orElse(null);
                examQuestion.enrichQuestion(question);
            });
        }
        exam.enrichExamQuestions(examQuestions);
        exam.enrichQuestions(questions);
        return exam;
    }
}

