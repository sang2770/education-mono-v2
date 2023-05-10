package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectEntityRepository extends JpaRepository<SubjectEntity, String> {
    @Query("from SubjectEntity u where u.deleted = false and ( :keyword is null or ( u.name like :keyword))")
    Page<SubjectEntity> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("from SubjectEntity u where u.id in :ids")
    List<SubjectEntity> findAllByIds(@Param("ids") List<String> ids);

    @Query("from SubjectEntity u where u.code = :code and u.deleted = false")
    Optional<SubjectEntity> findByCode(@Param("code") String code);
}
