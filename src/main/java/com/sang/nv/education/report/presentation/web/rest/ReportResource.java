package com.sang.nv.education.report.presentation.web.rest;


import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.report.application.dto.request.NumberUserAndPeriodReportRequest;
import com.sang.nv.education.report.application.dto.request.ReportGeneralRequest;
import com.sang.nv.education.report.application.dto.request.UserExamReportRequest;
import com.sang.nv.education.report.domain.GeneralReport;
import com.sang.nv.education.report.domain.NumberUserAndPeriod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Api(tags = "Report Resource")
@RequestMapping("/api")
public interface ReportResource {
    // Thống kê số lượng phòng thi, thí sinh, ki thi
    @ApiOperation(value = "Report general")
    @GetMapping("/report/general")
    Response<GeneralReport> generalReport(ReportGeneralRequest request);

    // Thống kê số kì thi được tạo theo từng tháng
    // Thống kê người dùng: Số tài khoản quản lý, sinh viên được tạo theo từng tháng
    @ApiOperation(value = "Report number user")
    @GetMapping("/report/number-users")
    Response<List<NumberUserAndPeriod>> numberUserAndPeriodReport(NumberUserAndPeriodReportRequest request);

    // Thong ke ket qua thi
    @ApiOperation(value = "Report user exam")
    @GetMapping("/report/user-exams")
    Response<List<UserExamResult>> userExamReport(UserExamReportRequest request);
}
