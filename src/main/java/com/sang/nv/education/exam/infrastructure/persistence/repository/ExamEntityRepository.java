package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamEntity;
import com.sang.nv.education.exam.infrastructure.persistence.repository.custom.ExamEntityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamEntityRepository extends JpaRepository<ExamEntity, String>, ExamEntityRepositoryCustom {
    @Query("from ExamEntity u where u.deleted = false and u.periodId = :periodId and u.subjectId = :subjectId")
    List<ExamEntity> findAllByPeriodAnsSubject(@Param("periodId") String periodId, @Param("subjectId") String subjectId);

    @Query("from ExamEntity u where u.deleted = false and u.periodId in :periodIds and u.subjectId = :subjectId")
    Page<ExamEntity> searchByPeriods(@Param("periodIds") List<String> periodIds, @Param("subjectId") String subjectId, Pageable pageable);


    @Query("from ExamEntity u where u.deleted = false and u.id in :ids")
    List<ExamEntity> findAllByIds(@Param("ids") List<String> ids);

    @Query("Select count(u.periodId) from ExamEntity u where u.deleted = false and u.periodId in :periodIds")
    Integer countByPeriodIds(List<String> periodIds);
}
