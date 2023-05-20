package com.sang.nv.education.notification.domain.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCmd {
    private String id;
    private String fileName;
    private String originFileName;
    private String filePath;
}
