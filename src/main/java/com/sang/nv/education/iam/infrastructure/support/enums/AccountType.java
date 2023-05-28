package com.sang.nv.education.iam.infrastructure.support.enums;

import lombok.Getter;

@Getter
public enum AccountType {
    STUDENT(UserType.STUDENT, "Thí sinh"),
    MANAGER(UserType.MANAGER, "Quản trị viên")
    ;
    private UserType code;
    private String name;
    AccountType(UserType code, String name){
        this.code = code;
        this.name = name;
    }
}
