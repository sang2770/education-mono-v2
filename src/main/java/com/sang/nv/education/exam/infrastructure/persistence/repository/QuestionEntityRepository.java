package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.QuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.repository.custom.QuestionEntityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionEntityRepository extends JpaRepository<QuestionEntity, String>, QuestionEntityRepositoryCustom {
    @Query("from QuestionEntity u where u.deleted = false and ( :keyword is null or ( u.title like :keyword))")
    Page<QuestionEntity> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("from QuestionEntity u where  u.groupId = :groupId ")
    List<QuestionEntity> findByGroupId(@Param("groupId") String groupId);

    @Query("from QuestionEntity u where u.groupId in :groupIds ")
    List<QuestionEntity> findAllByGroupIds(@Param("groupIds") List<String> groupIds);

    @Query("from QuestionEntity u where u.id in :ids order by u.groupId ")
    List<QuestionEntity> findAllById(@Param("ids") List<String> ids);

    @Query(value = "FROM QuestionEntity where e.groupId = :groupId ORDER BY RAND() LIMIT :numberOfQuestions", nativeQuery = true)
    List<QuestionEntity> findRandomQuestions(@Param("groupId") String groupId, @Param("numberOfQuestions") int numberOfQuestions);
}
