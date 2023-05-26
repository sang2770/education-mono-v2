package com.sang.nv.education.notification.infrastructure.persistence.repository;

import com.sang.nv.education.notification.infrastructure.persistence.entity.EventTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventTargetEntityRepository extends JpaRepository<EventTargetEntity, String>{
    @Query("from EventTargetEntity e where e.deleted = false and e.id = :id ")
    Optional<EventTargetEntity> findById(String id);

    @Query("from EventTargetEntity e where e.deleted = false and e.eventId = :eventId")
    List<EventTargetEntity> findAllByEventId(@Param("eventId") String eventId);
}
