package com.sang.nv.education.notification.infrastructure.support.enums;


import com.sang.commonmodel.error.ResponseError;

public enum NotFoundError implements ResponseError {

    USER_NOT_FOUND(40401001, "User not found: {0}"),
    NOTIFICATION_NOT_FOUND(40401001, "Notification not found: {0}"),
    ;

    private final Integer code;
    private final String message;

    NotFoundError(Integer code, String message) {
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
        return 404;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
