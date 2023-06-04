package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamReviewFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamReviewFileEntityRepository extends JpaRepository<ExamReviewFileEntity, String> {
    @Query("from ExamReviewFileEntity u where u.examReviewId = :id and u.deleted = false ")
    List<ExamReviewFileEntity> findByExamReviewId(String id);
    @Query("from ExamReviewFileEntity u where u.examReviewId in :examReviewIds and u.deleted = false ")
    List<ExamReviewFileEntity> findByExamReviewIds(List<String> examReviewIds);
}
