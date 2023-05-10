package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.UserExamInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamInfoEntityRepository extends JpaRepository<UserExamInfoEntity, String> {
    @Query("from UserExamInfoEntity u where u.deleted = false and u.userExamId = :userExamId")
    List<UserExamInfoEntity> findAllByUserExamId(@Param("userExamId") String userExamId);
}
