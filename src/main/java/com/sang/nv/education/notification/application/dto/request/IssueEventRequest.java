package com.sang.nv.education.notification.application.dto.request;

import com.sang.commonmodel.dto.request.Request;
import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueEventRequest extends Request{
    private String title;
    private String content;
    private EventType eventType;
    private String attachedLink;
    private List<String> targetIds;
}
