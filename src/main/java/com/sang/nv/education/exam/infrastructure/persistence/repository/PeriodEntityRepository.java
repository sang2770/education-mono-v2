package com.sang.nv.education.exam.infrastructure.persistence.repository;


import com.sang.nv.education.exam.infrastructure.persistence.entity.PeriodEntity;
import com.sang.nv.education.exam.infrastructure.persistence.readmodel.StatisticPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodEntityRepository extends JpaRepository<PeriodEntity, String> {

    @Query("from PeriodEntity u where u.deleted = false and ( :keyword is null or ( u.name like :keyword))")
    Page<PeriodEntity> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("from PeriodEntity u where u.id in :ids")
    List<PeriodEntity> findAllByIds(@Param("ids") List<String> ids);

    @Query("from PeriodEntity u where u.id = :id")
    Optional<PeriodEntity> findById(@Param("id") String id);

    @Query("select count( distinct u.id) from PeriodEntity u left join PeriodRoomEntity p on u.id = p.periodId  where u.deleted = false and p.deleted = false and ( (:roomIds) is null or p.roomId in :roomIds)")
    int countAll(@Param("roomIds") List<String> roomIds);

    @Query("select new com.sang.nv.education.exam.infrastructure.persistence.readmodel.StatisticPeriod(YEAR(p.createdAt), MONTH(p.createdAt), COUNT(*)) " +
            "from PeriodEntity p where YEAR(p.createdAt) = :year group by YEAR(p.createdAt), MONTH(p.createdAt)")
    List<StatisticPeriod> statisticPeriod(@Param("year") Integer year);

    @Query("select u from PeriodEntity u left join PeriodRoomEntity pu on u.id = pu.periodId where u.deleted = false and ((:roomIds) is null or pu.roomId in :roomIds) ")
    List<PeriodEntity> findAllByRoomIds(@Param("roomIds") List<String> roomIds);

}
