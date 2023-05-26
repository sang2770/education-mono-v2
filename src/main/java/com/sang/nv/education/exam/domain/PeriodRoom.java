package com.sang.nv.education.exam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.DataUtil;
import com.sang.commonutil.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class PeriodRoom extends AuditableDomain {
    String id;
    String roomId;
    String periodId;
    Boolean deleted;
    Period period;
    Boolean isSendExam;
    Boolean isDone;
    Instant startSendAt;
    Instant endSendAt;
    Long time;
    Long timeDelay;

    public PeriodRoom(String periodId, String roomId) {
        this.id = IdUtils.nextId();
        this.periodId = periodId;
        this.roomId = roomId;
        this.isSendExam = false;
        this.isDone = false;
        this.deleted = false;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichPeriod(Period period) {
        this.period = period;
    }

    public void updateIsSendExam(Boolean isSendExam, Long time, Long timeDelay) {
        this.isSendExam = isSendExam;
        this.time = DataUtil.getValueOrDefault(time, 0L);
        this.timeDelay = timeDelay;
        if (isSendExam) {
            this.startSendAt = Instant.now();
        }
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
        if (isDone) {
            this.endSendAt = Instant.now();
        }
    }
}
