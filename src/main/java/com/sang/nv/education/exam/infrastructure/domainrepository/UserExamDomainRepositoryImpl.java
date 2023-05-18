package com.sang.nv.education.exam.infrastructure.domainrepository;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.domain.Exam;
import com.sang.nv.education.exam.domain.Period;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.UserExam;
import com.sang.nv.education.exam.domain.repository.UserExamDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.UserExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.UserExamInfoEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.PeriodEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.RoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.UserExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.UserExamInfoEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.PeriodEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.RoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.UserExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.UserExamInfoEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserExamDomainRepositoryImpl extends AbstractDomainRepository<UserExam, UserExamEntity, String> implements UserExamDomainRepository {
    private final UserExamEntityRepository userExamEntityRepository;
    private final UserExamInfoEntityRepository userExamInfoEntityRepository;
    private final UserExamEntityMapper userExamEntityMapper;
    private final UserExamInfoEntityMapper userExamInfoEntityMapper;
    private final PeriodEntityRepository periodEntityRepository;
    private final ExamEntityRepository examEntityRepository;
    private final ExamEntityMapper examEntityMapper;
    private final PeriodEntityMapper periodEntityMapper;
    private final RoomEntityRepository roomEntityRepository;
    private final RoomEntityMapper roomEntityMapper;
    private final UserService userService;

    public UserExamDomainRepositoryImpl(UserExamEntityRepository userExamEntityRepository, UserExamInfoEntityRepository userExamInfoEntityRepository, UserExamEntityMapper userExamEntityMapper, UserExamInfoEntityMapper userExamInfoEntityMapper, PeriodEntityRepository periodEntityRepository, ExamEntityRepository examEntityRepository, ExamEntityMapper examEntityMapper, PeriodEntityMapper periodEntityMapper, RoomEntityRepository roomEntityRepository, RoomEntityMapper roomEntityMapper, UserService userService) {
        super(userExamEntityRepository, userExamEntityMapper);
        this.userExamEntityRepository = userExamEntityRepository;
        this.userExamInfoEntityRepository = userExamInfoEntityRepository;
        this.userExamEntityMapper = userExamEntityMapper;
        this.userExamInfoEntityMapper = userExamInfoEntityMapper;
        this.periodEntityRepository = periodEntityRepository;
        this.examEntityRepository = examEntityRepository;
        this.examEntityMapper = examEntityMapper;
        this.periodEntityMapper = periodEntityMapper;
        this.roomEntityRepository = roomEntityRepository;
        this.roomEntityMapper = roomEntityMapper;
        this.userService = userService;
    }

    @Override
    public UserExam getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.USER_EXAM_NOT_EXISTED));
    }

    @Override
    public UserExam save(UserExam domain) {
        if (!CollectionUtils.isEmpty(domain.getUserExamInfos())) {
            List<UserExamInfoEntity> userExamInfoEntities = this.userExamInfoEntityMapper.toEntity(domain.getUserExamInfos());
            this.userExamInfoEntityRepository.saveAll(userExamInfoEntities);
        }
        return super.save(domain);
    }

    @Override
    public List<UserExam> saveAll(List<UserExam> domains) {
        return super.saveAll(domains);
    }

    @Override
    protected UserExam enrich(UserExam userExam) {
        List<UserExamInfoEntity> userExamInfoEntities = this.userExamInfoEntityRepository.findAllByUserExamId(userExam.getId());
        userExam.enrichUserExamInfo(this.userExamInfoEntityMapper.toDomain(userExamInfoEntities));
        FindByIdsRequest findByIdsRequest = new FindByIdsRequest();
        findByIdsRequest.setIds(List.of(userExam.getUserId()));
//        Response<List<UserDTO>> userDTOResponse = this.iamClient.getUserByIds(findByIdsRequest);
//        if (userDTOResponse.isSuccess() && userDTOResponse.getData().size() > 0) {
//            List<UserDTO> userDTOS = userDTOResponse.getData();
//            userExam.enrichUser(userDTOS.get(0));
//        } else {
//            throw new ResponseException(BadRequestError.USER_INVALID);
//        }
        List<User> users = this.userService.findByIds(findByIdsRequest.getIds());
        if (CollectionUtils.isEmpty(users)) {
            throw new ResponseException(BadRequestError.USER_INVALID);
        }
        userExam.enrichUser(users.get(0));
        Period period = this.periodEntityRepository.findById(userExam.getPeriodId()).map(periodEntityMapper::toDomain).orElseThrow(() -> new ResponseException(BadRequestError.PERIOD_NOT_EXISTED));
        userExam.enrichPeriod(period);
        Room room = this.roomEntityRepository.findById(userExam.getRoomId()).map(roomEntityMapper::toDomain).orElseThrow(() -> new ResponseException(BadRequestError.ROOM_NOT_EXISTED));
        userExam.enrichRoom(room);
        return userExam;
    }

    @Override
    protected List<UserExam> enrichList(List<UserExam> userExams) {
        FindByIdsRequest findByIdsRequest = new FindByIdsRequest();
        findByIdsRequest.setIds(userExams.stream().map(UserExam::getUserId).collect(Collectors.toList()));
        List<User> users = this.userService.findByIds(findByIdsRequest.getIds());
        userExams.forEach(userExam -> {
            Optional<User> optionalUser = users.stream().filter(user -> user.getId().equals(userExam.getUserId())).findFirst();
            if (optionalUser.isPresent()) {
                userExam.enrichUser(optionalUser.get());
            }
        });
//        Response<List<UserDTO>> userDTOResponse = this.iamClient.getUserByIds(findByIdsRequest);
//        if (userDTOResponse.isSuccess()) {
//            userExams.forEach(userExam -> {
//                Optional<UserDTO> optionalUserDTO = userDTOResponse.getData().stream().filter(userDTO -> userDTO.getId().equals(userExam.getUserId())).findFirst();
//                if (optionalUserDTO.isPresent()) {
//                    userExam.enrichUser(optionalUserDTO.get());
//                }
//            });
//        } else {
//            throw new ResponseException(BadRequestError.USER_INVALID);
//        }
        // Enrich exam
        List<String> examIds = userExams.stream().map(UserExam::getExamId).collect(Collectors.toList());
        List<ExamEntity> examEntities = this.examEntityRepository.findAllByIds(examIds);
        List<Exam> exams = this.examEntityMapper.toDomain(examEntities);
        userExams.forEach(userExam -> {
            Optional<Exam> optionalExam = exams.stream().filter(exam -> exam.getId().equals(userExam.getExamId())).findFirst();
            if (optionalExam.isPresent()) {
                userExam.enrichExam(optionalExam.get());
            }
        });
        // Enrich result
        userExams.forEach(userExam -> {
            Long duration = 0L;
            if (Objects.nonNull(userExam.getTimeStart()) && Objects.nonNull(userExam.getTimeEnd())) {
                duration = Duration.between(userExam.getTimeStart(), userExam.getTimeEnd()).toMinutes();
            }
            userExam.enrichUserExamResult(UserExamResult.builder()
                    .userId(userExam.getUserId())
                    .examId(userExam.getExamId())
                    .point(userExam.getTotalPoint())
                    .totalPoint(userExam.getMaxPoint())
                    .user(userExam.getUser())
                    .timeStart(userExam.getTimeStart())
                    .timeEnd(userExam.getTimeEnd())
                    .totalTimeUsed(duration)
                    .build());
        });
        return userExams;
    }

    @Override
    public UserExam findByExamIdAndPeriodId(String examId, String periodId) {
        Optional<UserExamEntity> optionalUserExam = this.userExamEntityRepository.findByExamId(examId, periodId);
        if (optionalUserExam.isEmpty()) {
            throw new ResponseException(NotFoundError.USER_EXAM_NOT_EXISTED);
        }
        UserExam userExams = this.userExamEntityMapper.toDomain(optionalUserExam.get());
        return this.enrich(userExams);
    }

//    @Override
//    public UserExam findById(String id) {
//        Optional<UserExamEntity> optionalUserExam = this.userExamEntityRepository.findById(id);
//        if (optionalUserExam.isEmpty())
//        {
//            throw new ResponseException(NotFoundError.USER_EXAM_NOT_EXISTED);
//        }
//        UserExam userExam = this.userExamEntityMapper.toDomain(optionalUserExam.get());
//        return  this.enrich(userExam);
//    }

    @Override
    public PageDTO<UserExam> searchUserExam(String roomId, String periodId, Pageable pageable) {
        Page<UserExamEntity> userExamEntities = this.userExamEntityRepository.searchUserExam(roomId, periodId, pageable);
        List<UserExam> userExams = this.userExamEntityMapper.toDomain(userExamEntities.getContent());
        this.enrichList(userExams);
        return PageDTO.of(userExams, userExamEntities.getTotalPages(), userExamEntities.getNumber(), userExamEntities.getSize());
    }

    @Override
    public PageDTO<UserExam> getMyExam(String roomId, String periodId, List<String> userIds, Pageable pageable) {
        Page<UserExamEntity> userExamEntities = this.userExamEntityRepository.getMyExam(roomId, periodId, userIds, pageable);
        List<UserExam> userExams = this.userExamEntityMapper.toDomain(userExamEntities.getContent());
        this.enrichList(userExams);
        return PageDTO.of(userExams, userExamEntities.getTotalPages(), userExamEntities.getNumber(), userExamEntities.getSize());
    }
}

