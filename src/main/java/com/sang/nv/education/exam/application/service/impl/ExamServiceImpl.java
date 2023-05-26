package com.sang.nv.education.exam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.nv.education.exam.application.dto.request.exam.ExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.exam.ExamSearchRequest;
import com.sang.nv.education.exam.application.dto.request.exam.ExamUpdateRequest;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapper;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapperQuery;
import com.sang.nv.education.exam.application.service.ExamService;
import com.sang.nv.education.exam.domain.Exam;
import com.sang.nv.education.exam.domain.PeriodRoom;
import com.sang.nv.education.exam.domain.Question;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.command.ExamCreateCmd;
import com.sang.nv.education.exam.domain.repository.ExamDomainRepository;
import com.sang.nv.education.exam.domain.repository.RoomDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamQuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.PeriodEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.UserExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.QuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.query.ExamSearchQuery;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamQuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.GroupQuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.PeriodEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.PeriodRoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.QuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.SubjectEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.UserExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.BadRequestError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamEntityRepository ExamEntityRepository;
    private final GroupQuestionEntityRepository groupQuestionEntityRepository;
    private final QuestionEntityRepository questionEntityRepository;
    private final ExamQuestionEntityRepository examQuestionEntityRepository;
    private final ExamAutoMapper examAutoMapper;
    private final QuestionEntityMapper questionEntityMapper;
    private final ExamAutoMapperQuery examAutoMapperQuery;
    private final ExamDomainRepository examDomainRepository;
    private final ExamEntityRepository examEntityRepository;
    private final PeriodEntityRepository periodEntityRepository;
    private final PeriodRoomEntityRepository periodRoomEntityRepository;
    private final ExamEntityMapper ExamEntityMapper;
    private final RoomDomainRepository roomDomainRepository;
    private final SubjectEntityRepository subjectEntityRepository;
    private final SeqRepository seqRepository;
    private final UserExamEntityRepository userExamEntityRepository;

    public ExamServiceImpl(ExamEntityRepository ExamEntityRepository,
                           GroupQuestionEntityRepository GroupQuestionEntityRepository,
                           QuestionEntityRepository questionEntityRepository,
                           ExamQuestionEntityRepository examQuestionEntityRepository, ExamAutoMapper examAutoMapper,
                           QuestionEntityMapper questionEntityMapper, ExamAutoMapperQuery examAutoMapperQuery, ExamDomainRepository ExamDomainRepository,
                           com.sang.nv.education.exam.infrastructure.persistence.repository.ExamEntityRepository examEntityRepository, PeriodEntityRepository periodEntityRepository, PeriodRoomEntityRepository periodRoomEntityRepository, ExamEntityMapper ExamEntityMapper, RoomDomainRepository roomDomainRepository, SubjectEntityRepository subjectEntityRepository, SeqRepository seqRepository, UserExamEntityRepository userExamEntityRepository) {
        this.ExamEntityRepository = ExamEntityRepository;
        this.groupQuestionEntityRepository = GroupQuestionEntityRepository;
        this.questionEntityRepository = questionEntityRepository;
        this.examQuestionEntityRepository = examQuestionEntityRepository;
        this.examAutoMapper = examAutoMapper;
        this.questionEntityMapper = questionEntityMapper;
        this.examAutoMapperQuery = examAutoMapperQuery;
        this.examDomainRepository = ExamDomainRepository;
        this.examEntityRepository = examEntityRepository;
        this.periodEntityRepository = periodEntityRepository;
        this.periodRoomEntityRepository = periodRoomEntityRepository;
        this.ExamEntityMapper = ExamEntityMapper;
        this.roomDomainRepository = roomDomainRepository;
        this.subjectEntityRepository = subjectEntityRepository;
        this.seqRepository = seqRepository;
        this.userExamEntityRepository = userExamEntityRepository;
    }

    @Override
    public Exam create(ExamCreateRequest request) {
        Optional<PeriodEntity> periodEntityOptional = this.periodEntityRepository.findById(request.getPeriodId());
        if (periodEntityOptional.isEmpty()) {
            throw new ResponseException(BadRequestError.PERIOD_NOT_EXISTED);
        }
        Optional<SubjectEntity> subjectEntityOptional = this.subjectEntityRepository.findById(request.getSubjectId());
        if (subjectEntityOptional.isEmpty()) {
            throw new ResponseException(BadRequestError.SUBJECT_NOT_EXISTED);
        }
        List<Question> questions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getQuestionIds())) {
            List<QuestionEntity> questionEntities = this.questionEntityRepository.findAllById(request.getQuestionIds());
            questions.addAll(this.questionEntityMapper.toDomain(questionEntities));
        }

        ExamCreateCmd cmd = this.examAutoMapper.from(request);
        cmd.setCode(this.seqRepository.generateUserExamCode());
        cmd.setPeriodName(periodEntityOptional.get().getName());
        cmd.setSubjectName(subjectEntityOptional.get().getName());
        Exam Exam = new Exam(cmd, questions);
        this.examDomainRepository.save(Exam);
        return Exam;
    }

    @Override
    public Exam update(String id, ExamUpdateRequest request) {
        Exam exam = this.getById(id);
        List<QuestionEntity> questionEntities = this.questionEntityRepository.findAllById(request.getQuestionIds());
        exam.update(this.examAutoMapper.from(request), this.questionEntityMapper.toDomain(questionEntities));
        return exam;
    }

    @Override
    public PageDTO<Exam> search(ExamSearchRequest request) {
        ExamSearchQuery query = this.examAutoMapperQuery.from(request);
        Long count = this.ExamEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<Exam> exams = this.ExamEntityMapper.toDomain(this.ExamEntityRepository.search(query));
        this.checkExamIsStarted(exams);
        return PageDTO.of(exams, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public Exam getById(String id) {
        return this.examDomainRepository.getById(id);
    }

    @Override
    public PageDTO<Exam> getByRoomId(String roomId, ExamSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        Room room = this.roomDomainRepository.getById(roomId);
        List<PeriodRoom> periodRooms = room.getPeriodRooms();
        List<String> periodIds = periodRooms.stream().map(PeriodRoom::getPeriodId).collect(Collectors.toList());
        Page<ExamEntity> examEntities = this.examEntityRepository.searchByPeriods(periodIds, room.getSubjectId(), pageable);
        List<Exam> exams = this.ExamEntityMapper.toDomain(examEntities.getContent());
        return PageDTO.of(exams, request.getPageIndex(), request.getPageSize(), examEntities.getTotalElements());
    }

    @Override
    public void addQuestionToExam(String examId, String questionId) {
        Exam exam = this.getById(examId);
        Optional<ExamQuestionEntity> optionalExamQuestion = this.examQuestionEntityRepository.findByExamIdAndQuestionId(examId, questionId);
        if (optionalExamQuestion.isPresent()) {
            throw new ResponseException(BadRequestError.QUESTION_IS_IN_EXAM);
        }
        exam.addQuestion(questionId);
        this.examDomainRepository.save(exam);
    }

    @Override
    public void removeQuestionToExam(String examId, String questionId) {
        Exam exam = this.getById(examId);
        Optional<ExamQuestionEntity> optionalExamQuestion = this.examQuestionEntityRepository.findByExamIdAndQuestionId(examId, questionId);
        if (optionalExamQuestion.isEmpty()) {
            throw new ResponseException(BadRequestError.QUESTION_IS_NOT_IN_EXAM);
        }
        exam.removeQuestion(questionId);
        this.examDomainRepository.save(exam);
    }

    private void generateQuestion() {
        //        request.getExamGroupQuestionRequests().forEach(examGroupQuestionRequest -> {
//            List<QuestionEntity> questionEntities = this.questionEntityRepository.findRandomQuestions(
//                    examGroupQuestionRequest.getGroupQuestionId(), examGroupQuestionRequest.getNumberQuestion());
//            if (Objects.equals(questionEntities.size(), examGroupQuestionRequest.getNumberQuestion()))
//            {
//                throw new ResponseException(BadRequestError.NUMBER_QUESTION_INVALID);
//            }
//            questionIds.addAll(questionEntities.stream().map(QuestionEntity::getId).collect(Collectors.toList()));
//        });
//        List<QuestionEntity> questionEntities = this.questionEntityRepository.findAllByGroupIds(groupIds);
//        request.getExamGroupQuestionRequests().forEach(item -> {
//            List<QuestionEntity> entities = questionEntities.stream()
//                    .filter(questionEntity -> Objects.equals(questionEntity.getGroupId(), item.getGroupQuestionId()))
//                    .collect(Collectors.toList());
//            List<QuestionEntity> entityListValid = IntStream.generate(() -> new Random().nextInt(entities.size()))
//                    .limit(item.getNumberQuestion())
//                    .mapToObj(entities::get)
//                    .collect(Collectors.toList());
//            if (Objects.equals(entityListValid.size(), item.getNumberQuestion())) {
//                throw new ResponseException(BadRequestError.NUMBER_QUESTION_INVALID);
//            }
//            questionIds.addAll(entityListValid.stream().map(QuestionEntity::getId).collect(Collectors.toList()));
//        });
    }

    @Override
    public Integer countExam(List<String> roomIds) {
        List<PeriodEntity> periodEntities = this.periodEntityRepository.findAllByRoomIds(roomIds);
        List<String> periodIds = periodEntities.stream().map(PeriodEntity::getId).collect(Collectors.toList());
        return this.examEntityRepository.countByPeriodIds(periodIds);
    }

    private void checkExamIsStarted(List<Exam> exams) {
        List<String> examIds = exams.stream().map(Exam::getId).collect(Collectors.toList());
        List<UserExamEntity> userExamEntities = this.userExamEntityRepository.findAllByExamId(examIds);
        exams.forEach(exam -> {
            Optional<UserExamEntity> optionalUserExamEntity = userExamEntities.stream()
                    .filter(userExamEntity -> Objects.equals(userExamEntity.getExamId(), exam.getId()))
                    .findFirst();
            if (optionalUserExamEntity.isPresent()) {
                exam.enrichIsStarted(true);
            }
        });
    }

    @Override
    public Integer countExamByPeriodIds(List<String> periodIds) {
        return this.examEntityRepository.countByPeriodIds(periodIds);
    }
}
