package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.RoomCreateOrUpdateCmd;
import com.sang.nv.education.exam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import com.sang.nv.education.iam.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Room extends AuditableDomain {
    String id;
    String name;
    String subjectId;
    String subjectName;
    Boolean deleted;
    String description;
    String code;
    List<UserRoom> userRooms;

    List<PeriodRoom> periodRooms;
    Subject subject;

    public Room(RoomCreateOrUpdateCmd cmd) {
        this.description = cmd.getDescription();
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.subjectName = cmd.getSubjectName();
        this.subjectId = cmd.getSubjectId();
        this.userRooms = new ArrayList<>();
        this.deleted = Boolean.FALSE;
        this.code = cmd.getCode();
    }

    public void update(RoomCreateOrUpdateCmd cmd) {
        this.subjectId = cmd.getSubjectId();
        this.subjectName = cmd.getSubjectName();
        this.name = cmd.getName();
        this.deleted = Boolean.FALSE;
        this.description = cmd.getDescription();
    }

    public void enrichUser(List<UserRoom> userRooms) {
        this.userRooms = userRooms;
    }

    public void enrichExam(List<PeriodRoom> periodRooms) {
        this.periodRooms = periodRooms;
    }

    public void addUser(List<String> userIds, List<User> userDTOS) {
        userIds.forEach(userId -> {
            Optional<UserRoom> userRoomOptional = this.userRooms.stream().filter(userRoom -> Objects.equals(userId, userRoom.getUserId())).findFirst();
            if (userRoomOptional.isEmpty()) {
                Optional<User> optionalUserDTO = userDTOS.stream().filter(userDTO -> Objects.equals(userId, userDTO.getId())).findFirst();
                if (optionalUserDTO.isEmpty()) {
                    throw new ResponseException(NotFoundError.USER_NOT_FOUND);
                }
                this.userRooms.add(new UserRoom(this.id, optionalUserDTO.get()));
            }
        });
    }

    public void removeUser(List<String> userIds) {
        userIds.forEach(userId -> {
            Optional<UserRoom> userRoomOptional = this.userRooms.stream().filter(userRoom -> Objects.equals(userId, userRoom.getUserId())).findFirst();
            if (userRoomOptional.isEmpty()) {
                throw new ResponseException(BadRequestError.USER_IS_NOT_IN_GROUP);
            } else {
                userRoomOptional.get().deleted();
            }
        });
    }

    public void addPeriod(List<String> periodIds) {
        periodIds.forEach(periodId -> {
            Optional<PeriodRoom> periodRoomOptional = this.periodRooms.stream().filter(periodRoom ->
                    Objects.equals(periodId, periodRoom.getPeriodId())).findFirst();
            if (periodRoomOptional.isEmpty()) {
                this.periodRooms.add(new PeriodRoom(periodId, this.id));
            }
        });
    }

    public void removePeriod(List<String> periodIds) {
        periodIds.forEach(examId -> {
            Optional<PeriodRoom> periodRoomOptional = this.periodRooms.stream().filter(periodRoom ->
                    Objects.equals(examId, periodRoom.getPeriodId())).findFirst();
            if (periodRoomOptional.isEmpty()) {
                throw new ResponseException(BadRequestError.USER_IS_NOT_IN_GROUP);
            }
            this.periodRooms.remove(periodRoomOptional.get());

        });
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichSubject(Subject subject) {
        this.subject = subject;
    }
}
