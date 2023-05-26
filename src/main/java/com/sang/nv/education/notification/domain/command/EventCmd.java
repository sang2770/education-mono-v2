package com.sang.nv.education.notification.domain.command;

import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCmd {
    private String title;
    private String content;
    private EventType eventType;
    private String attachedLink;

}
