package com.sang.nv.education.report.application.dto.request;

import com.sang.commonmodel.dto.request.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserExamReportRequest extends Request {
    List<String> userIds;
    Instant fromDate;
    Instant toDate;
}
