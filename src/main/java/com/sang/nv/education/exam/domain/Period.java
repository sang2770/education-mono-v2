package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.exam.domain.command.PeriodCreateOrUpdateCmd;
import com.sang.nv.education.exam.infrastructure.persistence.entity.PeriodEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Period extends AuditableDomain {
    String id;
    String name;
    String code;
    LocalDate startDate;
    LocalDate endDate;
    Boolean deleted;

    public Period(PeriodCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.startDate = cmd.getStartDate();
        this.endDate = cmd.getEndDate();
        this.code = cmd.getCode();
        this.deleted = Boolean.FALSE;
    }

    public Period(PeriodEntity cmd) {
        this.id = cmd.getId();
        this.name = cmd.getName();
        this.startDate = cmd.getStartDate();
        this.endDate = cmd.getEndDate();
        this.code = cmd.getCode();
        this.deleted = Boolean.FALSE;
    }

    public void update(PeriodCreateOrUpdateCmd cmd) {
        this.name = cmd.getName();
        this.startDate = cmd.getStartDate();
        this.endDate = cmd.getEndDate();
        this.deleted = Boolean.FALSE;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
