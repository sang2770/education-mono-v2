package com.sang.nv.education.exam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.commonpersistence.support.SqlUtils;
import com.sang.nv.education.common.web.support.SecurityUtils;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamReviewDoneRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapper;
import com.sang.nv.education.exam.application.service.UserExamService;
import com.sang.nv.education.exam.domain.*;
import com.sang.nv.education.exam.domain.command.UserExamCreateCmd;
import com.sang.nv.education.exam.domain.repository.ExamDomainRepository;
import com.sang.nv.education.exam.domain.repository.ExamReviewDomainRepository;
import com.sang.nv.education.exam.domain.repository.UserExamDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.*;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.PeriodEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.PeriodRoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.RoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.UserExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.*;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewStatus;
import com.sang.nv.education.exam.infrastructure.support.enums.UserExamStatus;
import com.sang.nv.education.exam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import com.sang.nv.education.report.application.dto.request.UserExamReportRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class UserExamServiceImpl implements UserExamService {
    private final ExamEntityRepository ExamEntityRepository;
    private final RoomEntityRepository roomEntityRepository;
    private final RoomEntityMapper roomEntityMapper;
    private final ExamQuestionEntityRepository examQuestionEntityRepository;
    private final ExamAutoMapper examAutoMapper;
    private final ExamDomainRepository examDomainRepository;
    private final UserExamDomainRepository userExamDomainRepository;
    private final UserExamEntityRepository userExamEntityRepository;
    private final UserExamEntityMapper userExamEntityMapper;
    private final PeriodRoomEntityRepository periodRoomEntityRepository;
    private final PeriodRoomEntityMapper periodRoomEntityMapper;
    private final ExamReviewDomainRepository examReviewDomainRepository;
    private final SeqRepository seqRepository;
    private final PeriodEntityMapper periodEntityMapper;
    private final PeriodEntityRepository periodEntityRepository;
    public UserExamServiceImpl(ExamEntityRepository ExamEntityRepository,
                               QuestionEntityRepository questionEntityRepository,
                               RoomEntityRepository roomEntityRepository, RoomEntityMapper roomEntityMapper, ExamQuestionEntityRepository examQuestionEntityRepository,
                               ExamAutoMapper examAutoMapper,
                               ExamDomainRepository ExamDomainRepository, UserExamDomainRepository userExamDomainRepository, UserExamEntityRepository examEntityRepository, UserExamEntityMapper userExamEntityMapper, PeriodRoomEntityRepository periodRoomEntityRepository, PeriodRoomEntityMapper periodRoomEntityMapper, ExamReviewDomainRepository examReviewDomainRepository, SeqRepository seqRepository, PeriodEntityMapper periodEntityMapper, PeriodEntityRepository periodEntityRepository) {
        this.ExamEntityRepository = ExamEntityRepository;
        this.roomEntityRepository = roomEntityRepository;
        this.roomEntityMapper = roomEntityMapper;
        this.examQuestionEntityRepository = examQuestionEntityRepository;
        this.examAutoMapper = examAutoMapper;
        this.examDomainRepository = ExamDomainRepository;
        this.userExamDomainRepository = userExamDomainRepository;
        this.userExamEntityRepository = examEntityRepository;
        this.userExamEntityMapper = userExamEntityMapper;
        this.periodRoomEntityRepository = periodRoomEntityRepository;
        this.periodRoomEntityMapper = periodRoomEntityMapper;
        this.examReviewDomainRepository = examReviewDomainRepository;
        this.seqRepository = seqRepository;
        this.periodEntityMapper = periodEntityMapper;
        this.periodEntityRepository = periodEntityRepository;
    }

    @Override
    public UserExamResult send(String roomId, String id, UserExamCreateRequest request) {
        UserExamCreateCmd cmd = this.examAutoMapper.from(request);
        Exam exam = this.examDomainRepository.getById(request.getExamId());
        UserExam userExam = this.userExamDomainRepository.getById(id);
        if (Objects.equals(userExam.getStatus(), UserExamStatus.DONE)) {
            throw new ResponseException(BadRequestError.USER_EXAM_FINISHED);
        }
        Optional<RoomEntity> roomEntityOptional = this.roomEntityRepository.findById(roomId);
        if (roomEntityOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.ROOM_NOT_FOUND);
        }
        userExam.update(cmd, exam.getExamQuestions());
//        UserExam userExam = new UserExam(cmd, exam.getExamQuestions());
        this.userExamDomainRepository.save(userExam);

        // check period Done
        PeriodRoomEntity periodRoomEntity = this.periodRoomEntityRepository.findByRoomIdAndPeriodId(roomId, request.getPeriodId())
                .orElseThrow(() -> new ResponseException(NotFoundError.PERIOD_NOT_EXISTED_IN_ROOM));
        PeriodRoom periodRoom = this.periodRoomEntityMapper.toDomain(periodRoomEntity);
        List<UserExam> userExams = this.userExamEntityMapper.toDomain(this.userExamEntityRepository.findByRoomAndPeriod(roomId, request.getPeriodId()));
        UserExam userExamDone = userExams.stream().filter(item -> Objects.equals(userExam.getId(), item.getId())).findFirst().orElse(null);
        if (Objects.nonNull(userExamDone)) {
            userExamDone.updateStatus(UserExamStatus.DONE);
        }
        Boolean isDone = true;
        for (UserExam item : userExams) {
            if (!item.getStatus().equals(UserExamStatus.DONE)) {
                isDone = false;
                break;
            }
        }
        periodRoom.setIsDone(isDone);
        this.periodRoomEntityRepository.save(this.periodRoomEntityMapper.toEntity(periodRoom));
        // End Period Done

        Long duration = 0L;
        if (Objects.nonNull(userExam.getTimeStart()) && Objects.nonNull(userExam.getTimeEnd())) {
            duration = Duration.between(userExam.getTimeStart(), userExam.getTimeEnd()).toSeconds();
        }
        return UserExamResult.builder()
                .userId(userExam.getUserId())
                .examId(userExam.getExamId())
                .point(userExam.getTotalPoint())
                .totalPoint(userExam.getMaxPoint())
                .user(userExam.getUser())
                .timeStart(userExam.getTimeStart())
                .timeEnd(userExam.getTimeEnd())
                .totalTimeUsed(duration)
                .userExamId(id)
                .build();
    }

    @Override
    public UserExam getById(String id) {
        return this.userExamDomainRepository.getById(id);
    }

    @Override
    public UserExam testingExam(String id) {
        UserExamEntity userExamEntity = this.userExamEntityRepository.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.USER_EXAM_NOT_FOUND));
        UserExam userExam = this.userExamEntityMapper.toDomain(userExamEntity);
        if (Objects.equals(userExam.getStatus(), UserExamStatus.DONE)) {
            throw new ResponseException(BadRequestError.USER_EXAM_FINISHED);
        }

        if (Objects.equals(userExam.getStatus(), UserExamStatus.WAITING)) {
            ExamEntity examEntity = this.ExamEntityRepository.findById(userExam.getExamId()).orElseThrow(() -> new ResponseException(NotFoundError.EXAM_NOT_EXISTED));
            Long delayTime = 0L;
            if (Objects.nonNull(examEntity.getTimeDelay())) {
                delayTime = examEntity.getTimeDelay();
            }
            Long totalTime = Duration.between(userExam.getCreatedAt(), Instant.now()).toSeconds();
            if (totalTime > Duration.ofMinutes(delayTime).get(SECONDS)) {
                userExam.overTimeExam();
                this.userExamDomainRepository.save(userExam);
                return userExam;
            }
            userExam.startTesting();
            userExam.updateStatus(UserExamStatus.DOING);
            this.userExamDomainRepository.save(userExam);
        }
        PeriodRoomEntity periodRoomEntity = this.periodRoomEntityRepository.findByRoomIdAndPeriodId(userExam.getRoomId(), userExam.getPeriodId())
                .orElseThrow(() -> new ResponseException(NotFoundError.PERIOD_NOT_EXISTED_IN_ROOM));
        PeriodRoom periodRoom = this.periodRoomEntityMapper.toDomain(periodRoomEntity);
        userExam.enrichPeriodRoom(periodRoom);
        Optional<RoomEntity> roomEntity = this.roomEntityRepository.findById(userExam.getRoomId());
        if (roomEntity.isPresent())
        {
            Room room = this.roomEntityMapper.toDomain(roomEntity.get());
            userExam.enrichRoom(room);
        }
        Optional<PeriodEntity> optionalPeriodEntity = this.periodEntityRepository.findById(userExam.getPeriodId());
        if (optionalPeriodEntity.isPresent())
        {
            Period period = this.periodEntityMapper.toDomain(optionalPeriodEntity.get());
            userExam.enrichPeriod(period);
        }
        return userExam;
    }

    @Override
    public UserExam getByExamIdAndPeriodId(String examId, String periodId) {
        return this.userExamDomainRepository.findByExamIdAndPeriodId(examId, periodId);
    }

    @Override
    public PageDTO<UserExam> searchByRoomAndPeriod(String roomId, String periodId, UserRoomSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        return this.userExamDomainRepository.searchUserExam(roomId, periodId, SqlUtils.encodeKeyword(request.getKeyword()), pageable);
    }

    @Override
    public PageDTO<UserExam> getMyExamByRoomAndPeriod(String roomId, String periodId, UserRoomSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        Optional<String> userId = SecurityUtils.getCurrentUserLoginId();
        if (userId.isEmpty()) {
            throw new ResponseException(BadRequestError.USER_INVALID);
        }
        request.setUserIds(List.of(userId.get()));
        return this.userExamDomainRepository.getMyExam(roomId, periodId, request.getUserIds(), pageable);
    }

    @Override
    public List<UserExamResult> statisticResult(UserExamReportRequest request) {
        List<UserExam> userExams = this.userExamEntityMapper.toDomain(
                this.userExamEntityRepository.statisticResult(List.of(UserExamStatus.DONE, UserExamStatus.OVERTIME),
                        request.getUserIds(), request.getFromDate(), request.getToDate()));
        return userExams.stream().map(userExam -> {
            Long duration = 0L;
            if (Objects.nonNull(userExam.getTimeStart()) && Objects.nonNull(userExam.getTimeEnd())) {
                duration = Duration.between(userExam.getTimeStart(), userExam.getTimeEnd()).toSeconds();
            }
            return UserExamResult.builder()
                    .userId(userExam.getUserId())
                    .examId(userExam.getExamId())
                    .point(userExam.getTotalPoint())
                    .totalPoint(userExam.getMaxPoint())
                    .user(userExam.getUser())
                    .timeStart(userExam.getTimeStart())
                    .timeEnd(userExam.getTimeEnd())
                    .totalTimeUsed(duration)
                    .userExamId(userExam.getId())
                    .createdAt(userExam.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public ExamReview review(String id) {
        UserExam userExam = this.userExamDomainRepository.getById(id);
        if (this.examReviewDomainRepository.checkExist(id))
        {
            throw new ResponseException(BadRequestError.EXAM_REVIEW_EXISTED);
        }
        ExamReview examReview = new ExamReview(userExam, this.seqRepository.generateUserExamCode());
        examReviewDomainRepository.save(examReview);
        return examReview;
    }

    @Override
    public List<ExamReview> getAllReview(String id, String keyword) {
        return this.examReviewDomainRepository.getByUserExamId(id, keyword);
    }

    @Override
    public ExamReview receiveReview(String id, String reviewId) {
        ExamReview examReview = this.examReviewDomainRepository.getById(reviewId);
        if (!examReview.getStatus().equals(ExamReviewStatus.NEW))
        {
            throw new ResponseException(BadRequestError.EXAM_REVIEW_MUST_BE_NEW);
        }
        examReview.updateStatus(ExamReviewStatus.RECEIVED);
        examReviewDomainRepository.save(examReview);
        return examReview;
    }

    @Override
    public ExamReview doneReview(String id, String reviewId, UserExamReviewDoneRequest request) {
        ExamReview examReview = this.examReviewDomainRepository.getById(reviewId);
        if (!examReview.getStatus().equals(ExamReviewStatus.RECEIVED))
        {
            throw new ResponseException(BadRequestError.EXAM_REVIEW_MUST_BE_RECEIVED);
        }
        examReview.complete(request.getFeedBack(), request.getReviewFileIds());
        examReviewDomainRepository.save(examReview);
        return examReview;
    }

    @Override
    public List<ExamReview> getAllReviewByPeriodRoom(String roomId, String periodId, String keyword) {
        return this.examReviewDomainRepository.getAllByPeriodRoom(periodId, roomId, keyword);
    }
}
