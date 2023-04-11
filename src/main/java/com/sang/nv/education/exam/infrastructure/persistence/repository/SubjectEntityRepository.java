package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectEntityRepository extends JpaRepository<SubjectEntity, String>{
    @Query("from SubjectEntity u where u.deleted = false and ( :keyword is null or ( u.name like :keyword))")
    Page<SubjectEntity> search(@Param("keyword") String keyword, Pageable pageable);
}
