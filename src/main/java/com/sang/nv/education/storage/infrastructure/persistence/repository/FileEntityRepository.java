package com.sang.nv.education.storage.infrastructure.persistence.repository;

import com.sang.commonpersistence.repository.custom.BaseRepositoryCustom;
import com.sang.nv.education.storage.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileEntityRepository extends JpaRepository<FileEntity, String>, BaseRepositoryCustom<FileEntity> {
    @Query("from FileEntity e where e.deleted = false and e.id = :id ")
    Optional<FileEntity> findById(String id);

    @Query("from FileEntity e where e.deleted = false and e.id in :ids ")
    List<FileEntity> findByIds(@Param("ids") List<String> ids);
}
