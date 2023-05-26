package com.sang.nv.education.report.application.dto.request;

import com.sang.commonmodel.dto.request.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ReportGeneralRequest extends Request {
    List<String> roomIds;
    List<String> userIds;
    Boolean isClient;
}
