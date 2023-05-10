package com.sang.nv.education.exam.infrastructure.domainrepository;


import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.exam.domain.PeriodRoom;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.domain.UserRoom;
import com.sang.nv.education.exam.domain.repository.RoomDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.RoomEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.PeriodRoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.RoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.UserRoomEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.PeriodRoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.RoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.UserRoomEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class RoomDomainRepositoryImpl extends AbstractDomainRepository<Room, RoomEntity, String> implements RoomDomainRepository {
    private final RoomEntityRepository RoomEntityRepository;
    private final RoomEntityMapper RoomEntityMapper;
    private final UserRoomEntityMapper UserRoomEntityMapper;
    private final UserRoomEntityRepository UserRoomEntityRepository;
    private final PeriodRoomEntityMapper periodRoomEntityMapper;
    private final PeriodRoomEntityRepository periodRoomEntityRepository;

    public RoomDomainRepositoryImpl(RoomEntityRepository RoomEntityRepository,
                                    RoomEntityMapper RoomEntityMapper, UserRoomEntityMapper UserRoomEntityMapper,
                                    UserRoomEntityRepository UserRoomEntityRepository, PeriodRoomEntityMapper periodRoomEntityMapper, PeriodRoomEntityRepository periodRoomEntityRepository) {
        super(RoomEntityRepository, RoomEntityMapper);
        this.RoomEntityRepository = RoomEntityRepository;
        this.RoomEntityMapper = RoomEntityMapper;
        this.UserRoomEntityMapper = UserRoomEntityMapper;
        this.UserRoomEntityRepository = UserRoomEntityRepository;
        this.periodRoomEntityMapper = periodRoomEntityMapper;
        this.periodRoomEntityRepository = periodRoomEntityRepository;
    }

    @Override
    public Room getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.GROUP_NOT_EXISTED));
    }

    @Override
    public Room save(Room domain) {
        // save UserRoom
        if (!CollectionUtils.isEmpty(domain.getUserRooms())) {
            List<UserRoom> userRooms = domain.getUserRooms();
            this.UserRoomEntityRepository.saveAll(this.UserRoomEntityMapper.toEntity(userRooms));
        }
        if (!CollectionUtils.isEmpty(domain.getPeriodRooms())) {
            List<PeriodRoom> periodRooms = domain.getPeriodRooms();
            this.periodRoomEntityRepository.saveAll(this.periodRoomEntityMapper.toEntity(periodRooms));
        }
        return super.save(domain);
    }


    @Override
    protected Room enrich(Room Room) {
        // enrich UserRoom
        List<UserRoom> UserRooms = this.UserRoomEntityMapper.toDomain(this.UserRoomEntityRepository.findByRoomId(Room.getId()));
        Room.enrichUser(UserRooms);
        List<PeriodRoom> periodRooms = this.periodRoomEntityMapper.toDomain(this.periodRoomEntityRepository.findAllByRoomIds(Room.getId()));
        Room.enrichExam(periodRooms);
        return super.enrich(Room);
    }
}

