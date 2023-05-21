package com.sang.nv.education.notification.infrastructure.persistence.repository;

import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationEntity;
import com.sang.nv.education.notification.infrastructure.persistence.repository.custom.NotificationEntityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, String>, NotificationEntityRepositoryCustom {
    @Query("from NotificationEntity e where e.deleted = false and e.id = :id ")
    Optional<NotificationEntity> findById(String id);

    @Query("from NotificationEntity e where e.deleted = false and e.isSend = true and e.id in :ids and e.targetId = :targetId")
    List<NotificationEntity> findAllByIdsAndUserId(List<String> ids, String targetId);

    @Query("from NotificationEntity e where e.deleted = false and e.isSend = true and e.id = :id and e.targetId = :targetId")
    Optional<NotificationEntity> findByIdAndUserId(String id, String targetId);

    @Query(" SELECT COUNT (e) from NotificationEntity e where e.deleted = false and e.isSend = true and e.isRead = false " +
            "and e.targetId = :targetId")
    Long countUnreadNotification(String targetId);

    @Query("from NotificationEntity e where e.deleted = false and e.isSend = true and e.isRead = false and e.targetId = :targetId")
    List<NotificationEntity> findAllUnreadNotiByUserId(String targetId);

}

