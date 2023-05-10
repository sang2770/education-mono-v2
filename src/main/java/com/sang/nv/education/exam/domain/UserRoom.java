package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class UserRoom extends AuditableDomain {
    String id;
    String roomId;
    String userId;
    String username;
    UserType userType;
    String code;
    String fullName;
    User userDTO;
    Boolean deleted;

    public UserRoom(String userId, String roomId) {
        this.id = IdUtils.nextId();
        this.userId = userId;
        this.roomId = roomId;
        this.deleted = false;
    }

    public UserRoom(String roomId, User userDTO) {
        this.id = IdUtils.nextId();
        this.roomId = roomId;
        if (Objects.nonNull(userDTO)) {
            this.userId = userDTO.getId();
            this.userType = userDTO.getUserType();
            this.username = userDTO.getUsername();
            this.fullName = userDTO.getFullName();
            this.code = userDTO.getCode();
        }
        this.deleted = false;
    }


    public void enrichUserDTO(User userDTO) {
        this.userDTO = userDTO;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
