package com.sang.nv.education.report.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class GeneralReport extends AuditableDomain {
    private Integer numberUser;
    private Integer numberPeriod;
    private Integer numberExam;
    private Integer numberRoom;
}
