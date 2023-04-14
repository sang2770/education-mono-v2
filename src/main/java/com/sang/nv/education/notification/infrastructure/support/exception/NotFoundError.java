package com.sang.nv.education.notification.infrastructure.support.exception;


import com.sang.commonmodel.error.ResponseError;

public enum NotFoundError implements ResponseError {
    BUILDING_CODE_NOT_FOUND(40404001, "Building not found: {0}"),
    NOTIFICATION_NOT_FOUND(40404002, "Notification not found: {0}"),
    USER_ID_IS_NULL(40404003, "User id is null: {0}"),
    EVENT_NOT_FOUND(40404004, "Event not found: {0}"),
    ROLE_NOT_FOUND(40404005, "Role not found"),
    EVEN_CONFIGURATION_NOT_FOUND(40404005, "Role not found"),
    EMAIL_NOT_FOUND(40404006, "Email not found");

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
