package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamReviewEntity;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamReviewEntityRepository extends JpaRepository<ExamReviewEntity, String> {
    @Query("from ExamReviewEntity u where u.userExamId = :id and u.deleted = false and (:keyword is null or u.code like :keyword) ")
    List<ExamReviewEntity> findByUserExamId(@Param("id") String id, String keyword);
    @Query("select count(u.id) from ExamReviewEntity u where u.userExamId = :id and u.status != :status and u.deleted = false ")
    Long countById(String id, ExamReviewStatus status);

    @Query("from ExamReviewEntity u where u.periodId = :periodId and u.roomId = :roomId and u.deleted = false and (:keyword is null or u.code like :keyword)")
    List<ExamReviewEntity> findAllByPeriodRoom(String periodId, String roomId, String keyword);
}
