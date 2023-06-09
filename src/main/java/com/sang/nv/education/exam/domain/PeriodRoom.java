package com.sang.nv.education.exam.domain;

import com.sang.commonutil.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class PeriodRoom {
    String id;
    String roomId;
    String periodId;
    Boolean deleted;
    Period period;
    Boolean isSendExam;

    public PeriodRoom(String periodId, String roomId)
    {
        this.id = IdUtils.nextId();
        this.periodId = periodId;
        this.roomId = roomId;
        this.isSendExam = false;
        this.deleted = false;
    }

    public void deleted(){
        this.deleted = true;
    }

    public void unDelete(){
        this.deleted = false;
    }
    public void enrichPeriod(Period period){
        this.period = period;
    }

    public void updateIsSendExam(Boolean isSendExam){
        this.isSendExam = isSendExam;
    }
}
