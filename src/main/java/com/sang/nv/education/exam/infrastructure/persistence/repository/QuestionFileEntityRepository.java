package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionFileEntityRepository extends JpaRepository<QuestionFileEntity, String> {
    @Query("from QuestionFileEntity u where u.questionId in :ids and u.deleted = false ")
    List<QuestionFileEntity> findAllByQuestionIds(@Param("ids") List<String> ids);
}
