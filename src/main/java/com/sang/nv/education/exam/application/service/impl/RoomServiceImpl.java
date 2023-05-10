package com.sang.nv.education.exam.application.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.nv.education.common.web.support.SecurityUtils;
import com.sang.nv.education.exam.application.dto.request.UpdateMemberInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UpdatePeriodInRoomRequest;
import com.sang.nv.education.exam.application.dto.request.UserExamCreateRequest;
import com.sang.nv.education.exam.application.dto.request.UserRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.PeriodRoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.dto.request.room.RoomSearchRequest;
import com.sang.nv.education.exam.application.dto.request.room.SendExamToUserRequest;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapper;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapperQuery;
import com.sang.nv.education.exam.application.service.RoomService;
import com.sang.nv.education.exam.domain.Exam;
import com.sang.nv.education.exam.domain.Period;
import com.sang.nv.education.exam.domain.PeriodRoom;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.Subject;
import com.sang.nv.education.exam.domain.UserExam;
import com.sang.nv.education.exam.domain.UserRoom;
import com.sang.nv.education.exam.domain.command.RoomCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.UserExamCreateCmd;
import com.sang.nv.education.exam.domain.repository.RoomDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.PeriodRoomEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.UserRoomEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.PeriodEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.PeriodRoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.RoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.SubjectEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.UserExamEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.UserRoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.query.RoomSearchQuery;
import com.sang.nv.education.exam.infrastructure.persistence.query.UserRoomSearchQuery;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.PeriodEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.PeriodRoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.RoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.SubjectEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.UserExamEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.UserRoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.enums.UserExamStatus;
import com.sang.nv.education.exam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomEntityRepository roomEntityRepository;
    private final ExamAutoMapper examAutoMapper;
    private final ExamAutoMapperQuery examAutoMapperQuery;
    private final RoomDomainRepository roomDomainRepository;
    private final RoomEntityMapper roomEntityMapper;
    private final UserRoomEntityRepository userRoomEntityRepository;
    private final UserRoomEntityMapper userRoomEntityMapper;

    private final UserExamEntityRepository userExamEntityRepository;
    private final UserExamEntityMapper userExamEntityMapper;
    private final PeriodRoomEntityRepository periodRoomEntityRepository;
    private final SubjectEntityRepository subjectEntityRepository;
    private final SubjectEntityMapper subjectEntityMapper;
    private final ExamEntityRepository examEntityRepository;
    private final ExamEntityMapper examEntityMapper;

    private final SeqRepository seqRepository;
    private final PeriodRoomEntityMapper periodRoomEntityMapper;
    private final PeriodEntityRepository periodEntityRepository;
    private final PeriodEntityMapper periodEntityMapper;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public RoomServiceImpl(RoomEntityRepository roomEntityRepository, ExamAutoMapper examAutoMapper,
                           ExamAutoMapperQuery examAutoMapperQuery, RoomDomainRepository RoomDomainRepository,
                           RoomEntityMapper roomEntityMapper, UserRoomEntityRepository userRoomEntityRepository,
                           UserRoomEntityMapper userRoomEntityMapper,
                           UserExamEntityRepository userExamEntityRepository, UserExamEntityMapper userExamEntityMapper, PeriodRoomEntityRepository periodRoomEntityRepository,
                           SubjectEntityRepository subjectEntityRepository,
                           SubjectEntityMapper subjectEntityMapper,
                           ExamEntityRepository examEntityRepository,
                           ExamEntityMapper examEntityMapper,
                           SeqRepository seqRepository, PeriodRoomEntityMapper periodRoomEntityMapper, PeriodEntityRepository periodEntityRepository, PeriodEntityMapper periodEntityMapper, UserService userService, ObjectMapper objectMapper) {
        this.examAutoMapper = examAutoMapper;
        this.examAutoMapperQuery = examAutoMapperQuery;
        this.roomDomainRepository = RoomDomainRepository;
        this.roomEntityRepository = roomEntityRepository;
        this.roomEntityMapper = roomEntityMapper;
        this.userRoomEntityRepository = userRoomEntityRepository;
        this.userRoomEntityMapper = userRoomEntityMapper;
        this.userExamEntityRepository = userExamEntityRepository;
        this.userExamEntityMapper = userExamEntityMapper;
        this.periodRoomEntityRepository = periodRoomEntityRepository;
        this.subjectEntityRepository = subjectEntityRepository;
        this.subjectEntityMapper = subjectEntityMapper;
        this.examEntityRepository = examEntityRepository;
        this.examEntityMapper = examEntityMapper;
        this.seqRepository = seqRepository;
        this.periodRoomEntityMapper = periodRoomEntityMapper;
        this.periodEntityRepository = periodEntityRepository;
        this.periodEntityMapper = periodEntityMapper;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Room create(RoomCreateOrUpdateRequest request) {
        Subject subject = this.subjectEntityMapper.toDomain(this.subjectEntityRepository.findById(request.getSubjectId()).orElseThrow(() -> new ResponseException(BadRequestError.SUBJECT_NOT_EXISTED)));
        RoomCreateOrUpdateCmd cmd = this.examAutoMapper.from(request);
        cmd.setSubjectName(subject.getName());
        cmd.setCode(this.seqRepository.generateRoomCode());
        Room Room = new Room(cmd);
        this.roomDomainRepository.save(Room);
        return Room;
    }

    @Override
    public Room update(String id, RoomCreateOrUpdateRequest request) {
        Room Room = this.getById(id);
        Room.update(this.examAutoMapper.from(request));
        this.roomDomainRepository.save(Room);
        return Room;
    }

    @Override
    public PageDTO<Room> search(RoomSearchRequest request) {
        RoomSearchQuery query = this.examAutoMapperQuery.from(request);
        return this.searchRoom(query);
    }

    @Override
    public Room getById(String id) {
        return this.roomDomainRepository.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.ROOM_NOT_EXISTED));
    }

    @Override
    public Room addMemberInRoom(String id, UpdateMemberInRoomRequest request) {
        Room room = this.getById(id);
        if (!CollectionUtils.isEmpty(request.getMemberIds())) {
            List<User> users = this.userService.findByIds(request.getMemberIds());
            if (CollectionUtils.isEmpty(users)) {
                throw new ResponseException(BadRequestError.USER_INVALID);
            }
            room.addUser(request.getMemberIds(), users);
//            FindByIdsRequest findByIdsRequest = FindByIdsRequest.builder().ids(request.getMemberIds()).build();
//            Response<List<UserDTO>> userDTOResponse = this.iamClient.getUserByIds(findByIdsRequest);
//            if (!userDTOResponse.isSuccess()) {
//                throw new ResponseException(BadRequestError.USER_INVALID);
//            }
//            room.addUser(request.getMemberIds(), userDTOResponse.getData());
            this.roomDomainRepository.save(room);
        }
        return room;
    }

    @Override
    public Room removeMemberInRoom(String id, UpdateMemberInRoomRequest request) {
        Room room = this.getById(id);
        if (!CollectionUtils.isEmpty(request.getMemberIds())) {
            room.removeUser(request.getMemberIds());
        }
        this.roomDomainRepository.save(room);
        return room;
    }

    @Override
    public Room addPeriodInRoom(String id, UpdatePeriodInRoomRequest request) {
        Room room = this.getById(id);
        if (!CollectionUtils.isEmpty(request.getPeriodIds())) {
            room.addPeriod(request.getPeriodIds());
        }
        this.roomDomainRepository.save(room);
        return room;
    }

    @Override
    public Room removePeriodInRoom(String id, UpdatePeriodInRoomRequest request) {
        Room room = this.getById(id);
        if (!CollectionUtils.isEmpty(request.getPeriodIds())) {
            room.removePeriod(request.getPeriodIds());
            this.roomDomainRepository.save(room);
        }
        return room;
    }

    @Override
    public PageDTO<UserRoom> getUserInRoom(String id, UserRoomSearchRequest request) {
        UserRoomSearchQuery query = this.examAutoMapperQuery.from(request);
        query.setRoomIds(List.of(id));
        Long count = this.userRoomEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<UserRoom> userRooms = this.userRoomEntityMapper.toDomain(this.userRoomEntityRepository.search(query));
//        Enrich User
        List<User> users = this.userService.findByIds(userRooms.stream().map(UserRoom::getUserId).collect(Collectors.toList()));
        userRooms.forEach(userRoom -> {
            Optional<User> user = users.stream().filter(user1 -> user1.getId().equals(userRoom.getUserId())).findFirst();
            if (user.isPresent()) {
                userRoom.enrichUserDTO(user.get());
            }
        });
//        FindByIdsRequest findByIdsRequest = FindByIdsRequest.builder().ids(userRooms.stream().map(UserRoom::getUserId).collect(Collectors.toList())).build();
//        Response<List<UserDTO>> userDTOResponse = this.iamClient.getUserByIds(findByIdsRequest);
//        if (!userDTOResponse.isSuccess()) {
//            throw new ResponseException(BadRequestError.USER_INVALID);
//        }
//        List<UserDTO> userDTOS = userDTOResponse.getData();
//        userRooms.forEach(userRoom -> {
//            Optional<UserDTO> userDTO = userDTOS.stream().filter(userDTO1 -> userDTO1.getId().equals(userRoom.getUserId())).findFirst();
//            if (userDTO.isPresent()) {
//                userRoom.enrichUserDTO(userDTO.get());
//            }
//        });
        return PageDTO.of(userRooms, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public PageDTO<PeriodRoom> getPeriodInRoom(String id, PeriodRoomSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
//        log.info("key", SqlUtils.encodeKeyword(request.getKeyword()));
        Page<PeriodRoomEntity> periodRoomEntities = this.periodRoomEntityRepository.searchByRoomId(id, request.getIds(), request.getKeyword(), pageable);
        List<String> periodIds = periodRoomEntities.getContent().stream().map(PeriodRoomEntity::getPeriodId).collect(Collectors.toList());
        List<Period> periods = new ArrayList<>();
        if (!CollectionUtils.isEmpty(periodIds)) {
            periods.addAll(this.periodEntityMapper.toDomain(this.periodEntityRepository.findAllByIds(periodIds)));
        }
        List<PeriodRoom> periodRooms = this.periodRoomEntityMapper.toDomain(periodRoomEntities.getContent());
        periodRooms.forEach(periodRoom -> {
            Optional<Period> period = periods.stream().filter(period1 -> period1.getId().equals(periodRoom.getPeriodId())).findFirst();
            if (period.isPresent()) {
                periodRoom.enrichPeriod(period.get());
            }
        });
        return PageDTO.of(periodRooms, request.getPageIndex(), request.getPageSize(), periodRoomEntities.getTotalElements());
    }

    @Override
    public void sendExamToUser(String id, UserExamCreateRequest request) {
        // check permision
        if (SecurityUtils.getCurrentUser().isEmpty()) {
            throw new ResponseException(BadRequestError.USER_INVALID);
        }
        User user = this.objectMapper.convertValue(SecurityUtils.getCurrentUser().get(), User.class);
        List<UserRoomEntity> userRoomEntity = this.userRoomEntityRepository.findByUserIds(List.of(user.getId()));
        if (CollectionUtils.isEmpty(userRoomEntity) && !user.getIsRoot()) {
            throw new ResponseException(BadRequestError.PERMISSION_DENY);
        }
        Room room = this.getById(id);
        PeriodRoom periodRoom = this.periodRoomEntityMapper.toDomain(this.periodRoomEntityRepository.findByRoomIdAndPeriodId(id, request.getPeriodId()).orElseThrow(() -> new ResponseException(NotFoundError.PERIOD_NOT_EXISTED_IN_ROOM)));
        periodRoom.updateIsSendExam(true);
        Optional<PeriodRoomEntity> periodRoomEntityOptional = this.periodRoomEntityRepository.findByRoomIdAndPeriodId(id, request.getPeriodId());
        if (periodRoomEntityOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.PERIOD_NOT_EXISTED_IN_ROOM);
        }
        List<Exam> exams = this.examEntityMapper.toDomain(this.examEntityRepository.findAllByPeriodAnsSubject(request.getPeriodId(), room.getSubjectId()));
        List<UserRoom> userRooms = this.userRoomEntityMapper.toDomain(this.userRoomEntityRepository.findByRoomId(id));
        List<UserExam> userExams = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        if (exams.size() > 0) {
            userRooms.forEach((userRoom) -> {
                Exam exam = exams.get(count.get() % exams.size());
                count.getAndIncrement();
                UserExamCreateCmd cmd = UserExamCreateCmd.builder()
                        .maxPoint(exam.getTotalPoint())
                        .code(exam.getCode())
                        .examId(exam.getId())
                        .userId(userRoom.getUserId())
                        .roomId(id)
                        .periodId(request.getPeriodId())
                        .build();
                UserExam userExam = new UserExam(cmd);
                userExam.updateStatus(UserExamStatus.DOING);
                userExams.add(userExam);
            });
        }
        this.periodRoomEntityRepository.save(this.periodRoomEntityMapper.toEntity(periodRoom));
        this.userExamEntityRepository.saveAll(this.userExamEntityMapper.toEntity(userExams));
    }

    @Override
    public void sendExam(String id, SendExamToUserRequest request) {
        Room room = this.getById(id);
        PeriodRoom periodRoom = this.periodRoomEntityMapper.toDomain(this.periodRoomEntityRepository
                .findByRoomIdAndPeriodId(id, request.getPeriodId()).orElseThrow(() -> new ResponseException(NotFoundError.PERIOD_NOT_EXISTED_IN_ROOM)));
        periodRoom.updateIsSendExam(true);
        Optional<PeriodRoomEntity> periodRoomEntityOptional = this.periodRoomEntityRepository.findByRoomIdAndPeriodId(id, request.getPeriodId());
        if (periodRoomEntityOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.PERIOD_NOT_EXISTED_IN_ROOM);
        }
        List<Exam> exams = this.examEntityMapper.toDomain(this.examEntityRepository.findAllByPeriodAnsSubject(request.getPeriodId(), room.getSubjectId()));
        if (CollectionUtils.isEmpty(exams)) {
            throw new ResponseException(BadRequestError.PERIOD_NOT_EXAM);
        }
        List<UserRoom> userRooms = this.userRoomEntityMapper.toDomain(this.userRoomEntityRepository.findByRoomId(id))
                .stream().filter(userRoom -> Objects.equals(userRoom.getUserType(), UserType.STUDENT)).collect(Collectors.toList());
        List<UserExam> userExams = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        if (!exams.isEmpty()) {
            userRooms.forEach((userRoom) -> {
                Exam exam = exams.get(count.get() % exams.size());
                count.getAndIncrement();
                UserExamCreateCmd cmd = UserExamCreateCmd.builder()
                        .maxPoint(exam.getTotalPoint())
                        .code(this.seqRepository.generateUserExamCode())
                        .examId(exam.getId())
                        .userId(userRoom.getUserId())
                        .roomId(id)
                        .timeDelay(Objects.nonNull(request.getTimeDelay()) ? request.getTimeDelay() : exam.getTimeDelay())
                        .periodId(request.getPeriodId())
                        .build();
                UserExam userExam = new UserExam(cmd);
                userExam.updateStatus(UserExamStatus.WAITING);
                userExams.add(userExam);
            });
        }
        this.periodRoomEntityRepository.save(this.periodRoomEntityMapper.toEntity(periodRoom));
        this.userExamEntityRepository.saveAll(this.userExamEntityMapper.toEntity(userExams));
    }

    @Override
    public PageDTO<Room> getMyRoom(RoomSearchRequest request) {
        RoomSearchQuery query = this.examAutoMapperQuery.from(request);
        Optional<String> userId = SecurityUtils.getCurrentUserLoginId();
        if (userId.isEmpty()) {
            throw new ResponseException(BadRequestError.USER_INVALID);
        }
        query.setUserIds(List.of(userId.get()));
        return this.searchRoom(query);
    }

    @Override
    public List<Room> getByIds(List<String> ids) {
        return this.roomEntityMapper.toDomain(this.roomEntityRepository.findAllById(ids));
    }

    @Override
    public List<Room> getAll() {
        return this.roomEntityMapper.toDomain(this.roomEntityRepository.findAll());
    }

    private PageDTO<Room> searchRoom(RoomSearchQuery query) {
        Long count = this.roomEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<Room> rooms = this.roomEntityMapper.toDomain(this.roomEntityRepository.search(query));
        this.enrichRoom(rooms);
        return PageDTO.of(rooms, query.getPageIndex(), query.getPageSize(), count);
    }

    private void enrichRoom(List<Room> room) {
        List<String> subjectIds = room.stream().map(Room::getSubjectId).collect(Collectors.toList());
        List<Subject> subjects = this.subjectEntityRepository.findAllByIds(subjectIds).stream().map(this.subjectEntityMapper::toDomain).collect(Collectors.toList());
        room.forEach(room1 -> {
            Optional<Subject> subject = subjects.stream().filter(subject1 -> subject1.getId().equals(room1.getSubjectId())).findFirst();
            subject.ifPresent(room1::enrichSubject);
        });
        List<String> roomIds = room.stream().map(Room::getId).collect(Collectors.toList());
        List<UserRoom> userRooms = this.userRoomEntityRepository.findAllByRoomIds(roomIds).stream().map(this.userRoomEntityMapper::toDomain).collect(Collectors.toList());
        room.forEach(room1 -> {
            List<UserRoom> userRoom = userRooms.stream().filter(userRoom1 -> userRoom1.getRoomId().equals(room1.getId())).collect(Collectors.toList());
            room1.enrichUser(userRoom);
        });
    }

}
