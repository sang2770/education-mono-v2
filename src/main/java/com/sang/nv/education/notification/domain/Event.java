package com.sang.nv.education.notification.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.nv.education.common.web.support.SecurityUtils;
import com.sang.nv.education.notification.domain.command.EventCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Event extends AuditableDomain {
    private String id;
    private String fileName;
    private String filePath;
    private String originFileName;
    private String owner;
    private String ownerId;
    private Boolean deleted;

    public Event(EventCmd cmd) {
        this.id = UUID.randomUUID().toString();
//        File name firebase
        this.fileName = cmd.getFileName();
//
        this.filePath = cmd.getFilePath();
        this.originFileName = cmd.getOriginFileName();
        this.owner = SecurityUtils.authentication().getName();
        this.ownerId = SecurityUtils.authentication().getUserId();
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
