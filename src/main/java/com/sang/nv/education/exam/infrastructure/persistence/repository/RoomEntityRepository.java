package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.RoomEntity;
import com.sang.nv.education.exam.infrastructure.persistence.repository.custom.RoomEntityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomEntityRepository extends JpaRepository<RoomEntity, String>, RoomEntityRepositoryCustom {
    @Query("from RoomEntity u where u.deleted = false and ( :keyword is null or ( u.name like :keyword))")
    Page<RoomEntity> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("from RoomEntity u where u.deleted = false and u.id = :id")
    Optional<RoomEntity> findById(@Param("id") String id);

}
