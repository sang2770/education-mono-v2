package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.PeriodRoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRoomEntityRepository extends JpaRepository<PeriodRoomEntity, String> {
    @Query("from PeriodRoomEntity u where u.deleted = false and u.roomId = :roomId and u.periodId = :periodId")
    Optional<PeriodRoomEntity> findByRoomIdAndPeriodId(@Param("roomId") String roomId, @Param("periodId") String periodId);

    @Query("from PeriodRoomEntity u where u.deleted = false and u.roomId = :roomId ")
    List<PeriodRoomEntity> findAllByRoomIds(@Param("roomId") String roomId);

    @Query("from PeriodRoomEntity u where u.deleted = false and u.roomId in :roomIds ")
    List<PeriodRoomEntity> findAllByRoomIds(@Param("roomIds") List<String> roomIds);

    @Query("select u from PeriodRoomEntity u join PeriodEntity p on u.periodId = p.id where u.deleted = false and u.roomId = :roomId and ( :periodIds is null or ( u.periodId in :periodIds)) and (:keyword is null or (p.name like :keyword)) ")
    Page<PeriodRoomEntity> searchByRoomId(@Param("roomId") String roomId, @Param("periodIds") List<String> periodIds, @Param("keyword") String keyword, Pageable pageable);

    @Query("select COUNT(DISTINCT u.periodId) from PeriodRoomEntity u where u.deleted = false and u.roomId in :roomIds ")
    Integer countAllByRoomIds(@Param("roomIds") List<String> roomIds);
}

