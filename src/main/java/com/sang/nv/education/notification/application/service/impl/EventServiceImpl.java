package com.sang.nv.education.notification.application.service.impl;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.Task;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.notification.application.dto.request.IssueEventRequest;
import com.sang.nv.education.notification.application.mapper.NotificationAutoMapper;
import com.sang.nv.education.notification.application.service.EventService;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.command.EventCmd;
import com.sang.nv.education.notification.domain.repository.EventDomainRepository;
import com.sang.nv.education.notification.infrastructure.support.BadRequestError;
import com.sang.nv.education.notification.infrastructure.support.enums.EventStatus;
import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sang.nv.education.notification.infrastructure.support.util.JobConst.SEND_DELAY;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final NotificationAutoMapper notificationAutoMapper;
    private final UserService userService;
    private final EventDomainRepository eventDomainRepository;
    private final Scheduler scheduler;
    private final Task<String> manuallySendEventTask;

    @Override
    public Event issued(IssueEventRequest request) {
        EventCmd cmd = this.notificationAutoMapper.from(request);
        List<User> users = this.userService.findByIds(request.getTargetIds());
        // Default type notification
        if (Objects.isNull(cmd.getEventType())) {
            cmd.setEventType(EventType.NOTIFICATION);
        }
        Event event = new Event(cmd, users);
        this.eventDomainRepository.save(event);
        return event;
    }

    @Override
    public void send(String id) {
        Event event = this.eventDomainRepository.getById(id);
        if (!Objects.equals(event.getStatus(), EventStatus.PENDING)) {
            throw new ResponseException(BadRequestError.EVENT_CAN_NOT_CHANGE);
        }

        // Gửi lại
        if (EventStatus.FAILED.equals(event.getStatus())) {
            // Gui lai
            event.retrySent();
            this.eventDomainRepository.save(event);
            scheduler.schedule(this.manuallySendEventTask.instance(UUID.randomUUID().toString(), event.getId()),
                    Instant.now().plusSeconds(SEND_DELAY));
            log.info("Send event {}, create job", id);
            return;
        }

        // Gui lan dau
        scheduler.schedule(this.manuallySendEventTask.instance(UUID.randomUUID().toString(), event.getId()),
                Instant.now().plusSeconds(SEND_DELAY));
        log.info("Send event {}, create job", id);
    }


}
