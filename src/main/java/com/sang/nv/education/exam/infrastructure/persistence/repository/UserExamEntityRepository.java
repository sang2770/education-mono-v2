package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.UserExamEntity;
import com.sang.nv.education.exam.infrastructure.support.enums.UserExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserExamEntityRepository extends JpaRepository<UserExamEntity, String> {
    @Query("from UserExamEntity u where u.deleted = false and u.examId = :examId and u.periodId = :periodId")
    Optional<UserExamEntity> findByExamId(@Param("examId") String examId, @Param("periodId") String periodId);

    @Query("from UserExamEntity u where u.deleted = false and u.roomId = :roomId and u.periodId = :periodId")
    List<UserExamEntity> findByRoomAndPeriod(@Param("roomId") String roomId, @Param("periodId") String periodId);

    @Query("from UserExamEntity u where u.deleted = false and u.examId in :examIds")
    List<UserExamEntity> findAllByExamId(@Param("examIds") List<String> examId);

    @Query("from UserExamEntity u where u.deleted = false and u.userId = :userId and u.examId = :examId and u.periodId = :periodId")
    Optional<UserExamEntity> findByPeriod(@Param("userId") String userId, @Param("examId") String examId, @Param("periodId") String periodId);

    @Query("from UserExamEntity u where u.deleted = false and u.id = :id")
    Optional<UserExamEntity> findById(@Param("id") String id);

    @Query("from UserExamEntity u where u.deleted = false and u.roomId = :roomId and u.periodId = :periodId and (:keyword is null or u.code like :keyword )")
    Page<UserExamEntity> searchUserExam(@Param("roomId") String roomId, @Param("periodId") String periodId, String keyword, Pageable pageable);

    @Query("from UserExamEntity u where u.deleted = false and u.roomId = :roomId and u.periodId = :periodId and u.userId in :userIds")
    Page<UserExamEntity> getMyExam(@Param("roomId") String roomId, @Param("periodId") String periodId, @Param("userIds") List<String> userIds, Pageable pageable);

    @Query("from UserExamEntity u where u.deleted = false and u.status in :statuses " +
            "and (:userIds is null or u.userId in :userIds) " +
            "and (cast(:fromDate as timestamp) is null or u.createdAt >= :fromDate) " +
            "and (cast(:fromDate as timestamp) is null or u.createdAt <= :toDate)")
    List<UserExamEntity> statisticResult(@Param("statuses") List<UserExamStatus> status,
                                         @Param("userIds") List<String> userIds,
                                         @Param("fromDate") Instant fromDate,
                                         @Param("toDate") Instant toDate);
}
