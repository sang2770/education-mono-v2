package com.sang.nv.education.notification.infrastructure.persistence.repository;

import com.sang.commonpersistence.repository.custom.BaseRepositoryCustom;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventEntityRepository extends JpaRepository<EventEntity, String>{
    @Query("from EventEntity e where e.deleted = false and e.id = :id ")
    Optional<EventEntity> findById(String id);

}
