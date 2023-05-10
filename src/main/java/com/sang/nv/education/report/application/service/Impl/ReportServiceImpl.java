package com.sang.nv.education.report.application.service.Impl;

import com.sang.commonutil.DateUtils;
import com.sang.commonutil.ReportingPeriodType;
import com.sang.nv.education.exam.application.dto.request.room.RoomSearchRequest;
import com.sang.nv.education.exam.application.dto.response.UserExamResult;
import com.sang.nv.education.exam.application.service.ExamService;
import com.sang.nv.education.exam.application.service.PeriodService;
import com.sang.nv.education.exam.application.service.RoomService;
import com.sang.nv.education.exam.application.service.UserExamService;
import com.sang.nv.education.exam.domain.Room;
import com.sang.nv.education.exam.infrastructure.persistence.readmodel.StatisticPeriod;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.infrastructure.persistence.repository.readmodel.StatisticUser;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import com.sang.nv.education.report.application.dto.request.NumberUserAndPeriodReportRequest;
import com.sang.nv.education.report.application.dto.request.ReportGeneralRequest;
import com.sang.nv.education.report.application.dto.request.UserExamReportRequest;
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
    private final PeriodService periodService;
    private final ExamService examService;
    private final UserExamService userExamService;

    @Override
    public GeneralReport generalReport(ReportGeneralRequest request) {
        RoomSearchRequest roomSearchRequest = new RoomSearchRequest();
        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            roomSearchRequest.setUserIds(request.getUserIds());
        }
        if (!CollectionUtils.isEmpty(request.getRoomIds())) {
            roomSearchRequest.setIds(request.getRoomIds()
            );
        }
        List<Room> rooms = roomService.search(roomSearchRequest).getData();
//        List<String> roomIds = rooms.stream().map(Room::getId).collect(Collectors.toList());
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
        months.forEach(month -> {
            NumberUserAndPeriod numberUserAndPeriod = new NumberUserAndPeriod();
            Optional<StatisticUser> statisticUserAdmin = statisticUsers.stream().filter(item ->
                    Objects.equals(item.getMonth(), month.getValue()) &&
                            UserType.MANAGER.equals(item.getUserType())).findFirst();
            statisticUserAdmin.ifPresent(statisticUser -> numberUserAndPeriod.setNumberUserAdmin(statisticUser.getNumberUser()));
            Optional<StatisticUser> statisticUserClient = statisticUsers.stream().filter(item -> Objects.equals(item.getMonth(), month.getValue()) &&
                    UserType.STUDENT.equals(item.getUserType())).findFirst();
            statisticUserClient.ifPresent(statisticUser -> numberUserAndPeriod.setNumberUserClient(statisticUser.getNumberUser()));
            Optional<StatisticPeriod> statisticPeriod = statisticPeriods.stream().filter(item -> Objects.equals(item.getMonth(), month.getValue())).findFirst();
            statisticPeriod.ifPresent(period -> numberUserAndPeriod.setNumberPeriod(period.getNumberPeriod()));
            numberUserAndPeriod.setMonth(month.getValue());
            numberUserAndPeriods.add(numberUserAndPeriod);
        });
        return numberUserAndPeriods;
    }

    @Override
    public List<UserExamResult> userExamReport(UserExamReportRequest request) {
        return userExamService.statisticResult(request);
    }


}
