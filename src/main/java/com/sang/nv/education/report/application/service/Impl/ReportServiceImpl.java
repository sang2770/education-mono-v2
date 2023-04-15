package com.sang.nv.education.report.application.service.Impl;

import com.sang.commonmodel.enums.UserType;
import com.sang.commonutil.DateUtils;
import com.sang.commonutil.ReportingPeriodType;
import com.sang.nv.education.exam.application.service.ExamService;
import com.sang.nv.education.exam.application.service.PeriodService;
import com.sang.nv.education.exam.application.service.RoomService;
import com.sang.nv.education.exam.application.service.UserExamService;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.infrastructure.persistence.readmodel.StatisticPeriod;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.infrastructure.persistence.repository.readmodel.StatisticUser;
import com.sang.nv.education.report.application.dto.request.NumberUserAndPeriodReportRequest;
import com.sang.nv.education.report.application.dto.request.ReportGeneralRequest;
import com.sang.nv.education.report.application.mapper.ReportAutoMapper;
import com.sang.nv.education.report.application.service.ReportService;
import com.sang.nv.education.report.domain.GeneralReport;
import com.sang.nv.education.report.domain.NumberUserAndPeriod;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final RoomService roomService;
    private final UserService userService;
    private final ExamService examService;
    private final PeriodService periodService;
    private final UserExamService userExamService;
    private final ReportAutoMapper reportAutoMapper;

    @Override
    public GeneralReport generalReport(ReportGeneralRequest request) {
        List<Room> rooms = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getRoomIds())) {
            rooms.addAll(roomService.getByIds(request.getRoomIds()));
        } else {
            rooms.addAll(roomService.getAll());
        }
        return GeneralReport.builder()
                .numberRoom(rooms.size())
                .numberPeriod(this.periodService.count(request.getRoomIds()))
                .numberExam(this.examService.countExam(request.getRoomIds()))
                .numberUser(this.userService.countUser(request.getRoomIds()))
                .build();
    }

    @Override
    public List<NumberUserAndPeriod> numberUserAndPeriodReport(NumberUserAndPeriodReportRequest request) {
        List<Month> months = DateUtils.getMonthByPeriod(ReportingPeriodType.YEAR);
        List<StatisticPeriod> statisticPeriods = this.periodService.statisticPeriod(request.getYear());
        List<StatisticUser> statisticUsers = this.userService.statisticsUser(request.getYear());
        List<NumberUserAndPeriod> numberUserAndPeriods = new ArrayList<>();
        months.stream().forEach(month -> {
            NumberUserAndPeriod numberUserAndPeriod = new NumberUserAndPeriod();
            Optional<StatisticUser> statisticUserAdmin = statisticUsers.stream().filter(item -> Objects.equals(item.getMonth(), month.getValue()) && Objects.equals(item.getUserType(),  UserType.MANAGER)).findFirst();
            if (statisticUserAdmin.isPresent()) {
                numberUserAndPeriod.setNumberUserAdmin(statisticUserAdmin.get().getNumberUser());
            }
            Optional<StatisticUser> statisticUserClient = statisticUsers.stream().filter(item -> Objects.equals(item.getMonth(), month.getValue()) && Objects.equals(item.getUserType(), UserType.STUDENT)).findFirst();
            if (statisticUserClient.isPresent()) {
                numberUserAndPeriod.setNumberUserAdmin(statisticUserClient.get().getNumberUser());
            }
            Optional<StatisticPeriod> statisticPeriod = statisticPeriods.stream().filter(item -> Objects.equals(item.getMonth(), month.getValue())).findFirst();
            if (statisticPeriod.isPresent()) {
                numberUserAndPeriod.setNumberPeriod(statisticPeriod.get().getNumberPeriod());
            }
            numberUserAndPeriod.setMonth(month.getValue());
            numberUserAndPeriods.add(numberUserAndPeriod);
        });
        return numberUserAndPeriods;
    }


}
