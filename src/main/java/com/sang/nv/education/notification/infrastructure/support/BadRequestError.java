package com.sang.nv.education.notification.infrastructure.support;

import com.sang.commonmodel.error.ResponseError;
import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    EVENT_NOT_FOUND(404, "Event not found!"),
    EVENT_CAN_NOT_CHANGE(400, "Event can not change!"),
    ;

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
}
