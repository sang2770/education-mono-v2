package com.sang.nv.education.notification.infrastructure.persistence.repository;

import com.sang.nv.education.notification.infrastructure.persistence.entity.EventTargetEntity;
import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationTemplateEntity;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationTemplateEntityRepository extends JpaRepository<NotificationTemplateEntity, String> {
    @Query("from NotificationTemplateEntity e where e.deleted = false and e.code = :notificationCode ")
    Optional<NotificationTemplateEntity> findById(NotificationCode notificationCode);
}

