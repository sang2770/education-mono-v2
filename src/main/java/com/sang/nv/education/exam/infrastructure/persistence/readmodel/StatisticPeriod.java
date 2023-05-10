package com.sang.nv.education.exam.infrastructure.persistence.readmodel;

import lombok.Data;

@Data
public class StatisticPeriod {
    Integer numberPeriod;
    Integer month;
    Integer year;

    public StatisticPeriod(Integer year, Integer month, Long numberPeriod) {
        this.numberPeriod = numberPeriod.intValue();
        this.month = month;
        this.year = year;
    }
}
