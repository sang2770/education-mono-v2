package com.sang.nv.education.exam.infrastructure.support.exception;


import com.sang.commonmodel.error.ResponseError;

public enum NotFoundError implements ResponseError {
    USER_NOT_FOUND(404, "Không tìm thấy người dùng"),
    ROOM_NOT_FOUND(404, "Không tìm thấy phòng thi"),
    SUBJECT_NOT_EXISTED(404, "SUBJECT_NOT_EXISTED"),
    QUESTION_NOT_EXISTED(404, "QUESTION_NOT_EXISTED"),
    EXAM_NOT_EXISTED(404, "EXAM_NOT_EXISTED"),
    ROOM_NOT_EXISTED(404, "ROOM_NOT_EXISTED"),
    USER_EXAM_NOT_EXISTED(404, "ROOM_NOT_EXISTED"),
    ANSWER_NOT_EXISTED(404, "ANSWER_NOT_EXISTED"),
    PERIOD_NOT_EXISTED_IN_ROOM(404, "PERIOD_NOT_EXISTED_IN_ROOM"),
    USER_EXAM_NOT_FOUND(404, "USER_EXAM_NOT_FOUND"),
    GROUP_NOT_EXISTED(404, "GROUP_NOT_EXISTED");

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
