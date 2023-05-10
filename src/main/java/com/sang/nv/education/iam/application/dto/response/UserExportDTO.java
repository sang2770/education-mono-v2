package com.sang.nv.education.iam.application.dto.response;

import com.sang.nv.education.iam.domain.Classes;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class UserExportDTO {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String departmentName;
    private String className;
    private String code;
    private String courseName;
    private LocalDate dayOfBirth;
    private UserType userType;


    public UserExportDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phoneNumber = user.getPhoneNumber();
        if (Objects.nonNull(user.getClasses())) {
            Classes classes = user.getClasses();
            this.className = user.getClasses().getName();
            this.departmentName = Objects.nonNull(user.getClasses().getDepartment()) ? classes.getDepartment().getName() : "";
            this.courseName = Objects.nonNull(user.getClasses().getKey()) ? classes.getKey().getName() : "";
        }
        this.code = user.getCode();
        this.dayOfBirth = user.getDayOfBirth();
        this.userType = user.getUserType();
    }

}
