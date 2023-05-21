package com.sang.nv.education.notification.application.runner;

import com.sang.commonutil.IdUtils;
import com.sang.nv.education.notification.domain.NotificationTemplate;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.NotificationTemplateEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.repository.NotificationTemplateEntityRepository;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationTemplateResource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NotificationSeedDatabaseRunner implements CommandLineRunner {
    private final NotificationTemplateEntityRepository notificationTemplateEntityRepository;
    private final NotificationTemplateEntityMapper notificationTemplateEntityMapper;
    @Override
    public void run(String... args) throws Exception {
        this.initData();
    }

    @Transactional
    public void initData() {
        NotificationTemplateResource[] resourceCategories = NotificationTemplateResource.values();
        List<NotificationTemplate> notificationTemplateList = new ArrayList<>();
        List<NotificationTemplate> notificationTemplateExisted = this.notificationTemplateEntityMapper.toDomain(this.notificationTemplateEntityRepository.findAll());
        for (NotificationTemplateResource resourceCategory : resourceCategories) {
            Optional<NotificationTemplate> notificationTemplateOptional = notificationTemplateExisted.stream().filter(notificationTemplate -> notificationTemplate.getCode().equals(resourceCategory.getCode())).findFirst();
            if (notificationTemplateOptional.isPresent())
            {
                continue;
            }
            NotificationTemplate notificationTemplate = NotificationTemplate.builder()
                    .code(resourceCategory.getCode())
                    .title(resourceCategory.getTitle())
                    .content(resourceCategory.getContent())
                    .url(resourceCategory.getUrl())
                    .id(IdUtils.nextId())
                    .build();

            notificationTemplateList.add(notificationTemplate);
        }
        this.notificationTemplateEntityRepository.saveAll(this.notificationTemplateEntityMapper.toEntity(notificationTemplateList));
    }
}
