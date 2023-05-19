package com.sang.nv.education.exam.infrastructure.support.exception;


import com.sang.commonmodel.error.ResponseError;
import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    BAD_GROUP_REQUEST(400, "BAD_GROUP_REQUEST"),
    NUMBER_QUESTION_INVALID(400, "Số lượng câu hỏi không đủ"),
    QUESTION_IS_IN_EXAM(400, "QUESTION_IS_IN_EXAM"),
    QUESTION_IS_NOT_IN_EXAM(400, "QUESTION_IS_NOT_IN_EXAM"),
    USER_IS_NOT_IN_GROUP(400, "USER_IS_NOT_IN_GROUP"),
    EXAM_IS_NOT_IN_GROUP(400, "EXAM_IS_NOT_IN_GROUP"),
    PERIOD_NOT_EXISTED(400, "Period not existed"),
    ROOM_NOT_EXISTED(400, "Room not existed"),
    SUBJECT_NOT_EXISTED(400, "Subject not existed"),
    USER_EXAM_FINISHED(400, "User exam finished"),
    PERIOD_NOT_EXAM(400, "PERIOD NOT EXAM"),

    USER_INVALID(400, "User invalid"),

    PERMISSION_DENY(400, "Permission deny"),
    EXAM_IS_STARTED(400, "Exam is started"),
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

    @Override
    public Integer getCode() {
        return code;
    }
}
