package com.sang.nv.education.iam.infrastructure.support.exception;


import com.sang.commonmodel.error.ResponseError;
import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    //    Login, authentication
    LOGIN_FAIL_DUE_INACTIVE_ACCOUNT(400, " Login Fail"),
    LOGIN_FAIL_BLOCK_ACCOUNT(400, "Login fail due account was block!"),
    USER_NOT_PERMISSION_FAIL_ACCOUNT(400, "Login fail!"),
    LOGIN_FAIL(400, "Login fail!"),
    LOGIN_FAIL_WARNING_BEFORE_BLOCK(400, "Warning account will be block!"),
    CHANGE_PASSWORD_NOT_SUPPORTED(400, "Change password is supported"),
    PASSWORD_NOT_STRONG(400, "Password required strong"),
    REPEAT_PASSWORD_DOES_NOT_MATCH(400, "Repeat password does not match"),
    PASSWORD_REQUIRED(400, "Password is required"),
    WRONG_PASSWORD(400, "Wrong password"),
    USER_EMAIL_EXITED(400, "Email exited"),
    USER_USERNAME_EXITED(400, "Username exited"),
    USER_INVALID(400, "User invalid: {0}"),
    QUESTION_INVALID(400, "Question invalid: {0}"),
    STATUS_INVALID(400, "Status invalid: {0}"),
    IDS_IS_REQUIRED(400, "Ids is required"),
    CLIENT_NOT_EXISTED(400, "Client not exited"),
    USER_PHONE_NUMBER_EXITED(400, "Phone number exited"),
    USER_EMPLOYEE_CODE_EXITED(400, "Employee code exited"),
    CLIENT_NAME_EXITED(400, "Client name exited"),
    CANNOT_DELETE_ACTIVE_USER(400, "Can not delete because user is active"),
    CANNOT_DELETE_DELETED_USER(400, "User has been deleted"),
    EMAIL_NOT_EXISTED_IN_SYSTEM(400, "Email not existed in system"),
    ROLE_INVALID(400, "Role is invalid"),
    IDS_INVALID(400, "Ids invalid"),
    ACCOUNT_NOT_PERMISSION_ON_WEB(400, "Account not permission on web"),
    FILE_NOT_EXISTED(400, "Avatar file not existed"),

    //    Department
    DEPARTMENT_NOT_EXISTED(400, "Department not existed"),
    //    Classes
    CLASSES_NOT_EXISTED(400, "Classes not existed"),
    IO_EXCEPTION(400, "IO Exception"),

    INCORRECT_CAPTCHA(400, "Incorrect captcha"),

    KEYS_NOT_EXISTED(400, "Keys not existed");
    private final Integer code;
    private final String message;

    BadRequestError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
