package com.sang.nv.education.iam.infrastructure.support.util;

public class Const {

    public static final String COL_USER_CODE_NOT_EMPTY = "Mã người dùng không được để trống";
    public static final String COL_EMAIL_EXISTED = "Email đã tồn tại";
    public static final String COL_EMAIL_NOT_EMPTY = "Email không được để trống";
    public static final String COL_PHONE_NOT_EMPTY = "SDT không được để trống";
    public static final String TYPE_USER_NOT_EMPTY = "Loại tài khoản không được để trống";
    public static final String COL_EMAIL_NOT_FORMAT = "Email không đúng định dạng";
    public static final String COL_PHONE_NOT_FORMAT = "SDT không đúng định dạng";
    public static final String USER_ADMIN = "admin";
    public static final String USER_STUDENT = "student";
    public static final String CLASS_NOT_FOUND = "Không tìm thấy lớp học";
    public static final String INVALID = "Không hợp lệ";
    public static final String DEFAULT_DOMAIN = "";
    public static final String COL_DATE_OF_BIRTH_NOT_FORMAT = "Ngày sinh không đúng định dạng";


    private Const() {
        throw new IllegalStateException("Utility class");
    }
}
