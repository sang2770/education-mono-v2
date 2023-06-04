package com.sang.nv.education.exam.infrastructure.support.exception;


import com.sang.commonmodel.error.ResponseError;
import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    BAD_GROUP_REQUEST(400, "BAD_GROUP_REQUEST"),
    NUMBER_QUESTION_INVALID(400, "Số lượng câu hỏi dễ không đủ"),
    NUMBER_QUESTION_MEDIUM_INVALID(400, "Số lượng câu hỏi trung bình không đủ"),
    NUMBER_QUESTION_HIGH_INVALID(400, "Số lượng câu hỏi khó không đủ"),
    QUESTION_IS_IN_EXAM(400, "Câu hỏi đã tồn tại trong đề thi"),
    QUESTION_IS_NOT_IN_EXAM(400, "Câu hỏi không tồn tại trong đề thi"),
    USER_IS_NOT_IN_GROUP(400, "Người dùng không tồn tại trong nhóm"),
    EXAM_IS_NOT_IN_GROUP(400, "Đề thi không tồn tại trong nhóm"),
    PERIOD_NOT_EXISTED(400, "Kỳ thi không tồn tại"),
    ROOM_NOT_EXISTED(400, "Phòng thi không tồn tại"),
    SUBJECT_NOT_EXISTED(400, "Môn thi không tồn tại"),
    USER_EXAM_FINISHED(400, "Bài thi đã được kết thúc"),
    PERIOD_NOT_EXAM(400, "Kỳ thi không ứng với bài thi"),

    USER_INVALID(400, "User invalid"),

    PERMISSION_DENY(400, "Không có quyền truy cập"),
    EXAM_IS_STARTED(400, "Bài thi đã được bắt đầu"),
    EXAM_REVIEW_EXISTED(400, "Tồn tại bản phúc khảo chưa được xử lý"),
    EXAM_REVIEW_MUST_BE_NEW(400, "Bản phúc khảo phải ở trạng thái chờ tiếp nhận"),
    EXAM_REVIEW_MUST_BE_RECEIVED(400, "Bản phúc khảo phải ở trạng thái đã tiếp nhận");
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
