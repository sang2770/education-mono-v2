package com.sang.nv.education.exam.application.dto.request;

import com.sang.commonmodel.dto.request.BaseSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomSearchRequest extends BaseSearchRequest {
    List<String> subjectIds;
    List<String> userIds;
    List<String> ids;
}
