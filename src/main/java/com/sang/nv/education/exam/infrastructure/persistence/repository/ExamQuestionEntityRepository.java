package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamQuestionEntityRepository extends JpaRepository<ExamQuestionEntity, String> {
    @Query("from ExamQuestionEntity u where u.deleted = false and u.examId = :examId ")
    List<ExamQuestionEntity> findByExamId(@Param("examId") String examId);

    @Query("from ExamQuestionEntity u where u.deleted = false and u.questionId in :questionIds ")
    List<ExamQuestionEntity> findAllByQuestionIds(@Param("questionIds") List<String> questionIds);

    @Query("from ExamQuestionEntity u where u.deleted = false and u.examId = :examId and u.questionId = :questionId ")
    Optional<ExamQuestionEntity> findByExamIdAndQuestionId(@Param("examId") String examId, @Param("questionId") String questionId);
}
