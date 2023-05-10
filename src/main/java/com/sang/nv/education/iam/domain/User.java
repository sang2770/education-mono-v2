package com.sang.nv.education.iam.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.command.UserCreateOrUpdateCmd;
import com.sang.nv.education.iam.infrastructure.support.enums.UserStatus;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
import io.jsonwebtoken.lang.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
@Builder
public class User extends AuditableDomain {

    private String id;
    private String code;

    private String username;

    @JsonIgnore
    private String password;

    private String fullName;

    private String email;

    private String phoneNumber;

    private LocalDate dayOfBirth;

    private Boolean deleted;

    private UserStatus status = UserStatus.ACTIVE;
    private String avatarFileId;
    private String avatarFileViewUrl;
    private Instant lastAuthChangeAt;
    private String classId;
    private Boolean isRoot;
    @JsonIgnore
    private List<UserRole> userRoles;

    private List<Role> roles;

    private UserType userType;

    private Classes classes;
    private List<String> grantedPermissions;


    public User(UserCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.username = cmd.getUsername();
        this.password = cmd.getPassword();
        this.fullName = cmd.getFullName();
        this.email = cmd.getEmail();
        this.phoneNumber = cmd.getPhoneNumber();
        this.dayOfBirth = cmd.getDayOfBirth();
        this.status = UserStatus.ACTIVE;
        this.classId = cmd.getClassId();
        this.code = cmd.getCode();
        this.isRoot = cmd.getIsRoot();
        this.userType = cmd.getUserType();
        this.deleted = false;
    }

    public User(String username, String password, String fullName, String roleId, Boolean isRoot, UserType userType) {
        this.id = IdUtils.nextId();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = fullName;
        this.status = UserStatus.ACTIVE;
        this.userRoles = new ArrayList<>();
        this.isRoot = isRoot;
        this.userType = userType;
        this.userRoles.add(new UserRole(this.id, roleId));
        this.deleted = false;
    }

    public User(UserCreateOrUpdateCmd cmd, List<Role> existedRoles) {
        this.id = IdUtils.nextId();
        this.username = cmd.getUsername().toLowerCase();
        this.fullName = cmd.getFullName();
        this.email = cmd.getEmail();
        this.password = cmd.getPassword();
        this.phoneNumber = cmd.getPhoneNumber();
        this.dayOfBirth = cmd.getDayOfBirth();
        this.classId = cmd.getClassId();
        this.code = cmd.getCode();
        this.avatarFileId = cmd.getAvatarFileId();
        this.avatarFileViewUrl = cmd.getAvatarFileViewUrl();
        this.userType = cmd.getUserType();
        if (!CollectionUtils.isEmpty(cmd.getRoleIds())) {
            this.userRoles = new ArrayList<>();
            this.assignRoles(cmd.getRoleIds(), existedRoles);
        }
        this.deleted = false;
    }

    public void updateRole(List<String> roleIds, List<Role> existedRoles) {
        if (!CollectionUtils.isEmpty(roleIds)) {
            this.assignRoles(roleIds, existedRoles);
        }
        this.deleted = false;
    }

    public void update(UserCreateOrUpdateCmd cmd, List<Role> existedRoles) {
        this.fullName = cmd.getFullName();
        this.email = cmd.getEmail();
        this.phoneNumber = cmd.getPhoneNumber();
        this.status = UserStatus.ACTIVE;
        this.classId = cmd.getClassId();
        this.code = cmd.getCode();
        if (!CollectionUtils.isEmpty(cmd.getRoleIds())) {
            this.assignRoles(cmd.getRoleIds(), existedRoles);
        }
        this.userType = cmd.getUserType();
        this.avatarFileId = cmd.getAvatarFileId();
        this.avatarFileViewUrl = cmd.getAvatarFileViewUrl();
        this.deleted = false;

    }

    public void update(UserCreateOrUpdateCmd cmd) {
        this.fullName = cmd.getFullName();
        this.email = cmd.getEmail();
        this.phoneNumber = cmd.getPhoneNumber();
        this.dayOfBirth = cmd.getDayOfBirth();
        this.status = UserStatus.ACTIVE;
        this.classId = cmd.getClassId();
        this.code = cmd.getCode();
        this.userType = cmd.getUserType();
        this.deleted = false;
    }

    private void assignRoles(List<String> roleIds, List<Role> existedRoles) {
        if (Collections.isEmpty(existedRoles)) {
            throw new ResponseException(BadRequestError.ROLE_INVALID);
        }
        this.userRoles.forEach(UserRole::deleted);
        roleIds.forEach(id -> {
            Optional<Role> roleOptional = existedRoles.stream().filter(existedRole -> existedRole.getId().equals(id)).findFirst();
            if (roleOptional.isEmpty()) {
                throw new ResponseException(BadRequestError.ROLE_INVALID);
            }
            Role role = roleOptional.get();
            Optional<UserRole> userRole = this.userRoles.stream().filter(item -> item.getRoleId().equals(role.getId())).findFirst();
            if (userRole.isEmpty()) {
                this.userRoles.add(new UserRole(this.id, role.getId()));
            } else {
                userRole.get().unDelete();
            }
        });
    }

    public void enrichRoles(List<Role> userRoles) {
        this.roles = userRoles;
    }

    public void enrichClasses(Classes classes) {
        this.classes = classes;
    }

    public void enricUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public void enrichAvatarFileViewUrl(String avatarFileViewUrl) {
        this.avatarFileViewUrl = avatarFileViewUrl;
    }

    public void active() {
        this.status = UserStatus.ACTIVE;
        this.lastAuthChangeAt = null;
    }

    public void inactive() {
        this.status = UserStatus.INACTIVE;
        this.lastAuthChangeAt = Instant.now();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.lastAuthChangeAt = Instant.now();
    }

    public void deleted() {
        if (UserStatus.ACTIVE.equals(this.status)) {
            throw new ResponseException(BadRequestError.CANNOT_DELETE_ACTIVE_USER);
        }
        if (Boolean.TRUE.equals(this.deleted)) {
            throw new ResponseException(BadRequestError.CANNOT_DELETE_DELETED_USER);
        }
        this.deleted = true;
    }

    public void enrichViewUrlFile(String avatarFileViewUrl) {
        this.avatarFileViewUrl = avatarFileViewUrl;
    }

}
