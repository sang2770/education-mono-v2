package com.sang.nv.education.notification.application.config;

import com.github.kagkarlsson.scheduler.task.DeadExecutionHandler;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.sang.nv.education.notification.application.service.SendService;
import com.sang.nv.education.notification.infrastructure.support.util.JobConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({SendService.class})
@Slf4j
public class NotificationTaskConfiguration {

    private final SendService sendService;

    public NotificationTaskConfiguration(SendService sendService) {
        this.sendService = sendService;
    }

    @Bean
    Task<String> manuallySendEventTask() {
        return Tasks.oneTime(JobConst.SEND_EVENT_TASK, String.class)
                .onDeadExecution(new DeadExecutionHandler.ReviveDeadExecution<>())   // re-run task which was dead when shutdown server
                .execute(((taskInstance, executionContext) -> {
                    log.info("Start execution with task name: " + taskInstance.getTaskName()
                            + " with task instance: " + taskInstance.getTaskAndInstance());
                    String eventId = taskInstance.getData();
                    sendService.sendNotification(eventId);
                }));
    }
}
